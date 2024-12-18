package TCOTS.entity.necrophages;

import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class BullvoreEntity extends NecrophageMonster implements GeoEntity {
    //xTODO: Add drops -> Something related to the G'valchir
    //xTODO: Add Loot table
    //xTODO: Add spawn (Like an illager patrol)
    //xTODO: Add bestiary entry

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final TrackedData<Boolean> CHARGING = DataTracker.registerData(BullvoreEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public BullvoreEntity(EntityType<? extends BullvoreEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints=20;
        this.setPathfindingPenalty(PathNodeType.LEAVES, 0.0f);
    }

    @Override

    public int getMaxHeadRotation() {
        return 62;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.0)

                .add(EntityAttributes.GENERIC_MAX_HEALTH, 80.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 14.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20f)

                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,1.5)

                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 24);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(CHARGING, Boolean.FALSE);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new Bullvore_MeleeAttackGoal(this, 1.2D, false, 1.6D, 100));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.75, 100));
        this.goalSelector.add(3, new LookAroundGoal(this));


        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, BullvoreEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, SheepEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    protected static class Bullvore_MeleeAttackGoal extends Goal {

        protected final BullvoreEntity mob;

        public final double speed;
        private final boolean pauseWhenMobIdle;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int updateCountdownTicks;
        private int cooldown;
        private long lastUpdateTime;
        private final double speedMultiplierRunValue;
        private final int chargeCooldownTicks;

        private Path pathCharge;
        private double toChargeX;
        private double toChargeY;
        private double toChargeZ;

        public Bullvore_MeleeAttackGoal(BullvoreEntity mob, double speed, boolean pauseWhenMobIdle, double speedMultiplier, int chargeCooldown) {
            this.mob = mob;
            this.speed = speed;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            this.speedMultiplierRunValue =speedMultiplier;
            this.chargeCooldownTicks=chargeCooldown;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        @Override
        public boolean canStart() {
            long l = this.mob.getWorld().getTime();
            if (l - this.lastUpdateTime < 20L) {
                return false;
            }
            this.lastUpdateTime = l;
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
            if (this.path != null) {
                return true;
            }
            return this.mob.isInAttackRange(livingEntity);
        }


        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (!this.pauseWhenMobIdle) {
                return !this.mob.getNavigation().isIdle();
            }
            if (!this.mob.isInWalkTargetRange(livingEntity.getBlockPos())) {
                return false;
            }
            return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
        }

        @Override
        public void start() {
            this.mob.getNavigation().startMovingAlong(this.path, this.speed);
            this.mob.setAttacking(true);
            this.updateCountdownTicks = 0;
            this.cooldown = 0;

            this.mob.chargeCooldownTimer=60;
            this.mob.chargeCooldown=true;
        }
        @Override
        public void stop() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.mob.setTarget(null);
            }
            this.mob.setAttacking(false);
            this.mob.getNavigation().stop();

            this.mob.setCharging(false);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return;
            }


            //Normal Attack
            if(!mob.isCharging()) {
                this.mob.getLookControl().lookAt(target, 30.0f, 30.0f);
                this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
                if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(target)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                    this.targetX = target.getX();
                    this.targetY = target.getY();
                    this.targetZ = target.getZ();
                    this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                    double d = this.mob.squaredDistanceTo(target);
                    if (d > 1024.0) {
                        this.updateCountdownTicks += 10;
                    } else if (d > 256.0) {
                        this.updateCountdownTicks += 5;
                    }
                    if (!this.mob.getNavigation().startMovingTo(target, this.speed)) {
                        this.updateCountdownTicks += 15;
                    }
                    this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
                }
                this.cooldown = Math.max(this.cooldown - 1, 0);
                this.attack(target);
            }


            // Get the monster's position and facing direction
            Vec3d monsterPosition = this.mob.getPos();
            Vec3d monsterLookVec = this.mob.getRotationVector(0.0f, mob.getHeadYaw()); // This gives the direction the monster is facing
            boolean isFacingTarget=false;
            //To only active when it's facing directly to the player
            {
                Vec3d targetPosition = target.getPos(); // Assuming 'target' is the player or another entity
                Vec3d directionToTarget = targetPosition.subtract(monsterPosition).normalize(); // Normalize the direction

                // Calculate the dot product between the two vectors
                double dotProduct = monsterLookVec.dotProduct(directionToTarget);

                // Set a threshold for the facing direction (1.0 means exactly the same direction)
                double threshold = 0.95; // Adjust this threshold as needed

                // Activate the boolean if the monster is facing towards the target
                if (dotProduct > threshold) {
                    isFacingTarget = true;
                }
            }

            //Start Charging
            if(this.mob.distanceTo(target) > 8 && !this.mob.chargeCooldown && !this.mob.isCharging() && isFacingTarget){

                this.mob.getLookControl().lookAt(
                        this.targetX,
                        this.targetY,
                        this.targetZ,
                        30.0f, 30.0f);

                // Define how far you want the monster to charge (distance in blocks)
                double chargeDistance = 20.0;
                // Calculate the target position in front of the monster
                Vec3d movingDirection = monsterPosition.add(monsterLookVec.multiply(chargeDistance, 0, chargeDistance));

                this.toChargeX=movingDirection.x;
                this.toChargeY=movingDirection.y;
                this.toChargeZ=movingDirection.z;

                this.pathCharge = this.mob.getNavigation().findPathTo(this.toChargeX, this.toChargeY, this.toChargeZ, 0);

                if(this.pathCharge==null){
                    return;
                }

                this.mob.setCharging(true);
                this.mob.getLookControl().lookAt(
                        this.toChargeX,
                        this.toChargeY-8,
                        this.toChargeZ,
                        30.0f, 30.0f);

                this.mob.playSound(TCOTS_Sounds.BULLVORE_CHARGE, 1.0f, 1.0f);
            }

            //While it's charging
            if(mob.isCharging()){
                this.mob.getLookControl().lookAt(
                        this.toChargeX,
                        this.toChargeY-8,
                        this.toChargeZ,
                        30.0f, 30.0f);

                this.mob.getNavigation().startMovingAlong(this.pathCharge,this.speed*speedMultiplierRunValue);


                //If Bullvore reach the coordinates or something blocks the path
                if(this.mob.squaredDistanceTo(this.toChargeX, this.toChargeY, this.toChargeZ) < 2 || this.mob.horizontalCollision){

                 this.mob.setCharging(false);

                 this.mob.chargeCooldownTimer=this.chargeCooldownTicks;
                 this.mob.chargeCooldown=true;
                }
            }

        }

        protected void attack(LivingEntity target) {
            if (this.canAttack(target)) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);

                //Triggers the Animation
                int randomAttack = this.mob.getRandom().nextBetween(0, 2);
                if (randomAttack == 0) {
                    mob.triggerAnim("AttackController", "attack1");
                } else if (randomAttack == 1) {
                    mob.triggerAnim("AttackController", "attack2");
                } else {
                    mob.triggerAnim("AttackController", "attack3");
                }

                this.mob.tryAttack(target);
            }
        }

        protected void resetCooldown() {
            this.cooldown = this.getTickCount(20);
        }

        protected boolean isCooledDown() {
            return this.cooldown <= 0;
        }

        protected boolean canAttack(LivingEntity target) {
            return this.isCooledDown() && this.mob.isInAttackRange(target) && this.mob.getVisibilityCache().canSee(target);
        }

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, GeoControllersUtil::idleWalkRunController)
        );

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 2, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
                        .triggerableAnim("attack3", GeoControllersUtil.ATTACK3)
        );
    }

    //Explosion Damage Resistance
    @Override
    public boolean damage(DamageSource source, float amount) {
        if(source.isOf(DamageTypes.EXPLOSION) || source.isOf(DamageTypes.PLAYER_EXPLOSION)){
            amount=amount/12;
        }

        return super.damage(source, amount);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !isCharging();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.isAlive()) {
            return;
        }

        //Destructive charge
        if (this.horizontalCollision && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            boolean bl = false;
            Box box = this.getBoundingBox().expand(0.2);
            for (BlockPos blockPos : BlockPos.iterate(MathHelper.floor(box.minX), MathHelper.floor(box.minY), MathHelper.floor(box.minZ), MathHelper.floor(box.maxX), MathHelper.floor(box.maxY), MathHelper.floor(box.maxZ))) {
                BlockState blockState = this.getWorld().getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!(block instanceof LeavesBlock)) continue;
                bl = this.getWorld().breakBlock(blockPos, true, this) || bl;
            }
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);
        if(target instanceof PlayerEntity player && player.isBlocking()){
            player.disableShield();
        }
        return bl;
    }

    int chargeCooldownTimer;

    boolean chargeCooldown=false;


    @Override
    public void tick() {
        if(chargeCooldownTimer>0){
            --chargeCooldownTimer;
        } else if (chargeCooldown){
            chargeCooldown=false;
        }

        if(this.isCharging()){
            EntitiesUtil.spawnGroundParticles(this);
        }

        super.tick();
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        if(this.isCharging()){
            EntitiesUtil.pushAndDamageEntities(this, 14f, 1.1, 1.1, 1.5D, BullvoreEntity.class, RotfiendEntity.class);
        }
    }

    public boolean isCharging() {
        return this.dataTracker.get(CHARGING);
    }

    public void setCharging(boolean wasCharging) {
        this.dataTracker.set(CHARGING, wasCharging);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("ChargingCooldown", this.chargeCooldownTimer);
        nbt.putBoolean("Charging", isCharging());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.chargeCooldownTimer = nbt.getInt("ChargingCooldown");
        setCharging(nbt.getBoolean("Charging"));
    }



    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.BULLVORE_IDLE;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if(getStepSound()!=null){
            this.playSound(this.getStepSound(),  this.isCharging()? 0.5f: 0.15F, 1.0F);
        }
        else {
            BlockSoundGroup blockSoundGroup = state.getSoundGroup();
            this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch());
        }
    }

    @Override
    protected SoundEvent getStepSound() {
        if(this.isCharging()){
            return SoundEvents.ENTITY_COW_STEP;
        } else {
        return SoundEvents.ENTITY_ZOGLIN_STEP;}
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.BULLVORE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.BULLVORE_DEATH;
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.BULLVORE_ATTACK;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

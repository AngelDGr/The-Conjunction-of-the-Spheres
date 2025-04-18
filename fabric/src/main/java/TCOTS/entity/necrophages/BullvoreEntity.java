package TCOTS.entity.necrophages;

import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
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

    protected static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(BullvoreEntity.class, EntityDataSerializers.BOOLEAN);

    public BullvoreEntity(EntityType<? extends BullvoreEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward=20;
        this.setPathfindingMalus(PathType.LEAVES, 0.0f);
    }

    @Override

    public int getMaxHeadYRot() {
        return 62;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.STEP_HEIGHT, 1.0)

                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.ATTACK_DAMAGE, 14.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.20f)

                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ATTACK_KNOCKBACK,1.5)

                .add(Attributes.FOLLOW_RANGE, 24);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGING, Boolean.FALSE);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new Bullvore_MeleeAttackGoal(this, 1.2D, false, 1.6D, 100));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.75, 100));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));


        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, BullvoreEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            long l = this.mob.level().getGameTime();
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
            this.path = this.mob.getNavigation().createPath(livingEntity, 0);
            if (this.path != null) {
                return true;
            }
            return this.mob.isWithinMeleeAttackRange(livingEntity);
        }


        @Override
        public boolean canContinueToUse() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (!this.pauseWhenMobIdle) {
                return !this.mob.getNavigation().isDone();
            }
            if (!this.mob.isWithinRestriction(livingEntity.blockPosition())) {
                return false;
            }
            return !(livingEntity instanceof Player) || !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speed);
            this.mob.setAggressive(true);
            this.updateCountdownTicks = 0;
            this.cooldown = 0;

            this.mob.chargeCooldownTimer=60;
            this.mob.chargeCooldown=true;
        }
        @Override
        public void stop() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.mob.setTarget(null);
            }
            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();

            this.mob.setCharging(false);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
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
                this.mob.getLookControl().setLookAt(target, 30.0f, 30.0f);
                this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
                if ((this.pauseWhenMobIdle || this.mob.getSensing().hasLineOfSight(target)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                    this.targetX = target.getX();
                    this.targetY = target.getY();
                    this.targetZ = target.getZ();
                    this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                    double d = this.mob.distanceToSqr(target);
                    if (d > 1024.0) {
                        this.updateCountdownTicks += 10;
                    } else if (d > 256.0) {
                        this.updateCountdownTicks += 5;
                    }
                    if (!this.mob.getNavigation().moveTo(target, this.speed)) {
                        this.updateCountdownTicks += 15;
                    }
                    this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
                }
                this.cooldown = Math.max(this.cooldown - 1, 0);
                this.attack(target);
            }


            // Get the monster's position and facing direction
            Vec3 monsterPosition = this.mob.position();
            Vec3 monsterLookVec = this.mob.calculateViewVector(0.0f, mob.getYHeadRot()); // This gives the direction the monster is facing
            boolean isFacingTarget=false;
            //To only active when it's facing directly to the player
            {
                Vec3 targetPosition = target.position(); // Assuming 'target' is the player or another entity
                Vec3 directionToTarget = targetPosition.subtract(monsterPosition).normalize(); // Normalize the direction

                // Calculate the dot product between the two vectors
                double dotProduct = monsterLookVec.dot(directionToTarget);

                // Set a threshold for the facing direction (1.0 means exactly the same direction)
                double threshold = 0.95; // Adjust this threshold as needed

                // Activate the boolean if the monster is facing towards the target
                if (dotProduct > threshold) {
                    isFacingTarget = true;
                }
            }

            //Start Charging
            if(this.mob.distanceTo(target) > 8 && !this.mob.chargeCooldown && !this.mob.isCharging() && isFacingTarget){

                this.mob.getLookControl().setLookAt(
                        this.targetX,
                        this.targetY,
                        this.targetZ,
                        30.0f, 30.0f);

                // Define how far you want the monster to charge (distance in blocks)
                double chargeDistance = 20.0;
                // Calculate the target position in front of the monster
                Vec3 movingDirection = monsterPosition.add(monsterLookVec.multiply(chargeDistance, 0, chargeDistance));

                this.toChargeX=movingDirection.x;
                this.toChargeY=movingDirection.y;
                this.toChargeZ=movingDirection.z;

                this.pathCharge = this.mob.getNavigation().createPath(this.toChargeX, this.toChargeY, this.toChargeZ, 0);

                if(this.pathCharge==null){
                    return;
                }

                this.mob.setCharging(true);
                this.mob.getLookControl().setLookAt(
                        this.toChargeX,
                        this.toChargeY-8,
                        this.toChargeZ,
                        30.0f, 30.0f);

                this.mob.playSound(TCOTS_Sounds.BULLVORE_CHARGE, 1.0f, 1.0f);
            }

            //While it's charging
            if(mob.isCharging()){
                this.mob.getLookControl().setLookAt(
                        this.toChargeX,
                        this.toChargeY-8,
                        this.toChargeZ,
                        30.0f, 30.0f);

                this.mob.getNavigation().moveTo(this.pathCharge,this.speed*speedMultiplierRunValue);


                //If Bullvore reach the coordinates or something blocks the path
                if(this.mob.distanceToSqr(this.toChargeX, this.toChargeY, this.toChargeZ) < 2 || this.mob.horizontalCollision){

                 this.mob.setCharging(false);

                 this.mob.chargeCooldownTimer=this.chargeCooldownTicks;
                 this.mob.chargeCooldown=true;
                }
            }

        }

        protected void attack(LivingEntity target) {
            if (this.canAttack(target)) {
                this.resetCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);

                //Triggers the Animation
                int randomAttack = this.mob.getRandom().nextIntBetweenInclusive(0, 2);
                if (randomAttack == 0) {
                    mob.triggerAnim("AttackController", "attack1");
                } else if (randomAttack == 1) {
                    mob.triggerAnim("AttackController", "attack2");
                } else {
                    mob.triggerAnim("AttackController", "attack3");
                }

                this.mob.doHurtTarget(target);
            }
        }

        protected void resetCooldown() {
            this.cooldown = this.adjustedTickDelay(20);
        }

        protected boolean isCooledDown() {
            return this.cooldown <= 0;
        }

        protected boolean canAttack(LivingEntity target) {
            return this.isCooledDown() && this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target);
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
    public boolean hurt(DamageSource source, float amount) {
        if(source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)){
            amount=amount/12;
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !isCharging();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.isAlive()) {
            return;
        }

        //Destructive charge
        if (this.horizontalCollision && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            boolean bl = false;
            AABB box = this.getBoundingBox().inflate(0.2);
            for (BlockPos blockPos : BlockPos.betweenClosed(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ))) {
                BlockState blockState = this.level().getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!(block instanceof LeavesBlock)) continue;
                bl = this.level().destroyBlock(blockPos, true, this) || bl;
            }
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean bl = super.doHurtTarget(target);
        if(target instanceof Player player && player.isBlocking()){
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
    protected void customServerAiStep() {
        super.customServerAiStep();

        if(this.isCharging()){
            EntitiesUtil.pushAndDamageEntities(this, 14f, 1.1, 1.1, 1.5D, BullvoreEntity.class, RotfiendEntity.class);
        }
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean wasCharging) {
        this.entityData.set(CHARGING, wasCharging);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("ChargingCooldown", this.chargeCooldownTimer);
        nbt.putBoolean("Charging", isCharging());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
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
            SoundType blockSoundGroup = state.getSoundType();
            this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch());
        }
    }

    @Override
    protected SoundEvent getStepSound() {
        if(this.isCharging()){
            return SoundEvents.COW_STEP;
        } else {
        return SoundEvents.ZOGLIN_STEP;}
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return TCOTS_Sounds.BULLVORE_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
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

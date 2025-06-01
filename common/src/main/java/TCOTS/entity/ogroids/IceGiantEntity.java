package TCOTS.entity.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Sounds;
import TCOTS.TCOTS_Tags;
import TCOTS.registry.TCOTS_Blocks;
import TCOTS.registry.TCOTS_Items;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.entity.goals.ReturnToNestGoal;
import TCOTS.entity.interfaces.GuardNestMob;
import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.registry.TCOTS_Effects;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.utils.MiscUtil;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class IceGiantEntity extends OgroidMonster implements GeoEntity, VibrationSystem, GuardNestMob {
    //xTODO: Add sounds
    //xTODO: Add behavior
    //When it reaches 66%, it pushes the player away, and go for its anchor
    //Can make a ground punch
    //Sleeps and only wake up if you attack, walk above snow or make any sound (Similar to Warden??)
    //Can charge
    //xTODO: Add return to anchor block if it's far (Return to home)
    //xTODO: Add structure
    //xTODO: Add map to find structure

    //xTODO: Add drops to monster & sword block
    //xTODO: Add bestiary entry

    private static final Logger LOGGER = LogUtils.getLogger();
    protected static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(IceGiantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos>  ANCHOR_POS = SynchedEntityData.defineId(IceGiantEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean>   SLEEPING = SynchedEntityData.defineId(IceGiantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean>   WAKING_UP = SynchedEntityData.defineId(IceGiantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> NEST_POS = SynchedEntityData.defineId(IceGiantEntity.class, EntityDataSerializers.BLOCK_POS);


    protected static final EntityDataAccessor<Boolean> FALLING = SynchedEntityData.defineId(IceGiantEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> FALLING_DISTANCE = SynchedEntityData.defineId(IceGiantEntity.class, EntityDataSerializers.FLOAT);

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        if(spawnReason!=MobSpawnType.NATURAL && spawnReason!=MobSpawnType.SPAWN_EGG){
            this.setNestPos(this.blockPosition());
        }
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    public static final RawAnimation WAKE_UP = RawAnimation.begin().thenPlayAndHold("sleeping.wake_up");
    public static final RawAnimation GO_TO_SLEEP = RawAnimation.begin().thenPlay("sleeping.go_to_sleep").thenLoop("sleeping.idle");
    public static final RawAnimation BIG_ATTACK = RawAnimation.begin().thenPlay("attack.big_swing");
    public static final RawAnimation ANCHOR_LAUNCH = RawAnimation.begin().thenPlay("attack.anchor_launch");


    public static final RawAnimation JUMP = RawAnimation.begin().thenPlayAndHold("special.jumping");
    public static final RawAnimation LANDING = RawAnimation.begin().thenPlay("special.landing");
    private final ServerBossEvent bossBar = (ServerBossEvent)new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public IceGiantEntity(EntityType<? extends OgroidMonster> entityType, Level world) {
        super(entityType, world);
        this.xpReward=25;

        this.vibrationCallback = new VibrationCallback(this);
        this.vibrationListenerData = new VibrationSystem.Data();
        this.gameEventHandler = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.STEP_HEIGHT, 1.5f)

                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.ATTACK_DAMAGE, 12.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.23f)

                .add(Attributes.ATTACK_KNOCKBACK, 3.0f)
                .add(Attributes.ARMOR, 10f)
                .add(Attributes.ARMOR_TOUGHNESS, 4f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8f)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);

        builder.define(CHARGING, Boolean.FALSE);
        builder.define(SLEEPING, Boolean.FALSE);
        builder.define(WAKING_UP, Boolean.FALSE);
        builder.define(ANCHOR_POS, BlockPos.ZERO);

        builder.define(FALLING, Boolean.FALSE);
        builder.define(FALLING_DISTANCE, fallDistance);

        builder.define(NEST_POS, BlockPos.ZERO);
    }

    private final float healthToReachSecondPhase=this.getMaxHealth()*0.6f;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new WakeUpGoal(this, MiscUtil.getTimeInTicks(120)));

        this.goalSelector.addGoal(1, new FloatGoal(this));

        this.goalSelector.addGoal(2, new GoForAnchor(this, 1.4D));

        this.goalSelector.addGoal(3, new GoToSleepGoal(this));

        this.goalSelector.addGoal(4, new IceGiantJumpAttack(this, MiscUtil.getTimeInTicks(20), MiscUtil.getTimeInTicks(10)));

        this.goalSelector.addGoal(5, new MeleeAttackGoal_IceGiant(this,1.2D, false, 1.4D, MiscUtil.getTimeInTicks(12), MiscUtil.getTimeInTicks(120)));

        this.goalSelector.addGoal(6, new ReturnToNestGoal(this, 0.75, 400));

        this.goalSelector.addGoal(7, new WanderAroundGoal_Giant(this, 0.75f, 20));

        this.goalSelector.addGoal(8, new LookAroundGoal_Giant(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public BlockPos getNestPos() {
        return this.entityData.get(NEST_POS);
    }

    @Override
    public void setNestPos(BlockPos pos) {
        this.entityData.set(NEST_POS, pos);
    }

    @Override
    public boolean canHaveNest() {
        return true;
    }

    @Override
    public void setCanHaveNest(boolean canHaveNest) {
    }

    @Override
    public boolean getExtraReasonToNotGoToNest() {
        return !this.isFalling() && !isGiantSleeping() &&

                !(this.getAnchorPos()!= BlockPos.ZERO
                && this.getHealth() < this.healthToReachSecondPhase
                && !this.hasAnchor());
    }

    private static class IceGiantJumpAttack extends Goal {

        private final IceGiantEntity giant;
        private final int minJumpCooldown;
        private final int maxJumpCooldown;

        public IceGiantJumpAttack(IceGiantEntity giant, int minJumpCooldown, int maxExtraRandomCooldown) {
            this.giant = giant;
            this.minJumpCooldown = minJumpCooldown;
            this.maxJumpCooldown = maxExtraRandomCooldown;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.giant.getTarget();

            if (target != null) {
                return !this.giant.cooldownBetweenJumps
                        && this.giant.isAggressive()
                        && !this.giant.isInLiquid()
                        && !this.giant.isPassenger()
                        && !this.giant.level().getBlockState(this.giant.blockPosition()).is(Blocks.HONEY_BLOCK)
                        && !this.giant.level().getBlockState(this.giant.blockPosition()).is(Blocks.COBWEB)
                        //Max
                        && this.giant.distanceTo(target) < 8

                        && !this.giant.hasAnchor()
                        ;

            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.giant.getTarget();

            if(target!=null){
                jumpAttack();
            }
        }

        private void jumpAttack(){
            giant.jumpFromGround();
            giant.setIsFalling(true);
            giant.cooldownBetweenJumps=true;
            giant.jumpTicks= minJumpCooldown + giant.random.nextIntBetweenInclusive(0,maxJumpCooldown);
        }
    }


    private VibrationSystem.Data vibrationListenerData;
    private final VibrationSystem.User vibrationCallback;
    private final DynamicGameEventListener<VibrationSystem.Listener> gameEventHandler;
    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> callback) {
        Level world = this.level();
        if (world instanceof ServerLevel serverWorld) {
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }
    @Override
    public @NotNull Data getVibrationData() {
        return vibrationListenerData;
    }

    @Override
    public @NotNull User getVibrationUser() {
        return vibrationCallback;
    }

    boolean listenToSound=false;
    private static class VibrationCallback implements VibrationSystem.User {
        private static final int RANGE = 16;
        private final PositionSource positionSource;

        private final IceGiantEntity giant;

        VibrationCallback(IceGiantEntity giant) {
            this.giant=giant;
            this.positionSource = new EntityPositionSource(giant, (float)(giant.getY()));
        }

        @Override
        public int getListenerRadius() {
            return RANGE;
        }

        @Override
        public @NotNull PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public @NotNull TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.WARDEN_CAN_LISTEN;
        }

        @Override
        public boolean canReceiveVibration(@NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull Holder<GameEvent> event, GameEvent.@NotNull Context emitter) {
            if (giant.isNoAi() || !giant.isGiantSleeping() || giant.isDeadOrDying() || giant.isGiantWakingUp()) {
                return false;
            }

            Entity entity = emitter.sourceEntity();
            return !(entity instanceof LivingEntity) || (!(entity instanceof Monster) && giant.canAttack((LivingEntity)entity));
        }

        @Override
        public void onReceiveVibration(@NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull Holder<GameEvent> event, @Nullable Entity sourceEntity, @Nullable Entity entity, float distance) {
            if (giant.isDeadOrDying()) {
                return;
            }
            giant.listenToSound=true;
        }
    }


    private static class MeleeAttackGoal_IceGiant extends MeleeAttackGoal_Animated{
        private final IceGiantEntity giant;
        private final double speedMultiplierRunValue;
        private final int chargeCooldownTicks;
        private final int sleepCooldown;

        private Path pathCharge;
        private double toChargeX;
        private double toChargeY;
        private double toChargeZ;


        public MeleeAttackGoal_IceGiant(IceGiantEntity mob, double speed, boolean pauseWhenMobIdle, double speedMultiplier, int chargeCooldown, int sleepCooldown) {
            super(mob, speed, pauseWhenMobIdle, 2);

            this.giant =mob;
            this.speedMultiplierRunValue = speedMultiplier;
            this.chargeCooldownTicks = chargeCooldown;
            this.sleepCooldown =sleepCooldown;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && (!giant.isGiantWakingUp() || !giant.isGiantSleeping()) && !giant.isFalling()

                    && !(giant.getAnchorPos()!= BlockPos.ZERO && giant.getHealth() < giant.healthToReachSecondPhase && !giant.hasAnchor());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && (!giant.isGiantWakingUp() || !giant.isGiantSleeping()) && !giant.isFalling()

                    && !(giant.getAnchorPos()!= BlockPos.ZERO && giant.getHealth() < giant.healthToReachSecondPhase && !giant.hasAnchor());
        }

        @Override
        public void start() {
            super.start();

            this.giant.chargeCooldownTimer = 40;
            this.giant.chargeCooldown = true;
            this.giant.canSleepTimer= sleepCooldown +giant.getRandom().nextIntBetweenInclusive(1,501);
        }

        @Override
        public void stop() {
            super.stop();

            this.giant.setIsCharging(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.giant.getTarget();
            if (target == null) {
                return;
            }

            if(giant.level().isClientSide) return;

            //Normal Attack, triggered when isn't charging and isn't launching the anchor
            if(!giant.isCharging() && giant.theConjunctionOfTheSpheres$getAnchor() == null) {
                this.giant.getLookControl().setLookAt(target, 30.0f, 30.0f);
                this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
                if ((this.pauseWhenMobIdle || this.giant.getSensing().hasLineOfSight(target)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.giant.getRandom().nextFloat() < 0.05f)) {
                    this.targetX = target.getX();
                    this.targetY = target.getY();
                    this.targetZ = target.getZ();
                    this.updateCountdownTicks = 4 + this.giant.getRandom().nextInt(7);
                    double d = this.giant.distanceToSqr(target);
                    if (d > 1024.0) {
                        this.updateCountdownTicks += 10;
                    } else if (d > 256.0) {
                        this.updateCountdownTicks += 5;
                    }
                    if (!this.giant.getNavigation().moveTo(target, this.speed)) {
                        this.updateCountdownTicks += 15;
                    }
                    this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
                }
                this.cooldown = Math.max(this.cooldown - 1, 0);
                this.attack(target);
            }



                // Get the monster's position and facing direction
                Vec3 monsterPosition = this.giant.position();
                Vec3 monsterLookVec = this.giant.calculateViewVector(0.0f, giant.getYHeadRot()); // This gives the direction the monster is facing
                boolean isFacingTarget = false;
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

            //Charging Attack
                //Start Charging
                if (this.giant.distanceTo(target) > 1 && !this.giant.chargeCooldown && !this.giant.isCharging() && isFacingTarget && this.giant.theConjunctionOfTheSpheres$getAnchor()==null) {

                    this.giant.getLookControl().setLookAt(
                            this.targetX,
                            this.targetY,
                            this.targetZ,
                            30.0f, 30.0f);

                    // Define how far you want the monster to charge (distance in blocks)
                    double chargeDistance = 20.0;
                    // Calculate the target position in front of the monster
                    Vec3 movingDirection = monsterPosition.add(monsterLookVec.multiply(chargeDistance, 0, chargeDistance));

                    this.toChargeX = movingDirection.x;
                    this.toChargeY = movingDirection.y;
                    this.toChargeZ = movingDirection.z;

                    this.pathCharge = this.giant.getNavigation().createPath(this.toChargeX, this.toChargeY, this.toChargeZ, 0);

                    if (this.pathCharge == null) {
                        return;
                    }

                    this.giant.setIsCharging(true);
                    this.giant.getLookControl().setLookAt(
                            this.toChargeX,
                            this.toChargeY,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.giant.playSound(TCOTS_Sounds.getSoundEvent("ice_giant_charge"), 1.0f, 1.0f);
                }

                //While it's charging
                if (giant.isCharging()) {
                    this.giant.getLookControl().setLookAt(
                            this.toChargeX,
                            this.toChargeY,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.giant.getNavigation().moveTo(this.pathCharge, this.speed * speedMultiplierRunValue);


                    //If Giant reach the coordinates or something blocks the path
                    if (this.giant.distanceToSqr(this.toChargeX, this.toChargeY, this.toChargeZ) < 2 || this.giant.horizontalCollision) {

                        this.giant.setIsCharging(false);

                        this.giant.chargeCooldownTimer = giant.hasAnchor()? this.chargeCooldownTicks*3 :this.chargeCooldownTicks;
                        this.giant.chargeCooldown = true;
                    }
                }

            //Anchor Attack
                //Launch Anchor Logic
                if(!giant.isCharging() && this.giant.distanceTo(target) < 10 && giant.hasAnchor() && !giant.anchorLaunchCooldown && isFacingTarget){
                    giant.getNavigation().stop();
                    giant.getLookControl().setLookAt(targetX, targetY+8, targetZ);
                    giant.launchAnchor();
                }

        }
    }

    private static class GoToSleepGoal extends Goal {

        private final IceGiantEntity giant;

        public GoToSleepGoal(IceGiantEntity giant) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
            this.giant=giant;
        }

        protected boolean isAtFavoredLocation() {
            BlockPos blockPos = BlockPos.containing(giant.getX(), giant.getBoundingBox().maxY, giant.getZ());
            return (!giant.level().canSeeSky(blockPos) && giant.getWalkTargetValue(blockPos) >= 0.0f ) || giant.level().isNight();
        }

        @Override
        public boolean canUse() {
            Iterable<VoxelShape> iterable = giant.level().getBlockCollisions(giant, giant.sleepingBox());
            for (VoxelShape voxelShape : iterable) {
                if (voxelShape.isEmpty()) continue;
                return false;
            }

            return !giant.isAggressive() && isAtFavoredLocation() && !giant.getSleepCooldown() && !giant.isGiantWakingUp();
        }

//        int AnimationTicks=5;
        @Override
        public void start() {
            giant.getNavigation().stop();
//            AnimationTicks=5;
            giant.setIsGiantSleeping(true);
//            giant.setIsGiantGoingToSleep(true);
            this.stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    private static class GoForAnchor extends Goal{

        private final IceGiantEntity giant;
        private final double speed;

        public GoForAnchor(IceGiantEntity giant, double speed){
            this.giant = giant;

            this.speed=speed;
        }

        @Override
        public boolean canUse() {
            return
                     giant.getAnchorPos()!= BlockPos.ZERO
                    && giant.getHealth() < giant.healthToReachSecondPhase
                    && !giant.hasAnchor();
        }

        @Override
        public boolean canContinueToUse() {
            return !giant.hasAnchor();
        }

        @Override
        public void start() {
            giant.triggerAnim("AttackController", "big_attack");

            giant.playSound(TCOTS_Sounds.getSoundEvent("ice_giant_charge"), 1, 1);

            EntitiesUtil.pushAndDamageEntities(giant, 2, 2, 1.5, 4.0);

            this.startMovingTo(giant.getNavigation(), giant.getAnchorPos().getX(), giant.getAnchorPos().getY(), giant.getAnchorPos().getZ(), speed);
        }

        @Override
        public void tick() {
            this.startMovingTo(giant.getNavigation(), giant.getAnchorPos().getX(), giant.getAnchorPos().getY(), giant.getAnchorPos().getZ(), speed);


            if(giant.distanceToSqr(giant.getAnchorPos().getCenter()) < 16){
                giant.level().destroyBlock(giant.getAnchorPos(), false);

                giant.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(TCOTS_Items.GIANT_ANCHOR.get()));
            }
        }

        public void startMovingTo(PathNavigation navigation, int x, int y, int z, double speed) {
            navigation.moveTo(navigation.createPath(x, y, z, 2), speed);
        }
    }

    private boolean hasAnchor(){
        if(TCOTS_Items.GIANT_ANCHOR==null){
            return false;
        }

        return this.getMainHandItem().is(TCOTS_Items.GIANT_ANCHOR.get());
    }

    private void launchAnchor(){
        if(this.hasAnchor()) {
            ItemStack stack = this.getMainHandItem();
            AnchorProjectileEntity anchorProjectile = new AnchorProjectileEntity(this, this.level());

            anchorProjectile.setDamage(12);
            anchorProjectile.setEnchanted(stack.hasFoil());
            anchorProjectile.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0f, 0.4f, 1.0f);

            this.triggerAnim("AttackController", "anchor_launch");
            this.level().addFreshEntity(anchorProjectile);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), TCOTS_Sounds.getSoundEvent("anchor_throw"), this.getSoundSource(),
                    1.0f, 1.0f);
            this.playSound(this.getAttackSound(), 1.0F, 1.0F);

            stack.set(TCOTS_Items.AnchorRetrieve(), true);

            this.anchorLaunchCooldownTimer=MiscUtil.getTimeInTicks(10);
            this.anchorLaunchCooldown=true;
            this.retrieveAnchorTimer=0;
        }
    }

    private void retrieveAnchor(){
        AnchorProjectileEntity anchorProjectile= (AnchorProjectileEntity) this.theConjunctionOfTheSpheres$getAnchor();
        if(anchorProjectile!=null && retrieveAnchorTimer==30) {
            anchorProjectile.pickupType= AbstractArrow.Pickup.ALLOWED;
            anchorProjectile.dealtDamage=true;
            anchorProjectile.setPos(this.blockPosition().getCenter());
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), TCOTS_Sounds.getSoundEvent("anchor_chain"), this.getSoundSource(), 1.0f, 1.0f);
            anchorProjectile.discard();

            ItemStack stack = this.getMainHandItem();
            stack.remove(TCOTS_Items.AnchorRetrieve());
        }
    }




    private static class WakeUpGoal extends Goal {

        private final IceGiantEntity giant;
        private final int returnTicks;

        public WakeUpGoal(IceGiantEntity giant, int returnTicks) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
            this.giant=giant;
            this.returnTicks=returnTicks;
        }


        @Override
        public boolean canUse() {
            return giant.isGiantSleeping() && giant.listenToSound;
        }

        @Override
        public boolean canContinueToUse() {
            return giant.isGiantSleeping();
        }

        int AnimationTicks=70;
        @Override
        public void start(){
            giant.playSound(TCOTS_Sounds.getSoundEvent("ice_giant_wake_up"), 1, 1);
            AnimationTicks=70;
            giant.setIsGiantWakingUp(true);
        }

        @Override
        public void tick(){
            if (AnimationTicks > 0) {
                --AnimationTicks;
            } else {
                stop();
            }
        }

        @Override
        public void stop(){
            giant.setIsGiantWakingUp(false);
            giant.canSleepTimer=returnTicks+giant.getRandom().nextIntBetweenInclusive(1,501);
            giant.setIsGiantSleeping(false);
            giant.listenToSound=false;
            giant.setSleepCooldown(true);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

    }


    private static class WanderAroundGoal_Giant extends RandomStrollGoal {
        private final IceGiantEntity giant;

        public WanderAroundGoal_Giant(IceGiantEntity mob, double speed, int chance) {
            super(mob, speed, chance);
            this.giant=mob;
        }


        @Override
        public boolean canUse() {
            return super.canUse() && !giant.isGiantSleeping() && !giant.isGiantWakingUp();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !giant.isGiantSleeping() && !giant.isGiantWakingUp();
        }
    }

    private static class LookAroundGoal_Giant extends RandomLookAroundGoal{
        private final IceGiantEntity giant;

        public LookAroundGoal_Giant(IceGiantEntity mob) {
            super(mob);
            this.giant=mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !giant.isGiantSleeping();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !giant.isGiantSleeping();
        }
    }

    public void setIsCharging(boolean isCharging) {
        this.entityData.set(CHARGING, isCharging);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setIsGiantSleeping(boolean isSleeping) {
        this.entityData.set(SLEEPING, isSleeping);
    }

    public boolean isGiantSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setIsGiantWakingUp(boolean isSleeping) {
        this.entityData.set(WAKING_UP, isSleeping);
    }

    public boolean isGiantWakingUp() {
        return this.entityData.get(WAKING_UP);
    }

    public void setAnchorPos(BlockPos anchorPos) {
        this.entityData.set(ANCHOR_POS, anchorPos);
    }

    public BlockPos getAnchorPos() {
        return this.entityData.get(ANCHOR_POS);
    }

    public void setSleepCooldown(boolean cannotSleep) {
        this.sleepCooldown=cannotSleep;
    }

    public boolean getSleepCooldown() {
        return sleepCooldown;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, state ->
                {
                    if(!this.isGiantSleeping() && !this.isGiantWakingUp()) {
                        return GeoControllersUtil.idleWalkRunController(state);
                    } else {
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                })
        );

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
                        .triggerableAnim("big_attack", BIG_ATTACK)
                        .triggerableAnim("anchor_launch", ANCHOR_LAUNCH)
        );

        //Sleep Control
        controllerRegistrar.add(
                new AnimationController<>(this, "SleepingController", 1, state -> {
                    if(this.isGiantSleeping()){
                        state.setAnimation(GO_TO_SLEEP);
                        return PlayState.CONTINUE;
                    } else {
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                }));

        //Waking Up
        controllerRegistrar.add(
                new AnimationController<>(this, "WakingUpController", 1, state -> {
                    if (this.isGiantWakingUp()){
                        state.setAnimation(WAKE_UP);
                        return PlayState.CONTINUE;
                    }
                    else {
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                }).setParticleKeyframeHandler(event -> {
                        BlockState blockState = this.getBlockStateOn();
                        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
                            for (int i = 0; i < 32; ++i) {

                                Vec3 vec3dVelocity = new Vec3(
                                        ((double)this.random.nextFloat()) * 0.3,
                                        0.0,
                                        ((double)this.random.nextFloat()) * 0.3)
                                        .xRot(-this.getXRot() * ((float)Math.PI / 180))
                                        .yRot(-this.getYRot() * ((float)Math.PI / 180));

                                Vec3 vec3dPos = new Vec3((
                                        (double)this.random.nextFloat() - 0.5) * 0.6,
                                        (double)(-this.random.nextFloat()) * 0.01,
                                        1.6 + ((double)this.random.nextFloat() - 0.5) * 0.6)
                                        .yRot(-this.yBodyRot * ((float)Math.PI / 180))
                                        .add(this.getX(), this.getY(), this.getZ());

                                this.level().addParticle(
                                        new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                                        //Position
                                        vec3dPos.x,
                                        vec3dPos.y,
                                        vec3dPos.z,
                                        //Velocity
                                        vec3dVelocity.x,
                                        vec3dVelocity.y + 0.1,
                                        vec3dVelocity.z);
                            }
                        }
                }).setSoundKeyframeHandler(event -> this.playSound(TCOTS_Sounds.getSoundEvent("big_impact"), 1, 1))
        );

        //Jumping
        controllerRegistrar.add(
                new AnimationController<>(this, "JumpController", 1, state -> {
                    if(this.isFalling()){
                        state.setAnimation(JUMP);
                        return PlayState.CONTINUE;
                    } else {
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                }));

        //Landing
        controllerRegistrar.add(
                new AnimationController<>(this, "LandingController", 1, state -> PlayState.STOP)
                        .triggerableAnim("landing", LANDING)
        );
    }

    int chargeCooldownTimer;
    boolean chargeCooldown = false;

    private void tickCharge(){
        if(chargeCooldownTimer>0){
            --chargeCooldownTimer;
        } else if (chargeCooldown){
            chargeCooldown=false;
        }

        if(this.isCharging()){
            EntitiesUtil.spawnGroundParticles(this);
        }
    }

    private int canSleepTimer;
    private boolean sleepCooldown;

    private void tickSleep(){
        if(canSleepTimer>0){
            --canSleepTimer;
        } else if (this.getSleepCooldown()){
            this.setSleepCooldown(false);
        }
    }


    int anchorLaunchCooldownTimer;
    boolean anchorLaunchCooldown = false;

    private void tickAnchorLaunch(){
        if(anchorLaunchCooldownTimer>0){
            --anchorLaunchCooldownTimer;
        } else if (anchorLaunchCooldown){
            anchorLaunchCooldown=false;
        }

    }

    @Override
    public @NotNull Vec3 getRopeHoldPosition(float delta) {
        double d = 1.3 * (this.getMainArm() == HumanoidArm.RIGHT ? -1.0 : 1.0);
        float f = Mth.lerp(delta * 0.5f, this.getXRot(), this.xRotO) * ((float)Math.PI / 180);
        float g = Mth.lerp(delta, this.yBodyRotO, this.yBodyRot) * ((float)Math.PI / 180);
        if (this.isFallFlying() || this.isAutoSpinAttack()) {
            float k;
            Vec3 vec3d = this.getViewVector(delta);
            Vec3 vec3d2 = this.getDeltaMovement();
            double e = vec3d2.horizontalDistanceSqr();
            double h = vec3d.horizontalDistanceSqr();
            if (e > 0.0 && h > 0.0) {
                double i = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(e * h);
                double j = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
                k = (float)(Math.signum(j) * Math.acos(i));
            } else {
                k = 0.0f;
            }
            return this.getPosition(delta).add(new Vec3(d, -0.11, 0.85).zRot(-k).xRot(-f).yRot(-g));
        }
        if (this.isVisuallySwimming()) {
            return this.getPosition(delta).add(new Vec3(d, 0.2, -0.15).xRot(-f).yRot(-g));
        }
        double l = this.getBoundingBox().getYsize() - 2.6;
        double e = this.isCrouching() ? -0.2 : 0.07;
        return this.getPosition(delta).add(new Vec3(d, l, e).yRot(-g));
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        if(this.isGiantSleeping()){
            return this.sleepingBox();
        } else {
            return super.makeBoundingBox();
        }
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return this.isGiantSleeping()? this.getType().getDimensions().withEyeHeight(1.6f) : super.getDefaultDimensions(pose);
    }

    private AABB sleepingBox(){
        return new AABB(this.getX() - 1.8, this.getY() + 2.6, this.getZ() - 1.5,
                this.getX() + 1.8, this.getY(), this.getZ() + 1.5);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);

        if(isGiantSleeping() || !isGiantSleeping()){
            this.setBoundingBox(this.makeBoundingBox());
            this.refreshDimensions();
        }
    }


    private void tickFindAnchor(){

        if(this.getAnchorPos()==BlockPos.ZERO && !this.hasAnchor()){
            Optional<BlockPos> optional = this.findAnchor(this);
            optional.ifPresent(this::setAnchorPos);
        }

        //To avoid triggering the goal if it has already the anchor, or it's way too far from the Anchor (50+ blocks)
        if(this.hasAnchor() ||
                (this.getAnchorPos()!=BlockPos.ZERO &&
                this.distanceToSqr(this.getAnchorPos().getCenter()) > 250)
        ){
            this.setAnchorPos(BlockPos.ZERO);
        }
    }

    private Optional<BlockPos> findAnchor(PathfinderMob entity) {
        double searchDistance = 20;

        Predicate<BlockPos> predicate = pos -> entity.level().getBlockState(pos).is(TCOTS_Blocks.GiantAnchor());

        BlockPos blockPos = entity.blockPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        int i = 0;
        while ((double)i <= searchDistance) {
            int j = 0;
            while ((double)j < searchDistance) {
                int k = 0;
                while (k <= j) {
                    int l;
                    l = k < j && k > -j ? j : 0;
                    while (l <= j) {
                        mutable.setWithOffset(blockPos, k, i - 1, l);
                        if (blockPos.closerThan(mutable, searchDistance) && predicate.test(mutable)) {
                            return Optional.of(mutable);
                        }
                        l = l > 0 ? -l : 1 - l;
                    }
                    k = k > 0 ? -k : 1 - k;
                }
                ++j;
            }
            i = i > 0 ? -i : 1 - i;
        }
        return Optional.empty();
    }
    private int retrieveAnchorTimer=0;
    private void retrieveAnchorTimer(){

        if(this.hasAnchor() && this.theConjunctionOfTheSpheres$getAnchor()!=null && retrieveAnchorTimer<30){
            this.getNavigation().stop();
            retrieveAnchorTimer++;
        }

        this.retrieveAnchor();
    }

    @Override
    public void tick() {
        super.tick();

        this.tickCharge();

        this.tickSleep();


        if (this.level() instanceof ServerLevel serverWorld) {
            VibrationSystem.Ticker.tick(serverWorld, this.vibrationListenerData, this.vibrationCallback);
        }

        //To sync fallDistance with the client
        setFallingDistance(fallDistance);

        //Tick for jumps
        tickJump();

        tickFindAnchor();

        tickAnchorLaunch();

        if(this.isSnowing()){
            this.spawnSnowflakesAround();
        }
    }



    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());

        if(this.isCharging()){
            EntitiesUtil.pushAndDamageEntities(this, 12f, 1.3, 1.2, 1.6D, IceGiantEntity.class);
        }

        //Destroys blocks to avoid suffocation when waking up
        int j;
        int i;
        if (this.isGiantWakingUp() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            i = Mth.floor(this.getY());
            j = Mth.floor(this.getX());
            int k = Mth.floor(this.getZ());
            for (int l = -1; l <= 1; ++l) {
                for (int m = -1; m <= 1; ++m) {
                    for (int n = 0; n <= 4; ++n) {
                        int o = j + l;
                        int p = i + n;
                        int q = k + m;
                        BlockPos blockPos = new BlockPos(o, p, q);
                        BlockState blockState = this.level().getBlockState(blockPos);
                        if (!WitherBoss.canDestroy(blockState)) continue;
                        this.level().destroyBlock(blockPos, true, this);
                    }
                }
            }
        }

        this.retrieveAnchorTimer();

        //If it's snowing increases its strength
        if(this.isSnowing()){
            AttributeInstance entityAttributeInstance_attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if(entityAttributeInstance_attack!=null) {
                entityAttributeInstance_attack.removeModifier(BLIZZARD_STRENGTH_BOOST.id());
                entityAttributeInstance_attack.addTransientModifier(BLIZZARD_STRENGTH_BOOST);
            }

            AttributeInstance entityAttributeInstance_speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if(entityAttributeInstance_speed!=null) {
                entityAttributeInstance_speed.removeModifier(BLIZZARD_SPEED_BOOST.id());
                entityAttributeInstance_speed.addTransientModifier(BLIZZARD_SPEED_BOOST);
            }
        } else {
            AttributeInstance entityAttributeInstance_attack = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if(entityAttributeInstance_attack!=null) entityAttributeInstance_attack.removeModifier(BLIZZARD_STRENGTH_BOOST.id());

            AttributeInstance entityAttributeInstance_speed = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if(entityAttributeInstance_speed!=null) entityAttributeInstance_speed.removeModifier(BLIZZARD_SPEED_BOOST.id());
        }
    }

    private static final AttributeModifier BLIZZARD_STRENGTH_BOOST = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "giant_blizzard_strength_boost"),
            4.0f,
            AttributeModifier.Operation.ADD_VALUE);
    private static final AttributeModifier BLIZZARD_SPEED_BOOST = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "giant_blizzard_speed_boost"),
            0.1f,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    private boolean isSnowing(){
        Biome biome = this.level().getBiome(this.blockPosition()).value();
        return this.level().isRaining() && biome.shouldSnow(this.level(), this.blockPosition());
    }

    protected void spawnSnowflakesAround(){
        if(this.tickCount%6 == 0){
            for (int i = 0; i < 8; ++i) {
                double d = this.getX() + (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
                double e = (this.getEyeY()-0.5f)+ (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
                double f = this.getZ() + (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
                this.level().addParticle(ParticleTypes.SNOWFLAKE, d,e,f,0,0,0);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("Charging", this.isCharging());
        nbt.putInt("ChargingCooldown", this.chargeCooldownTimer);

        nbt.putBoolean("Sleeping", this.isGiantSleeping());
        nbt.putInt("CanSleepTimer", this.canSleepTimer);
        nbt.putBoolean("SleepCooldown", this.getSleepCooldown());


        nbt.putInt("AnchorPosX", this.getAnchorPos().getX());
        nbt.putInt("AnchorPosY", this.getAnchorPos().getY());
        nbt.putInt("AnchorPosZ", this.getAnchorPos().getZ());

        VibrationSystem.Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationListenerData).resultOrPartial(LOGGER::error).ifPresent(listenerData -> nbt.put("listener", listenerData));
        nbt.putBoolean("ListenToSound",this.listenToSound);

        nbt.putInt("JumpCooldown", this.jumpTicks);

        nbt.putInt("AnchorLaunchTimer", this.anchorLaunchCooldownTimer);
        nbt.putBoolean("AnchorLaunchCooldown", this.anchorLaunchCooldown);

        writeNbtGuardNest(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

        this.setIsCharging(nbt.getBoolean("Charging"));
        this.chargeCooldownTimer = nbt.getInt("ChargingCooldown");

        this.setIsGiantSleeping(nbt.getBoolean("Sleeping"));
        this.canSleepTimer = nbt.getInt("CanSleepTimer");
        this.setSleepCooldown(nbt.getBoolean("SleepCooldown"));

        int x = nbt.getInt("AnchorPosX");
        int y = nbt.getInt("AnchorPosY");
        int z = nbt.getInt("AnchorPosZ");

        this.setAnchorPos(new BlockPos(x,y,z));

        if (nbt.contains("listener", Tag.TAG_COMPOUND)) {
            VibrationSystem.Data.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener")))
                    .resultOrPartial(LOGGER::error).ifPresent(listenerData -> this.vibrationListenerData = listenerData);
        }

        this.listenToSound=nbt.getBoolean("ListenToSound");

        this.jumpTicks=nbt.getInt("JumpCooldown");

        this.anchorLaunchCooldownTimer=nbt.getInt("AnchorLaunchTimer");
        this.anchorLaunchCooldown=nbt.getBoolean("AnchorLaunchCooldown");

        readNbtGuardNest(nbt);
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() != TCOTS_Effects.NorthernWindEffect() && super.canBeAffected(effect);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !isCharging() && !isGiantSleeping();
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || this.isGiantWakingUp() || this.isFalling();
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_EFFECTS)) {
            return amount;
        }
        if(source.is(DamageTypeTags.IS_PROJECTILE)){
            return amount*0.2f;
        }

        return super.getDamageAfterMagicAbsorb(source, amount);
    }

    @Override
    protected void dropFromLootTable(@NotNull DamageSource damageSource, boolean causedByPlayer) {
        super.dropFromLootTable(damageSource, causedByPlayer);

        if(this.hasAnchor() && causedByPlayer && this.random.nextIntBetweenInclusive(0,10)==0){
            this.spawnAtLocation(this.getMainHandItem().getItem());
        }
    }

    //Jumping Stuff
    @Override
    protected float getJumpPower() {
        return 0.7f * this.getBlockJumpFactor() + this.getJumpBoostPower();
    }

    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 4;
    }

    @Override
    public void jumpFromGround() {
        this.playSound(TCOTS_Sounds.getSoundEvent("ice_giant_punch"), 1.0f, 1.0f);
        super.jumpFromGround();
    }
    private static final byte FALLING_PARTICLES = 42;
    @Override
    public void resetFallDistance() {
        if(isFalling()){
            setIsFalling(false);
            if(this.level().getBlockState(this.blockPosition().below()).getFluidState().is(Fluids.EMPTY)
                    && !this.level().getBlockState(this.blockPosition()).is(TCOTS_Tags.NEGATES_DEVOURER_JUMP)
                    && !this.level().getBlockState(this.blockPosition().below()).is(TCOTS_Tags.NEGATES_DEVOURER_JUMP))
            {
                this.triggerAnim("LandingController","landing");
                EntitiesUtil.pushAndDamageEntities(this, 8 + (fallDistance*2f), 4.0f + (fallDistance * 0.5f), 3, 3.0);
                this.playSound(TCOTS_Sounds.getSoundEvent("big_impact"), 1.0f, 1.0f);
                this.level().broadcastEntityEvent(this, FALLING_PARTICLES);
            }
        }

        super.resetFallDistance();
    }
    @Override
    public void handleEntityEvent(byte status) {
        if(status==FALLING_PARTICLES){
            EntitiesUtil.spawnImpactParticles(this, 1.9975f + (4.0f + (getFallingDistance() * 0.5f)), getFallingDistance());
        } else{
            super.handleEntityEvent(status);
        }
    }

    public boolean cooldownBetweenJumps=false;
    private int jumpTicks;

    private void tickJump(){

        if (jumpTicks > 0) {
            --jumpTicks;
        } else {
            cooldownBetweenJumps=false;
        }
    }


    public void setIsFalling(boolean isFalling) {
        this.entityData.set(FALLING, isFalling);
    }

    public boolean isFalling() {
        return this.entityData.get(FALLING);
    }

    public void setFallingDistance(float fallingDistance) {
        this.entityData.set(FALLING_DISTANCE, fallingDistance);
    }

    public double getFallingDistance() {
        return this.entityData.get(FALLING_DISTANCE);
    }

    //Sounds
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.isGiantWakingUp()) {
            if (this.isGiantSleeping()) {
                return TCOTS_Sounds.getSoundEvent("ice_giant_snore");
            } else {
                return TCOTS_Sounds.getSoundEvent("ice_giant_idle");
            }
        }
        else {
            return null;
        }
    }



    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("ice_giant_attack");
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("ice_giant_hurt");
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("ice_giant_death");
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

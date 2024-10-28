package TCOTS.entity.ogroids;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.utils.MiscUtil;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.GameEventTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class IceGiantEntity extends OgroidMonster implements GeoEntity, Vibrations {
    //xTODO: Add sounds
    //xTODO: Add behavior
    //When it reaches 66%, it pushes the player away, and go for its anchor
    //Can make a ground punch
    //Sleeps and only wake up if you attack, walk above snow or make any sound (Similar to Warden??)
    //Can charge
    //TODO: Add return to anchor block if it's far (Return to home)
    //TODO: Add structure
    //TODO: Add map
    //TODO: Add drops to monster & sword block
    //TODO: Add bestiary entry
    //TODO: Add map to find structure

    private static final Logger LOGGER = LogUtils.getLogger();
    protected static final TrackedData<Boolean> CHARGING = DataTracker.registerData(IceGiantEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos>  ANCHOR_POS = DataTracker.registerData(IceGiantEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Boolean>   SLEEPING = DataTracker.registerData(IceGiantEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean>   WAKING_UP = DataTracker.registerData(IceGiantEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    protected static final TrackedData<Boolean> FALLING = DataTracker.registerData(IceGiantEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Float> FALLING_DISTANCE = DataTracker.registerData(IceGiantEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Random random = world.getRandom();
        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE)).addPersistentModifier(new EntityAttributeModifier("Random spawn bonus", random.nextTriangular(0.0, 0.11485000000000001), EntityAttributeModifier.Operation.MULTIPLY_BASE));
        return entityData;
    }

    public static final RawAnimation WAKE_UP = RawAnimation.begin().thenPlayAndHold("sleeping.wake_up");
    public static final RawAnimation GO_TO_SLEEP = RawAnimation.begin().thenPlay("sleeping.go_to_sleep").thenLoop("sleeping.idle");
    public static final RawAnimation BIG_ATTACK = RawAnimation.begin().thenPlay("attack.big_swing");
    public static final RawAnimation ANCHOR_LAUNCH = RawAnimation.begin().thenPlay("attack.anchor_launch");


    public static final RawAnimation JUMP = RawAnimation.begin().thenPlayAndHold("special.jumping");
    public static final RawAnimation LANDING = RawAnimation.begin().thenPlay("special.landing");
    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS).setDarkenSky(true);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public IceGiantEntity(EntityType<? extends OgroidMonster> entityType, World world) {
        super(entityType, world);
        this.setStepHeight(1.5f);
        this.experiencePoints=25;

        this.vibrationCallback = new VibrationCallback(this);
        this.vibrationListenerData = new Vibrations.ListenerData();
        this.gameEventHandler = new EntityGameEventHandler<>(new Vibrations.VibrationListener(this));
    }



    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 12.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 3.0f)
                .add(EntityAttributes.GENERIC_ARMOR, 10f)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 4f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(CHARGING, Boolean.FALSE);
        this.dataTracker.startTracking(SLEEPING, Boolean.FALSE);
        this.dataTracker.startTracking(WAKING_UP, Boolean.FALSE);
        this.dataTracker.startTracking(ANCHOR_POS, BlockPos.ORIGIN);

        this.dataTracker.startTracking(FALLING, Boolean.FALSE);
        this.dataTracker.startTracking(FALLING_DISTANCE, fallDistance);
    }

    private final float healthToReachSecondPhase=this.getMaxHealth()*0.6f;

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new WakeUpGoal(this, MiscUtil.getTimeInTicks(120)));

        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new GoForAnchor(this, 1.2D));

        this.goalSelector.add(3, new GoToSleepGoal(this));

        this.goalSelector.add(4, new IceGiantJumpAttack(this, MiscUtil.getTimeInTicks(20), MiscUtil.getTimeInTicks(10)));

        this.goalSelector.add(5, new MeleeAttackGoal_IceGiant(this,1.2D, false, 1.4D, MiscUtil.getTimeInTicks(12), MiscUtil.getTimeInTicks(120)));

        this.goalSelector.add(6, new WanderAroundGoal_Giant(this, 0.75f, 20));

        this.goalSelector.add(7, new LookAroundGoal_Giant(this));

        this.targetSelector.add(0, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
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
        public boolean canStart() {
            LivingEntity target = this.giant.getTarget();

            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !this.giant.cooldownBetweenJumps
                        && this.giant.isAttacking()
                        && !this.giant.isInFluid()
                        && !this.giant.hasVehicle()
                        && !this.giant.getWorld().getBlockState(this.giant.getBlockPos()).isOf(Blocks.HONEY_BLOCK)
                        && !this.giant.getWorld().getBlockState(this.giant.getBlockPos()).isOf(Blocks.COBWEB)
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
            giant.jump();
            giant.setIsFalling(true);
            giant.cooldownBetweenJumps=true;
            giant.jumpTicks= minJumpCooldown + giant.random.nextBetween(0,maxJumpCooldown);
        }
    }


    private Vibrations.ListenerData vibrationListenerData;
    private final Vibrations.Callback vibrationCallback;
    private final EntityGameEventHandler<Vibrations.VibrationListener> gameEventHandler;
    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }
    @Override
    public ListenerData getVibrationListenerData() {
        return vibrationListenerData;
    }

    @Override
    public Callback getVibrationCallback() {
        return vibrationCallback;
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
        public boolean canStart() {
            return super.canStart() && (!giant.isGiantWakingUp() || !giant.isGiantSleeping()) && !giant.isFalling()

                    && !(giant.getAnchorPos()!= BlockPos.ORIGIN && giant.getHealth() < giant.healthToReachSecondPhase && !giant.hasAnchor());
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && (!giant.isGiantWakingUp() || !giant.isGiantSleeping()) && !giant.isFalling()

                    && !(giant.getAnchorPos()!= BlockPos.ORIGIN && giant.getHealth() < giant.healthToReachSecondPhase && !giant.hasAnchor());
        }

        @Override
        public void start() {
            super.start();

            this.giant.chargeCooldownTimer = 40;
            this.giant.chargeCooldown = true;
            this.giant.canSleepTimer= sleepCooldown +giant.getRandom().nextBetween(1,501);
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

            if(giant.getWorld().isClient) return;

            //Normal Attack, triggered when isn't charging and isn't launching the anchor
            if(!giant.isCharging() && giant.theConjunctionOfTheSpheres$getAnchor() == null) {
                this.giant.getLookControl().lookAt(target, 30.0f, 30.0f);
                this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
                if ((this.pauseWhenMobIdle || this.giant.getVisibilityCache().canSee(target)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.giant.getRandom().nextFloat() < 0.05f)) {
                    this.targetX = target.getX();
                    this.targetY = target.getY();
                    this.targetZ = target.getZ();
                    this.updateCountdownTicks = 4 + this.giant.getRandom().nextInt(7);
                    double d = this.giant.squaredDistanceTo(target);
                    if (d > 1024.0) {
                        this.updateCountdownTicks += 10;
                    } else if (d > 256.0) {
                        this.updateCountdownTicks += 5;
                    }
                    if (!this.giant.getNavigation().startMovingTo(target, this.speed)) {
                        this.updateCountdownTicks += 15;
                    }
                    this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
                }
                this.cooldown = Math.max(this.cooldown - 1, 0);
                this.attack(target);
            }



                // Get the monster's position and facing direction
                Vec3d monsterPosition = this.giant.getPos();
                Vec3d monsterLookVec = this.giant.getRotationVector(0.0f, giant.getHeadYaw()); // This gives the direction the monster is facing
                boolean isFacingTarget = false;
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

            //Charging Attack
                //Start Charging
                if (this.giant.distanceTo(target) > 1 && !this.giant.chargeCooldown && !this.giant.isCharging() && isFacingTarget && this.giant.theConjunctionOfTheSpheres$getAnchor()==null) {

                    this.giant.getLookControl().lookAt(
                            this.targetX,
                            this.targetY,
                            this.targetZ,
                            30.0f, 30.0f);

                    // Define how far you want the monster to charge (distance in blocks)
                    double chargeDistance = 20.0;
                    // Calculate the target position in front of the monster
                    Vec3d movingDirection = monsterPosition.add(monsterLookVec.multiply(chargeDistance, 0, chargeDistance));

                    this.toChargeX = movingDirection.x;
                    this.toChargeY = movingDirection.y;
                    this.toChargeZ = movingDirection.z;

                    this.pathCharge = this.giant.getNavigation().findPathTo(this.toChargeX, this.toChargeY, this.toChargeZ, 0);

                    if (this.pathCharge == null) {
                        return;
                    }

                    this.giant.setIsCharging(true);
                    this.giant.getLookControl().lookAt(
                            this.toChargeX,
                            this.toChargeY,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.giant.playSound(TCOTS_Sounds.ICE_GIANT_CHARGE, 1.0f, 1.0f);
                }

                //While it's charging
                if (giant.isCharging()) {
                    this.giant.getLookControl().lookAt(
                            this.toChargeX,
                            this.toChargeY,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.giant.getNavigation().startMovingAlong(this.pathCharge, this.speed * speedMultiplierRunValue);


                    //If Giant reach the coordinates or something blocks the path
                    if (this.giant.squaredDistanceTo(this.toChargeX, this.toChargeY, this.toChargeZ) < 2 || this.giant.horizontalCollision) {

                        this.giant.setIsCharging(false);

                        this.giant.chargeCooldownTimer = giant.hasAnchor()? this.chargeCooldownTicks*3 :this.chargeCooldownTicks;
                        this.giant.chargeCooldown = true;
                    }
                }

            //Anchor Attack
                //Launch Anchor Logic
                if(!giant.isCharging() && this.giant.distanceTo(target) < 10 && giant.hasAnchor() && !giant.anchorLaunchCooldown && isFacingTarget){
                    giant.getNavigation().stop();
                    giant.getLookControl().lookAt(targetX, targetY+8, targetZ);
                    giant.launchAnchor();
                }

        }
    }

    private static class GoToSleepGoal extends Goal {

        private final IceGiantEntity giant;

        public GoToSleepGoal(IceGiantEntity giant) {
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
            this.giant=giant;
        }

        protected boolean isAtFavoredLocation() {
            BlockPos blockPos = BlockPos.ofFloored(giant.getX(), giant.getBoundingBox().maxY, giant.getZ());
            return (!giant.getWorld().isSkyVisible(blockPos) && giant.getPathfindingFavor(blockPos) >= 0.0f ) || giant.getWorld().isNight();
        }

        @Override
        public boolean canStart() {
            Iterable<VoxelShape> iterable = giant.getWorld().getBlockCollisions(giant, giant.sleepingBox());
            for (VoxelShape voxelShape : iterable) {
                if (voxelShape.isEmpty()) continue;
                return false;
            }

            return !giant.isAttacking() && isAtFavoredLocation() && !giant.getSleepCooldown() && !giant.isGiantWakingUp();
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
        public boolean shouldRunEveryTick() {
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
        public boolean canStart() {
            return
                     giant.getAnchorPos()!= BlockPos.ORIGIN
                    && giant.getHealth() < giant.healthToReachSecondPhase
                    && !giant.hasAnchor();
        }

        @Override
        public boolean shouldContinue() {
            return !giant.hasAnchor();
        }

        @Override
        public void start() {
            giant.triggerAnim("AttackController", "big_attack");

            giant.playSound(TCOTS_Sounds.ICE_GIANT_CHARGE, 1, 1);

            EntitiesUtil.pushAndDamageEntities(giant, 2, 2, 1.5, 4.0);

            this.startMovingTo(giant.getNavigation(), giant.getAnchorPos().getX(), giant.getAnchorPos().getY(), giant.getAnchorPos().getZ(), speed);
        }

        @Override
        public void tick() {
            this.startMovingTo(giant.getNavigation(), giant.getAnchorPos().getX(), giant.getAnchorPos().getY(), giant.getAnchorPos().getZ(), speed);


            if(giant.squaredDistanceTo(giant.getAnchorPos().toCenterPos()) < 16){
                giant.getWorld().breakBlock(giant.getAnchorPos(), false);

                giant.setStackInHand(Hand.MAIN_HAND, new ItemStack(TCOTS_Items.GIANT_ANCHOR));
            }
        }

        public void startMovingTo(EntityNavigation navigation, int x, int y, int z, double speed) {
            navigation.startMovingAlong(navigation.findPathTo(x, y, z, 2), speed);
        }
    }

    private boolean hasAnchor(){
        return this.getMainHandStack().isOf(TCOTS_Items.GIANT_ANCHOR);
    }

    private void launchAnchor(){
        if(this.hasAnchor()) {
            ItemStack stack = this.getMainHandStack();
            AnchorProjectileEntity anchorProjectile = new AnchorProjectileEntity(this, this.getWorld());

            anchorProjectile.setDamage(12);
            anchorProjectile.setEnchanted(stack.hasGlint());
            anchorProjectile.setVelocity(this, this.getPitch(), this.getYaw(), 0.0f, 0.4f, 1.0f);

            this.triggerAnim("AttackController", "anchor_launch");
            this.getWorld().spawnEntity(anchorProjectile);
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), TCOTS_Sounds.ANCHOR_THROW, this.getSoundCategory(),
                    1.0f, 1.0f);
            this.playSound(this.getAttackSound(), 1.0F, 1.0F);

            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("nextRetrieve", true);
            stack.getOrCreateNbt().put("State", nbt);

            this.anchorLaunchCooldownTimer=MiscUtil.getTimeInTicks(10);
            this.anchorLaunchCooldown=true;
            this.retrieveAnchorTimer=0;
        }
    }

    private void retrieveAnchor(){
        AnchorProjectileEntity anchorProjectile= this.theConjunctionOfTheSpheres$getAnchor();
        if(anchorProjectile!=null && retrieveAnchorTimer==30) {
            anchorProjectile.pickupType= PersistentProjectileEntity.PickupPermission.ALLOWED;
            anchorProjectile.dealtDamage=true;
            anchorProjectile.setPosition(this.getBlockPos().toCenterPos());
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), TCOTS_Sounds.ANCHOR_CHAIN, this.getSoundCategory(), 1.0f, 1.0f);
            anchorProjectile.discard();

            ItemStack stack = this.getMainHandStack();
            stack.removeSubNbt("State");
        }
    }


    boolean listenToSound=false;
    private static class VibrationCallback implements Vibrations.Callback {
        private static final int RANGE = 16;
        private final PositionSource positionSource;

        private final IceGiantEntity giant;

        VibrationCallback(IceGiantEntity giant) {
            this.giant=giant;
            this.positionSource = new EntityPositionSource(giant, (float)(giant.getY()));
        }

        @Override
        public int getRange() {
            return RANGE;
        }

        @Override
        public PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public TagKey<GameEvent> getTag() {
            return GameEventTags.WARDEN_CAN_LISTEN;
        }

        @Override
        public boolean accepts(ServerWorld world, BlockPos pos, GameEvent event, GameEvent.Emitter emitter) {
            if (giant.isAiDisabled() || !giant.isGiantSleeping() || giant.isDead() || giant.isGiantWakingUp()) {
                return false;
            }

            Entity entity = emitter.sourceEntity();
            return !(entity instanceof LivingEntity) || giant.canTarget((LivingEntity)entity);
        }

        @Override
        public void accept(ServerWorld world, BlockPos pos, GameEvent event, @Nullable Entity sourceEntity, @Nullable Entity entity, float distance) {
            if (giant.isDead()) {
                return;
            }
            giant.listenToSound=true;
        }
    }

    private static class WakeUpGoal extends Goal {

        private final IceGiantEntity giant;
        private final int returnTicks;

        public WakeUpGoal(IceGiantEntity giant, int returnTicks) {
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Goal.Control.JUMP));
            this.giant=giant;
            this.returnTicks=returnTicks;
        }


        @Override
        public boolean canStart() {
            return giant.isGiantSleeping() && giant.listenToSound;
        }

        @Override
        public boolean shouldContinue() {
            return giant.isGiantSleeping();
        }

        int AnimationTicks=70;
        @Override
        public void start(){
            giant.playSound(TCOTS_Sounds.ICE_GIANT_WAKE_UP, 1, 1);
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
            giant.canSleepTimer=returnTicks+giant.getRandom().nextBetween(1,501);
            giant.setIsGiantSleeping(false);
            giant.listenToSound=false;
            giant.setSleepCooldown(true);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

    }


    private static class WanderAroundGoal_Giant extends WanderAroundGoal {
        private final IceGiantEntity giant;

        public WanderAroundGoal_Giant(IceGiantEntity mob, double speed, int chance) {
            super(mob, speed, chance);
            this.giant=mob;
        }


        @Override
        public boolean canStart() {
            return super.canStart() && !giant.isGiantSleeping() && !giant.isGiantWakingUp();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !giant.isGiantSleeping() && !giant.isGiantWakingUp();
        }
    }

    private static class LookAroundGoal_Giant extends LookAroundGoal{
        private final IceGiantEntity giant;

        public LookAroundGoal_Giant(IceGiantEntity mob) {
            super(mob);
            this.giant=mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !giant.isGiantSleeping();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !giant.isGiantSleeping();
        }
    }

    public void setIsCharging(boolean isCharging) {
        this.dataTracker.set(CHARGING, isCharging);
    }

    public boolean isCharging() {
        return this.dataTracker.get(CHARGING);
    }

    public void setIsGiantSleeping(boolean isSleeping) {
        this.dataTracker.set(SLEEPING, isSleeping);
    }

    public boolean isGiantSleeping() {
        return this.dataTracker.get(SLEEPING);
    }

    public void setIsGiantWakingUp(boolean isSleeping) {
        this.dataTracker.set(WAKING_UP, isSleeping);
    }

    public boolean isGiantWakingUp() {
        return this.dataTracker.get(WAKING_UP);
    }

    public void setAnchorPos(BlockPos anchorPos) {
        this.dataTracker.set(ANCHOR_POS, anchorPos);
    }

    public BlockPos getAnchorPos() {
        return this.dataTracker.get(ANCHOR_POS);
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
                        BlockState blockState = this.getSteppingBlockState();
                        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
                            for (int i = 0; i < 32; ++i) {

                                Vec3d vec3dVelocity = new Vec3d(
                                        ((double)this.random.nextFloat()) * 0.3,
                                        0.0,
                                        ((double)this.random.nextFloat()) * 0.3)
                                        .rotateX(-this.getPitch() * ((float)Math.PI / 180))
                                        .rotateY(-this.getYaw() * ((float)Math.PI / 180));

                                Vec3d vec3dPos = new Vec3d((
                                        (double)this.random.nextFloat() - 0.5) * 0.6,
                                        (double)(-this.random.nextFloat()) * 0.01,
                                        1.6 + ((double)this.random.nextFloat() - 0.5) * 0.6)
                                        .rotateY(-this.bodyYaw * ((float)Math.PI / 180))
                                        .add(this.getX(), this.getY(), this.getZ());

                                this.getWorld().addParticle(
                                        new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState),
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
                }).setSoundKeyframeHandler(event -> this.playSound(TCOTS_Sounds.BIG_IMPACT, 1, 1))
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
    public Vec3d getLeashPos(float delta) {
        double d = 1.3 * (this.getMainArm() == Arm.RIGHT ? -1.0 : 1.0);
        float f = MathHelper.lerp(delta * 0.5f, this.getPitch(), this.prevPitch) * ((float)Math.PI / 180);
        float g = MathHelper.lerp(delta, this.prevBodyYaw, this.bodyYaw) * ((float)Math.PI / 180);
        if (this.isFallFlying() || this.isUsingRiptide()) {
            float k;
            Vec3d vec3d = this.getRotationVec(delta);
            Vec3d vec3d2 = this.getVelocity();
            double e = vec3d2.horizontalLengthSquared();
            double h = vec3d.horizontalLengthSquared();
            if (e > 0.0 && h > 0.0) {
                double i = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(e * h);
                double j = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
                k = (float)(Math.signum(j) * Math.acos(i));
            } else {
                k = 0.0f;
            }
            return this.getLerpedPos(delta).add(new Vec3d(d, -0.11, 0.85).rotateZ(-k).rotateX(-f).rotateY(-g));
        }
        if (this.isInSwimmingPose()) {
            return this.getLerpedPos(delta).add(new Vec3d(d, 0.2, -0.15).rotateX(-f).rotateY(-g));
        }
        double l = this.getBoundingBox().getLengthY() - 2.6;
        double e = this.isInSneakingPose() ? -0.2 : 0.07;
        return this.getLerpedPos(delta).add(new Vec3d(d, l, e).rotateY(-g));
    }

    @Override
    protected Box calculateBoundingBox() {
        if(this.isGiantSleeping()){
            return this.sleepingBox();
        } else {
            return super.calculateBoundingBox();
        }
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        if(this.isGiantSleeping()){
            return 1.6f;
        } else {
            return super.getActiveEyeHeight(pose, dimensions);
        }
    }

    private Box sleepingBox(){
        return new Box(this.getX() - 1.8, this.getY() + 2.6, this.getZ() - 1.5,
                this.getX() + 1.8, this.getY(), this.getZ() + 1.5);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);

        if(isGiantSleeping() || !isGiantSleeping()){
            this.setBoundingBox(this.calculateBoundingBox());
            this.calculateDimensions();
        }
    }


    private void tickFindAnchor(){

        if(this.getAnchorPos()==BlockPos.ORIGIN && !this.hasAnchor()){
            Optional<BlockPos> optional = this.findAnchor(this);
            optional.ifPresent(this::setAnchorPos);
        }

        //To avoid triggering the goal if it has already the anchor, or it's way too far from the Anchor (50+ blocks)
        if(this.hasAnchor() ||
                (this.getAnchorPos()!=BlockPos.ORIGIN &&
                this.squaredDistanceTo(this.getAnchorPos().toCenterPos()) > 250)
        ){
            this.setAnchorPos(BlockPos.ORIGIN);
        }
    }

    private Optional<BlockPos> findAnchor(PathAwareEntity entity) {
        double searchDistance = 20;

        Predicate<BlockPos> predicate = pos -> entity.getWorld().getBlockState(pos).isOf(TCOTS_Blocks.GIANT_ANCHOR);

        BlockPos blockPos = entity.getBlockPos();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        int i = 0;
        while ((double)i <= searchDistance) {
            int j = 0;
            while ((double)j < searchDistance) {
                int k = 0;
                while (k <= j) {
                    int l;
                    l = k < j && k > -j ? j : 0;
                    while (l <= j) {
                        mutable.set(blockPos, k, i - 1, l);
                        if (blockPos.isWithinDistance(mutable, searchDistance) && predicate.test(mutable)) {
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


        if (this.getWorld() instanceof ServerWorld serverWorld) {
            Vibrations.Ticker.tick(serverWorld, this.vibrationListenerData, this.vibrationCallback);
        }

        //To sync fallDistance with the client
        setFallingDistance(fallDistance);

        //Tick for jumps
        tickJump();

        tickFindAnchor();

        tickAnchorLaunch();
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

        if(this.isCharging()){
            EntitiesUtil.pushAndDamageEntities(this, 12f, 1.3, 1.2, 1.6D, IceGiantEntity.class);
        }

        //Destroys blocks to avoid suffocation when waking up
        int j;
        int i;
        if (this.isGiantWakingUp() && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            i = MathHelper.floor(this.getY());
            j = MathHelper.floor(this.getX());
            int k = MathHelper.floor(this.getZ());
            for (int l = -1; l <= 1; ++l) {
                for (int m = -1; m <= 1; ++m) {
                    for (int n = 0; n <= 4; ++n) {
                        int o = j + l;
                        int p = i + n;
                        int q = k + m;
                        BlockPos blockPos = new BlockPos(o, p, q);
                        BlockState blockState = this.getWorld().getBlockState(blockPos);
                        if (!WitherEntity.canDestroy(blockState)) continue;
                        this.getWorld().breakBlock(blockPos, true, this);
                    }
                }
            }
        }

        this.retrieveAnchorTimer();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("Charging", this.isCharging());
        nbt.putInt("ChargingCooldown", this.chargeCooldownTimer);

        nbt.putBoolean("Sleeping", this.isGiantSleeping());
        nbt.putInt("CanSleepTimer", this.canSleepTimer);
        nbt.putBoolean("SleepCooldown", this.getSleepCooldown());


        nbt.putInt("AnchorPosX", this.getAnchorPos().getX());
        nbt.putInt("AnchorPosY", this.getAnchorPos().getY());
        nbt.putInt("AnchorPosZ", this.getAnchorPos().getZ());

        Vibrations.ListenerData.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationListenerData).resultOrPartial(LOGGER::error).ifPresent(listenerData -> nbt.put("listener", listenerData));
        nbt.putBoolean("ListenToSound",this.listenToSound);

        nbt.putInt("JumpCooldown", this.jumpTicks);

        nbt.putInt("AnchorLaunchTimer", this.anchorLaunchCooldownTimer);
        nbt.putBoolean("AnchorLaunchCooldown", this.anchorLaunchCooldown);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
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

        if (nbt.contains("listener", NbtElement.COMPOUND_TYPE)) {
            Vibrations.ListenerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener")))
                    .resultOrPartial(LOGGER::error).ifPresent(listenerData -> this.vibrationListenerData = listenerData);
        }

        this.listenToSound=nbt.getBoolean("ListenToSound");

        this.jumpTicks=nbt.getInt("JumpCooldown");

        this.anchorLaunchCooldownTimer=nbt.getInt("AnchorLaunchTimer");
        this.anchorLaunchCooldown=nbt.getBoolean("AnchorLaunchCooldown");
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }



    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != TCOTS_Effects.NORTHERN_WIND_EFFECT && super.canHaveStatusEffect(effect);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !isCharging() && !isGiantSleeping();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || this.isGiantWakingUp() || this.isFalling();
    }

    @Override
    protected float modifyAppliedDamage(DamageSource source, float amount) {
        if (source.isIn(DamageTypeTags.BYPASSES_EFFECTS)) {
            return amount;
        }
        if(source.isIn(DamageTypeTags.IS_PROJECTILE)){
            return amount*0.2f;
        }

        return super.modifyAppliedDamage(source, amount);
    }

    //Jumping Stuff
    @Override
    protected float getJumpVelocity() {
        return 0.7f * this.getJumpVelocityMultiplier() + this.getJumpBoostVelocityModifier();
    }

    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return super.computeFallDamage(fallDistance, damageMultiplier) - 4;
    }

    @Override
    protected void jump() {
        this.playSound(TCOTS_Sounds.ICE_GIANT_PUNCH, 1.0f, 1.0f);
        super.jump();
    }
    private static final byte FALLING_PARTICLES = 42;
    @Override
    public void onLanding() {
        if(isFalling()){
            setIsFalling(false);
            if(this.getWorld().getBlockState(this.getBlockPos().down()).getFluidState().isOf(Fluids.EMPTY)
                    && !this.getWorld().getBlockState(this.getBlockPos()).isIn(TCOTS_Blocks.NEGATES_DEVOURER_JUMP)
                    && !this.getWorld().getBlockState(this.getBlockPos().down()).isIn(TCOTS_Blocks.NEGATES_DEVOURER_JUMP))
            {
                this.triggerAnim("LandingController","landing");
                EntitiesUtil.pushAndDamageEntities(this, 8 + (fallDistance*2f), 4.0f + (fallDistance * 0.5f), 3, 3.0);
                this.playSound(TCOTS_Sounds.BIG_IMPACT, 1.0f, 1.0f);
                this.getWorld().sendEntityStatus(this, FALLING_PARTICLES);
            }
        }

        super.onLanding();
    }
    @Override
    public void handleStatus(byte status) {
        if(status==FALLING_PARTICLES){
            EntitiesUtil.spawnImpactParticles(this, 1.9975f + (4.0f + (getFallingDistance() * 0.5f)), getFallingDistance());
        } else{
            super.handleStatus(status);
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
        this.dataTracker.set(FALLING, isFalling);
    }

    public boolean isFalling() {
        return this.dataTracker.get(FALLING);
    }

    public void setFallingDistance(float fallingDistance) {
        this.dataTracker.set(FALLING_DISTANCE, fallingDistance);
    }

    public double getFallingDistance() {
        return this.dataTracker.get(FALLING_DISTANCE);
    }

    //Sounds
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.isGiantWakingUp()) {
            if (this.isGiantSleeping()) {
                return TCOTS_Sounds.ICE_GIANT_SNORE;
            } else {
                return TCOTS_Sounds.ICE_GIANT_IDLE;
            }
        }
        else {
            return null;
        }
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.ICE_GIANT_ATTACK;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.ICE_GIANT_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.ICE_GIANT_DEATH;
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.GuardNestMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;

public class GhoulEntity extends NecrophageMonster implements GeoEntity, LungeMob, Ownable, GuardNestMob {

    //xTODO: Add new combat/regeneration
    //xTODO: Add Ghoul's Blood
    //xTODO: Add monster nests & spawn
    public static final byte GHOUL_REGENERATING = 99;
    public final int GHOUL_REGENERATION_TIME=200;
    private static final TrackedData<BlockPos> NEST_POS = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected static final TrackedData<Boolean> CAN_HAVE_NEST = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Nullable
    private MobEntity owner;
    @Nullable
    private UUID ownerUuid;

    public int getGHOUL_REGENERATION_TIME() {
        return GHOUL_REGENERATION_TIME;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation START_REGEN = RawAnimation.begin().thenPlay("special.regen");

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> REGENERATING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVOKING_REGENERATING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> TIME_FOR_REGEN = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> EATING_TIME = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public GhoulEntity(EntityType<? extends GhoulEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
    }

    @Override
    public int getMaxHeadRotation() {
        return 50;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
        this.dataTracker.startTracking(REGENERATING, Boolean.FALSE);
        this.dataTracker.startTracking(INVOKING_REGENERATING, Boolean.FALSE);
        this.dataTracker.startTracking(TIME_FOR_REGEN, 0);
        this.dataTracker.startTracking(NEST_POS, BlockPos.ORIGIN);
        this.dataTracker.startTracking(CAN_HAVE_NEST, Boolean.FALSE);
        this.dataTracker.startTracking(EATING_TIME, -1);
    }

    @Override
    public int getMaxLookPitchChange() {
        return 15;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new GhoulRegeneration(this, 60, 400, GHOUL_REGENERATION_TIME,
                0.5f,35));

        this.goalSelector.add(2, new LungeAttackGoal(this, 100, 1.2,10,30));

        this.goalSelector.add(3, new Ghoul_MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.add(4, new GhoulGoForFlesh(this,1D));

        this.goalSelector.add(5, new ReturnToNestGoal(this,0.75));

        this.goalSelector.add(6, new FollowMonsterOwnerGoal(this, 0.75));

        this.goalSelector.add(7, new WanderAroundGoal(this, 0.75f,80));

        this.goalSelector.add(8, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.add(1, new AttackOwnerEnemyTarget(this));

        this.targetSelector.add(2, new RevengeGoal(this, GhoulEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZombieEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZoglinEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZombieHorseEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        this.targetSelector.add(5, new ActiveTargetGoal<>(this, HoglinEntity.class, true));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, CowEntity.class, true));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, PigEntity.class, true));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, SheepEntity.class, true));
        this.targetSelector.add(7, new ActiveTargetGoal<>(this, GoatEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 18.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.29f);
    }

    @Override
    public boolean getExtraReasonToNotGoToNest() {
        List<ItemEntity> listFlesh = this.getWorld().getEntitiesByClass(ItemEntity.class,
                this.getBoundingBox().expand(8.0, 2.0, 8.0), itemEntity -> GhoulEntity.isEdibleMeat(itemEntity.getStack()));

        return this.getOwner()==null && listFlesh.isEmpty();
    }

    @Override
    public BlockPos getNestPos() {
        return this.dataTracker.get(NEST_POS);
    }

    @Override
    public void setNestPos(BlockPos pos) {
        this.dataTracker.set(NEST_POS, pos);
    }

    @Override
    public boolean canHaveNest() {
        return this.dataTracker.get(CAN_HAVE_NEST);
    }

    @Override
    public void setCanHaveNest(boolean canHaveNest) {
        this.dataTracker.set(CAN_HAVE_NEST, canHaveNest);
    }
    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1, 1, 1);
    @Override
    protected Vec3i getItemPickUpRangeExpander() {
        return ITEM_PICKUP_RANGE_EXPANDER;
    }

    protected static class GhoulRegeneration extends Goal {
        private final int ticksAttackedBeforeRegen;
        private final int CooldownBetweenRegens;
        private final int TimeForRegen;
        private final GhoulEntity mob;
        private final int stopTicks;
        protected final float healthPercentage;

        public GhoulRegeneration(GhoulEntity mob, int ticksAttackedBeforeRegen, int CooldownBetweenRegens, int TimeForRegen, float HealthPercentageToStart, int stopTicks) {
            this.ticksAttackedBeforeRegen = ticksAttackedBeforeRegen;
            this.mob=mob;
            this.CooldownBetweenRegens=CooldownBetweenRegens;
            this.TimeForRegen=TimeForRegen;
            this.healthPercentage=HealthPercentageToStart;
            this.stopTicks=stopTicks;
        }

        @Override
        public boolean canStart() {
            return canStartRegen() && !MoonDustBomb.checkEffect(mob);
        }

        @Override
        public boolean shouldContinue() {
            return !mob.getIsRegenerating() && !MoonDustBomb.checkEffect(mob);
        }

        private int StoppedTicks;
        @Override
        public void start() {
            StoppedTicks=stopTicks;
            mob.setIsInvokingRegen(true);
            mob.getNavigation().stop();
            mob.playSound(mob.getScreamSound(),1,1);
            mob.triggerAnim("RegenController", "start_regen");
        }

        @Override
        public void stop() {
            this.mob.setIsRegenerating(!MoonDustBomb.checkEffect(mob));
            mob.setTimeForRegen(TimeForRegen);
            if (!this.mob.isSilent()) {
                this.mob.getWorld().sendEntityStatus(this.mob, GHOUL_REGENERATING);
            }

            mob.setHasCooldownForRegen(true);
            mob.setCooldownForRegen(CooldownBetweenRegens);

            StoppedTicks=stopTicks;
            mob.setIsInvokingRegen(false);
        }
        @Override
        public void tick() {
            if (StoppedTicks > 0) {
                mob.getNavigation().stop();
                --StoppedTicks;
            } else {
                stop();
            }
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        protected boolean canStartRegen(){
            return ((mob.getLastAttackedTime() + ticksAttackedBeforeRegen) < mob.age) && !(mob.getIsRegenerating())
                    && (mob.getHealth() < (mob.getMaxHealth() * healthPercentage)) && mob.isOnGround() && !(mob.hasCooldownForRegen());
        }
    }

    protected static class Ghoul_MeleeAttackGoal extends MeleeAttackGoal_Animated {

        public Ghoul_MeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !((GhoulEntity)mob).getIsInvokingRegen();
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !((GhoulEntity)mob).getIsInvokingRegen();
        }
    }

    protected static class GhoulGoForFlesh extends Goal {

        private final GhoulEntity ghoul;

        private final double speed;

        public GhoulGoForFlesh(GhoulEntity ghoul, double speed){
            this.ghoul=ghoul;
            this.speed=speed;
        }

        @Override
        public boolean canStart() {
            List<ItemEntity> list = searchFleshList();

            return !list.isEmpty() && ghoul.getStackInHand(Hand.MAIN_HAND).isOf(ItemStack.EMPTY.getItem());
        }

        @Override
        public boolean shouldContinue() {
            List<ItemEntity> list = searchFleshList();

            return !list.isEmpty() && ghoul.getStackInHand(Hand.MAIN_HAND).isOf(ItemStack.EMPTY.getItem());
        }

        @Override
        public void start() {
            List<ItemEntity> list = searchFleshList();

            if (!list.isEmpty() && ghoul.getStackInHand(Hand.MAIN_HAND).isOf(ItemStack.EMPTY.getItem())) {
                this.startMovingTo(ghoul.getNavigation(), list.get(0), speed);
            }
        }

        @Override
        public void tick() {
            List<ItemEntity> list = searchFleshList();

            if (!list.isEmpty() && ghoul.getStackInHand(Hand.MAIN_HAND).isOf(ItemStack.EMPTY.getItem())) {
                this.startMovingTo(ghoul.getNavigation(), list.get(0), speed);
                ghoul.getLookControl().lookAt(list.get(0), 30.0f, 30.0f);
            }
        }

        public void startMovingTo(EntityNavigation navigation, Entity entity, double speed) {
            Path path = navigation.findPathTo(entity, 0);
            if (path != null) {
                navigation.startMovingAlong(path, speed);
            }
        }


        private List<ItemEntity> searchFleshList(){
            return
                    this.ghoul.getWorld().getEntitiesByClass(ItemEntity.class,
                    this.ghoul.getBoundingBox().expand(8.0, 2.0, 8.0), itemEntity -> GhoulEntity.isEdibleMeat(itemEntity.getStack()));
        }
    }

    protected int getTotalEatingTime(){
        return 20+this.getRandom().nextBetween(0,10);
    }

    public void setEatingTime(int eatingTime) {
        this.dataTracker.set(EATING_TIME, eatingTime);
    }
    public int getEatingTime() {
        return this.dataTracker.get(EATING_TIME);
    }

    public void addEatingTime(){
        this.setEatingTime(getEatingTime()+1);
    }


    @Override
    public boolean canGather(ItemStack stack) {
        return isEdibleMeat(stack) && this.getStackInHand(Hand.MAIN_HAND).isOf(ItemStack.EMPTY.getItem());
    }

    private static boolean isEdibleMeat(ItemStack stack){
        return stack.isOf(Items.ROTTEN_FLESH)
                || (stack.isOf(Items.BEEF))
                || (stack.isOf(Items.PORKCHOP))
                || (stack.isOf(Items.MUTTON));
    }

    @Override
    protected void loot(ItemEntity item) {
        this.triggerItemPickedUpByEntityCriteria(item);
        this.handleItemFromGround(item);
    }

    private void handleItemFromGround(ItemEntity item){
        this.sendPickup(item, 1);
        ItemStack itemStack = EntitiesUtil.getItemFromStack(item);
        this.setStackInHand(Hand.MAIN_HAND, itemStack);
        this.setEatingTime(0);
    }

    private void tickEatingItem(){

        if(this.getEatingTime() < this.getTotalEatingTime() && this.getEatingTime()!=-1){
            ItemStack foodStack = this.getStackInHand(Hand.MAIN_HAND);
            this.addEatingTime();
            if (foodStack.getUseAction() == UseAction.EAT && this.getEatingTime()%5==0) {
                this.spawnItemParticles(foodStack);
                this.playSound(this.getEatSound(foodStack), 0.5f + 0.5f * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }

        } else if (this.getEatingTime() == this.getTotalEatingTime()){
            this.handleEndsFeed(this.getStackInHand(Hand.MAIN_HAND));
        }
    }

    private void handleEndsFeed(ItemStack foodStack) {
        if(!this.getWorld().isClient) {

            if(foodStack.getFoodComponent()!=null){
                this.heal((float) foodStack.getFoodComponent().getHunger() /2);
            }
            this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

            this.setEatingTime(-1);
        }
    }

        @Override
    public boolean isPushable() {
        if(this.getIsInvokingRegen()){
            return false;
        }
        return super.isPushable();
    }

    public static boolean canSpawnGhoul(EntityType<? extends NecrophageMonster> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if(spawnReason == SpawnReason.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL;
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random) &&
                    pos.getY() >= 0;
        }
    }

    @Nullable
    @Override
    public EntityData initialize(@NotNull ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Random random = world.getRandom();
        if(!(spawnReason == SpawnReason.SPAWN_EGG) && !(spawnReason == SpawnReason.STRUCTURE)) {
            //Can spawn an Alghoul with it instead
            if (random.nextInt() % 5 == 0) {
                AlghoulEntity alghoul = TCOTS_Entities.ALGHOUL.create(this.getWorld());
                if (alghoul != null) {
                    alghoul.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                    this.getWorld().spawnEntity(alghoul);
                }
            }
        }

        if(spawnReason==SpawnReason.SPAWNER || spawnReason==SpawnReason.STRUCTURE){
            this.setCanHaveNest(true);
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, GeoControllersUtil::idleWalkRunController));


        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
        );

        //Lunge Controller
        lungeAnimationController(this, controllerRegistrar);

        //RegenAnimation Controller
        controllerRegistrar.add(new AnimationController<>(this, "RegenController", 1, state -> PlayState.STOP)
                .triggerableAnim("start_regen", START_REGEN)
        );
    }

    @Nullable
    @Override
    public Entity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld && (entity = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (MobEntity) entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable MobEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    boolean hasCooldownForRegen=false;

    public boolean hasCooldownForRegen() {
        return hasCooldownForRegen;
    }

    public void setHasCooldownForRegen(boolean hasCooldownForRegen) {
        this.hasCooldownForRegen = hasCooldownForRegen;
    }

    protected void spawnItemParticles(ItemStack stack){
        for (int i = 0; i < 5; ++i) {
            Vec3d vec3dVelocity = new Vec3d(
                    ((double) this.random.nextFloat() - 0.5) * 0.1,
                    Math.random() * 0.1 + 0.1,
                    0.0)
                    .rotateX(-this.getPitch() * ((float) Math.PI / 180))
                    .rotateY(-this.getYaw() * ((float) Math.PI / 180));


            Vec3d vec3dPos = new Vec3d((
                    (double)this.random.nextFloat() - 0.5) * 0.1,
                    (double)(-this.random.nextFloat()) * 0.01,
                    0.95 + ((double)this.random.nextFloat() - 0.5) * 0.1)
                    .rotateY(-this.bodyYaw * ((float)Math.PI / 180))
                    .add(this.getX(), this.getEyeY() - 0.15, this.getZ());

            this.getWorld().addParticle(
                    new ItemStackParticleEffect(ParticleTypes.ITEM, stack),
                    vec3dPos.x,
                    vec3dPos.y,
                    vec3dPos.z,

                    vec3dVelocity.x,
                    vec3dVelocity.y + 0.05,
                    vec3dVelocity.z);
        }
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.62f;
    }

    @Override
    public void tick() {
        if(getIsRegenerating() && MoonDustBomb.checkEffect(this)){
            setIsRegenerating(false);
        }

        this.tickLunge();

        if(getIsRegenerating() && (this.age%10==0) && this.getHealth() < this.getMaxHealth()){
            this.heal(1);
        }

        timersTick();

        //To disable the nest
        this.tickGuardNest(this);

        this.tickEatingItem();


        super.tick();
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        if(!(this instanceof AlghoulEntity) && this.getOwner()==null){
            List<AlghoulEntity> list =
            this.getWorld().getEntitiesByClass(AlghoulEntity.class, this.getBoundingBox().expand(10,10,10),
            alghoul -> true);

            if(!list.isEmpty()){
                this.setOwner(list.get(0));
            }
        }

        if(this.getOwner() != null && !this.getOwner().isAlive())
            setOwner(null);
    }

    private void timersTick(){
        if(this.getTimeForRegen() > 0){
            this.setTimeForRegen(getTimeForRegen()-1);
        } else if (getIsRegenerating()) {
            this.setIsRegenerating(false);
        }

        if(this.getCooldownForRegen()>0){
            this.setCooldownForRegen(this.getCooldownForRegen()-1);
        } else if (hasCooldownForRegen()) {
            this.setHasCooldownForRegen(false);
        }
    }

    public boolean cooldownBetweenLunges=false;
    @Override
    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

    @Override
    public void setCooldownBetweenLunges(boolean cooldownBetweenLunges) {
        this.cooldownBetweenLunges=cooldownBetweenLunges;
    }

    public boolean getIsRegenerating() {
        return this.dataTracker.get(REGENERATING);
    }

    public void setIsRegenerating(boolean isRegenerating) {
        this.dataTracker.set(REGENERATING, isRegenerating);
    }

    public boolean getIsInvokingRegen() {
        return this.dataTracker.get(INVOKING_REGENERATING);
    }

    public void setIsInvokingRegen(boolean isRegenerating) {
        this.dataTracker.set(INVOKING_REGENERATING, isRegenerating);
    }

    private int cooldownForRegen;

    public void setCooldownForRegen(int cooldownForRegen) {
        this.cooldownForRegen = cooldownForRegen;
    }

    public int getCooldownForRegen() {
        return cooldownForRegen;
    }

    protected void setTimeForRegen(int timeForRegen) {
        this.dataTracker.set(TIME_FOR_REGEN, timeForRegen);
    }

    protected int getTimeForRegen() {
        return this.dataTracker.get(TIME_FOR_REGEN);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Regeneration",getIsRegenerating());
        nbt.putInt("CooldownRegen", getCooldownForRegen());
        nbt.putBoolean("CooldownRegenActive", hasCooldownForRegen);
        nbt.putInt("RegenerationTime", getTimeForRegen());

        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }

        nbt.putInt("EatingTime", this.getEatingTime());

        this.writeNbtGuardNest(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.setIsRegenerating(nbt.getBoolean("Regeneration"));
        this.setCooldownForRegen(nbt.getInt("CooldownRegen"));
        this.setTimeForRegen(nbt.getInt("RegenerationTime"));
        this.setHasCooldownForRegen(nbt.getBoolean("CooldownRegenActive"));

        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }

        setEatingTime(nbt.getInt("EatingTime"));

        this.readNbtGuardNest(nbt);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.GHOUL_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.GHOUL_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.GHOUL_IDLE;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.GHOUL_LUNGES;
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.GHOUL_ATTACK;
    }

    protected SoundEvent getScreamSound() {
        return TCOTS_Sounds.GHOUL_SCREAMS;
    }

    public SoundEvent getRegeneratingSound(){
        return TCOTS_Sounds.GHOUL_REGEN;
    }

    public int LungeTicks;
    @Override
    public int getLungeTicks() {
        return LungeTicks;
    }

    @Override
    public void setLungeTicks(int lungeTicks) {
        LungeTicks=lungeTicks;
    }

    public void playSpawnEffects() {
        if (this.getWorld().isClient) {
            BlockState blockState = this.getSteppingBlockState();
            if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {

                for (int i = 0; i < 40; ++i) {
                    double d = this.getX() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);
                    double e = this.getY()+0.5;
                    double f = this.getZ() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);

                    this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_SPAWN_EFFECTS);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

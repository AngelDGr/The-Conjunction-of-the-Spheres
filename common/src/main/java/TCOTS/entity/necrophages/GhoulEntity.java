package TCOTS.entity.necrophages;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.entity.goals.*;
import TCOTS.entity.goals.FollowMonsterOwnerGoal;
import TCOTS.entity.goals.LungeAttackGoal;
import TCOTS.entity.goals.ReturnToNestGoal;
import TCOTS.entity.interfaces.GuardNestMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TraceableEntity;
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
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GhoulEntity extends NecrophageMonster implements GeoEntity, LungeMob, TraceableEntity, GuardNestMob {

    //xTODO: Add new combat/regeneration
    //xTODO: Add Ghoul's Blood
    //xTODO: Add monster nests & spawn
    public static final byte GHOUL_REGENERATING = 99;
    public final int GHOUL_REGENERATION_TIME=200;
    private static final EntityDataAccessor<BlockPos> NEST_POS = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.BLOCK_POS);
    protected static final EntityDataAccessor<Boolean> CAN_HAVE_NEST = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private Mob owner;
    @Nullable
    private UUID ownerUuid;

    public int getRegenerationTime() {
        return GHOUL_REGENERATION_TIME;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation START_REGEN = RawAnimation.begin().thenPlay("special.regen");

    protected static final EntityDataAccessor<Boolean> LUGGING = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> REGENERATING = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> INVOKING_REGENERATING = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> TIME_FOR_REGEN = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> EATING_TIME = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.INT);


    public GhoulEntity(EntityType<? extends GhoulEntity> entityType, Level world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
    }

    @Override
    public int getMaxHeadYRot() {
        return 50;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LUGGING, Boolean.FALSE);
        builder.define(REGENERATING, Boolean.FALSE);
        builder.define(INVOKING_REGENERATING, Boolean.FALSE);
        builder.define(TIME_FOR_REGEN, 0);
        builder.define(NEST_POS, BlockPos.ZERO);
        builder.define(CAN_HAVE_NEST, Boolean.FALSE);
        builder.define(EATING_TIME, -1);
    }

    @Override
    public int getMaxHeadXRot() {
        return 15;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new GhoulRegeneration(this, 60, 400, GHOUL_REGENERATION_TIME,
                0.5f,35));

        this.goalSelector.addGoal(2, new LungeAttackGoal(this, 100, 1.2,10,30));

        this.goalSelector.addGoal(3, new Ghoul_MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.addGoal(4, new GhoulGoForFlesh(this,1D));

        this.goalSelector.addGoal(5, new ReturnToNestGoal(this,0.75));

        this.goalSelector.addGoal(6, new FollowMonsterOwnerGoal(this, 0.75));

        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.75f,80));

        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        //Objectives
        this.targetSelector.addGoal(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.addGoal(1, new AttackOwnerEnemyTarget(this));

        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, GhoulEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Zoglin.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, ZombieHorse.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));

        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Hoglin.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Cow.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Pig.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Goat.class, true));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 18.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.29f);
    }

    @Override
    public boolean getExtraReasonToNotGoToNest() {
        List<ItemEntity> listFlesh = this.level().getEntitiesOfClass(ItemEntity.class,
                this.getBoundingBox().inflate(8.0, 2.0, 8.0), itemEntity -> GhoulEntity.isEdibleMeat(itemEntity.getItem()));

        return this.getOwner()==null && listFlesh.isEmpty();
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
        return this.entityData.get(CAN_HAVE_NEST);
    }

    @Override
    public void setCanHaveNest(boolean canHaveNest) {
        this.entityData.set(CAN_HAVE_NEST, canHaveNest);
    }
    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1, 1, 1);
    @Override
    protected @NotNull Vec3i getPickupReach() {
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
        public boolean canUse() {
            return canStartRegen() && !MoonDustBomb.checkEffectAndSplinters(mob);
        }

        @Override
        public boolean canContinueToUse() {
            return !mob.getIsRegenerating() && !MoonDustBomb.checkEffectAndSplinters(mob);
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
            this.mob.setIsRegenerating(!MoonDustBomb.checkEffectAndSplinters(mob));
            mob.setTimeForRegen(TimeForRegen);
            if (!this.mob.isSilent()) {
                this.mob.level().broadcastEntityEvent(this.mob, GHOUL_REGENERATING);
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
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        protected boolean canStartRegen(){
            return ((mob.getLastHurtByMobTimestamp() + ticksAttackedBeforeRegen) < mob.tickCount) && !(mob.getIsRegenerating())
                    && (mob.getHealth() < (mob.getMaxHealth() * healthPercentage)) && mob.onGround() && !(mob.hasCooldownForRegen());
        }
    }

    protected static class Ghoul_MeleeAttackGoal extends MeleeAttackGoal_Animated {

        public Ghoul_MeleeAttackGoal(PathfinderMob mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !((GhoulEntity)mob).getIsInvokingRegen();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !((GhoulEntity)mob).getIsInvokingRegen();
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
        public boolean canUse() {
            List<ItemEntity> list = searchFleshList();

            return !list.isEmpty() && ghoul.getItemInHand(InteractionHand.MAIN_HAND).is(ItemStack.EMPTY.getItem())
                    && ghoul.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        }

        @Override
        public boolean canContinueToUse() {
            List<ItemEntity> list = searchFleshList();

            return !list.isEmpty() && ghoul.getItemInHand(InteractionHand.MAIN_HAND).is(ItemStack.EMPTY.getItem())
                    && ghoul.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        }

        @Override
        public void start() {
            List<ItemEntity> list = searchFleshList();

            if (!list.isEmpty() && ghoul.getItemInHand(InteractionHand.MAIN_HAND).is(ItemStack.EMPTY.getItem())
                    && ghoul.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                this.startMovingTo(ghoul.getNavigation(), list.getFirst(), speed);
            }
        }

        @Override
        public void tick() {
            List<ItemEntity> list = searchFleshList();

            if (!list.isEmpty() && ghoul.getItemInHand(InteractionHand.MAIN_HAND).is(ItemStack.EMPTY.getItem())) {
                this.startMovingTo(ghoul.getNavigation(), list.getFirst(), speed);
                ghoul.getLookControl().setLookAt(list.getFirst(), 30.0f, 30.0f);
            }
        }

        public void startMovingTo(PathNavigation navigation, Entity entity, double speed) {
            Path path = navigation.createPath(entity, 0);
            if (path != null) {
                navigation.moveTo(path, speed);
            }
        }


        private List<ItemEntity> searchFleshList(){
            return
                    this.ghoul.level().getEntitiesOfClass(ItemEntity.class,
                    this.ghoul.getBoundingBox().inflate(8.0, 2.0, 8.0), itemEntity -> GhoulEntity.isEdibleMeat(itemEntity.getItem()));
        }
    }

    protected int getTotalEatingTime(){
        return 20+this.getRandom().nextIntBetweenInclusive(0,10);
    }

    public void setEatingTime(int eatingTime) {
        this.entityData.set(EATING_TIME, eatingTime);
    }
    public int getEatingTime() {
        return this.entityData.get(EATING_TIME);
    }

    public void addEatingTime(){
        this.setEatingTime(getEatingTime()+1);
    }


    @Override
    public boolean wantsToPickUp(@NotNull ItemStack stack) {
        return this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && isEdibleMeat(stack) && this.getItemInHand(InteractionHand.MAIN_HAND).is(ItemStack.EMPTY.getItem());
    }

    private static boolean isEdibleMeat(ItemStack stack){
        return stack.is(Items.ROTTEN_FLESH)
                || (stack.is(Items.BEEF))
                || (stack.is(Items.PORKCHOP))
                || (stack.is(Items.MUTTON));
    }

    @Override
    protected void pickUpItem(@NotNull ItemEntity item) {
        this.onItemPickup(item);
        this.handleItemFromGround(item);
    }

    private void handleItemFromGround(ItemEntity item){
        this.take(item, 1);
        ItemStack itemStack = EntitiesUtil.getItemFromStack(item);
        this.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        this.setEatingTime(0);
    }

    private void tickEatingItem(){

        if(this.getEatingTime() < this.getTotalEatingTime() && this.getEatingTime()!=-1){
            ItemStack foodStack = this.getItemInHand(InteractionHand.MAIN_HAND);
            this.addEatingTime();
            if (foodStack.getUseAnimation() == UseAnim.EAT && this.getEatingTime()%5==0) {
                this.spawnItemParticles(foodStack);
                this.playSound(this.getEatingSound(foodStack), 0.5f + 0.5f * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }

        } else if (this.getEatingTime() == this.getTotalEatingTime()){
            this.handleEndsFeed(this.getItemInHand(InteractionHand.MAIN_HAND));
        }
    }

    private void handleEndsFeed(ItemStack foodStack) {
        if(!this.level().isClientSide) {

            if(foodStack.has(DataComponents.FOOD)){

                this.heal((float) Objects.requireNonNull(foodStack.get(DataComponents.FOOD)).nutrition() /2);

            }
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

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

    public static boolean canSpawnGhoul(EntityType<? extends NecrophageMonster> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        if(spawnReason == MobSpawnType.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL;
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random) &&
                    pos.getY() >= 0;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        RandomSource random = world.getRandom();
        if(!(spawnReason == MobSpawnType.SPAWN_EGG) && !(spawnReason == MobSpawnType.STRUCTURE)) {
            //Can spawn an Alghoul with it instead
            if (random.nextInt() % 5 == 0) {
                AlghoulEntity alghoul = TCOTS_Entities.Alghoul().create(this.level());
                if (alghoul != null) {
                    alghoul.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0f);
                    this.level().addFreshEntity(alghoul);
                }
            }
        }

        if(spawnReason==MobSpawnType.SPAWNER || spawnReason==MobSpawnType.STRUCTURE){
            this.setCanHaveNest(true);
        }

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
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
        if (this.owner == null && this.ownerUuid != null && this.level() instanceof ServerLevel && (entity = ((ServerLevel)this.level()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (Mob) entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable Mob owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUUID();
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
            Vec3 vec3dVelocity = new Vec3(
                    ((double) this.random.nextFloat() - 0.5) * 0.1,
                    Math.random() * 0.1 + 0.1,
                    0.0)
                    .xRot(-this.getXRot() * ((float) Math.PI / 180))
                    .yRot(-this.getYRot() * ((float) Math.PI / 180));


            Vec3 vec3dPos = new Vec3((
                    (double)this.random.nextFloat() - 0.5) * 0.1,
                    (double)(-this.random.nextFloat()) * 0.01,
                    0.95 + ((double)this.random.nextFloat() - 0.5) * 0.1)
                    .yRot(-this.yBodyRot * ((float)Math.PI / 180))
                    .add(this.getX(), this.getEyeY() - 0.15, this.getZ());

            this.level().addParticle(
                    new ItemParticleOption(ParticleTypes.ITEM, stack),
                    vec3dPos.x,
                    vec3dPos.y,
                    vec3dPos.z,

                    vec3dVelocity.x,
                    vec3dVelocity.y + 0.05,
                    vec3dVelocity.z);
        }
    }

    @Override
    public void tick() {
        if(getIsRegenerating() && MoonDustBomb.checkEffectAndSplinters(this)){
            setIsRegenerating(false);
        }

        this.tickLunge();

        if(getIsRegenerating() && (this.tickCount%10==0) && this.getHealth() < this.getMaxHealth()){
            this.heal(1);
        }

        timersTick();

        //To disable the nest
        this.tickGuardNest(this);

        this.tickEatingItem();


        super.tick();
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if(!(this instanceof AlghoulEntity) && this.getOwner()==null){
            List<AlghoulEntity> list =
            this.level().getEntitiesOfClass(AlghoulEntity.class, this.getBoundingBox().inflate(10,10,10),
            alghoul -> true);

            if(!list.isEmpty()){
                this.setOwner(list.getFirst());
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
        return this.entityData.get(REGENERATING);
    }

    public void setIsRegenerating(boolean isRegenerating) {
        this.entityData.set(REGENERATING, isRegenerating);
    }

    public boolean getIsInvokingRegen() {
        return this.entityData.get(INVOKING_REGENERATING);
    }

    public void setIsInvokingRegen(boolean isRegenerating) {
        this.entityData.set(INVOKING_REGENERATING, isRegenerating);
    }

    private int cooldownForRegen;

    public void setCooldownForRegen(int cooldownForRegen) {
        this.cooldownForRegen = cooldownForRegen;
    }

    public int getCooldownForRegen() {
        return cooldownForRegen;
    }

    protected void setTimeForRegen(int timeForRegen) {
        this.entityData.set(TIME_FOR_REGEN, timeForRegen);
    }

    protected int getTimeForRegen() {
        return this.entityData.get(TIME_FOR_REGEN);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Regeneration",getIsRegenerating());
        nbt.putInt("CooldownRegen", getCooldownForRegen());
        nbt.putBoolean("CooldownRegenActive", hasCooldownForRegen);
        nbt.putInt("RegenerationTime", getTimeForRegen());

        if (this.ownerUuid != null) {
            nbt.putUUID("Owner", this.ownerUuid);
        }

        nbt.putInt("EatingTime", this.getEatingTime());

        this.writeNbtGuardNest(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.setIsRegenerating(nbt.getBoolean("Regeneration"));
        this.setCooldownForRegen(nbt.getInt("CooldownRegen"));
        this.setTimeForRegen(nbt.getInt("RegenerationTime"));
        this.setHasCooldownForRegen(nbt.getBoolean("CooldownRegenActive"));

        if (nbt.hasUUID("Owner")) {
            this.ownerUuid = nbt.getUUID("Owner");
        }

        setEatingTime(nbt.getInt("EatingTime"));

        this.readNbtGuardNest(nbt);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("ghoul_hurt");
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("ghoul_death");
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.getSoundEvent("ghoul_idle");
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.getSoundEvent("ghoul_lunge");
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("ghoul_attack");
    }

    protected SoundEvent getScreamSound() {
        return TCOTS_Sounds.getSoundEvent("ghoul_scream");
    }

    public SoundEvent getRegeneratingSound(){
        return TCOTS_Sounds.getSoundEvent("ghoul_regen");
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

    public void spawnAnim() {
        if (this.level().isClientSide) {
            BlockState blockState = this.getBlockStateOn();
            if (blockState.getRenderShape() != RenderShape.INVISIBLE) {

                for (int i = 0; i < 40; ++i) {
                    double d = this.getX() + (double) Mth.randomBetween(random, -0.7F, 0.7F);
                    double e = this.getY()+0.5;
                    double f = this.getZ() + (double) Mth.randomBetween(random, -0.7F, 0.7F);

                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
        } else {
            this.level().broadcastEntityEvent(this, EntityEvent.SILVERFISH_MERGE_ANIM);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

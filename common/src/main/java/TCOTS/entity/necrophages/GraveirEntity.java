package TCOTS.entity.necrophages;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.registry.TCOTS_Effects;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class GraveirEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob {
    //xTODO: Add sounds
    //xTODO: Add spawn
    //xTODO: Add bestiary
    //xTODO: Add drop
    //xTODO: Add new attack

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    protected static final EntityDataAccessor<Boolean> InGROUND = SynchedEntityData.defineId(GraveirEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EMERGING = SynchedEntityData.defineId(GraveirEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(GraveirEntity.class, EntityDataSerializers.BOOLEAN);
//    protected static final TrackedData<Boolean> PUNCHING = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public GraveirEntity(EntityType<? extends GraveirEntity> entityType, Level world) {
        super(entityType, world);
        xpReward=20;
    }

    @Override
    public int getMaxHeadYRot() {
        return 35;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 9.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.20f)

                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
                .add(Attributes.ATTACK_KNOCKBACK,1.5)

                .add(Attributes.ARMOR, 8f)
                .add(Attributes.ARMOR_TOUGHNESS, 8f);
    }


    @Override
    protected void registerGoals() {
        //Emerge from ground
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 600));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        //Returns to ground
        this.goalSelector.addGoal(2, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.addGoal(3, new MeleeAttackGoal_Excavator(this, 1.2D, false, 3600));

        this.goalSelector.addGoal(4, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.addGoal(5, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, GraveirEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(InGROUND, Boolean.FALSE);
        builder.define(EMERGING, Boolean.FALSE);
        builder.define(INVISIBLE, Boolean.FALSE);
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

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }


    //Explosion Damage Resistance
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)){
            amount=amount/16;
        }

        return super.hurt(source, amount);
    }

    //Excavator Common
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("InGround", this.entityData.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.entityData.get(INVISIBLE));
    }

    //Cadaverine damage
    @Override
    public boolean doHurtTarget(Entity target) {
        boolean bl = super.doHurtTarget(target);
        if(target instanceof LivingEntity living && bl && this.random.nextInt()%3==0){
            living.addEffect(new MobEffectInstance(TCOTS_Effects.Cadaverine(), 160), this);
        }

        if(target instanceof Player player && player.isBlocking()){
//            if(player.getOffHandStack().getItem() instanceof ShieldItem)
//                player.getOffHandStack().damage(10, random, (ServerPlayerEntity) (player));

            player.getOffhandItem().hurtAndBreak(10, player, getSlotForHand(player.getUsedItemHand()));
        }

        return bl;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
    }

    @Override
    public void tick() {
        //Counter for particles
        this.tickExcavator(this);

        super.tick();
    }

    @Override
    protected void customServerAiStep() {
        mobTickExcavator(
                List.of(BlockTags.DIRT, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                List.of(Blocks.SAND),
                this
        );

        this.setInvisible(this.getInvisibleData());
        super.customServerAiStep();
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        if (getInGround()) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.makeBoundingBox();
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGround() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.getIsEmerging() && !this.getInGround();
    }

    @Override
    public boolean fireImmune() {
        return super.fireImmune() || this.getInGround();
    }

    int AnimationParticlesTicks=36;

    @Override
    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    @Override
    public void setAnimationParticlesTicks(int animationParticlesTicks) {
        AnimationParticlesTicks=animationParticlesTicks;
    }

    @Override
    public boolean getInGround() {
        return this.entityData.get(InGROUND);
    }

    @Override
    public void setInGround(boolean wasInGround) {
        this.entityData.set(InGROUND, wasInGround);
    }

    @Override
    public boolean getIsEmerging() {
        return this.entityData.get(EMERGING);
    }

    @Override
    public void setIsEmerging(boolean wasEmerging) {
        this.entityData.set(EMERGING, wasEmerging);
    }
    public int ReturnToGround_Ticks=20;
    @Override
    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    @Override
    public void setReturnToGround_Ticks(int returnToGround_Ticks) {
        ReturnToGround_Ticks=returnToGround_Ticks;
    }

    @Override
    public boolean getInvisibleData() {
        return this.entityData.get(INVISIBLE);
    }

    @Override
    public void setInvisibleData(boolean isInvisible) {
        this.entityData.set(INVISIBLE, isInvisible);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull Pose pose) {
        return this.getInGround()? this.getType().getDimensions().withEyeHeight(0.1f): super.getDefaultDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (!this.getInGround() || this.getInGround()) {
            this.setBoundingBox(this.makeBoundingBox());
            this.refreshDimensions();
        }
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGround()) {
            return TCOTS_Sounds.getSoundEvent("graveir_idle");
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ZOGLIN_STEP;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("graveir_hurt");
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("graveir_death");
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("graveir_attack");
    }

    //Spawn
    public static boolean canSpawnGraveir(EntityType<? extends NecrophageMonster> type, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        if(spawnReason == MobSpawnType.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random);
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random)
                    && pos.getY() <= 63
                    && (world.getBlockState(pos.below()).is(BlockTags.BASE_STONE_OVERWORLD));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

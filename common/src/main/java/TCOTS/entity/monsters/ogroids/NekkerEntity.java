package TCOTS.entity.monsters.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Sounds;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.GuardNestMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;


public class NekkerEntity extends OgroidMonster implements GeoEntity, ExcavatorMob, LungeMob, TraceableEntity, GuardNestMob {

    //xTODO: Add spawn
    //xTODO: Add drops

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final EntityDataAccessor<Boolean> InGROUND = SynchedEntityData.defineId(NekkerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EMERGING = SynchedEntityData.defineId(NekkerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(NekkerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> NEST_POS = SynchedEntityData.defineId(NekkerEntity.class, EntityDataSerializers.BLOCK_POS);
    protected static final EntityDataAccessor<Boolean> CAN_HAVE_NEST = SynchedEntityData.defineId(NekkerEntity.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private Mob owner;
    @Nullable
    private UUID ownerUuid;

    public NekkerEntity(final EntityType<? extends NekkerEntity> entityType, final Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0f) //Amount of health that hurts you
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.28f)

                .add(Attributes.ATTACK_KNOCKBACK, 0.2f)
                .add(Attributes.ARMOR, 2f);
    }

    @Override
    public BlockPos getNestPos() {
        return this.entityData.get(NEST_POS);
    }

    @Override
    public void setNestPos(final BlockPos pos) {
        this.entityData.set(NEST_POS, pos);
    }

    @Override
    public boolean canHaveNest() {
        return this.entityData.get(CAN_HAVE_NEST);
    }

    @Override
    public void setCanHaveNest(final boolean canHaveNest) {
        this.entityData.set(CAN_HAVE_NEST, canHaveNest);
    }

    @Override
    public int getMaxHeadYRot() {
        return 40;
    }

    @Override
    protected void registerGoals() {

        //Emerge from ground
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        this.goalSelector.addGoal(2, new LungeAttackGoal(this, 150, 1.8, 5, 40));

        //Returns to ground
        this.goalSelector.addGoal(3, new ReturnToGroundGoal_Excavator(this));

        //Attack
        this.goalSelector.addGoal(4, new MeleeAttackGoal_Excavator(this, 1.2D, false, 2400));

        this.goalSelector.addGoal(5, new ReturnToNestGoal(this, 0.75));

        this.goalSelector.addGoal(6, new FollowMonsterOwnerGoal(this, 0.75));

        this.goalSelector.addGoal(7, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.addGoal(8, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.addGoal(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.addGoal(1, new AttackOwnerEnemyTarget(this));

        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, NekkerEntity.class).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }


    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor world, @NotNull final DifficultyInstance difficulty, @NotNull final MobSpawnType spawnReason, @Nullable final SpawnGroupData entityData) {
        final RandomSource random = world.getRandom();
        if(!this.level().isClientSide && !(spawnReason == MobSpawnType.SPAWN_EGG) && !(spawnReason == MobSpawnType.STRUCTURE) && this.getType()!= TCOTS_Entities.NekkerWarrior()) {
            //Can spawn a Nekker Warrior with it instead
            if (random.nextInt() % 5 == 0) {
                final NekkerWarriorEntity nekker_warrior = TCOTS_Entities.NekkerWarrior().create(this.level());
                if (nekker_warrior != null) {
                    nekker_warrior.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0f);
                    nekker_warrior.finalizeSpawn(world, world.getCurrentDifficultyAt(nekker_warrior.blockPosition()), spawnReason, null);
                    ((ServerLevel)(this.level())).tryAddFreshEntityWithPassengers(nekker_warrior);
                }
            }
        }

        if(spawnReason==MobSpawnType.SPAWNER || spawnReason==MobSpawnType.STRUCTURE){
            this.setCanHaveNest(true);
        }


        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public boolean getExtraReasonToNotGoToNest() {
        return this.getOwner()==null;
    }

    public boolean cooldownBetweenLunges = false;

    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

    public void setCooldownBetweenLunges(final boolean cooldownBetweenLunges) {
        this.cooldownBetweenLunges = cooldownBetweenLunges;
    }

    public int LungeTicks;

    @Override
    public int getLungeTicks() {
        return LungeTicks;
    }

    @Override
    public void setLungeTicks(final int lungeTicks) {
        LungeTicks = lungeTicks;
    }

    public int ReturnToGround_Ticks = 20;

    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    public void setReturnToGround_Ticks(final int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, GeoControllersUtil::idleWalkRunController)
        );

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
                        .triggerableAnim("attack3", GeoControllersUtil.ATTACK3)
        );

        //Lunge Controller // With 0 tick transition, so it can spin
        controllerRegistrar.add(
                new AnimationController<>(this, "LungeController", 0, state -> PlayState.STOP)
                        .triggerableAnim("lunge", getLungeAnimation())
        );

        //Digging Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "DiggingController", 1, this::animationDiggingPredicate)
        );

        //Emerging Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(InGROUND, Boolean.FALSE);
        builder.define(EMERGING, Boolean.FALSE);
        builder.define(INVISIBLE, Boolean.FALSE);
        builder.define(NEST_POS, BlockPos.ZERO);
        builder.define(CAN_HAVE_NEST, Boolean.FALSE);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull final Pose pose) {
        return this.getInGround()? this.getType().getDimensions().withEyeHeight(0.1f): super.getDefaultDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull final EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (!this.getInGround() || this.getInGround()) {
            this.setBoundingBox(this.makeBoundingBox());
            this.refreshDimensions();
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putBoolean("InGround", this.entityData.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.entityData.get(INVISIBLE));

        if (this.ownerUuid != null && !(this instanceof NekkerWarriorEntity)) {
            nbt.putUUID("Owner", this.ownerUuid);
        }

        writeNbtGuardNest(nbt);
    }

    @Override
    public void readAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));

        if (nbt.hasUUID("Owner") && !(this instanceof NekkerWarriorEntity)) {
            this.ownerUuid = nbt.getUUID("Owner");
        }

        readNbtGuardNest(nbt);
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        if (entityData.get(InGROUND)) {
            return groundBox(this);
        } else {
            // Normal hit-box otherwise
            return super.makeBoundingBox();
        }
    }

    public final boolean getIsEmerging() {
        return this.entityData.get(EMERGING);
    }

    public final void setIsEmerging(final boolean wasEmerging) {
        this.entityData.set(EMERGING, wasEmerging);
    }

    public boolean getInGround() {
        return this.entityData.get(InGROUND);
    }

    public void setInGround(final boolean wasInGround) {
        this.entityData.set(InGROUND, wasInGround);
    }

    public final boolean getInvisibleData() {
        return this.entityData.get(INVISIBLE);
    }

    public final void setInvisibleData(final boolean isInvisible) {
        this.entityData.set(INVISIBLE, isInvisible);
    }

    int AnimationParticlesTicks = 36;

    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    public void setAnimationParticlesTicks(final int animationParticlesTicks) {
        AnimationParticlesTicks = animationParticlesTicks;
    }

    @Override
    public void tick() {
        //Counter for Lunge attack
        this.tickLunge();
        //Counter for particles
        this.tickExcavator(this);

        //To disable the nest
        this.tickGuardNest(this);

        super.tick();
    }

    @Override
    public void push(@NotNull final Entity entity) {
        if(entity instanceof final NekkerEntity nekker && nekker.getInGround()){
            return;
        }

        super.push(entity);
    }

    private static final AttributeModifier LEADER_STRENGTH_BOOST = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nekker_leader_strength_boost"),
            2.0f, AttributeModifier.Operation.ADD_VALUE);



    @Override
    public void customServerAiStep() {
        mobTickExcavator(
                List.of(BlockTags.DIRT),
                List.of(Blocks.SAND),
                this
        );

        this.setInvisible(this.getInvisibleData());

        //Adds leader boost
        if(this.getOwner()!=null && this.getOwner().isAlive()){
            final AttributeInstance entityAttributeInstance = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) {
                entityAttributeInstance.removeModifier(LEADER_STRENGTH_BOOST.id());
                entityAttributeInstance.addTransientModifier(LEADER_STRENGTH_BOOST);
            }
        }

        if(!(this instanceof NekkerWarriorEntity) && this.getOwner()==null){
            final List<NekkerWarriorEntity> list =
                    this.level().getEntitiesOfClass(NekkerWarriorEntity.class, this.getBoundingBox().inflate(10,10,10),
                            nekkerWarrior -> true);

            if(!list.isEmpty()){
                this.setOwner(list.get(0));
            }
        }

        //Removes owner and strength
        if(this.getOwner() != null && !this.getOwner().isAlive()) {
            setOwner(null);
            final AttributeInstance entityAttributeInstance = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(LEADER_STRENGTH_BOOST.id());
        }


        super.customServerAiStep();
    }



    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGround()) {
            return TCOTS_Sounds.getSoundEvent("nekker_idle");
        } else {
            return null;
        }
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("nekker_hurt");
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("nekker_death");
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.getSoundEvent("nekker_lunge");
    }

    @Override
    public boolean isInvulnerableTo(@NotNull final DamageSource damageSource) {
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

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("nekker_attack");
    }

    public void spawnAnim() {
        if (this.level().isClientSide) {
            final BlockState blockState = this.getBlockStateOn();
            if (blockState.getRenderShape() != RenderShape.INVISIBLE) {

                for (int i = 0; i < 40; ++i) {
                    final double d = this.getX() + (double) Mth.randomBetween(random, -0.7F, 0.7F);
                    final double e = this.getY()+0.5;
                    final double f = this.getZ() + (double) Mth.randomBetween(random, -0.7F, 0.7F);

                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
        } else {
            this.level().broadcastEntityEvent(this, EntityEvent.SILVERFISH_MERGE_ANIM);
        }
    }

    @Nullable
    @Override
    public Entity getOwner() {
        final Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.level() instanceof ServerLevel && (entity = ((ServerLevel)this.level()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (Mob) entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable final Mob owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUUID();
    }


    public static boolean canSpawnNekker(final EntityType<? extends OgroidMonster> type, final ServerLevelAccessor world, final MobSpawnType spawnReason, final BlockPos pos, final RandomSource random) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (spawnReason.equals(MobSpawnType.SPAWNER)) {
                return true;
            }
            return Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random);
        }
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


}

package TCOTS.entity.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.GuardNestMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;


public class NekkerEntity extends OgroidMonster implements GeoEntity, ExcavatorMob, LungeMob, Ownable, GuardNestMob {

    //xTODO: Add spawn
    //xTODO: Add drops

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> NEST_POS = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    protected static final TrackedData<Boolean> CAN_HAVE_NEST = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Nullable
    private MobEntity owner;
    @Nullable
    private UUID ownerUuid;

    public NekkerEntity(EntityType<? extends NekkerEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR, 2f);
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

    @Override
    public int getMaxHeadRotation() {
        return 40;
    }

    @Override
    protected void initGoals() {

        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new LungeAttackGoal(this, 150, 1.8, 5, 40));

        //Returns to ground
        this.goalSelector.add(3, new ReturnToGroundGoal_Excavator(this));

        //Attack
        this.goalSelector.add(4, new MeleeAttackGoal_Excavator(this, 1.2D, false, 2400));

        this.goalSelector.add(5, new ReturnToNestGoal(this, 0.75));

        this.goalSelector.add(6, new FollowMonsterOwnerGoal(this, 0.75));

        this.goalSelector.add(7, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(8, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.add(1, new AttackOwnerEnemyTarget(this));

        this.targetSelector.add(2, new RevengeGoal(this, NekkerEntity.class).setGroupRevenge());
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }


    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        Random random = world.getRandom();
        if(!this.getWorld().isClient && !(spawnReason == SpawnReason.SPAWN_EGG) && !(spawnReason == SpawnReason.STRUCTURE) && this.getType()!=TCOTS_Entities.NEKKER_WARRIOR) {
            //Can spawn a Nekker Warrior with it instead
            if (random.nextInt() % 5 == 0) {
                NekkerWarriorEntity nekker_warrior = TCOTS_Entities.NEKKER_WARRIOR.create(this.getWorld());
                if (nekker_warrior != null) {
                    nekker_warrior.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                    ((MobEntity)nekker_warrior).initialize(world, world.getLocalDifficulty(nekker_warrior.getBlockPos()), spawnReason, null);
                    ((ServerWorld)(this.getWorld())).spawnNewEntityAndPassengers(nekker_warrior);
                }
            }
        }

        if(spawnReason==SpawnReason.SPAWNER || spawnReason==SpawnReason.STRUCTURE){
            this.setCanHaveNest(true);
        }


        return super.initialize(world, difficulty, spawnReason, entityData);
    }

    @Override
    public boolean getExtraReasonToNotGoToNest() {
        return this.getOwner()==null;
    }

    public boolean cooldownBetweenLunges = false;

    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

    public void setCooldownBetweenLunges(boolean cooldownBetweenLunges) {
        this.cooldownBetweenLunges = cooldownBetweenLunges;
    }

    public int LungeTicks;

    @Override
    public int getLungeTicks() {
        return LungeTicks;
    }

    @Override
    public void setLungeTicks(int lungeTicks) {
        LungeTicks = lungeTicks;
    }

    public int ReturnToGround_Ticks = 20;

    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    public void setReturnToGround_Ticks(int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
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
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(InGROUND, Boolean.FALSE);
        builder.add(EMERGING, Boolean.FALSE);
        builder.add(INVISIBLE, Boolean.FALSE);
        builder.add(NEST_POS, BlockPos.ORIGIN);
        builder.add(CAN_HAVE_NEST, Boolean.FALSE);
    }

    @Override
    protected EntityDimensions getBaseDimensions(EntityPose pose) {
        return this.getInGround()? this.getType().getDimensions().withEyeHeight(0.1f): super.getBaseDimensions(pose);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (!this.getInGround() || this.getInGround()) {
            this.setBoundingBox(this.calculateBoundingBox());
            this.calculateDimensions();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.dataTracker.get(INVISIBLE));

        if (this.ownerUuid != null && !(this instanceof NekkerWarriorEntity)) {
            nbt.putUuid("Owner", this.ownerUuid);
        }

        writeNbtGuardNest(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));

        if (nbt.containsUuid("Owner") && !(this instanceof NekkerWarriorEntity)) {
            this.ownerUuid = nbt.getUuid("Owner");
        }

        readNbtGuardNest(nbt);
    }

    @Override
    protected Box calculateBoundingBox() {
        if (dataTracker.get(InGROUND)) {
            return groundBox(this);
        } else {
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    public final boolean getIsEmerging() {
        return this.dataTracker.get(EMERGING);
    }

    public final void setIsEmerging(boolean wasEmerging) {
        this.dataTracker.set(EMERGING, wasEmerging);
    }

    public boolean getInGround() {
        return this.dataTracker.get(InGROUND);
    }

    public void setInGround(boolean wasInGround) {
        this.dataTracker.set(InGROUND, wasInGround);
    }

    public final boolean getInvisibleData() {
        return this.dataTracker.get(INVISIBLE);
    }

    public final void setInvisibleData(boolean isInvisible) {
        this.dataTracker.set(INVISIBLE, isInvisible);
    }

    int AnimationParticlesTicks = 36;

    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    public void setAnimationParticlesTicks(int animationParticlesTicks) {
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
    public void pushAwayFrom(Entity entity) {
        if(entity instanceof NekkerEntity nekker && nekker.getInGround()){
            return;
        }

        super.pushAwayFrom(entity);
    }

    private static final EntityAttributeModifier LEADER_STRENGTH_BOOST = new EntityAttributeModifier(
            Identifier.of(TCOTS_Main.MOD_ID, "nekker_leader_strength_boost"),
            2.0f, EntityAttributeModifier.Operation.ADD_VALUE);



    @Override
    public void mobTick() {
        mobTickExcavator(
                List.of(BlockTags.DIRT),
                List.of(Blocks.SAND),
                this
        );

        this.setInvisible(this.getInvisibleData());

        //Adds leader boost
        if(this.getOwner()!=null && this.getOwner().isAlive()){
            EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) {
                entityAttributeInstance.removeModifier(LEADER_STRENGTH_BOOST.id());
                entityAttributeInstance.addTemporaryModifier(LEADER_STRENGTH_BOOST);
            }
        }

        if(!(this instanceof NekkerWarriorEntity) && this.getOwner()==null){
            List<NekkerWarriorEntity> list =
                    this.getWorld().getEntitiesByClass(NekkerWarriorEntity.class, this.getBoundingBox().expand(10,10,10),
                            nekkerWarrior -> true);

            if(!list.isEmpty()){
                this.setOwner(list.get(0));
            }
        }

        //Removes owner and strength
        if(this.getOwner() != null && !this.getOwner().isAlive()) {
            setOwner(null);
            EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(LEADER_STRENGTH_BOOST.id());
        }


        super.mobTick();
    }



    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGround()) {
            return TCOTS_Sounds.NEKKER_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.NEKKER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.NEKKER_DEATH;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.NEKKER_LUNGE;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGround() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.getIsEmerging() && !this.getInGround();
    }

    @Override
    public boolean isFireImmune() {
        return super.isFireImmune() || this.getInGround();
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.NEKKER_ATTACK;
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


    public static boolean canSpawnNekker(EntityType<? extends OgroidMonster> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (spawnReason.equals(SpawnReason.SPAWNER)) {
                return true;
            }
            return HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
        }
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


}

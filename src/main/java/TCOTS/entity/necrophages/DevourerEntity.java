package TCOTS.entity.necrophages;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.misc.CommonControllers;
import TCOTS.items.potions.TCOTS_Effects;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public class DevourerEntity extends Necrophage_Base implements GeoEntity, ExcavatorMob {

    //TODO: Fix the jump particles
    //TODO: Finish bestiary
    //TODO: Add jumping animation
    //xTODO: Add natural spawn
    //xTODO: Add drop

    public static final RawAnimation JUMP = RawAnimation.begin().thenPlay("special.jumping");

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> FALLING = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public DevourerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        experiencePoints=10;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)

                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    protected void initGoals() {
        //Attack
        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.add(1, new SwimGoal(this));

        //Returns to ground
        this.goalSelector.add(2, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.add(3, new DevourerJumpAttack(this,120));

        this.goalSelector.add(4, new MeleeAttackGoal_Excavator(this, 1.2D, false, 3600));

        this.goalSelector.add(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(2, new RevengeGoal(this, DevourerEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    private static class DevourerJumpAttack extends Goal {

        private final DevourerEntity devourer;
        private final int jumpCooldown;

        public DevourerJumpAttack(DevourerEntity devourer, int jumpCooldown) {
            this.devourer=devourer;
            this.jumpCooldown=jumpCooldown;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.devourer.getTarget();

            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !this.devourer.cooldownBetweenJumps && this.devourer.isAttacking()
                        && this.devourer.squaredDistanceTo(target) < 5;
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.devourer.getTarget();

            if(target!=null){
                jumpAttack(target);
            }
        }

        private void jumpAttack(LivingEntity target){
            devourer.triggerAnim("JumpController","jump");
            devourer.setJumping(true);
            devourer.addVelocity(0f,0.5f,0f);
            devourer.cooldownBetweenJumps=true;
            devourer.jumpTicks=jumpCooldown;
        }
    }

    private boolean activateParticles=false;
    @Override
    public void onLanding() {
        if(isFalling()){
            pushAndDamageEntities(1.5,2,1.5, 1.2);
            setIsFalling(false);
            activateParticles=true;
        }
        super.onLanding();
    }

    private void pushAndDamageEntities(double xExpansion, double yExpansion, double zExpansion, double knockbackStrength){
        List<LivingEntity> listMobs= this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(xExpansion,yExpansion,zExpansion),
                livingEntity -> !(livingEntity instanceof DevourerEntity));

        for (LivingEntity entity : listMobs){
            double d = this.getX() - entity.getX();
            double e = this.getZ() - entity.getZ();
            entity.takeKnockback(knockbackStrength,d,e);
            if(entity.isBlocking() && entity instanceof PlayerEntity){
                ((PlayerEntity) entity).disableShield(true);
            }

            if(entity instanceof ServerPlayerEntity && !((ServerPlayerEntity) entity).isCreative()){
                ((ServerPlayerEntity) entity).networkHandler.send(new EntityVelocityUpdateS2CPacket(entity), null);
            }

            entity.damage(this.getDamageSources().mobAttack(this), 2);
        }
    }

    private int counter=0;

    private void spawnSmokeParticles(int counter){
        Vec3d vec3d = this.getBoundingBox().getCenter();
        while (counter < 20) {
            double d = this.random.nextGaussian() * 0.2;
            double e = this.random.nextGaussian() * 0.2;
            double f = this.random.nextGaussian() * 0.2;
            this.getWorld().addParticle(ParticleTypes.WHITE_SMOKE, vec3d.x, vec3d.y, vec3d.z, d, e, f);
            ++counter;
        }
        activateParticles=false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(INVISIBLE, Boolean.FALSE);
        this.dataTracker.startTracking(FALLING, Boolean.FALSE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5,
                state -> CommonControllers.idleWalkRun(state, this, RUNNING, WALKING, IDLE)
        ));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 2, state -> PlayState.STOP)
                        .triggerableAnim("attack1", CommonControllers.ATTACK1)
                        .triggerableAnim("attack2", CommonControllers.ATTACK2)
                        .triggerableAnim("attack3", CommonControllers.ATTACK3)
        );

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );

        controllerRegistrar.add(
                new AnimationController<>(this, "JumpController", 1, state -> PlayState.STOP)
                        .triggerableAnim("jump", JUMP)
        );
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.dataTracker.get(INVISIBLE));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setInGroundDataTracker(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
        super.readCustomDataFromNbt(nbt);
    }

    public boolean cooldownBetweenJumps=false;
    int jumpTicks;

    private void tickJump(){
        if(counter>0){
            counter=0;
        }

        if (jumpTicks > 0) {
            --jumpTicks;
        } else {
            cooldownBetweenJumps=false;
        }
    }


    @Override
    public void tick() {
        //Counter for particles
        this.tickExcavator(this);
        //Tick for jumps
        this.tickJump();

        if(activateParticles){
            spawnSmokeParticles(counter);

        }

        super.tick();
    }

    @Override
    protected void mobTick() {
        mobTickExcavator(
                List.of(BlockTags.DIRT, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                List.of(Blocks.SAND),
                this
        );

        if(cooldownBetweenJumps && !this.isOnGround() && !this.isTouchingWater()){
            setIsFalling(true);
        }


        this.setInvisible(this.getInvisibleData());
        super.mobTick();
    }

    @Override
    protected Box calculateBoundingBox() {
        if (dataTracker.get(InGROUND)) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGroundDataTracker()) {
            return TCOTS_Sounds.DEVOURER_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.DEVOURER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.DEVOURER_DEATH;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGroundDataTracker() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return !this.getIsEmerging() && !this.getInGroundDataTracker();
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.DEVOURER_ATTACK;
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

    public void setIsFalling(boolean isFalling) {
        this.dataTracker.set(FALLING, isFalling);
    }

    public boolean isFalling() {
        return this.dataTracker.get(FALLING);
    }

    @Override
    public boolean getInGroundDataTracker() {
        return this.dataTracker.get(InGROUND);
    }

    @Override
    public void setInGroundDataTracker(boolean wasInGround) {
        this.dataTracker.set(InGROUND, wasInGround);
    }

    @Override
    public boolean getIsEmerging() {
        return this.dataTracker.get(EMERGING);
    }

    @Override
    public void setIsEmerging(boolean wasEmerging) {
        this.dataTracker.set(EMERGING, wasEmerging);
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
        return this.dataTracker.get(INVISIBLE);
    }

    @Override
    public void setInvisibleData(boolean isInvisible) {
        this.dataTracker.set(INVISIBLE, isInvisible);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != TCOTS_Effects.SAMUM_EFFECT && super.canHaveStatusEffect(effect);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

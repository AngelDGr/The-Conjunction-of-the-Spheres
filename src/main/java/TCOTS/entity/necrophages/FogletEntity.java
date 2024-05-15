package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.misc.CommonControllers;
import TCOTS.entity.misc.FoglingEntity;
import TCOTS.items.potions.bombs.DimeritiumBomb;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class FogletEntity extends Necrophage_Base implements GeoEntity {

    //xTODO: Add fog attack goal
        //xTODO: Trigger animation and sound
        //xTODO: Spawn particles
        //xTODO: Make entity invisible with some visual effect
        //xTODO: Attacks and become visible
        //xTODO: Makes that only one at the time can generates fog particles
        //xTODO: Spawn like two fog illusion
        //xTODO: Detect own fog illusion by owner

    //xTODO: Add fog particle
    //xTODO: Add fog illusion entity to spawn

    //xTODO: Add drops
        //xTODO: Foglet Teeth, used in Samum bomb
        //xTODO: Add Foglet Mutagen
            //xTODO: Foglet Decoction: Increases Sign intensity during cloudy weather??? -> Increases resistance during rain/storm?
    //xTODO: Add spawns (Swamp, Mountains and Forests)

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation TRIGGER_FOG = RawAnimation.begin().thenPlay("special.fog");

    public FogletEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.2f);
        this.experiencePoints = 8;
    }

    protected static final TrackedData<Boolean> ACTIVATES_FOG = DataTracker.registerData(FogletEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Float> ALPHA_VALUE = DataTracker.registerData(FogletEntity.class, TrackedDataHandlerRegistry.FLOAT);

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new Foglet_Swim(this));
        this.goalSelector.add(1, new Foglet_AttackWithFog<>(this, 1.0, false,100));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.75, 20));
        this.goalSelector.add(3, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(1, new RevengeGoal(this, FogletEntity.class));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.21f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.3f);
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.INVISIBILITY && super.canHaveStatusEffect(effect);
    }

    protected static class Foglet_Swim extends SwimGoal{

        FogletEntity foglet;

        public Foglet_Swim(MobEntity mob) {
            super(mob);

            this.foglet=(FogletEntity) mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !foglet.getIsFog();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !foglet.getIsFog();
        }
    }

    protected static class Foglet_AttackWithFog<T extends LivingEntity> extends Goal{
        private final T actor;
        private final PathAwareEntity mob;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int updateCountdownTicks;
        private int cooldown;
        public final double speed;

        private final boolean pauseWhenMobIdle;

        private final int ticksBeforeSummon;

        private int TicksBeforeFoglings=-1;

        public Foglet_AttackWithFog(T actor, double speed, boolean pauseWhenMobIdle, int ticksBeforeSummon) {
            this.actor = actor;
            this.mob= (PathAwareEntity) actor;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            this.speed=speed;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
            this.ticksBeforeSummon=ticksBeforeSummon;
        }

        int tickBeforeFog=40;
        @Override
        public boolean canStart() {
            return ((MobEntity) this.actor).getTarget() != null;
        }

        @Override
        public boolean shouldContinue() {
            return (this.canStart() || !((MobEntity)this.actor).getNavigation().isIdle());
        }

        @Override
        public void start() {
            super.start();
            ((MobEntity)this.actor).setAttacking(true);
            tickBeforeFog=40;
        }

        @Override
        public void stop() {
            super.stop();
            ((MobEntity)this.actor).setAttacking(false);
            ((FogletEntity)actor).setIsFog(false);
            if(actor.isInvisible()){
                actor.setInvisible(false);
            }
            if(((FogletEntity)actor).getAnimationTicks() > 0){
                ((FogletEntity)actor).setAnimationTicks(-1);
            }
            if(!(((FogletEntity)actor).foglingsList.isEmpty())){
                ((FogletEntity)actor).foglingsList.forEach(
                        foglingEntity -> foglingEntity.damage(actor.getDamageSources().magic(), 10)
                );
            }
            ((FogletEntity)actor).foglingsList.clear();
            TicksBeforeFoglings=-1;
        }


        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        private boolean attack=false;

        private int ticksBeforeAttack=40;
        @Override
        public void tick() {
            LivingEntity livingEntity = ((MobEntity)this.actor).getTarget();
            if (livingEntity == null) {
                return;
            }

            if(!(actor instanceof FoglingEntity)) {
                for (int i = 0; i < ((FogletEntity) actor).foglingsList.size(); i++) {
                    if (!(((FogletEntity) actor).foglingsList.get(i).isAlive())) {
                        ((FogletEntity) actor).foglingsList.remove(i);
                    }
                }

                if (((FogletEntity) actor).foglingsList.size() == 2) {
                    TicksBeforeFoglings = ticksBeforeSummon;
                }

                if (TicksBeforeFoglings > 0) {
                    --TicksBeforeFoglings;
                } else {
                    TicksBeforeFoglings = -1;
                }
            }

            if(!DimeritiumBomb.checkEffect(actor)){
                generatesFog(livingEntity);
            }

            if(((this.actor.isInvisible() || attack) && tickBeforeFog <= -1) || DimeritiumBomb.checkEffect(actor)) {
                meleeAttack(livingEntity);
            }

            if(attack){
                --ticksBeforeAttack;
            } else if(tickBeforeFog == -2) {
                tickBeforeFog=20;
            }

            if(ticksBeforeAttack==0){
                attack=false;
            }

        }

        private double getSpeed(){
            if(((FogletEntity)actor).getIsFog()){
                return this.speed*1.5;
            }
            else {
                return this.speed;
            }
        }

        private void generatesFog(LivingEntity livingEntity){
            if(tickBeforeFog>0){
                ((MobEntity)this.actor).getMoveControl().strafeTo(-0.5f, 0);
                ((MobEntity)this.actor).lookAtEntity(livingEntity, 30.0f, 30.0f);
            }

            if(tickBeforeFog>0){
                --tickBeforeFog;
            }

            if(tickBeforeFog==0){
                ((FogletEntity)actor).setIsFog(true);
                actor.playSound(TCOTS_Sounds.FOGLET_FOG, 1, 1);
                ((FogletEntity)actor).setAnimationTicks(24);
                tickBeforeFog=-1;
            }

            if(((FogletEntity)actor).getAnimationTicks()>0){
                ((FogletEntity)actor).setAnimationTicks(((FogletEntity)actor).getAnimationTicks()-1);
            } else if (((FogletEntity)actor).getAnimationTicks() == 0) {
                if(!(actor instanceof FoglingEntity) && TicksBeforeFoglings==-1) {
                    this.generateFoglings();
                }
                actor.setInvisible(true);
                ((FogletEntity)actor).setAnimationTicks(-1);
            }
        }

        private void generateFoglings(){
            ServerWorld serverWorld = (ServerWorld)actor.getWorld();

            if(((FogletEntity)actor).foglingsList.size() < 2) {
                for (int j = ((FogletEntity)actor).foglingsList.size(); j < 2; ++j) {
                    BlockPos blockPos = actor.getBlockPos().add(-2 + actor.getRandom().nextInt(5), 0, -2 + actor.getRandom().nextInt(5));
                    FoglingEntity foglingEntity = TCOTS_Entities.FOGLING.create(actor.getWorld());
                    if (foglingEntity == null) continue;
                    foglingEntity.refreshPositionAndAngles(blockPos, 0.0f, 0.0f);
                    foglingEntity.initialize(serverWorld, actor.getWorld().getLocalDifficulty(blockPos), SpawnReason.MOB_SUMMONED, null, null);
                    foglingEntity.setOwner((MobEntity) actor);
                    serverWorld.spawnEntityAndPassengers(foglingEntity);
                    ((FogletEntity) actor).foglingsList.add(foglingEntity);
                }
            }
        }

        private void meleeAttack(LivingEntity livingEntity){
            this.mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
            double d =
//                    this.mob.getSquaredDistanceToAttackPosOf(livingEntity);
            this.mob.squaredDistanceTo(livingEntity);
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.targetX = livingEntity.getX();
                this.targetY = livingEntity.getY();
                this.targetZ = livingEntity.getZ();
                this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5;
                }
                if (!this.mob.getNavigation().startMovingTo(livingEntity, this.getSpeed())) {
                    this.updateCountdownTicks += 15;
                }
                this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
            }
            this.cooldown = Math.max(this.cooldown - 1, 0);
            this.attack(livingEntity, d);
        }

        protected void attack(LivingEntity target, double squaredDistance) {
            double d = this.getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && this.cooldown <= 0) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(target);


                ((FogletEntity)actor).setIsFog(false);
                actor.setInvisible(false);
                attack=true;
                if(ticksBeforeAttack == 0){
                    ticksBeforeAttack=40;
                }
                tickBeforeFog=-2;
            }
        }

        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f) + entity.getWidth();
        }

        protected void resetCooldown() {
            this.cooldown = this.getTickCount(20);
        }
    }
    List<FoglingEntity> foglingsList = new ArrayList<>();
    private int AnimationTicks=-1;

    public int getAnimationTicks() {
        return AnimationTicks;
    }

    public void setAnimationTicks(int animationTicks) {
        AnimationTicks = animationTicks;
    }

    protected void spawnFogParticlesItself(){
        if(this.age%12 == 0){
            double d = this.getX() + (double) MathHelper.nextBetween(this.getRandom(), -0.8F, 0.8F);
            double e = (this.getEyeY()-0.5f)+ (double) MathHelper.nextBetween(this.getRandom(), -1F, 1F);
            double f = this.getZ() + (double) MathHelper.nextBetween(this.getRandom(), -0.8F, 0.8F);
            this.getWorld().addParticle(TCOTS_Particles.FOGLET_FOG, d,e,f,0,0,0);
        }
    }

    private void spawnFogParticlesAround(){
        if(this.age%8 == 0){
            double d = this.getX() + (double) MathHelper.nextBetween(this.getRandom(), -10F, 10F);
            double e = (this.getEyeY()-0.5f)+ (double) MathHelper.nextBetween(this.getRandom(), -1F, 1F);
            double f = this.getZ() + (double) MathHelper.nextBetween(this.getRandom(), -10F, 10F);
            this.getWorld().addParticle(TCOTS_Particles.FOGLET_FOG_AROUND, d,e,f,0,0,0);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Idle/Walk/Run", 5, state -> {

            //If it's attacking and moving
            if(this.isAttacking() && state.isMoving()){
                state.setControllerSpeed(1f);
                return state.setAndContinue(WALKING);
            }else
            if(state.isMoving()){
                state.setControllerSpeed(0.5f);
                return state.setAndContinue(WALKING);
            }
            else{
                state.setControllerSpeed(1f);
                return state.setAndContinue(IDLE);
            }
        }));

        //Attack Controller
        controllers.add(
                new AnimationController<>(this, "AttackController", 1, state -> CommonControllers.animationTwoAttacksPredicate(state,this.handSwinging,random))
        );

        //Fog Controller
        controllers.add(
                new AnimationController<>(this, "FogController", 1, state -> {
                    if (this.getIsFog()) {
                        return state.setAndContinue(TRIGGER_FOG);
                    }
                    state.getController().forceAnimationReset();
                    return PlayState.CONTINUE;
                })
        );
    }

    List<FogletEntity> fogletList=new ArrayList<>();

    List<FogletEntity> fogletLeader=new ArrayList<>();

    boolean leader;
    private void getOtherFoglet() {
        if(canGenerateFog && isAttacking()) {
            fogletList.clear();
            fogletList =
                    this.getWorld().getEntitiesByClass(FogletEntity.class, this.getBoundingBox().expand(20, 20, 20),
                            foglet -> foglet != this
                    );

            fogletList.forEach(
                    fogletEntity -> fogletEntity.canGenerateFog=false
            );

            leader = true;
        }
    }

    private void getFogletLeader(){
        if(!canGenerateFog && isAttacking()) {
            fogletLeader.clear();
            fogletLeader =
                    this.getWorld().getEntitiesByClass(FogletEntity.class, this.getBoundingBox().expand(20, 20, 20),
                            foglet -> foglet.leader && foglet != this
                    );

            if(fogletLeader.isEmpty()){
             canGenerateFog= true;
            }
        }
    }
    protected boolean canGenerateFog=true;
    @Override
    public void tick() {
        if(this.isInvisible()){
            spawnFogParticlesItself();
        }

        if(this.getIsFog() && DimeritiumBomb.checkEffect(this)){
            this.setIsFog(false);
        }

        NoParticles:
        if(!(this instanceof FoglingEntity)){
            this.getOtherFoglet();
            this.getFogletLeader();
            this.getFoglings();

            if(!this.getWorld().isClient && DimeritiumBomb.checkEffect(this)){
                break NoParticles;
            }

            if(this.getWorld().isClient && this.isAttacking() && canGenerateFog) {
                spawnFogParticlesAround();
            }
        }


        if(this.getIsFog() && getAlphaValue()>0){
            setAlphaValue(getAlphaValue() - ((float) 0.05));
        } else if (!this.getIsFog()) {
            if(getAlphaValue()<0.9){
                setAlphaValue(getAlphaValue() + ((float) 0.1));
            }
        }

        if(this.getIsFog() && !this.isAttacking()){
            this.setIsFog(false);
        }

        if(this.getAnimationTicks() > 0 && !this.isAttacking()){
            this.setAnimationTicks(-1);
        }

        this.updateFloating();

        this.checkBlockCollision();
        super.tick();
    }

    private void getFoglings(){
        if(isAttacking() && this.foglingsList.isEmpty()) {
            foglingsList =
                    this.getWorld().getEntitiesByClass(FoglingEntity.class, this.getBoundingBox().expand(20, 20, 20),
                            fogling ->
                                    fogling.getOwner() != null && fogling.getOwner().getUuid() == this.getUuid());
        }
    }

    public final boolean getIsFog() {
        return this.dataTracker.get(ACTIVATES_FOG);
    }

    public final void setIsFog(boolean wasFog) {
        this.dataTracker.set(ACTIVATES_FOG, wasFog);
    }

    public float getAlphaValue() {
        return this.dataTracker.get(ALPHA_VALUE);
    }

    public void setAlphaValue(float AlphaValue) {
        this.dataTracker.set(ALPHA_VALUE, AlphaValue);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ACTIVATES_FOG, Boolean.FALSE);
        this.dataTracker.startTracking(ALPHA_VALUE, 1f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("InFog", this.dataTracker.get(ACTIVATES_FOG));
        nbt.putFloat("AlphaValue", this.dataTracker.get(ALPHA_VALUE));
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setIsFog(nbt.getBoolean("InFog"));
        this.setAlphaValue(nbt.getFloat("AlphaValue"));
        super.readCustomDataFromNbt(nbt);
    }
    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.FOGLET_IDLE;
    }

    @Override
    public boolean canWalkOnFluid(FluidState state) {
        return ((state.isIn(FluidTags.LAVA) || state.isIn(FluidTags.WATER)) && this.getIsFog());
    }

    protected void updateFloating() {
        if ((this.isInLava() || this.isInWater()) && this.getIsFog()) {
            ShapeContext shapeContext = ShapeContext.of(this);
            if (!shapeContext.isAbove(FluidBlock.COLLISION_SHAPE, this.getBlockPos(), true) || this.getWorld().getFluidState(this.getBlockPos().up()).isIn(FluidTags.LAVA)
                    || this.getWorld().getFluidState(this.getBlockPos().up()).isIn(FluidTags.WATER)
            ) {
                this.setVelocity(this.getVelocity().multiply(0.5).add(0.0, 0.05, 0.0));
            } else {
                this.setOnGround(true);
            }
        }
    }

    public boolean isInWater() {
        return !this.firstUpdate && this.fluidHeight.getDouble(FluidTags.WATER) > 0.0;
    }

    @Override
    public boolean isFireImmune() {
        return super.isFireImmune() || this.getIsFog();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.FOGLET_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.FOGLET_DEATH;
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.FOGLET_ATTACK;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(this.getIsFog()){
            amount=amount/8;
        }

        return super.damage(source, amount);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if(!(this.foglingsList.isEmpty())){
            this.foglingsList.forEach(
                    foglingEntity -> foglingEntity.damage(this.getDamageSources().magic(), 10)
            );
        }
        super.onDeath(damageSource);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getAnimationTicks() > 0 || this.getIsFog() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

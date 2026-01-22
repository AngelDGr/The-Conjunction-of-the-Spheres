package mors.tcots.entity.monsters.necrophages;

import mors.tcots.client.geo.animation.entity.necrophage.FogletAnimations;
import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.items.concoctions.bombs.MoonDustBomb;
import mors.tcots.entity.misc.FoglingEntity;
import mors.tcots.registry.TCOTS_Particles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class FogletEntity extends NecrophageMonster implements GeoEntity {

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

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation TRIGGER_FOG = RawAnimation.begin().thenPlay("special.fog");

    public FogletEntity(final EntityType<? extends FogletEntity> entityType, final Level world) {
        super(entityType, world);
        this.setPathfindingMalus(PathType.WATER, 0.2f);
        this.xpReward = 8;
    }

    @Override
    public int getMaxHeadYRot() {
        return 50;
    }

    protected static final EntityDataAccessor<Boolean> ACTIVATES_FOG = SynchedEntityData.defineId(FogletEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> ALPHA_VALUE = SynchedEntityData.defineId(FogletEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new Foglet_Swim(this));
        this.goalSelector.addGoal(1, new Foglet_AttackWithFog<>(this, 1.0, false,100));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.75, 20));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, FogletEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.21f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3f);
    }

    @Override
    public boolean canBeAffected(final MobEffectInstance effect) {
        return effect.getEffect() != MobEffects.INVISIBILITY && super.canBeAffected(effect);
    }

    protected static class Foglet_Swim extends FloatGoal{

        FogletEntity foglet;

        public Foglet_Swim(final Mob mob) {
            super(mob);

            this.foglet=(FogletEntity) mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !foglet.getIsFog();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !foglet.getIsFog();
        }
    }

    protected static class Foglet_AttackWithFog<T extends LivingEntity> extends Goal{
        private final T actor;
        private final PathfinderMob mob;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int updateCountdownTicks;
        private int cooldown;
        public final double speed;

        private final boolean pauseWhenMobIdle;

        private final int ticksBeforeSummon;

        private int TicksBeforeFoglings=-1;

        public Foglet_AttackWithFog(final T actor, final double speed, final boolean pauseWhenMobIdle, final int ticksBeforeSummon) {
            this.actor = actor;
            this.mob= (PathfinderMob) actor;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            this.speed=speed;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.ticksBeforeSummon=ticksBeforeSummon;
        }

        int tickBeforeFog=40;
        @Override
        public boolean canUse() {
            return ((Mob) this.actor).getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return (this.canUse() || !((Mob)this.actor).getNavigation().isDone());
        }

        @Override
        public void start() {
            super.start();
            ((Mob)this.actor).setAggressive(true);
            tickBeforeFog=40;
        }

        @Override
        public void stop() {
            super.stop();
            ((Mob)this.actor).setAggressive(false);
            ((FogletEntity)actor).setIsFog(false);
            if(actor.isInvisible()){
                actor.setInvisible(false);
            }
            if(((FogletEntity)actor).getAnimationTicks() > 0){
                ((FogletEntity)actor).setAnimationTicks(-1);
            }
            if(!(((FogletEntity)actor).foglingsList.isEmpty())){
                ((FogletEntity)actor).foglingsList.forEach(
                        foglingEntity -> foglingEntity.hurt(actor.damageSources().magic(), 10)
                );
            }
            ((FogletEntity)actor).foglingsList.clear();
            TicksBeforeFoglings=-1;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        private boolean attack=false;

        private int ticksBeforeAttack=40;
        @SuppressWarnings("all")
        @Override
        public void tick() {
            LivingEntity livingEntity = ((Mob)this.actor).getTarget();
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

            if(!MoonDustBomb.checkEffectAndSplinters(actor)){
                generatesFog(livingEntity);
            }

            if(((this.actor.isInvisible() || attack) && tickBeforeFog <= -1) || MoonDustBomb.checkEffectAndSplinters(actor)) {
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

        private void generatesFog(final LivingEntity livingEntity){
            if(tickBeforeFog>0){
                ((Mob)this.actor).getMoveControl().strafe(-0.5f, 0);
                ((Mob)this.actor).lookAt(livingEntity, 30.0f, 30.0f);
            }

            if(tickBeforeFog>0){
                --tickBeforeFog;
            }

            if(tickBeforeFog==0){
                ((FogletEntity)actor).setIsFog(true);
                ((FogletEntity)actor).triggerAnim("base_controller", "fog");
                actor.playSound(TCOTS_Sounds.getSoundEvent("foglet_fog"), 1, 1);
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
            final ServerLevel serverWorld = (ServerLevel)actor.level();

            if(((FogletEntity)actor).foglingsList.size() < 2) {
                for (int j = ((FogletEntity)actor).foglingsList.size(); j < 2; ++j) {
                    final BlockPos blockPos = actor.blockPosition().offset(-2 + actor.getRandom().nextInt(5), 0, -2 + actor.getRandom().nextInt(5));
                    final FoglingEntity foglingEntity = TCOTS_Entities.Fogling().create(actor.level());
                    if (foglingEntity == null) continue;
                    foglingEntity.moveTo(blockPos, 0.0f, 0.0f);
                    foglingEntity.finalizeSpawn(serverWorld, actor.level().getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null);
                    foglingEntity.setOwner((Mob) actor);
                    serverWorld.addFreshEntityWithPassengers(foglingEntity);
                    ((FogletEntity) actor).foglingsList.add(foglingEntity);
                }
            }
        }

        private void meleeAttack(final LivingEntity livingEntity){
            this.mob.getLookControl().setLookAt(livingEntity, 30.0f, 30.0f);
            final double d = this.mob.distanceToSqr(livingEntity);
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.mob.getSensing().hasLineOfSight(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.targetX = livingEntity.getX();
                this.targetY = livingEntity.getY();
                this.targetZ = livingEntity.getZ();
                this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5;
                }
                if (!this.mob.getNavigation().moveTo(livingEntity, this.getSpeed())) {
                    this.updateCountdownTicks += 15;
                }
                this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
            }
            this.cooldown = Math.max(this.cooldown - 1, 0);
            this.attack(livingEntity, d);
        }

        protected void attack(final LivingEntity target, final double squaredDistance) {
            final double d = this.getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && this.cooldown <= 0) {
                this.resetCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);

                if(this.mob instanceof final GeoEntity geo) {
                    final int randomAttack = this.mob.getRandom().nextIntBetweenInclusive(0, 1);
                    if (randomAttack == 0) {
                        geo.triggerAnim("base_controller", "attack1");
                    } else {
                        geo.triggerAnim("base_controller", "attack2");
                    }
                }

                this.mob.doHurtTarget(target);

                ((FogletEntity)actor).setIsFog(false);
                actor.setInvisible(false);
                attack=true;
                if(ticksBeforeAttack == 0){
                    ticksBeforeAttack=40;
                }
                tickBeforeFog=-2;
            }
        }

        protected double getSquaredMaxAttackDistance(final LivingEntity entity) {
            return this.mob.getBbWidth() * 2.0f * (this.mob.getBbWidth() * 2.0f) + entity.getBbWidth();
        }

        protected void resetCooldown() {
            this.cooldown = this.adjustedTickDelay(20);
        }
    }
    List<FoglingEntity> foglingsList = new ArrayList<>();
    private int AnimationTicks=-1;

    public int getAnimationTicks() {
        return AnimationTicks;
    }

    public void setAnimationTicks(final int animationTicks) {
        AnimationTicks = animationTicks;
    }

    protected void spawnFogParticlesItself(){
        if(this.tickCount%12 == 0){
            final double d = this.getX() + (double) Mth.randomBetween(this.getRandom(), -0.8F, 0.8F);
            final double e = (this.getEyeY()-0.5f)+ (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
            final double f = this.getZ() + (double) Mth.randomBetween(this.getRandom(), -0.8F, 0.8F);
            this.level().addParticle(TCOTS_Particles.FogletFog(), d,e,f,0,0,0);
        }
    }

    private void spawnFogParticlesAround(){
        if(this.tickCount%8 == 0){
            final double d = this.getX() + (double) Mth.randomBetween(this.getRandom(), -10F, 10F);
            final double e = (this.getEyeY()-0.5f)+ (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
            final double f = this.getZ() + (double) Mth.randomBetween(this.getRandom(), -10F, 10F);
            this.level().addParticle(TCOTS_Particles.FogletFogAround(), d,e,f,0,0,0);
        }
    }

    @Override
    public void setTarget(@Nullable final LivingEntity livingEntity) {
        super.setTarget(livingEntity);
        if(!this.foglingsList.isEmpty()){
            this.foglingsList.forEach(foglingEntity -> foglingEntity.setTarget(livingEntity));
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        //Walk/Idle Controller
        controllers.add(FogletAnimations.mainController(this));
    }

    @Override
    protected void playStepSound(@NotNull final BlockPos pos, @NotNull final BlockState state) {
        if(this.getIsFog()) return;

        super.playStepSound(pos, state);
    }

    List<FogletEntity> fogletList=new ArrayList<>();

    List<FogletEntity> fogletLeader=new ArrayList<>();

    boolean leader;
    private void getOtherFoglet() {
        if(canGenerateFog && isAggressive()) {
            fogletList.clear();
            fogletList =
                    this.level().getEntitiesOfClass(FogletEntity.class, this.getBoundingBox().inflate(20, 20, 20),
                            foglet -> foglet != this
                    );

            fogletList.forEach(
                    fogletEntity -> fogletEntity.canGenerateFog=false
            );

            leader = true;
        }
    }

    private void getFogletLeader(){
        if(!canGenerateFog && isAggressive()) {
            fogletLeader.clear();
            fogletLeader =
                    this.level().getEntitiesOfClass(FogletEntity.class, this.getBoundingBox().inflate(20, 20, 20),
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

        if(this.getIsFog() && MoonDustBomb.checkEffectAndSplinters(this)){
            this.setIsFog(false);
        }

        NoParticles:
        if(!(this instanceof FoglingEntity)){
            this.getOtherFoglet();
            this.getFogletLeader();
            this.getFoglings();

            if(!this.level().isClientSide && MoonDustBomb.checkEffectAndSplinters(this)){
                break NoParticles;
            }

            if(this.level().isClientSide && this.isAggressive() && canGenerateFog) {
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

        if(this.getIsFog() && !this.isAggressive()){
            this.setIsFog(false);
        }

        if(this.getAnimationTicks() > 0 && !this.isAggressive()){
            this.setAnimationTicks(-1);
        }

        this.updateFloating();

        this.checkInsideBlocks();
        super.tick();
    }

    private void getFoglings(){
        if(isAggressive() && this.foglingsList.isEmpty()) {
            foglingsList =
                    this.level().getEntitiesOfClass(FoglingEntity.class, this.getBoundingBox().inflate(20, 20, 20),
                            fogling ->
                                    fogling.getOwner() != null && fogling.getOwner().getUUID() == this.getUUID());
        }
    }

    public final boolean getIsFog() {
        return this.entityData.get(ACTIVATES_FOG);
    }

    public final void setIsFog(final boolean wasFog) {
        this.entityData.set(ACTIVATES_FOG, wasFog);
    }

    public float getAlphaValue() {
        return this.entityData.get(ALPHA_VALUE);
    }

    public void setAlphaValue(final float AlphaValue) {
        this.entityData.set(ALPHA_VALUE, AlphaValue);
    }


    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ACTIVATES_FOG, Boolean.FALSE);
        builder.define(ALPHA_VALUE, 1f);
    }

    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("InFog", this.entityData.get(ACTIVATES_FOG));
        nbt.putFloat("AlphaValue", this.entityData.get(ALPHA_VALUE));
    }
    @Override
    public void readAdditionalSaveData(final CompoundTag nbt) {
        this.setIsFog(nbt.getBoolean("InFog"));
        this.setAlphaValue(nbt.getFloat("AlphaValue"));
        super.readAdditionalSaveData(nbt);
    }
    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.getSoundEvent("foglet_idle");
    }

    @Override
    public boolean canStandOnFluid(final FluidState state) {
        return ((state.is(FluidTags.LAVA) || state.is(FluidTags.WATER)) && this.getIsFog());
    }

    protected void updateFloating() {
        if ((this.isInLava() || this.isInWater()) && this.getIsFog()) {
            final CollisionContext shapeContext = CollisionContext.of(this);
            if (!shapeContext.isAbove(LiquidBlock.STABLE_SHAPE, this.blockPosition(), true) || this.level().getFluidState(this.blockPosition().above()).is(FluidTags.LAVA)
                    || this.level().getFluidState(this.blockPosition().above()).is(FluidTags.WATER)
            ) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5).add(0.0, 0.05, 0.0));
            } else {
                this.setOnGround(true);
            }
        }
    }

    public boolean isInWater() {
        return !this.firstTick && this.fluidHeight.getDouble(FluidTags.WATER) > 0.0;
    }

    @Override
    public boolean fireImmune() {
        return super.fireImmune() || this.getIsFog();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("foglet_hurt");
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("foglet_death");
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("foglet_attack");
    }

    @Override
    public boolean hurt(@NotNull final DamageSource source, float amount) {
        if(this.getIsFog()){
            amount=amount/8;
        }

        return super.hurt(source, amount);
    }

    @Override
    public void die(@NotNull final DamageSource damageSource) {
        if(!(this.foglingsList.isEmpty())){
            this.foglingsList.forEach(
                    foglingEntity -> foglingEntity.hurt(this.damageSources().magic(), 10)
            );
        }
        super.die(damageSource);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull final DamageSource damageSource) {
        return this.getAnimationTicks() > 0 || this.getIsFog() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

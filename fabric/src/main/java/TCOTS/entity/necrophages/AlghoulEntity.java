package TCOTS.entity.necrophages;

import TCOTS.entity.goals.LungeAttackGoal;
import TCOTS.entity.goals.ReturnToNestGoal;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.animal.horse.ZombieHorse;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

public class AlghoulEntity extends GhoulEntity implements GeoEntity {
    //xTODO: Change sounds
    //xTODO: Modify goals
        //xTODO: Can invoke enraged mode even if you punch it
        //xTODO: Makes smoke
        //xTODO: Push other entities

        //xTODO: Makes other ghouls go in frenzy
    //xTODO: Add spikes layer
    //xTODO: Add thorns attack

    public final int GHOUL_REGENERATION_TIME=300;

    @Override
    public int getGHOUL_REGENERATION_TIME() {
        return GHOUL_REGENERATION_TIME;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AlghoulEntity(EntityType<? extends AlghoulEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward=10;
    }

    public static final RawAnimation SCREAM = RawAnimation.begin().thenPlay("special.scream");

    protected static final EntityDataAccessor<Boolean> SPIKED = SynchedEntityData.defineId(AlghoulEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> TIME_FOR_SPIKES = SynchedEntityData.defineId(AlghoulEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> IS_SCREAMING = SynchedEntityData.defineId(AlghoulEntity.class, EntityDataSerializers.BOOLEAN);



    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.29f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new AlghoulSpikedRegeneration(this,500, GHOUL_REGENERATION_TIME,
                0.5f, 60,1200));

        this.goalSelector.addGoal(2, new AlghoulCall(this,300,30));

        this.goalSelector.addGoal(3, new LungeAttackGoal(this, 200, 1.3,15,40));

        this.goalSelector.addGoal(4, new Ghoul_MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.addGoal(5, new GhoulGoForFlesh(this,1D));

        this.goalSelector.addGoal(6, new ReturnToNestGoal(this,0.75));

        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.75f,80));

        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, GhoulEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zoglin.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombieHorse.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));

        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Hoglin.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Cow.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Pig.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Goat.class, true));
    }

    private static class AlghoulSpikedRegeneration extends GhoulRegeneration{
        private final int TimeForSpikes;
        private final AlghoulEntity mob;

        public AlghoulSpikedRegeneration(AlghoulEntity mob, int CooldownBetweenRegens, int TimeForRegen, float HealthPercentageToStart, int stopTicks, int TimeForSpikes) {
            super(mob, 0, CooldownBetweenRegens, TimeForRegen, HealthPercentageToStart, stopTicks);
            this.TimeForSpikes=TimeForSpikes;
            this.mob=mob;
        }

        @Override
        public void stop() {
            if(!(this.mob.getIsSpiked())) {
                this.mob.setIsSpiked(true);
                if (!this.mob.isSilent()) {
                    this.mob.playSound(TCOTS_Sounds.ALGHOUL_SPIKES, 1, 1);
                }
            }
            mob.setTimeForSpikes(TimeForSpikes);
            mob.setHasCooldownForScream(true);
            mob.setCooldownForScream(50);
            super.stop();
        }
        @Override
        public void start() {
            super.start();
            pushEntities(5,2,5,1);
        }

        @Override
        public void tick() {
            pushEntities(3,1,3,0.1);

            super.tick();
        }

        private void pushEntities(double xExpansion, double yExpansion, double zExpansion,  double knockbackStrength){
            List<LivingEntity> listMobs= mob.level().getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(xExpansion,yExpansion,zExpansion),
                    livingEntity -> !(livingEntity instanceof GhoulEntity));

            for (LivingEntity entity : listMobs){
                double d = mob.getX() - entity.getX();
                double e = mob.getZ() - entity.getZ();
                entity.knockback(knockbackStrength,d,e);
                if(entity instanceof ServerPlayer && !((ServerPlayer) entity).isCreative()){
                    ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity), null);
                }
            }
        }

        @Override
        protected boolean canStartRegen() {
            return !(mob.getIsRegenerating()) && (mob.getHealth() < (mob.getMaxHealth() * healthPercentage)) && mob.onGround() && !(mob.hasCooldownForRegen()) && !MoonDustBomb.checkEffectAndSplinters(mob);
        }
    }

    private static class AlghoulCall extends Goal{

        private final AlghoulEntity alghoul;
        private final int cooldownForScream;
        private final int stopTicks;

        public AlghoulCall(AlghoulEntity alghoul, int cooldownForScream, int stopTicks){
            this.alghoul=alghoul;
            this.cooldownForScream=cooldownForScream;
            this.stopTicks=stopTicks;
        }
        @Override
        public boolean canUse() {
            List<GhoulEntity> listGhouls = generateGhoulList();

            return (alghoul.getIsSpiked() || alghoul.isAggressive()) && !listGhouls.isEmpty() && !alghoul.hasCooldownForScream() && alghoul.onGround() && !MoonDustBomb.checkEffectAndSplinters(alghoul);
        }

        @Override
        public boolean canContinueToUse() {
            List<GhoulEntity> listGhouls = generateGhoulList();
            return !listGhouls.isEmpty() && !alghoul.hasCooldownForScream() && !MoonDustBomb.checkEffectAndSplinters(alghoul);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        private int StoppedTicks;
        @Override
        public void start() {
            StoppedTicks=stopTicks;
            alghoul.getNavigation().stop();
            alghoul.playSound(alghoul.getScreamSound(),1,1);
            alghoul.setIsScreaming(true);
            pushEntities();
            alghoul.triggerAnim("ScreamController", "scream");

            super.start();
        }

        @Override
        public void stop() {
            alghoul.setCooldownForScream(cooldownForScream);
            alghoul.setHasCooldownForScream(true);
            StoppedTicks=stopTicks;
            alghoul.setIsScreaming(false);
        }

        @Override
        public void tick() {
            if (StoppedTicks > 0) {
                alghoul.getNavigation().stop();
                --StoppedTicks;
            } else {
                stop();
            }

            List<GhoulEntity> listGhouls = generateGhoulList();

            for(GhoulEntity ghoul: listGhouls){
                ghoul.setTimeForRegen(200);
                ghoul.setIsRegenerating(!MoonDustBomb.checkEffectAndSplinters(ghoul));
                if (!ghoul.isSilent()) {
                    this.alghoul.level().broadcastEntityEvent(ghoul, GHOUL_REGENERATING);
                }
            }

            super.tick();
        }

        private void pushEntities(){
            List<LivingEntity> listMobs= alghoul.level().getEntitiesOfClass(LivingEntity.class, alghoul.getBoundingBox().inflate(5, 2, 5),
                    livingEntity -> !(livingEntity instanceof GhoulEntity));

            for (LivingEntity entity : listMobs){
                double d = alghoul.getX() - entity.getX();
                double e = alghoul.getZ() - entity.getZ();
                entity.knockback(0.5,d,e);
                if(entity instanceof ServerPlayer && !((ServerPlayer) entity).isCreative()){
                    ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity), null);
                }
            }
        }

        private List<GhoulEntity> generateGhoulList(){
            return alghoul.level().getEntitiesOfClass(GhoulEntity.class, alghoul.getBoundingBox().inflate(5,2,5),
                    ghoul -> !(ghoul instanceof AlghoulEntity) && !ghoul.getIsRegenerating());
        }

    }



    @Override
    public boolean isPushable() {
        if(this.getIsInvokingRegen() || this.getIsScreaming()){
            return false;
        }
        return super.isPushable();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPIKED, Boolean.FALSE);
        builder.define(TIME_FOR_SPIKES, 0);
        builder.define(IS_SCREAMING, Boolean.FALSE);
    }

    private int cooldownForScream;

    public void setCooldownForScream(int cooldownForRegen) {
        this.cooldownForScream = cooldownForRegen;
    }

    public int getCooldownForScream() {
        return cooldownForScream;
    }

    boolean hasCooldownForScream=false;

    public boolean hasCooldownForScream() {
        return hasCooldownForScream;
    }

    public void setHasCooldownForScream(boolean hasCooldownForRegen) {
        this.hasCooldownForScream = hasCooldownForRegen;
    }

    public boolean getIsSpiked() {
        return this.entityData.get(SPIKED);
    }

    public void setIsSpiked(boolean isSpiked) {
        this.entityData.set(SPIKED, isSpiked);
    }

    protected void setTimeForSpikes(int timeForSpikes) {
        this.entityData.set(TIME_FOR_SPIKES, timeForSpikes);
    }

    protected int getTimeForSpikes() {
        return this.entityData.get(TIME_FOR_SPIKES);
    }

    public void setIsScreaming(boolean isScreaming) {
        this.entityData.set(IS_SCREAMING, isScreaming);
    }

    public boolean getIsScreaming() {
        return this.entityData.get(IS_SCREAMING);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || this.getIsInvokingRegen();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Spiked", getIsSpiked());
        nbt.putInt("SpikedTime", getTimeForSpikes());
        nbt.putInt("ScreamCooldown", getCooldownForScream());
        nbt.putBoolean("CooldownScreamActive", hasCooldownForScream());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setIsSpiked(nbt.getBoolean("Spiked"));
        this.setTimeForSpikes(nbt.getInt("SpikedTime"));
        this.setCooldownForScream(nbt.getInt("ScreamCooldown"));
        this.setHasCooldownForScream(nbt.getBoolean("CooldownScreamActive"));
    }

    int counter;
    @Override
    public void tick() {
        if(getIsSpiked() && MoonDustBomb.checkEffectAndSplinters(this)){
            setIsSpiked(false);
        }

        if(counter>0){
            counter=0;
        }

        tickTimersAlghoul();

        if(getIsInvokingRegen()){
            spawnSmokeParticles(counter);
        }

        if(getIsScreaming()){
            spawnSmokeParticles(counter);
        }

        super.tick();
    }

    private void tickTimersAlghoul(){
        if(this.getCooldownForScream()>0){
            this.setCooldownForScream(this.getCooldownForScream()-1);
        } else if (hasCooldownForScream()) {
            this.setHasCooldownForScream(false);
        }

        if(this.getTimeForSpikes() > 0){
            this.setTimeForSpikes(getTimeForSpikes()-1);
        } else if (getIsSpiked()) {
            if (!this.isSilent()) {
                this.playSound(TCOTS_Sounds.ALGHOUL_SPIKES,1,1);
            }
            this.setIsSpiked(false);
        }
    }

    private void spawnSmokeParticles(int counter){
        Vec3 vec3d = this.getBoundingBox().getCenter();
        while (counter < 20) {
            double d = this.random.nextGaussian() * 0.2;
            double e = this.random.nextGaussian() * 0.2;
            double f = this.random.nextGaussian() * 0.2;
            this.level().addParticle(ParticleTypes.WHITE_SMOKE, vec3d.x, vec3d.y, vec3d.z, d, e, f);
            ++counter;
        }
    }


    @Override
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
                    1.2 + ((double)this.random.nextFloat() - 0.5) * 0.1)
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
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if(this.getIsSpiked()){
            if(source.getEntity()!= null && source.getEntity() instanceof LivingEntity attacker
                    && !((source.getDirectEntity() instanceof Projectile) || (source.getDirectEntity() instanceof AbstractArrow)) ){
                if(amount>0){
                    float mirrorDamage;

                    if(amount*0.6f < 18){
                        mirrorDamage=amount*0.6f;
                    } else {
                        mirrorDamage=18;}

                    attacker.hurt(attacker.damageSources().thorns(this), mirrorDamage);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.THORNS_HIT, this.getSoundSource(),1f,1f);
                }
            }
        }

        return super.hurt(source, amount);
    }

    @Override
    protected int getTotalEatingTime() {
        return 10+this.getRandom().nextIntBetweenInclusive(0,8);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return TCOTS_Sounds.ALGHOUL_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.ALGHOUL_DEATH;
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
        controllerRegistrar.add(new AnimationController<>(this, "RegenController", 1, state -> {state.setControllerSpeed(0.5f);return PlayState.STOP;})
                .triggerableAnim("start_regen", START_REGEN)
        );

        //Scream Controller
        controllerRegistrar.add(new AnimationController<>(this, "ScreamController", 1, state -> PlayState.STOP)
                .triggerableAnim("scream", SCREAM)
        );
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.ALGHOUL_IDLE;
    }

    @Nullable
    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.ALGHOUL_LUNGES;
    }

    @Nullable
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.ALGHOUL_ATTACK;
    }

    @Override
    protected SoundEvent getScreamSound() {
        return TCOTS_Sounds.ALGHOUL_SCREAMS;
    }

    @Override
    public SoundEvent getRegeneratingSound(){
        return TCOTS_Sounds.ALGHOUL_REGEN;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

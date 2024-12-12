package TCOTS.entity.necrophages;

import TCOTS.entity.goals.LungeAttackGoal;
import TCOTS.entity.goals.ReturnToNestGoal;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
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

    public AlghoulEntity(EntityType<? extends AlghoulEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints=10;
    }

    public static final RawAnimation SCREAM = RawAnimation.begin().thenPlay("special.scream");

    protected static final TrackedData<Boolean> SPIKED = DataTracker.registerData(AlghoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> TIME_FOR_SPIKES = DataTracker.registerData(AlghoulEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Boolean> IS_SCREAMING = DataTracker.registerData(AlghoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);



    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.29f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.3f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new AlghoulSpikedRegeneration(this,500, GHOUL_REGENERATION_TIME,
                0.5f, 60,1200));

        this.goalSelector.add(2, new AlghoulCall(this,300,30));

        this.goalSelector.add(3, new LungeAttackGoal(this, 200, 1.3,15,40));

        this.goalSelector.add(4, new Ghoul_MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.add(5, new GhoulGoForFlesh(this,1D));

        this.goalSelector.add(6, new ReturnToNestGoal(this,0.75));

        this.goalSelector.add(7, new WanderAroundGoal(this, 0.75f,80));

        this.goalSelector.add(8, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, GhoulEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZombieEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZoglinEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZombieHorseEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        this.targetSelector.add(4, new ActiveTargetGoal<>(this, HoglinEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, CowEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, PigEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, SheepEntity.class, true));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, GoatEntity.class, true));
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
            List<LivingEntity> listMobs= mob.getWorld().getEntitiesByClass(LivingEntity.class, mob.getBoundingBox().expand(xExpansion,yExpansion,zExpansion),
                    livingEntity -> !(livingEntity instanceof GhoulEntity));

            for (LivingEntity entity : listMobs){
                double d = mob.getX() - entity.getX();
                double e = mob.getZ() - entity.getZ();
                entity.takeKnockback(knockbackStrength,d,e);
                if(entity instanceof ServerPlayerEntity && !((ServerPlayerEntity) entity).isCreative()){
                    ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity), null);
                }
            }
        }

        @Override
        protected boolean canStartRegen() {
            return !(mob.getIsRegenerating()) && (mob.getHealth() < (mob.getMaxHealth() * healthPercentage)) && mob.isOnGround() && !(mob.hasCooldownForRegen()) && !MoonDustBomb.checkEffectAndSplinters(mob);
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
        public boolean canStart() {
            List<GhoulEntity> listGhouls = generateGhoulList();

            return (alghoul.getIsSpiked() || alghoul.isAttacking()) && !listGhouls.isEmpty() && !alghoul.hasCooldownForScream() && alghoul.isOnGround() && !MoonDustBomb.checkEffectAndSplinters(alghoul);
        }

        @Override
        public boolean shouldContinue() {
            List<GhoulEntity> listGhouls = generateGhoulList();
            return !listGhouls.isEmpty() && !alghoul.hasCooldownForScream() && !MoonDustBomb.checkEffectAndSplinters(alghoul);
        }

        @Override
        public boolean shouldRunEveryTick() {
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
                    this.alghoul.getWorld().sendEntityStatus(ghoul, GHOUL_REGENERATING);
                }
            }

            super.tick();
        }

        private void pushEntities(){
            List<LivingEntity> listMobs= alghoul.getWorld().getEntitiesByClass(LivingEntity.class, alghoul.getBoundingBox().expand(5, 2, 5),
                    livingEntity -> !(livingEntity instanceof GhoulEntity));

            for (LivingEntity entity : listMobs){
                double d = alghoul.getX() - entity.getX();
                double e = alghoul.getZ() - entity.getZ();
                entity.takeKnockback(0.5,d,e);
                if(entity instanceof ServerPlayerEntity && !((ServerPlayerEntity) entity).isCreative()){
                    ((ServerPlayerEntity) entity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(entity), null);
                }
            }
        }

        private List<GhoulEntity> generateGhoulList(){
            return alghoul.getWorld().getEntitiesByClass(GhoulEntity.class, alghoul.getBoundingBox().expand(5,2,5),
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
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SPIKED, Boolean.FALSE);
        this.dataTracker.startTracking(TIME_FOR_SPIKES, 0);
        this.dataTracker.startTracking(IS_SCREAMING, Boolean.FALSE);
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
        return this.dataTracker.get(SPIKED);
    }

    public void setIsSpiked(boolean isSpiked) {
        this.dataTracker.set(SPIKED, isSpiked);
    }

    protected void setTimeForSpikes(int timeForSpikes) {
        this.dataTracker.set(TIME_FOR_SPIKES, timeForSpikes);
    }

    protected int getTimeForSpikes() {
        return this.dataTracker.get(TIME_FOR_SPIKES);
    }

    public void setIsScreaming(boolean isScreaming) {
        this.dataTracker.set(IS_SCREAMING, isScreaming);
    }

    public boolean getIsScreaming() {
        return this.dataTracker.get(IS_SCREAMING);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || this.getIsInvokingRegen();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Spiked", getIsSpiked());
        nbt.putInt("SpikedTime", getTimeForSpikes());
        nbt.putInt("ScreamCooldown", getCooldownForScream());
        nbt.putBoolean("CooldownScreamActive", hasCooldownForScream());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
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
        Vec3d vec3d = this.getBoundingBox().getCenter();
        while (counter < 20) {
            double d = this.random.nextGaussian() * 0.2;
            double e = this.random.nextGaussian() * 0.2;
            double f = this.random.nextGaussian() * 0.2;
            this.getWorld().addParticle(ParticleTypes.SMOKE, vec3d.x, vec3d.y, vec3d.z, d, e, f);
            ++counter;
        }
    }


    @Override
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
                    1.2 + ((double)this.random.nextFloat() - 0.5) * 0.1)
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
    public boolean damage(DamageSource source, float amount) {
        if(this.getIsSpiked()){
            if(source.getAttacker()!= null && source.getAttacker() instanceof LivingEntity attacker
                    && !((source.getSource() instanceof ProjectileEntity) || (source.getSource() instanceof PersistentProjectileEntity)) ){
                if(amount>0){
                    float mirrorDamage;

                    if(amount*0.6f < 18){
                        mirrorDamage=amount*0.6f;
                    } else {
                        mirrorDamage=18;}

                    attacker.damage(attacker.getDamageSources().thorns(this), mirrorDamage);
                    this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENCHANT_THORNS_HIT, this.getSoundCategory(),1f,1f);
                }
            }
        }

        return super.damage(source, amount);
    }

    @Override
    protected int getTotalEatingTime() {
        return 10+this.getRandom().nextBetween(0,8);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
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

package TCOTS.entity.ogroids;

import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.entity.misc.Troll_RockProjectileEntity;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class RockTrollEntity extends OgroidMonster implements GeoEntity, RangedAttackMob {
    //xTODO: Add sounds
    //xTODO: Add attacks
    //      x Block
    //      x Throw Rock
    //xTODO: Add drops
    //xTODO: Add mutagen and decoction
    //TODO: Add villager-like system
    //TODO  Add Villager-Like sounds using voice dialogue
    //      If you have neutral reputation, they just talk normally ("Talk?")
    //      If you have bad reputation,  they say other things
    //      If you have good reputation, they are happy to see u
    //      You can give them rocks (They say "Good!" or "Rocks!")
    //      They give you something, and also increases reputation (They say "Take!" or something)
    //TODO: Add Follower/Pet System
    //TODO: Add bestiary description
    //TODO: Add spawn
    //TODO: Add possible village-like structure?

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation BLOCK = RawAnimation.begin().thenPlayAndHold("special.block");
    public static final RawAnimation UNBLOCK = RawAnimation.begin().thenPlay("special.unblock");
    public static final RawAnimation ATTACK_THROW_ROCK = RawAnimation.begin().thenPlay("attack.rock_throw");


    protected static final TrackedData<Boolean> BLOCKING = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> TIME_BLOCKING = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public RockTrollEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new ProjectileAttackGoal_Troll(this,1.2D,30, 10.0f, 40f));

        this.goalSelector.add(2, new MeleeAttackGoal_Troll(this, 1.2D, false));

        this.goalSelector.add(3, new WanderAroundGoal_Troll(this, 0.75f, 20));

        this.goalSelector.add(4, new LookAroundGoal_Troll(this));

        //TODO: Disable this to make it neutral
        this.targetSelector.add(0, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, VillagerEntity.class, true));

        this.targetSelector.add(1, new RevengeGoal(this));
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        this.getNavigation().stop();
        Troll_RockProjectileEntity rockProjectileEntity = new Troll_RockProjectileEntity(this.getWorld(), this, 8);
        double d = target.getEyeY() - (double)1.1f;
        double e = target.getX() - this.getX();
        double f = d - rockProjectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(e * e + g * g) * (double)0.2f;
        rockProjectileEntity.setVelocity(e, f + h, g, 1.8f, 4.0f);
        this.triggerAnim("RockController", "rock_attack");
        this.playSound(TCOTS_Sounds.ROCK_PROJECTILE_THROWS, 1.0f, 0.4f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.getWorld().spawnEntity(rockProjectileEntity);

    }

    private static class MeleeAttackGoal_Troll extends MeleeAttackGoal_Animated{

        private final RockTrollEntity troll;

        public MeleeAttackGoal_Troll(RockTrollEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.troll=mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !troll.isBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !troll.isBlocking();
        }
    }

    private static class WanderAroundGoal_Troll extends WanderAroundGoal{
        private final RockTrollEntity troll;
        public WanderAroundGoal_Troll(RockTrollEntity mob, double speed, int chance) {
            super(mob, speed, chance);
            this.troll=mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.troll.isBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !this.troll.isBlocking();
        }
    }

    private static class LookAroundGoal_Troll extends LookAroundGoal{
        private final RockTrollEntity troll;
        public LookAroundGoal_Troll(RockTrollEntity mob) {
            super(mob);
            this.troll=mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.troll.isBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !this.troll.isBlocking();
        }
    }

    private static class ProjectileAttackGoal_Troll extends ProjectileAttackGoal {
        private final RockTrollEntity troll;
        private final float distanceForAttack;

        public ProjectileAttackGoal_Troll(RockTrollEntity mob, double mobSpeed, int intervalTicks, float maxShootRange, float distanceForAttack) {
            super(mob, mobSpeed, intervalTicks, maxShootRange);
            this.troll=mob;
            this.distanceForAttack=distanceForAttack;
        }

        private boolean distanceCondition(){
            if(troll.getTarget() != null){
                LivingEntity target = this.troll.getTarget();
                double d = this.troll.squaredDistanceTo(target);

                return !(d < distanceForAttack);
            }
            else {
                return true;
            }
        }

        @Override
        public boolean canStart() {
            return super.canStart()
                    && distanceCondition()
                    && !this.troll.isBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && distanceCondition();
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        //If attacks using a pickaxe
        if(isBlocking() &&
                source.getAttacker()!=null && source.getAttacker() instanceof LivingEntity attacker
                && attacker.getMainHandStack().getItem() instanceof PickaxeItem
                && !source.isIn(DamageTypeTags.IS_PROJECTILE)){
            this.playSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT_BREAK, 1.0f, 1.0f);
            this.setIsBlocking(false);
            this.setTimeBlocking(0);
        }

        if (this.isInvulnerableTo(source) && this.isBlocking()) {
            this.playSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT, 1.0f, 1.0f);
        }

        boolean damage = super.damage(source, amount);

        //If isn't already blocking and is alive
        if(!this.getWorld().isClient && !isBlocking() && this.isAlive()) {
            //If it has less than half its max life, the probability it's 1/5, else it's 1/10
            if(this.getHealth() <= this.getMaxHealth()/2){
                if(this.random.nextInt()%5==0){
                    this.setIsBlocking(true);
                }
            } else {
                if(this.random.nextInt()%10==0){
                    this.setIsBlocking(true);
                }
            }


        }

        return damage;
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        if(this.isBlocking()){
            setTimeBlocking(getTimeBlocking()+1);
        }

        if(this.isBlocking() && this.getTimeBlocking()==80){
            this.setIsBlocking(false);
            setTimeBlocking(0);
        }
    }

    @Override
    public int getMaxHeadRotation() {
        return super.getMaxHeadRotation();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5f)
                .add(EntityAttributes.GENERIC_ARMOR, 8f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BLOCKING, Boolean.FALSE);
        this.dataTracker.startTracking(TIME_BLOCKING, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Blocking", this.isBlocking());
        nbt.putInt("TimeBlocking", this.getTimeBlocking());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setIsBlocking(nbt.getBoolean("Blocking"));
        setTimeBlocking(nbt.getInt("TimeBlocking"));
    }

    public void setIsBlocking(boolean isBlocking) {
        this.dataTracker.set(BLOCKING, isBlocking);
        if(!isBlocking){
            this.triggerAnim("BlockController", "unblock");
        }
    }

    public boolean isBlocking() {
        return this.dataTracker.get(BLOCKING);
    }

    public void setTimeBlocking(int timeBlocking) {
        this.dataTracker.set(TIME_BLOCKING, timeBlocking);
    }

    public int getTimeBlocking() {
        return this.dataTracker.get(TIME_BLOCKING);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, GeoControllersUtil::idleWalkRunController)
        );

        //Attack Controller
        controllerRegistrar.add(GeoControllersUtil.attackController(this, 2));

        //Block Control
        controllerRegistrar.add(
                new AnimationController<>(this, "BlockController", 1, state -> {
                    if(this.isBlocking()){
                        state.setAnimation(BLOCK);
                        return PlayState.CONTINUE;
                    } else {
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
        }).triggerableAnim("unblock", UNBLOCK));

        //Rock throw Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "RockController", 1, state -> PlayState.STOP)
                        .triggerableAnim("rock_attack", ATTACK_THROW_ROCK)
        );
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.isBlocking() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isBlocking();
    }

    @Override
    public boolean isCollidable() {
        return this.isBlocking() && this.isAlive();
    }

    //Sounds
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.TROLL_ATTACK;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.TROLL_IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.TROLL_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.TROLL_HURT;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

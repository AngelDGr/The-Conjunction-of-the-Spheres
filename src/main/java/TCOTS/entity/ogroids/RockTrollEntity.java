package TCOTS.entity.ogroids;

import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.entity.misc.Troll_RockProjectileEntity;
import TCOTS.entity.ogroids.troll.TrollGossipsReputation;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import com.mojang.serialization.Dynamic;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;

public class RockTrollEntity extends OgroidMonster implements GeoEntity, RangedAttackMob, Angerable, Ownable, InteractionObserver {
    //xTODO: Add sounds
    //xTODO: Add attacks
    //      x Block
    //      x Throw Rock
    //xTODO: Add drops
    //xTODO: Add mutagen and decoction

    //TODO: Add villager-like system

    // They have two values; Reputation and Friendship.
    // Reputation determines if you can barter with them
    // Reputation it's individual for every troll, but they can communicate with other trolls to low your reputation
    // Friendship determines if they can follow you and guard different positions
    // You can give them alcohol or meat to increase friendship
    // You can give them Amethyst to increase reputation and a little friendship
    // If you give them Amethyst, they are going to barter like a Piglin
    // x They stare at you if you held a weapon, and you have neutral/low reputation
    // x They ATTACK you if you held a weapon, and you have low reputation
    // x They ATTACK you on sight if you have very low reputation
    // They can spawn as rabid trolls, trolls that are totally aggressive, killing them don't lower your reputation

    //TODO: Add Reputation/Friendship values
    //TODO: Add ways to increase/decrease values
    //TODO: Add Bartering system (Using Amethyst Shards)
    //TODO: Add Befriending system (Using Meat and Alcohol)
    //TODO: Add three states when they are friends; Follow, Guarding and Wandering
    // Follow: They follow you around and fight for you (Right click with HIGH friendship)
    // Guarding: You can set them in a specific position, similar to a dog sitting, they will protect the place (Right click)
    // Wandering: They are just gonna wander on their own, like a normal mob (Shift+Right click)
    // Orders:
    // If they are following you, you can use a stick to indicate them to move to that place, after that, they are gonna automatically going
    // to enter in the Guarding mode. If you have multiple Trolls following you, one randomly it's going to follow the order, if the Troll has a
    // name AND the stick has the same name, the specific named Troll it's going to move to that place, no matters if it's following you or not.
    // You can heal them using meat

    //TODO  Add Villager-Like sounds using voice dialogue
    //      If you have neutral reputation, they just talk normally ("Talk?")
    //      If you have bad reputation,  they say other things
    //      If you have good reputation, they are happy to see u
    //      You can Amethyst to them (They say "Good!" or "Rocks!")
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
    protected static final TrackedData<Boolean> RABID = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public RockTrollEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new ProjectileAttackGoal_Troll(this, 1.2D, 30, 10.0f, 40f));

        this.goalSelector.add(2, new MeleeAttackGoal_Troll(this, 1.2D, false));

        this.goalSelector.add(3, new LookAtPlayerWithWeaponGoal(this, PlayerEntity.class, 10.0f));

        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));

        this.goalSelector.add(4, new LookAtEntityGoal(this, VillagerEntity.class, 8.0f));

        this.goalSelector.add(4, new LookAtEntityGoal(this, RockTrollEntity.class, 8.0f));

        this.goalSelector.add(5, new WanderAroundGoal_Troll(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Troll(this));

        this.targetSelector.add(0, new RockTrollTargetWithReputationGoal(this));
        this.targetSelector.add(1, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, RaiderEntity.class, true));
        this.targetSelector.add(5, new TrollUniversalAngerGoal<>(this, true));
    }

    private static class MeleeAttackGoal_Troll extends MeleeAttackGoal_Animated {

        private final RockTrollEntity troll;

        public MeleeAttackGoal_Troll(RockTrollEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.troll = mob;
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
    private static class WanderAroundGoal_Troll extends WanderAroundGoal {
        private final RockTrollEntity troll;

        public WanderAroundGoal_Troll(RockTrollEntity mob, double speed, int chance) {
            super(mob, speed, chance);
            this.troll = mob;
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
    private static class LookAroundGoal_Troll extends LookAroundGoal {
        private final RockTrollEntity troll;

        public LookAroundGoal_Troll(RockTrollEntity mob) {
            super(mob);
            this.troll = mob;
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
    private static class LookAtPlayerWithWeaponGoal extends LookAtEntityGoal {
        private final RockTrollEntity troll;

        public LookAtPlayerWithWeaponGoal(RockTrollEntity troll, Class<? extends PlayerEntity> targetType, float range) {
            this(troll, targetType, range, 1f);
        }

        public LookAtPlayerWithWeaponGoal(RockTrollEntity troll, Class<? extends PlayerEntity> targetType, float range, float chance) {
            this(troll, targetType, range, chance, false);
        }

        public LookAtPlayerWithWeaponGoal(RockTrollEntity troll, Class<? extends PlayerEntity> targetType, float range, float chance, boolean lookForward) {
            super(troll, targetType, range, chance, lookForward);
            this.troll=troll;
            this.setControls(EnumSet.of(Goal.Control.LOOK, Goal.Control.MOVE));

        }

        @Override
        public boolean canStart() {

            if (this.troll.getTarget() != null) {
                this.target = this.troll.getTarget();
            }
            this.target =
                    this.troll.getWorld().getClosestPlayer(TargetPredicate.createNonAttackable().setBaseMaxDistance(range)
                                    //If it has a reputation less than 25 and it's holding a Sword or Axe
                                    .setPredicate(player -> this.troll.getReputation((PlayerEntity) player) < 25
                                            && (player.getMainHandStack().getItem() instanceof SwordItem || player.getMainHandStack().getItem() instanceof AxeItem)),

                            this.troll, this.troll.getX(), this.troll.getEyeY(), this.troll.getZ());

            return this.target != null;
        }
    }
    private static class ProjectileAttackGoal_Troll extends ProjectileAttackGoal {
        private final RockTrollEntity troll;
        private final float distanceForAttack;

        public ProjectileAttackGoal_Troll(RockTrollEntity mob, double mobSpeed, int intervalTicks, float maxShootRange, float distanceForAttack) {
            super(mob, mobSpeed, intervalTicks, maxShootRange);
            this.troll = mob;
            this.distanceForAttack = distanceForAttack;
        }

        private boolean distanceCondition() {
            if (troll.getTarget() != null) {
                LivingEntity target = this.troll.getTarget();
                double d = this.troll.squaredDistanceTo(target);

                return !(d < distanceForAttack);
            } else {
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
    private static class TrollUniversalAngerGoal<T extends RockTrollEntity> extends Goal {

        public TrollUniversalAngerGoal(T troll, boolean triggerOthers) {
            this.troll = troll;
            this.triggerOthers = triggerOthers;
        }

        private static final int BOX_VERTICAL_EXPANSION = 10;
        private final T troll;
        private final boolean triggerOthers;
        private int lastAttackedTime;

        @Override
        public boolean canStart() {
            return this.troll.getWorld().getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) && this.canStartUniversalAnger();
        }

        private boolean canStartUniversalAnger() {
            return this.troll.getAttacker() != null && this.troll.getAttacker().getType() == EntityType.PLAYER && this.troll.getLastAttackedTime() > this.lastAttackedTime;
        }

        @Override
        public void start() {
            this.lastAttackedTime = this.troll.getLastAttackedTime();
            this.troll.universallyAnger();

            if (this.triggerOthers) {
                this.getOthersInRange().stream().filter(entity -> entity != this.troll).map(entity -> (Angerable) entity).forEach(Angerable::universallyAnger);
            }
        }

        private List<? extends RockTrollEntity> getOthersInRange() {
            double d = this.troll.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
            Box box = Box.from(this.troll.getPos()).expand(d, BOX_VERTICAL_EXPANSION, d);
            //This way only other Rabid can trigger Rabid
            //This way the Rabid can't trigger others neutral Trolls
            if (troll.isRabid()) {
                return this.troll.getWorld().getEntitiesByClass(RockTrollEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR
                        .and(entity -> ((RockTrollEntity) (entity)).isRabid()));
            } else {
                return this.troll.getWorld().getEntitiesByClass(RockTrollEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR);
            }
        }

    }
    public static class RockTrollTargetWithReputationGoal extends TrackTargetGoal {
        private final RockTrollEntity troll;
        @Nullable
        private LivingEntity target;
        private final TargetPredicate targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);

        public RockTrollTargetWithReputationGoal(RockTrollEntity troll) {
            super(troll, false, true);
            this.troll = troll;
            this.setControls(EnumSet.of(Goal.Control.TARGET));
        }

        @Override
        public boolean canStart() {
            Box box = this.troll.getBoundingBox().expand(10.0, 8.0, 10.0);
            this.target = null;

            List<RockTrollEntity> listTrolls = this.troll.getWorld().getTargets(RockTrollEntity.class, this.targetPredicate, this.troll, box);
            List<PlayerEntity> listPlayers = this.troll.getWorld().getPlayers(this.targetPredicate, this.troll, box);
            //To add itself to the list
            listTrolls.add(this.troll);


            //Attack defending other trolls
            for (RockTrollEntity trollAround : listTrolls) {
                //To ignore reputation with rabid trolls
                if (trollAround.isRabid())
                    continue;

                for (PlayerEntity playerEntity : listPlayers) {
                    int reputation = trollAround.getReputation(playerEntity);
                    //If the reputation is below or equals -75, attack when the player it's holding a weapon (Axe or Sword)
                    if (reputation <= -75 && (playerEntity.getMainHandStack().getItem() instanceof SwordItem || playerEntity.getMainHandStack().getItem() instanceof AxeItem)) {
                        this.target = playerEntity;
                    }

                    //If the reputation is below -100, attack on sight
                    if (reputation <= -100) {
                        this.target = playerEntity;
                    }
                }
            }

            if (this.target == null) {
                return false;
            }

            return !(this.target instanceof PlayerEntity) || !this.target.isSpectator() && !((PlayerEntity) this.target).isCreative();
        }

        @Override
        public void start() {
            this.troll.setTarget(this.target);
            super.start();
        }
    }
    public static class TrollRevengeGoal extends TrackTargetGoal {
        private static final TargetPredicate VALID_AVOIDABLES_PREDICATE = TargetPredicate.createAttackable().ignoreVisibility().ignoreDistanceScalingFactor();
        private static final int BOX_VERTICAL_EXPANSION = 10;
        private boolean groupRevenge;
        private int lastAttackedTime;
        private final Class<?>[] noRevengeTypes;
        @Nullable
        private Class<?>[] noHelpTypes;

        public TrollRevengeGoal(PathAwareEntity mob, Class<?>... noRevengeTypes) {
            super(mob, true);
            this.noRevengeTypes = noRevengeTypes;
            this.setControls(EnumSet.of(Goal.Control.TARGET));
        }

        @Override
        public boolean canStart() {
            int i = this.mob.getLastAttackedTime();
            LivingEntity livingEntity = this.mob.getAttacker();
            if (i == this.lastAttackedTime || livingEntity == null) {
                return false;
            }
            if (livingEntity.getType() == EntityType.PLAYER && this.mob.getWorld().getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER)) {
                return false;
            }
            for (Class<?> class_ : this.noRevengeTypes) {
                if (!class_.isAssignableFrom(livingEntity.getClass())) continue;
                return false;
            }
            return this.canTrack(livingEntity, VALID_AVOIDABLES_PREDICATE);
        }

        public TrollRevengeGoal setGroupRevenge(Class<?>... noHelpTypes) {
            this.groupRevenge = true;
            this.noHelpTypes = noHelpTypes;
            return this;
        }

        @Override
        public void start() {
            this.mob.setTarget(this.mob.getAttacker());
            this.target = this.mob.getTarget();
            this.lastAttackedTime = this.mob.getLastAttackedTime();
            this.maxTimeWithoutVisibility = 300;
            if (this.groupRevenge) {
                this.callSameTypeForRevenge();
            }
            super.start();
        }

        protected void callSameTypeForRevenge() {
            double d = this.getFollowRange();
            Box box = Box.from(this.mob.getPos()).expand(d, BOX_VERTICAL_EXPANSION, d);
            List<RockTrollEntity> list = this.mob.getWorld().getEntitiesByClass(RockTrollEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR);
            for (RockTrollEntity otherTrolls : list) {

                if (this.mob == otherTrolls
                        || otherTrolls.getTarget() != null
                        || this.mob instanceof TameableEntity && ((TameableEntity) this.mob).getOwner()
                        != otherTrolls.getOwner() || otherTrolls.isTeammate(this.mob.getAttacker())
                        || otherTrolls.isRabid()) continue;

                //The Rabid Trolls don't trigger others
                if (this.mob instanceof RockTrollEntity && ((RockTrollEntity) (this.mob)).isRabid()) {
                    continue;
                }

                if (this.noHelpTypes != null) {
                    boolean bl = false;
                    for (Class<?> class_ : this.noHelpTypes) {
                        if (otherTrolls.getClass() != class_) continue;
                        bl = true;
                        break;
                    }
                    if (bl) continue;
                }

                this.setMobEntityTarget(otherTrolls, this.mob.getAttacker());
            }
        }

        protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
            mob.setTarget(target);
        }
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        //If it's NOT rabid, doesn't attack the Villagers
        if(!this.isRabid() && target instanceof MerchantEntity){
            return false;
        }

        //If it's NOT rabid but the Raider already attack it, attacks the raider
        if(!this.isRabid() && (this.getLastAttacker() instanceof RaiderEntity)){
            return true;
        }

        //If it's NOT rabid, doesn't attack the Raider
        if(!this.isRabid() && target instanceof RaiderEntity){
            return false;
        }

        if (target instanceof PlayerEntity && this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        return target.canTakeDamage();
    }

    private boolean shouldAngerAtPlayer(LivingEntity entity) {
        if(this.isRabid()){
            return true;
        }
        if (!this.canTarget(entity)) {
            return false;
        }
        if (entity.getType() == EntityType.PLAYER && this.isUniversallyAngry(entity.getWorld())) {
            return true;
        }
        return entity.getUuid().equals(this.getAngryAt());
    }
    @Nullable
    @Override
    public Entity getOwner() {
        return null;
    }
    private long lastGossipDecayTime;
    private void decayGossip() {
        long l = this.getWorld().getTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = l;
            return;
        }
        //Decays every Minecraft-Day
        if (l < this.lastGossipDecayTime + 24000L) {
            return;
        }
        this.gossipReputation.decay();
        this.lastGossipDecayTime = l;
    }

    @Override
    protected Text getDefaultName() {
        if(this.isRabid()){
            return Text.translatable(this.getType().getTranslationKey()+"_rabid");
        }

        return super.getDefaultName();
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

    private int angerTime;
    @Override
    public int getAngerTime() {
        return angerTime;
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    @Nullable
    private UUID angryAt;
    @Nullable
    @Override
    public UUID getAngryAt() {
        return angryAt;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt=angryAt;
    }
    private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
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

        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld)this.getWorld(), true);
        }
    }

    @Override
    public void tick() {
        super.tick();

        this.decayGossip();
    }

    @Override
    public int getMaxHeadRotation() {
        return super.getMaxHeadRotation();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5f)
                .add(EntityAttributes.GENERIC_ARMOR, 8f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2f);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if(spawnReason != SpawnReason.STRUCTURE){
            //1/20 probability to be a rabid troll
            if(random.nextInt()%20==0){
                this.setIsRabid(true);
            }
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BLOCKING, Boolean.FALSE);
        this.dataTracker.startTracking(TIME_BLOCKING, 0);
        this.dataTracker.startTracking(RABID, Boolean.FALSE);
    }

    //Reputation System
    private final TrollGossipsReputation gossipReputation = new TrollGossipsReputation();
    private final TrollGossipsReputation gossipsFriendship = new TrollGossipsReputation();

    //Bad Actions
    public static final EntityInteraction TROLL_HURT = EntityInteraction.create("troll_hurt");
    public static final EntityInteraction OTHER_TROLL_HURT = EntityInteraction.create("troll_hurt");
    public static final EntityInteraction TROLL_KILLED = EntityInteraction.create("troll_killed");

    //Good Actions
    public static final EntityInteraction TROLL_FED = EntityInteraction.create("troll_fed");
    public static final EntityInteraction TROLL_ALCOHOL = EntityInteraction.create("troll_alcohol");
    public static final EntityInteraction TROLL_BARTER = EntityInteraction.create("troll_trade");
    public static final EntityInteraction TROLL_DEFENDING = EntityInteraction.create("troll_defending");

    //Barter at 0 reputation
    //Follow at 100 friendship
    public int getReputation(PlayerEntity player) {
        return this.gossipReputation.getReputationFor(player.getUuid(), gossipType -> true);
    }
    public int getFriendship(PlayerEntity player) {
        return this.gossipReputation.getReputationFor(player.getUuid(), gossipType -> gossipType == TrollGossipsReputation.TrollGossipType.KILL_TROLL);
    }
    @Override
    public void onInteractionWith(EntityInteraction interaction, Entity entity) {
        //Good actions
        if(interaction == TROLL_DEFENDING){
            //+30 Reputation
            this.gossipReputation.startGossip(entity.getUuid(), TrollGossipsReputation.TrollGossipType.DEFENDING, 30);
            //+50 Friendship
        }
        else if(interaction == TROLL_ALCOHOL){
            //+10 Reputation
            this.gossipReputation.startGossip(entity.getUuid(), TrollGossipsReputation.TrollGossipType.FEEDING, 10);
            //+20 Friendship
        }
        else if(interaction == TROLL_FED){
            //+10 Reputation
            this.gossipReputation.startGossip(entity.getUuid(), TrollGossipsReputation.TrollGossipType.FEEDING, 10);
            //+15 Friendship
        }
        else if(interaction == TROLL_BARTER){
            //+5 Reputation
            this.gossipReputation.startGossip(entity.getUuid(), TrollGossipsReputation.TrollGossipType.BARTERING, 5);
            //+2 Friendship
        }
        //Bad actions
        else if (interaction == TROLL_KILLED) {
            //-80 Reputation
            this.gossipReputation.startGossip(entity.getUuid(), TrollGossipsReputation.TrollGossipType.KILL_TROLL, 80);
            //-50 Friendship
        } else if (interaction == TROLL_HURT) {
            //-25 Reputation
            this.gossipReputation.startGossip(entity.getUuid(), TrollGossipsReputation.TrollGossipType.HURT, 25);
            //-10 Friendship
        } else if (interaction == OTHER_TROLL_HURT){
            //-10 Reputation
            this.gossipReputation.startGossip(entity.getUuid(), TrollGossipsReputation.TrollGossipType.HURT, 10);
            //-5 Friendship
        }
    }

    @Override
    public void setAttacker(@Nullable LivingEntity attacker) {
        if (!this.isRabid() && attacker != null && this.getWorld() instanceof ServerWorld) {
            ((ServerWorld)this.getWorld()).handleInteraction(TROLL_HURT, attacker, this);
            for(RockTrollEntity nearTroll: getNearTrolls()){
                ((ServerWorld)this.getWorld()).handleInteraction(OTHER_TROLL_HURT, attacker, nearTroll);
            }
            if (this.isAlive() && attacker instanceof PlayerEntity) {
                this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
            }
        }
        super.setAttacker(attacker);
    }

    private List<RockTrollEntity> getNearTrolls(){
        return this.getWorld().getEntitiesByClass(RockTrollEntity.class, this.getBoundingBox().expand(10,2,10),
                troll -> troll.canSee(this) && troll != this && !troll.isRabid());
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        Entity attacker = damageSource.getAttacker();

        if(!this.isRabid() && this.getWorld() instanceof ServerWorld) {
            for (RockTrollEntity nearTroll : getNearTrolls()) {
                ((ServerWorld) this.getWorld()).handleInteraction(TROLL_KILLED, attacker, nearTroll);
            }
        }

        super.onDeath(damageSource);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if(item==Items.BEEF){
            if(!this.getWorld().isClient){
                ((ServerWorld)this.getWorld()).handleInteraction(TROLL_FED, player, this);
                itemStack.decrement(1);
                this.setPersistent();
            }
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            return ActionResult.CONSUME;
        }

        if(item ==Items.STICK){
            if(!this.getWorld().isClient && player instanceof ServerPlayerEntity){
                player.sendMessage(Text.literal("Reputation:"+this.getReputation(player)));
            }
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
        nbt.putBoolean("Blocking", this.isBlocking());
        nbt.putInt("TimeBlocking", this.getTimeBlocking());
        nbt.putBoolean("IsRabid", this.isRabid());

        nbt.put("Gossips", this.gossipReputation.serialize(NbtOps.INSTANCE));
        nbt.putLong("LastGossipDecay", this.lastGossipDecayTime);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.getWorld(), nbt);
        setIsBlocking(nbt.getBoolean("Blocking"));
        setTimeBlocking(nbt.getInt("TimeBlocking"));
        setIsRabid(nbt.getBoolean("IsRabid"));

        NbtList nbtList = nbt.getList("Gossips", NbtElement.COMPOUND_TYPE);
        this.gossipReputation.deserialize(new Dynamic<>(NbtOps.INSTANCE, nbtList));
        this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.ADD_VILLAGER_HEART_PARTICLES) {
            this.produceParticles(ParticleTypes.HEART);
        } else if (status == EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES) {
            this.produceParticles(ParticleTypes.ANGRY_VILLAGER);
        } else if (status == EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES) {
            this.produceParticles(ParticleTypes.HAPPY_VILLAGER);
        } else if (status == EntityStatuses.ADD_SPLASH_PARTICLES) {
            this.produceParticles(ParticleTypes.SPLASH);
        } else {
            super.handleStatus(status);
        }
    }

    protected void produceParticles(ParticleEffect parameters) {
        for (int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.getWorld().addParticle(parameters, this.getParticleX(1.0), this.getRandomBodyY() + 1.0, this.getParticleZ(1.0), d, e, f);
        }
    }

    public void setIsRabid(boolean isRabid) {
        this.dataTracker.set(RABID, isRabid);
    }

    public boolean isRabid() {
        return this.dataTracker.get(RABID);
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

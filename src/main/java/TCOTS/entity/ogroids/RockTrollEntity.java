package TCOTS.entity.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.AttackOwnerAttackerTarget;
import TCOTS.entity.goals.AttackOwnerEnemyTarget;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.entity.misc.Troll_RockProjectileEntity;
import TCOTS.items.HerbalMixture;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.WitcherPotions_Base;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;
import java.util.function.Predicate;

public class RockTrollEntity extends OgroidMonster implements GeoEntity, RangedAttackMob, Angerable, Ownable, InteractionObserver {
    //xTODO: Add sounds
    //xTODO: Add attacks
    //      x Block
    //      x Throw Rock
    //xTODO: Add drops
    //xTODO: Add mutagen and decoction

    //xTODO: Add villager-like system

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
    // x They can spawn as rabid trolls, trolls that are totally aggressive, killing them don't lower your reputation
    //xTODO: Add Reputation/Friendship values
    //xTODO: Add ways to increase/decrease values
    //xTODO: Add Bartering system (Using Amethyst Shards)
    //xTODO: Add Befriending system (Using Meat and Alcohol)
    //xTODO: Add three states when they are friends; Follow, Guarding and Wandering
    // xFollow: They follow you around and fight for you (Right click with HIGH friendship)
    // xGuarding: You can set them in a specific position, similar to a dog sitting, they will protect the place (Right click)
    // xWandering: They are just gonna wander on their own, like a normal mob (Shift+Right click)
    // Orders:
    // If they are following you, you can use a stick to indicate them to move to that place, after that, they are gonna automatically going
    // to enter in the Guarding mode.
    //xYou can heal them using meat

    //xTODO: They defend you when you are a friend (like foxes)
    //xTODO  Add Villager-Like sounds using voice dialogue
    //      If you have neutral reputation, they just talk normally ("Talk?")
    //      If you have bad reputation,  they say other things
    //      If you have good reputation, they are happy to see u
    //      You can Amethyst to them (They say "Good!" or "Rocks!")
    //      They give you something, and also increases reputation (They say "Take!" or something)
    //xTODO: Add Follower/Pet System
    //xTODO: Add bestiary description
    //xTODO: Add natural spawn
    //xTODO: Add possible village-like structure?

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation BLOCK = RawAnimation.begin().thenPlayAndHold("special.block");
    public static final RawAnimation UNBLOCK = RawAnimation.begin().thenPlay("special.unblock");
    public static final RawAnimation ATTACK_THROW_ROCK = RawAnimation.begin().thenPlay("attack.rock_throw");
    public static final RawAnimation GIVE_ITEM = RawAnimation.begin().thenPlay("special.give");

    protected static final TrackedData<Boolean> BLOCKING = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> TIME_BLOCKING = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Boolean> RABID = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> EATING_TIME = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<BlockPos> GUARDING_POS = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Integer> FOLLOWER_STATE = DataTracker.registerData(RockTrollEntity.class, TrackedDataHandlerRegistry.INTEGER);



    public RockTrollEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new ProjectileAttackGoal_Troll(this, 1.2D, 30, 10.0f, 40f));

        this.goalSelector.add(2, new MeleeAttackGoal_Troll(this, 1.2D, false));

        this.goalSelector.add(3, new LookAtItemInHand(this));

        this.goalSelector.add(4, new GoForItemInGroundGoal(this, 1.2D));

        this.goalSelector.add(5, new TrollFollowFriendGoal(this, 1.2D, 5.0f, 2.0f, false));

        this.goalSelector.add(6, new ReturnToGuardPosition(this, 1.2D));

        this.goalSelector.add(7, new LookAtPlayerWithWeaponGoal(this, PlayerEntity.class, 10.0f));

        this.goalSelector.add(8, new LookAtEntityGoal_Troll(this, PlayerEntity.class, 8.0f));

        this.goalSelector.add(9, new LookAtEntityGoal_Troll(this, VillagerEntity.class, 8.0f));

        this.goalSelector.add(10, new LookAtEntityGoal_Troll(this, RockTrollEntity.class, 8.0f));

        this.goalSelector.add(11, new WanderAroundGoal_Troll(this, 0.75f, 20));

        this.goalSelector.add(12, new LookAroundGoal_Troll(this));

        //Objectives
        this.targetSelector.add(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.add(1, new AttackOwnerEnemyTarget(this));

        //Defending a place
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MobEntity.class,
                5, true, false,
                entity -> entity instanceof Monster && !(entity instanceof CreeperEntity) && !(entity instanceof RockTrollEntity) && this.isWaiting()));

        this.targetSelector.add(3, new DefendFriendGoal(this, LivingEntity.class, false, true, entity ->
                entity instanceof PlayerEntity player?
                !(this.getFriendship(player) > 80 && this.getReputation(player) > 100):
                        !(entity instanceof RockTrollEntity troll) || troll.isRabid()));

        this.targetSelector.add(4, new RockTrollTargetWithReputationGoal(this));
        this.targetSelector.add(5, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.add(7, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(8, new ActiveTargetGoal<>(this, RaiderEntity.class, true));
        this.targetSelector.add(9, new TrollUniversalAngerGoal<>(this, true));
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
    private static class WanderAroundGoal_Troll extends WanderAroundGoal {
        private final RockTrollEntity troll;

        public WanderAroundGoal_Troll(RockTrollEntity mob, double speed, int chance) {
            super(mob, speed, chance);
            this.troll = mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.troll.isBlocking() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !this.troll.isBlocking() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering();
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
            return super.canStart() && !this.troll.isBlocking() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !this.troll.isBlocking() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol();
        }
    }
    private static class LookAtEntityGoal_Troll extends LookAtEntityGoal {

        private final RockTrollEntity troll;

        public LookAtEntityGoal_Troll(RockTrollEntity mob, Class<? extends LivingEntity> targetType, float range) {
            super(mob, targetType, range);
            this.troll=mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.troll.isBlocking() && !troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !this.troll.isBlocking() && !troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol();
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
                                    //If it has a reputation less than 25, and it's holding a Sword or Axe
                                    .setPredicate(player -> this.troll.getReputation((PlayerEntity) player) < 25
                                            && (player.getMainHandStack().getItem() instanceof SwordItem || player.getMainHandStack().getItem() instanceof AxeItem)),

                            this.troll, this.troll.getX(), this.troll.getEyeY(), this.troll.getZ());

            return this.target != null && !this.troll.hasBarteringItem() && !this.troll.isBlocking() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering();
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
        private static final TargetPredicate VALID_AVOIDABLE_PREDICATE = TargetPredicate.createAttackable().ignoreVisibility().ignoreDistanceScalingFactor();
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
            return this.canTrack(livingEntity, VALID_AVOIDABLE_PREDICATE);
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
                        || this.mob instanceof TameableEntity && ((TameableEntity) this.mob).getOwner() != otherTrolls.getOwner()
                        || otherTrolls.isTeammate(this.mob.getAttacker())
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
    public static class DefendFriendGoal extends ActiveTargetGoal<LivingEntity> {
        @Nullable
        private LivingEntity offender;
        @Nullable
        private LivingEntity friend;
        private int lastAttackedTime;
        private final RockTrollEntity troll;

        public DefendFriendGoal(RockTrollEntity troll, Class<LivingEntity> targetEntityClass, boolean checkVisibility, boolean checkCanNavigate, Predicate<LivingEntity> targetPredicate) {
            super(troll, targetEntityClass, 10, checkVisibility, checkCanNavigate, targetPredicate);
            this.troll=troll;
        }

        @Override
        public boolean canStart() {
            if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
                return false;
            }

            for (PlayerEntity player :
                    troll.getWorld().getEntitiesByClass(PlayerEntity.class, troll.getBoundingBox().expand(10, 5, 10),
                    player -> (troll.getReputation(player)>100) || (troll.getFriendship(player)>80))) {

                LivingEntity livingEntity;

                this.friend = livingEntity = player;

                this.offender = livingEntity.getAttacker();
                return livingEntity.getLastAttackedTime() != this.lastAttackedTime && this.canTrack(this.offender, this.targetPredicate);
            }

            return false;
        }

        @Override
        public void start() {
            this.setTargetEntity(this.offender);
            this.targetEntity = this.offender;
            if (this.friend != null) {
                this.lastAttackedTime = this.friend.getLastAttackedTime();
            }
            super.start();
        }
    }

    //Bartering
    private static class LookAtItemInHand extends Goal {

        private final RockTrollEntity troll;

        public LookAtItemInHand(RockTrollEntity troll){
            this.troll=troll;
            this.setControls(EnumSet.of(Control.LOOK));
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return troll.hasBarteringItem() || troll.hasFoodOrAlcohol();
        }

        @Override
        public void start() {
            troll.getNavigation().stop();
            super.start();
        }
    }
    private static class GoForItemInGroundGoal extends Goal {

        private final RockTrollEntity troll;
        private final double speed;

        public GoForItemInGroundGoal(RockTrollEntity troll, double speed){
            this.troll = troll;
            this.speed=speed;
        }

        @Override
        public boolean canStart() {
            List<ItemEntity> list = searchItemsList();

            return !list.isEmpty() && troll.canGather();
        }

        @Override
        public boolean shouldContinue() {
            List<ItemEntity> list = searchItemsList();

            return !list.isEmpty() && troll.canGather();
        }

        @Override
        public void start() {
            List<ItemEntity> list = searchItemsList();

            if (!list.isEmpty() && troll.canGather()) {
                this.troll.getNavigation().startMovingTo(list.get(0), speed);
            }
        }


        @Override
        public void tick() {
            List<ItemEntity> list = searchItemsList();

            if (!list.isEmpty() && troll.canGather()) {
                this.startMovingTo(troll.getNavigation(), list.get(0), speed);
                this.troll.getLookControl().lookAt(list.get(0), 30.0f, 30.0f);
            }
        }

        public void startMovingTo(EntityNavigation navigation, Entity entity, double speed) {
            Path path = navigation.findPathTo(entity, 0);
            if (path != null) {
                navigation.startMovingAlong(path, speed);
            }
        }

        private List<ItemEntity> searchItemsList(){
            return
                    this.troll.getWorld().getEntitiesByClass(ItemEntity.class,
                            this.troll.getBoundingBox().expand(8.0, 2.0, 8.0),
                            item ->
                                    !item.cannotPickup() && item.isAlive()
                                            && ((item.getStack().getItem() == RockTrollEntity.BARTERING_ITEM && troll.isWandering())
                                            || troll.isEdible(item.getStack().getItem()) || troll.isAlcohol(item.getStack().getItem())));
        }
    }

    //Following
    @SuppressWarnings("unused")
    private static class TrollFollowFriendGoal extends Goal {

        public static final int TELEPORT_DISTANCE = 12;
        private static final int HORIZONTAL_RANGE = 2;
        private static final int HORIZONTAL_VARIATION = 3;
        private static final int VERTICAL_VARIATION = 1;
        private final RockTrollEntity troll;
        private LivingEntity owner;
        private final WorldView world;
        private final double speed;
        private final EntityNavigation navigation;
        private int updateCountdownTicks;
        private final float maxDistance;
        private final float minDistance;
        private float oldWaterPathfindingPenalty;
        private final boolean leavesAllowed;

        public TrollFollowFriendGoal(RockTrollEntity troll, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
            this.troll = troll;
            this.world = troll.getWorld();
            this.speed = speed;
            this.navigation = troll.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.leavesAllowed = leavesAllowed;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
            if (!(troll.getNavigation() instanceof MobNavigation) && !(troll.getNavigation() instanceof BirdNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = (LivingEntity) troll.getOwner();
            if (livingEntity == null) {
                return false;
            }
            if (livingEntity.isSpectator()) {
                return false;
            }
            if (this.cannotFollow()) {
                return false;
            }
            if (this.troll.squaredDistanceTo(livingEntity) < (double)(this.minDistance * this.minDistance)) {
                return false;
            }
            this.owner = livingEntity;
            return true;
        }

        @Override
        public boolean shouldContinue() {
            if (this.navigation.isIdle()) {
                return false;
            }
            if (this.cannotFollow()) {
                return false;
            }
            return !(this.troll.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
        }

        private boolean cannotFollow() {
            return this.troll.isWaiting() || this.troll.hasVehicle() || this.troll.isLeashed();
        }

        @Override
        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.troll.getPathfindingPenalty(PathNodeType.WATER);
            this.troll.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        }

        @Override
        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.troll.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
        }

        @Override
        public void tick() {
            this.troll.getLookControl().lookAt(this.owner, 10.0f, this.troll.getMaxLookPitchChange());
            if (--this.updateCountdownTicks > 0) {
                return;
            }
            this.updateCountdownTicks = this.getTickCount(10);
            if (this.troll.squaredDistanceTo(this.owner) >= 324.0) {
                this.tryTeleport();
            } else {
                this.navigation.startMovingTo(this.owner, this.speed);
            }
        }

        private void tryTeleport() {
            BlockPos blockPos = this.owner.getBlockPos();
            for (int i = 0; i < 10; ++i) {
                int j = this.getRandomInt(-3, 3);
                int k = this.getRandomInt(-1, 1);
                int l = this.getRandomInt(-3, 3);
                boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
                if (!bl) continue;
                return;
            }
        }

        private boolean tryTeleportTo(int x, int y, int z) {
            if (Math.abs((double)x - this.owner.getX()) < 2.0 && Math.abs((double)z - this.owner.getZ()) < 2.0) {
                return false;
            }
            if (!this.canTeleportTo(new BlockPos(x, y, z))) {
                return false;
            }
            this.troll.refreshPositionAndAngles((double)x + 0.5, y, (double)z + 0.5, this.troll.getYaw(), this.troll.getPitch());
            this.navigation.stop();
            return true;
        }

        private boolean canTeleportTo(BlockPos pos) {
            PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
            if (pathNodeType != PathNodeType.WALKABLE) {
                return false;
            }
            BlockState blockState = this.world.getBlockState(pos.down());
            if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
                return false;
            }
            BlockPos blockPos = pos.subtract(this.troll.getBlockPos());
            return this.world.isSpaceEmpty(this.troll, this.troll.getBoundingBox().offset(blockPos));
        }

        private int getRandomInt(int min, int max) {
            return this.troll.getRandom().nextInt(max - min + 1) + min;
        }
    }
    private static class ReturnToGuardPosition extends Goal {

        private final RockTrollEntity troll;
        private final double speed;

        public ReturnToGuardPosition(RockTrollEntity troll, double speed){
            this.troll=troll;
            this.speed=speed;
        }

        @Override
        public boolean canStart() {
            return troll.isWaiting() && troll.getTarget()==null
                    && !troll.isBlocking()
                    && troll.getSteppingPos() != troll.getGuardingPos()
                    && searchItemsList().isEmpty()
                    && !(troll.hasFoodOrAlcohol() || troll.hasBarteringItem());
        }

        @Override
        public void start() {
            this.startMovingTo(troll.getNavigation(), troll.getGuardingPos().getX(), troll.getGuardingPos().getY(), troll.getGuardingPos().getZ(), speed);
        }

        @Override
        public void tick() {
            this.startMovingTo(troll.getNavigation(), troll.getGuardingPos().getX(), troll.getGuardingPos().getY(), troll.getGuardingPos().getZ(), speed);
        }

        private List<ItemEntity> searchItemsList(){
            return
                    this.troll.getWorld().getEntitiesByClass(ItemEntity.class,
                            this.troll.getBoundingBox().expand(8.0, 2.0, 8.0),
                            item ->
                                    !item.cannotPickup() && item.isAlive()
                                            && ((item.getStack().getItem() == RockTrollEntity.BARTERING_ITEM && troll.isWandering())
                                            || troll.isEdible(item.getStack().getItem()) || troll.isAlcohol(item.getStack().getItem())));
        }

        public void startMovingTo(EntityNavigation navigation, int x, int y, int z, double speed) {
            navigation.startMovingAlong(navigation.findPathTo(x, y, z, 0), speed);
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

        if(target instanceof PlayerEntity player && this.getOwner() == target){
            if(this.getFriendship(player)>=75){
                return false;
            } else {
                this.setOwner(null);
                this.setFollowerState(0);
                player.sendMessage(Text.translatable("tcots-witcher.gui.troll_wandering", this.getName()), true);
                this.setGuardingPos(BlockPos.ORIGIN);
                return true;
            }
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
        this.gossip.decay();
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
        rockProjectileEntity.setVelocity(e, f + h, g, 1.8f, this.isWandering()? 4.0f : 1.5f);
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

        //If isn't already blocking, is alive and has an attacker
        if(!this.getWorld().isClient && !isBlocking() && this.isAlive() && this.getAttacker()!=null && !this.hasBarteringItem() && !this.hasFoodOrAlcohol()) {
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
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 4f);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if(spawnReason == SpawnReason.NATURAL){
            //1/5 probability to be a rabid troll if it's a natural spawn
            if(random.nextInt()%5==0){
                this.setIsRabid(true);
            }
        }
        else if(spawnReason != SpawnReason.STRUCTURE){
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
        this.dataTracker.startTracking(EATING_TIME, -1);
        this.dataTracker.startTracking(GUARDING_POS, BlockPos.ORIGIN);
        this.dataTracker.startTracking(FOLLOWER_STATE, 0);
    }

    //Reputation System
    private final TrollGossips gossip = new TrollGossips();

    public TrollGossips getGossip() {
        return gossip;
    }

    //Bad Actions
    public static final EntityInteraction TROLL_KILLED = EntityInteraction.create("troll_killed");
    public static final EntityInteraction TROLL_HURT = EntityInteraction.create("troll_hurt");
    public static final EntityInteraction OTHER_TROLL_HURT = EntityInteraction.create("troll_hurt");

    //Good Actions
    public static final EntityInteraction TROLL_DEFENDING = EntityInteraction.create("troll_defending");
    public static final EntityInteraction TROLL_DEFENDING_FRIEND = EntityInteraction.create("troll_defending_other");
    public static final EntityInteraction TROLL_ALCOHOL = EntityInteraction.create("troll_alcohol");
    public static final EntityInteraction TROLL_ALCOHOL_FRIEND = EntityInteraction.create("troll_alcohol_friend");
    public static final EntityInteraction TROLL_FED = EntityInteraction.create("troll_fed");
    public static final EntityInteraction TROLL_FED_FRIEND = EntityInteraction.create("troll_fed_friend");
    public static final EntityInteraction TROLL_BARTER = EntityInteraction.create("troll_trade");
    public static final EntityInteraction TROLL_BARTER_FRIEND = EntityInteraction.create("troll_fed_friend");

    //Follow at 100 friendship
    public int getReputation(PlayerEntity player) {
        return this.gossip.getReputationFor(player.getUuid(), gossipType -> true);
    }
    public int getFriendship(PlayerEntity player) {
        return this.gossip.getFriendshipFor(player.getUuid(), gossipType -> true);
    }
    @Override
    public void onInteractionWith(EntityInteraction interaction, Entity entity) {
        //So a Rabid troll isn't bother with reputation and friendship
        if(this.isRabid()){
            return;
        }

        if(entity==null){
            return;
        }
        //Good actions
        if(interaction == TROLL_DEFENDING){
            //+30 Reputation
            //+50/20 Friendship (20 when they already your follower)
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.DEFENDING, 30, this.getOwner() == entity? 20: 50);
        } else if(interaction == TROLL_DEFENDING_FRIEND){
            //+30 Reputation
            //+10 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.DEFENDING, 30, 10);
        } else if(interaction == TROLL_ALCOHOL){
            //+10 Reputation
            //+20 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 20);
        } else if (interaction == TROLL_ALCOHOL_FRIEND){
            //+10 Reputation
            //+1 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 1);
        } else if(interaction == TROLL_FED){
            //+10 Reputation
            //+15 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 15);
        } else if (interaction == TROLL_FED_FRIEND) {
            //+10 Reputation
            //+1 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 1);
        } else if(interaction == TROLL_BARTER){
            //+5 Reputation
            //+2 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.BARTERING, 5, 2);
        } else if(interaction == TROLL_BARTER_FRIEND){
            //+5 Reputation
            //+1 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.BARTERING, 5, 1);
        }
        //Bad actions
        else if (interaction == TROLL_KILLED) {
            //-80 Reputation
            //-50 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.KILL_TROLL, 80, 50);
        } else if (interaction == TROLL_HURT) {
            //-25 Reputation
            //-10 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.HURT, 25, 10);
        } else if (interaction == OTHER_TROLL_HURT){
            //-10 Reputation
            //-5 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.HURT, 10, 5);
        }
    }

    @Override
    public void setAttacker(@Nullable LivingEntity attacker) {
        if (!this.isRabid() && attacker != null && this.getWorld() instanceof ServerWorld && attacker instanceof PlayerEntity player) {
            ((ServerWorld)this.getWorld()).handleInteraction(TROLL_HURT, attacker, this);
            this.handleNearTrollsInteraction(OTHER_TROLL_HURT, player);
            if (this.isAlive() && attacker instanceof PlayerEntity) {
                this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
                this.handleNearTrollsParticles(EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
            }
        }
        super.setAttacker(attacker);
    }

    public List<RockTrollEntity> getNearTrolls(){
        return this.getWorld().getEntitiesByClass(RockTrollEntity.class, this.getBoundingBox().expand(10,2,10),
                troll -> troll.canSee(this) && troll != this && !troll.isRabid());
    }

    public void handleNearTrollsInteraction(EntityInteraction interaction, PlayerEntity player) {
        for (RockTrollEntity nearTroll : this.getNearTrolls()) {
            ((ServerWorld) this.getWorld()).handleInteraction(interaction, player, nearTroll);
        }
    }

    public void handleNearTrollsParticles(byte byteSent) {
        for (RockTrollEntity nearTroll : this.getNearTrolls()) {
            nearTroll.getWorld().sendEntityStatus(nearTroll, byteSent);
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        Entity attacker = damageSource.getAttacker();

        if (!this.getWorld().isClient && this.getWorld().getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
            this.getOwner().sendMessage(this.getDamageTracker().getDeathMessage());
        }

        if(!this.isRabid() && this.getWorld() instanceof ServerWorld && attacker instanceof PlayerEntity player) {
            this.handleNearTrollsInteraction(TROLL_KILLED, player);
            this.handleNearTrollsParticles(EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
        }

        super.onDeath(damageSource);
    }
    public static final Item BARTERING_ITEM = Items.AMETHYST_SHARD;
    //Generates loot for the Bartering
    //xTODO: Add a custom Loot Table
    public static final Identifier ROCK_TROLL_BARTERING_GAMEPLAY = new Identifier(TCOTS_Main.MOD_ID,"gameplay/rock_troll_bartering");
    //xTODO: Make them also retrieve Amethyst Shard from the ground
    private List<ItemStack> getBarteredItems(RockTrollEntity troll) {
        if(troll.getWorld().getServer() == null){
            return Collections.emptyList();
        }

        LootTable lootTable = troll.getWorld().getServer().getLootManager().getLootTable(ROCK_TROLL_BARTERING_GAMEPLAY);
        return lootTable.generateLoot(new LootContextParameterSet.Builder((ServerWorld)troll.getWorld()).add(LootContextParameters.THIS_ENTITY, troll).build(LootContextTypes.BARTER));
    }

    //Drops the loot and gives reputation
    private void dropLoot(RockTrollEntity troll, @Nullable PlayerEntity player) {
        if(player!=null) {
            //Gives reputation
            ((ServerWorld) this.getWorld()).handleInteraction(TROLL_BARTER, player, this);
            this.handleNearTrollsInteraction(TROLL_BARTER_FRIEND, player);
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            //Only gives items to player if player it's above -30 Reputation
            if(this.getReputation(player)>=-30) {
                //Swings hand
                this.triggerAnim("GiveController", "give_item");
                for (ItemStack itemStack : this.getBarteredItems(this)) {
                    Vec3d vec3d = new Vec3d(0.3f, 0.3f, 0.3f);
                    //Looks at player
                    this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, player.getPos().add(0.0, 1.0, 0.0));
                    //Gives bartered item
                    this.give(troll, itemStack, player.getPos().add(0.0, 1.0, 0.0), vec3d, 0.3f);
                }
                //Gives Mutagen
                if(this.getReputation(player) >= 70 && this.getRandom().nextBetween(0,8)==1){
                    Vec3d vec3d = new Vec3d(0.3f, 0.3f, 0.3f);
                    this.give(troll, new ItemStack(TCOTS_Items.TROLL_MUTAGEN), player.getPos().add(0.0, 1.0, 0.0), vec3d, 0.3f);
                }
            }
            //Resets hand/Consumes Amethyst
            this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
            //Removes the player from the NBT
            this.setLastPlayer(null);

            this.playSound(TCOTS_Sounds.TROLL_BARTERING, 1.0f, 1.0f);
        } else {
            //Resets hand/Consumes Amethyst
            this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canGather(ItemStack stack) {
        return canGather();
    }

    public boolean canGather(){
        return this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && this.canPickUpLoot() && this.trollCanGather();
    }

    private boolean trollCanGather(){
        return !this.isAttacking() && !this.isBlocking() && !this.hasBarteringItem() && this.getAngerTime() == 0 && !this.hasFoodOrAlcohol() && this.getTarget()==null;
    }


    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1, 1, 1);
    @Override
    protected Vec3i getItemPickUpRangeExpander() {
        return ITEM_PICKUP_RANGE_EXPANDER;
    }

    @Override
    protected void loot(ItemEntity item) {
        this.triggerItemPickedUpByEntityCriteria(item);
        this.handleItemFromGround(item);
    }


    private void handleItemFromGround(ItemEntity item) {
        boolean isBartering = item.getStack().getItem() == BARTERING_ITEM;

        if ((isBartering && this.isWandering()) || isEdible(item.getStack().getItem()) || isDrinkable(item.getStack().getItem())) {
            this.sendPickup(item, 1);
            ItemStack itemStack = EntitiesUtil.getItemFromStack(item);
            this.setStackInHand(Hand.OFF_HAND, itemStack);
            if (item.getOwner() != null && item.getOwner() instanceof PlayerEntity player) {
                this.setLastPlayer(player);
            }
            if (isBartering) {
                this.setAdmiringTime(0);
            } else {
                this.setEatingTime(0);
            }
            this.setPersistent();
        }
    }

    public void give(LivingEntity entity, ItemStack stack, Vec3d targetLocation, Vec3d velocityFactor, float yOffset) {
        double d = entity.getEyeY() - (double)yOffset;
        ItemEntity itemEntity = new ItemEntity(entity.getWorld(), entity.getX(), d, entity.getZ(), stack);
        itemEntity.setThrower(entity);
        Vec3d vec3d = targetLocation.subtract(entity.getPos());
        vec3d = vec3d.normalize().multiply(velocityFactor.x, velocityFactor.y, velocityFactor.z);
        itemEntity.setVelocity(vec3d);
        itemEntity.setToDefaultPickupDelay();
        entity.getWorld().spawnEntity(itemEntity);
    }

    public boolean hasBarteringItem(){
        return this.getStackInHand(Hand.OFF_HAND).getItem() == BARTERING_ITEM;
    }

    public boolean hasFoodOrAlcohol(){
        return this.isEdible(this.getStackInHand(Hand.OFF_HAND).getItem()) || this.isDrinkable(this.getStackInHand(Hand.OFF_HAND).getItem());
    }

    private boolean isEdible(Item item){
        return (item.isFood() && Objects.requireNonNull(item.getFoodComponent()).isMeat()) || item == Items.RABBIT_STEW;
    }

    private boolean isDrinkable(Item item){
        return (
                item instanceof PotionItem
                && !(item instanceof SplashPotionItem)
                && !(item instanceof LingeringPotionItem)
                && (!(item instanceof WitcherPotions_Base))
                //To not being able to give Potion when they aren't following you or guarding
                && !this.isWandering()
        )
                || item == Items.HONEY_BOTTLE
                || item == Items.MILK_BUCKET
                || isAlcohol(item);
    }

    private boolean isAlcohol(Item item){
        return item instanceof WitcherAlcohol_Base;
    }

    @Override
    protected ActionResult interactMob(@NotNull PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        //Ensure this is only processed on the server
        //You can only interact with them when they aren't attacking and aren't bartering and isn't rabid and isn't blocking
        if (!this.getWorld().isClient && !this.isAttacking() && !this.hasBarteringItem() && !this.isRabid() && !this.isBlocking() && !this.hasFoodOrAlcohol() && this.getAngerTime() == 0) {

            // Change from Following/Waiting to Wandering
            if ((this.isFollowing() || this.isWaiting()) && this.getOwner() == player && player.isSneaking()) {
                this.setOwner(null);
                this.setFollowerState(0);
                player.sendMessage(Text.translatable("tcots-witcher.gui.troll_wandering", this.getName()), true);
                this.setGuardingPos(BlockPos.ORIGIN);
                return ActionResult.SUCCESS;
            }

            // Barter with Troll, only when reputation is above -75
            if (itemStack.isOf(BARTERING_ITEM) && this.getReputation(player) >= -75 && this.isWandering()) {
                this.setStackInHand(Hand.OFF_HAND, new ItemStack(BARTERING_ITEM));
                this.setLastPlayer(player);
                this.setAdmiringTime(0);

                itemStack.decrement(1);
                this.setPersistent();
                return ActionResult.SUCCESS;
            }

            // Feed Troll
            else if (isEdible(item)) {
                this.setStackInHand(Hand.OFF_HAND, itemStack.copyWithCount(1));
                this.setLastPlayer(player);
                this.setEatingTime(0);

                itemStack.decrement(1);
                this.setPersistent();
                return ActionResult.SUCCESS;
            }

            // Give Alcohol to Troll
            else if (isDrinkable(item)) {
                this.setStackInHand(Hand.OFF_HAND, itemStack.copyWithCount(1));
                this.setLastPlayer(player);
                this.setEatingTime(0);

                itemStack.decrement(1);
                this.setPersistent();
                return ActionResult.SUCCESS;
            }
            if (hand != Hand.MAIN_HAND) {
                return ActionResult.PASS;
            }

            // Handle changes in states (from Wandering to Following)
            if (((this.isWandering() && this.getFriendship(player) >= 150 && (this.getOwner() == null && this.getOwnerUuid() == null)) ||
                    //Following <-> Waiting
                    (!this.isWandering() && this.getOwner() == player)) && !player.isSneaking()) {

                if (this.getOwner() == null) {
                    this.setOwner(player);
                }

                this.setFollowerState(this.isWandering() ? 1 : (this.isFollowing() ? 2 : 1));

                player.sendMessage(this.isFollowing() ?
                        Text.translatable("tcots-witcher.gui.troll_follows", this.getName()) :
                        Text.translatable("tcots-witcher.gui.troll_waits", this.getName()), true);

                if (this.isWaiting()) {
                    this.setGuardingPos(this.getSteppingPos());
                }

                return ActionResult.SUCCESS;
            }


        }

        return ActionResult.PASS;
    }

    private int admiringTime=-1;
    public void setAdmiringTime(int admiringTime) {
        this.admiringTime = admiringTime;
    }
    public int getAdmiringTime() {
        return admiringTime;
    }
    public void addAdmiringTime(){
        this.setAdmiringTime(getAdmiringTime()+1);
    }



    public void setEatingTime(int eatingTime) {
        this.dataTracker.set(EATING_TIME, eatingTime);
    }
    public int getEatingTime() {
        return this.dataTracker.get(EATING_TIME);
    }
    public void addEatingTime(){
        this.setEatingTime(getEatingTime()+1);
    }


    @SuppressWarnings("all")
    private final int TOTAL_ADMIRING_TIME=60;
    @SuppressWarnings("all")
    private final int TOTAL_EATING_TIME=40;


    @Override
    public void tick() {
        super.tick();

        this.decayGossip();

        this.tickAdmiringItem();

        this.tickEatingItem();

        if(!this.getWorld().isClient && !this.isAttacking() && this.age%600==0) {
            for (RockTrollEntity troll: getNearTrolls()){
                this.gossip.shareGossipsWith(this, troll);
            }
        }
    }

    private void tickAdmiringItem(){
        //If player attacks mid-animation
        if(this.getAdmiringTime()!=-1 && this.getAdmiringTime() < TOTAL_ADMIRING_TIME && this.getAngerTime() > 0){
            this.setLastPlayer(null);
            this.dropLoot(this, this.getLastPlayer());
            this.setAdmiringTime(-1);
        }

        if(this.getAdmiringTime() < TOTAL_ADMIRING_TIME && this.getAdmiringTime()!=-1){
            this.addAdmiringTime();
        } else if (this.getAdmiringTime() == TOTAL_ADMIRING_TIME){
            this.dropLoot(this, this.getLastPlayer());
            this.setAdmiringTime(-1);
        }
    }

    private void tickEatingItem(){
        //If player attacks mid-animation
        if(this.getEatingTime()!=-1 && this.getEatingTime() < TOTAL_EATING_TIME && this.getAngerTime() > 0){
            this.setLastPlayer(null);
            this.handleEndsFeed(this.getLastPlayer(), this.getStackInHand(Hand.OFF_HAND));
            this.setEatingTime(-1);
        }

        if(this.getEatingTime() < TOTAL_EATING_TIME && this.getEatingTime()!=-1){
            ItemStack foodStack = this.getStackInHand(Hand.OFF_HAND);
            this.addEatingTime();
            if ((foodStack.getUseAction() == UseAction.DRINK) && this.getEatingTime()%5==0) {
                this.playSound(this.getDrinkSound(foodStack), 0.5f, this.getWorld().random.nextFloat() * 0.1f + 0.9f);
            }
            if (foodStack.getUseAction() == UseAction.EAT && this.getEatingTime()%5==0) {
                this.spawnItemParticles(foodStack);
                this.playSound(this.getEatSound(foodStack), 0.5f + 0.5f * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }

        } else if (this.getEatingTime() == TOTAL_EATING_TIME){
            this.handleEndsFeed(this.getLastPlayer(), this.getStackInHand(Hand.OFF_HAND));
        }
    }

    private void spawnItemParticles(ItemStack stack) {
        for (int i = 0; i < 5; ++i) {

            Vec3d vec3dVelocity = new Vec3d(
                    ((double)this.random.nextFloat() - 0.5) * 0.1,
                    Math.random() * 0.1 + 0.1,
                    0.0)
                    .rotateX(-this.getPitch() * ((float)Math.PI / 180))
                    .rotateY(-this.getYaw() * ((float)Math.PI / 180));

            Vec3d vec3dPos = new Vec3d((
                    (double)this.random.nextFloat() - 0.5) * 0.1,
                    (double)(-this.random.nextFloat()) * 0.01,
                    1.2 + ((double)this.random.nextFloat() - 0.5) * 0.1)
                    .rotateY(-this.bodyYaw * ((float)Math.PI / 180))
                    .add(this.getX(), this.getEyeY() - 0.35, this.getZ());

            this.getWorld().addParticle(
                    new ItemStackParticleEffect(ParticleTypes.ITEM, stack),
                    //Position
                    vec3dPos.x,
                    vec3dPos.y,
                    vec3dPos.z,
                    //Velocity
                    vec3dVelocity.x,
                    vec3dVelocity.y + 0.05,
                    vec3dVelocity.z);

        }
    }

    private void handleEndsFeed(PlayerEntity player, ItemStack foodStack){
        if(!this.getWorld().isClient) {
            if (foodStack.getUseAction() == UseAction.DRINK) {

                if(player!=null) {
                    ((ServerWorld) this.getWorld()).handleInteraction(TROLL_ALCOHOL, player, this);
                    this.handleNearTrollsInteraction(TROLL_ALCOHOL_FRIEND, player);
                    this.setPersistent();
                }

                ItemStack dropStack =
                        (foodStack.getItem() == Items.HONEY_BOTTLE || foodStack.getItem() instanceof PotionItem || foodStack.getItem() instanceof HerbalMixture) ?
                                new ItemStack(Items.GLASS_BOTTLE):
                                foodStack.getItem() == Items.MILK_BUCKET ?
                                        new ItemStack(Items.BUCKET):
                                        ItemStack.EMPTY;

                foodStack.finishUsing(this.getWorld(), this);

                this.dropStack(dropStack);
            }

            if (foodStack.getUseAction() == UseAction.EAT) {
                if(player!=null) {
                    ((ServerWorld) this.getWorld()).handleInteraction(TROLL_FED, player, this);
                    this.handleNearTrollsInteraction(TROLL_FED_FRIEND, player);
                    this.setPersistent();
                }

                if(foodStack.getFoodComponent()!=null){
                    this.heal(foodStack.getFoodComponent().getHunger());
                }

                this.dropStack(foodStack.getItem() == Items.RABBIT_STEW? new ItemStack(Items.BOWL): ItemStack.EMPTY);
            }


            this.getWorld().sendEntityStatus(this,
                    player!=null && this.getFriendship(player)>150? EntityStatuses.ADD_VILLAGER_HEART_PARTICLES:
                            EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);

            this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);

            this.setEatingTime(-1);
        }
    }

    @Nullable
    private PlayerEntity lastPlayer;
    @Nullable
    private UUID lastPlayerUuid;
    @Nullable
    public PlayerEntity getLastPlayer() {
        PlayerEntity entity;
        if (this.lastPlayer == null && this.lastPlayerUuid != null && this.getWorld() instanceof ServerWorld && (entity = this.getWorld().getPlayerByUuid(this.lastPlayerUuid)) != null) {
            this.lastPlayer = entity;
        }
        return this.lastPlayer;
    }
    public void setLastPlayer(@Nullable PlayerEntity lastPlayer) {
        this.lastPlayer = lastPlayer;
        this.lastPlayerUuid = lastPlayer == null ? null : lastPlayer.getUuid();
    }

    //Follower system
    @Nullable
    private UUID ownerUuid;
    @Nullable
    @Override
    public Entity getOwner() {
        UUID uUID = this.getOwnerUuid();
        if (uUID == null) {
            return null;
        }

        return this.getWorld().getPlayerByUuid(uUID);
    }
    public void setOwner(@Nullable PlayerEntity owner) {
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.ownerUuid;
    }

    public void setFollowerState(int followerState) {

        this.dataTracker.set(FOLLOWER_STATE, followerState);

        if(this.isFollowing() || this.isWaiting()){
            this.playSound(this.isWaiting()? TCOTS_Sounds.TROLL_WAITING: TCOTS_Sounds.TROLL_FOLLOW,1.0f,1.0f);

            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR)).setBaseValue(16.0);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).setBaseValue(8.0);
        } else {

            this.playSound(TCOTS_Sounds.TROLL_DISMISS, 1.0f,1.0f);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR)).setBaseValue(8.0);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).setBaseValue(4.0);
        }
    }

    public int getFollowerState() {
        return this.dataTracker.get(FOLLOWER_STATE);
    }

    public boolean isWaiting(){
        return this.getFollowerState()==2;
    }

    public boolean isFollowing(){
        return this.getFollowerState()==1;
    }

    public boolean isWandering(){
        return this.getFollowerState()==0;
    }


    public void setGuardingPos(BlockPos pos) {
        this.dataTracker.set(GUARDING_POS, pos);
    }

    public BlockPos getGuardingPos() {
        return this.dataTracker.get(GUARDING_POS);
    }


    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
        nbt.putBoolean("Blocking", this.isBlocking());
        nbt.putInt("TimeBlocking", this.getTimeBlocking());
        nbt.putBoolean("IsRabid", this.isRabid());

        nbt.put("Gossips", this.gossip.serialize(NbtOps.INSTANCE));
        nbt.putLong("LastGossipDecay", this.lastGossipDecayTime);
        nbt.putInt("AdmiringTime", this.admiringTime);
        nbt.putInt("EatingTime", this.getEatingTime());

        if (this.lastPlayerUuid != null) {
            nbt.putUuid("LastPlayer", this.lastPlayerUuid);
        }

        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }

        nbt.putInt("FollowerState", this.getFollowerState());

        nbt.putInt("GuardPosX", this.getGuardingPos().getX());
        nbt.putInt("GuardPosY", this.getGuardingPos().getY());
        nbt.putInt("GuardPosZ", this.getGuardingPos().getZ());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.getWorld(), nbt);
        setIsBlocking(nbt.getBoolean("Blocking"));
        setTimeBlocking(nbt.getInt("TimeBlocking"));
        setIsRabid(nbt.getBoolean("IsRabid"));

        NbtList nbtList = nbt.getList("Gossips", NbtElement.COMPOUND_TYPE);
        this.gossip.deserialize(new Dynamic<>(NbtOps.INSTANCE, nbtList));
        this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");
        this.admiringTime = nbt.getInt("AdmiringTime");
        setEatingTime(nbt.getInt("EatingTime"));

        if (nbt.containsUuid("LastPlayer")) {
            this.lastPlayerUuid = nbt.getUuid("LastPlayer");
        }

        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }

        this.setFollowerState(nbt.getInt("FollowerState"));

        int x = nbt.getInt("GuardPosX");
        int y = nbt.getInt("GuardPosY");
        int z = nbt.getInt("GuardPosZ");
        this.setGuardingPos(new BlockPos(x, y, z));
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
        }
        else {
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

        //Give Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "GiveController", 1, state -> PlayState.STOP)
                        .triggerableAnim("give_item", GIVE_ITEM)
        );
    }


    //Animation things
    public float eatingProgress;
    public float maxEatingDeviation;
    public float prevMaxEatingDeviation;
    public float prevEatingProgress;
    public float eatingSpeed = 1.0f;
    @Override
    public void tickMovement() {
        super.tickMovement();

        this.prevEatingProgress = this.eatingProgress;
        this.prevMaxEatingDeviation = this.maxEatingDeviation;

        if(this.hasFoodOrAlcohol()){
            this.maxEatingDeviation = this.maxEatingDeviation + (-1f*0.3f);
        }

        this.maxEatingDeviation = MathHelper.clamp(this.maxEatingDeviation, -0.05f, 0.05f);
        if (this.hasFoodOrAlcohol() && this.eatingSpeed < 1.0f) {
            this.eatingSpeed = 1.0f;
        }


        this.eatingSpeed = this.eatingSpeed * 0.9f;


        this.eatingProgress = this.eatingProgress + (this.eatingSpeed * 1.5f);

    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return (this.isBlocking()
                && (damageSource.isOf(DamageTypes.PLAYER_ATTACK) || damageSource.isOf(DamageTypes.MOB_ATTACK) || damageSource.isOf(DamageTypes.MOB_ATTACK_NO_AGGRO))
                && !(damageSource.isIn(DamageTypeTags.BYPASSES_ARMOR)))

                || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isBlocking() && !this.isWaiting();
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

    public int getMinAmbientSoundDelay() {
        return 120;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAttacking()? TCOTS_Sounds.TROLL_FURIOUS: this.isRabid()?  TCOTS_Sounds.TROLL_IDLE: TCOTS_Sounds.TROLL_GRUNT;
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
    protected boolean isDisallowedInPeaceful() {
        return !this.isPersistent();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

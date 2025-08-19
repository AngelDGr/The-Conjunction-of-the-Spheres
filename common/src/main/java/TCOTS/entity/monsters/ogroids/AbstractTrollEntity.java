package TCOTS.entity.monsters.ogroids;

import TCOTS.registry.TCOTS_Criteria;
import TCOTS.registry.TCOTS_Sounds;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.items.HerbalMixture;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.WitcherPotions_Base;
import TCOTS.utils.EntitiesUtil;
import com.mojang.serialization.Dynamic;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.LingeringPotionItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractTrollEntity extends OgroidMonster implements GeoEntity, NeutralMob, TraceableEntity, ReputationEventHandler {

    private static final EntityDataAccessor<Boolean> RABID = SynchedEntityData.defineId(AbstractTrollEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EATING_TIME = SynchedEntityData.defineId(AbstractTrollEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockPos> GUARDING_POS = SynchedEntityData.defineId(AbstractTrollEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> FOLLOWER_STATE = SynchedEntityData.defineId(AbstractTrollEntity.class, EntityDataSerializers.INT);


    protected static final EntityDataAccessor<Boolean> BLOCKING = SynchedEntityData.defineId(AbstractTrollEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> TIME_BLOCKING = SynchedEntityData.defineId(AbstractTrollEntity.class, EntityDataSerializers.INT);

    public static final RawAnimation GIVE_ITEM = RawAnimation.begin().thenPlay("special.give");

    public static final RawAnimation BLOCK = RawAnimation.begin().thenPlayAndHold("special.block");
    public static final RawAnimation UNBLOCK = RawAnimation.begin().thenPlay("special.unblock");

    public AbstractTrollEntity(final EntityType<? extends AbstractTrollEntity> entityType, final Level world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
        this.xpReward=8;
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);

        builder.define(RABID, Boolean.FALSE);
        builder.define(EATING_TIME, -1);
        builder.define(GUARDING_POS, BlockPos.ZERO);
        builder.define(FOLLOWER_STATE, 0);

        builder.define(BLOCKING, Boolean.FALSE);
        builder.define(TIME_BLOCKING, 0);
    }

    protected static class MeleeAttackGoal_Troll extends MeleeAttackGoal_Animated {

        private final AbstractTrollEntity troll;

        public MeleeAttackGoal_Troll(final AbstractTrollEntity mob, final double speed, final boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.troll = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !troll.isTrollBlocking();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !troll.isTrollBlocking();
        }
    }
    protected static class ProjectileAttackGoal_RockTroll extends RangedAttackGoal {
        private final RockTrollEntity troll;
        private final float distanceForAttack;

        public ProjectileAttackGoal_RockTroll(final RockTrollEntity mob, final double mobSpeed, final int intervalTicks, final float maxShootRange, final float distanceForAttack) {
            super(mob, mobSpeed, intervalTicks, maxShootRange);
            this.troll = mob;
            this.distanceForAttack = distanceForAttack;
        }

        private boolean distanceCondition() {
            if (troll.getTarget() != null) {
                final LivingEntity target = this.troll.getTarget();
                final double d = this.troll.distanceToSqr(target);

                return !(d < distanceForAttack);
            } else {
                return true;
            }
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && distanceCondition()
                    && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && distanceCondition();
        }
    }

    protected static class WanderAroundGoal_Troll extends RandomStrollGoal {
        private final AbstractTrollEntity troll;

        public WanderAroundGoal_Troll(final AbstractTrollEntity mob, final double speed, final int chance) {
            super(mob, speed, chance);
            this.troll = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering() && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering() && !this.troll.isTrollBlocking();
        }
    }

    protected static class LookAroundGoal_Troll extends RandomLookAroundGoal {
        private final AbstractTrollEntity troll;

        public LookAroundGoal_Troll(final AbstractTrollEntity mob) {
            super(mob);
            this.troll = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }
    }

    protected static class LookAtEntityGoal_Troll extends LookAtPlayerGoal {

        private final AbstractTrollEntity troll;

        public LookAtEntityGoal_Troll(final AbstractTrollEntity mob, final Class<? extends LivingEntity> targetType, final float range) {
            super(mob, targetType, range);
            this.troll = mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }
    }

    protected static class LookAtPlayerWithWeaponGoal extends LookAtPlayerGoal {
        private final AbstractTrollEntity troll;

        public LookAtPlayerWithWeaponGoal(final AbstractTrollEntity troll, final Class<? extends Player> targetType, final float range) {
            this(troll, targetType, range, 1f);
        }

        public LookAtPlayerWithWeaponGoal(final AbstractTrollEntity troll, final Class<? extends Player> targetType, final float range, final float chance) {
            this(troll, targetType, range, chance, false);
        }

        public LookAtPlayerWithWeaponGoal(final AbstractTrollEntity troll, final Class<? extends Player> targetType, final float range, final float chance, final boolean lookForward) {
            super(troll, targetType, range, chance, lookForward);
            this.troll = troll;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));

        }

        @Override
        public boolean canUse() {

            if (this.troll.getTarget() != null) {
                this.lookAt = this.troll.getTarget();
            }
            this.lookAt =
                    this.troll.level().getNearestPlayer(TargetingConditions.forNonCombat().range(lookDistance)
                                    //If it has a reputation less than 25, and it's holding a Sword or Axe
                                    .selector(player -> this.troll.getReputation((Player) player) < 25
                                            && (player.getMainHandItem().getItem() instanceof SwordItem || player.getMainHandItem().getItem() instanceof AxeItem)),

                            this.troll, this.troll.getX(), this.troll.getEyeY(), this.troll.getZ());

            return this.lookAt != null && !this.troll.hasBarteringItem() && !this.troll.isBlocking() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering();
        }
    }

    protected static class TrollUniversalAngerGoal<T extends AbstractTrollEntity> extends Goal {

        public TrollUniversalAngerGoal(final T troll, final boolean triggerOthers) {
            this.troll = troll;
            this.triggerOthers = triggerOthers;
        }

        private static final int BOX_VERTICAL_EXPANSION = 10;
        private final T troll;
        private final boolean triggerOthers;
        private int lastAttackedTime;

        @Override
        public boolean canUse() {
            return this.troll.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.canStartUniversalAnger();
        }

        private boolean canStartUniversalAnger() {
            return this.troll.getLastHurtByMob() != null && this.troll.getLastHurtByMob().getType() == EntityType.PLAYER && this.troll.getLastHurtByMobTimestamp() > this.lastAttackedTime;
        }

        @Override
        public void start() {
            this.lastAttackedTime = this.troll.getLastHurtByMobTimestamp();
            this.troll.forgetCurrentTargetAndRefreshUniversalAnger();

            if (this.triggerOthers) {
                this.getOthersInRange().stream().filter(entity -> entity != this.troll).map(entity -> (NeutralMob) entity).forEach(NeutralMob::forgetCurrentTargetAndRefreshUniversalAnger);
            }
        }

        private List<? extends AbstractTrollEntity> getOthersInRange() {
            final double d = this.troll.getAttributeValue(Attributes.FOLLOW_RANGE);
            final AABB box = AABB.unitCubeFromLowerCorner(this.troll.position()).inflate(d, BOX_VERTICAL_EXPANSION, d);
            //This way only other Rabid can trigger Rabid
            //This way the Rabid can't trigger others neutral Trolls
            if (troll.isRabid()) {
                return this.troll.level().getEntitiesOfClass(AbstractTrollEntity.class, box, EntitySelector.NO_SPECTATORS
                        .and(entity -> ((AbstractTrollEntity) (entity)).isRabid()).and(entity -> entity.getType()==this.troll.getType()));
            } else {
                return this.troll.level().getEntitiesOfClass(AbstractTrollEntity.class, box, EntitySelector.NO_SPECTATORS
                        .and(entity -> entity.getType()==this.troll.getType()));
            }
        }
    }

    protected static class TrollTargetWithReputationGoal extends TargetGoal {
        private final AbstractTrollEntity troll;
        @Nullable
        private LivingEntity target;
        private final TargetingConditions targetPredicate = TargetingConditions.forCombat().range(64.0);

        public TrollTargetWithReputationGoal(final AbstractTrollEntity troll) {
            super(troll, false, true);
            this.troll = troll;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            final AABB box = this.troll.getBoundingBox().inflate(10.0, 8.0, 10.0);
            this.targetMob = null;

            final List<AbstractTrollEntity> listTrolls = this.troll.level().getNearbyEntities(AbstractTrollEntity.class, this.targetPredicate, this.troll, box);
            final List<Player> listPlayers = this.troll.level().getNearbyPlayers(this.targetPredicate, this.troll, box);
            //To add itself to the list
            listTrolls.add(this.troll);


            //Attack defending other trolls
            for (final AbstractTrollEntity trollAround : listTrolls) {
                //To ignore reputation with rabid trolls
                if (trollAround.isRabid())
                    continue;

                for (final Player playerEntity : listPlayers) {
                    final int reputation = trollAround.getReputation(playerEntity);
                    //If the reputation is below or equals -75, attack when the player it's holding a weapon (Axe or Sword)
                    if (reputation <= -75 && (playerEntity.getMainHandItem().getItem() instanceof SwordItem || playerEntity.getMainHandItem().getItem() instanceof AxeItem)) {
                        this.targetMob = playerEntity;
                    }

                    //If the reputation is below -100, attack on sight
                    if (reputation <= -100) {
                        this.targetMob = playerEntity;
                    }
                }
            }

            if (this.targetMob == null) {
                return false;
            }

            return !(this.targetMob instanceof Player) || !this.targetMob.isSpectator() && !((Player) this.targetMob).isCreative();
        }

        @Override
        public void start() {
            this.troll.setTarget(this.targetMob);
            super.start();
        }
    }

    protected static class TrollRevengeGoal extends TargetGoal {
        private static final TargetingConditions VALID_AVOIDABLE_PREDICATE = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
        private static final int BOX_VERTICAL_EXPANSION = 10;
        private boolean groupRevenge;
        private int lastAttackedTime;
        private final Class<?>[] noRevengeTypes;
        @Nullable
        private Class<?>[] noHelpTypes;

        public TrollRevengeGoal(final PathfinderMob mob, final Class<?>... noRevengeTypes) {
            super(mob, true);
            this.noRevengeTypes = noRevengeTypes;
            this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            final int i = this.mob.getLastHurtByMobTimestamp();
            final LivingEntity livingEntity = this.mob.getLastHurtByMob();
            if (i == this.lastAttackedTime || livingEntity == null) {
                return false;
            }
            if (livingEntity.getType() == EntityType.PLAYER && this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            }
            for (final Class<?> class_ : this.noRevengeTypes) {
                if (!class_.isAssignableFrom(livingEntity.getClass())) continue;
                return false;
            }
            return this.canAttack(livingEntity, VALID_AVOIDABLE_PREDICATE);
        }

        public TrollRevengeGoal setGroupRevenge(final Class<?>... noHelpTypes) {
            this.groupRevenge = true;
            this.noHelpTypes = noHelpTypes;
            return this;
        }

        @Override
        public void start() {
            this.mob.setTarget(this.mob.getLastHurtByMob());
            this.targetMob = this.mob.getTarget();
            this.lastAttackedTime = this.mob.getLastHurtByMobTimestamp();
            this.unseenMemoryTicks = 300;
            if (this.groupRevenge) {
                this.callSameTypeForRevenge();
            }
            super.start();
        }

        protected void callSameTypeForRevenge() {
            final double d = this.getFollowDistance();
            final AABB box = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d, BOX_VERTICAL_EXPANSION, d);
            final List<AbstractTrollEntity> list = this.mob.level().getEntitiesOfClass(AbstractTrollEntity.class, box, troll -> this.mob.getType() == troll.getType() );
            for (final AbstractTrollEntity otherTroll : list) {

                if (this.mob == otherTroll
                        || otherTroll.getTarget() != null
                        || this.mob instanceof TamableAnimal && ((TamableAnimal) this.mob).getOwner() != otherTroll.getOwner()
                        || otherTroll.isAlliedTo(this.mob.getLastHurtByMob())
                        || (otherTroll.isRabid() && this.mob instanceof final AbstractTrollEntity troll && !troll.isRabid())) continue;

                //The Rabid Trolls don't trigger others
                if (this.mob instanceof final AbstractTrollEntity troll && troll.isRabid() && !otherTroll.isRabid()) {
                    continue;
                }

                if (this.noHelpTypes != null) {
                    boolean bl = false;
                    for (final Class<?> class_ : this.noHelpTypes) {
                        if (otherTroll.getClass() != class_) continue;
                        bl = true;
                        break;
                    }
                    if (bl) continue;
                }

                this.setMobEntityTarget(otherTroll, this.mob.getLastHurtByMob());
            }
        }

        protected void setMobEntityTarget(final Mob mob, final LivingEntity target) {
            mob.setTarget(target);
        }
    }
    protected static class DefendFriendGoal extends NearestAttackableTargetGoal<LivingEntity> {
        @Nullable
        private LivingEntity offender;
        @Nullable
        private LivingEntity friend;
        private int lastAttackedTime;
        private final AbstractTrollEntity troll;

        public DefendFriendGoal(final AbstractTrollEntity troll, final Class<LivingEntity> targetEntityClass, final boolean checkVisibility, final boolean checkCanNavigate, final Predicate<LivingEntity> targetPredicate) {
            super(troll, targetEntityClass, 10, checkVisibility, checkCanNavigate, targetPredicate);
            this.troll = troll;
        }

        @Override
        public boolean canUse() {
            if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
                return false;
            }

            for (final Player player :
                    troll.level().getEntitiesOfClass(Player.class, troll.getBoundingBox().inflate(10, 5, 10),
                            player -> (troll.getReputation(player) > 100) || (troll.getFriendship(player) > 80))) {

                final LivingEntity livingEntity;

                this.friend = livingEntity = player;

                this.offender = livingEntity.getLastHurtByMob();
                return livingEntity.getLastHurtByMobTimestamp() != this.lastAttackedTime && this.canAttack(this.offender, this.targetConditions);
            }

            return false;
        }

        @Override
        public void start() {
            this.setTarget(this.offender);
            this.target = this.offender;
            if (this.friend != null) {
                this.lastAttackedTime = this.friend.getLastHurtByMobTimestamp();
            }
            super.start();
        }
    }

    @Override
    protected @NotNull Component getTypeName() {
        if(this.isRabid()){
            return Component.translatable(this.getType().getDescriptionId()+"_rabid");
        }

        return super.getTypeName();
    }
    @Override
    public boolean canAttack(@NotNull final LivingEntity target) {
        //If it's NOT rabid, doesn't attack the Villagers
        if(!this.isRabid() && target instanceof AbstractVillager){
            return false;
        }

        //If it's NOT rabid but the Raider already attack it, attacks the raider
        if(!this.isRabid() && (this.getLastAttacker() instanceof Raider)){
            return true;
        }

        //If it's NOT rabid, AND it's not wandering doesn't attack the Raider
        if(!this.isRabid() && this.isWandering() && target instanceof Raider){
            return false;
        }

        if(target instanceof final Player player && this.getOwner() == target){
            if(this.getFriendship(player)>=this.getMinReputationToGoAgainstOwner()){
                return false;
            } else {
                this.setOwner(null);
                this.setFollowerState(0);
                player.displayClientMessage(Component.translatable("tcots_witcher.gui.troll_wandering", this.getName()), true);
                this.setGuardingPos(BlockPos.ZERO);
                return true;
            }
        }

        if (target instanceof Player && this.level().getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        return target.canBeSeenAsEnemy();
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull final Player player, @NotNull final InteractionHand hand) {
        final ItemStack itemStack = player.getItemInHand(hand);
        final Item item = itemStack.getItem();

        //Ensure this is only processed on the server
        //You can only interact with them when they aren't attacking and aren't bartering and isn't rabid and isn't blocking
        if (!this.level().isClientSide && !this.isAggressive() && !this.hasBarteringItem() && !this.isRabid() && !this.hasFoodOrAlcohol() && this.getRemainingPersistentAngerTime() == 0 && !this.isTrollBlocking()) {

            // Change from Following/Waiting to Wandering
            if ((this.isFollowing() || this.isWaiting()) && this.getOwner() == player && player.isShiftKeyDown()) {
                this.setOwner(null);
                this.setFollowerState(0);
                player.displayClientMessage(Component.translatable("tcots_witcher.gui.troll_wandering", this.getName()), true);
                this.setGuardingPos(BlockPos.ZERO);
                return InteractionResult.SUCCESS;
            }

            // Barter with Troll, only when reputation is above -75
            if (itemStack.is(this.getBarteringItem()) && this.getReputation(player) >= this.getMinReputationGiveBarterItem() && this.isWandering()) {
                this.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(this.getBarteringItem()));
                this.setLastPlayer(player);
                this.setAdmiringTime(0);

                itemStack.shrink(1);
                this.setPersistenceRequired();
                return InteractionResult.SUCCESS;
            }

            // Feed Troll
            else if (isEdible(itemStack)) {
                this.setItemInHand(InteractionHand.OFF_HAND, itemStack.copyWithCount(1));
                this.setLastPlayer(player);
                this.setEatingTime(0);

                itemStack.shrink(1);
                this.setPersistenceRequired();
                return InteractionResult.SUCCESS;
            }

            // Give Alcohol to Troll
            else if (isDrinkable(item)) {
                this.setItemInHand(InteractionHand.OFF_HAND, itemStack.copyWithCount(1));
                this.setLastPlayer(player);
                this.setEatingTime(0);

                itemStack.shrink(1);
                this.setPersistenceRequired();
                return InteractionResult.SUCCESS;
            }
            if (hand != InteractionHand.MAIN_HAND) {
                return InteractionResult.PASS;
            }

            // Handle changes in states (from Wandering to Following)
            if (((this.isWandering() && this.getFriendship(player) >= this.getMinFriendshipToBeFollower() && (this.getOwner() == null && this.getOwnerUuid() == null)) ||
                    //Following <-> Waiting
                    (!this.isWandering() && this.getOwner() == player)) && !player.isShiftKeyDown()) {

                if (this.getOwner() == null) {
                    this.setOwner(player);

                    if(player instanceof final ServerPlayer serverPlayer) TCOTS_Criteria.GetTrollFollower().trigger(serverPlayer, this);
                }

                this.setFollowerState(this.isWandering() ? 1 : (this.isFollowing() ? 2 : 1));

                player.displayClientMessage(this.isFollowing() ?
                        Component.translatable("tcots_witcher.gui.troll_follows", this.getName()) :
                        Component.translatable("tcots_witcher.gui.troll_waits", this.getName()), true);

                if (this.isWaiting()) {
                    this.setGuardingPos(this.getOnPos());
                }

                return InteractionResult.SUCCESS;
            }


        }

        return InteractionResult.PASS;
    }

    //Angerable Stuff
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), true);
        }

        if(this.isTrollBlocking()){
            setTimeBlocking(getTimeBlocking()+1);
        }

        if(this.isTrollBlocking() && this.getTimeBlocking()==this.maxTicksBlocking()){
            this.setIsTrollBlocking(false);
            setTimeBlocking(0);
        }
    }

    protected int maxTicksBlocking(){
        return 80;
    }

    protected boolean shouldAngerAtPlayer(final LivingEntity entity) {
        if(this.isRabid()){
            return true;
        }
        if (!this.canAttack(entity)) {
            return false;
        }
        if (entity.getType() == EntityType.PLAYER && this.isAngryAtAllPlayers(entity.level())) {
            return true;
        }
        return entity.getUUID().equals(this.getPersistentAngerTarget());
    }

    private int angerTime;
    @Override
    public int getRemainingPersistentAngerTime() {
        return angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(final int angerTime) {
        this.angerTime = angerTime;
    }

    @Nullable
    private UUID angryAt;
    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable final UUID angryAt) {
        this.angryAt=angryAt;
    }
    protected UniformInt getAngerTimeRange() {
        return TimeUtil.rangeOfSeconds(20, 39);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(this.getAngerTimeRange().sample(this.random));
    }

    public void setIsRabid(final boolean isRabid) {
        this.entityData.set(RABID, isRabid);
    }

    public boolean isRabid() {
        return this.entityData.get(RABID);
    }


    //Pickup stuff
    @Override
    public boolean wantsToPickUp(@NotNull final ItemStack stack) {
        return canGather();
    }

    public boolean canGather(){
        return this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && this.canPickUpLoot() && this.trollCanGather();
    }

    private boolean trollCanGather(){
        return !this.isAggressive() && !this.isBlocking() && !this.hasBarteringItem() && this.getRemainingPersistentAngerTime() == 0 && !this.hasFoodOrAlcohol() && this.getTarget()==null;
    }

    @Override
    protected void pickUpItem(@NotNull final ItemEntity item) {
        this.onItemPickup(item);
        this.handleItemFromGround(item);
    }

    protected void handleItemFromGround(@NotNull final ItemEntity item) {
        final boolean isBartering = item.getItem().getItem() == this.getBarteringItem();

        if ((isBartering && this.isWandering()) || isEdible(item.getItem()) || isDrinkable(item.getItem().getItem())) {
            this.take(item, 1);
            final ItemStack itemStack = EntitiesUtil.getItemFromStack(item);
            this.setItemInHand(InteractionHand.OFF_HAND, itemStack);
            if (item.getOwner() != null && item.getOwner() instanceof final Player player) {
                this.setLastPlayer(player);
            }
            if (isBartering) {
                this.setAdmiringTime(0);
            } else {
                this.setEatingTime(0);
            }
            this.setPersistenceRequired();
        }
    }

    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1, 1, 1);

    @Override
    protected @NotNull Vec3i getPickupReach() {
        return ITEM_PICKUP_RANGE_EXPANDER;
    }


    //Reputation System
    abstract public TrollGossips getGossip();

    abstract protected ReputationEventType getKillInteraction();
    abstract protected ReputationEventType getHurtInteraction(boolean isOther);

    abstract public ReputationEventType getDefendingInteraction(boolean isOther);
    abstract protected ReputationEventType getFeedInteraction(boolean isOther);
    abstract protected ReputationEventType getAlcoholInteraction(boolean isOther);
    abstract protected ReputationEventType getBarterInteraction(boolean isOther);

    private long lastGossipDecayTime;
    protected void decayGossip() {
        final long l = this.level().getGameTime();
        if (this.lastGossipDecayTime == 0L) {
            this.lastGossipDecayTime = l;
            return;
        }
        //Decays every Minecraft-Day
        if (l < this.lastGossipDecayTime + 24000L) {
            return;
        }
        this.getGossip().decay();
        this.lastGossipDecayTime = l;
    }

    public void handleNearTrollsInteraction(final ReputationEventType interaction, final Player player) {
        for (final AbstractTrollEntity nearTroll : this.getNearTrolls()) {
            ((ServerLevel) this.level()).onReputationEvent(interaction, player, nearTroll);
        }
    }

    public void handleNearTrollsParticles(final byte byteSent) {
        for (final AbstractTrollEntity nearTroll : this.getNearTrolls()) {
            nearTroll.level().broadcastEntityEvent(nearTroll, byteSent);
        }
    }

    public List<AbstractTrollEntity> getNearTrolls() {
        return this.level().getEntitiesOfClass(AbstractTrollEntity.class, this.getBoundingBox().inflate(10, 2, 10),
                troll -> troll.getType()== this.getType() && troll.hasLineOfSight(this) && troll != this && !troll.isRabid());
    }

    @Override
    public void setLastHurtByMob(@Nullable final LivingEntity attacker) {
        if (!this.isRabid() && attacker != null && this.level() instanceof ServerLevel && attacker instanceof final Player player) {
            ((ServerLevel) this.level()).onReputationEvent(this.getHurtInteraction(false), attacker, this);
            this.handleNearTrollsInteraction(this.getHurtInteraction(true), player);
            if (this.isAlive() && attacker instanceof Player) {
                this.level().broadcastEntityEvent(this, EntityEvent.VILLAGER_ANGRY);
                this.handleNearTrollsParticles(EntityEvent.VILLAGER_ANGRY);
            }
        }
        super.setLastHurtByMob(attacker);
    }
    @Override
    public void die(@NotNull final DamageSource damageSource) {
        final Entity attacker = damageSource.getEntity();

        if (!this.level().isClientSide && this.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
            this.getOwner().sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }

        if (!this.isRabid() && this.level() instanceof ServerLevel && attacker instanceof final Player player) {
            this.handleNearTrollsInteraction(this.getKillInteraction(), player);
            this.handleNearTrollsParticles(EntityEvent.VILLAGER_ANGRY);
        }

        super.die(damageSource);
    }

    public int getReputation(@NotNull final Player player){
        return this.getGossip().getReputationFor(player.getUUID(), gossipType -> true);
    }
    public int getFriendship(@NotNull final Player player){
        return this.getGossip().getFriendshipFor(player.getUUID(), gossipType -> true);
    }

    @Override
    public void handleEntityEvent(final byte status) {
        if (status == EntityEvent.LOVE_HEARTS) {
            this.produceParticles(ParticleTypes.HEART);
        } else if (status == EntityEvent.VILLAGER_ANGRY) {
            this.produceParticles(ParticleTypes.ANGRY_VILLAGER);
        } else if (status == EntityEvent.VILLAGER_HAPPY) {
            this.produceParticles(ParticleTypes.HAPPY_VILLAGER);
        } else if (status == EntityEvent.VILLAGER_SWEAT) {
            this.produceParticles(ParticleTypes.SPLASH);
        }
        else {
            super.handleEntityEvent(status);
        }
    }
    protected void produceParticles(final ParticleOptions parameters) {
        for (int i = 0; i < 5; ++i) {
            final double d = this.random.nextGaussian() * 0.02;
            final double e = this.random.nextGaussian() * 0.02;
            final double f = this.random.nextGaussian() * 0.02;
            this.level().addParticle(parameters, this.getRandomX(1.0), this.getRandomY() + 1.0, this.getRandomZ(1.0), d, e, f);
        }
    }


    //Bartering System
    protected static class LookAtItemInHand extends Goal {

        private final AbstractTrollEntity troll;

        public LookAtItemInHand(final AbstractTrollEntity troll) {
            this.troll = troll;
            this.setFlags(EnumSet.of(Flag.LOOK));
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return troll.hasBarteringItem() || troll.hasFoodOrAlcohol();
        }

        @Override
        public void start() {
            troll.getNavigation().stop();
            super.start();
        }
    }
    protected static class GoForItemInGroundGoal extends Goal {

        private final AbstractTrollEntity troll;
        private final double speed;

        public GoForItemInGroundGoal(final AbstractTrollEntity troll, final double speed) {
            this.troll = troll;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            final List<ItemEntity> list = searchItemsList();

            return !list.isEmpty() && troll.canGather();
        }

        @Override
        public boolean canContinueToUse() {
            final List<ItemEntity> list = searchItemsList();

            return !list.isEmpty() && troll.canGather();
        }

        @Override
        public void start() {
            final List<ItemEntity> list = searchItemsList();

            if (!list.isEmpty() && troll.canGather()) {
                this.troll.getNavigation().moveTo(list.get(0), speed);
            }
        }


        @Override
        public void tick() {
            final List<ItemEntity> list = searchItemsList();

            if (!list.isEmpty() && troll.canGather()) {
                this.startMovingTo(troll.getNavigation(), list.get(0), speed);
                this.troll.getLookControl().setLookAt(list.get(0), 30.0f, 30.0f);
            }
        }

        public void startMovingTo(final PathNavigation navigation, final Entity entity, final double speed) {
            final Path path = navigation.createPath(entity, 0);
            if (path != null) {
                navigation.moveTo(path, speed);
            }
        }

        private List<ItemEntity> searchItemsList() {
            return
                    this.troll.level().getEntitiesOfClass(ItemEntity.class,
                            this.troll.getBoundingBox().inflate(8.0, 2.0, 8.0),
                            item ->
                                    !item.hasPickUpDelay() && item.isAlive()
                                            && ((item.getItem().getItem() == troll.getBarteringItem() && troll.isWandering())
                                            || troll.isEdible(item.getItem()) || troll.isAlcohol(item.getItem().getItem())));
        }
    }

    protected int admiringTime=-1;
    public void setAdmiringTime(final int admiringTime) {
        this.admiringTime = admiringTime;
    }
    public int getAdmiringTime() {
        return admiringTime;
    }
    public void addAdmiringTime(){
        this.setAdmiringTime(getAdmiringTime()+1);
    }

    protected void tickAdmiringItem(){
        //If player attacks mid-animation
        if(this.getAdmiringTime()!=-1 && this.getAdmiringTime() < TOTAL_ADMIRING_TIME && this.getRemainingPersistentAngerTime() > 0){
            this.setLastPlayer(null);
            this.dropBarteringLoot(this, this.getLastPlayer());
            this.setAdmiringTime(-1);
        }

        if(this.getAdmiringTime() < TOTAL_ADMIRING_TIME && this.getAdmiringTime()!=-1){
            this.addAdmiringTime();
        } else if (this.getAdmiringTime() == TOTAL_ADMIRING_TIME){
            this.dropBarteringLoot(this, this.getLastPlayer());
            this.setAdmiringTime(-1);
        }
    }

    public boolean hasBarteringItem(){
        return this.getItemInHand(InteractionHand.OFF_HAND).getItem() == this.getBarteringItem();
    }

    protected void dropBarteringLoot(final AbstractTrollEntity troll, @Nullable final Player player) {
        //Drops the loot and gives reputation
        if(player!=null) {
            //Gives reputation
            ((ServerLevel) this.level()).onReputationEvent(this.getBarterInteraction(false), player, this);
            this.handleNearTrollsInteraction(this.getBarterInteraction(true), player);
            this.level().broadcastEntityEvent(this, this.getFriendship(player)>this.getMinFriendshipToBeFollower()? EntityEvent.LOVE_HEARTS: EntityEvent.VILLAGER_HAPPY);
            //Only gives items to player if player it's above -30 Reputation
            if(this.getReputation(player)>=this.getMinReputationToBarter()) {
                //Swings hand
                this.triggerAnim("GiveController", "give_item");
                for (final ItemStack itemStack : this.getBarteredItems(this)) {
                    final Vec3 vec3d = new Vec3(0.3f, 0.3f, 0.3f);
                    //Looks at player
                    this.lookAt(EntityAnchorArgument.Anchor.EYES, player.position().add(0.0, 1.0, 0.0));
                    //Gives bartered item
                    this.give(troll, itemStack, player.position().add(0.0, 1.0, 0.0), vec3d, 0.3f);
                }
                //Gives Mutagen
                if(this.getReputation(player) >= 70 && this.getRandom().nextIntBetweenInclusive(0,8)==1){
                    final Vec3 vec3d = new Vec3(0.3f, 0.3f, 0.3f);
                    this.give(troll, new ItemStack(TCOTS_Items.TROLL_MUTAGEN.get()), player.position().add(0.0, 1.0, 0.0), vec3d, 0.3f);
                }
            }
            //Resets hand/Consumes Amethyst
            this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            //Removes the player from the NBT
            this.setLastPlayer(null);

            this.playSound(TCOTS_Sounds.getSoundEvent("troll_bartering"), 1.0f, 1.0f);
        } else {
            //Resets hand/Consumes Amethyst
            this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    public void give(@NotNull final LivingEntity entity, final ItemStack stack, @NotNull final Vec3 targetLocation, @NotNull final Vec3 velocityFactor, final float yOffset) {
        final double d = entity.getEyeY() - (double)yOffset;
        final ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), d, entity.getZ(), stack);
        itemEntity.setThrower(entity);
        Vec3 vec3d = targetLocation.subtract(entity.position());
        vec3d = vec3d.normalize().multiply(velocityFactor.x, velocityFactor.y, velocityFactor.z);
        itemEntity.setDeltaMovement(vec3d);
        itemEntity.setDefaultPickUpDelay();
        entity.level().addFreshEntity(itemEntity);
    }

    protected List<ItemStack> getBarteredItems(@NotNull final AbstractTrollEntity troll) {
        if(troll.level().getServer() == null){
            return Collections.emptyList();
        }

        final LootTable lootTable = troll.level().getServer().reloadableRegistries().getLootTable(this.getTrollLootTable());
        return lootTable.getRandomItems(new LootParams.Builder((ServerLevel)troll.level()).withParameter(LootContextParams.THIS_ENTITY, troll).create(LootContextParamSets.PIGLIN_BARTER));
    }

    @Nullable
    protected Player lastPlayer;
    @Nullable
    protected UUID lastPlayerUuid;
    @Nullable
    public Player getLastPlayer() {
        final Player entity;
        if (this.lastPlayer == null && this.lastPlayerUuid != null && this.level() instanceof ServerLevel && (entity = this.level().getPlayerByUUID(this.lastPlayerUuid)) != null) {
            this.lastPlayer = entity;
        }
        return this.lastPlayer;
    }

    public void setLastPlayer(@Nullable final Player lastPlayer) {
        this.lastPlayer = lastPlayer;
        this.lastPlayerUuid = lastPlayer == null ? null : lastPlayer.getUUID();
    }

    protected int getMinReputationToBarter(){
        return -30;
    }

    protected int getMinReputationGiveBarterItem(){
        return -75;
    }

    abstract protected ResourceKey<LootTable> getTrollLootTable();

    abstract protected Item getBarteringItem();

    //Follower System
    protected static class TrollFollowFriendGoal extends Goal {

        private final AbstractTrollEntity troll;
        private LivingEntity owner;
        private final LevelReader world;
        private final double speed;
        private final PathNavigation navigation;
        private int updateCountdownTicks;
        private final float maxDistance;
        private final float minDistance;
        private float oldWaterPathfindingPenalty;
        private final boolean leavesAllowed;

        public TrollFollowFriendGoal(final AbstractTrollEntity troll, final double speed, final float minDistance, final float maxDistance, final boolean leavesAllowed) {
            this.troll = troll;
            this.world = troll.level();
            this.speed = speed;
            this.navigation = troll.getNavigation();
            this.minDistance = minDistance;
            this.maxDistance = maxDistance;
            this.leavesAllowed = leavesAllowed;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            if (!(troll.getNavigation() instanceof GroundPathNavigation) && !(troll.getNavigation() instanceof FlyingPathNavigation)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        @Override
        public boolean canUse() {
            final LivingEntity livingEntity = (LivingEntity) troll.getOwner();
            if (livingEntity == null) {
                return false;
            }
            if (livingEntity.isSpectator()) {
                return false;
            }
            if (this.cannotFollow()) {
                return false;
            }
            if (this.troll.distanceToSqr(livingEntity) < (double) (this.minDistance * this.minDistance)) {
                return false;
            }
            this.owner = livingEntity;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            }
            if (this.cannotFollow()) {
                return false;
            }
            return !(this.troll.distanceToSqr(this.owner) <= (double) (this.maxDistance * this.maxDistance));
        }

        private boolean cannotFollow() {
            return this.troll.isWaiting() || this.troll.isPassenger() || this.troll.isLeashed();
        }

        @Override
        public void start() {
            this.updateCountdownTicks = 0;
            this.oldWaterPathfindingPenalty = this.troll.getPathfindingMalus(PathType.WATER);
            this.troll.setPathfindingMalus(PathType.WATER, 0.0f);
        }

        @Override
        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.troll.setPathfindingMalus(PathType.WATER, this.oldWaterPathfindingPenalty);
        }

        @Override
        public void tick() {
            this.troll.getLookControl().setLookAt(this.owner, 10.0f, this.troll.getMaxHeadXRot());
            if (--this.updateCountdownTicks > 0) {
                return;
            }
            this.updateCountdownTicks = this.adjustedTickDelay(10);
            if (this.troll.distanceToSqr(this.owner) >= 324.0) {
                this.tryTeleport();
            } else {
                this.navigation.moveTo(this.owner, this.speed);
            }
        }

        private void tryTeleport() {
            final BlockPos blockPos = this.owner.blockPosition();
            for (int i = 0; i < 10; ++i) {
                final int j = this.getRandomInt(-3, 3);
                final int k = this.getRandomInt(-1, 1);
                final int l = this.getRandomInt(-3, 3);
                final boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
                if (!bl) continue;
                return;
            }
        }

        private boolean tryTeleportTo(final int x, final int y, final int z) {
            if (Math.abs((double) x - this.owner.getX()) < 2.0 && Math.abs((double) z - this.owner.getZ()) < 2.0) {
                return false;
            }
            if (!this.canTeleportTo(new BlockPos(x, y, z))) {
                return false;
            }
            this.troll.moveTo((double) x + 0.5, y, (double) z + 0.5, this.troll.getYRot(), this.troll.getXRot());
            this.navigation.stop();
            return true;
        }

        private boolean canTeleportTo(final BlockPos pos) {
            final PathType pathNodeType = WalkNodeEvaluator.getPathTypeStatic(this.troll, pos.mutable());
            if (pathNodeType != PathType.WALKABLE) {
                return false;
            }
            final BlockState blockState = this.world.getBlockState(pos.below());
            if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
                return false;
            }
            final BlockPos blockPos = pos.subtract(this.troll.blockPosition());
            return this.world.noCollision(this.troll, this.troll.getBoundingBox().move(blockPos));
        }

        private int getRandomInt(final int min, final int max) {
            return this.troll.getRandom().nextInt(max - min + 1) + min;
        }
    }
    protected static class ReturnToGuardPosition extends Goal {

        private final AbstractTrollEntity troll;
        private final double speed;

        public ReturnToGuardPosition(final AbstractTrollEntity troll, final double speed) {
            this.troll = troll;
            this.speed = speed;
        }

        @Override
        public boolean canUse() {
            return troll.isWaiting() && troll.getTarget() == null
                    && !troll.isTrollBlocking()
                    && troll.getOnPos() != troll.getGuardingPos()
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

        private List<ItemEntity> searchItemsList() {
            return
                    this.troll.level().getEntitiesOfClass(ItemEntity.class,
                            this.troll.getBoundingBox().inflate(8.0, 2.0, 8.0),
                            item ->
                                    !item.hasPickUpDelay() && item.isAlive()
                                            && ((item.getItem().getItem() == troll.getBarteringItem() && troll.isWandering())
                                            || troll.isEdible(item.getItem()) || troll.isAlcohol(item.getItem().getItem())));
        }

        public void startMovingTo(final PathNavigation navigation, final int x, final int y, final int z, final double speed) {
            navigation.moveTo(navigation.createPath(x, y, z, 0), speed);
        }

    }

    protected int getMinReputationToGoAgainstOwner(){
        return 75;
    }

    public int getMinFriendshipToBeFollower(){
        return 150;
    }

    protected @Nullable UUID ownerUuid;
    @Nullable
    @Override
    public Entity getOwner() {
        final UUID uUID = this.getOwnerUuid();
        if (uUID == null) {
            return null;
        }

        return this.level().getPlayerByUUID(uUID);
    }
    public void setOwner(@Nullable final Player owner) {
        this.ownerUuid = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.ownerUuid;
    }

    public void setFollowerState(final int followerState) {

        this.entityData.set(FOLLOWER_STATE, followerState);

        if (this.isFollowing() || this.isWaiting()) {
            this.playSound(this.isWaiting() ? TCOTS_Sounds.getSoundEvent("troll_waiting") : TCOTS_Sounds.getSoundEvent("troll_follow"), 1.0f, 1.0f);

            Objects.requireNonNull(this.getAttribute(Attributes.ARMOR)).setBaseValue(
                    this.getType() == TCOTS_Entities.RockTroll()? 16.0:
                    this.getType() == TCOTS_Entities.IceTroll()?  12.0:
                            4.0);
            Objects.requireNonNull(this.getAttribute(Attributes.ARMOR_TOUGHNESS)).setBaseValue(
                    this.getType() == TCOTS_Entities.RockTroll()? 8.0:
                    this.getType() == TCOTS_Entities.IceTroll()?  6.0:
                            1.0);
        } else {
            this.playSound(TCOTS_Sounds.getSoundEvent("troll_dismiss"), 1.0f, 1.0f);
            Objects.requireNonNull(this.getAttribute(Attributes.ARMOR)).setBaseValue(
                    this.getType() == TCOTS_Entities.RockTroll()? 8.0:
                    this.getType() == TCOTS_Entities.IceTroll()?  6.0:
                            4.0);
            Objects.requireNonNull(this.getAttribute(Attributes.ARMOR_TOUGHNESS)).setBaseValue(
                    this.getType() == TCOTS_Entities.RockTroll()? 4.0:
                    this.getType() == TCOTS_Entities.IceTroll()?  2.0:
                            1.0);
        }
    }

    public int getFollowerState() {
        return this.entityData.get(FOLLOWER_STATE);
    }

    public boolean isWaiting() {
        return this.getFollowerState() == 2;
    }

    public boolean isFollowing() {
        return this.getFollowerState() == 1;
    }

    public boolean isWandering() {
        return this.getFollowerState() == 0;
    }

    public void setGuardingPos(final BlockPos pos) {
        this.entityData.set(GUARDING_POS, pos);
    }
    public BlockPos getGuardingPos() {
        return this.entityData.get(GUARDING_POS);
    }

    //Eating Code
    public void setEatingTime(final int eatingTime) {
        this.entityData.set(EATING_TIME, eatingTime);
    }
    public int getEatingTime() {
        return this.entityData.get(EATING_TIME);
    }
    public void addEatingTime(){
        this.setEatingTime(getEatingTime()+1);
    }

    //Animation things
    public float eatingProgress;
    public float maxEatingDeviation;
    public float prevMaxEatingDeviation;
    public float prevEatingProgress;
    public float eatingSpeed = 1.0f;
    @Override
    public void aiStep() {
        super.aiStep();

        this.prevEatingProgress = this.eatingProgress;
        this.prevMaxEatingDeviation = this.maxEatingDeviation;

        if(this.hasFoodOrAlcohol()){
            this.maxEatingDeviation = this.maxEatingDeviation + (-1f*0.3f);
        }

        this.maxEatingDeviation = Mth.clamp(this.maxEatingDeviation, -0.05f, 0.05f);
        if (this.hasFoodOrAlcohol() && this.eatingSpeed < 1.0f) {
            this.eatingSpeed = 1.0f;
        }

        this.eatingSpeed = this.eatingSpeed * 0.9f;

        this.eatingProgress = this.eatingProgress + (this.eatingSpeed * 1.5f);
    }

    @SuppressWarnings("all")
    private final int TOTAL_ADMIRING_TIME=60;
    @SuppressWarnings("all")
    private final int TOTAL_EATING_TIME=40;

    protected void tickEatingItem(){
        //If player attacks mid-animation
        if(this.getEatingTime()!=-1 && this.getEatingTime() < TOTAL_EATING_TIME && this.getRemainingPersistentAngerTime() > 0){
            this.setLastPlayer(null);
            this.handleEndsFeed(this.getLastPlayer(), this.getItemInHand(InteractionHand.OFF_HAND));
            this.setEatingTime(-1);
        }

        if(this.getEatingTime() < TOTAL_EATING_TIME && this.getEatingTime()!=-1){
            final ItemStack foodStack = this.getItemInHand(InteractionHand.OFF_HAND);
            this.addEatingTime();
            if ((foodStack.getUseAnimation() == UseAnim.DRINK) && this.getEatingTime()%5==0) {
                this.playSound(this.getDrinkingSound(foodStack), 0.5f, this.level().random.nextFloat() * 0.1f + 0.9f);
            }
            if (foodStack.getUseAnimation() == UseAnim.EAT && this.getEatingTime()%5==0) {
                this.spawnItemParticles(foodStack);
                this.playSound(this.getEatingSound(foodStack), 0.5f + 0.5f * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
            }

        } else if (this.getEatingTime() == TOTAL_EATING_TIME){
            this.handleEndsFeed(this.getLastPlayer(), this.getItemInHand(InteractionHand.OFF_HAND));
        }
    }

    private void handleEndsFeed(final Player player, final ItemStack foodStack){
        if(!this.level().isClientSide) {
            if (foodStack.getUseAnimation() == UseAnim.DRINK) {

                if(player!=null) {
                    ((ServerLevel) this.level()).onReputationEvent(this.getAlcoholInteraction(false), player, this);
                    this.handleNearTrollsInteraction(this.getAlcoholInteraction(true), player);
                    this.setPersistenceRequired();
                }

                final ItemStack dropStack =
                        (foodStack.getItem() == Items.HONEY_BOTTLE || foodStack.getItem() instanceof PotionItem || foodStack.getItem() instanceof HerbalMixture) ?
                                new ItemStack(Items.GLASS_BOTTLE):
                                foodStack.getItem() == Items.MILK_BUCKET ?
                                        new ItemStack(Items.BUCKET):
                                        ItemStack.EMPTY;

                foodStack.finishUsingItem(this.level(), this);

                this.spawnAtLocation(dropStack);
            }

            if (foodStack.getUseAnimation() == UseAnim.EAT) {
                if(player!=null) {
                    ((ServerLevel) this.level()).onReputationEvent(this.getFeedInteraction(false), player, this);
                    this.handleNearTrollsInteraction(this.getFeedInteraction(true), player);
                    this.setPersistenceRequired();
                }

                if(foodStack.has(DataComponents.FOOD)){
                    this.heal(Objects.requireNonNull(foodStack.get(DataComponents.FOOD)).nutrition());
                }

                this.spawnAtLocation(foodStack.getItem() == Items.RABBIT_STEW? new ItemStack(Items.BOWL): ItemStack.EMPTY);
            }


            this.level().broadcastEntityEvent(this,
                    player!=null && this.getFriendship(player)>this.getMinFriendshipToBeFollower()? EntityEvent.LOVE_HEARTS:
                            EntityEvent.VILLAGER_HAPPY);

            this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);

            this.setEatingTime(-1);
        }
    }

    protected void spawnItemParticles(final ItemStack stack) {
        for (int i = 0; i < 5; ++i) {
            final Vec3 vec3dVelocity = new Vec3(
                    ((double)this.random.nextFloat() - 0.5) * 0.1,
                    Math.random() * 0.1 + 0.1,
                    0.0)
                    .xRot(-this.getXRot() * ((float)Math.PI / 180))
                    .yRot(-this.getYRot() * ((float)Math.PI / 180));

            final Vec3 vec3dPos = new Vec3((
                    (double)this.random.nextFloat() - 0.5) * 0.1,
                    (double)(-this.random.nextFloat()) * 0.01,
                    1.2 + ((double)this.random.nextFloat() - 0.5) * 0.1)
                    .yRot(-this.yBodyRot * ((float)Math.PI / 180))
                    .add(this.getX(), this.getY(), this.getZ());

            this.level().addParticle(
                    new ItemParticleOption(ParticleTypes.ITEM, stack),
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

    protected boolean hasFoodOrAlcohol(){
        return this.isEdible(this.getItemInHand(InteractionHand.OFF_HAND)) || this.isDrinkable(this.getItemInHand(InteractionHand.OFF_HAND).getItem());
    }

    protected boolean isEdible(final ItemStack itemStack){
        return (itemStack.has(DataComponents.FOOD) && itemStack.is(ItemTags.MEAT) || itemStack.is(Items.RABBIT_STEW));
    }

    protected boolean isDrinkable(final Item item){
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

    protected boolean isAlcohol(final Item item){
        return item instanceof WitcherAlcohol_Base;
    }

    @Override
    public void tick() {
        super.tick();

        this.decayGossip();

        this.tickAdmiringItem();

        this.tickEatingItem();

        if(!this.level().isClientSide && !this.isAggressive() && this.tickCount%600==0) {
            for (final AbstractTrollEntity troll: getNearTrolls()){
                this.getGossip().shareGossipsWith(this, troll);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        this.addPersistentAngerSaveData(nbt);
        nbt.put("Gossips", this.getGossip().serialize(NbtOps.INSTANCE));
        nbt.putLong("LastGossipDecay", this.lastGossipDecayTime);

        nbt.putBoolean("IsRabid", this.isRabid());

        nbt.putInt("AdmiringTime", this.admiringTime);
        nbt.putInt("EatingTime", this.getEatingTime());

        if (this.lastPlayerUuid != null) {
            nbt.putUUID("LastPlayer", this.lastPlayerUuid);
        }

        if (this.ownerUuid != null) {
            nbt.putUUID("Owner", this.ownerUuid);
        }

        nbt.putInt("FollowerState", this.getFollowerState());

        nbt.putInt("GuardPosX", this.getGuardingPos().getX());
        nbt.putInt("GuardPosY", this.getGuardingPos().getY());
        nbt.putInt("GuardPosZ", this.getGuardingPos().getZ());


        nbt.putBoolean("Blocking", this.isTrollBlocking());
        nbt.putInt("TimeBlocking", this.getTimeBlocking());
    }

    @Override
    public void readAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.readPersistentAngerSaveData(this.level(), nbt);
        final ListTag nbtList = nbt.getList("Gossips", Tag.TAG_COMPOUND);
        this.getGossip().deserialize(new Dynamic<>(NbtOps.INSTANCE, nbtList));
        this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");

        setIsRabid(nbt.getBoolean("IsRabid"));

        this.admiringTime = nbt.getInt("AdmiringTime");
        setEatingTime(nbt.getInt("EatingTime"));

        if (nbt.hasUUID("LastPlayer")) {
            this.lastPlayerUuid = nbt.getUUID("LastPlayer");
        }

        if (nbt.hasUUID("Owner")) {
            this.ownerUuid = nbt.getUUID("Owner");
        }

        this.setFollowerState(nbt.getInt("FollowerState"));

        final int x = nbt.getInt("GuardPosX");
        final int y = nbt.getInt("GuardPosY");
        final int z = nbt.getInt("GuardPosZ");
        this.setGuardingPos(new BlockPos(x, y, z));


        setIsTrollBlocking(nbt.getBoolean("Blocking"));
        setTimeBlocking(nbt.getInt("TimeBlocking"));
    }


    //Blocking System
    public void setIsTrollBlocking(final boolean isBlocking) {
        this.entityData.set(BLOCKING, isBlocking);
        if(!isBlocking){
            this.triggerAnim("BlockController", "unblock");
        }
    }

    public boolean isTrollBlocking() {
        return this.entityData.get(BLOCKING);
    }

    public void setTimeBlocking(final int timeBlocking) {
        this.entityData.set(TIME_BLOCKING, timeBlocking);
    }

    public int getTimeBlocking() {
        return this.entityData.get(TIME_BLOCKING);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull final DamageSource damageSource) {
        return (this.isTrollBlocking()
                && !damageSource.is(DamageTypeTags.BYPASSES_SHIELD)
                && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR)
                && !(damageSource.getDirectEntity() instanceof AbstractArrow && ((AbstractArrow) damageSource.getDirectEntity()).getPierceLevel() > 0)
                && !(this instanceof ForestTrollEntity))

                || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isTrollBlocking() && !this.isWaiting();
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isTrollBlocking() && this.isAlive();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return !this.isPersistenceRequired();
    }

    //Sounds
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("troll_attack");
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAggressive()? TCOTS_Sounds.getSoundEvent("troll_furious"): this.isRabid()?  TCOTS_Sounds.getSoundEvent("troll_idle"): TCOTS_Sounds.getSoundEvent("troll_grunt");
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("troll_death");
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("troll_hurt");
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }
}

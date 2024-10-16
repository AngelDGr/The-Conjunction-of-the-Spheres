package TCOTS.entity.ogroids;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.items.HerbalMixture;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.WitcherPotions_Base;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
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
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.*;
import java.util.function.Predicate;

public abstract class AbstractTrollEntity extends OgroidMonster implements GeoEntity, Angerable, Ownable, InteractionObserver {

    private static final TrackedData<Boolean> RABID = DataTracker.registerData(AbstractTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> EATING_TIME = DataTracker.registerData(AbstractTrollEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<BlockPos> GUARDING_POS = DataTracker.registerData(AbstractTrollEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Integer> FOLLOWER_STATE = DataTracker.registerData(AbstractTrollEntity.class, TrackedDataHandlerRegistry.INTEGER);


    protected static final TrackedData<Boolean> BLOCKING = DataTracker.registerData(AbstractTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> TIME_BLOCKING = DataTracker.registerData(AbstractTrollEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final RawAnimation GIVE_ITEM = RawAnimation.begin().thenPlay("special.give");

    public static final RawAnimation BLOCK = RawAnimation.begin().thenPlayAndHold("special.block");
    public static final RawAnimation UNBLOCK = RawAnimation.begin().thenPlay("special.unblock");

    public AbstractTrollEntity(EntityType<? extends AbstractTrollEntity> entityType, World world) {
        super(entityType, world);
        this.setCanPickUpLoot(true);
        this.experiencePoints=8;
        this.setStepHeight(1.0f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(RABID, Boolean.FALSE);
        this.dataTracker.startTracking(EATING_TIME, -1);
        this.dataTracker.startTracking(GUARDING_POS, BlockPos.ORIGIN);
        this.dataTracker.startTracking(FOLLOWER_STATE, 0);

        this.dataTracker.startTracking(BLOCKING, Boolean.FALSE);
        this.dataTracker.startTracking(TIME_BLOCKING, 0);
    }

    protected static class MeleeAttackGoal_Troll extends MeleeAttackGoal_Animated {

        private final AbstractTrollEntity troll;

        public MeleeAttackGoal_Troll(AbstractTrollEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.troll = mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !troll.isTrollBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !troll.isTrollBlocking();
        }
    }
    protected static class ProjectileAttackGoal_RockTroll extends ProjectileAttackGoal {
        private final RockTrollEntity troll;
        private final float distanceForAttack;

        public ProjectileAttackGoal_RockTroll(RockTrollEntity mob, double mobSpeed, int intervalTicks, float maxShootRange, float distanceForAttack) {
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
                    && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && distanceCondition();
        }
    }

    protected static class WanderAroundGoal_Troll extends WanderAroundGoal {
        private final AbstractTrollEntity troll;

        public WanderAroundGoal_Troll(AbstractTrollEntity mob, double speed, int chance) {
            super(mob, speed, chance);
            this.troll = mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering() && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && this.troll.isWandering() && !this.troll.isTrollBlocking();
        }
    }

    protected static class LookAroundGoal_Troll extends LookAroundGoal {
        private final AbstractTrollEntity troll;

        public LookAroundGoal_Troll(AbstractTrollEntity mob) {
            super(mob);
            this.troll = mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !this.troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }
    }

    protected static class LookAtEntityGoal_Troll extends LookAtEntityGoal {

        private final AbstractTrollEntity troll;

        public LookAtEntityGoal_Troll(AbstractTrollEntity mob, Class<? extends LivingEntity> targetType, float range) {
            super(mob, targetType, range);
            this.troll = mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !troll.hasBarteringItem() && !this.troll.hasFoodOrAlcohol() && !this.troll.isTrollBlocking();
        }
    }

    protected static class LookAtPlayerWithWeaponGoal extends LookAtEntityGoal {
        private final AbstractTrollEntity troll;

        public LookAtPlayerWithWeaponGoal(AbstractTrollEntity troll, Class<? extends PlayerEntity> targetType, float range) {
            this(troll, targetType, range, 1f);
        }

        public LookAtPlayerWithWeaponGoal(AbstractTrollEntity troll, Class<? extends PlayerEntity> targetType, float range, float chance) {
            this(troll, targetType, range, chance, false);
        }

        public LookAtPlayerWithWeaponGoal(AbstractTrollEntity troll, Class<? extends PlayerEntity> targetType, float range, float chance, boolean lookForward) {
            super(troll, targetType, range, chance, lookForward);
            this.troll = troll;
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

    protected static class TrollUniversalAngerGoal<T extends AbstractTrollEntity> extends Goal {

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

        private List<? extends AbstractTrollEntity> getOthersInRange() {
            double d = this.troll.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
            Box box = Box.from(this.troll.getPos()).expand(d, BOX_VERTICAL_EXPANSION, d);
            //This way only other Rabid can trigger Rabid
            //This way the Rabid can't trigger others neutral Trolls
            if (troll.isRabid()) {
                return this.troll.getWorld().getEntitiesByClass(AbstractTrollEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR
                        .and(entity -> ((AbstractTrollEntity) (entity)).isRabid()).and(entity -> entity.getType()==this.troll.getType()));
            } else {
                return this.troll.getWorld().getEntitiesByClass(AbstractTrollEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR
                        .and(entity -> entity.getType()==this.troll.getType()));
            }
        }
    }

    protected static class TrollTargetWithReputationGoal extends TrackTargetGoal {
        private final AbstractTrollEntity troll;
        @Nullable
        private LivingEntity target;
        private final TargetPredicate targetPredicate = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);

        public TrollTargetWithReputationGoal(AbstractTrollEntity troll) {
            super(troll, false, true);
            this.troll = troll;
            this.setControls(EnumSet.of(Goal.Control.TARGET));
        }

        @Override
        public boolean canStart() {
            Box box = this.troll.getBoundingBox().expand(10.0, 8.0, 10.0);
            this.target = null;

            List<AbstractTrollEntity> listTrolls = this.troll.getWorld().getTargets(AbstractTrollEntity.class, this.targetPredicate, this.troll, box);
            List<PlayerEntity> listPlayers = this.troll.getWorld().getPlayers(this.targetPredicate, this.troll, box);
            //To add itself to the list
            listTrolls.add(this.troll);


            //Attack defending other trolls
            for (AbstractTrollEntity trollAround : listTrolls) {
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

    protected static class TrollRevengeGoal extends TrackTargetGoal {
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
            List<AbstractTrollEntity> list = this.mob.getWorld().getEntitiesByClass(AbstractTrollEntity.class, box, troll -> this.mob.getType() == troll.getType() );
            for (AbstractTrollEntity otherTroll : list) {

                if (this.mob == otherTroll
                        || otherTroll.getTarget() != null
                        || this.mob instanceof TameableEntity && ((TameableEntity) this.mob).getOwner() != otherTroll.getOwner()
                        || otherTroll.isTeammate(this.mob.getAttacker())
                        || (otherTroll.isRabid() && this.mob instanceof AbstractTrollEntity troll && !troll.isRabid())) continue;

                //The Rabid Trolls don't trigger others
                if (this.mob instanceof AbstractTrollEntity troll && troll.isRabid() && !otherTroll.isRabid()) {
                    continue;
                }

                if (this.noHelpTypes != null) {
                    boolean bl = false;
                    for (Class<?> class_ : this.noHelpTypes) {
                        if (otherTroll.getClass() != class_) continue;
                        bl = true;
                        break;
                    }
                    if (bl) continue;
                }

                this.setMobEntityTarget(otherTroll, this.mob.getAttacker());
            }
        }

        protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
            mob.setTarget(target);
        }
    }
    protected static class DefendFriendGoal extends ActiveTargetGoal<LivingEntity> {
        @Nullable
        private LivingEntity offender;
        @Nullable
        private LivingEntity friend;
        private int lastAttackedTime;
        private final AbstractTrollEntity troll;

        public DefendFriendGoal(AbstractTrollEntity troll, Class<LivingEntity> targetEntityClass, boolean checkVisibility, boolean checkCanNavigate, Predicate<LivingEntity> targetPredicate) {
            super(troll, targetEntityClass, 10, checkVisibility, checkCanNavigate, targetPredicate);
            this.troll = troll;
        }

        @Override
        public boolean canStart() {
            if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
                return false;
            }

            for (PlayerEntity player :
                    troll.getWorld().getEntitiesByClass(PlayerEntity.class, troll.getBoundingBox().expand(10, 5, 10),
                            player -> (troll.getReputation(player) > 100) || (troll.getFriendship(player) > 80))) {

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

    @Override
    protected Text getDefaultName() {
        if(this.isRabid()){
            return Text.translatable(this.getType().getTranslationKey()+"_rabid");
        }

        return super.getDefaultName();
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

        //If it's NOT rabid, AND it's not wandering doesn't attack the Raider
        if(!this.isRabid() && this.isWandering() && target instanceof RaiderEntity){
            return false;
        }

        if(target instanceof PlayerEntity player && this.getOwner() == target){
            if(this.getFriendship(player)>=this.getMinReputationToGoAgainstOwner()){
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

    @Override
    protected ActionResult interactMob(@NotNull PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        //Ensure this is only processed on the server
        //You can only interact with them when they aren't attacking and aren't bartering and isn't rabid and isn't blocking
        if (!this.getWorld().isClient && !this.isAttacking() && !this.hasBarteringItem() && !this.isRabid() && !this.hasFoodOrAlcohol() && this.getAngerTime() == 0 && !this.isTrollBlocking()) {

            // Change from Following/Waiting to Wandering
            if ((this.isFollowing() || this.isWaiting()) && this.getOwner() == player && player.isSneaking()) {
                this.setOwner(null);
                this.setFollowerState(0);
                player.sendMessage(Text.translatable("tcots-witcher.gui.troll_wandering", this.getName()), true);
                this.setGuardingPos(BlockPos.ORIGIN);
                return ActionResult.SUCCESS;
            }

            // Barter with Troll, only when reputation is above -75
            if (itemStack.isOf(this.getBarteringItem()) && this.getReputation(player) >= this.getMinReputationGiveBarterItem() && this.isWandering()) {
                this.setStackInHand(Hand.OFF_HAND, new ItemStack(this.getBarteringItem()));
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
            if (((this.isWandering() && this.getFriendship(player) >= this.getMinFriendshipToBeFollower() && (this.getOwner() == null && this.getOwnerUuid() == null)) ||
                    //Following <-> Waiting
                    (!this.isWandering() && this.getOwner() == player)) && !player.isSneaking()) {

                if (this.getOwner() == null) {
                    this.setOwner(player);

                    if(player instanceof ServerPlayerEntity serverPlayer) TCOTS_Criteria.GET_TROLL_FOLLOWER.trigger(serverPlayer, this);
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

    //Angerable Stuff
    @Override
    protected void mobTick() {
        super.mobTick();

        if (!this.getWorld().isClient) {
            this.tickAngerLogic((ServerWorld)this.getWorld(), true);
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

    protected boolean shouldAngerAtPlayer(LivingEntity entity) {
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
    protected UniformIntProvider getAngerTimeRange() {
        return TimeHelper.betweenSeconds(20, 39);
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(this.getAngerTimeRange().get(this.random));
    }

    public void setIsRabid(boolean isRabid) {
        this.dataTracker.set(RABID, isRabid);
    }

    public boolean isRabid() {
        return this.dataTracker.get(RABID);
    }


    //Pickup stuff
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

    @Override
    protected void loot(ItemEntity item) {
        this.triggerItemPickedUpByEntityCriteria(item);
        this.handleItemFromGround(item);
    }

    protected void handleItemFromGround(@NotNull ItemEntity item) {
        boolean isBartering = item.getStack().getItem() == this.getBarteringItem();

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

    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1, 1, 1);

    @Override
    protected Vec3i getItemPickUpRangeExpander() {
        return ITEM_PICKUP_RANGE_EXPANDER;
    }


    //Reputation System
    abstract public TrollGossips getGossip();

    abstract protected EntityInteraction getKillInteraction();
    abstract protected EntityInteraction getHurtInteraction(boolean isOther);

    abstract public EntityInteraction getDefendingInteraction(boolean isOther);
    abstract protected EntityInteraction getFeedInteraction(boolean isOther);
    abstract protected EntityInteraction getAlcoholInteraction(boolean isOther);
    abstract protected EntityInteraction getBarterInteraction(boolean isOther);

    private long lastGossipDecayTime;
    protected void decayGossip() {
        long l = this.getWorld().getTime();
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

    public void handleNearTrollsInteraction(EntityInteraction interaction, PlayerEntity player) {
        for (AbstractTrollEntity nearTroll : this.getNearTrolls()) {
            ((ServerWorld) this.getWorld()).handleInteraction(interaction, player, nearTroll);
        }
    }

    public void handleNearTrollsParticles(byte byteSent) {
        for (AbstractTrollEntity nearTroll : this.getNearTrolls()) {
            nearTroll.getWorld().sendEntityStatus(nearTroll, byteSent);
        }
    }

    public List<AbstractTrollEntity> getNearTrolls() {
        return this.getWorld().getEntitiesByClass(AbstractTrollEntity.class, this.getBoundingBox().expand(10, 2, 10),
                troll -> troll.getType()== this.getType() && troll.canSee(this) && troll != this && !troll.isRabid());
    }

    @Override
    public void setAttacker(@Nullable LivingEntity attacker) {
        if (!this.isRabid() && attacker != null && this.getWorld() instanceof ServerWorld && attacker instanceof PlayerEntity player) {
            ((ServerWorld) this.getWorld()).handleInteraction(this.getHurtInteraction(false), attacker, this);
            this.handleNearTrollsInteraction(this.getHurtInteraction(true), player);
            if (this.isAlive() && attacker instanceof PlayerEntity) {
                this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
                this.handleNearTrollsParticles(EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
            }
        }
        super.setAttacker(attacker);
    }
    @Override
    public void onDeath(@NotNull DamageSource damageSource) {
        Entity attacker = damageSource.getAttacker();

        if (!this.getWorld().isClient && this.getWorld().getGameRules().getBoolean(GameRules.SHOW_DEATH_MESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
            this.getOwner().sendMessage(this.getDamageTracker().getDeathMessage());
        }

        if (!this.isRabid() && this.getWorld() instanceof ServerWorld && attacker instanceof PlayerEntity player) {
            this.handleNearTrollsInteraction(this.getKillInteraction(), player);
            this.handleNearTrollsParticles(EntityStatuses.ADD_VILLAGER_ANGRY_PARTICLES);
        }

        super.onDeath(damageSource);
    }

    public int getReputation(@NotNull PlayerEntity player){
        return this.getGossip().getReputationFor(player.getUuid(), gossipType -> true);
    }
    public int getFriendship(@NotNull PlayerEntity player){
        return this.getGossip().getFriendshipFor(player.getUuid(), gossipType -> true);
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


    //Bartering System
    protected static class LookAtItemInHand extends Goal {

        private final AbstractTrollEntity troll;

        public LookAtItemInHand(AbstractTrollEntity troll) {
            this.troll = troll;
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
    protected static class GoForItemInGroundGoal extends Goal {

        private final AbstractTrollEntity troll;
        private final double speed;

        public GoForItemInGroundGoal(AbstractTrollEntity troll, double speed) {
            this.troll = troll;
            this.speed = speed;
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

        private List<ItemEntity> searchItemsList() {
            return
                    this.troll.getWorld().getEntitiesByClass(ItemEntity.class,
                            this.troll.getBoundingBox().expand(8.0, 2.0, 8.0),
                            item ->
                                    !item.cannotPickup() && item.isAlive()
                                            && ((item.getStack().getItem() == troll.getBarteringItem() && troll.isWandering())
                                            || troll.isEdible(item.getStack().getItem()) || troll.isAlcohol(item.getStack().getItem())));
        }
    }

    protected int admiringTime=-1;
    public void setAdmiringTime(int admiringTime) {
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
        if(this.getAdmiringTime()!=-1 && this.getAdmiringTime() < TOTAL_ADMIRING_TIME && this.getAngerTime() > 0){
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
        return this.getStackInHand(Hand.OFF_HAND).getItem() == this.getBarteringItem();
    }

    protected void dropBarteringLoot(AbstractTrollEntity troll, @Nullable PlayerEntity player) {
        //Drops the loot and gives reputation
        if(player!=null) {
            //Gives reputation
            ((ServerWorld) this.getWorld()).handleInteraction(this.getBarterInteraction(false), player, this);
            this.handleNearTrollsInteraction(this.getBarterInteraction(true), player);
            this.getWorld().sendEntityStatus(this, this.getFriendship(player)>this.getMinFriendshipToBeFollower()? EntityStatuses.ADD_VILLAGER_HEART_PARTICLES: EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            //Only gives items to player if player it's above -30 Reputation
            if(this.getReputation(player)>=this.getMinReputationToBarter()) {
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

    public void give(@NotNull LivingEntity entity, ItemStack stack, @NotNull Vec3d targetLocation, @NotNull Vec3d velocityFactor, float yOffset) {
        double d = entity.getEyeY() - (double)yOffset;
        ItemEntity itemEntity = new ItemEntity(entity.getWorld(), entity.getX(), d, entity.getZ(), stack);
        itemEntity.setThrower(entity);
        Vec3d vec3d = targetLocation.subtract(entity.getPos());
        vec3d = vec3d.normalize().multiply(velocityFactor.x, velocityFactor.y, velocityFactor.z);
        itemEntity.setVelocity(vec3d);
        itemEntity.setToDefaultPickupDelay();
        entity.getWorld().spawnEntity(itemEntity);
    }

    protected List<ItemStack> getBarteredItems(@NotNull AbstractTrollEntity troll) {
        if(troll.getWorld().getServer() == null){
            return Collections.emptyList();
        }

        LootTable lootTable = troll.getWorld().getServer().getLootManager().getLootTable(this.getTrollLootTable());
        return lootTable.generateLoot(new LootContextParameterSet.Builder((ServerWorld)troll.getWorld()).add(LootContextParameters.THIS_ENTITY, troll).build(LootContextTypes.BARTER));
    }

    @Nullable
    protected PlayerEntity lastPlayer;
    @Nullable
    protected UUID lastPlayerUuid;
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

    protected int getMinReputationToBarter(){
        return -30;
    }

    protected int getMinReputationGiveBarterItem(){
        return -75;
    }

    abstract protected Identifier getTrollLootTable();

    abstract protected Item getBarteringItem();

    //Follower System
    protected static class TrollFollowFriendGoal extends Goal {

        private final AbstractTrollEntity troll;
        private LivingEntity owner;
        private final WorldView world;
        private final double speed;
        private final EntityNavigation navigation;
        private int updateCountdownTicks;
        private final float maxDistance;
        private final float minDistance;
        private float oldWaterPathfindingPenalty;
        private final boolean leavesAllowed;

        public TrollFollowFriendGoal(AbstractTrollEntity troll, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
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
            if (this.troll.squaredDistanceTo(livingEntity) < (double) (this.minDistance * this.minDistance)) {
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
            return !(this.troll.squaredDistanceTo(this.owner) <= (double) (this.maxDistance * this.maxDistance));
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
            if (Math.abs((double) x - this.owner.getX()) < 2.0 && Math.abs((double) z - this.owner.getZ()) < 2.0) {
                return false;
            }
            if (!this.canTeleportTo(new BlockPos(x, y, z))) {
                return false;
            }
            this.troll.refreshPositionAndAngles((double) x + 0.5, y, (double) z + 0.5, this.troll.getYaw(), this.troll.getPitch());
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
    protected static class ReturnToGuardPosition extends Goal {

        private final AbstractTrollEntity troll;
        private final double speed;

        public ReturnToGuardPosition(AbstractTrollEntity troll, double speed) {
            this.troll = troll;
            this.speed = speed;
        }

        @Override
        public boolean canStart() {
            return troll.isWaiting() && troll.getTarget() == null
                    && !troll.isTrollBlocking()
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

        private List<ItemEntity> searchItemsList() {
            return
                    this.troll.getWorld().getEntitiesByClass(ItemEntity.class,
                            this.troll.getBoundingBox().expand(8.0, 2.0, 8.0),
                            item ->
                                    !item.cannotPickup() && item.isAlive()
                                            && ((item.getStack().getItem() == troll.getBarteringItem() && troll.isWandering())
                                            || troll.isEdible(item.getStack().getItem()) || troll.isAlcohol(item.getStack().getItem())));
        }

        public void startMovingTo(EntityNavigation navigation, int x, int y, int z, double speed) {
            navigation.startMovingAlong(navigation.findPathTo(x, y, z, 0), speed);
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

        if (this.isFollowing() || this.isWaiting()) {
            this.playSound(this.isWaiting() ? TCOTS_Sounds.TROLL_WAITING : TCOTS_Sounds.TROLL_FOLLOW, 1.0f, 1.0f);

            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR)).setBaseValue(
                    this.getType() == TCOTS_Entities.ROCK_TROLL? 16.0:
                    this.getType() == TCOTS_Entities.ICE_TROLL?  12.0:
                            4.0);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).setBaseValue(
                    this.getType() == TCOTS_Entities.ROCK_TROLL? 8.0:
                    this.getType() == TCOTS_Entities.ICE_TROLL?  6.0:
                            1.0);
        } else {
            this.playSound(TCOTS_Sounds.TROLL_DISMISS, 1.0f, 1.0f);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR)).setBaseValue(
                    this.getType() == TCOTS_Entities.ROCK_TROLL? 8.0:
                    this.getType() == TCOTS_Entities.ICE_TROLL?  6.0:
                            4.0);
            Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)).setBaseValue(
                    this.getType() == TCOTS_Entities.ROCK_TROLL? 4.0:
                    this.getType() == TCOTS_Entities.ICE_TROLL?  2.0:
                            1.0);
        }
    }

    public int getFollowerState() {
        return this.dataTracker.get(FOLLOWER_STATE);
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

    public void setGuardingPos(BlockPos pos) {
        this.dataTracker.set(GUARDING_POS, pos);
    }
    public BlockPos getGuardingPos() {
        return this.dataTracker.get(GUARDING_POS);
    }

    //Eating Code
    public void setEatingTime(int eatingTime) {
        this.dataTracker.set(EATING_TIME, eatingTime);
    }
    public int getEatingTime() {
        return this.dataTracker.get(EATING_TIME);
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

    @SuppressWarnings("all")
    private final int TOTAL_ADMIRING_TIME=60;
    @SuppressWarnings("all")
    private final int TOTAL_EATING_TIME=40;

    protected void tickEatingItem(){
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

    private void handleEndsFeed(PlayerEntity player, ItemStack foodStack){
        if(!this.getWorld().isClient) {
            if (foodStack.getUseAction() == UseAction.DRINK) {

                if(player!=null) {
                    ((ServerWorld) this.getWorld()).handleInteraction(this.getAlcoholInteraction(false), player, this);
                    this.handleNearTrollsInteraction(this.getAlcoholInteraction(true), player);
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
                    ((ServerWorld) this.getWorld()).handleInteraction(this.getFeedInteraction(false), player, this);
                    this.handleNearTrollsInteraction(this.getFeedInteraction(true), player);
                    this.setPersistent();
                }

                if(foodStack.getFoodComponent()!=null){
                    this.heal(foodStack.getFoodComponent().getHunger());
                }

                this.dropStack(foodStack.getItem() == Items.RABBIT_STEW? new ItemStack(Items.BOWL): ItemStack.EMPTY);
            }


            this.getWorld().sendEntityStatus(this,
                    player!=null && this.getFriendship(player)>this.getMinFriendshipToBeFollower()? EntityStatuses.ADD_VILLAGER_HEART_PARTICLES:
                            EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);

            this.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);

            this.setEatingTime(-1);
        }
    }

    protected void spawnItemParticles(ItemStack stack) {
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
                    .add(this.getX(), this.getY(), this.getZ());

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

    protected boolean hasFoodOrAlcohol(){
        return this.isEdible(this.getStackInHand(Hand.OFF_HAND).getItem()) || this.isDrinkable(this.getStackInHand(Hand.OFF_HAND).getItem());
    }

    protected boolean isEdible(Item item){
        return (item.isFood() && Objects.requireNonNull(item.getFoodComponent()).isMeat()) || item == Items.RABBIT_STEW;
    }

    protected boolean isDrinkable(Item item){
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

    protected boolean isAlcohol(Item item){
        return item instanceof WitcherAlcohol_Base;
    }

    @Override
    public void tick() {
        super.tick();

        this.decayGossip();

        this.tickAdmiringItem();

        this.tickEatingItem();

        if(!this.getWorld().isClient && !this.isAttacking() && this.age%600==0) {
            for (AbstractTrollEntity troll: getNearTrolls()){
                this.getGossip().shareGossipsWith(this, troll);
            }
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
        nbt.put("Gossips", this.getGossip().serialize(NbtOps.INSTANCE));
        nbt.putLong("LastGossipDecay", this.lastGossipDecayTime);

        nbt.putBoolean("IsRabid", this.isRabid());

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


        nbt.putBoolean("Blocking", this.isTrollBlocking());
        nbt.putInt("TimeBlocking", this.getTimeBlocking());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readAngerFromNbt(this.getWorld(), nbt);
        NbtList nbtList = nbt.getList("Gossips", NbtElement.COMPOUND_TYPE);
        this.getGossip().deserialize(new Dynamic<>(NbtOps.INSTANCE, nbtList));
        this.lastGossipDecayTime = nbt.getLong("LastGossipDecay");

        setIsRabid(nbt.getBoolean("IsRabid"));

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


        setIsTrollBlocking(nbt.getBoolean("Blocking"));
        setTimeBlocking(nbt.getInt("TimeBlocking"));
    }


    //Blocking System
    public void setIsTrollBlocking(boolean isBlocking) {
        this.dataTracker.set(BLOCKING, isBlocking);
        if(!isBlocking){
            this.triggerAnim("BlockController", "unblock");
        }
    }

    public boolean isTrollBlocking() {
        return this.dataTracker.get(BLOCKING);
    }

    public void setTimeBlocking(int timeBlocking) {
        this.dataTracker.set(TIME_BLOCKING, timeBlocking);
    }

    public int getTimeBlocking() {
        return this.dataTracker.get(TIME_BLOCKING);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return (this.isTrollBlocking()
                && !damageSource.isIn(DamageTypeTags.BYPASSES_SHIELD)
                && !damageSource.isIn(DamageTypeTags.BYPASSES_ARMOR)
                && !(damageSource.getSource() instanceof PersistentProjectileEntity && ((PersistentProjectileEntity) damageSource.getSource()).getPierceLevel() > 0)
                && !(this instanceof ForestTrollEntity))

                || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isTrollBlocking() && !this.isWaiting();
    }

    @Override
    public boolean isCollidable() {
        return this.isTrollBlocking() && this.isAlive();
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return !this.isPersistent();
    }

    //Sounds
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.TROLL_ATTACK;
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
    public int getMinAmbientSoundDelay() {
        return 120;
    }
}

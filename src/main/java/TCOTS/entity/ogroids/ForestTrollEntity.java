package TCOTS.entity.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.AttackOwnerAttackerTarget;
import TCOTS.entity.goals.AttackOwnerEnemyTarget;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public class ForestTrollEntity extends AbstractTrollEntity {
    //TODO: Change bartering loot table
    //TODO: Add drops
    //TODO: Add bestiary entry
    //TODO: Add spawning structures (Like little camps)
    //TODO: Add natural spawning
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ForestTrollEntity(EntityType<? extends AbstractTrollEntity> entityType, World world) {
        super(entityType, world);
    }

    public static final RawAnimation BLOCK = RawAnimation.begin().thenPlayAndHold("special.block");
    public static final RawAnimation UNBLOCK = RawAnimation.begin().thenPlay("special.unblock");

    protected static final TrackedData<Boolean> BAND_RIGHT_UP = DataTracker.registerData(ForestTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> BAND_LEFT_UP = DataTracker.registerData(ForestTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> CROWN = DataTracker.registerData(ForestTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> BARREL = DataTracker.registerData(ForestTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> NECK_BONE = DataTracker.registerData(ForestTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> CHARGING = DataTracker.registerData(ForestTrollEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> HOME_POS = DataTracker.registerData(ForestTrollEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);


    /**
     * Set the value of the boolean clothing
     *
     * @param select Select the clothing: BandRightUp (0), BandLeftUp (1), Crown (2), Barrel (3), NeckBone (4)
     * @param value  The value to set in the boolean
     */
    public void setClothing(boolean value, int select) {
        switch (select) {
            case 0:
                this.dataTracker.set(BAND_RIGHT_UP, value);
                break;
            case 1:
                this.dataTracker.set(BAND_LEFT_UP, value);
                break;
            case 2:
                this.dataTracker.set(CROWN, value);
                break;
            case 3:
                this.dataTracker.set(BARREL, value);
                break;
            case 4:
                this.dataTracker.set(NECK_BONE, value);
                break;
            default:
                break;
        }
    }

    /**
     * Get the value of the boolean clothing
     *
     * @param select Select the clothing: BandRightUp (0), BandLeftUp (1), Crown (2), Barrel (3), NeckBone (4)
     */
    public boolean getClothing(int select) {
        return switch (select) {
            case 0 -> this.dataTracker.get(BAND_RIGHT_UP);
            case 1 -> this.dataTracker.get(BAND_LEFT_UP);
            case 2 -> this.dataTracker.get(CROWN);
            case 3 -> this.dataTracker.get(BARREL);
            case 4 -> this.dataTracker.get(NECK_BONE);
            default -> false;
        };
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();

        this.dataTracker.startTracking(BAND_RIGHT_UP, Boolean.FALSE);
        this.dataTracker.startTracking(BAND_LEFT_UP, Boolean.FALSE);
        this.dataTracker.startTracking(CROWN, Boolean.FALSE);
        this.dataTracker.startTracking(BARREL, Boolean.FALSE);
        this.dataTracker.startTracking(NECK_BONE, Boolean.FALSE);

        this.dataTracker.startTracking(CHARGING, Boolean.FALSE);

        this.dataTracker.startTracking(HOME_POS, BlockPos.ORIGIN);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.2f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 1f);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (spawnReason == SpawnReason.NATURAL) {
            //1/5 probability to be a rabid troll if it's a natural spawn
            if (random.nextInt() % 10 == 0) {
                this.setIsRabid(true);
            }
        } else if (spawnReason != SpawnReason.STRUCTURE) {
            //1/20 probability to be a rabid troll
            if (random.nextInt() % 50 == 0) {
                this.setIsRabid(true);
            }

            this.setHomePos(this.getBlockPos());
        }

        //1/4 to appear with right band
        this.setClothing(random.nextBetween(0, 4) == 0, 0);

        //1/4 to appear with left band
        this.setClothing(random.nextBetween(0, 4) == 0, 1);

        //1/10 to appear with crown
        this.setClothing(random.nextBetween(0, 10) == 0, 2);

        //1/8 to appear with barrel
        this.setClothing(random.nextBetween(0, 8) == 0, 3);

        //1/8 that doesn't appear with the Neck Bone
        this.setClothing(!(random.nextBetween(0, 8) == 0), 4);

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new MeleeAttackGoal_ForestTroll(this, 1.2D, false, 1.4D, 80));

        this.goalSelector.add(2, new LookAtItemInHand(this));

        this.goalSelector.add(3, new GoForItemInGroundGoal(this, 1.2D));

        this.goalSelector.add(4, new TrollFollowFriendGoal(this, 1.2D, 5.0f, 2.0f, false));

        this.goalSelector.add(5, new ReturnToGuardPosition(this, 1.2D));

        this.goalSelector.add(6, new ReturnToHomePosition(this, 1.2D, 100));

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
                entity -> (
                        (entity instanceof AbstractTrollEntity troll && troll.isRabid()) ||

                                (entity instanceof Monster && !(entity instanceof AbstractTrollEntity) && !(entity instanceof CreeperEntity)))
                        && this.isWaiting()));

        this.targetSelector.add(3, new DefendFriendGoal(this, LivingEntity.class, false, true,
                entity ->
                        entity instanceof PlayerEntity player ?
                                !(this.getFriendship(player) > 80 && this.getReputation(player) > 100) :
                                (entity.getType() != this.getType()) || (entity instanceof AbstractTrollEntity troll && troll.isRabid())));

        this.targetSelector.add(4, new TrollTargetWithReputationGoal(this));
        this.targetSelector.add(5, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.add(7, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(8, new ActiveTargetGoal<>(this, RaiderEntity.class, true));
        this.targetSelector.add(9, new TrollUniversalAngerGoal<>(this, true));
    }

    protected static class ReturnToHomePosition extends Goal {

        private final ForestTrollEntity troll;
        private final double speed;
        private final double distanceBeforeReturning;

        public ReturnToHomePosition(ForestTrollEntity troll, double speed, double distanceBeforeReturning) {
            this.troll = troll;
            this.speed = speed;
            this.distanceBeforeReturning=distanceBeforeReturning;
        }

        @Override
        public boolean canStart() {
            return troll.isWaiting() && troll.getTarget() == null
                    && !troll.isTrollBlocking()

                    && troll.getSteppingPos().getSquaredDistance(
                    troll.getHomePos().getX(),
                    troll.getHomePos().getY(),
                    troll.getHomePos().getZ())

                    > distanceBeforeReturning
                    && searchItemsList().isEmpty()
                    && !(troll.hasFoodOrAlcohol() || troll.hasBarteringItem())

                    && troll.isWandering()
                    && troll.getHomePos()!= BlockPos.ORIGIN;
        }

        @Override
        public void start() {
            this.startMovingTo(troll.getNavigation(), troll.getHomePos().getX(), troll.getHomePos().getY(), troll.getHomePos().getZ(), speed);
        }

        @Override
        public void tick() {
            this.startMovingTo(troll.getNavigation(), troll.getHomePos().getX(), troll.getHomePos().getY(), troll.getHomePos().getZ(), speed);
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

    public void setHomePos(BlockPos pos) {
        this.dataTracker.set(HOME_POS, pos);
    }
    public BlockPos getHomePos() {
        return this.dataTracker.get(HOME_POS);
    }

    @Override
    public void setFollowerState(int followerState) {
        super.setFollowerState(followerState);

        if(this.getHomePos()!=BlockPos.ORIGIN){
            this.setHomePos(BlockPos.ORIGIN);
        }
    }

    private static class MeleeAttackGoal_ForestTroll extends MeleeAttackGoal_Animated {
        private final ForestTrollEntity troll;
        private final double speedMultiplierRunValue;
        private final int chargeCooldownTicks;


        private Path pathCharge;
        private double toChargeX;
        private double toChargeY;
        private double toChargeZ;

        public MeleeAttackGoal_ForestTroll(ForestTrollEntity mob, double speed, boolean pauseWhenMobIdle, double speedMultiplier, int chargeCooldown) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.troll = mob;
            this.speedMultiplierRunValue = speedMultiplier;
            this.chargeCooldownTicks = chargeCooldown;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !troll.isTrollBlocking();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !troll.isTrollBlocking();
        }

        @Override
        public void start() {
            super.start();

            this.troll.chargeCooldownTimer = 20;
            this.troll.chargeCooldown = true;
        }

        @Override
        public void stop() {
            super.stop();

            this.troll.setCharging(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.troll.getTarget();
            if (target == null) {
                return;
            }

            //Normal Attack
            if(!troll.isCharging()) {
                this.troll.getLookControl().lookAt(target, 30.0f, 30.0f);
                this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
                if ((this.pauseWhenMobIdle || this.troll.getVisibilityCache().canSee(target)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.troll.getRandom().nextFloat() < 0.05f)) {
                    this.targetX = target.getX();
                    this.targetY = target.getY();
                    this.targetZ = target.getZ();
                    this.updateCountdownTicks = 4 + this.troll.getRandom().nextInt(7);
                    double d = this.troll.squaredDistanceTo(target);
                    if (d > 1024.0) {
                        this.updateCountdownTicks += 10;
                    } else if (d > 256.0) {
                        this.updateCountdownTicks += 5;
                    }
                    if (!this.troll.getNavigation().startMovingTo(target, this.speed)) {
                        this.updateCountdownTicks += 15;
                    }
                    this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
                }
                this.cooldown = Math.max(this.cooldown - 1, 0);
                this.attack(target);
            }

            //Charging Attack
            {
                // Get the monster's position and facing direction
                Vec3d monsterPosition = this.troll.getPos();
                Vec3d monsterLookVec = this.troll.getRotationVector(0.0f, troll.getHeadYaw()); // This gives the direction the monster is facing
                boolean isFacingTarget = false;
                //To only active when it's facing directly to the player
                {
                    Vec3d targetPosition = target.getPos(); // Assuming 'target' is the player or another entity
                    Vec3d directionToTarget = targetPosition.subtract(monsterPosition).normalize(); // Normalize the direction

                    // Calculate the dot product between the two vectors
                    double dotProduct = monsterLookVec.dotProduct(directionToTarget);

                    // Set a threshold for the facing direction (1.0 means exactly the same direction)
                    double threshold = 0.95; // Adjust this threshold as needed

                    // Activate the boolean if the monster is facing towards the target
                    if (dotProduct > threshold) {
                        isFacingTarget = true;
                    }
                }

                //Start Charging
                if (this.troll.distanceTo(target) > 8 && !this.troll.chargeCooldown && !this.troll.isCharging() && isFacingTarget) {

                    this.troll.getLookControl().lookAt(
                            this.targetX,
                            this.targetY,
                            this.targetZ,
                            30.0f, 30.0f);

                    // Define how far you want the monster to charge (distance in blocks)
                    double chargeDistance = 20.0;
                    // Calculate the target position in front of the monster
                    Vec3d movingDirection = monsterPosition.add(monsterLookVec.multiply(chargeDistance, 0, chargeDistance));

                    this.toChargeX = movingDirection.x;
                    this.toChargeY = movingDirection.y;
                    this.toChargeZ = movingDirection.z;

                    this.pathCharge = this.troll.getNavigation().findPathTo(this.toChargeX, this.toChargeY, this.toChargeZ, 0);

                    if (this.pathCharge == null) {
                        return;
                    }

                    this.troll.setCharging(true);
                    this.troll.getLookControl().lookAt(
                            this.toChargeX,
                            this.toChargeY - 8,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.troll.playSound(TCOTS_Sounds.TROLL_FURIOUS, 1.0f, 1.0f);
                }

                //While it's charging
                if (troll.isCharging()) {
                    this.troll.getLookControl().lookAt(
                            this.toChargeX,
                            this.toChargeY - 8,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.troll.getNavigation().startMovingAlong(this.pathCharge, this.speed * speedMultiplierRunValue);


                    //If Bullvore reach the coordinates or something blocks the path
                    if (this.troll.squaredDistanceTo(this.toChargeX, this.toChargeY, this.toChargeZ) < 2 || this.troll.horizontalCollision) {

                        this.troll.setCharging(false);

                        this.troll.chargeCooldownTimer = this.chargeCooldownTicks;
                        this.troll.chargeCooldown = true;
                    }
                }
            }
        }
    }

    int chargeCooldownTimer;

    boolean chargeCooldown = false;

    public boolean isCharging() {
        return this.dataTracker.get(CHARGING);
    }

    public void setCharging(boolean wasCharging) {
        this.dataTracker.set(CHARGING, wasCharging);
    }

    @Override
    public void tick() {
        super.tick();

        if(chargeCooldownTimer>0){
            --chargeCooldownTimer;
        } else if (chargeCooldown){
            chargeCooldown=false;
        }

        if(this.isCharging()){
            EntitiesUtil.spawnGroundParticles(this);
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        //Destructive charge
        if (this.horizontalCollision && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            boolean bl = false;
            Box box = this.getBoundingBox().expand(0.2);
            for (BlockPos blockPos : BlockPos.iterate(MathHelper.floor(box.minX), MathHelper.floor(box.minY), MathHelper.floor(box.minZ), MathHelper.floor(box.maxX), MathHelper.floor(box.maxY), MathHelper.floor(box.maxZ))) {
                BlockState blockState = this.getWorld().getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!(block instanceof LeavesBlock)) continue;
                bl = this.getWorld().breakBlock(blockPos, true, this) || bl;
            }
        }
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        if(this.isCharging()){
            EntitiesUtil.pushAndDamageEntities(this, 10f, 1.1, 1.1, 1.2D, ForestTrollEntity.class);
        }
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !isCharging();
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        //If attacks using an axe
        if(isTrollBlocking() &&
                source.getAttacker()!=null && source.getAttacker() instanceof LivingEntity attacker
                && attacker.getMainHandStack().getItem() instanceof AxeItem
                && !source.isIn(DamageTypeTags.IS_PROJECTILE)){
            this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
            this.setIsTrollBlocking(false);
            this.setTimeBlocking(0);
        }

        if (this.isInvulnerableTo(source) && this.isTrollBlocking()) {
            this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0f, 1.0f);
        }

        boolean damage = super.damage(source, amount);

        //If isn't already blocking, is alive and has an attacker
        if(!this.getWorld().isClient && !isTrollBlocking() && this.isAlive() && this.getAttacker()!=null && !this.hasBarteringItem() && !this.hasFoodOrAlcohol()) {
            //If it has less than half its max life, the probability it's 1/5, else it's 1/10
            if(this.getHealth() <= this.getMaxHealth()/2){
                if(this.random.nextInt()%4==0){
                    this.setIsTrollBlocking(true);
                }
            } else {
                if(this.random.nextInt()%8==0){
                    this.setIsTrollBlocking(true);
                }
            }
        }

        return damage;
    }

    @Override
    protected int maxTicksBlocking() {
        return 60;
    }

    private final TrollGossips gossip = new TrollGossips();
    @Override
    public TrollGossips getGossip() {
        return gossip;
    }

    //Bad Actions
    public static final EntityInteraction TROLL_KILL = EntityInteraction.create("forest_troll_kill");
    public static final EntityInteraction TROLL_HURT = EntityInteraction.create("forest_troll_hurt");
    public static final EntityInteraction TROLL_HURT_FRIEND = EntityInteraction.create("forest_troll_hurt_friend");

    //Good Actions
    public static final EntityInteraction TROLL_DEFENDING = EntityInteraction.create("forest_troll_defending");
    public static final EntityInteraction TROLL_DEFENDING_FRIEND = EntityInteraction.create("forest_troll_defending_other");
    public static final EntityInteraction TROLL_ALCOHOL = EntityInteraction.create("forest_troll_alcohol");
    public static final EntityInteraction TROLL_ALCOHOL_FRIEND = EntityInteraction.create("forest_troll_alcohol_friend");
    public static final EntityInteraction TROLL_FED = EntityInteraction.create("forest_troll_fed");
    public static final EntityInteraction TROLL_FED_FRIEND = EntityInteraction.create("forest_troll_fed_friend");
    public static final EntityInteraction TROLL_BARTER = EntityInteraction.create("forest_troll_trade");
    public static final EntityInteraction TROLL_BARTER_FRIEND = EntityInteraction.create("forest_troll_fed_friend");

    @Override
    protected EntityInteraction getKillInteraction() {
        return TROLL_KILL;
    }

    @Override
    protected EntityInteraction getHurtInteraction(boolean isOther) {
        return isOther ? TROLL_HURT_FRIEND : TROLL_HURT;
    }

    @Override
    public EntityInteraction getDefendingInteraction(boolean isOther) {
        return isOther ? TROLL_DEFENDING_FRIEND : TROLL_DEFENDING;
    }

    @Override
    protected EntityInteraction getFeedInteraction(boolean isOther) {
        return isOther ? TROLL_FED_FRIEND : TROLL_FED;
    }

    @Override
    protected EntityInteraction getAlcoholInteraction(boolean isOther) {
        return isOther ? TROLL_ALCOHOL_FRIEND : TROLL_ALCOHOL;
    }

    @Override
    protected EntityInteraction getBarterInteraction(boolean isOther) {
        return isOther ? TROLL_BARTER_FRIEND : TROLL_BARTER;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        NbtCompound nbtClothing = new NbtCompound();

        nbtClothing.putBoolean("BandRU", this.getClothing(0));
        nbtClothing.putBoolean("BandLU", this.getClothing(1));
        nbtClothing.putBoolean("Crown",  this.getClothing(2));
        nbtClothing.putBoolean("Barrel", this.getClothing(3));
        nbtClothing.putBoolean("NeckBone", this.getClothing(4));

        nbt.put("Clothing", nbtClothing);

        nbt.putInt("ChargingCooldown", this.chargeCooldownTimer);
        nbt.putBoolean("Charging", isCharging());

        int x = nbt.getInt("HomePosX");
        int y = nbt.getInt("HomePosY");
        int z = nbt.getInt("HomePosZ");
        this.setHomePos(new BlockPos(x, y, z));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        NbtCompound nbtClothing = nbt.getCompound("Clothing");

        this.setClothing(nbtClothing.getBoolean("BandRU"), 0);
        this.setClothing(nbtClothing.getBoolean("BandLU"), 1);
        this.setClothing(nbtClothing.getBoolean("Crown"),  2);
        this.setClothing(nbtClothing.getBoolean("Barrel"), 3);
        this.setClothing(nbtClothing.getBoolean("NeckBone"), 4);

        this.chargeCooldownTimer = nbt.getInt("ChargingCooldown");
        setCharging(nbt.getBoolean("Charging"));

        nbt.putInt("HomePosX", this.getHomePos().getX());
        nbt.putInt("HomePosY", this.getHomePos().getY());
        nbt.putInt("HomePosZ", this.getHomePos().getZ());
    }

    @Override
    protected Identifier getTrollLootTable() {
        return new Identifier(TCOTS_Main.MOD_ID,"gameplay/forest_troll_bartering");
    }

    @Override
    protected Item getBarteringItem() {
        return Items.COPPER_INGOT;
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
        if(interaction == this.getDefendingInteraction(false)){
            //+30 Reputation
            //+50/20 Friendship (20 when they already your follower)
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.DEFENDING, 30, this.getOwner() == entity? 20: 50);
        } else if(interaction == this.getDefendingInteraction(true)){
            //+30 Reputation
            //+10 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.DEFENDING, 30, 10);
        } else if(interaction == this.getAlcoholInteraction(false)){
            //+10 Reputation
            //+20 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 20);
        } else if (interaction == this.getAlcoholInteraction(true)){
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 1);
        } else if(interaction == this.getFeedInteraction(false)){
            //+10 Reputation
            //+15 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 15);
        } else if (interaction == this.getFeedInteraction(true)) {
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 10, 1);
        } else if(interaction == this.getBarterInteraction(false)){
            //+5 Reputation
            //+2 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.BARTERING, 5, 2);
        } else if(interaction == this.getBarterInteraction(true)){
            //+5 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.BARTERING, 5, 1);
        }
        //Bad actions
        else if (interaction == this.getKillInteraction()) {
            //-80 Reputation
            //-50 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.KILL_TROLL, 80, 50);
        } else if (interaction == this.getHurtInteraction(false)) {
            //-25 Reputation
            //-10 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.HURT, 25, 10);
        } else if (interaction == this.getHurtInteraction(true)){
            //-10 Reputation
            //-5 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.HURT, 10, 5);
        }
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
                    if(this.isTrollBlocking()){
                        state.setAnimation(BLOCK);
                        return PlayState.CONTINUE;
                    } else {
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                }).triggerableAnim("unblock", UNBLOCK));

        //Give Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "GiveController", 1, state -> PlayState.STOP)
                        .triggerableAnim("give_item", GIVE_ITEM)
        );
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return (
                (this.isTrollBlocking()
                && blockedByArm(damageSource))
                && !damageSource.isIn(DamageTypeTags.BYPASSES_SHIELD)
                && !damageSource.isIn(DamageTypeTags.BYPASSES_ARMOR)
                && !(damageSource.getSource() instanceof PersistentProjectileEntity && ((PersistentProjectileEntity) damageSource.getSource()).getPierceLevel() > 0))

                || super.isInvulnerableTo(damageSource);
    }

    public boolean blockedByArm(DamageSource source) {
        Vec3d attackSourcePosVector;
        Entity entity = source.getSource();

        boolean hasPiercing = entity instanceof PersistentProjectileEntity && ((PersistentProjectileEntity) entity).getPierceLevel() > 0;

        if (!source.isIn(DamageTypeTags.BYPASSES_SHIELD) && !hasPiercing && (attackSourcePosVector = source.getPosition()) != null) {
            Vec3d rotationVector = this.getRotationVector(0.0f, this.getHeadYaw());
            Vec3d attackDirection = attackSourcePosVector.relativize(this.getPos());
            attackDirection = new Vec3d(attackDirection.x, 0.0, attackDirection.z).normalize();
            return attackDirection.dotProduct(rotationVector) < 0.0;
        }

        return false;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != StatusEffects.POISON && super.canHaveStatusEffect(effect);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

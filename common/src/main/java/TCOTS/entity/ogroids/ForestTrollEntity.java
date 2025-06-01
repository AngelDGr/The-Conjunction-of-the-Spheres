package TCOTS.entity.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Sounds;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.AttackOwnerAttackerTarget;
import TCOTS.entity.goals.AttackOwnerEnemyTarget;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class ForestTrollEntity extends AbstractTrollEntity {
    //xTODO: Change bartering loot table
    //xTODO: Add drops
    //xTODO: Add bestiary entry
    //xTODO: Add spawning structures (Like little camps)
    //xTODO: Add natural spawning
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ForestTrollEntity(EntityType<? extends AbstractTrollEntity> entityType, Level world) {
        super(entityType, world);
    }

    protected static final EntityDataAccessor<Boolean> BAND_RIGHT_UP = SynchedEntityData.defineId(ForestTrollEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> BAND_LEFT_UP = SynchedEntityData.defineId(ForestTrollEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> CROWN = SynchedEntityData.defineId(ForestTrollEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> BARREL = SynchedEntityData.defineId(ForestTrollEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> NECK_BONE = SynchedEntityData.defineId(ForestTrollEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(ForestTrollEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(ForestTrollEntity.class, EntityDataSerializers.BLOCK_POS);


    /**
     * Set the value of the boolean clothing
     *
     * @param select Select the clothing: BandRightUp (0), BandLeftUp (1), Crown (2), Barrel (3), NeckBone (4)
     * @param value  The value to set in the boolean
     */
    public void setClothing(boolean value, int select) {
        switch (select) {
            case 0:
                this.entityData.set(BAND_RIGHT_UP, value);
                break;
            case 1:
                this.entityData.set(BAND_LEFT_UP, value);
                break;
            case 2:
                this.entityData.set(CROWN, value);
                break;
            case 3:
                this.entityData.set(BARREL, value);
                break;
            case 4:
                this.entityData.set(NECK_BONE, value);
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
            case 0 -> this.entityData.get(BAND_RIGHT_UP);
            case 1 -> this.entityData.get(BAND_LEFT_UP);
            case 2 -> this.entityData.get(CROWN);
            case 3 -> this.entityData.get(BARREL);
            case 4 -> this.entityData.get(NECK_BONE);
            default -> false;
        };
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);

        builder.define(BAND_RIGHT_UP, Boolean.FALSE);
        builder.define(BAND_LEFT_UP, Boolean.FALSE);
        builder.define(CROWN, Boolean.FALSE);
        builder.define(BARREL, Boolean.FALSE);
        builder.define(NECK_BONE, Boolean.FALSE);

        builder.define(CHARGING, Boolean.FALSE);

        builder.define(HOME_POS, BlockPos.ZERO);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.STEP_HEIGHT, 1.0)

                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.24f)

                .add(Attributes.ATTACK_KNOCKBACK, 1.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8f)
                .add(Attributes.ARMOR, 4f)
                .add(Attributes.ARMOR_TOUGHNESS, 1f);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        if (spawnReason == MobSpawnType.NATURAL) {
            //1/20 probability to be a rabid troll if it's a natural spawn
            if (random.nextInt() % 20 == 0) {
                this.setIsRabid(true);
            }
        } else if (spawnReason != MobSpawnType.STRUCTURE) {
            //1/50 probability to be a rabid troll
            if (random.nextInt() % 50 == 0) {
                this.setIsRabid(true);
            }
        }

        if(spawnReason == MobSpawnType.STRUCTURE) this.setHomePos(this.blockPosition());

        //1/4 to appear with right band
        this.setClothing(random.nextIntBetweenInclusive(0, 4) == 0, 0);

        //1/4 to appear with left band
        this.setClothing(random.nextIntBetweenInclusive(0, 4) == 0, 1);

        //1/10 to appear with crown
        this.setClothing(random.nextIntBetweenInclusive(0, 10) == 0, 2);

        //1/8 to appear with barrel
        this.setClothing(random.nextIntBetweenInclusive(0, 8) == 0, 3);

        //1/8 that doesn't appear with the Neck Bone
        this.setClothing(!(random.nextIntBetweenInclusive(0, 8) == 0), 4);


        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new MeleeAttackGoal_ForestTroll(this, 1.2D, false, 1.4D, 80));

        this.goalSelector.addGoal(2, new LookAtItemInHand(this));

        this.goalSelector.addGoal(3, new GoForItemInGroundGoal(this, 1.2D));

        this.goalSelector.addGoal(4, new TrollFollowFriendGoal(this, 1.2D, 5.0f, 2.0f, false));

        this.goalSelector.addGoal(5, new ReturnToGuardPosition(this, 1.2D));

        this.goalSelector.addGoal(6, new ReturnToHomePosition(this, 0.75f, 100));

        this.goalSelector.addGoal(7, new LookAtPlayerWithWeaponGoal(this, Player.class, 10.0f));

        this.goalSelector.addGoal(8, new LookAtEntityGoal_Troll(this, Player.class, 8.0f));

        this.goalSelector.addGoal(9, new LookAtEntityGoal_Troll(this, Villager.class, 8.0f));

        this.goalSelector.addGoal(10, new LookAtEntityGoal_Troll(this, RockTrollEntity.class, 8.0f));

        this.goalSelector.addGoal(11, new WanderAroundGoal_Troll(this, 0.75f, 20));

        this.goalSelector.addGoal(12, new LookAroundGoal_Troll(this));

        //Objectives
        this.targetSelector.addGoal(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.addGoal(1, new AttackOwnerEnemyTarget(this));

        //Defending a place
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class,
                5, true, false,
                entity -> (
                        (entity instanceof AbstractTrollEntity troll && troll.isRabid()) ||

                                (entity instanceof Enemy && !(entity instanceof AbstractTrollEntity) && !(entity instanceof Creeper)))
                        && this.isWaiting()));

        this.targetSelector.addGoal(3, new DefendFriendGoal(this, LivingEntity.class, false, true,
                entity ->
                        entity instanceof Player player ?
                                !(this.getFriendship(player) > 80 && this.getReputation(player) > 100) :
                                (entity.getType() != this.getType()) || (entity instanceof AbstractTrollEntity troll && troll.isRabid())));

        this.targetSelector.addGoal(4, new TrollTargetWithReputationGoal(this));
        this.targetSelector.addGoal(5, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(9, new TrollUniversalAngerGoal<>(this, true));
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
        public boolean canUse() {
            return troll.getTarget() == null
                    && !troll.isTrollBlocking()

                    && troll.getOnPos().distToLowCornerSqr(
                    troll.getHomePos().getX(),
                    troll.getHomePos().getY(),
                    troll.getHomePos().getZ())
                    > distanceBeforeReturning

                    && searchItemsList().isEmpty()
                    && !(troll.hasFoodOrAlcohol() || troll.hasBarteringItem())

                    && troll.isWandering()
                    && troll.getHomePos()!= BlockPos.ZERO;
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
                    this.troll.level().getEntitiesOfClass(ItemEntity.class,
                            this.troll.getBoundingBox().inflate(8.0, 2.0, 8.0),
                            item ->
                                    !item.hasPickUpDelay() && item.isAlive()
                                            && ((item.getItem().getItem() == troll.getBarteringItem() && troll.isWandering())
                                            || troll.isEdible(item.getItem()) || troll.isAlcohol(item.getItem().getItem())));
        }

        public void startMovingTo(PathNavigation navigation, int x, int y, int z, double speed) {
            navigation.moveTo(navigation.createPath(x, y, z, 0), speed);
        }

    }

    public void setHomePos(BlockPos pos) {
        this.entityData.set(HOME_POS, pos);
    }
    public BlockPos getHomePos() {
        return this.entityData.get(HOME_POS);
    }

    @Override
    public void setFollowerState(int followerState) {
        super.setFollowerState(followerState);

        if(this.getHomePos()!=BlockPos.ZERO && !this.isWandering()){
            this.setHomePos(BlockPos.ZERO);
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
        public boolean canUse() {
            return super.canUse() && !troll.isTrollBlocking();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !troll.isTrollBlocking();
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

            this.troll.setIsCharging(false);
        }

        @Override
        public void tick() {
            LivingEntity target = this.troll.getTarget();
            if (target == null) {
                return;
            }

            //Normal Attack
            if(!troll.isCharging()) {
                this.troll.getLookControl().setLookAt(target, 30.0f, 30.0f);
                this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
                if ((this.pauseWhenMobIdle || this.troll.getSensing().hasLineOfSight(target)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.troll.getRandom().nextFloat() < 0.05f)) {
                    this.targetX = target.getX();
                    this.targetY = target.getY();
                    this.targetZ = target.getZ();
                    this.updateCountdownTicks = 4 + this.troll.getRandom().nextInt(7);
                    double d = this.troll.distanceToSqr(target);
                    if (d > 1024.0) {
                        this.updateCountdownTicks += 10;
                    } else if (d > 256.0) {
                        this.updateCountdownTicks += 5;
                    }
                    if (!this.troll.getNavigation().moveTo(target, this.speed)) {
                        this.updateCountdownTicks += 15;
                    }
                    this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
                }
                this.cooldown = Math.max(this.cooldown - 1, 0);
                this.attack(target);
            }

            //Charging Attack
            {
                // Get the monster's position and facing direction
                Vec3 monsterPosition = this.troll.position();
                Vec3 monsterLookVec = this.troll.calculateViewVector(0.0f, troll.getYHeadRot()); // This gives the direction the monster is facing
                boolean isFacingTarget = false;
                //To only active when it's facing directly to the player
                {
                    Vec3 targetPosition = target.position(); // Assuming 'target' is the player or another entity
                    Vec3 directionToTarget = targetPosition.subtract(monsterPosition).normalize(); // Normalize the direction

                    // Calculate the dot product between the two vectors
                    double dotProduct = monsterLookVec.dot(directionToTarget);

                    // Set a threshold for the facing direction (1.0 means exactly the same direction)
                    double threshold = 0.95; // Adjust this threshold as needed

                    // Activate the boolean if the monster is facing towards the target
                    if (dotProduct > threshold) {
                        isFacingTarget = true;
                    }
                }

                //Start Charging
                if (this.troll.distanceTo(target) > 8 && !this.troll.chargeCooldown && !this.troll.isCharging() && isFacingTarget) {

                    this.troll.getLookControl().setLookAt(
                            this.targetX,
                            this.targetY,
                            this.targetZ,
                            30.0f, 30.0f);

                    // Define how far you want the monster to charge (distance in blocks)
                    double chargeDistance = 20.0;
                    // Calculate the target position in front of the monster
                    Vec3 movingDirection = monsterPosition.add(monsterLookVec.multiply(chargeDistance, 0, chargeDistance));

                    this.toChargeX = movingDirection.x;
                    this.toChargeY = movingDirection.y;
                    this.toChargeZ = movingDirection.z;

                    this.pathCharge = this.troll.getNavigation().createPath(this.toChargeX, this.toChargeY, this.toChargeZ, 0);

                    if (this.pathCharge == null) {
                        return;
                    }

                    this.troll.setIsCharging(true);
                    this.troll.getLookControl().setLookAt(
                            this.toChargeX,
                            this.toChargeY - 8,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.troll.playSound(TCOTS_Sounds.getSoundEvent("troll_furious"), 1.0f, 1.0f);
                }

                //While it's charging
                if (troll.isCharging()) {
                    this.troll.getLookControl().setLookAt(
                            this.toChargeX,
                            this.toChargeY - 8,
                            this.toChargeZ,
                            30.0f, 30.0f);

                    this.troll.getNavigation().moveTo(this.pathCharge, this.speed * speedMultiplierRunValue);


                    //If Bullvore reach the coordinates or something blocks the path
                    if (this.troll.distanceToSqr(this.toChargeX, this.toChargeY, this.toChargeZ) < 2 || this.troll.horizontalCollision) {

                        this.troll.setIsCharging(false);

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
        return this.entityData.get(CHARGING);
    }

    public void setIsCharging(boolean wasCharging) {
        this.entityData.set(CHARGING, wasCharging);
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
    public void aiStep() {
        super.aiStep();

        //Destructive charge
        if (this.horizontalCollision && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            boolean bl = false;
            AABB box = this.getBoundingBox().inflate(0.2);
            for (BlockPos blockPos : BlockPos.betweenClosed(Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ), Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ))) {
                BlockState blockState = this.level().getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!(block instanceof LeavesBlock)) continue;
                bl = this.level().destroyBlock(blockPos, true, this) || bl;
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if(this.isCharging()){
            EntitiesUtil.pushAndDamageEntities(this, 10f, 1.1, 1.1, 1.2D, ForestTrollEntity.class);
        }

        if(this.getHealth()<this.getMaxHealth() && this.tickCount%60==0){
            this.heal(1);
        }
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !isCharging();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        //If attacks using an axe
        if(isTrollBlocking() &&
                source.getEntity()!=null && source.getEntity() instanceof LivingEntity attacker
                && attacker.getMainHandItem().getItem() instanceof AxeItem
                && !source.is(DamageTypeTags.IS_PROJECTILE)){
            this.playSound(SoundEvents.ITEM_BREAK, 1.0f, 1.0f);
            this.setIsTrollBlocking(false);
            this.setTimeBlocking(0);
        }

        if (this.isInvulnerableTo(source) && this.isTrollBlocking()) {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0f, 1.0f);
        }

        boolean damage = super.hurt(source, amount);

        //If isn't already blocking, is alive and has an attacker
        if(!this.level().isClientSide && !isTrollBlocking() && this.isAlive() && this.getLastHurtByMob()!=null && !this.hasBarteringItem() && !this.hasFoodOrAlcohol()) {
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
    public static final ReputationEventType TROLL_KILL = ReputationEventType.register("forest_troll_kill");
    public static final ReputationEventType TROLL_HURT = ReputationEventType.register("forest_troll_hurt");
    public static final ReputationEventType TROLL_HURT_FRIEND = ReputationEventType.register("forest_troll_hurt_friend");

    //Good Actions
    public static final ReputationEventType TROLL_DEFENDING = ReputationEventType.register("forest_troll_defending");
    public static final ReputationEventType TROLL_DEFENDING_FRIEND = ReputationEventType.register("forest_troll_defending_other");
    public static final ReputationEventType TROLL_ALCOHOL = ReputationEventType.register("forest_troll_alcohol");
    public static final ReputationEventType TROLL_ALCOHOL_FRIEND = ReputationEventType.register("forest_troll_alcohol_friend");
    public static final ReputationEventType TROLL_FED = ReputationEventType.register("forest_troll_fed");
    public static final ReputationEventType TROLL_FED_FRIEND = ReputationEventType.register("forest_troll_fed_friend");
    public static final ReputationEventType TROLL_BARTER = ReputationEventType.register("forest_troll_trade");
    public static final ReputationEventType TROLL_BARTER_FRIEND = ReputationEventType.register("forest_troll_fed_friend");

    @Override
    protected ReputationEventType getKillInteraction() {
        return TROLL_KILL;
    }

    @Override
    protected ReputationEventType getHurtInteraction(boolean isOther) {
        return isOther ? TROLL_HURT_FRIEND : TROLL_HURT;
    }

    @Override
    public ReputationEventType getDefendingInteraction(boolean isOther) {
        return isOther ? TROLL_DEFENDING_FRIEND : TROLL_DEFENDING;
    }

    @Override
    protected ReputationEventType getFeedInteraction(boolean isOther) {
        return isOther ? TROLL_FED_FRIEND : TROLL_FED;
    }

    @Override
    protected ReputationEventType getAlcoholInteraction(boolean isOther) {
        return isOther ? TROLL_ALCOHOL_FRIEND : TROLL_ALCOHOL;
    }

    @Override
    protected ReputationEventType getBarterInteraction(boolean isOther) {
        return isOther ? TROLL_BARTER_FRIEND : TROLL_BARTER;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        CompoundTag nbtClothing = new CompoundTag();

        nbtClothing.putBoolean("BandRU", this.getClothing(0));
        nbtClothing.putBoolean("BandLU", this.getClothing(1));
        nbtClothing.putBoolean("Crown",  this.getClothing(2));
        nbtClothing.putBoolean("Barrel", this.getClothing(3));
        nbtClothing.putBoolean("NeckBone", this.getClothing(4));

        nbt.put("Clothing", nbtClothing);

        nbt.putInt("ChargingCooldown", this.chargeCooldownTimer);
        nbt.putBoolean("Charging", isCharging());

        nbt.putInt("HomePosX", this.getHomePos().getX());
        nbt.putInt("HomePosY", this.getHomePos().getY());
        nbt.putInt("HomePosZ", this.getHomePos().getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        CompoundTag nbtClothing = nbt.getCompound("Clothing");

        this.setClothing(nbtClothing.getBoolean("BandRU"), 0);
        this.setClothing(nbtClothing.getBoolean("BandLU"), 1);
        this.setClothing(nbtClothing.getBoolean("Crown"),  2);
        this.setClothing(nbtClothing.getBoolean("Barrel"), 3);
        this.setClothing(nbtClothing.getBoolean("NeckBone"), 4);

        this.chargeCooldownTimer = nbt.getInt("ChargingCooldown");
        setIsCharging(nbt.getBoolean("Charging"));

        int x = nbt.getInt("HomePosX");
        int y = nbt.getInt("HomePosY");
        int z = nbt.getInt("HomePosZ");
        this.setHomePos(new BlockPos(x, y, z));
    }

    @Override
    protected ResourceKey<LootTable> getTrollLootTable() {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"gameplay/forest_troll_bartering"));
    }

    @Override
    protected Item getBarteringItem() {
        return Items.COPPER_INGOT;
    }

    @Override
    public void onReputationEventFrom(ReputationEventType interaction, Entity entity) {
        //So a Rabid troll isn't bother with reputation and friendship
        if(this.isRabid()){
            return;
        }

        if(entity==null){
            return;
        }

        //Good actions
        if(interaction == this.getDefendingInteraction(false)){
            //+40 Reputation
            //+60/30 Friendship (20 when they already your follower)
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.DEFENDING, 40, this.getOwner() == entity? 30: 60);
        } else if(interaction == this.getDefendingInteraction(true)){
            //+35 Reputation
            //+15 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.DEFENDING, 35, 15);
        } else if(interaction == this.getAlcoholInteraction(false)){
            //+15 Reputation
            //+40 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 15, 40);
        } else if (interaction == this.getAlcoholInteraction(true)){
            //+5 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 5, 1);
        } else if(interaction == this.getFeedInteraction(false)){
            //+15 Reputation
            //+20 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 15, 20);
        } else if (interaction == this.getFeedInteraction(true)) {
            //+5 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 5, 1);
        } else if(interaction == this.getBarterInteraction(false)){
            //+10 Reputation
            //+4 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.BARTERING, 10, 4);
        } else if(interaction == this.getBarterInteraction(true)){
            //+2 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.BARTERING, 2, 1);
        }
        //Bad actions
        else if (interaction == this.getKillInteraction()) {
            //-60 Reputation
            //-40 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.KILL_TROLL, 60, 40);
        } else if (interaction == this.getHurtInteraction(false)) {
            //-15 Reputation
            //-5 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.HURT, 15, 5);
        } else if (interaction == this.getHurtInteraction(true)){
            //-5 Reputation
            //-5 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.HURT, 5, 5);
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
                && !damageSource.is(DamageTypeTags.BYPASSES_SHIELD)
                && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR)
                && !(damageSource.getDirectEntity() instanceof AbstractArrow && ((AbstractArrow) damageSource.getDirectEntity()).getPierceLevel() > 0))

                || super.isInvulnerableTo(damageSource);
    }

    public boolean blockedByArm(DamageSource source) {
        Vec3 attackSourcePosVector;
        Entity entity = source.getDirectEntity();

        boolean hasPiercing = entity instanceof AbstractArrow && ((AbstractArrow) entity).getPierceLevel() > 0;

        if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && !hasPiercing && (attackSourcePosVector = source.getSourcePosition()) != null) {
            Vec3 rotationVector = this.calculateViewVector(0.0f, this.getYHeadRot());
            Vec3 attackDirection = attackSourcePosVector.vectorTo(this.position());
            attackDirection = new Vec3(attackDirection.x, 0.0, attackDirection.z).normalize();
            return attackDirection.dot(rotationVector) < 0.0;
        }

        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

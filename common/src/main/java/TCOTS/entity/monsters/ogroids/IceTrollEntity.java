package TCOTS.entity.monsters.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Sounds;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.AttackOwnerAttackerTarget;
import TCOTS.entity.goals.AttackOwnerEnemyTarget;
import TCOTS.entity.misc.Troll_RockProjectileEntity;
import TCOTS.registry.TCOTS_Effects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class IceTrollEntity extends RockTrollEntity {
    //xTODO: Add drops
    //xTODO: Add bestiary entry
    //xTODO: Add ice caves to spawn in the world
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public IceTrollEntity(final EntityType<? extends AbstractTrollEntity> entityType, final Level world) {
        super(entityType, world);
        this.xpReward=10;
    }
    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.STEP_HEIGHT, 1.0)

                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.22f)

                .add(Attributes.ATTACK_KNOCKBACK, 1.5f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0f)
                .add(Attributes.ARMOR, 6f)
                .add(Attributes.ARMOR_TOUGHNESS, 2f);
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new ProjectileAttackGoal_RockTroll(this, 1.2D, 30, 10.0f, 40f));

        this.goalSelector.addGoal(2, new MeleeAttackGoal_Troll(this, 1.2D, false));

        this.goalSelector.addGoal(3, new LookAtItemInHand(this));

        this.goalSelector.addGoal(4, new GoForItemInGroundGoal(this, 1.2D));

        this.goalSelector.addGoal(5, new TrollFollowFriendGoal(this, 1.2D, 5.0f, 2.0f, false));

        this.goalSelector.addGoal(6, new ReturnToGuardPosition(this, 1.2D));

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
                        (entity instanceof final AbstractTrollEntity troll && troll.isRabid()) ||

                                (entity instanceof Enemy && !(entity instanceof AbstractTrollEntity) && !(entity instanceof Creeper)))
                        && this.isWaiting()));

        this.targetSelector.addGoal(3, new DefendFriendGoal(this, LivingEntity.class, false, true, entity ->
                entity instanceof final Player player?
                        !(this.getFriendship(player) > 160 && this.getReputation(player) > 200):
                        (entity.getType()!=this.getType()) || (entity instanceof final AbstractTrollEntity troll && troll.isRabid())));

        this.targetSelector.addGoal(4, new TrollTargetWithReputationGoal(this));
        this.targetSelector.addGoal(5, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(9, new TrollUniversalAngerGoal<>(this, true));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(final ServerLevelAccessor world, final DifficultyInstance difficulty, final MobSpawnType spawnReason, @Nullable final SpawnGroupData entityData) {
        if(spawnReason == MobSpawnType.NATURAL){
            //1/2 probability to be a rabid troll if it's a natural spawn
            if(random.nextInt()%2==0){
                this.setIsRabid(true);
            }
        }
        else if(spawnReason != MobSpawnType.STRUCTURE){
            //1/5 probability to be a rabid troll
            if(random.nextInt()%5==0){
                this.setIsRabid(true);
            }
        }

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void performRangedAttack(@NotNull final LivingEntity target, final float pullProgress) {
        this.getNavigation().stop();
        final Troll_RockProjectileEntity rockProjectileEntity = new Troll_RockProjectileEntity(this.level(), this, this.isSnowing()? 8: 6);
        final double d = target.getEyeY() - (double)1.1f;
        final double e = target.getX() - this.getX();
        final double f = d - rockProjectileEntity.getY();
        final double g = target.getZ() - this.getZ();
        final double h = Math.sqrt(e * e + g * g) * (double)0.2f;
        rockProjectileEntity.shoot(e, f + h, g, 2.2f, this.isWandering()? 4.0f : 1.5f);
        this.triggerAnim("RockController", "rock_attack");
        this.playSound(TCOTS_Sounds.getSoundEvent("rock_projectile_throws"), 1.0f, 0.4f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity(rockProjectileEntity);
    }

    private final TrollGossips gossip = new TrollGossips();

    @Override
    public TrollGossips getGossip() {
        return gossip;
    }
    //Bad Actions
    public static final ReputationEventType TROLL_KILL = ReputationEventType.register("ice_troll_kill");
    public static final ReputationEventType TROLL_HURT = ReputationEventType.register("ice_troll_hurt");
    public static final ReputationEventType TROLL_HURT_FRIEND = ReputationEventType.register("ice_troll_hurt_friend");

    //Good Actions
    public static final ReputationEventType TROLL_DEFENDING = ReputationEventType.register("ice_troll_defending");
    public static final ReputationEventType TROLL_DEFENDING_FRIEND = ReputationEventType.register("ice_troll_defending_other");
    public static final ReputationEventType TROLL_ALCOHOL = ReputationEventType.register("ice_troll_alcohol");
    public static final ReputationEventType TROLL_ALCOHOL_FRIEND = ReputationEventType.register("ice_troll_alcohol_friend");
    public static final ReputationEventType TROLL_FED = ReputationEventType.register("ice_troll_fed");
    public static final ReputationEventType TROLL_FED_FRIEND = ReputationEventType.register("ice_troll_fed_friend");
    public static final ReputationEventType TROLL_BARTER = ReputationEventType.register("ice_troll_trade");
    public static final ReputationEventType TROLL_BARTER_FRIEND = ReputationEventType.register("ice_troll_fed_friend");

    @Override
    protected ReputationEventType getKillInteraction() {
        return TROLL_KILL;
    }

    @Override
    protected ReputationEventType getHurtInteraction(final boolean isOther) {
        return isOther ? TROLL_HURT_FRIEND : TROLL_HURT;
    }

    @Override
    public ReputationEventType getDefendingInteraction(final boolean isOther) {
        return isOther ? TROLL_DEFENDING_FRIEND : TROLL_DEFENDING;
    }

    @Override
    protected ReputationEventType getFeedInteraction(final boolean isOther) {
        return isOther ? TROLL_FED_FRIEND : TROLL_FED;
    }

    @Override
    protected ReputationEventType getAlcoholInteraction(final boolean isOther) {
        return isOther ? TROLL_ALCOHOL_FRIEND : TROLL_ALCOHOL;
    }

    @Override
    protected ReputationEventType getBarterInteraction(final boolean isOther) {
        return isOther ? TROLL_BARTER_FRIEND : TROLL_BARTER;
    }

    @Override
    protected Item getBarteringItem() {
        return Items.LAPIS_LAZULI;
    }

    //xTODO: Modify this Loot Table
    @Override
    protected ResourceKey<LootTable> getTrollLootTable() {
        return TCOTS_Entities.ICE_TROLL_BARTERING;
    }

    @Override
    public int getMinFriendshipToBeFollower() {
        return 250;
    }

    @Override
    public void onReputationEventFrom(final ReputationEventType interaction, final Entity entity) {
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
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.DEFENDING, 20, this.getOwner() == entity? 5: 30);
        } else if(interaction == this.getDefendingInteraction(true)){
            //+30 Reputation
            //+10 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.DEFENDING, 15, 5);
        } else if(interaction == this.getAlcoholInteraction(false)){
            //+10 Reputation
            //+20 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 5, 12);
        } else if (interaction == this.getAlcoholInteraction(true)){
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 5, 1);
        } else if(interaction == this.getFeedInteraction(false)){
            //+10 Reputation
            //+15 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 5, 10);
        } else if (interaction == this.getFeedInteraction(true)) {
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 5, 1);
        } else if(interaction == this.getBarterInteraction(false)){
            //+5 Reputation
            //+2 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.BARTERING, 1, 1);
        } else if(interaction == this.getBarterInteraction(true)){
            //+5 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.BARTERING, 5, 1);
        }
        //Bad actions
        else if (interaction == this.getKillInteraction()) {
            //-80 Reputation
            //-50 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.KILL_TROLL, 100, 80);
        } else if (interaction == this.getHurtInteraction(false)) {
            //-25 Reputation
            //-10 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.HURT, 40, 20);
        } else if (interaction == this.getHurtInteraction(true)){
            //-10 Reputation
            //-5 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.HURT, 25, 15);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(this.isSnowing()){
            this.spawnSnowflakesAround();
        }
    }

    @Override
    public boolean isInvulnerableTo(final DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource);
    }

    protected void spawnSnowflakesAround(){
        if(this.tickCount%6 == 0){
            for (int i = 0; i < 8; ++i) {
                final double d = this.getX() + (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
                final double e = (this.getEyeY()-0.5f)+ (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
                final double f = this.getZ() + (double) Mth.randomBetween(this.getRandom(), -1F, 1F);
                this.level().addParticle(ParticleTypes.SNOWFLAKE, d,e,f,0,0,0);
            }
        }
    }
    private static final AttributeModifier BLIZZARD_STRENGTH_BOOST = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"troll_blizzard_strength_boost"),
            4.0f,
            AttributeModifier.Operation.ADD_VALUE);

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        //If it's snowing increases its strength
        if(this.isSnowing()){
            final AttributeInstance entityAttributeInstance = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) {
                entityAttributeInstance.removeModifier(BLIZZARD_STRENGTH_BOOST.id());
                entityAttributeInstance.addTransientModifier(BLIZZARD_STRENGTH_BOOST);
            }
        } else {
            final AttributeInstance entityAttributeInstance = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(BLIZZARD_STRENGTH_BOOST.id());
        }
    }
    private boolean isSnowing(){
        final Biome biome = this.level().getBiome(this.blockPosition()).value();
        return this.level().isRaining() && biome.shouldSnow(this.level(), this.blockPosition());
    }

    @Override
    public boolean canBeAffected(final MobEffectInstance effect) {
        return effect.getEffect() != TCOTS_Effects.NorthernWindEffect() && super.canBeAffected(effect);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

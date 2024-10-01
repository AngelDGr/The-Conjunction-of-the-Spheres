package TCOTS.entity.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.AttackOwnerAttackerTarget;
import TCOTS.entity.goals.AttackOwnerEnemyTarget;
import TCOTS.entity.misc.Troll_RockProjectileEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.UUID;

public class IceTrollEntity extends RockTrollEntity {
    //xTODO: Add drops
    //xTODO: Add bestiary entry
    //xTODO: Add ice caves to spawn in the world
    // Continue with the generation testing
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public IceTrollEntity(EntityType<? extends AbstractTrollEntity> entityType, World world) {
        super(entityType, world);
    }
    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0f)
                .add(EntityAttributes.GENERIC_ARMOR, 6f)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2f);
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
                entity -> entity instanceof Monster && !(entity instanceof CreeperEntity) && (entity.getType() != this.getType()) && this.isWaiting()));

        this.targetSelector.add(3, new DefendFriendGoal(this, LivingEntity.class, false, true, entity ->
                entity instanceof PlayerEntity player?
                        !(this.getFriendship(player) > 160 && this.getReputation(player) > 200):
                        (entity.getType()!=this.getType()) || (entity instanceof AbstractTrollEntity troll && troll.isRabid())));

        this.targetSelector.add(4, new RockTrollTargetWithReputationGoal(this));
        this.targetSelector.add(5, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.add(7, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(8, new ActiveTargetGoal<>(this, RaiderEntity.class, true));
        this.targetSelector.add(9, new TrollUniversalAngerGoal<>(this, true));
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if(spawnReason == SpawnReason.NATURAL){
            //1/2 probability to be a rabid troll if it's a natural spawn
            if(random.nextInt()%2==0){
                this.setIsRabid(true);
            }
        }
        else if(spawnReason != SpawnReason.STRUCTURE){
            //1/5 probability to be a rabid troll
            if(random.nextInt()%5==0){
                this.setIsRabid(true);
            }
        }

        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void shootAt(@NotNull LivingEntity target, float pullProgress) {
        this.getNavigation().stop();
        Troll_RockProjectileEntity rockProjectileEntity = new Troll_RockProjectileEntity(this.getWorld(), this, this.isSnowing()? 8: 6);
        double d = target.getEyeY() - (double)1.1f;
        double e = target.getX() - this.getX();
        double f = d - rockProjectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(e * e + g * g) * (double)0.2f;
        rockProjectileEntity.setVelocity(e, f + h, g, 2.2f, this.isWandering()? 4.0f : 1.5f);
        this.triggerAnim("RockController", "rock_attack");
        this.playSound(TCOTS_Sounds.ROCK_PROJECTILE_THROWS, 1.0f, 0.4f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.getWorld().spawnEntity(rockProjectileEntity);
    }

    private final TrollGossips gossip = new TrollGossips();

    @Override
    public TrollGossips getGossip() {
        return gossip;
    }
    //Bad Actions
    public static final EntityInteraction TROLL_KILL = EntityInteraction.create("ice_troll_kill");
    public static final EntityInteraction TROLL_HURT = EntityInteraction.create("ice_troll_hurt");
    public static final EntityInteraction TROLL_HURT_FRIEND = EntityInteraction.create("ice_troll_hurt_friend");

    //Good Actions
    public static final EntityInteraction TROLL_DEFENDING = EntityInteraction.create("ice_troll_defending");
    public static final EntityInteraction TROLL_DEFENDING_FRIEND = EntityInteraction.create("ice_troll_defending_other");
    public static final EntityInteraction TROLL_ALCOHOL = EntityInteraction.create("ice_troll_alcohol");
    public static final EntityInteraction TROLL_ALCOHOL_FRIEND = EntityInteraction.create("ice_troll_alcohol_friend");
    public static final EntityInteraction TROLL_FED = EntityInteraction.create("ice_troll_fed");
    public static final EntityInteraction TROLL_FED_FRIEND = EntityInteraction.create("ice_troll_fed_friend");
    public static final EntityInteraction TROLL_BARTER = EntityInteraction.create("ice_troll_trade");
    public static final EntityInteraction TROLL_BARTER_FRIEND = EntityInteraction.create("ice_troll_fed_friend");

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
    protected Item getBarteringItem() {
        return Items.LAPIS_LAZULI;
    }

    //xTODO: Modify this Loot Table
    @Override
    protected Identifier getTrollLootTable() {
        return new Identifier(TCOTS_Main.MOD_ID,"gameplay/ice_troll_bartering");
    }

    @Override
    protected int getMinFriendshipToBeFollower() {
        return 250;
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
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.DEFENDING, 20, this.getOwner() == entity? 5: 30);
        } else if(interaction == this.getDefendingInteraction(true)){
            //+30 Reputation
            //+10 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.DEFENDING, 15, 5);
        } else if(interaction == this.getAlcoholInteraction(false)){
            //+10 Reputation
            //+20 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 5, 12);
        } else if (interaction == this.getAlcoholInteraction(true)){
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 5, 1);
        } else if(interaction == this.getFeedInteraction(false)){
            //+10 Reputation
            //+15 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 5, 10);
        } else if (interaction == this.getFeedInteraction(true)) {
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.FEEDING, 5, 1);
        } else if(interaction == this.getBarterInteraction(false)){
            //+5 Reputation
            //+2 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.BARTERING, 1, 1);
        } else if(interaction == this.getBarterInteraction(true)){
            //+5 Reputation
            //+1 Friendship
            this.gossip.startGossip(entity.getUuid(), TrollGossips.TrollGossipType.BARTERING, 5, 1);
        }
        //Bad actions
        else if (interaction == this.getKillInteraction()) {
            //-80 Reputation
            //-50 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.KILL_TROLL, 100, 80);
        } else if (interaction == this.getHurtInteraction(false)) {
            //-25 Reputation
            //-10 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.HURT, 40, 20);
        } else if (interaction == this.getHurtInteraction(true)){
            //-10 Reputation
            //-5 Friendship
            this.getGossip().startGossip(entity.getUuid(), TrollGossips.TrollGossipType.HURT, 25, 15);
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
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource);
    }

    protected void spawnSnowflakesAround(){
        if(this.age%6 == 0){
            for (int i = 0; i < 8; ++i) {
                double d = this.getX() + (double) MathHelper.nextBetween(this.getRandom(), -1F, 1F);
                double e = (this.getEyeY()-0.5f)+ (double) MathHelper.nextBetween(this.getRandom(), -1F, 1F);
                double f = this.getZ() + (double) MathHelper.nextBetween(this.getRandom(), -1F, 1F);
                this.getWorld().addParticle(ParticleTypes.SNOWFLAKE, d,e,f,0,0,0);
            }
        }
    }

    private static final UUID BLIZZARD_STRENGTH_BOOST_ID = UUID.fromString("6120c998-43c2-4e54-a337-8b4a95ce81de");
    private static final EntityAttributeModifier BLIZZARD_STRENGTH_BOOST = new EntityAttributeModifier(BLIZZARD_STRENGTH_BOOST_ID, "Blizzard strength boost", 4.0f, EntityAttributeModifier.Operation.ADDITION);

    @Override
    protected void mobTick() {
        super.mobTick();

        //If it's snowing increases its strength
        if(this.isSnowing()){
            EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) {
                entityAttributeInstance.removeModifier(BLIZZARD_STRENGTH_BOOST.getId());
                entityAttributeInstance.addTemporaryModifier(BLIZZARD_STRENGTH_BOOST);
            }
        } else {
            EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(BLIZZARD_STRENGTH_BOOST.getId());
        }
    }
    private boolean isSnowing(){
        Biome biome = this.getWorld().getBiome(this.getBlockPos()).value();
        return this.getWorld().isRaining() && biome.canSetSnow(this.getWorld(), this.getBlockPos());
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

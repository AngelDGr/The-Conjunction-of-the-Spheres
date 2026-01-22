package mors.tcots.entity.monsters.ogroids;

import mors.tcots.client.geo.animation.entity.ogroid.RockTrollAnimations;
import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.entity.TrollGossips;
import mors.tcots.entity.goal.AttackOwnerAttackerTarget;
import mors.tcots.entity.goal.AttackOwnerEnemyTarget;
import mors.tcots.entity.misc.Troll_RockProjectileEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RockTrollEntity extends AbstractTrollEntity implements RangedAttackMob {
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
    //xTODO: Add possible village-like structure
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public RockTrollEntity(final EntityType<? extends AbstractTrollEntity> entityType, final Level world) {
        super(entityType, world);
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

        this.targetSelector.addGoal(3, new DefendFriendGoal(this, LivingEntity.class, false, true,
                entity ->
                entity instanceof final Player player?
                !(this.getFriendship(player) > 80 && this.getReputation(player) > 100):
                        (entity.getType()!=this.getType()) || (entity instanceof final AbstractTrollEntity troll && troll.isRabid())));

        this.targetSelector.addGoal(4, new TrollTargetWithReputationGoal(this));
        this.targetSelector.addGoal(5, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(9, new TrollUniversalAngerGoal<>(this, true));
    }

    @Override
    public void performRangedAttack(@NotNull final LivingEntity target, final float pullProgress) {
        this.getNavigation().stop();
        final Troll_RockProjectileEntity rockProjectileEntity = new Troll_RockProjectileEntity(this.level(), this, 8);
        final double d = target.getEyeY() - (double)1.1f;
        final double e = target.getX() - this.getX();
        final double f = d - rockProjectileEntity.getY();
        final double g = target.getZ() - this.getZ();
        final double h = Math.sqrt(e * e + g * g) * (double)0.2f;
        rockProjectileEntity.shoot(e, f + h, g, 1.8f, this.isWandering()? 4.0f : 1.5f);
        this.triggerAnim("base_controller", "rock_attack");
        this.playSound(TCOTS_Sounds.getSoundEvent("rock_projectile_throws"), 1.0f, 0.4f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity(rockProjectileEntity);

    }

    @Override
    public boolean hurt(@NotNull final DamageSource source, final float amount) {
        if(blockedByBack(source)
                &&
                !(source.getEntity()!=null && source.getEntity() instanceof final LivingEntity attacker && attacker.getMainHandItem().getItem() instanceof PickaxeItem))
        {
            this.playSound(TCOTS_Sounds.getSoundEvent("troll_block_impact"), 1.0f, 1.0f);
            return false;
        }

        //If attacks using a pickaxe
        if(isTrollBlocking() &&
                source.getEntity()!=null && source.getEntity() instanceof final LivingEntity attacker
                && attacker.getMainHandItem().getItem() instanceof PickaxeItem
                && !source.is(DamageTypeTags.IS_PROJECTILE)){
            this.playSound(TCOTS_Sounds.getSoundEvent("troll_block_impact_break"), 1.0f, 1.0f);
            this.setIsTrollBlocking(false);
            this.setTimeBlocking(0);
        }

        if (this.isInvulnerableTo(source) && this.isTrollBlocking()) {
            this.playSound(TCOTS_Sounds.getSoundEvent("troll_block_impact"), 1.0f, 1.0f);
        }

        final boolean damage = super.hurt(source, amount);

        //If isn't already blocking, is alive and has an attacker
        if(!this.level().isClientSide && !isTrollBlocking() && this.isAlive() && this.getLastHurtByMob()!=null && !this.hasBarteringItem() && !this.hasFoodOrAlcohol()) {
            //If it has less than half its max life, the probability it's 1/5, else it's 1/10
            if(this.getHealth() <= this.getMaxHealth()/2){
                if(this.random.nextInt()%5==0){
                    this.setIsTrollBlocking(true);
                }
            } else {
                if(this.random.nextInt()%10==0){
                    this.setIsTrollBlocking(true);
                }
            }
        }

        return damage;
    }

    public boolean blockedByBack(final DamageSource source) {
        final Vec3 vec3d;
        final Entity entity = source.getDirectEntity();

        final boolean hasPiercing = entity instanceof AbstractArrow && ((AbstractArrow) entity).getPierceLevel() > 0;


        if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && !hasPiercing && (vec3d = source.getSourcePosition()) != null) {
            final Vec3 vec3d2 = this.calculateViewVector(0.0f, this.getYHeadRot());
            Vec3 vec3d3 = vec3d.vectorTo(this.position());
            vec3d3 = new Vec3(vec3d3.x, 0.0, vec3d3.z).normalize();return vec3d3.dot(vec3d2) > 0.0;
        }

        return false;
    }
    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.STEP_HEIGHT, 1.0)

                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.22f)

                .add(Attributes.ATTACK_KNOCKBACK, 1.5f)
                .add(Attributes.ARMOR, 8f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0f)
                .add(Attributes.ARMOR_TOUGHNESS, 4f);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull final ServerLevelAccessor world, @NotNull final DifficultyInstance difficulty, @NotNull final MobSpawnType spawnReason, @Nullable final SpawnGroupData entityData) {
        if(spawnReason == MobSpawnType.NATURAL){
            //1/5 probability to be a rabid troll if it's a natural spawn
            if(random.nextInt()%5==0){
                this.setIsRabid(true);
            }
        }
        else if(spawnReason != MobSpawnType.STRUCTURE){
            //1/20 probability to be a rabid troll
            if(random.nextInt()%20==0){
                this.setIsRabid(true);
            }
        }

        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    //Reputation System
    private final TrollGossips gossip = new TrollGossips();

    @Override
    public TrollGossips getGossip() {
        return gossip;
    }
    @Override
    public void onReputationEventFrom(@NotNull final ReputationEventType interaction, @NotNull final Entity entity) {
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
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.DEFENDING, 30, this.getOwner() == entity? 20: 50);
        } else if(interaction == this.getDefendingInteraction(true)){
            //+30 Reputation
            //+10 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.DEFENDING, 30, 10);
        } else if(interaction == this.getAlcoholInteraction(false)){
            //+10 Reputation
            //+20 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 10, 20);
        } else if (interaction == this.getAlcoholInteraction(true)){
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 10, 1);
        } else if(interaction == this.getFeedInteraction(false)){
            //+10 Reputation
            //+15 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 10, 15);
        } else if (interaction == this.getFeedInteraction(true)) {
            //+10 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.FEEDING, 10, 1);
        } else if(interaction == this.getBarterInteraction(false)){
            //+5 Reputation
            //+2 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.BARTERING, 5, 2);
        } else if(interaction == this.getBarterInteraction(true)){
            //+5 Reputation
            //+1 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.BARTERING, 5, 1);
        }
        //Bad actions
        else if (interaction == this.getKillInteraction()) {
            //-80 Reputation
            //-50 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.KILL_TROLL, 80, 50);
        } else if (interaction == this.getHurtInteraction(false)) {
            //-25 Reputation
            //-10 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.HURT, 25, 10);
        } else if (interaction == this.getHurtInteraction(true)){
            //-10 Reputation
            //-5 Friendship
            this.getGossip().startGossip(entity.getUUID(), TrollGossips.TrollGossipType.HURT, 10, 5);
        }
    }

    //Bad Actions
    public static final ReputationEventType TROLL_KILL = ReputationEventType.register("rock_troll_kill");
    public static final ReputationEventType TROLL_HURT = ReputationEventType.register("rock_troll_hurt");
    public static final ReputationEventType TROLL_HURT_FRIEND = ReputationEventType.register("rock_troll_hurt_friend");

    //Good Actions
    public static final ReputationEventType TROLL_DEFENDING = ReputationEventType.register("rock_troll_defending");
    public static final ReputationEventType TROLL_DEFENDING_FRIEND = ReputationEventType.register("rock_troll_defending_other");
    public static final ReputationEventType TROLL_ALCOHOL = ReputationEventType.register("rock_troll_alcohol");
    public static final ReputationEventType TROLL_ALCOHOL_FRIEND = ReputationEventType.register("rock_troll_alcohol_friend");
    public static final ReputationEventType TROLL_FED = ReputationEventType.register("rock_troll_fed");
    public static final ReputationEventType TROLL_FED_FRIEND = ReputationEventType.register("rock_troll_fed_friend");
    public static final ReputationEventType TROLL_BARTER = ReputationEventType.register("rock_troll_trade");
    public static final ReputationEventType TROLL_BARTER_FRIEND = ReputationEventType.register("rock_troll_fed_friend");

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
    protected Item getBarteringItem(){
        return Items.AMETHYST_SHARD;
    }

    @Override
    protected ResourceKey<LootTable> getTrollLootTable(){
        return TCOTS_Entities.ROCK_TROLL_BARTERING;
    }


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(RockTrollAnimations.mainController(this));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

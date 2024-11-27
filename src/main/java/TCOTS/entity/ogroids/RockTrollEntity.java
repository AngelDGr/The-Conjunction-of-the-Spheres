package TCOTS.entity.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TrollGossips;
import TCOTS.entity.goals.AttackOwnerAttackerTarget;
import TCOTS.entity.goals.AttackOwnerEnemyTarget;
import TCOTS.entity.misc.Troll_RockProjectileEntity;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
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

    public static final RawAnimation ATTACK_THROW_ROCK = RawAnimation.begin().thenPlay("attack.rock_throw");


    public RockTrollEntity(EntityType<? extends AbstractTrollEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new ProjectileAttackGoal_RockTroll(this, 1.2D, 30, 10.0f, 40f));

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
                entity -> (
                        (entity instanceof AbstractTrollEntity troll && troll.isRabid()) ||

                                (entity instanceof Monster && !(entity instanceof AbstractTrollEntity) && !(entity instanceof CreeperEntity)))
                        && this.isWaiting()));

        this.targetSelector.add(3, new DefendFriendGoal(this, LivingEntity.class, false, true,
                entity ->
                entity instanceof PlayerEntity player?
                !(this.getFriendship(player) > 80 && this.getReputation(player) > 100):
                        (entity.getType()!=this.getType()) || (entity instanceof AbstractTrollEntity troll && troll.isRabid())));

        this.targetSelector.add(4, new TrollTargetWithReputationGoal(this));
        this.targetSelector.add(5, new TrollRevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAtPlayer));
        this.targetSelector.add(7, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(8, new ActiveTargetGoal<>(this, RaiderEntity.class, true));
        this.targetSelector.add(9, new TrollUniversalAngerGoal<>(this, true));
    }

    @Override
    public void shootAt(@NotNull LivingEntity target, float pullProgress) {
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

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(blockedByBack(source)
                &&
                !(source.getAttacker()!=null && source.getAttacker() instanceof LivingEntity attacker && attacker.getMainHandStack().getItem() instanceof PickaxeItem))
        {
            this.playSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT, 1.0f, 1.0f);
            return false;
        }

        //If attacks using a pickaxe
        if(isTrollBlocking() &&
                source.getAttacker()!=null && source.getAttacker() instanceof LivingEntity attacker
                && attacker.getMainHandStack().getItem() instanceof PickaxeItem
                && !source.isIn(DamageTypeTags.IS_PROJECTILE)){
            this.playSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT_BREAK, 1.0f, 1.0f);
            this.setIsTrollBlocking(false);
            this.setTimeBlocking(0);
        }

        if (this.isInvulnerableTo(source) && this.isTrollBlocking()) {
            this.playSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT, 1.0f, 1.0f);
        }

        boolean damage = super.damage(source, amount);

        //If isn't already blocking, is alive and has an attacker
        if(!this.getWorld().isClient && !isTrollBlocking() && this.isAlive() && this.getAttacker()!=null && !this.hasBarteringItem() && !this.hasFoodOrAlcohol()) {
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

    public boolean blockedByBack(DamageSource source) {
        Vec3d vec3d;
        Entity entity = source.getSource();

        boolean hasPiercing = entity instanceof PersistentProjectileEntity && ((PersistentProjectileEntity) entity).getPierceLevel() > 0;


        if (!source.isIn(DamageTypeTags.BYPASSES_SHIELD) && !hasPiercing && (vec3d = source.getPosition()) != null) {
            Vec3d vec3d2 = this.getRotationVector(0.0f, this.getHeadYaw());
            Vec3d vec3d3 = vec3d.relativize(this.getPos());
            vec3d3 = new Vec3d(vec3d3.x, 0.0, vec3d3.z).normalize();return vec3d3.dotProduct(vec3d2) > 0.0;
        }

        return false;
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

    //Reputation System
    private final TrollGossips gossip = new TrollGossips();

    @Override
    public TrollGossips getGossip() {
        return gossip;
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

    //Bad Actions
    public static final EntityInteraction TROLL_KILL = EntityInteraction.create("rock_troll_kill");
    public static final EntityInteraction TROLL_HURT = EntityInteraction.create("rock_troll_hurt");
    public static final EntityInteraction TROLL_HURT_FRIEND = EntityInteraction.create("rock_troll_hurt_friend");

    //Good Actions
    public static final EntityInteraction TROLL_DEFENDING = EntityInteraction.create("rock_troll_defending");
    public static final EntityInteraction TROLL_DEFENDING_FRIEND = EntityInteraction.create("rock_troll_defending_other");
    public static final EntityInteraction TROLL_ALCOHOL = EntityInteraction.create("rock_troll_alcohol");
    public static final EntityInteraction TROLL_ALCOHOL_FRIEND = EntityInteraction.create("rock_troll_alcohol_friend");
    public static final EntityInteraction TROLL_FED = EntityInteraction.create("rock_troll_fed");
    public static final EntityInteraction TROLL_FED_FRIEND = EntityInteraction.create("rock_troll_fed_friend");
    public static final EntityInteraction TROLL_BARTER = EntityInteraction.create("rock_troll_trade");
    public static final EntityInteraction TROLL_BARTER_FRIEND = EntityInteraction.create("rock_troll_fed_friend");

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
    protected Item getBarteringItem(){
        return Items.AMETHYST_SHARD;
    }

    @Override
    protected Identifier getTrollLootTable(){
        return new Identifier(TCOTS_Main.MOD_ID,"gameplay/rock_troll_bartering");
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


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

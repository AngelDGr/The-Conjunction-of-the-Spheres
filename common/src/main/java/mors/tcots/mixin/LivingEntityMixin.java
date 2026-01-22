package mors.tcots.mixin;

import mors.tcots.TCOTS_Main;
import mors.tcots.registry.*;
import mors.tcots.entity.misc.AnchorProjectileEntity;
import mors.tcots.entity.monsters.ogroids.AbstractTrollEntity;
import mors.tcots.interfaces.LivingEntityMixinInterface;
import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.items.components.MonsterOilComponent;
import mors.tcots.items.concoctions.bombs.MoonDustBomb;
import mors.tcots.items.concoctions.bombs.NorthernWindBomb;
import mors.tcots.items.concoctions.bombs.SamumBomb;
import mors.tcots.utils.TCOTS_EntitiesUtil;
import mors.tcots.utils.TCOTS_Util;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, LivingEntityMixinInterface {

    public LivingEntityMixin(final EntityType<?> type, final Level world) {
        super(type, world);
    }
    @SuppressWarnings("all")
    @Unique
    LivingEntity tcots$THIS = (LivingEntity)(Object)this;

    @Shadow public abstract void kill();

    @Shadow public abstract boolean hurt(@NotNull DamageSource source, float amount);

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract float getHealth();

    @Shadow public abstract @Nullable LivingEntity getLastHurtMob();

    @Shadow public abstract @Nullable LivingEntity getLastHurtByMob();

    //Killer Whale
    @ModifyVariable(method = "decreaseAirSupply", at = @At("STORE"))
    private double tcots$checkForKillerWhaleEffect(double d){
        if(this.hasEffect(TCOTS_Effects.KillerWhaleEffect())){
            d = 2;
        }

        return d;
    }

    //Kill Counter
    @Unique
    private static final EntityDataAccessor<Integer> KILL_COUNT = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> KILL_COUNTDOWN = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.INT);


    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void tcots$injectKillCountDataTracker(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(KILL_COUNT, 0);
        builder.define(KILL_COUNTDOWN, 0);
    }

    @Override
    public int tcots$getKillCount() {
        return this.entityData.get(KILL_COUNT);
    }

    @Override
    public void tcots$setKillCount(final int killCount) {
        this.entityData.set(KILL_COUNT, killCount);
    }

    @Override
    public int tcots$getKillCountdown() {
        return this.entityData.get(KILL_COUNTDOWN);
    }

    @Override
    public void tcots$setKillCountdown(final int killCountdown) {
        this.entityData.set(KILL_COUNTDOWN, killCountdown);
    }

    @Override
    public void tcots$incrementKillCount() {
        final int count = this.tcots$getKillCount();
        this.tcots$setKillCount(count + 1);
    }


    @Inject(method = "setLastHurtMob", at = @At("HEAD"))
    private void tcots$injectCountdownAttack(final CallbackInfo ci){
        tcots$THIS.tcots$setKillCountdown(300);
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void tcots$injectCountdown(final CallbackInfo ci){
        if(tcots$THIS.hasEffect(TCOTS_Effects.GraveHagDecoctionEffect())) {
            final int count = tcots$THIS.tcots$getKillCountdown();
            if (tcots$THIS.tcots$getKillCountdown() > 0) {

                tcots$THIS.tcots$setKillCountdown(count - 1);

            } else if (tcots$THIS.tcots$getKillCountdown() == 0) {
                tcots$THIS.tcots$setKillCount(0);
            }
        } else{
            tcots$THIS.tcots$setKillCountdown(0);
            tcots$THIS.tcots$setKillCount(0);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void tcots$injectKillCounter(final DamageSource damageSource, final CallbackInfo ci){
        final LivingEntity livingEntity = tcots$THIS.getKillCredit();
        if (livingEntity != null) {
            if(livingEntity.hasEffect(TCOTS_Effects.GraveHagDecoctionEffect())){

                final int killCount =  livingEntity.tcots$getKillCount();

                if(killCount < 20){
                    livingEntity.tcots$incrementKillCount();
                    livingEntity.tcots$setKillCountdown(420);
                }

            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void tcots$killCountWriteNBT(final CompoundTag nbt, final CallbackInfo ci){
        nbt.putInt("KillCount", tcots$THIS.tcots$getKillCount());
        nbt.putInt("KillCountdown", tcots$THIS.tcots$getKillCountdown());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void tcots$killCountReadNBT(final CompoundTag nbt, final CallbackInfo ci){
        tcots$THIS.tcots$setKillCount(nbt.getInt("KillCount"));
        tcots$THIS.tcots$setKillCountdown(nbt.getInt("KillCountdown"));
    }

    //Foglet Decoction
    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float tcots$foggyResistance(final float amount){
        if(this.hasEffect(TCOTS_Effects.FogletDecoctionEffect())
                && this.level() instanceof ServerLevel
                && (this.level().isRaining() || this.level().isThundering()))
        {
            return amount/2;
        }

        return amount;
    }


    //Black Blood
    @Inject(method = "hurt", at = @At("TAIL"))
    private void tcots$blackBloodDamage(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir){
        if(this.hasEffect(TCOTS_Effects.BlackBloodEffect())){
            //To reflect damage
            final int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.BlackBloodEffect())).getAmplifier();
            final float damageMultiplier = switch (amplifier) {
                case 0 ->  0.15f;
                case 1 ->  0.20f;
                default -> 0.30f;
            };

            if(source.getEntity() != null && source.getEntity() instanceof final LivingEntity attackerBlack &&
                    !((source.getDirectEntity() instanceof Projectile) || (source.getDirectEntity() instanceof AbstractArrow))){
                //Damage
                if(amount > 0 && (TCOTS_EntitiesUtil.isNecrophage(attackerBlack) || TCOTS_EntitiesUtil.isVampire(attackerBlack))){
                    attackerBlack.hurt(attackerBlack.damageSources().magic(), amount*damageMultiplier);
                }

                //For Knockback above level 0
                final double d = this.getX() - attackerBlack.getX();
                final double e = this.getZ() - attackerBlack.getZ();
                if(amplifier > 0){
                    attackerBlack.knockback(amplifier*0.5f, d, e);
                }
            }

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), TCOTS_Sounds.getSoundEvent("black_blood_hit"), this.getSoundSource(),1f,1f);

            this.level().broadcastEntityEvent(tcots$THIS, BLACK_BLOOD_PARTICLES);
        }
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void tcots$blackBloodBleedingEffect(final CallbackInfo ci){
        if(this.hasEffect(TCOTS_Effects.BlackBloodEffect())) {
            final int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.BlackBloodEffect())).getAmplifier();
            if(amplifier> 1){
                final List<LivingEntity> list= this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5,2,5),
                livingEntity -> TCOTS_EntitiesUtil.isNecrophage(livingEntity) || TCOTS_EntitiesUtil.isVampire(livingEntity));
                //To apply bleeding effect to near mobs
                if(!list.isEmpty()) {
                    list.forEach(livingEntity -> {
                        if(!(livingEntity.hasEffect(TCOTS_Effects.BleedingBlackBloodEffect()))){
                            livingEntity.addEffect(new MobEffectInstance(TCOTS_Effects.BleedingBlackBloodEffect(), 60, 0, true, false, false));
                        }
                    });
                }
            }
        }
    }

    //SamumEffect
    @Inject(method = "hurt", at = @At("TAIL"))
    private void tcots$removeSamumOnHit(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(tcots$THIS)){
            this.removeEffect(TCOTS_Effects.SamumEffect());
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void tcots$immunityToStun(final MobEffectInstance effect, final CallbackInfoReturnable<Boolean> cir){
        if(((tcots$THIS instanceof Warden) || (tcots$THIS instanceof Guardian)) && (effect.getEffect()== TCOTS_Effects.SamumEffect()))
            cir.setReturnValue(false);
    }

    //NorthernWind
    @Unique
    private static final EntityDataAccessor<Boolean> IS_FROZEN = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.BOOLEAN);

    @Override
    public boolean tcots$isFrozen() {
        return this.entityData.get(IS_FROZEN);
    }

    @Unique
    public void tcots$setIsFrozen(final boolean frozen) {
        this.entityData.set(IS_FROZEN, frozen);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void tcots$northernWindDataTracker(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(IS_FROZEN, false);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void tcots$removeNorthernWindOnHit(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir){
        if(NorthernWindBomb.checkEffect(tcots$THIS)){
            this.playSound(SoundEvents.GLASS_BREAK,1,1);
            this.removeEffect(TCOTS_Effects.NorthernWindEffect());
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float tcots$extraDamageNorthernWind(final float amount){
        if(NorthernWindBomb.checkEffect(tcots$THIS)){
            final int amplifier= Objects.requireNonNull(this.getEffect(TCOTS_Effects.NorthernWindEffect())).getAmplifier();
            final int randomN=this.random.nextIntBetweenInclusive(0,10);
            //Instant kill chance or extra damage
            if(this.getMaxHealth() <= 100 && amplifier>1 && randomN==0){
                return this.getHealth();
            } else if (amplifier>1 && randomN==0){
                return amount + 20;
            }
            return amount + (1+amplifier);
        }
        return amount;
    }

    @ModifyVariable(method = "knockback", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double tcots$extraKnockbackNorthernWind(final double strength){
        if(NorthernWindBomb.checkEffect(tcots$THIS)){
            return strength * 1.8;
        }
        return strength;
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void tcots$northernWindNoPushable(final CallbackInfoReturnable<Boolean> cir){
        if(NorthernWindBomb.checkEffect(tcots$THIS)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void tcots$isFrozen(final CallbackInfo ci){
        if(!tcots$THIS.level().isClientSide) {
            tcots$setIsFrozen(NorthernWindBomb.checkEffect(tcots$THIS));
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void tcots$immunityToFreeze(final MobEffectInstance effect, final CallbackInfoReturnable<Boolean> cir){
        if(tcots$THIS.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) && (effect.getEffect()== TCOTS_Effects.NorthernWindEffect()))
            cir.setReturnValue(false);
    }

    //Moon Dust
    @Unique
    private static final EntityDataAccessor<Boolean> SILVER_SPLINTERS = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.BOOLEAN);

    @Override
    public boolean tcots$hasSilverSplinters() {
        return this.entityData.get(SILVER_SPLINTERS);
    }

    @Unique
    public void tcots$setSilverSplinters(final boolean frozen) {
        this.entityData.set(SILVER_SPLINTERS, frozen);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void tcots$moonDustDataTracker(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(SILVER_SPLINTERS, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void tcots$silverSplintersWriteNBT(final CompoundTag nbt, final CallbackInfo ci){
        nbt.putBoolean("SilverSplinters", tcots$THIS.tcots$hasSilverSplinters());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void tcots$silverSplintersReadNBT(final CompoundTag nbt, final CallbackInfo ci){
        this.tcots$setSilverSplinters(nbt.getBoolean("SilverSplinters"));
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void tcots$hasSilverSplinters(final CallbackInfo ci){
        if(!tcots$THIS.level().isClientSide) {
            if (MoonDustBomb.checkOnlyEffect(tcots$THIS)){
                final int amplifier = Objects.requireNonNull(tcots$THIS.getEffect(TCOTS_Effects.MoonDustEffect())).getAmplifier();
                if(amplifier>1){
                    tcots$setSilverSplinters(true);
                }
            }
        }
    }

    //Immunity to bleeding
    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void tcots$immunityToBleeding(final MobEffectInstance effect, final CallbackInfoReturnable<Boolean> cir){
        if((
                (tcots$THIS instanceof AbstractSkeleton)
                || TCOTS_EntitiesUtil.isElementa(tcots$THIS)
                || (tcots$THIS instanceof WitherBoss)
                || TCOTS_EntitiesUtil.isSpecter(tcots$THIS))

                && (effect.getEffect()== TCOTS_Effects.Bleeding() || effect.getEffect()== TCOTS_Effects.BleedingBlackBloodEffect()))
            cir.setReturnValue(false);
    }

    //G'valchir damage
    @Unique
    private boolean tcots$attackerHasGvalchir =false;
    @Inject(method ="getDamageAfterArmorAbsorb", at = @At("HEAD"))
    private void tcots$getAttackerGvalchirBoolean(final DamageSource source, final float amount, final CallbackInfoReturnable<Float> cir){
        if(source.getEntity() instanceof final LivingEntity livingEntity){
            this.tcots$attackerHasGvalchir =
                    //Has the G'valchir in hand
                    livingEntity.getMainHandItem().getItem() == TCOTS_Items.GVALCHIR.get() &&
                    //To avoid ignore armor with attacks with projectiles
                    !((source.getDirectEntity() instanceof Projectile) || (source.getDirectEntity() instanceof AbstractArrow))
                    //To avoid ignore armor when there's thorns damage
                    && !(source.typeHolder() == DamageTypes.MAGIC);
        } else {
            tcots$attackerHasGvalchir = false;
        }
    }

    @ModifyArgs(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"))
    private void tcots$armorPenetration(final Args args){
        if(tcots$attackerHasGvalchir){
            final float armor = args.get(3);
            final float armorToughness = args.get(4);

            args.set(3, armor*(1- TCOTS_Util.gvalchir_penetration));
            args.set(4, armorToughness*0.50f);
        }
    }

    //Nekker Warrior Decoction
    @Unique
    private boolean tcots$passengerHasDecoction =false;
    @Unique
    private static final AttributeModifier PASSENGER_SPEED_BOOST = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"passenger_speed_boost"),
            0.5f,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @Inject(method = "tick", at = @At("HEAD"))
    private void tcots$extraNekkerWarriorSpeed(final CallbackInfo ci){
        if(tcots$THIS.isVehicle()){
            final Entity passenger = tcots$THIS.getControllingPassenger();

            if(passenger instanceof final LivingEntity livingPassenger){
                if(livingPassenger.hasEffect(TCOTS_Effects.NekkerWarriorDecoctionEffect())){
                    tcots$passengerHasDecoction = true;
                    final AttributeInstance entityAttributeInstance = tcots$THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                    if(entityAttributeInstance!=null) {
                        entityAttributeInstance.removeModifier(PASSENGER_SPEED_BOOST.id());
                        entityAttributeInstance.addTransientModifier(PASSENGER_SPEED_BOOST);
                    }
                } else if(tcots$passengerHasDecoction){
                    final AttributeInstance entityAttributeInstance = tcots$THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                    if(entityAttributeInstance!=null) {
                        entityAttributeInstance.removeModifier(PASSENGER_SPEED_BOOST.id());
                        tcots$passengerHasDecoction = false;
                    }
                }
            }
        }

    }

    //Troll Reputation
    @Inject(method = "die", at = @At("TAIL"))
    private void tcots$trollTriggerDefending(final DamageSource damageSource, final CallbackInfo ci){

        if(this.level() instanceof ServerLevel && getLastHurtMob() instanceof final AbstractTrollEntity troll && getLastHurtByMob() instanceof final Player player){
            if(!this.level().isClientSide && !troll.isRabid()) {
                ((ServerLevel) this.level()).onReputationEvent(troll.getDefendingInteraction(false), player, troll);
                troll.handleNearTrollsInteraction(troll.getDefendingInteraction(true), player);
                troll.level().broadcastEntityEvent(troll, troll.getFriendship(player)>troll.getMinFriendshipToBeFollower()? EntityEvent.LOVE_HEARTS: EntityEvent.VILLAGER_HAPPY);
                troll.handleNearTrollsParticles(EntityEvent.VILLAGER_HAPPY);
            }
        }
    }

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void tcots$golemNotAttackFriendlyIceTroll(final LivingEntity target, final CallbackInfoReturnable<Boolean> cir){
        if(tcots$THIS.getType() == EntityType.IRON_GOLEM){
            if(target instanceof final AbstractTrollEntity troll && !troll.isRabid()){
                cir.setReturnValue(false);
            }
        }
    }


    //Trigger Advancement
    @Inject(method = "die", at = @At("TAIL"))
    private void tcots$injectTriggerAdvancement(final DamageSource damageSource, final CallbackInfo ci){
        if(TCOTS_EntitiesUtil.isHumanoid(tcots$THIS) && getLastHurtByMob() instanceof final Player player){
            if(player.getMainHandItem().has(TCOTS_Items.MonsterOilComponent()) && player.getMainHandItem().get(TCOTS_Items.MonsterOilComponent())!=null){
                final MonsterOilComponent monsterOil = player.getMainHandItem().get(TCOTS_Items.MonsterOilComponent());

                if(monsterOil!=null && monsterOil.groupId()==11)
                    if(player instanceof final ServerPlayer serverPlayer)
                        TCOTS_Criteria.KillWithHanged().trigger(serverPlayer);
            }
        }
    }


    @Shadow public abstract ItemStack getMainHandItem();

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> effect);

    @Shadow public abstract boolean removeEffect(Holder<MobEffect> effect);

    @Shadow public abstract Collection<MobEffectInstance> getActiveEffects();

    @Inject(method ="getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void tcots$monsterResistance(final DamageSource source, final float amount, final CallbackInfoReturnable<Float> cir){
        if(source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)){
            cir.setReturnValue(amount);
        }

        final var resistanceAttribute = tcots$THIS.getAttribute(TCOTS_EntityAttributes.RESISTANCE_AGAINST_MONSTERS);

        double resistance = 1;
        if (resistanceAttribute != null) resistance = resistanceAttribute.getValue();

        if(source.getEntity()!=null
                && source.getEntity() instanceof final LivingEntity attacker && TCOTS_EntitiesUtil.isMonster(attacker)){
            final double resistedDamage= amount * (resistance-1);

            cir.setReturnValue(Math.max(0, (float) (amount - resistedDamage)));
        }
    }

    @Inject(method = "handleEquipmentChanges", at = @At("TAIL"))
    private void tcots$applyArmorSetBonuses(final Map<EquipmentSlot, ItemStack> map, final CallbackInfo ci) {
        ArmorSet.recalculate(tcots$THIS);
    }

    @ModifyVariable(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z",
            at = @At("HEAD"),
            argsOnly = true
    )
    private MobEffectInstance tcots$manticoreExtendPotionDuration(final MobEffectInstance effect) {
        if (ArmorSet.MANTICORE.hasFullBonus(tcots$THIS) && effect.getEffect().value().isBeneficial()) {

            return new MobEffectInstance(
                    effect.getEffect(),
                    (int) (effect.getDuration() * 1.50),
                    effect.getAmplifier(),
                    effect.isAmbient(),
                    effect.isVisible(),
                    effect.showIcon(),
                    effect.hiddenEffect
            );
        }

        return effect;
    }

    //Winter's Blade
    @Inject(method =  "tick", at= @At("HEAD"))
    private void tcots$injectWintersBladeExtinguish(final CallbackInfo ci){
        if(tcots$THIS.isOnFire()){
            if(tcots$THIS.getMainHandItem().is(TCOTS_Items.WINTERS_BLADE.get()) || tcots$THIS.getOffhandItem().is(TCOTS_Items.WINTERS_BLADE.get())) tcots$THIS.clearFire();
        }
    }

    //Tundra Horse Armor
    @Unique
    private static final AttributeModifier TUNDRA_ARMOR_BONUS = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "tundra_armor_speed_boost"),
            0.2f,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void tcots$injectSnowSpeed(final CallbackInfo ci){
        if(tcots$THIS instanceof final Horse horse){
            if(horse.getBodyArmorItem().is(TCOTS_Items.TUNDRA_HORSE_ARMOR.get()) &&
                    (this.tcots$isSteepingOrInside(horse, Blocks.POWDER_SNOW)
                            || this.tcots$isSteepingOrInside(horse, Blocks.SNOW_BLOCK) || this.tcots$isSteepingOrInside(horse, Blocks.SNOW) ||
                            this.tcots$isSteepingOrInside(horse, Blocks.ICE) || this.tcots$isSteepingOrInside(horse, Blocks.BLUE_ICE) || this.tcots$isSteepingOrInside(horse, Blocks.PACKED_ICE) || this.tcots$isSteepingOrInside(horse, Blocks.FROSTED_ICE)
                    )){
                final AttributeInstance entityAttributeInstance = tcots$THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) {
                    entityAttributeInstance.removeModifier(TUNDRA_ARMOR_BONUS.id());
                    entityAttributeInstance.addTransientModifier(TUNDRA_ARMOR_BONUS);}
            } else {
                final AttributeInstance entityAttributeInstance = tcots$THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(TUNDRA_ARMOR_BONUS.id());
            }
        }

    }

    @Unique
    private boolean tcots$isSteepingOrInside(final LivingEntity entity, final Block block){
        return entity.getBlockStateOn().is(block) || entity.level().getBlockState(entity.blockPosition()).is(block);
    }

    @Inject(method ="getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void tcots$knightHorseArmorResistance(final DamageSource source, final float amount, final CallbackInfoReturnable<Float> cir){
        if(tcots$THIS instanceof final Horse horse && horse.getBodyArmorItem().is(TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR.get())){
            if(source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)){
                cir.setReturnValue(amount);
            }

            if(source.is(DamageTypeTags.IS_PROJECTILE)){
                cir.setReturnValue(amount*0.60f);
            }

            if(source.is(DamageTypeTags.IS_EXPLOSION)){
                cir.setReturnValue(amount*0.80f);
            }
        }
    }

    //Giant Anchor
    @Unique
    @Nullable
    public AnchorProjectileEntity tcots$anchorProjectile;
    @Override
    public AnchorProjectileEntity tcots$getAnchor(){
        return tcots$anchorProjectile;
    }

    @Override
    public void tcots$setAnchor(final Object anchor) {
        this.tcots$anchorProjectile = (AnchorProjectileEntity) anchor;
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void injectDiscardAnchor(final CallbackInfo ci){
        if(!this.level().isClientSide && this.tcots$getAnchor()!=null){
            this.tcots$getAnchor().setOwner(null);
        }
    }

    //Bleeding effect
    @Unique
    private static final byte BLOOD_PARTICLES = 72;

    @Unique
    private static final byte BLACK_BLOOD_PARTICLES = 73;
    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void tcots$injectParticles(final byte status, final CallbackInfo ci){
        if(status == BLOOD_PARTICLES){
            tcots$spawnBloodParticles(tcots$THIS, TCOTS_Particles.FallingBloodParticle());
        }

        if(status == BLACK_BLOOD_PARTICLES){
            tcots$spawnBloodParticles(tcots$THIS, TCOTS_Particles.FallingBlackBloodParticle());
        }
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void tcots$injectInDamage(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir){
        if(source.is(TCOTS_DamageTypes.BLEEDING) && !this.hasEffect(TCOTS_Effects.BlackBloodEffect())){
            this.level().broadcastEntityEvent(tcots$THIS,
                    tcots$THIS.hasEffect(TCOTS_Effects.BleedingBlackBloodEffect())? BLACK_BLOOD_PARTICLES :BLOOD_PARTICLES);
        }
    }

    @Unique
    protected void tcots$spawnBloodParticles(final LivingEntity entity, final SimpleParticleType particle){
        for(int i=0; i<10; i++){
            final double d = entity.getX() + (double) Mth.randomBetween(entity.getRandom(),
                    (float)-entity.getBoundingBox().getXsize()/2,
                    (float) entity.getBoundingBox().getXsize()/2);
            final double e =  (entity.getEyeY())+ (double) Mth.randomBetween(entity.getRandom(),
                    -0.5f,
                    0.25f);
            final double f = entity.getZ() + (double) Mth.randomBetween(entity.getRandom(),
                    (float)-entity.getBoundingBox().getZsize()/2,
                    (float) entity.getBoundingBox().getZsize()/2);
            entity.level().addParticle(particle, d,e,f,0,0,0);
        }
    }

    @Unique
    private int tcots$bindweedCooldown = 0;
    // Bindweed I   -> Skips damage each 3 hurt
    // Bindweed II  -> Skips damage each 2 hurt
    // Bindweed III -> Skips damage each 1 hurt
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void tcots$bindweedPreventDamage(final DamageSource source, final float amount, final CallbackInfoReturnable<Boolean> cir){
        if(this.hasEffect(TCOTS_Effects.BindweedEffect())){
            final MobEffectInstance bindweed= this.getEffect(TCOTS_Effects.BindweedEffect());

            boolean sufferedEffectDamage=false;

            for (final MobEffectInstance effectInstance: this.getActiveEffects()){
                final int duration = effectInstance.isInfiniteDuration() ? tcots$THIS.tickCount : effectInstance.getDuration();

                if(effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL
                        && effectInstance.getEffect().value().shouldApplyEffectTickThisTick(duration, effectInstance.getAmplifier())){
                    sufferedEffectDamage=true;
                }
            }

            if(sufferedEffectDamage){
                assert bindweed != null;

                if(tcots$bindweedCooldown <= 0){
                    cir.setReturnValue(false);
                    tcots$bindweedCooldown =
                            // Wait 3/2/1 hurt before next possible skip
                            bindweed.getAmplifier()==0? 3:
                                    bindweed.getAmplifier()==1? 2:
                                            1;
                } else {
                    tcots$bindweedCooldown--;
                }
            }
        }
    }
}

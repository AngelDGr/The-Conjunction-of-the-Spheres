package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.entity.ogroids.AbstractTrollEntity;
import TCOTS.interfaces.LivingEntityMixinInterface;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import TCOTS.items.concoctions.bombs.NorthernWindBomb;
import TCOTS.items.concoctions.bombs.SamumBomb;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil_Fabric;
import TCOTS.world.TCOTS_DamageTypes;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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

import java.util.List;
import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, LivingEntityMixinInterface {

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }
    @Unique
    LivingEntity THIS = (LivingEntity)(Object)this;

    @Shadow public abstract void kill();

    @Shadow public abstract boolean hurt(@NotNull DamageSource source, float amount);

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract float getHealth();

    @Shadow public abstract @Nullable LivingEntity getLastHurtMob();

    @Shadow public abstract @Nullable LivingEntity getLastHurtByMob();

    //Killer Whale
    @ModifyVariable(method = "decreaseAirSupply", at = @At("STORE"))
    private double checkForKillerWhaleEffect(double d){

        if(this.hasEffect(TCOTS_Effects.KILLER_WHALE_EFFECT)){
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
    private void injectKillCountDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(KILL_COUNT, 0);
        builder.define(KILL_COUNTDOWN, 0);
    }

    @Override
    public int theConjunctionOfTheSpheres$getKillCount() {
        return this.entityData.get(KILL_COUNT);
    }

    @Override
    public void theConjunctionOfTheSpheres$setKillCount(int killCount) {
        this.entityData.set(KILL_COUNT, killCount);
    }

    @Override
    public int theConjunctionOfTheSpheres$getKillCountdown() {
        return this.entityData.get(KILL_COUNTDOWN);
    }

    @Override
    public void theConjunctionOfTheSpheres$setKillCountdown(int killCountdown) {
        this.entityData.set(KILL_COUNTDOWN, killCountdown);
    }

    @Override
    public void theConjunctionOfTheSpheres$incrementKillCount() {
        int count = this.theConjunctionOfTheSpheres$getKillCount();
        this.theConjunctionOfTheSpheres$setKillCount(count + 1);
    }


    @Inject(method = "setLastHurtMob", at = @At("HEAD"))
    private void injectCountdownAttack(CallbackInfo ci){
        THIS.theConjunctionOfTheSpheres$setKillCountdown(300);
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void injectCountdown(CallbackInfo ci){
        if(THIS.hasEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT)) {
            int count = THIS.theConjunctionOfTheSpheres$getKillCountdown();
            if (THIS.theConjunctionOfTheSpheres$getKillCountdown() > 0) {

                THIS.theConjunctionOfTheSpheres$setKillCountdown(count - 1);

            } else if (THIS.theConjunctionOfTheSpheres$getKillCountdown() == 0) {
                THIS.theConjunctionOfTheSpheres$setKillCount(0);
            }
        } else{
            THIS.theConjunctionOfTheSpheres$setKillCountdown(0);
            THIS.theConjunctionOfTheSpheres$setKillCount(0);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void injectKillCounter(DamageSource damageSource, CallbackInfo ci){
        LivingEntity livingEntity = THIS.getKillCredit();
        if (livingEntity != null) {
            if(livingEntity.hasEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT)){

                int killCount =  livingEntity.theConjunctionOfTheSpheres$getKillCount();

                if(killCount < 20){
                    livingEntity.theConjunctionOfTheSpheres$incrementKillCount();
                    livingEntity.theConjunctionOfTheSpheres$setKillCountdown(420);
                }

            }
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectKillCountWriteNBT(CompoundTag nbt, CallbackInfo ci){
        nbt.putInt("KillCount", THIS.theConjunctionOfTheSpheres$getKillCount());
        nbt.putInt("KillCountdown", THIS.theConjunctionOfTheSpheres$getKillCountdown());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectKillCountReadNBT(CompoundTag nbt, CallbackInfo ci){
        THIS.theConjunctionOfTheSpheres$setKillCount(nbt.getInt("KillCount"));
        THIS.theConjunctionOfTheSpheres$setKillCountdown(nbt.getInt("KillCountdown"));
    }

    //Foglet Decoction
    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float injectFoggyResistance(float amount){
        if(this.hasEffect(TCOTS_Effects.FOGLET_DECOCTION_EFFECT)
                && this.level() instanceof ServerLevel
                && (this.level().isRaining() || this.level().isThundering()))
        {
            return amount/2;
        }

        return amount;
    }


    //Black Blood
    @Inject(method = "hurt", at = @At("TAIL"))
    private void injectBlackBloodDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(this.hasEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)){
            int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)).getAmplifier();
            float damageMultiplier = switch (amplifier) {
                case 0 ->  0.15f;
                case 1 ->  0.20f;
                default -> 0.30f;
            };

            if(source.getEntity() != null && source.getEntity() instanceof LivingEntity attackerBlack &&
                    !((source.getDirectEntity() instanceof Projectile) || (source.getDirectEntity() instanceof AbstractArrow))){
                //Damage
                if(amount > 0 && (EntitiesUtil_Fabric.isNecrophage(attackerBlack) || EntitiesUtil_Fabric.isVampire(attackerBlack))){
                    attackerBlack.hurt(attackerBlack.damageSources().magic(), amount*damageMultiplier);
                }

                //For Knockback above level 0
                double d = this.getX() - attackerBlack.getX();
                double e = this.getZ() - attackerBlack.getZ();
                if(amplifier > 0){
                    attackerBlack.knockback(amplifier*0.5f, d, e);
                }
            }

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), TCOTS_Sounds.BLACK_BLOOD_HIT, this.getSoundSource(),1f,1f);

            this.level().broadcastEntityEvent(THIS, BLACK_BLOOD_PARTICLES);
        }
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void injectBlackBloodBleedingEffect(CallbackInfo ci){
        if(this.hasEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)) {
            int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)).getAmplifier();
            if(amplifier> 1){
                List<LivingEntity> list= this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5,2,5),
                livingEntity -> EntitiesUtil_Fabric.isNecrophage(livingEntity) || EntitiesUtil_Fabric.isVampire(livingEntity));
                //To apply bleeding effect to near mobs
                if(!list.isEmpty()) {
                    list.forEach(livingEntity -> {
                        if(!(livingEntity.hasEffect(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT))){
                            livingEntity.addEffect(new MobEffectInstance(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT, 60, 0, true, false, false));
                        }
                    });
                }
            }
        }
    }

    //SamumEffect
    @Inject(method = "hurt", at = @At("TAIL"))
    private void injectRemoveSamumOnHit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(THIS)){
            this.removeEffect(TCOTS_Effects.SAMUM_EFFECT);
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void injectImmunityToStun(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir){
        if(((THIS instanceof Warden) || (THIS instanceof Guardian)) && (effect.getEffect()==TCOTS_Effects.SAMUM_EFFECT))
            cir.setReturnValue(false);
    }

    //NorthernWind
    @Unique
    private static final EntityDataAccessor<Boolean> IS_FROZEN = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.BOOLEAN);

    @Override
    public boolean theConjunctionOfTheSpheres$isFrozen() {
        return this.entityData.get(IS_FROZEN);
    }

    @Unique
    public void setIsFrozen(boolean frozen) {
        this.entityData.set(IS_FROZEN, frozen);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectNorthernWindDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(IS_FROZEN, false);
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void injectRemoveNorthernWindOnHit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(NorthernWindBomb.checkEffect(THIS)){
            this.playSound(SoundEvents.GLASS_BREAK,1,1);
            this.removeEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT);
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float injectExtraDamageNorthernWind(float amount){
        if(NorthernWindBomb.checkEffect(THIS)){
            int amplifier= Objects.requireNonNull(this.getEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT)).getAmplifier();
            int randomN=this.random.nextIntBetweenInclusive(0,10);
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
    private double injectExtraKnockbackNorthernWind(double strength){
        if(NorthernWindBomb.checkEffect(THIS)){
            return strength * 1.8;
        }
        return strength;
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void injectNorthernWindNoPushable(CallbackInfoReturnable<Boolean> cir){
        if(NorthernWindBomb.checkEffect(THIS)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void injectIsFrozen(CallbackInfo ci){
        if(!THIS.level().isClientSide) {
            setIsFrozen(NorthernWindBomb.checkEffect(THIS));
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void injectImmunityToFreeze(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir){
        if(THIS.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) && (effect.getEffect()==TCOTS_Effects.NORTHERN_WIND_EFFECT))
            cir.setReturnValue(false);
    }

    //Moon Dust
    @Unique
    private static final EntityDataAccessor<Boolean> SILVER_SPLINTERS = SynchedEntityData.defineId(LivingEntityMixin.class, EntityDataSerializers.BOOLEAN);

    @Override
    public boolean theConjunctionOfTheSpheres$hasSilverSplinters() {
        return this.entityData.get(SILVER_SPLINTERS);
    }

    @Unique
    public void setSilverSplinters(boolean frozen) {
        this.entityData.set(SILVER_SPLINTERS, frozen);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectMoonDustDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(SILVER_SPLINTERS, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectSilverSplintersWriteNBT(CompoundTag nbt, CallbackInfo ci){
        nbt.putBoolean("SilverSplinters", THIS.theConjunctionOfTheSpheres$hasSilverSplinters());
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectSilverSplintersReadNBT(CompoundTag nbt, CallbackInfo ci){
        this.setSilverSplinters(nbt.getBoolean("SilverSplinters"));
    }

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void injectHasSilverSplinters(CallbackInfo ci){
        if(!THIS.level().isClientSide) {
            if (MoonDustBomb.checkOnlyEffect(THIS)){
                int amplifier = Objects.requireNonNull(THIS.getEffect(TCOTS_Effects.MOON_DUST_EFFECT)).getAmplifier();
                if(amplifier>1){
                    setSilverSplinters(true);
                }
            }
        }
    }

    //Immunity to bleeding
    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void injectImmunityToBleeding(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir){
        if((
                (THIS instanceof AbstractSkeleton)
                || EntitiesUtil_Fabric.isElementa(THIS)
                || (THIS instanceof WitherBoss)
                || EntitiesUtil_Fabric.isSpecter(THIS))

                && (effect.getEffect()==TCOTS_Effects.BLEEDING || effect.getEffect()==TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT))
            cir.setReturnValue(false);
    }

    //G'valchir damage
    @Unique
    private boolean attackerHasGvalchir =false;
    @Inject(method ="getDamageAfterArmorAbsorb", at = @At("HEAD"))
    private void getAttackerGvalchirBoolean(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(source.getEntity() instanceof LivingEntity livingEntity){
            this.attackerHasGvalchir =
                    //Has the G'valchir in hand
                    livingEntity.getMainHandItem().getItem() == TCOTS_Items_Fabric.GVALCHIR &&
                    //To avoid ignore armor with attacks with projectiles
                    !((source.getDirectEntity() instanceof Projectile) || (source.getDirectEntity() instanceof AbstractArrow))
                    //To avoid ignore armor when there's thorns damage
                    && !(source.typeHolder() == DamageTypes.MAGIC);
        } else {
            attackerHasGvalchir = false;
        }
    }

    @ModifyArgs(method = "getDamageAfterArmorAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;FF)F"))
    private void injectArmorPenetration(Args args){
        if(attackerHasGvalchir){
            float armor = args.get(3);
            float armorToughness = args.get(4);

            args.set(3, armor*0.25f);
            args.set(4, armorToughness*0.50f);
        }
    }

    //Nekker Warrior Decoction
    @Unique
    private boolean passengerHasDecoction =false;
    @Unique
    private static final AttributeModifier PASSENGER_SPEED_BOOST = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"passenger_speed_boost"),
            0.5f,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectExtraNekkerWarriorSpeed(CallbackInfo ci){
        if(THIS.isVehicle()){
            Entity passenger = THIS.getControllingPassenger();

            if(passenger instanceof LivingEntity livingPassenger){
                if(livingPassenger.hasEffect(TCOTS_Effects.NEKKER_WARRIOR_DECOCTION_EFFECT)){
                    passengerHasDecoction = true;
                    AttributeInstance entityAttributeInstance = THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                    if(entityAttributeInstance!=null) {
                        entityAttributeInstance.removeModifier(PASSENGER_SPEED_BOOST.id());
                        entityAttributeInstance.addTransientModifier(PASSENGER_SPEED_BOOST);
                    }
                } else if(passengerHasDecoction){
                    AttributeInstance entityAttributeInstance = THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                    if(entityAttributeInstance!=null) {
                        entityAttributeInstance.removeModifier(PASSENGER_SPEED_BOOST.id());
                        passengerHasDecoction = false;
                    }
                }
            }
        }

    }

    //Troll Reputation
    @Inject(method = "die", at = @At("TAIL"))
    private void injectTrollTriggerDefending(DamageSource damageSource, CallbackInfo ci){

        if(this.level() instanceof ServerLevel && getLastHurtMob() instanceof AbstractTrollEntity troll && getLastHurtByMob() instanceof Player player){
            if(!this.level().isClientSide && !troll.isRabid()) {
                ((ServerLevel) this.level()).onReputationEvent(troll.getDefendingInteraction(false), player, troll);
                troll.handleNearTrollsInteraction(troll.getDefendingInteraction(true), player);
                troll.level().broadcastEntityEvent(troll, troll.getFriendship(player)>troll.getMinFriendshipToBeFollower()? EntityEvent.LOVE_HEARTS: EntityEvent.VILLAGER_HAPPY);
                troll.handleNearTrollsParticles(EntityEvent.VILLAGER_HAPPY);
            }
        }
    }

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void GolemNotAttackFriendlyIceTroll(LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if(THIS.getType() == EntityType.IRON_GOLEM){
            if(target instanceof AbstractTrollEntity troll && !troll.isRabid()){
                cir.setReturnValue(false);
            }
        }
    }


    //Trigger Advancement
    @Inject(method = "die", at = @At("TAIL"))
    private void injectTriggerAdvancement(DamageSource damageSource, CallbackInfo ci){
        if(EntitiesUtil_Fabric.isHumanoid(THIS) && getLastHurtByMob() instanceof Player player){
            if(player.getMainHandItem().has(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT) && player.getMainHandItem().get(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT)!=null){
                MonsterOilComponent monsterOil = player.getMainHandItem().get(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT);

                if(monsterOil!=null && monsterOil.groupId()==11)
                {
                    if(player instanceof ServerPlayer serverPlayer){
                        TCOTS_Criteria.KILL_WITH_HANGED.trigger(serverPlayer);
                    }
                }
            }
        }
    }


    @Shadow public abstract ItemStack getMainHandItem();

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> effect);

    @Shadow public abstract boolean removeEffect(Holder<MobEffect> effect);

    
    @Inject(method ="getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void injectArmorExtraMonsterResistance(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS)){
            cir.setReturnValue(amount);
        }

        if(EntitiesUtil_Fabric.isWearingRavensArmor(THIS) && source.getEntity()!=null
                && source.getEntity() instanceof LivingEntity attacker && EntitiesUtil_Fabric.isMonster(attacker)){
            cir.setReturnValue(amount*0.50f);
        } else
        if(EntitiesUtil_Fabric.isWearingWarriorsLeatherArmor(THIS) && source.getEntity()!=null
                && source.getEntity() instanceof LivingEntity attacker && EntitiesUtil_Fabric.isMonster(attacker)){
            cir.setReturnValue(amount*0.75f);
        }
    }


    //Raven Armor Set Bonus
    @Unique
    private static final AttributeModifier RAVEN_SPEED_BONUS = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "raven_speed_boost"),
            0.1f,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    @Inject(method = "aiStep", at = @At("TAIL"))
    private void injectRavensArmorSetBonus(CallbackInfo ci){

        //Adds Speed boost
        if(EntitiesUtil_Fabric.isWearingRavensArmor(THIS)){
            AttributeInstance entityAttributeInstance = THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) {
                    entityAttributeInstance.removeModifier(RAVEN_SPEED_BONUS.id());
                    entityAttributeInstance.addTransientModifier(RAVEN_SPEED_BONUS);}
        } else {
            AttributeInstance entityAttributeInstance = THIS.getAttribute(Attributes.MOVEMENT_SPEED);
            if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(RAVEN_SPEED_BONUS.id());
        }

    }

    //Winter's Blade
    @Inject(method =  "tick", at= @At("HEAD"))
    private void injectWintersBladeExtinguish(CallbackInfo ci){
        if(THIS.isOnFire()){
            if(THIS.getMainHandItem().is(TCOTS_Items_Fabric.WINTERS_BLADE) || THIS.getOffhandItem().is(TCOTS_Items_Fabric.WINTERS_BLADE)) THIS.clearFire();
        }
    }

    //Tundra Horse Armor
    @Unique
    private static final AttributeModifier TUNDRA_ARMOR_BONUS = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "tundra_armor_speed_boost"),
            0.2f,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void injectSnowSpeed(CallbackInfo ci){
        if(THIS instanceof Horse horse){
            if(horse.getBodyArmorItem().is(TCOTS_Items_Fabric.TUNDRA_HORSE_ARMOR) &&
                    (this.isSteepingOrInside(horse, Blocks.POWDER_SNOW)
                            || this.isSteepingOrInside(horse, Blocks.SNOW_BLOCK) || this.isSteepingOrInside(horse, Blocks.SNOW) ||
                            this.isSteepingOrInside(horse, Blocks.ICE) || this.isSteepingOrInside(horse, Blocks.BLUE_ICE) || this.isSteepingOrInside(horse, Blocks.PACKED_ICE) || this.isSteepingOrInside(horse, Blocks.FROSTED_ICE)
                    )){
                AttributeInstance entityAttributeInstance = THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) {
                    entityAttributeInstance.removeModifier(TUNDRA_ARMOR_BONUS.id());
                    entityAttributeInstance.addTransientModifier(TUNDRA_ARMOR_BONUS);}
            } else {
                AttributeInstance entityAttributeInstance = THIS.getAttribute(Attributes.MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(TUNDRA_ARMOR_BONUS.id());
            }
        }

    }

    @Unique
    private boolean isSteepingOrInside(LivingEntity entity, Block block){
        return entity.getBlockStateOn().is(block) || entity.level().getBlockState(entity.blockPosition()).is(block);
    }

    @Inject(method ="getDamageAfterMagicAbsorb", at = @At("RETURN"), cancellable = true)
    private void injectKnightHorseArmorResistance(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(THIS instanceof Horse horse && horse.getBodyArmorItem().is(TCOTS_Items_Fabric.KNIGHT_ERRANTS_HORSE_ARMOR)){
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
    public AnchorProjectileEntity anchorProjectile;
    @Override
    public AnchorProjectileEntity theConjunctionOfTheSpheres$getAnchor(){
        return anchorProjectile;
    }

    @Override
    public void theConjunctionOfTheSpheres$setAnchor(Object anchor) {
        this.anchorProjectile= (AnchorProjectileEntity) anchor;
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void injectDiscardAnchor(CallbackInfo ci){
        if(!this.level().isClientSide && this.theConjunctionOfTheSpheres$getAnchor()!=null){
            this.theConjunctionOfTheSpheres$getAnchor().setOwner(null);
        }
    }

    //Bleeding effect
    @Unique
    private static final byte BLOOD_PARTICLES = 72;

    @Unique
    private static final byte BLACK_BLOOD_PARTICLES = 73;
    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void injectParticles(byte status, CallbackInfo ci){
        if(status == BLOOD_PARTICLES){
            spawnBloodParticles(THIS, TCOTS_Particles.FALLING_BLOOD_PARTICLE);
        }

        if(status == BLACK_BLOOD_PARTICLES){
            spawnBloodParticles(THIS, TCOTS_Particles.FALLING_BLACK_BLOOD_PARTICLE);
        }
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void injectInDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(source.is(TCOTS_DamageTypes.BLEEDING) && !this.hasEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)){
            this.level().broadcastEntityEvent(THIS,
                    THIS.hasEffect(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT)? BLACK_BLOOD_PARTICLES :BLOOD_PARTICLES);
        }
    }

    //xTODO: Test this to better adapt to size
    @Unique
    protected void spawnBloodParticles(LivingEntity entity, SimpleParticleType particle){

        for(int i=0; i<10; i++){
            double d = entity.getX() + (double) Mth.randomBetween(entity.getRandom(),
                    (float)-entity.getBoundingBox().getXsize()/2,
                    (float) entity.getBoundingBox().getXsize()/2);
            double e =  (entity.getEyeY())+ (double) Mth.randomBetween(entity.getRandom(),
                    -0.5f,
                    0.25f);
            double f = entity.getZ() + (double) Mth.randomBetween(entity.getRandom(),
                    (float)-entity.getBoundingBox().getZsize()/2,
                    (float) entity.getBoundingBox().getZsize()/2);
            entity.level().addParticle(particle, d,e,f,0,0,0);
        }


    }
}

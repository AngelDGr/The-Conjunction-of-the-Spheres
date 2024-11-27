package TCOTS.mixin;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.entity.ogroids.AbstractTrollEntity;
import TCOTS.interfaces.LivingEntityMixinInterface;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.bombs.MoonDustBomb;
import TCOTS.items.concoctions.bombs.NorthernWindBomb;
import TCOTS.items.concoctions.bombs.SamumBomb;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, LivingEntityMixinInterface {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Unique
    LivingEntity THIS = (LivingEntity)(Object)this;
    @Shadow @Nullable
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract Random getRandom();

    @Shadow public abstract void kill();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    @Shadow public abstract boolean removeStatusEffect(StatusEffect type);

    @Shadow public abstract float getMaxHealth();

    @Shadow public abstract float getHealth();

    @Shadow public abstract @Nullable LivingEntity getAttacking();

    @Shadow public abstract @Nullable LivingEntity getAttacker();

    //Killer Whale
    @Inject(method = "getNextAirUnderwater", at = @At("TAIL"), cancellable = true)
    private void checkForKillerWhaleEffect(int air, CallbackInfoReturnable<Integer> cir){
        //If the player has RespirationIII, the Respiration effect overrides the Killer Whale respiration effect
        if(this.hasStatusEffect(TCOTS_Effects.KILLER_WHALE_EFFECT) && EnchantmentHelper.getRespiration(THIS) < 3){

            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.KILLER_WHALE_EFFECT)).getAmplifier();
            //With +2 works exactly as respiration I at amplifier I
            //With +3 it should work as respiration II at amplifier I
            if(this.getRandom().nextInt(amplifier+3)>0){
                cir.setReturnValue(air);
            }
        }
    }

    //Kill Counter
    @Unique
    private static final TrackedData<Integer> KILL_COUNT = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> KILL_COUNTDOWN = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectKillCountDataTracker(CallbackInfo ci){
        this.dataTracker.startTracking(KILL_COUNT, 0);
        this.dataTracker.startTracking(KILL_COUNTDOWN, 0);
    }

    @Override
    public int theConjunctionOfTheSpheres$getKillCount() {
        return this.dataTracker.get(KILL_COUNT);
    }

    @Override
    public void theConjunctionOfTheSpheres$setKillCount(int killCount) {
        this.dataTracker.set(KILL_COUNT, killCount);
    }

    @Override
    public int theConjunctionOfTheSpheres$getKillCountdown() {
        return this.dataTracker.get(KILL_COUNTDOWN);
    }

    @Override
    public void theConjunctionOfTheSpheres$setKillCountdown(int killCountdown) {
        this.dataTracker.set(KILL_COUNTDOWN, killCountdown);
    }

    @Override
    public void theConjunctionOfTheSpheres$incrementKillCount() {
        int count = this.theConjunctionOfTheSpheres$getKillCount();
        this.theConjunctionOfTheSpheres$setKillCount(count + 1);
    }


    @Inject(method = "onAttacking", at = @At("HEAD"))
    private void injectCountdownAttack(CallbackInfo ci){
        THIS.theConjunctionOfTheSpheres$setKillCountdown(300);
    }

    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void injectCountdown(CallbackInfo ci){
        if(THIS.hasStatusEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT)) {
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

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void injectKillCounter(DamageSource damageSource, CallbackInfo ci){
        LivingEntity livingEntity = THIS.getPrimeAdversary();
        if (livingEntity != null) {
            if(livingEntity.hasStatusEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT)){

                int killCount =  livingEntity.theConjunctionOfTheSpheres$getKillCount();

                if(killCount < 20){
                    livingEntity.theConjunctionOfTheSpheres$incrementKillCount();
                    livingEntity.theConjunctionOfTheSpheres$setKillCountdown(420);
                }

            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectKillCountWriteNBT(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("KillCount", THIS.theConjunctionOfTheSpheres$getKillCount());
        nbt.putInt("KillCountdown", THIS.theConjunctionOfTheSpheres$getKillCountdown());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectKillCountReadNBT(NbtCompound nbt, CallbackInfo ci){
        THIS.theConjunctionOfTheSpheres$setKillCount(nbt.getInt("KillCount"));
        THIS.theConjunctionOfTheSpheres$setKillCountdown(nbt.getInt("KillCountdown"));
    }

    //Foglet Decoction
    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float injectFoggyResistance(float amount){
        if(this.hasStatusEffect(TCOTS_Effects.FOGLET_DECOCTION_EFFECT)
                && this.getWorld() instanceof ServerWorld
                && (this.getWorld().isRaining() || this.getWorld().isThundering()))
        {
            return amount/2;
        }

        return amount;
    }


    //Black Blood
    @Inject(method = "damage", at = @At("TAIL"))
    private void injectBlackBloodDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(this.hasStatusEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)){
            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)).getAmplifier();
            float damageMultiplier = switch (amplifier) {
                case 0 ->  0.15f;
                case 1 ->  0.20f;
                default -> 0.30f;
            };

            if(source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attackerBlack &&
                    !((source.getSource() instanceof ProjectileEntity) || (source.getSource() instanceof PersistentProjectileEntity))){
                //Damage
                if(amount > 0 && (EntitiesUtil.isNecrophage(attackerBlack) || EntitiesUtil.isVampire(attackerBlack))){
                    attackerBlack.damage(attackerBlack.getDamageSources().magic(), amount*damageMultiplier);
                }

                //For Knockback above level 0
                double d = this.getX() - attackerBlack.getX();
                double e = this.getZ() - attackerBlack.getZ();
                if(amplifier > 0){
                    attackerBlack.takeKnockback(amplifier*0.5f, d, e);
                }
            }

            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), TCOTS_Sounds.BLACK_BLOOD_HIT, this.getSoundCategory(),1f,1f);

            this.getWorld().sendEntityStatus(THIS, BLACK_BLOOD_PARTICLES);
        }
    }

    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void injectBlackBloodBleedingEffect(CallbackInfo ci){
        if(this.hasStatusEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)) {
            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)).getAmplifier();
            if(amplifier> 1){
                List<LivingEntity> list= this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(5,2,5),
                livingEntity -> EntitiesUtil.isNecrophage(livingEntity) || EntitiesUtil.isVampire(livingEntity));
                //To apply bleeding effect to near mobs
                if(!list.isEmpty()) {
                    list.forEach(livingEntity -> {
                        if(!(livingEntity.hasStatusEffect(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT))){
                            livingEntity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT, 60, 0, true, false, false));
                        }
                    });
                }
            }
        }
    }

    //SamumEffect
    @Inject(method = "damage", at = @At("TAIL"))
    private void injectRemoveSamumOnHit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(THIS)){
            this.removeStatusEffect(TCOTS_Effects.SAMUM_EFFECT);
        }
    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void injectImmunityToStun(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir){
        if(((THIS instanceof WardenEntity) || (THIS instanceof GuardianEntity)) && (effect.getEffectType()==TCOTS_Effects.SAMUM_EFFECT))
            cir.setReturnValue(false);
    }

    //NorthernWind
    @Unique
    private static final TrackedData<Boolean> IS_FROZEN = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public boolean theConjunctionOfTheSpheres$isFrozen() {
        return this.dataTracker.get(IS_FROZEN);
    }

    @Unique
    public void setIsFrozen(boolean frozen) {
        this.dataTracker.set(IS_FROZEN, frozen);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectNorthernWindDataTracker(CallbackInfo ci){
        this.dataTracker.startTracking(IS_FROZEN, false);
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void injectRemoveNorthernWindOnHit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(NorthernWindBomb.checkEffect(THIS)){
            this.playSound(SoundEvents.BLOCK_GLASS_BREAK,1,1);
            this.removeStatusEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT);
        }
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float injectExtraDamageNorthernWind(float amount){
        if(NorthernWindBomb.checkEffect(THIS)){
            int amplifier= Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT)).getAmplifier();
            int randomN=this.random.nextBetween(0,10);
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

    @ModifyVariable(method = "takeKnockback", at = @At("HEAD"), ordinal = 0, argsOnly = true)
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

    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void injectIsFrozen(CallbackInfo ci){
        if(!THIS.getWorld().isClient) {
            setIsFrozen(NorthernWindBomb.checkEffect(THIS));
        }
    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void injectImmunityToFreeze(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir){
        if(THIS.getType().isIn(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES) && (effect.getEffectType()==TCOTS_Effects.NORTHERN_WIND_EFFECT))
            cir.setReturnValue(false);
    }

    //Moon Dust
    @Unique
    private static final TrackedData<Boolean> SILVER_SPLINTERS = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Override
    public boolean theConjunctionOfTheSpheres$hasSilverSplinters() {
        return this.dataTracker.get(SILVER_SPLINTERS);
    }

    @Unique
    public void setSilverSplinters(boolean frozen) {
        this.dataTracker.set(SILVER_SPLINTERS, frozen);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectMoonDustDataTracker(CallbackInfo ci){
        this.dataTracker.startTracking(SILVER_SPLINTERS, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectSilverSplintersWriteNBT(NbtCompound nbt, CallbackInfo ci){
        nbt.putBoolean("SilverSplinters", THIS.theConjunctionOfTheSpheres$hasSilverSplinters());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectSilverSplintersReadNBT(NbtCompound nbt, CallbackInfo ci){
        this.setSilverSplinters(nbt.getBoolean("SilverSplinters"));
    }

    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void injectHasSilverSplinters(CallbackInfo ci){
        if(!THIS.getWorld().isClient) {
            if (MoonDustBomb.checkOnlyEffect(THIS)){
                int amplifier = Objects.requireNonNull(THIS.getStatusEffect(TCOTS_Effects.MOON_DUST_EFFECT)).getAmplifier();
                if(amplifier>1){
                    setSilverSplinters(true);
                }
            }
        }
    }

    //Immunity to bleeding
    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void injectImmunityToBleeding(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir){
        if((
                (THIS instanceof AbstractSkeletonEntity)
                || EntitiesUtil.isElementa(THIS)
                || (THIS instanceof WitherEntity)
                || EntitiesUtil.isSpecter(THIS))

                && (effect.getEffectType()==TCOTS_Effects.BLEEDING || effect.getEffectType()==TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT))
            cir.setReturnValue(false);
    }

    //G'valchir damage
    @Unique
    private boolean attackerHasGvalchir =false;
    @Inject(method ="applyArmorToDamage", at = @At("HEAD"))
    private void getAttackerGvalchirBoolean(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(source.getAttacker() instanceof LivingEntity livingEntity){
            this.attackerHasGvalchir =
                    //Has the G'valchir in hand
                    livingEntity.getMainHandStack().getItem() == TCOTS_Items.GVALCHIR &&
                    //To avoid ignore armor with attacks with projectiles
                    !((source.getSource() instanceof ProjectileEntity) || (source.getSource() instanceof PersistentProjectileEntity))
                    //To avoid ignore armor when there's thorns damage
                    && !(source.getTypeRegistryEntry() == DamageTypes.MAGIC);
        } else {
            attackerHasGvalchir = false;
        }
    }

    @ModifyArgs(method = "applyArmorToDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F"))
    private void injectArmorPenetration(Args args){
        if(attackerHasGvalchir){
            float armor = args.get(1);
            float armorToughness = args.get(2);

            args.set(1, armor*0.25f);
            args.set(2, armorToughness*0.50f);
        }
    }

    //Nekker Warrior Decoction
    @Unique
    private boolean passengerHasDecoction =false;
    @Unique
    private static final UUID PASSENGER_SPEED_BOOST_ID = UUID.fromString("c06c875b-75cf-4716-a817-2c461b84f8ec");
    @Unique
    private static final EntityAttributeModifier PASSENGER_SPEED_BOOST = new EntityAttributeModifier(PASSENGER_SPEED_BOOST_ID, "passenger speed boost", 0.5f, EntityAttributeModifier.Operation.MULTIPLY_BASE);

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectExtraNekkerWarriorSpeed(CallbackInfo ci){
        if(THIS.hasPassengers()){
            Entity passenger = THIS.getControllingPassenger();

            if(passenger instanceof LivingEntity livingPassenger){
                if(livingPassenger.hasStatusEffect(TCOTS_Effects.NEKKER_WARRIOR_DECOCTION_EFFECT)){
                    passengerHasDecoction = true;
                    EntityAttributeInstance entityAttributeInstance = THIS.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    if(entityAttributeInstance!=null) {
                        entityAttributeInstance.removeModifier(PASSENGER_SPEED_BOOST.getId());
                        entityAttributeInstance.addTemporaryModifier(PASSENGER_SPEED_BOOST);
                    }
                } else if(passengerHasDecoction){
                    EntityAttributeInstance entityAttributeInstance = THIS.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                    if(entityAttributeInstance!=null) {
                        entityAttributeInstance.removeModifier(PASSENGER_SPEED_BOOST.getId());
                        passengerHasDecoction = false;
                    }
                }
            }
        }

    }

    //Troll Reputation
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void injectTrollTriggerDefending(DamageSource damageSource, CallbackInfo ci){

        if(this.getWorld() instanceof ServerWorld && getAttacking() instanceof AbstractTrollEntity troll && getAttacker() instanceof PlayerEntity player){
            if(!this.getWorld().isClient && !troll.isRabid()) {
                ((ServerWorld) this.getWorld()).handleInteraction(troll.getDefendingInteraction(false), player, troll);
                troll.handleNearTrollsInteraction(troll.getDefendingInteraction(true), player);
                troll.getWorld().sendEntityStatus(troll, troll.getFriendship(player)>troll.getMinFriendshipToBeFollower()? EntityStatuses.ADD_VILLAGER_HEART_PARTICLES: EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                troll.handleNearTrollsParticles(EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            }
        }
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void GolemNotAttackFriendlyIceTroll(LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if(THIS.getType() == EntityType.IRON_GOLEM){
            if(target instanceof AbstractTrollEntity troll && !troll.isRabid()){
                cir.setReturnValue(false);
            }
        }
    }


    //Trigger Advancement
    @Inject(method = "onDeath", at = @At("TAIL"))
    private void injectTriggerAdvancement(DamageSource damageSource, CallbackInfo ci){
        if(EntitiesUtil.isHumanoid(THIS) && getAttacker() instanceof PlayerEntity player){
            NbtCompound nbt = player.getMainHandStack().getNbt();

            if(nbt!=null && nbt.contains("Monster Oil")){
                NbtCompound monsterOil = player.getMainHandStack().getSubNbt("Monster Oil");
                assert monsterOil != null;
                if(monsterOil.getInt("Id")==11)
                {
                    if(player instanceof ServerPlayerEntity serverPlayer){
                        TCOTS_Criteria.KILL_WITH_HANGED.trigger(serverPlayer);
                    }
                }
            }

        }
    }

    @Shadow public abstract ItemStack getMainHandStack();
    
    @Inject(method ="modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void injectArmorExtraMonsterResistance(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)){
            cir.setReturnValue(amount);
        }

        if(EntitiesUtil.isWearingRavensArmor(THIS) && source.getAttacker()!=null
                && source.getAttacker() instanceof LivingEntity attacker && EntitiesUtil.isMonster(attacker)){
            cir.setReturnValue(amount*0.50f);
        } else
        if(EntitiesUtil.isWearingWarriorsLeatherArmor(THIS) && source.getAttacker()!=null
                && source.getAttacker() instanceof LivingEntity attacker && EntitiesUtil.isMonster(attacker)){
            cir.setReturnValue(amount*0.75f);
        }
    }


    //Raven Armor Set Bonus
    @Unique
    private static final UUID RAVEN_SPEED_BONUS_ID = UUID.fromString("a1f7779d-f64f-4a12-b69f-1a8f4ac13419");
    @Unique
    private static final EntityAttributeModifier RAVEN_SPEED_BONUS = new EntityAttributeModifier(RAVEN_SPEED_BONUS_ID, "Raven speed boost", 0.1f, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void injectRavensArmorSetBonus(CallbackInfo ci){

        //Adds Speed boost
        if(EntitiesUtil.isWearingRavensArmor(THIS)){
            EntityAttributeInstance entityAttributeInstance = THIS.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) {
                    entityAttributeInstance.removeModifier(RAVEN_SPEED_BONUS.getId());
                    entityAttributeInstance.addTemporaryModifier(RAVEN_SPEED_BONUS);}
        } else {
            EntityAttributeInstance entityAttributeInstance = THIS.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
            if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(RAVEN_SPEED_BONUS.getId());
        }

    }

    //Winter's Blade
    @Inject(method =  "tick", at= @At("HEAD"))
    private void injectWintersBladeExtinguish(CallbackInfo ci){
        if(THIS.isOnFire()){
            if(THIS.getMainHandStack().isOf(TCOTS_Items.WINTERS_BLADE) || THIS.getOffHandStack().isOf(TCOTS_Items.WINTERS_BLADE)) THIS.extinguish();
        }
    }

    //Tundra Horse Armor
    @Unique
    private static final UUID TUNDRA_ARMOR_SPEED_BONUS_ID = UUID.fromString("c0ee7c38-8973-49a3-bfbd-9b62e1e494ec");
    @Unique
    private static final EntityAttributeModifier TUNDRA_ARMOR_BONUS = new EntityAttributeModifier(TUNDRA_ARMOR_SPEED_BONUS_ID, "Tundra armor speed boost", 0.2f, EntityAttributeModifier.Operation.MULTIPLY_BASE);

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void injectSnowSpeed(CallbackInfo ci){
        if(THIS instanceof HorseEntity horse){
            if(horse.getArmorType().isOf(TCOTS_Items.TUNDRA_HORSE_ARMOR) &&
                    (this.isSteepingOrInside(horse, Blocks.POWDER_SNOW)
                            || this.isSteepingOrInside(horse, Blocks.SNOW_BLOCK) || this.isSteepingOrInside(horse, Blocks.SNOW) ||
                            this.isSteepingOrInside(horse, Blocks.ICE) || this.isSteepingOrInside(horse, Blocks.BLUE_ICE) || this.isSteepingOrInside(horse, Blocks.PACKED_ICE) || this.isSteepingOrInside(horse, Blocks.FROSTED_ICE)
                    )){
                EntityAttributeInstance entityAttributeInstance = THIS.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) {
                    entityAttributeInstance.removeModifier(TUNDRA_ARMOR_BONUS.getId());
                    entityAttributeInstance.addTemporaryModifier(TUNDRA_ARMOR_BONUS);}
            } else {
                EntityAttributeInstance entityAttributeInstance = THIS.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
                if(entityAttributeInstance!=null) entityAttributeInstance.removeModifier(TUNDRA_ARMOR_BONUS.getId());
            }
        }

    }

    @Unique
    private boolean isSteepingOrInside(LivingEntity entity, Block block){
        return entity.getSteppingBlockState().isOf(block) || entity.getWorld().getBlockState(entity.getBlockPos()).isOf(block);
    }

    @Inject(method ="modifyAppliedDamage", at = @At("RETURN"), cancellable = true)
    private void injectKnightHorseArmorResistance(DamageSource source, float amount, CallbackInfoReturnable<Float> cir){
        if(THIS instanceof HorseEntity horse && horse.getArmorType().isOf(TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR)){
            if(source.isIn(DamageTypeTags.BYPASSES_ENCHANTMENTS)){
                cir.setReturnValue(amount);
            }

            if(source.isIn(DamageTypeTags.IS_PROJECTILE)){
                cir.setReturnValue(amount*0.60f);
            }

            if(source.isIn(DamageTypeTags.IS_EXPLOSION)){
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
    public void theConjunctionOfTheSpheres$setAnchor(AnchorProjectileEntity anchor) {
        this.anchorProjectile=anchor;
    }

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void injectDiscardAnchor(CallbackInfo ci){
        if(!this.getWorld().isClient && this.theConjunctionOfTheSpheres$getAnchor()!=null){
            this.theConjunctionOfTheSpheres$getAnchor().setOwner(null);
        }
    }

    //Bleeding effect
    @Unique
    private static final byte BLOOD_PARTICLES = 72;

    @Unique
    private static final byte BLACK_BLOOD_PARTICLES = 73;
    @Inject(method = "handleStatus", at = @At("TAIL"))
    private void injectParticles(byte status, CallbackInfo ci){
        if(status == BLOOD_PARTICLES){
            spawnBloodParticles(THIS, TCOTS_Particles.FALLING_BLOOD_PARTICLE);
        }

        if(status == BLACK_BLOOD_PARTICLES){
            spawnBloodParticles(THIS, TCOTS_Particles.FALLING_BLACK_BLOOD_PARTICLE);
        }
    }

    @Inject(method = "damage", at = @At("TAIL"))
    private void injectInDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(source.isOf(TCOTS_DamageTypes.BLEEDING) && !this.hasStatusEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)){
            this.getWorld().sendEntityStatus(THIS,
                    THIS.hasStatusEffect(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT)? BLACK_BLOOD_PARTICLES :BLOOD_PARTICLES);
        }
    }

    //xTODO: Test this to better adapt to size
    @Unique
    protected void spawnBloodParticles(LivingEntity entity, DefaultParticleType particle){

        for(int i=0; i<10; i++){
            double d = entity.getX() + (double) MathHelper.nextBetween(entity.getRandom(),
                    (float)-entity.getBoundingBox().getLengthX()/2,
                    (float) entity.getBoundingBox().getLengthX()/2);
            double e =  (entity.getEyeY())+ (double) MathHelper.nextBetween(entity.getRandom(),
                    -0.5f,
                    0.25f);
            double f = entity.getZ() + (double) MathHelper.nextBetween(entity.getRandom(),
                    (float)-entity.getBoundingBox().getLengthZ()/2,
                    (float) entity.getBoundingBox().getLengthZ()/2);
            entity.getWorld().addParticle(particle, d,e,f,0,0,0);
        }


    }
}

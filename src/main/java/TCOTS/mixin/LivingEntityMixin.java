package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.interfaces.LivingEntityMixinInterface;
import TCOTS.potions.TCOTS_Effects;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
@Debug(export = true) // Enables exporting for the targets of this mixin
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable, LivingEntityMixinInterface {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow @Nullable
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract Random getRandom();

    @Shadow public abstract void kill();

    @Shadow public abstract boolean damage(DamageSource source, float amount);

    //Killer Whale
    @Inject(method = "getNextAirUnderwater", at = @At("TAIL"), cancellable = true)
    private void checkForKillerWhaleEffect(int air, CallbackInfoReturnable<Integer> cir){
        LivingEntity thisObject = (LivingEntity)(Object)this;

        //If the player has RespirationIII, the Respiration effect overrides the Killer Whale respiration effect
        if(this.hasStatusEffect(TCOTS_Effects.KILLER_WHALE_EFFECT) && EnchantmentHelper.getRespiration(thisObject) < 3){

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
        LivingEntity THIS = (LivingEntity)(Object)this;
        THIS.theConjunctionOfTheSpheres$setKillCountdown(300);
    }

    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void injectCountdown(CallbackInfo ci){
        LivingEntity THIS = (LivingEntity)(Object)this;
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
        LivingEntity THIS = (LivingEntity)(Object)this;

        LivingEntity livingEntity = THIS.getPrimeAdversary();
        if (livingEntity != null) {
            if(livingEntity.hasStatusEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT)){

                int killCount =  livingEntity.theConjunctionOfTheSpheres$getKillCount();

                if(killCount < 20){
                    livingEntity.theConjunctionOfTheSpheres$incrementKillCount();
                    livingEntity.theConjunctionOfTheSpheres$setKillCountdown(400);
                }

            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectKillCountWriteNBT(NbtCompound nbt, CallbackInfo ci){
        LivingEntity THIS = (LivingEntity)(Object)this;

        nbt.putInt("KillCount", THIS.theConjunctionOfTheSpheres$getKillCount());

        nbt.putInt("KillCountdown", THIS.theConjunctionOfTheSpheres$getKillCountdown());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectKillCountReadNBT(NbtCompound nbt, CallbackInfo ci){
        LivingEntity THIS = (LivingEntity)(Object)this;

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
                case 0 -> 0.15f;
                case 1 -> 0.2f;
                default -> 0.3f;
            };

            if(source.getAttacker() != null && source.getAttacker() instanceof LivingEntity attackerBlack &&
                    !((source.getSource() instanceof ProjectileEntity) || (source.getSource() instanceof PersistentProjectileEntity))){
                //Damage
                if(amount > 0 && (attackerBlack.getGroup() == TCOTS_Entities.NECROPHAGES || attackerBlack.getGroup() == EntityGroup.UNDEAD)){
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
        }
    }

    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void injectBlackBloodBleedingEffect(CallbackInfo ci){
        if(this.hasStatusEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)) {
            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.BLACK_BLOOD_EFFECT)).getAmplifier();
            if(amplifier> 1){
                List<LivingEntity> list= this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(5,2,5),
                livingEntity -> (livingEntity.getGroup() == TCOTS_Entities.NECROPHAGES || livingEntity.getGroup() == EntityGroup.UNDEAD));
                //To apply bleeding effect to near mobs
                if(!list.isEmpty()) {
                    list.forEach(livingEntity -> {
                        if(!(livingEntity.hasStatusEffect(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT))){
                            livingEntity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.BLEEDING_BLACK_BLOOD_EFFECT, 10, 0));
                        }
                    });
                }
            }
        }
    }
}

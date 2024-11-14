package TCOTS.mixin;

import TCOTS.items.concoctions.WitcherPotionsSplash_Base;
import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionEntity.class)
public class PotionEntityMixin extends ThrownItemEntity implements FlyingItemEntity {

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onCollisionWitcherPotion(HitResult hitResult, CallbackInfo ci){

        PotionEntity thisObject = (PotionEntity)(Object)this;

        if (!thisObject.getWorld().isClient) {

            ItemStack itemStack = thisObject.getStack();
            if(itemStack.getItem() instanceof WitcherPotionsSplash_Base){
                List<StatusEffectInstance> statusEffectList = ((WitcherPotionsSplash_Base) itemStack.getItem()).getPotionEffects();
                int toxicity = ((WitcherPotionsSplash_Base) itemStack.getItem()).getToxicity();

                this.applySplashWitcherPotion(statusEffectList, hitResult.getType() == net.minecraft.util.hit.HitResult.Type.ENTITY ? ((EntityHitResult)hitResult).getEntity() : null, toxicity);

                int i = ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffectType().value().isInstant() ? 2007 : 2002;
                this.getWorld().syncWorldEvent(i, this.getBlockPos(), ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffectType().value().getColor());
                this.discard();
                ci.cancel();
            }
        }
    }

    @Unique
    private void applySplashWitcherPotion(List<StatusEffectInstance> statusEffects, Entity entity, int toxicity) {
        Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);
        List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, box);
        if (!list.isEmpty()) {
            Entity entity2 = this.getEffectCause();

            for (LivingEntity livingEntity : list) {
                if (livingEntity.isAffectedBySplashPotions()) {
                    double d = this.squaredDistanceTo(livingEntity);
                    if (d < 16.0) {
                        double e;
                        if (livingEntity == entity) {
                            e = 1.0;
                        } else {
                            e = 1.0 - Math.sqrt(d) / 4.0;
                        }

                        //Returns if you have already enough toxicity
                        if (livingEntity instanceof PlayerEntity player && player.theConjunctionOfTheSpheres$getMaxToxicity() < player.theConjunctionOfTheSpheres$getAllToxicity()+toxicity) {
                            player.sendMessage(Text.translatable("tcots-witcher.gui.toxicity_danger").formatted(Formatting.DARK_GREEN), true);
                            player.damage(TCOTS_DamageTypes.toxicityDamage(getWorld()),1+(toxicity*0.1f));

                            return;
                        }

                        for (StatusEffectInstance statusEffectInstance : statusEffects) {
                            RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
                            if (registryEntry.value().isInstant()) {
                                registryEntry.value().applyInstantEffect(this, this.getOwner(), livingEntity, statusEffectInstance.getAmplifier(), e);
                            } else {
                                int i = statusEffectInstance.mapDuration(duration -> (int) (e * (double) duration + 0.5));
                                StatusEffectInstance statusEffectInstance2 = new StatusEffectInstance(
                                        registryEntry, i, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()
                                );
                                if (!statusEffectInstance2.isDurationBelow(20)) {
                                    livingEntity.addStatusEffect(statusEffectInstance2, entity2);
                                }
                            }
                        }

                        //To add toxicity to players
                        if (livingEntity instanceof PlayerEntity player)
                            player.theConjunctionOfTheSpheres$addToxicity(toxicity, false);
                    }
                }
            }
        }
    }

    @Shadow
    protected Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }
}

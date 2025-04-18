package TCOTS.mixin;

import TCOTS.items.concoctions.WitcherPotionsSplash_Base;
import TCOTS.world.TCOTS_DamageTypes;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

@Mixin(ThrownPotion.class)
public class PotionEntityMixin extends ThrowableItemProjectile implements ItemSupplier {

    public PotionEntityMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void onCollisionWitcherPotion(HitResult hitResult, CallbackInfo ci){

        ThrownPotion thisObject = (ThrownPotion)(Object)this;

        if (!thisObject.level().isClientSide) {

            ItemStack itemStack = thisObject.getItem();
            if(itemStack.getItem() instanceof WitcherPotionsSplash_Base){
                List<MobEffectInstance> statusEffectList = ((WitcherPotionsSplash_Base) itemStack.getItem()).getPotionEffects();
                int toxicity = ((WitcherPotionsSplash_Base) itemStack.getItem()).getToxicity();

                this.applySplashWitcherPotion(statusEffectList, hitResult.getType() == net.minecraft.world.phys.HitResult.Type.ENTITY ? ((EntityHitResult)hitResult).getEntity() : null, toxicity);

                int i = ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffect().value().isInstantenous() ? 2007 : 2002;
                this.level().levelEvent(i, this.blockPosition(), ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffect().value().getColor());
                this.discard();
                ci.cancel();
            }
        }
    }

    @Unique
    private void applySplashWitcherPotion(List<MobEffectInstance> statusEffects, Entity entity, int toxicity) {
        AABB box = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);
        if (!list.isEmpty()) {
            Entity entity2 = this.getEffectSource();

            for (LivingEntity livingEntity : list) {
                if (livingEntity.isAffectedByPotions()) {
                    double d = this.distanceToSqr(livingEntity);
                    if (d < 16.0) {
                        double e;
                        if (livingEntity == entity) {
                            e = 1.0;
                        } else {
                            e = 1.0 - Math.sqrt(d) / 4.0;
                        }

                        //Returns if you have already enough toxicity
                        if (livingEntity instanceof Player player && player.theConjunctionOfTheSpheres$getMaxToxicity() < player.theConjunctionOfTheSpheres$getAllToxicity()+toxicity) {
                            player.displayClientMessage(Component.translatable("tcots_witcher.gui.toxicity_danger").withStyle(ChatFormatting.DARK_GREEN), true);
                            player.hurt(TCOTS_DamageTypes.toxicityDamage(level()),1+(toxicity*0.1f));

                            return;
                        }

                        for (MobEffectInstance statusEffectInstance : statusEffects) {
                            Holder<MobEffect> registryEntry = statusEffectInstance.getEffect();
                            if (registryEntry.value().isInstantenous()) {
                                registryEntry.value().applyInstantenousEffect(this, this.getOwner(), livingEntity, statusEffectInstance.getAmplifier(), e);
                            } else {
                                int i = statusEffectInstance.mapDuration(duration -> (int) (e * (double) duration + 0.5));
                                MobEffectInstance statusEffectInstance2 = new MobEffectInstance(
                                        registryEntry, i, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.isVisible()
                                );
                                if (!statusEffectInstance2.endsWithin(20)) {
                                    livingEntity.addEffect(statusEffectInstance2, entity2);
                                }
                            }
                        }

                        //To add toxicity to players
                        if (livingEntity instanceof Player player)
                            player.theConjunctionOfTheSpheres$addToxicity(toxicity, false);
                    }
                }
            }
        }
    }

    @Shadow
    protected @NotNull Item getDefaultItem() {
        return Items.SPLASH_POTION;
    }
}

package mors.tcots.mixin;

import mors.tcots.items.concoctions.WitcherPotionsSplash_Base;
import mors.tcots.registry.TCOTS_DamageTypes;
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

    public PotionEntityMixin(final EntityType<? extends ThrowableItemProjectile> entityType, final Level world) {
        super(entityType, world);
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void tcots$onCollisionWitcherPotion(final HitResult hitResult, final CallbackInfo ci){

        final ThrownPotion thisObject = (ThrownPotion)(Object)this;

        if (!thisObject.level().isClientSide) {

            final ItemStack itemStack = thisObject.getItem();
            if(itemStack.getItem() instanceof WitcherPotionsSplash_Base){
                final List<MobEffectInstance> statusEffectList = ((WitcherPotionsSplash_Base) itemStack.getItem()).getPotionEffects();
                final int toxicity = ((WitcherPotionsSplash_Base) itemStack.getItem()).getToxicity();

                this.tcots$applySplashWitcherPotion(statusEffectList, hitResult.getType() == net.minecraft.world.phys.HitResult.Type.ENTITY ? ((EntityHitResult)hitResult).getEntity() : null, toxicity);

                final int i = ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffect().value().isInstantenous() ? 2007 : 2002;
                this.level().levelEvent(i, this.blockPosition(), ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffect().value().getColor());
                this.discard();
                ci.cancel();
            }
        }
    }

    @Unique
    private void tcots$applySplashWitcherPotion(final List<MobEffectInstance> statusEffects, final Entity entity, final int toxicity) {
        final AABB box = this.getBoundingBox().inflate(4.0, 2.0, 4.0);
        final List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, box);
        if (!list.isEmpty()) {
            final Entity entity2 = this.getEffectSource();

            for (final LivingEntity livingEntity : list) {
                if (livingEntity.isAffectedByPotions()) {
                    final double d = this.distanceToSqr(livingEntity);
                    if (d < 16.0) {
                        final double e;
                        if (livingEntity == entity) {
                            e = 1.0;
                        } else {
                            e = 1.0 - Math.sqrt(d) / 4.0;
                        }

                        //Returns if you have already enough toxicity
                        if (livingEntity instanceof final Player player && player.tcots$setMaxToxicity() < player.tcots$getAllToxicity()+toxicity) {
                            player.displayClientMessage(Component.translatable("gui.tcots_witcher.message.toxicity_danger").withStyle(ChatFormatting.DARK_GREEN), true);
                            player.hurt(TCOTS_DamageTypes.toxicityDamage(level()),1+(toxicity*0.1f));

                            return;
                        }

                        for (final MobEffectInstance statusEffectInstance : statusEffects) {
                            final Holder<MobEffect> registryEntry = statusEffectInstance.getEffect();
                            if (registryEntry.value().isInstantenous()) {
                                registryEntry.value().applyInstantenousEffect(this, this.getOwner(), livingEntity, statusEffectInstance.getAmplifier(), e);
                            } else {
                                final int i = statusEffectInstance.mapDuration(duration -> (int) (e * (double) duration + 0.5));
                                final MobEffectInstance statusEffectInstance2 = new MobEffectInstance(
                                        registryEntry, i, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.isVisible()
                                );
                                if (!statusEffectInstance2.endsWithin(20)) {
                                    livingEntity.addEffect(statusEffectInstance2, entity2);
                                }
                            }
                        }

                        //To add toxicity to players
                        if (livingEntity instanceof final Player player)
                            player.tcots$addToxicity(toxicity, false);
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

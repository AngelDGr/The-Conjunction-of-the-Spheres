package TCOTS.mixin;

import TCOTS.potions.WitcherPotionsSplash_Base;
import TCOTS.potions.WitcherPotions_Base;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(PotionEntity.class)
public class PotionEntityMixin extends ThrownItemEntity implements FlyingItemEntity {

    public PotionEntityMixin(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onColissionWitcherPotion(HitResult hitResult, CallbackInfo ci){

        PotionEntity thisObject = (PotionEntity)(Object)this;

        if (!thisObject.getWorld().isClient) {

            ItemStack itemStack = thisObject.getStack();
            if(itemStack.getItem() instanceof WitcherPotionsSplash_Base){

                List<StatusEffectInstance> statusEffectList = ((WitcherPotionsSplash_Base) itemStack.getItem()).getPotionEffects();

                this.applySplashWitcherPotion(statusEffectList, hitResult.getType() == net.minecraft.util.hit.HitResult.Type.ENTITY ? ((EntityHitResult)hitResult).getEntity() : null);

                int i = ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffectType().isInstant() ? 2007 : 2002;
                this.getWorld().syncWorldEvent(i, this.getBlockPos(), ((WitcherPotionsSplash_Base) itemStack.getItem()).getStatusEffect().getEffectType().getColor());
                this.discard();
                ci.cancel();
            }

        }

    }

    @Unique
    private void applySplashWitcherPotion(List<StatusEffectInstance> statusEffects, Entity entity) {
        Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);
        PotionEntity thisObject = (PotionEntity)(Object)this;
        List<LivingEntity> list = thisObject.getWorld().getNonSpectatingEntities(LivingEntity.class, box);


        if (!list.isEmpty()) {
            Entity entity2 = thisObject.getEffectCause();
            Iterator var6 = list.iterator();

            while(true) {
                LivingEntity livingEntity;
                double d;
                do {
                    do {
                        if (!var6.hasNext()) {
                            return;
                        }

                        livingEntity = (LivingEntity)var6.next();
                    } while(!livingEntity.isAffectedBySplashPotions());

                    d = this.squaredDistanceTo(livingEntity);
                } while(!(d < 16.0));

                double e;
                if (livingEntity == entity) {
                    e = 1.0;
                } else {
                    e = 1.0 - Math.sqrt(d) / 4.0;
                }

                Iterator var12 = statusEffects.iterator();

                while(var12.hasNext()) {
                    StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var12.next();
                    StatusEffect statusEffect = statusEffectInstance.getEffectType();
                    if (statusEffect.isInstant()) {
                        statusEffect.applyInstantEffect(this, this.getOwner(), livingEntity, statusEffectInstance.getAmplifier(), e);
                    } else {
                        int i = statusEffectInstance.mapDuration((ix) -> {
                            return (int)(e * (double)ix + 0.5);
                        });
                        StatusEffectInstance statusEffectInstance2 = new StatusEffectInstance(statusEffect, i, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles());
                        if (!statusEffectInstance2.isDurationBelow(20)) {
                            livingEntity.addStatusEffect(statusEffectInstance2, entity2);
                        }
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

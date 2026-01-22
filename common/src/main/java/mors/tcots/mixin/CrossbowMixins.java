package mors.tcots.mixin;

import mors.tcots.entity.misc.ScurverSpineEntity;
import mors.tcots.entity.misc.bolts.WitcherBolt;
import mors.tcots.items.weapons.BoltItem;
import mors.tcots.items.weapons.WitcherBaseCrossbow;
import mors.tcots.registry.TCOTS_Items;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.function.Predicate;

public class CrossbowMixins {
    @Mixin(PlayerRenderer.class)
    public static class PlayerEntityRendererMixin {
        @Inject(method = "getArmPose", at = @At("TAIL"), cancellable = true)
        private static void tcots$injectKnightCrossbowPose(final AbstractClientPlayer player, final InteractionHand hand, final CallbackInfoReturnable<HumanoidModel.ArmPose> cir){
            final ItemStack itemStack = player.getItemInHand(hand);
            if (!player.swinging && (itemStack.getItem() instanceof WitcherBaseCrossbow) && CrossbowItem.isCharged(itemStack)) {
                cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
            }
        }
    }

    @Mixin(ItemInHandRenderer.class)
    public static class HeldItemRendererMixin {
        @Inject(method = "selectionUsingItemWhileHoldingBowLike", at = @At("TAIL"), cancellable = true)
        private static void tcots$injectKnightCrossbowHandRenderer(final LocalPlayer player, final CallbackInfoReturnable<ItemInHandRenderer.HandRenderSelection> cir){
            final ItemStack itemStack = player.getUseItem();
            final InteractionHand hand = player.getUsedItemHand();
            if (itemStack.getItem() instanceof WitcherBaseCrossbow) {
                cir.setReturnValue(ItemInHandRenderer.HandRenderSelection.onlyForHand(hand));
            }
        }

        @Inject(method = "isChargedCrossbow", at = @At("TAIL"), cancellable = true)
        private static void tcots$injectKnightCrossbowCharged(final ItemStack stack, final CallbackInfoReturnable<Boolean> cir){
            cir.setReturnValue((stack.getItem() instanceof WitcherBaseCrossbow) && CrossbowItem.isCharged(stack));
        }
    }

    @Mixin(CrossbowItem.class)
    public static class CrossbowItemMixin {
        @Inject(method = "getChargeDuration", at = @At("HEAD"), cancellable = true)
        private static void tcots$getPullTimeCorrectlyForAnimation(final ItemStack stack, final LivingEntity user, final CallbackInfoReturnable<Integer> cir){
            if(stack.getItem() instanceof final WitcherBaseCrossbow crossbow){
                cir.setReturnValue(crossbow.getCrossbowPullTime(stack, user));
            }
        }

        @Unique
        private static final Predicate<ItemStack> CROSSBOW_BOLTS = stack -> stack.is(TCOTS_Items.BASE_BOLT.get())      ||
                stack.is(TCOTS_Items.BLUNT_BOLT.get())     ||
                stack.is(TCOTS_Items.PRECISION_BOLT.get()) ||
                stack.is(TCOTS_Items.EXPLODING_BOLT.get()) ||
                stack.is(TCOTS_Items.BROADHEAD_BOLT.get());

        @Inject(method = "getAllSupportedProjectiles()Ljava/util/function/Predicate;", at = @At("RETURN"), cancellable = true)
        private void tcots$insertCrossbowProjectiles(final CallbackInfoReturnable<Predicate<ItemStack>> cir){
            cir.setReturnValue(cir.getReturnValue().or(CROSSBOW_BOLTS));
        }

        @Inject(method = "getSupportedHeldProjectiles()Ljava/util/function/Predicate;", at = @At("RETURN"), cancellable = true)
        private void tcots$insertCrossbowHeldProjectiles(final CallbackInfoReturnable<Predicate<ItemStack>> cir){
            cir.setReturnValue(cir.getReturnValue().or(CROSSBOW_BOLTS));
        }

        @Inject(method = "createProjectile", at = @At("RETURN"), cancellable = true)
        private void tcots$injectExtraPiercing(final Level world, final LivingEntity shooter, final ItemStack weaponStack, final ItemStack arrow, final boolean critical, final CallbackInfoReturnable<Projectile> cir){
            final boolean precisionBolt = arrow.getItem() instanceof final BoltItem bolt && Objects.equals(bolt.getId(), "precision_bolt");
            if(precisionBolt) {
                if(cir.getReturnValue() instanceof final AbstractArrow persistentProjectileEntity){
                    persistentProjectileEntity.setPierceLevel((byte) (persistentProjectileEntity.getPierceLevel() + 2));
                    cir.setReturnValue(persistentProjectileEntity);
                }

            }
        }
    }

    @Mixin(AbstractArrow.class)
    public static class PersistentProjectileEntityMixin {

        @Unique
        AbstractArrow tcots$THIS = (AbstractArrow)(Object)this;

        @WrapWithCondition(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setArrowCount(I)V"))
        private boolean tcots$dontStuckArrows(final LivingEntity instance, final int stuckArrowCount, @Local final LivingEntity entity){
            return !(tcots$THIS instanceof WitcherBolt) && !(tcots$THIS instanceof ScurverSpineEntity);
        }

    }
}

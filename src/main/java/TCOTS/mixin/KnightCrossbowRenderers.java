package TCOTS.mixin;

import TCOTS.items.TCOTS_Items;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class KnightCrossbowRenderers {
    @Mixin(PlayerEntityRenderer.class)
    public static class PlayerEntityRendererMixin {
        @Inject(method = "getArmPose", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir){
            ItemStack itemStack = player.getStackInHand(hand);
            if (!player.handSwinging && itemStack.isOf(TCOTS_Items.KNIGHT_CROSSBOW) && CrossbowItem.isCharged(itemStack)) {
                cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_HOLD);
            }
        }
    }

    @Mixin(HeldItemRenderer.class)
    public static class HeldItemRendererMixin {
        @Inject(method = "getUsingItemHandRenderType", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowHandRenderer(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir){
            ItemStack itemStack = player.getActiveItem();
            Hand hand = player.getActiveHand();
            if (itemStack.isOf(TCOTS_Items.KNIGHT_CROSSBOW)) {
                cir.setReturnValue(HeldItemRenderer.HandRenderType.shouldOnlyRender(hand));
            }
        }

        @Inject(method = "isChargedCrossbow", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowCharged(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
            cir.setReturnValue(stack.isOf(TCOTS_Items.KNIGHT_CROSSBOW) && CrossbowItem.isCharged(stack));
        }

        @Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 1))
        private boolean injectCrossbowFirstPerson(ItemStack stack, Item item) {
            return stack.isOf(TCOTS_Items.KNIGHT_CROSSBOW) || stack.isOf(item);
        }
    }
}

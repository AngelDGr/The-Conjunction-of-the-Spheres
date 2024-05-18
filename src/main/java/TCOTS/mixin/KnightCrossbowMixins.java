package TCOTS.mixin;

import TCOTS.items.TCOTS_Items;
import TCOTS.items.weapons.KnightCrossbow;
import TCOTS.items.weapons.WitcherBaseCrossbow;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

public class KnightCrossbowMixins {
    @Mixin(PlayerEntityRenderer.class)
    public static class PlayerEntityRendererMixin {
        @Inject(method = "getArmPose", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir){
            ItemStack itemStack = player.getStackInHand(hand);
            if (!player.handSwinging && (itemStack.getItem() instanceof WitcherBaseCrossbow) && CrossbowItem.isCharged(itemStack)) {
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
            if (itemStack.getItem() instanceof WitcherBaseCrossbow) {
                cir.setReturnValue(HeldItemRenderer.HandRenderType.shouldOnlyRender(hand));
            }
        }

        @Inject(method = "isChargedCrossbow", at = @At("TAIL"), cancellable = true)
        private static void injectKnightCrossbowCharged(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
            cir.setReturnValue((stack.getItem() instanceof WitcherBaseCrossbow) && CrossbowItem.isCharged(stack));
        }

        @Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 1))
        private boolean injectCrossbowFirstPerson(ItemStack stack, Item item) {
            return (stack.getItem() instanceof WitcherBaseCrossbow) || stack.isOf(item);
        }
    }

    @Mixin(CrossbowItem.class)
    public static class CrossbowItemMixin {
        @Inject(method = "getPullTime", at = @At("HEAD"), cancellable = true)
        private static void getPullTimeCorrectly(ItemStack stack, CallbackInfoReturnable<Integer> cir){
            if(stack.isOf(TCOTS_Items.KNIGHT_CROSSBOW)){
                cir.setReturnValue(KnightCrossbow.getPullTime(stack));
            }
        }

        @Unique
        private static final Predicate<ItemStack> CROSSBOW_BOLTS = stack -> stack.isOf(TCOTS_Items.BASE_BOLT)      ||
                                                                            stack.isOf(TCOTS_Items.BLUNT_BOLT)     ||
                                                                            stack.isOf(TCOTS_Items.PRECISION_BOLT) ||
                                                                            stack.isOf(TCOTS_Items.EXPLODING_BOLT) ||
                                                                            stack.isOf(TCOTS_Items.BROADHEAD_BOLT);

        @Inject(method = "getProjectiles()Ljava/util/function/Predicate;", at = @At("RETURN"), cancellable = true)
        private void insertCrossbowProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cir){
            cir.setReturnValue(cir.getReturnValue().or(CROSSBOW_BOLTS));
        }

        @Inject(method = "getHeldProjectiles", at = @At("RETURN"), cancellable = true)
        private void insertCrossbowHeldProjectiles(CallbackInfoReturnable<Predicate<ItemStack>> cir){
            cir.setReturnValue(cir.getReturnValue().or(CROSSBOW_BOLTS));
        }
    }
}

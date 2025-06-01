package fabric.TCOTS.mixin;

import TCOTS.items.weapons.WitcherBaseCrossbow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CrossbowMixins {

    @Mixin(ItemInHandRenderer.class)
    public static class HeldItemRendererMixin {

        @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 1))
        private boolean injectCrossbowFirstPerson(ItemStack stack, Item item) {
            return (stack.getItem() instanceof WitcherBaseCrossbow) || stack.is(item);
        }
    }

}

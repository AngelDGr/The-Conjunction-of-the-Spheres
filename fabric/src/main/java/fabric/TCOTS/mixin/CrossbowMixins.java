package fabric.TCOTS.mixin;

import TCOTS.items.weapons.WitcherBaseCrossbow;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;

public class CrossbowMixins {

    @Mixin(ItemInHandRenderer.class)
    public static class HeldItemRendererMixin {

        @ModifyExpressionValue(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 1))
        private boolean injectCrossbowFirstPerson(final boolean original, @Local(argsOnly = true) final ItemStack stack) {
            return original || (stack.getItem() instanceof WitcherBaseCrossbow);
        }
    }

}

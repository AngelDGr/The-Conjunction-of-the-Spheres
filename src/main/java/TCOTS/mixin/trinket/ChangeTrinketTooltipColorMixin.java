package TCOTS.mixin.trinket;

import TCOTS.entity.TCOTS_EntityAttributes;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(value = ItemStack.class, priority = 500000)
public class ChangeTrinketTooltipColorMixin {

    @TargetHandler(
            mixin = "dev.emi.trinkets.mixin.ItemStackMixin",
            name = "addAttributes"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE"
                    ,target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
                    ordinal = 0))
    private Formatting injectColorChange(Formatting formatting,
                                          @Local Map.Entry<EntityAttribute, EntityAttributeModifier> entry){

        if(entry.getKey().equals(TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY)){
            return Formatting.DARK_GREEN;
        }

        return formatting;
    }
}

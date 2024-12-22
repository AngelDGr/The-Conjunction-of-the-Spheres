package TCOTS.mixin.trinket;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(value = ItemStack.class, priority = 500000)
public class ChangeTrinketTooltipColorMixin {

    @TargetHandler(
            mixin = "dev.emi.trinkets.mixin.ItemStackMixin",
            name = "addAttributes"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE"
                    ,target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
                    ordinal = 0))
    private MutableText injectColorChange(MutableText instance, Formatting formatting,
                                          @Local Map.Entry<RegistryEntry<EntityAttribute>, EntityAttributeModifier> entry){

        if(entry.getKey() == Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(Identifier.of("tcots-witcher","generic.witcher_toxicity")))){
            return instance.formatted(Formatting.DARK_GREEN);
        }

        return instance.formatted(formatting);
    }
}

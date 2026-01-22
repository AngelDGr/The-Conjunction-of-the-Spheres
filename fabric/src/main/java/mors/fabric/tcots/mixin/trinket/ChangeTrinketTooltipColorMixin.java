package mors.fabric.tcots.mixin.trinket;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import mors.tcots.registry.TCOTS_EntityAttributes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
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
                    ,target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;",
                    ordinal = 0))
    private ChatFormatting tcots$attributeColorChange(final ChatFormatting formatting,
                                             @Local final Map.Entry<Holder<Attribute>, AttributeModifier> entry){

        if(entry.getKey() == TCOTS_EntityAttributes.WITCHER_MAX_TOXICITY)
            return ChatFormatting.DARK_GREEN;

        return formatting;
    }
}

package fabric.TCOTS.mixin.trinket;

import TCOTS.TCOTS_Main;
import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

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
    private ChatFormatting injectColorChange(ChatFormatting formatting,
                                             @Local Map.Entry<Holder<Attribute>, AttributeModifier> entry){

        if(entry.getKey() == BuiltInRegistries.ATTRIBUTE.wrapAsHolder(BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"generic.witcher_toxicity")))){
            return ChatFormatting.DARK_GREEN;
        }

        return formatting;
    }
}

package neoforge.TCOTS.mixin;

import TCOTS.entity.TCOTS_EntityAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.extensions.IAttributeExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(IAttributeExtension.class)
public interface IAttributeExtensionMixin {

    @ModifyArg(method = "toComponent", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 0))
    private ChatFormatting manticoreAttributeMaxToxicityColorNeo(final ChatFormatting formatting, @Local final Attribute attribute){
        if(attribute == TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY.value()){
            return ChatFormatting.DARK_GREEN;
        }

        return formatting;
    }
}

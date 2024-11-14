package TCOTS.items.components;

import TCOTS.items.TCOTS_Items;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;

import java.util.List;

public record CustomEffectsComponent(List<StatusEffectInstance> customEffects) {
    public static final CustomEffectsComponent DEFAULT = new CustomEffectsComponent(List.of());


    public static final Codec<CustomEffectsComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    StatusEffectInstance.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(CustomEffectsComponent::customEffects)
                    )
                    .apply(instance, CustomEffectsComponent::new)
    );

    public static ItemStack of(ItemStack stack, List<StatusEffectInstance> customEffects){
        stack.set(TCOTS_Items.CUSTOM_EFFECTS_COMPONENT, new CustomEffectsComponent(customEffects));

        return stack;
    }
}

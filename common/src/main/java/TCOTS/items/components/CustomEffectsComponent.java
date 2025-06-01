package TCOTS.items.components;

import TCOTS.registry.TCOTS_Items;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

public record CustomEffectsComponent(List<MobEffectInstance> customEffects) {
    public static final CustomEffectsComponent DEFAULT = new CustomEffectsComponent(List.of());


    public static final Codec<CustomEffectsComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    MobEffectInstance.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(CustomEffectsComponent::customEffects)
                    )
                    .apply(instance, CustomEffectsComponent::new)
    );

    public static ItemStack of(ItemStack stack, List<MobEffectInstance> customEffects){
        stack.set(TCOTS_Items.CustomEffects(), new CustomEffectsComponent(customEffects));

        return stack;
    }
}

package TCOTS.items.components;

import TCOTS.registry.TCOTS_Items;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record RecipeTeacherComponent(String recipeName, boolean isDecoction) {

        public static final Codec<RecipeTeacherComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.STRING.fieldOf("recipeName").forGetter(RecipeTeacherComponent::recipeName),
                            Codec.BOOL.fieldOf("isDecoction").forGetter(RecipeTeacherComponent::isDecoction)
                    )
                    .apply(instance, RecipeTeacherComponent::new)
    );

    public static ItemStack set(final ItemStack stack, final String recipeName, final boolean isDecoction){
        stack.set(TCOTS_Items.RecipeTeacher(), new RecipeTeacherComponent(recipeName, isDecoction));

        return stack;
    }

    public static ItemStack set(final ItemStack stack, final String recipeName){
        return set(stack, recipeName, false);
    }

}

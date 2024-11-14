package TCOTS.items.components;

import TCOTS.items.TCOTS_Items;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

public record RecipeTeacherComponent(String recipeName, boolean isDecoction) {

        public static final Codec<RecipeTeacherComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.STRING.fieldOf("recipeName").forGetter(RecipeTeacherComponent::recipeName),
                            Codec.BOOL.fieldOf("isDecoction").forGetter(RecipeTeacherComponent::isDecoction)
                    )
                    .apply(instance, RecipeTeacherComponent::new)
    );

    public static ItemStack set(ItemStack stack, String recipeName, boolean isDecoction){
        stack.set(TCOTS_Items.RECIPE_TEACHER_COMPONENT, new RecipeTeacherComponent(recipeName, isDecoction));

        return stack;
    }

    public static ItemStack set(ItemStack stack, String recipeName){
        return set(stack, recipeName, false);
    }

}

package TCOTS.potions;

import net.minecraft.item.Item;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;

public class WitcherPotions_CustomBrewingRecipe<T> extends BrewingRecipeRegistry.Recipe<T> {

    public final T input;
    public final Ingredient ingredient;
    public final Item output;

    public WitcherPotions_CustomBrewingRecipe(T input, Ingredient ingredient, Item output) {
        super(input, ingredient, input);

        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

}

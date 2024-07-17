package TCOTS.items.concoctions.recipes;

import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AlchemyTableRecipeJsonBuilder {
    private final AlchemyTableRecipeCategory category;
    private final float order;
    private final List<Ingredient> ingredients;
    private final List<Integer> ingredientCount;
    private final ItemStack base;
    private final ItemStack output;

    public AlchemyTableRecipeJsonBuilder(float order, AlchemyTableRecipeCategory category, List<Ingredient> ingredients, List<Integer> ingredientCount, ItemStack base, ItemStack output) {
        this.order = order;
        this.category = category;
        this.ingredients = ingredients;
        this.ingredientCount = ingredientCount;
        this.base = base;
        this.output = output;
    }

    public static AlchemyTableRecipeJsonBuilder create(float order, AlchemyTableRecipeCategory category, List<ItemStack> ingredients, Item base, Item output) {
        List<Integer> integerList = new ArrayList<>();

        List<Ingredient> ingredientList = new ArrayList<>();

        for(ItemStack ing: ingredients){
            integerList.add(ing.getCount());

            ingredientList.add(Ingredient.ofItems(ing.getItem()));
        }

        return new AlchemyTableRecipeJsonBuilder(order, category, ingredientList, integerList, base.getDefaultStack(), output.getDefaultStack());
    }

    public static AlchemyTableRecipeJsonBuilder createPotion(float order, List<ItemStack> ingredients, Item base, Item output) {
        return create(order, AlchemyTableRecipeCategory.POTIONS, ingredients, base, output);
    }


    public void offerTo(RecipeExporter exporter, Identifier recipeId) {
        AlchemyTableRecipe alchemyTableRecipe = new AlchemyTableRecipe(this.order, this.category, this.ingredients, this.ingredientCount, this.base, this.output);

        exporter.accept(recipeId, alchemyTableRecipe, null);
    }

    public void offerTo(RecipeExporter exporter) {
        AlchemyTableRecipe alchemyTableRecipe = new AlchemyTableRecipe(this.order, this.category, this.ingredients, this.ingredientCount, this.base, this.output);

        exporter.accept(Registries.ITEM.getId(output.getItem()), alchemyTableRecipe, null);
    }
}

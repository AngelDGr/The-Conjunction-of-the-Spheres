package mors.tcots.recipes.dynamic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class RecipeAdvancementJsonBuilder {

    private final String hasCondition;
    private final ResourceLocation recipeId;
    private final ResourceLocation ingredientId;

    public RecipeAdvancementJsonBuilder(final String hasCondition, final ResourceLocation recipeId, final ResourceLocation ingredientId) {
        this.hasCondition=hasCondition;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
    }

    public JsonObject export() {
        final JsonObject root = new JsonObject();

        // parent
        root.addProperty("parent", "minecraft:recipes/root");

        // criteria
        final JsonObject criteria = new JsonObject();

        // has_cured_monster_leather
        final JsonObject hasItem = new JsonObject();
        hasItem.addProperty("trigger", "minecraft:inventory_changed");

        final JsonObject hasItemConditions = new JsonObject();
        final JsonArray itemsArray = new JsonArray();

        final JsonObject itemEntry = new JsonObject();
        itemEntry.addProperty("items", ingredientId.toString());
        itemsArray.add(itemEntry);

        hasItemConditions.add("items", itemsArray);
        hasItem.add("conditions", hasItemConditions);

        criteria.add(hasCondition, hasItem);

        // has_the_recipe
        final JsonObject hasRecipe = new JsonObject();
        hasRecipe.addProperty("trigger", "minecraft:recipe_unlocked");

        final JsonObject hasRecipeConditions = new JsonObject();
        hasRecipeConditions.addProperty("recipe", recipeId.toString());
        hasRecipe.add("conditions", hasRecipeConditions);

        criteria.add("has_the_recipe", hasRecipe);

        root.add("criteria", criteria);

        // requirements
        final JsonArray requirements = new JsonArray();
        final JsonArray requirementGroup = new JsonArray();
        requirementGroup.add("has_the_recipe");
        requirementGroup.add(hasCondition);
        requirements.add(requirementGroup);

        root.add("requirements", requirements);

        // rewards
        final JsonObject rewards = new JsonObject();
        final JsonArray recipes = new JsonArray();
        recipes.add(recipeId.toString());
        rewards.add("recipes", recipes);

        root.add("rewards", rewards);

        return root;
    }
}

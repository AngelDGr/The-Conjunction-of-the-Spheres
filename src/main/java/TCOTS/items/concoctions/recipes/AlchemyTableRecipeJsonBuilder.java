package TCOTS.items.concoctions.recipes;

import TCOTS.TCOTS_Main;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.WitcherBombs_Base;
import TCOTS.items.concoctions.WitcherMonsterOil_Base;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

    public static AlchemyTableRecipeJsonBuilder create(float order, Item output, AlchemyTableRecipeCategory category, List<ItemStack> ingredients, ItemStack base) {
        List<Integer> integerList = new ArrayList<>();

        List<Ingredient> ingredientList = new ArrayList<>();

        for(ItemStack ing: ingredients){
            integerList.add(ing.getCount());

            ingredientList.add(Ingredient.ofItems(ing.getItem()));
        }

        return new AlchemyTableRecipeJsonBuilder(order, category, ingredientList, integerList, base, output.getDefaultStack());
    }

    /**
    Basic recipe creation methods
     */
    public static AlchemyTableRecipeJsonBuilder create(float order, ItemStack output, AlchemyTableRecipeCategory category, List<ItemStack> ingredients, Item base) {
        List<Integer> integerList = new ArrayList<>();

        List<Ingredient> ingredientList = new ArrayList<>();

        for(ItemStack ing: ingredients){
            integerList.add(ing.getCount());

            ingredientList.add(Ingredient.ofItems(ing.getItem()));
        }

        return new AlchemyTableRecipeJsonBuilder(order, category, ingredientList, integerList, base.getDefaultStack(), output);
    }

    public static AlchemyTableRecipeJsonBuilder create(float order, Item output, AlchemyTableRecipeCategory category, List<ItemStack> ingredients, Item base) {
        return create(order, output.getDefaultStack(), category, ingredients, base);
    }

    /**
    Just creates a recipe with the category misc
     */
    public static AlchemyTableRecipeJsonBuilder createMisc(float order, ItemStack output, List<ItemStack> ingredients, Item base){
     return create(order,output, AlchemyTableRecipeCategory.MISC, ingredients, base);
    }

    /**
    Creates a recipe substance-style (With 2 items as output and using as base White Gull)
     */
    public static AlchemyTableRecipeJsonBuilder createMisc(float order, Item output, List<ItemStack> ingredients){
        return create(order, new ItemStack(output,2), AlchemyTableRecipeCategory.MISC, ingredients, TCOTS_Items.WHITE_GULL);
    }

    /**
      Create a basic bomb, using one Gunpowder as base
     */
    public static AlchemyTableRecipeJsonBuilder createBomb(float order, Item output, List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, Items.GUNPOWDER);
    }

    /**
    Create a basic oil, using one honeycomb as base
     */
    public static AlchemyTableRecipeJsonBuilder createOil(float order, Item output, List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, Items.HONEYCOMB);
    }

    /**
     Creates an upgraded oil, depending on the oil level it add as first ingredient Monster Fat or Alchemy Paste
     */
    public static AlchemyTableRecipeJsonBuilder createOil(float order, Item output, List<ItemStack> ingredients, int countSpecial, Item base) {
        int level = 0;
        if(output instanceof WitcherMonsterOil_Base bomb){
            level = bomb.getLevel() - 1;
        }

        return createDecoctionWithLevel(order, output, level, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, base, new ItemStack(TCOTS_Items.MONSTER_FAT, countSpecial), new ItemStack(TCOTS_Items.ALCHEMY_PASTE, countSpecial));
    }

    /**
    Creates an upgraded bomb,  depending on the bomb level it add as first ingredient Stammelford's Dust or Alchemists' Powder
     */
    public static AlchemyTableRecipeJsonBuilder createBomb(float order, Item output, List<ItemStack> ingredients, Item base) {
        int level = 0;
        if(output instanceof WitcherBombs_Base bomb){
            level = bomb.getLevel();
        }

        return createDecoctionWithLevel(order, output, level, AlchemyTableRecipeCategory.BOMBS_OILS, ingredients, base, TCOTS_Items.STAMMELFORDS_DUST, TCOTS_Items.ALCHEMISTS_POWDER);
    }

    /**
    Create a basic potion, using a Dwarven Spirit as base
     */
    public static AlchemyTableRecipeJsonBuilder createPotion(float order, Item output, List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.POTIONS, ingredients, TCOTS_Items.DWARVEN_SPIRIT);
    }

    /**
    Creates a basic decoction potion
     */
    public static AlchemyTableRecipeJsonBuilder createDecoction(float order, Item output, List<ItemStack> ingredients) {
        return create(order, output, AlchemyTableRecipeCategory.DECOCTIONS, ingredients, TCOTS_Items.DWARVEN_SPIRIT);
    }

    /**
    Creates an upgraded potion, depending on the level it add as first ingredient Alcohest or White Gull
     */
    public static AlchemyTableRecipeJsonBuilder createPotion(float order, Item output, int level, List<ItemStack> ingredients, Item base) {
        return createDecoctionWithLevel(order, output, level, AlchemyTableRecipeCategory.POTIONS, ingredients, base, TCOTS_Items.ALCOHEST, TCOTS_Items.WHITE_GULL);
    }

    /**
    Creates a decoction, using the level value to add a main, first ingredient
     */
    public static AlchemyTableRecipeJsonBuilder createDecoctionWithLevel(float order, Item output, int level, AlchemyTableRecipeCategory category, List<ItemStack> ingredients, Item base, Item firstLevelMain, Item secondLevelMain) {
        return createDecoctionWithLevel(order, output, level, category, ingredients, base, firstLevelMain.getDefaultStack(), secondLevelMain.getDefaultStack());
    }

    public static AlchemyTableRecipeJsonBuilder createDecoctionWithLevel(float order, Item output, int level, AlchemyTableRecipeCategory category, List<ItemStack> ingredients, Item base, ItemStack firstLevelMain, ItemStack secondLevelMain) {
        if(level == 1){
            List<ItemStack> enhancedPotion = new ArrayList<>();
            enhancedPotion.add(firstLevelMain);
            enhancedPotion.addAll(ingredients);
            return create(order, output, category, enhancedPotion, base);
        } else if (level == 2){
            List<ItemStack> superiorPotion = new ArrayList<>();
            superiorPotion.add(secondLevelMain);
            superiorPotion.addAll(ingredients);
            return create(order, output, category, superiorPotion, base);
        } else {
            return create(order, output, category, ingredients, base);
        }
    }


    /**
    Creates a basic splash potion, just putting one gunpowder as unique ingredient
     */
    public static AlchemyTableRecipeJsonBuilder createPotionSplash(float order, Item output, Item base) {
        return create(order, output, AlchemyTableRecipeCategory.POTIONS, List.of(new ItemStack(Items.GUNPOWDER)), base);
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter) {

        Identifier recipeID=new Identifier(TCOTS_Main.MOD_ID, Registries.ITEM.getId(this.output.getItem()).getPath());


        AlchemyTableRecipeJsonProvider alchemyTableRecipe = new AlchemyTableRecipeJsonProvider(
                recipeID,
                this.order,
                this.category.asString(),
                this.ingredients,
                this.ingredientCount,
                this.base.getItem(),
                this.base.getCount(),
                this.output.getItem(),
                this.output.getCount());

        exporter.accept(alchemyTableRecipe);
    }

    public static class AlchemyTableRecipeJsonProvider implements RecipeJsonProvider {
        private final float order;
        private final List<Ingredient> ingredients;
        private final List<Integer> ingredientCount;
        private final Item base;
        private final int baseCount;
        private final Identifier recipeId;
        private final String category;
        private final Item output;
        private final int count;

        public AlchemyTableRecipeJsonProvider(
                Identifier recipeId,

                float order,
                String category,
                List<Ingredient> ingredients,
                List<Integer> ingredientCount,
                Item base,
                int baseCount,
                Item output,
                int outputCount
        ) {
            this.recipeId = recipeId;

            this.order=order;
            this.category = category;
            this.ingredients=ingredients;
            this.ingredientCount=ingredientCount;
            this.base=base;
            this.baseCount=baseCount;

            this.output = output;
            this.count = outputCount;
        }

        @Override
        public void serialize(JsonObject json) {
            //Order
            if (this.order!=0) {
                json.addProperty("order", this.order);
            } else {
                json.addProperty("order", 99);
            }

            //Category
            if (!this.category.isEmpty()) {
                json.addProperty("category", this.category);
            }

            //Ingredients
            JsonArray jsonArrayIngredients = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                jsonArrayIngredients.add(ingredient.toJson());
            }
            json.add("ingredients", jsonArrayIngredients);


            //Ingredients count
            JsonArray jsonArrayIngredientsCount = new JsonArray();
            for (int count : this.ingredientCount) {
                jsonArrayIngredientsCount.add(count);
            }
            json.add("ingredient_counters", jsonArrayIngredientsCount);

            //Base
            JsonObject jsonObjectBase = new JsonObject();
            jsonObjectBase.addProperty("item", Registries.ITEM.getId(this.base).toString());
            if (this.baseCount > 1) {
                jsonObjectBase.addProperty("count", this.baseCount);
            }
            json.add("base", jsonObjectBase);

            //Output
            JsonObject jsonObjectOutput = new JsonObject();
            jsonObjectOutput.addProperty("item", Registries.ITEM.getId(this.output).toString());
            if (this.count > 1) {
                jsonObjectOutput.addProperty("count", this.count);
            }
            json.add("result", jsonObjectOutput);
        }

        @Override
        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return AlchemyTableRecipe.Serializer.INSTANCE;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return null;
        }
    }

}

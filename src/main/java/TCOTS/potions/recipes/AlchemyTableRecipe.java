package TCOTS.potions.recipes;

import com.google.common.collect.Lists;
import com.google.gson.*;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlchemyTableRecipe implements Recipe<SimpleInventory>, Comparable {
    private final ItemStack output;
    private final ItemStack base;
    private final List<Ingredient> recipeItems;
    private final int[] recipeCounts;
    private final Identifier id;
    public static final String ID_STRING = "alchemy_table";

    private final AlchemyTableRecipeCategory category;

    public AlchemyTableRecipe(Identifier id, List<Ingredient> ingredients, int[] IngredientCount, ItemStack base, ItemStack output, AlchemyTableRecipeCategory category) {
        this.output = output;
        this.recipeItems = ingredients;
        this.id = id;
        this.base = base;
        this.recipeCounts= IngredientCount;
        this.category = category;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        // Obtener el hash code de tu objeto y del objeto pasado como argumento
        int hashCodeThis = this.getId().hashCode();
        int hashCodeOther = o.hashCode();

        // Comparar los hash codes
        return Integer.compare(hashCodeThis, hashCodeOther);
    }

    public static class Type implements RecipeType<AlchemyTableRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "alchemy_table";
    }
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }


    //If a given inventory satisfies a recipe's input.
    @Override
    public boolean matches(SimpleInventory recipeInputInventory, World world) {
        if(world.isClient()) {
            return false;
        }
        Ingredient Ing1;
        Ingredient Ing2;
        Ingredient Ing3;
        Ingredient Ing4;
        Ingredient Ing5;

        Item Item1;
        Item Item2;
        Item Item3;
        Item Item4;
        Item Item5;

        Item BaseItem = getBaseItem().getItem();

        switch (getIngredients().size()){
            case 1:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStack(0).getItem()) &&
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts()[0] &&
                        recipeInputInventory.getStack(1).isEmpty() &&
                        recipeInputInventory.getStack(2).isEmpty() &&
                        recipeInputInventory.getStack(3).isEmpty() &&
                        recipeInputInventory.getStack(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStack(5).getItem())
                        && recipeInputInventory.getStack(5).getCount() == getBaseItem().getCount();
            case 2:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStack(0).getItem()) &&
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts()[0] &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts()[1] &&
                        recipeInputInventory.getStack(2).isEmpty() &&
                        recipeInputInventory.getStack(3).isEmpty() &&
                        recipeInputInventory.getStack(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStack(5).getItem())
                        && recipeInputInventory.getStack(5).getCount() == getBaseItem().getCount();
            case 3:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getMatchingStacks()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStack(0).getItem()) &&
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts()[0] &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts()[1] &&
                        Item3.equals(recipeInputInventory.getStack(2).getItem()) &&
                        recipeInputInventory.getStack(2).getCount() == getIngredientsCounts()[2] &&
                        recipeInputInventory.getStack(3).isEmpty() &&
                        recipeInputInventory.getStack(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStack(5).getItem())
                        && recipeInputInventory.getStack(5).getCount() == getBaseItem().getCount();
            case 4:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getMatchingStacks()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getMatchingStacks()[0].getItem();
                Ing4 = getIngredients().get(3);
                Item4 = Ing4.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStack(0).getItem()) &&
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts()[0] &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts()[1] &&
                        Item3.equals(recipeInputInventory.getStack(2).getItem()) &&
                        recipeInputInventory.getStack(2).getCount() == getIngredientsCounts()[2] &&
                        Item4.equals(recipeInputInventory.getStack(3).getItem()) &&
                        recipeInputInventory.getStack(3).getCount() == getIngredientsCounts()[3] &&
                        recipeInputInventory.getStack(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStack(5).getItem())
                        && recipeInputInventory.getStack(5).getCount() == getBaseItem().getCount();
            case 5:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getMatchingStacks()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getMatchingStacks()[0].getItem();
                Ing4 = getIngredients().get(3);
                Item4 = Ing4.getMatchingStacks()[0].getItem();
                Ing5 = getIngredients().get(4);
                Item5 = Ing5.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStack(0).getItem()) &&
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts()[0] &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts()[1] &&
                        Item3.equals(recipeInputInventory.getStack(2).getItem()) &&
                        recipeInputInventory.getStack(2).getCount() == getIngredientsCounts()[2] &&
                        Item4.equals(recipeInputInventory.getStack(3).getItem()) &&
                        recipeInputInventory.getStack(3).getCount() == getIngredientsCounts()[3] &&
                        Item5.equals(recipeInputInventory.getStack(4).getItem()) &&
                        recipeInputInventory.getStack(4).getCount() == getIngredientsCounts()[4] &&
                        BaseItem.equals(recipeInputInventory.getStack(5).getItem())
                        && recipeInputInventory.getStack(5).getCount() == getBaseItem().getCount();
        }


//        Item Item1 = Ing1.getMatchingStacks()[0].getItem();
//        Item Item2 = Ing2.getMatchingStacks()[0].getItem();
//        Item Item3 = Ing3.getMatchingStacks()[0].getItem();
//        Item Item4 = Ing4.getMatchingStacks()[0].getItem();
//        Item Item5 = Ing5.getMatchingStacks()[0].getItem();



//        if(
//                Item1.equals(recipeInputInventory.getStack(0).getItem()) &&
//                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts()[0] &&
//                Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
//                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts()[1] &&
//                Item3.equals(recipeInputInventory.getStack(2).getItem()) &&
//                        recipeInputInventory.getStack(2).getCount() == getIngredientsCounts()[2] &&
//                Item4.equals(recipeInputInventory.getStack(3).getItem()) &&
//                        recipeInputInventory.getStack(3).getCount() == getIngredientsCounts()[3] &&
//                Item5.equals(recipeInputInventory.getStack(4).getItem()) &&
//                        recipeInputInventory.getStack(4).getCount() == getIngredientsCounts()[4] &&
//                BaseItem.equals(recipeInputInventory.getStack(5).getItem())
//        ){
//            return true;
//        }
//        else{
//            return false;
//        }
        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return this.getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.recipeItems.size());
        list.addAll(recipeItems);
        return list;
    }

    public int[] getIngredientsCounts() {
        return this.recipeCounts;
    }

    public ItemStack getBaseItem() {
        return this.base;
    }

    public AlchemyTableRecipeCategory getCategory() {
        return this.category;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<AlchemyTableRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "alchemy_table";

        // Turns json into Recipe
        @Override
        public AlchemyTableRecipe read(Identifier identifier, JsonObject jsonObject) {
            DefaultedList<Ingredient> ingredientsList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            AlchemyTableRecipeCategory alchemyTableRecipeCategory = AlchemyTableRecipeCategory.CODEC.byId(JsonHelper.getString(jsonObject, "category", null), AlchemyTableRecipeCategory.MISC);

            int[] ingredientCounts = getIngredientsCounters(JsonHelper.getArray(jsonObject, "ingredient_counters"));

            if (ingredientsList.isEmpty()) {
                throw new JsonParseException("No ingredients for witcher potion recipe");
            }
            if (ingredientsList.size() > 5) {
                throw new JsonParseException("Too many ingredients for witcher potion recipe");
            }

            if (ingredientCounts.length > 5) {
                throw new JsonParseException("Too many ingredients counter for witcher potion recipe");
            }

            ItemStack base = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "base"));
            ItemStack result = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));

            return new AlchemyTableRecipe(identifier, ingredientsList, ingredientCounts, base, result, alchemyTableRecipeCategory);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i), true);
                if (ingredient.isEmpty()) continue;
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        private static int[] getIngredientsCounters(JsonArray json) {
            int[] defaultedList={1,1,1,1,1};
            for (int i = 0; i < json.size(); ++i) {
                defaultedList[i] = JsonHelper.getInt((JsonObject) json.get(i), "count");
            }
            return defaultedList;
        }

        // Turns Recipe into PacketByteBuf
        @Override
        public void write(PacketByteBuf buf, AlchemyTableRecipe recipe) {
            recipe.getIngredients().get(0).write(buf);
            recipe.getIngredients().get(1).write(buf);
            recipe.getIngredients().get(2).write(buf);
            recipe.getIngredients().get(3).write(buf);
            recipe.getIngredients().get(4).write(buf);

            buf.writeIntArray(recipe.getIngredientsCounts());

            buf.writeItemStack(recipe.getBaseItem());
            buf.writeItemStack(recipe.getOutput(null));
            buf.writeEnumConstant((recipe).getCategory());
        }


        // Turns PacketByteBuf into Recipe(InGame)
        @Override
        public AlchemyTableRecipe read(Identifier id, PacketByteBuf buf) {
            // Make sure the read in the same order you have written!
            List<Ingredient> ingredientList = Lists.newArrayList();
            Ingredient I1 = Ingredient.fromPacket(buf);
            Ingredient I2 = Ingredient.fromPacket(buf);
            Ingredient I3 = Ingredient.fromPacket(buf);
            Ingredient I4 = Ingredient.fromPacket(buf);
            Ingredient I5 = Ingredient.fromPacket(buf);
            ingredientList.add(I1);
            ingredientList.add(I2);
            ingredientList.add(I3);
            ingredientList.add(I4);
            ingredientList.add(I5);

            int[] inputsCount = buf.readIntArray();

            ItemStack base = buf.readItemStack();
            ItemStack output = buf.readItemStack();
            AlchemyTableRecipeCategory alchemyTableRecipeCategory = buf.readEnumConstant(AlchemyTableRecipeCategory.class);

            return new AlchemyTableRecipe(id, ingredientList, inputsCount, base, output, alchemyTableRecipeCategory);
        }
    }

}

package TCOTS.items.concoctions.recipes;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class AlchemyTableRecipe implements Recipe<SimpleInventory>, Comparable<AlchemyTableRecipe>{
    private final ItemStack output;
    private final ItemStack base;
    private final List<Ingredient> recipeItems;
    private final List<Integer> recipeCounts;
    private final float order;

    public static final String ID_STRING = "alchemy_table";

    public final AlchemyTableRecipeCategory category;

    public AlchemyTableRecipe(float order, AlchemyTableRecipeCategory category, List<Ingredient> ingredients, List<Integer> IngredientCount, ItemStack base, ItemStack output) {
        this.output = output;
        this.recipeItems = ingredients;
        this.order =order;
        this.base = base;
        this.recipeCounts= IngredientCount;
        this.category = category;
    }

    /**
     * Return all the ingredients of the recipe with the assigned count.
     */
    public List<ItemStack> returnItemStackWithQuantity(){
        List<ItemStack> list = new ArrayList<>();
        for(int i=0;i<getIngredients().size();i++){
            Item itemForStack = getIngredients().get(i).getMatchingStacks()[0].getItem();
            int count = getIngredientsCounts().get(i);

            ItemStack stack = new ItemStack(itemForStack, count);
            list.add(stack);
        }

        return list;
    }

    @Override
    public Identifier getId() {
        return new Identifier(TCOTS_Main.MOD_ID, Registries.ITEM.getId(this.output.getItem()).getPath());
    }

    @Override
    public int compareTo(@NotNull AlchemyTableRecipe o) {
        return Float.compare(this.getOrder(), o.getOrder());
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

    public float getOrder() {
        return this.order;
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
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts().get(0) &&
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
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts().get(1) &&
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
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getStack(2).getItem()) &&
                        recipeInputInventory.getStack(2).getCount() == getIngredientsCounts().get(2) &&
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
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getStack(2).getItem()) &&
                        recipeInputInventory.getStack(2).getCount() == getIngredientsCounts().get(2) &&
                        Item4.equals(recipeInputInventory.getStack(3).getItem()) &&
                        recipeInputInventory.getStack(3).getCount() == getIngredientsCounts().get(3) &&
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
                        recipeInputInventory.getStack(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStack(1).getItem()) &&
                        recipeInputInventory.getStack(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getStack(2).getItem()) &&
                        recipeInputInventory.getStack(2).getCount() == getIngredientsCounts().get(2) &&
                        Item4.equals(recipeInputInventory.getStack(3).getItem()) &&
                        recipeInputInventory.getStack(3).getCount() == getIngredientsCounts().get(3) &&
                        Item5.equals(recipeInputInventory.getStack(4).getItem()) &&
                        recipeInputInventory.getStack(4).getCount() == getIngredientsCounts().get(4) &&
                        BaseItem.equals(recipeInputInventory.getStack(5).getItem())
                        && recipeInputInventory.getStack(5).getCount() == getBaseItem().getCount();
        }
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

    public List<Integer> getIngredientsCounts() {
        return this.recipeCounts;
    }

    public ItemStack getBaseItem() {
        return this.base;
    }

    public AlchemyTableRecipeCategory getCategory() {
        return this.category;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(TCOTS_Blocks.ALCHEMY_TABLE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<AlchemyTableRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "alchemy_table";


        //Json Reader
        @Override
        public AlchemyTableRecipe read(Identifier id, JsonObject json) {
            //Order Reader
            float order = JsonHelper.getFloat(json, "order", 99);

            //Category reader
            AlchemyTableRecipeCategory category = AlchemyTableRecipeCategory.CODEC.byId(
                    JsonHelper.getString(json, "category", AlchemyTableRecipeCategory.MISC.asString()));

            //Ingredients List reader
            DefaultedList<Ingredient> ingredients = getIngredients(JsonHelper.getArray(json, "ingredients"));

            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for witcher potion recipe");
            } else if (ingredients.size() > 5) {
                throw new JsonParseException("Too many ingredients for witcher potion recipe");
            }

            //Ingredients quantity reader
            DefaultedList<Integer> ingredientsCount = getIngredientsCounts(JsonHelper.getArray(json, "ingredient_counters"));

            //Base reader
            JsonObject baseStack = JsonHelper.getObject(json, "base");
            Identifier idBase = new Identifier(JsonHelper.getString(baseStack, "item"));
            ItemStack base = new ItemStack(
                    Registries.ITEM.getOrEmpty(idBase).orElseThrow(() -> new IllegalStateException("Item: " + idBase + " does not exist")),
                     baseStack.has("count")? JsonHelper.getInt(baseStack, "count"): 1
            );


            //Result reader
            JsonObject resultStack = JsonHelper.getObject(json, "result");
            Identifier idOutput = new Identifier(JsonHelper.getString(resultStack, "item"));
            ItemStack output = new ItemStack(
                    Registries.ITEM.getOrEmpty(idOutput).orElseThrow(() -> new IllegalStateException("Item: " + idOutput + " does not exist")),
                    resultStack.has("count")? JsonHelper.getInt(resultStack, "count"): 1
            );

            return new AlchemyTableRecipe(order, category, ingredients, ingredientsCount, base, output);
        }

        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();

            for (int i = 0; i < json.size(); i++) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i), false);
                if (!ingredient.isEmpty()) {
                    defaultedList.add(ingredient);
                }
            }

            return defaultedList;
        }

        private static DefaultedList<Integer> getIngredientsCounts(JsonArray json) {
            DefaultedList<Integer> defaultedList = DefaultedList.copyOf(1,1,1,1,1,1);

            for (int i = 0; i < json.size(); i++) {
                defaultedList.set(i, json.get(i).getAsInt());
            }

            return defaultedList;
        }

//        public static Integer fromJson(@Nullable JsonElement json, boolean allowAir) {
//            if (json == null || json.isJsonNull()) {
//                throw new JsonSyntaxException("Item cannot be null");
//            } else if (json.isJsonObject()) {
//                return ofEntries(Stream.of(entryFromJson(json.getAsJsonObject())));
//            } else if (json.isJsonArray()) {
//                JsonArray jsonArray = json.getAsJsonArray();
//                if (jsonArray.size() == 0 && !allowAir) {
//                    throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
//                } else {
//                    return ofEntries(StreamSupport.stream(jsonArray.spliterator(), false).map(jsonElement -> entryFromJson(JsonHelper.asObject(jsonElement, "item"))));
//                }
//            } else {
//                throw new JsonSyntaxException("Expected item to be object or array of objects");
//            }
//        }
//        private static Ingredient.Entry entryFromJson(JsonObject json) {
//            if (json.has("item") && json.has("tag")) {
//                throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
//            } else if (json.has("item")) {
//                Item item = ShapedRecipe.getItem(json);
//                return new Ingredient.StackEntry(new ItemStack(item));
//            } else if (json.has("tag")) {
//                Identifier identifier = new Identifier(JsonHelper.getString(json, "tag"));
//                TagKey<Item> tagKey = TagKey.of(RegistryKeys.ITEM, identifier);
//                return new Ingredient.TagEntry(tagKey);
//            } else {
//                throw new JsonParseException("An ingredient entry needs either a tag or an item");
//            }
//        }
//        public static final Ingredient EMPTY = new Ingredient(Stream.empty());
//        public static Ingredient ofEntries(Stream<? extends Ingredient.Entry> entries) {
//            Ingredient ingredient = new Ingredient(entries);
//            return ingredient.isEmpty() ? EMPTY : ingredient;
//        }

        // Turns Recipe into PacketByteBuf
        @Override
        public void write(PacketByteBuf buf, AlchemyTableRecipe recipe) {
            buf.writeFloat(recipe.getOrder());

            recipe.getIngredients().get(0).write(buf);
            recipe.getIngredients().get(1).write(buf);
            recipe.getIngredients().get(2).write(buf);
            recipe.getIngredients().get(3).write(buf);
            recipe.getIngredients().get(4).write(buf);

            buf.writeInt(recipe.getIngredientsCounts().get(0));
            buf.writeInt(recipe.getIngredientsCounts().get(1));
            buf.writeInt(recipe.getIngredientsCounts().get(2));
            buf.writeInt(recipe.getIngredientsCounts().get(3));
            buf.writeInt(recipe.getIngredientsCounts().get(4));

            buf.writeItemStack(recipe.getBaseItem());
            buf.writeItemStack(recipe.getOutput(null));
            buf.writeEnumConstant((recipe).getCategory());
        }

        // Turns PacketByteBuf into Recipe(InGame)
        @Override
        public AlchemyTableRecipe read(Identifier id, PacketByteBuf buf) {
            // Make sure the read in the same order you have written!
            float order = buf.readFloat();

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

            List<Integer> ingredientCountList = Lists.newArrayList();
            int C1 = buf.readInt();
            int C2 = buf.readInt();
            int C3 = buf.readInt();
            int C4 = buf.readInt();
            int C5 = buf.readInt();
            ingredientCountList.add(C1);
            ingredientCountList.add(C2);
            ingredientCountList.add(C3);
            ingredientCountList.add(C4);
            ingredientCountList.add(C5);

            ItemStack base = buf.readItemStack();
            ItemStack output = buf.readItemStack();
            AlchemyTableRecipeCategory alchemyTableRecipeCategory = buf.readEnumConstant(AlchemyTableRecipeCategory.class);

            return new AlchemyTableRecipe(order, alchemyTableRecipeCategory, ingredientList, ingredientCountList, base, output);
        }
    }
}

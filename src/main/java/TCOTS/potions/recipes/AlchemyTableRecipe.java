package TCOTS.potions.recipes;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
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
    private final float id;

    public static final String ID_STRING = "alchemy_table";

    public final AlchemyTableRecipeCategory category;

    public AlchemyTableRecipe(float order, AlchemyTableRecipeCategory category, List<Ingredient> ingredients, List<Integer> IngredientCount, ItemStack base, ItemStack output) {
        this.output = output;
        this.recipeItems = ingredients;
        this.id=order;
        this.base = base;
        this.recipeCounts= IngredientCount;
        this.category = category;
    }

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
    public int compareTo(@NotNull AlchemyTableRecipe o) {
        return Float.compare(this.getId(), o.getId());
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

    public float getId() {
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
        return this.getResult(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
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
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<AlchemyTableRecipe>{

        //Json Reader
        public static final Codec<AlchemyTableRecipe> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        //Order Reader
                        Codecs.POSITIVE_FLOAT.fieldOf("order").orElse(99f)
                                .forGetter(recipe -> recipe.id),

                        //Category reader
                        AlchemyTableRecipeCategory.CODEC.fieldOf("category").orElse(AlchemyTableRecipeCategory.MISC)
                                .forGetter(recipe -> recipe.category),

                        //Ingredients List reader
                        Ingredient.ALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients")
                                .flatXmap(ingredients -> {
                                    Ingredient[] ingredients2 = ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
                                    if (ingredients2.length == 0) {
                                        return DataResult.error(() -> "No ingredients for witcher potion recipe");
                                    }
                                    if (ingredients2.length > 5) {
                                        return DataResult.error(() -> "Too many ingredients for witcher potion recipe");
                                    }
                                    return DataResult.success(DefaultedList.copyOf(Ingredient.EMPTY, ingredients2));
                                }, DataResult::success)
                                .forGetter(AlchemyTableRecipe::getIngredients),

                        //Ingredients quantity reader
                        Codecs.POSITIVE_INT.listOf().fieldOf("ingredient_counters")

                                .forGetter(recipe -> recipe.recipeCounts),

                        //Base reader
                        ItemStack.RECIPE_RESULT_CODEC.fieldOf("base")
                                .forGetter(recipe -> recipe.base),

                        //Result reader
                        ItemStack.RECIPE_RESULT_CODEC.fieldOf("result")
                                .forGetter(recipe -> recipe.output)

                        ).apply(instance, AlchemyTableRecipe::new));


        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "alchemy_table";

        @Override
        public Codec<AlchemyTableRecipe> codec() {
            return CODEC;
        }


        // Turns Recipe into PacketByteBuf
        @Override
        public void write(PacketByteBuf buf, AlchemyTableRecipe recipe) {
            buf.writeFloat(recipe.getId());

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
            buf.writeItemStack(recipe.getResult(null));
            buf.writeEnumConstant((recipe).getCategory());
        }

        // Turns PacketByteBuf into Recipe(InGame)
        @Override
        public AlchemyTableRecipe read(PacketByteBuf buf) {
            // Make sure the read in the same order you have written!
            float count = buf.readFloat();

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

            return new AlchemyTableRecipe(count, alchemyTableRecipeCategory, ingredientList, ingredientCountList, base, output);
        }
    }
}

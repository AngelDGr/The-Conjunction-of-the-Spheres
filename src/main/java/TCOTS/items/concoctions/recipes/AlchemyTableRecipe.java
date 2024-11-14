package TCOTS.items.concoctions.recipes;

import TCOTS.blocks.TCOTS_Blocks;
import com.google.common.collect.Lists;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AlchemyTableRecipe implements Recipe<AlchemyTableRecipe.AlchemyTableInventory>, Comparable<AlchemyTableRecipe>{
    public record AlchemyTableInventory(ItemStack ingredient1,
                                        ItemStack ingredient2,
                                        ItemStack ingredient3,
                                        ItemStack ingredient4,
                                        ItemStack ingredient5,
                                        ItemStack base)  implements RecipeInput {
        @Override
        public ItemStack getStackInSlot(int slot) {
            return switch (slot){
                case 0 -> this.ingredient1;
                case 1 -> this.ingredient2;
                case 2 -> this.ingredient3;
                case 3 -> this.ingredient4;
                case 4 -> this.ingredient5;
                case 5 -> this.base;
                default -> throw new IllegalStateException("Unexpected value: " + slot);
            };
        }

        @Override
        public int getSize() {
            return 6;
        }
    }
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
    public boolean matches(AlchemyTableInventory recipeInputInventory, World world) {
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

                return Item1.equals(recipeInputInventory.getStackInSlot(0).getItem()) &&
                        recipeInputInventory.getStackInSlot(0).getCount() == getIngredientsCounts().get(0) &&
                        recipeInputInventory.getStackInSlot(1).isEmpty() &&
                        recipeInputInventory.getStackInSlot(2).isEmpty() &&
                        recipeInputInventory.getStackInSlot(3).isEmpty() &&
                        recipeInputInventory.getStackInSlot(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStackInSlot(5).getItem())
                        && recipeInputInventory.getStackInSlot(5).getCount() == getBaseItem().getCount();
            case 2:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStackInSlot(0).getItem()) &&
                        recipeInputInventory.getStackInSlot(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStackInSlot(1).getItem()) &&
                        recipeInputInventory.getStackInSlot(1).getCount() == getIngredientsCounts().get(1) &&
                        recipeInputInventory.getStackInSlot(2).isEmpty() &&
                        recipeInputInventory.getStackInSlot(3).isEmpty() &&
                        recipeInputInventory.getStackInSlot(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStackInSlot(5).getItem())
                        && recipeInputInventory.getStackInSlot(5).getCount() == getBaseItem().getCount();
            case 3:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getMatchingStacks()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStackInSlot(0).getItem()) &&
                        recipeInputInventory.getStackInSlot(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStackInSlot(1).getItem()) &&
                        recipeInputInventory.getStackInSlot(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getStackInSlot(2).getItem()) &&
                        recipeInputInventory.getStackInSlot(2).getCount() == getIngredientsCounts().get(2) &&
                        recipeInputInventory.getStackInSlot(3).isEmpty() &&
                        recipeInputInventory.getStackInSlot(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStackInSlot(5).getItem())
                        && recipeInputInventory.getStackInSlot(5).getCount() == getBaseItem().getCount();
            case 4:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getMatchingStacks()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getMatchingStacks()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getMatchingStacks()[0].getItem();
                Ing4 = getIngredients().get(3);
                Item4 = Ing4.getMatchingStacks()[0].getItem();

                return Item1.equals(recipeInputInventory.getStackInSlot(0).getItem()) &&
                        recipeInputInventory.getStackInSlot(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStackInSlot(1).getItem()) &&
                        recipeInputInventory.getStackInSlot(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getStackInSlot(2).getItem()) &&
                        recipeInputInventory.getStackInSlot(2).getCount() == getIngredientsCounts().get(2) &&
                        Item4.equals(recipeInputInventory.getStackInSlot(3).getItem()) &&
                        recipeInputInventory.getStackInSlot(3).getCount() == getIngredientsCounts().get(3) &&
                        recipeInputInventory.getStackInSlot(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getStackInSlot(5).getItem())
                        && recipeInputInventory.getStackInSlot(5).getCount() == getBaseItem().getCount();
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

                return Item1.equals(recipeInputInventory.getStackInSlot(0).getItem()) &&
                        recipeInputInventory.getStackInSlot(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getStackInSlot(1).getItem()) &&
                        recipeInputInventory.getStackInSlot(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getStackInSlot(2).getItem()) &&
                        recipeInputInventory.getStackInSlot(2).getCount() == getIngredientsCounts().get(2) &&
                        Item4.equals(recipeInputInventory.getStackInSlot(3).getItem()) &&
                        recipeInputInventory.getStackInSlot(3).getCount() == getIngredientsCounts().get(3) &&
                        Item5.equals(recipeInputInventory.getStackInSlot(4).getItem()) &&
                        recipeInputInventory.getStackInSlot(4).getCount() == getIngredientsCounts().get(4) &&
                        BaseItem.equals(recipeInputInventory.getStackInSlot(5).getItem())
                        && recipeInputInventory.getStackInSlot(5).getCount() == getBaseItem().getCount();
        }
        return false;
    }

    @Override
    public ItemStack craft(AlchemyTableInventory input, RegistryWrapper.WrapperLookup lookup) {
        return this.getResult(lookup).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }


    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
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

        //Json Reader
        public static final MapCodec<AlchemyTableRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        //Order Reader
                        Codecs.POSITIVE_FLOAT.fieldOf("order").orElse(99f)
                                .forGetter(recipe -> recipe.order),

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
                        ItemStack.VALIDATED_CODEC.fieldOf("base")
                                .forGetter(recipe -> recipe.base),

                        //Result reader
                        ItemStack.VALIDATED_CODEC.fieldOf("result")
                                .forGetter(recipe -> recipe.output)

                        ).apply(instance, AlchemyTableRecipe::new));


        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "alchemy_table";

        @Override
        public MapCodec<AlchemyTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, AlchemyTableRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public static final PacketCodec<RegistryByteBuf, AlchemyTableRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                Serializer::write, Serializer::read
        );


        // Turns Recipe into PacketByteBuf
        public static void write(RegistryByteBuf buf, AlchemyTableRecipe recipe) {
            buf.writeFloat(recipe.getOrder());

            int limit=recipe.getIngredients().size();

            buf.writeInt(limit);

            for(int i =0;i<limit;i++){
                Ingredient.PACKET_CODEC.encode(buf, recipe.getIngredients().get(i));
            }

            for(int i =0 ; i<limit; i++){
                buf.writeInt(recipe.getIngredientsCounts().get(i));
            }

            ItemStack.PACKET_CODEC.encode(buf, recipe.getBaseItem());
            ItemStack.PACKET_CODEC.encode(buf, recipe.getResult(null));
            buf.writeEnumConstant((recipe).getCategory());
        }

        // Turns PacketByteBuf into Recipe(InGame)
        public static AlchemyTableRecipe read(RegistryByteBuf buf) {
            // Make sure the read in the same order you have written!

            float count = buf.readFloat();

            int limit = buf.readInt();

            List<Ingredient> ingredientList = Lists.newArrayList();
            for(int i =0;i<limit;i++){
                ingredientList.add(Ingredient.PACKET_CODEC.decode(buf));
            }

            List<Integer> ingredientCountList = Lists.newArrayList();
            for(int i =0;i<limit;i++){
                ingredientCountList.add(buf.readInt());
            }

            ItemStack base = ItemStack.PACKET_CODEC.decode(buf);
            ItemStack output = ItemStack.PACKET_CODEC.decode(buf);
            AlchemyTableRecipeCategory alchemyTableRecipeCategory = buf.readEnumConstant(AlchemyTableRecipeCategory.class);

            return new AlchemyTableRecipe(count, alchemyTableRecipeCategory, ingredientList, ingredientCountList, base, output);
        }
    }
}

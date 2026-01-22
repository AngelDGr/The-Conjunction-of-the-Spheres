package mors.tcots.recipes;

import mors.tcots.registry.TCOTS_Blocks;
import com.google.common.collect.Lists;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AlchemyTableRecipe implements Recipe<AlchemyTableRecipe.AlchemyTableInventory>, Comparable<AlchemyTableRecipe>{
    public record AlchemyTableInventory(ItemStack ingredient1,
                                        ItemStack ingredient2,
                                        ItemStack ingredient3,
                                        ItemStack ingredient4,
                                        ItemStack ingredient5,
                                        ItemStack base)  implements RecipeInput {
        @Override
        public @NotNull ItemStack getItem(final int slot) {
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
        public int size() {
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

    public AlchemyTableRecipe(final float order, final AlchemyTableRecipeCategory category, final List<Ingredient> ingredients, final List<Integer> IngredientCount, final ItemStack base, final ItemStack output) {
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
        final List<ItemStack> list = new ArrayList<>();
        for(int i=0;i<getIngredients().size();i++){
            final Item itemForStack = getIngredients().get(i).getItems()[0].getItem();
            final int count = getIngredientsCounts().get(i);

            final ItemStack stack = new ItemStack(itemForStack, count);
            list.add(stack);
        }

        return list;
    }

    @Override
    public int compareTo(@NotNull final AlchemyTableRecipe o) {
        return Float.compare(this.getOrder(), o.getOrder());
    }

    public static class Type implements RecipeType<AlchemyTableRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "alchemy_table";
    }
    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public float getOrder() {
        return this.order;
    }

    //If a given inventory satisfies a recipe's input.
    @Override
    public boolean matches(@NotNull final AlchemyTableInventory recipeInputInventory, final Level world) {
        if(world.isClientSide()) {
            return false;
        }
        final Ingredient Ing1;
        final Ingredient Ing2;
        final Ingredient Ing3;
        final Ingredient Ing4;
        final Ingredient Ing5;

        final Item Item1;
        final Item Item2;
        final Item Item3;
        final Item Item4;
        final Item Item5;

        final Item BaseItem = getBaseItem().getItem();

        switch (getIngredients().size()){
            case 1:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getItems()[0].getItem();

                return Item1.equals(recipeInputInventory.getItem(0).getItem()) &&
                        recipeInputInventory.getItem(0).getCount() == getIngredientsCounts().get(0) &&
                        recipeInputInventory.getItem(1).isEmpty() &&
                        recipeInputInventory.getItem(2).isEmpty() &&
                        recipeInputInventory.getItem(3).isEmpty() &&
                        recipeInputInventory.getItem(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getItem(5).getItem())
                        && recipeInputInventory.getItem(5).getCount() == getBaseItem().getCount();
            case 2:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getItems()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getItems()[0].getItem();

                return Item1.equals(recipeInputInventory.getItem(0).getItem()) &&
                        recipeInputInventory.getItem(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getItem(1).getItem()) &&
                        recipeInputInventory.getItem(1).getCount() == getIngredientsCounts().get(1) &&
                        recipeInputInventory.getItem(2).isEmpty() &&
                        recipeInputInventory.getItem(3).isEmpty() &&
                        recipeInputInventory.getItem(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getItem(5).getItem())
                        && recipeInputInventory.getItem(5).getCount() == getBaseItem().getCount();
            case 3:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getItems()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getItems()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getItems()[0].getItem();

                return Item1.equals(recipeInputInventory.getItem(0).getItem()) &&
                        recipeInputInventory.getItem(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getItem(1).getItem()) &&
                        recipeInputInventory.getItem(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getItem(2).getItem()) &&
                        recipeInputInventory.getItem(2).getCount() == getIngredientsCounts().get(2) &&
                        recipeInputInventory.getItem(3).isEmpty() &&
                        recipeInputInventory.getItem(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getItem(5).getItem())
                        && recipeInputInventory.getItem(5).getCount() == getBaseItem().getCount();
            case 4:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getItems()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getItems()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getItems()[0].getItem();
                Ing4 = getIngredients().get(3);
                Item4 = Ing4.getItems()[0].getItem();

                return Item1.equals(recipeInputInventory.getItem(0).getItem()) &&
                        recipeInputInventory.getItem(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getItem(1).getItem()) &&
                        recipeInputInventory.getItem(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getItem(2).getItem()) &&
                        recipeInputInventory.getItem(2).getCount() == getIngredientsCounts().get(2) &&
                        Item4.equals(recipeInputInventory.getItem(3).getItem()) &&
                        recipeInputInventory.getItem(3).getCount() == getIngredientsCounts().get(3) &&
                        recipeInputInventory.getItem(4).isEmpty() &&
                        BaseItem.equals(recipeInputInventory.getItem(5).getItem())
                        && recipeInputInventory.getItem(5).getCount() == getBaseItem().getCount();
            case 5:
                Ing1 = getIngredients().get(0);
                Item1 = Ing1.getItems()[0].getItem();
                Ing2 = getIngredients().get(1);
                Item2 = Ing2.getItems()[0].getItem();
                Ing3 = getIngredients().get(2);
                Item3 = Ing3.getItems()[0].getItem();
                Ing4 = getIngredients().get(3);
                Item4 = Ing4.getItems()[0].getItem();
                Ing5 = getIngredients().get(4);
                Item5 = Ing5.getItems()[0].getItem();

                return Item1.equals(recipeInputInventory.getItem(0).getItem()) &&
                        recipeInputInventory.getItem(0).getCount() == getIngredientsCounts().get(0) &&
                        Item2.equals(recipeInputInventory.getItem(1).getItem()) &&
                        recipeInputInventory.getItem(1).getCount() == getIngredientsCounts().get(1) &&
                        Item3.equals(recipeInputInventory.getItem(2).getItem()) &&
                        recipeInputInventory.getItem(2).getCount() == getIngredientsCounts().get(2) &&
                        Item4.equals(recipeInputInventory.getItem(3).getItem()) &&
                        recipeInputInventory.getItem(3).getCount() == getIngredientsCounts().get(3) &&
                        Item5.equals(recipeInputInventory.getItem(4).getItem()) &&
                        recipeInputInventory.getItem(4).getCount() == getIngredientsCounts().get(4) &&
                        BaseItem.equals(recipeInputInventory.getItem(5).getItem())
                        && recipeInputInventory.getItem(5).getCount() == getBaseItem().getCount();
        }
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull final AlchemyTableInventory input, final HolderLookup.@NotNull Provider lookup) {
        return this.getResultItem(lookup).copy();
    }

    @Override
    public boolean canCraftInDimensions(final int width, final int height) {
        return true;
    }


    @Override
    public @NotNull ItemStack getResultItem(@Nullable final HolderLookup.Provider registriesLookup) {
        return output;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        final NonNullList<Ingredient> list = NonNullList.createWithCapacity(this.recipeItems.size());
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
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(TCOTS_Blocks.AlchemyTable());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<AlchemyTableRecipe>{

        //Json Reader
        public static final MapCodec<AlchemyTableRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        //Order Reader
                        ExtraCodecs.POSITIVE_FLOAT.fieldOf("order").orElse(99f)
                                .forGetter(recipe -> recipe.order),

                        //Category reader
                        AlchemyTableRecipeCategory.CODEC.fieldOf("category").orElse(AlchemyTableRecipeCategory.MISC)
                                .forGetter(recipe -> recipe.category),

                        //Ingredients List reader
                        Ingredient.CODEC.listOf().fieldOf("ingredients")
                                .flatXmap(ingredients -> {
                                    Ingredient[] ingredients2 = ingredients.stream().filter(ingredient -> !ingredient.isEmpty()).toArray(Ingredient[]::new);
                                    if (ingredients2.length == 0) {
                                        return DataResult.error(() -> "No ingredients for witcher potion recipe");
                                    }
                                    if (ingredients2.length > 5) {
                                        return DataResult.error(() -> "Too many ingredients for witcher potion recipe");
                                    }
                                    return DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients2));
                                }, DataResult::success)
                                .forGetter(AlchemyTableRecipe::getIngredients),

                        //Ingredients quantity reader
                        ExtraCodecs.POSITIVE_INT.listOf().fieldOf("ingredient_counters")

                                .forGetter(recipe -> recipe.recipeCounts),

                        //Base reader
                        ItemStack.STRICT_CODEC.fieldOf("base")
                                .forGetter(recipe -> recipe.base),

                        //Result reader
                        ItemStack.STRICT_CODEC.fieldOf("result")
                                .forGetter(recipe -> recipe.output)

                        ).apply(instance, AlchemyTableRecipe::new));


        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "alchemy_table";

        @Override
        public @NotNull MapCodec<AlchemyTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, AlchemyTableRecipe> streamCodec() {
            return PACKET_CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyTableRecipe> PACKET_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read
        );


        // Turns Recipe into PacketByteBuf
        public static void write(final RegistryFriendlyByteBuf buf, final AlchemyTableRecipe recipe) {
            buf.writeFloat(recipe.getOrder());

            final int limit=recipe.getIngredients().size();

            buf.writeInt(limit);

            for(int i =0;i<limit;i++){
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getIngredients().get(i));
            }

            for(int i =0 ; i<limit; i++){
                buf.writeInt(recipe.getIngredientsCounts().get(i));
            }

            ItemStack.STREAM_CODEC.encode(buf, recipe.getBaseItem());
            ItemStack.STREAM_CODEC.encode(buf, recipe.getResultItem(null));
            buf.writeEnum((recipe).getCategory());
        }

        // Turns PacketByteBuf into Recipe(InGame)
        public static AlchemyTableRecipe read(final RegistryFriendlyByteBuf buf) {
            // Make sure the read in the same order you have written!

            final float count = buf.readFloat();

            final int limit = buf.readInt();

            final List<Ingredient> ingredientList = Lists.newArrayList();
            for(int i =0;i<limit;i++){
                ingredientList.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
            }

            final List<Integer> ingredientCountList = Lists.newArrayList();
            for(int i =0;i<limit;i++){
                ingredientCountList.add(buf.readInt());
            }

            final ItemStack base = ItemStack.STREAM_CODEC.decode(buf);
            final ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
            final AlchemyTableRecipeCategory alchemyTableRecipeCategory = buf.readEnum(AlchemyTableRecipeCategory.class);

            return new AlchemyTableRecipe(count, alchemyTableRecipeCategory, ingredientList, ingredientCountList, base, output);
        }
    }
}

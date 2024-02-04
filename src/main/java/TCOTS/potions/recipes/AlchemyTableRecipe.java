package TCOTS.potions.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.List;

public class AlchemyTableRecipe implements Recipe<SimpleInventory> {
    private final ItemStack output;
    private final List<Ingredient> recipeItems;

    private final Identifier id;


    public AlchemyTableRecipe(Identifier id, List<Ingredient> ingredients, ItemStack itemStack) {
        this.output = itemStack;
        this.recipeItems = ingredients;
        this.id = id;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient()) {
            return false;
        }

//        return recipeItems.get(0).test(inventory.getStack(0));

        RecipeMatcher recipeMatcher = new RecipeMatcher();
        int i = 0;
        for (int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.getStack(j);
            if (itemStack.isEmpty()) continue;
            ++i;
            recipeMatcher.addInput(itemStack, 1);
        }
        return i == this.recipeItems.size() && recipeMatcher.match(this, null);
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    public ItemStack getRawOutput() {
        return output;
    }



    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.recipeItems.size());
        list.addAll(recipeItems);
        return list;
    }

    public static class Type implements RecipeType<AlchemyTableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "alchemy_table-witcher";
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<AlchemyTableRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "alchemy_table-witcher";
        @Override
        public AlchemyTableRecipe read(Identifier identifier, JsonObject jsonObject) {

            DefaultedList<Ingredient> defaultedList = AlchemyTableRecipe.Serializer.getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (defaultedList.isEmpty()) {
                throw new JsonParseException("No ingredients for witcher potion recipe");
            }
            if (defaultedList.size() > 5) {
                throw new JsonParseException("Too many ingredients for witcher potions recipe");
            }
            ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new AlchemyTableRecipe(identifier, defaultedList, itemStack);
        }


        private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
            DefaultedList<Ingredient> defaultedList = DefaultedList.of();
            for (int i = 0; i < json.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(json.get(i), false);
                if (ingredient.isEmpty()) continue;
                defaultedList.add(ingredient);
            }
            return defaultedList;
        }

        @Override
        public AlchemyTableRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new AlchemyTableRecipe(id, inputs, output);
        }

        @Override
        public void write(PacketByteBuf buf, AlchemyTableRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.write(buf);
            }

            buf.writeItemStack(recipe.getOutput(null));
        }
    }

}

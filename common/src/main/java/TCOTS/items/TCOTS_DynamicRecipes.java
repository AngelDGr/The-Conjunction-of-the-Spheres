package TCOTS.items;

import TCOTS.TCOTS_Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;

import java.util.List;

public class TCOTS_DynamicRecipes {
    //Dynamic Recipes
    public static JsonObject GVALCHIR=null;
    public static JsonObject MOONBLADE=null;
    public static JsonObject DYAEBL=null;
    public static JsonObject ARDAENYE=null;
    public static JsonObject KNIGHT_HORSE_ARMOR=null;

    static void addRecipe(String recipeId, JsonObject json){
        TCOTS_Main.recipes.add(new Tuple<>(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, recipeId), json));
    }

    static @NotNull JsonObject createShapedRecipe(@NotNull RecipeCategory category, Item result,
                                                  @NotNull List<Triplet<String, Character, ResourceLocation>> keys,
                                                  @NotNull List<String> pattern){
        JsonObject mainJson = new JsonObject();

        //Type
        mainJson.addProperty("type", "minecraft:crafting_shaped");

        //Category
        mainJson.addProperty("category", category.getFolderName());

        //We create a new Json Element, and add our crafting pattern to it.
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(pattern.get(0));
        jsonArray.add(pattern.get(1));
        jsonArray.add(pattern.get(2));
        //Then we add the pattern to our json object.
        mainJson.add("pattern", jsonArray);

        //Next we need to define what the keys in the pattern are. For this we need different JsonObjects per key definition, and one main JsonObject that will contain all the defined keys.
        JsonObject individualKey; //Individual key
        JsonObject keyList = new JsonObject(); //The main key object, containing all the keys

        for (Triplet<String, Character, ResourceLocation> key : keys) {
            individualKey = new JsonObject();
            individualKey.addProperty(key.getA(), key.getC().toString()); //This will create a key in the form "type": "input", where type is either "item" or "tag", and input is our input item.
            keyList.add(key.getB() + "", individualKey); //Then we add this key to the main key object.
            //This will add:
            //"#": { "tag": "c:copper_ingots" }
            //and after that
            //"|": { "item": "minecraft:sticks" }
            //and so on.
        }

        mainJson.add("key", keyList);

        //Result
        JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("id", BuiltInRegistries.ITEM.getKey(result).toString());
        jsonobject.addProperty("count", 1);
        mainJson.add("result", jsonobject);

        return mainJson;
    }

    static Triplet<String, Character, ResourceLocation> addItem(Character c, Item item){
        return new Triplet<>("item", c, BuiltInRegistries.ITEM.getKey(item));
    }

    @SuppressWarnings("all")
    static Triplet<String, Character, ResourceLocation> addItem(Character c, TagKey<Item> tag){
        return new Triplet<>("tag", c, tag.location());
    }

    static Triplet<String, Character, ResourceLocation> addItem(Character c, ResourceLocation item){
        return new Triplet<>("item", c, item);
    }
}

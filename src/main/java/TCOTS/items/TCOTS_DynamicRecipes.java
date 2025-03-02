package TCOTS.items;

import TCOTS.TCOTS_Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;

import java.util.ArrayList;
import java.util.List;

public class TCOTS_DynamicRecipes {
    //Dynamic Recipes
    public static JsonObject GVALCHIR=null;
    public static JsonObject MOONBLADE=null;
    public static JsonObject DYAEBL=null;
    public static JsonObject ARDAENYE=null;
    public static JsonObject KNIGHT_HORSE_ARMOR=null;
    public static List<Pair<Identifier, JsonObject>> recipes= new ArrayList<>();
    public static void registerDynamicRecipes(){
        //Swords
        {
            //G'valchir
            {
                GVALCHIR = createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items.GVALCHIR,
                        List.of(
                                addItem('F', TCOTS_Items.FOGLET_TEETH),
                                addItem('H', TCOTS_Items.BULLVORE_HORN_FRAGMENT),
                                addItem('D', TCOTS_Items.DEVOURER_TEETH),
                                addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        Identifier.of("witcher_rpg", "dark_iron_witcher_sword"):
                                        Identifier.of("minecraft","iron_sword"))
                        ),
                        List.of(
                                "FHD",
                                "HSH",
                                "DHF"
                        )
                );
                addRecipe("gvalchir", GVALCHIR);
            }

            //Moonblade
            {
                MOONBLADE = createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items.MOONBLADE,
                        List.of(
                                addItem('G', TCOTS_Items.GRAVEIR_BONE),
                                addItem('H', TCOTS_Items.NEKKER_HEART),
                                addItem('B', TCOTS_Items.MONSTER_BLOOD),
                                addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        Identifier.of("witcher_rpg", "silver_witcher_sword"):
                                        Identifier.of("minecraft","golden_sword"))
                        ),
                        List.of(
                                "HBG",
                                "BSB",
                                "GBH"
                        )
                );
                addRecipe("moonblade", MOONBLADE);
            }

            //Ard'aenye
            {
                ARDAENYE = createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items.ARDAENYE,
                        List.of(
                                addItem('C', TCOTS_Items.CADAVERINE),
                                addItem('D', TCOTS_Items.DEVOURER_TEETH),
                                addItem('B', Items.BLAZE_POWDER),
                                addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        Identifier.of("witcher_rpg", "dark_steel_witcher_sword"):
                                        Identifier.of("minecraft","diamond_sword"))
                        ),
                        List.of(
                                "BDC",
                                "DSD",
                                "CDB"
                        )
                );
                addRecipe("ardaenye", ARDAENYE);
            }

            //D'yaebl
            {
                DYAEBL = createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items.DYAEBL,
                        List.of(
                                addItem('R', TCOTS_Items.ROTFIEND_BLOOD),
                                addItem('G', TCOTS_Items.GHOUL_BLOOD),
                                addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        Identifier.of("witcher_rpg", "steel_witcher_sword"):
                                        Identifier.of("minecraft","iron_sword"))
                        ),
                        List.of(
                                "GRG",
                                "RSR",
                                "GRG"
                        )
                );
                addRecipe("dyaebl", DYAEBL);
            }
        }

        //Horse Armor
        {
            KNIGHT_HORSE_ARMOR = createShapedRecipe(
                    RecipeCategory.MISC,
                    TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR,
                    List.of(
                            addItem('N', Items.IRON_NUGGET),
                            addItem('I', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                    Identifier.of("witcher_rpg", "steel_ingot"):
                                    Identifier.of("minecraft","iron_ingot")),
                            addItem('A', Items.IRON_HORSE_ARMOR)
                    ),
                    List.of(
                            "  I",
                            "IAN",
                            "IIN"
                    )
            );
            addRecipe("knight_errants_horse_armor", KNIGHT_HORSE_ARMOR);
        }

    }

    private static void addRecipe(String recipeId, JsonObject json){
        recipes.add(new Pair<>(Identifier.of(TCOTS_Main.MOD_ID, recipeId), json));
    }


    private static @NotNull JsonObject createShapedRecipe(@NotNull RecipeCategory category, Item result,
                                                          @NotNull List<Triplet<String, Character, Identifier>> keys,
                                                          @NotNull List<String> pattern){
        JsonObject mainJson = new JsonObject();

        //Type
        mainJson.addProperty("type", "minecraft:crafting_shaped");

        //Category
        mainJson.addProperty("category", category.getName());

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

        for (Triplet<String, Character, Identifier> key : keys) {
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
        jsonobject.addProperty("item", Registries.ITEM.getId(result).toString());
        jsonobject.addProperty("count", 1);
        mainJson.add("result", jsonobject);

        System.out.println(mainJson);

        return mainJson;
    }


    private static Triplet<String, Character, Identifier> addItem(Character c, Item item){
        return new Triplet<>("item", c, Registries.ITEM.getId(item));
    }
    @SuppressWarnings("all")
    private static Triplet<String, Character, Identifier> addItem(Character c, TagKey<Item> tag){
        return new Triplet<>("tag", c, tag.id());
    }

    private static Triplet<String, Character, Identifier> addItem(Character c, Identifier item){
        return new Triplet<>("item", c, item);
    }

}

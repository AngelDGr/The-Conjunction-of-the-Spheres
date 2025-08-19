package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.utils.MiscUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Triplet;

import java.util.List;
import java.util.function.Supplier;

public class TCOTS_DynamicRecipes {
    //Dynamic Recipes
    public static JsonObject GVALCHIR=null;
    public static JsonObject MOONBLADE=null;
    public static JsonObject DYAEBL=null;
    public static JsonObject ARDAENYE=null;
    public static JsonObject KNIGHT_HORSE_ARMOR=null;

    public static void initDynamicRecipes(){
        //Swords
        {
            //G'valchir
            {
                GVALCHIR = createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Main.id("gvalchir"),
                        List.of(
                                addItem('F', TCOTS_Main.id("foglet_teeth")),
                                addItem('H', TCOTS_Main.id("bullvore_horn_fragment")),
                                addItem('D', TCOTS_Main.id("devourer_teeth")),
                                addItem('S', MiscUtil.isWitcherRPGLoaded()?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_iron_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("iron_sword"))
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
                        TCOTS_Main.id("moonblade"),
                        List.of(
                                addItem('G', TCOTS_Main.id("graveir_bone")),
                                addItem('H', TCOTS_Main.id("nekker_heart")),
                                addItem('B', TCOTS_Tags.MONSTER_BLOOD),
                                addItem('S', MiscUtil.isWitcherRPGLoaded()?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "silver_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("golden_sword"))
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
                        TCOTS_Main.id("ardaenye"),
                        List.of(
                                addItem('C', TCOTS_Main.id("cadaverine")),
                                addItem('D', TCOTS_Main.id("devourer_teeth")),
                                addItem('B', Items.BLAZE_POWDER),
                                addItem('S', MiscUtil.isWitcherRPGLoaded()?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_steel_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("diamond_sword"))
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
                        TCOTS_Main.id("dyaebl"),
                        List.of(
                                addItem('R', TCOTS_Main.id("rotfiend_blood")),
                                addItem('G', TCOTS_Main.id("ghoul_blood")),
                                addItem('S', MiscUtil.isWitcherRPGLoaded()?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("iron_sword"))
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
                    TCOTS_Main.id("knight_errants_horse_armor"),
                    List.of(
                            addItem('N', Items.IRON_NUGGET),
                            addItem('I', MiscUtil.isWitcherRPGLoaded()?
                                    ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_ingot"):
                                    ResourceLocation.withDefaultNamespace("iron_ingot")),
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

    public static void addRecipe(final String recipeId, final JsonObject json){
        TCOTS_Main.recipes.add(new Tuple<>(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, recipeId), json));
    }

    public static @NotNull JsonObject createShapedRecipe(@NotNull final RecipeCategory category, final Item result,
                                                         @NotNull final List<Triplet<String, Character, ResourceLocation>> keys,
                                                         @NotNull final List<String> pattern){
        return createShapedRecipe(category, BuiltInRegistries.ITEM.getKey(result), keys, pattern);
    }

    public static @NotNull JsonObject createShapedRecipe(@NotNull final RecipeCategory category, final ResourceLocation result,
                                                         @NotNull final List<Triplet<String, Character, ResourceLocation>> keys,
                                                         @NotNull final List<String> pattern){
        final JsonObject mainJson = new JsonObject();

        //Type
        mainJson.addProperty("type", "minecraft:crafting_shaped");

        //Category
        mainJson.addProperty("category", category.getFolderName());

        //We create a new Json Element, and add our crafting pattern to it.
        final JsonArray jsonArray = new JsonArray();
        jsonArray.add(pattern.get(0));
        jsonArray.add(pattern.get(1));
        jsonArray.add(pattern.get(2));
        //Then we add the pattern to our json object.
        mainJson.add("pattern", jsonArray);

        //Next we need to define what the keys in the pattern are. For this we need different JsonObjects per key definition, and one main JsonObject that will contain all the defined keys.
        JsonObject individualKey; //Individual key
        final JsonObject keyList = new JsonObject(); //The main key object, containing all the keys

        for (final Triplet<String, Character, ResourceLocation> key : keys) {
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
        final JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("id", result.toString());
        jsonobject.addProperty("count", 1);
        mainJson.add("result", jsonobject);

        return mainJson;
    }

    public static Triplet<String, Character, ResourceLocation> addItem(final Character c, final Supplier<Item> item){
        return new Triplet<>("item", c, BuiltInRegistries.ITEM.getKey(item.get()));
    }

    public static Triplet<String, Character, ResourceLocation> addItem(final Character c, final Item item){
        return new Triplet<>("item", c, BuiltInRegistries.ITEM.getKey(item));
    }

    @SuppressWarnings("all")
    public static Triplet<String, Character, ResourceLocation> addItem(Character c, TagKey<Item> tag){
        return new Triplet<>("tag", c, tag.location());
    }

    public static Triplet<String, Character, ResourceLocation> addItem(final Character c, final ResourceLocation tag, final boolean isTag){

        return new Triplet<>(isTag? "tag": "item", c, tag);
    }

    public static Triplet<String, Character, ResourceLocation> addItem(final Character c, final ResourceLocation item){
        return addItem(c, item, false);
    }
}

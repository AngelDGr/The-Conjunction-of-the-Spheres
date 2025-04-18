package TCOTS.items;

import TCOTS.TCOTS_Tags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.List;

public class TCOTS_DynamicRecipes_Fabric extends TCOTS_DynamicRecipes {

    public static void registerDynamicRecipes(){
        //Swords
        {
            //G'valchir
            {
                TCOTS_DynamicRecipes.GVALCHIR = TCOTS_DynamicRecipes.createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items_Fabric.GVALCHIR,
                        List.of(
                                TCOTS_DynamicRecipes.addItem('F', TCOTS_Items_Fabric.FOGLET_TEETH),
                                TCOTS_DynamicRecipes.addItem('H', TCOTS_Items_Fabric.BULLVORE_HORN_FRAGMENT),
                                TCOTS_DynamicRecipes.addItem('D', TCOTS_Items_Fabric.DEVOURER_TEETH),
                                TCOTS_DynamicRecipes.addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_iron_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("iron_sword"))
                        ),
                        List.of(
                                "FHD",
                                "HSH",
                                "DHF"
                        )
                );
                TCOTS_DynamicRecipes.addRecipe("gvalchir", TCOTS_DynamicRecipes.GVALCHIR);
            }

            //Moonblade
            {
                TCOTS_DynamicRecipes.MOONBLADE = TCOTS_DynamicRecipes.createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items_Fabric.MOONBLADE,
                        List.of(
                                TCOTS_DynamicRecipes.addItem('G', TCOTS_Items_Fabric.GRAVEIR_BONE),
                                TCOTS_DynamicRecipes.addItem('H', TCOTS_Items_Fabric.NEKKER_HEART),
                                TCOTS_DynamicRecipes.addItem('B', TCOTS_Tags.MONSTER_BLOOD),
                                TCOTS_DynamicRecipes.addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "silver_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("golden_sword"))
                        ),
                        List.of(
                                "HBG",
                                "BSB",
                                "GBH"
                        )
                );
                TCOTS_DynamicRecipes.addRecipe("moonblade", TCOTS_DynamicRecipes.MOONBLADE);
            }

            //Ard'aenye
            {
                TCOTS_DynamicRecipes.ARDAENYE = TCOTS_DynamicRecipes.createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items_Fabric.ARDAENYE,
                        List.of(
                                TCOTS_DynamicRecipes.addItem('C', TCOTS_Items_Fabric.CADAVERINE),
                                TCOTS_DynamicRecipes.addItem('D', TCOTS_Items_Fabric.DEVOURER_TEETH),
                                TCOTS_DynamicRecipes.addItem('B', Items.BLAZE_POWDER),
                                TCOTS_DynamicRecipes.addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_steel_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("diamond_sword"))
                        ),
                        List.of(
                                "BDC",
                                "DSD",
                                "CDB"
                        )
                );
                TCOTS_DynamicRecipes.addRecipe("ardaenye", TCOTS_DynamicRecipes.ARDAENYE);
            }

            //D'yaebl
            {
                TCOTS_DynamicRecipes.DYAEBL = TCOTS_DynamicRecipes.createShapedRecipe(
                        RecipeCategory.COMBAT,
                        TCOTS_Items_Fabric.DYAEBL,
                        List.of(
                                TCOTS_DynamicRecipes.addItem('R', TCOTS_Items_Fabric.ROTFIEND_BLOOD),
                                TCOTS_DynamicRecipes.addItem('G', TCOTS_Items_Fabric.GHOUL_BLOOD),
                                TCOTS_DynamicRecipes.addItem('S', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                        ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_witcher_sword"):
                                        ResourceLocation.withDefaultNamespace("iron_sword"))
                        ),
                        List.of(
                                "GRG",
                                "RSR",
                                "GRG"
                        )
                );
                TCOTS_DynamicRecipes.addRecipe("dyaebl", TCOTS_DynamicRecipes.DYAEBL);
            }
        }

        //Horse Armor
        {
            TCOTS_DynamicRecipes.KNIGHT_HORSE_ARMOR = TCOTS_DynamicRecipes.createShapedRecipe(
                    RecipeCategory.MISC,
                    TCOTS_Items_Fabric.KNIGHT_ERRANTS_HORSE_ARMOR,
                    List.of(
                            TCOTS_DynamicRecipes.addItem('N', Items.IRON_NUGGET),
                            TCOTS_DynamicRecipes.addItem('I', FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                                    ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_ingot"):
                                    ResourceLocation.withDefaultNamespace("iron_ingot")),
                            TCOTS_DynamicRecipes.addItem('A', Items.IRON_HORSE_ARMOR)
                    ),
                    List.of(
                            "  I",
                            "IAN",
                            "IIN"
                    )
            );
            TCOTS_DynamicRecipes.addRecipe("knight_errants_horse_armor", TCOTS_DynamicRecipes.KNIGHT_HORSE_ARMOR);
        }

    }

}

package TCOTS.items.concoctions.recipes;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.screen.AlchemyTableScreenHandler;
import TCOTS.screen.HerbalTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlersAndRecipesRegister {
    public static ScreenHandlerType<AlchemyTableScreenHandler> ALCHEMY_TABLE_SCREEN_HANDLER;
    public static ScreenHandlerType<HerbalTableScreenHandler> HERBAL_TABLE_SCREEN_HANDLER;

    public static RecipeType<AlchemyTableRecipe> ALCHEMY_TABLE;
    public static RecipeType<HerbalTableRecipe> HERBAL_TABLE;
    public static void registerScreenHandlersAndRecipes()
    {
        ALCHEMY_TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(TCOTS_Main.MOD_ID, "alchemy_table"),
                new ExtendedScreenHandlerType<>(
                        AlchemyTableScreenHandler::new,
                        AlchemyTableBlockEntity.AlchemyBlockData.PACKET_CODEC));

        //RecipeSerializer
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(TCOTS_Main.MOD_ID, AlchemyTableRecipe.Serializer.ID),
                AlchemyTableRecipe.Serializer.INSTANCE);

        ALCHEMY_TABLE =
                Registry.register(Registries.RECIPE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, AlchemyTableRecipe.ID_STRING),
                        //TypeInstance
                        AlchemyTableRecipe.Type.INSTANCE);

        HERBAL_TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(TCOTS_Main.MOD_ID, "herbal_table"),
                new ScreenHandlerType<>(HerbalTableScreenHandler::new, FeatureFlags.VANILLA_FEATURES));

        HERBAL_TABLE =
                Registry.register(Registries.RECIPE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, HerbalTableRecipe.ID_STRING),
                        //TypeInstance
                        HerbalTableRecipe.Type.INSTANCE);

        //RecipeSerializer
        Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(TCOTS_Main.MOD_ID, HerbalTableRecipe.ID_STRING),
                HerbalTableRecipe.Serializer.INSTANCE);

    }

}




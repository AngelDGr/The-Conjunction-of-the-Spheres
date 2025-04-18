package TCOTS.items.concoctions.recipes;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.screen.AlchemyTableScreenHandler;
import TCOTS.screen.HerbalTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;

public class ScreenHandlersAndRecipesRegister {
    public static MenuType<AlchemyTableScreenHandler> ALCHEMY_TABLE_SCREEN_HANDLER;
    public static MenuType<HerbalTableScreenHandler> HERBAL_TABLE_SCREEN_HANDLER;

    public static RecipeType<AlchemyTableRecipe> ALCHEMY_TABLE;
    public static RecipeType<HerbalTableRecipe> HERBAL_TABLE;
    public static void registerScreenHandlersAndRecipes()
    {
        ALCHEMY_TABLE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_table"),
                new ExtendedScreenHandlerType<>(
                        AlchemyTableScreenHandler::new,
                        AlchemyTableBlockEntity.AlchemyBlockData.PACKET_CODEC));

        //RecipeSerializer
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, AlchemyTableRecipe.Serializer.ID),
                AlchemyTableRecipe.Serializer.INSTANCE);

        ALCHEMY_TABLE =
                Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, AlchemyTableRecipe.ID_STRING),
                        //TypeInstance
                        AlchemyTableRecipe.Type.INSTANCE);

        HERBAL_TABLE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "herbal_table"),
                new MenuType<>(HerbalTableScreenHandler::new, FeatureFlags.VANILLA_SET));

        HERBAL_TABLE =
                Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, HerbalTableRecipe.ID_STRING),
                        //TypeInstance
                        HerbalTableRecipe.Type.INSTANCE);

        //RecipeSerializer
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, HerbalTableRecipe.ID_STRING),
                HerbalTableRecipe.Serializer.INSTANCE);

    }

}




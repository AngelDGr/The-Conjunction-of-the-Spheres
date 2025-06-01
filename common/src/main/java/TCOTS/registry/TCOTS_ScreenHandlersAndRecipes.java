package TCOTS.registry;

import TCOTS.TCOTS_Registries;
import TCOTS.recipes.AlchemyTableRecipe;
import TCOTS.recipes.HerbalTableRecipe;
import TCOTS.screen.AlchemyTableScreenHandler;
import TCOTS.screen.HerbalTableScreenHandler;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;

public class TCOTS_ScreenHandlersAndRecipes {

    private static RegistrySupplier<MenuType<AlchemyTableScreenHandler>> ALCHEMY_TABLE_SCREEN_HANDLER;
    private static RegistrySupplier<MenuType<HerbalTableScreenHandler>> HERBAL_TABLE_SCREEN_HANDLER;
    private static RegistrySupplier<RecipeType<AlchemyTableRecipe>> ALCHEMY_TABLE;
    private static RegistrySupplier<RecipeType<HerbalTableRecipe>> HERBAL_TABLE;

    public static void initScreenHandlersAndRecipes(){

        ALCHEMY_TABLE_SCREEN_HANDLER = TCOTS_Registries.MENU.register("alchemy_table", ()-> MenuRegistry.ofExtended(AlchemyTableScreenHandler::new));

        //RecipeSerializer
        TCOTS_Registries.RECIPE_SERIALIZER.register(AlchemyTableRecipe.Serializer.ID, () -> AlchemyTableRecipe.Serializer.INSTANCE);

        ALCHEMY_TABLE = TCOTS_Registries.RECIPE_TYPE.register(AlchemyTableRecipe.ID_STRING, ()->AlchemyTableRecipe.Type.INSTANCE);

        HERBAL_TABLE_SCREEN_HANDLER = TCOTS_Registries.MENU.register("herbal_table", ()-> new MenuType<>(HerbalTableScreenHandler::new, FeatureFlags.VANILLA_SET));

        HERBAL_TABLE = TCOTS_Registries.RECIPE_TYPE.register(HerbalTableRecipe.ID_STRING, ()-> HerbalTableRecipe.Type.INSTANCE);

        //RecipeSerializer
        TCOTS_Registries.RECIPE_SERIALIZER.register(HerbalTableRecipe.ID_STRING, ()-> HerbalTableRecipe.Serializer.INSTANCE);
    }


    public static MenuType<? extends AlchemyTableScreenHandler> AlchemyTableScreenHandler(){ return ALCHEMY_TABLE_SCREEN_HANDLER.get(); }
    public static MenuType<HerbalTableScreenHandler> HerbalTableScreenHandler(){ return HERBAL_TABLE_SCREEN_HANDLER.get(); }
    public static RecipeType<AlchemyTableRecipe> AlchemyTable(){ return ALCHEMY_TABLE.get(); }
    public static RecipeType<HerbalTableRecipe> HerbalTable(){ return HERBAL_TABLE.get(); }

}

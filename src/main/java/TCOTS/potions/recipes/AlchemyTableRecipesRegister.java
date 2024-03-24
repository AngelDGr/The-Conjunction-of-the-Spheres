package TCOTS.potions.recipes;

import TCOTS.TCOTS_Main;
import TCOTS.screen.AlchemyTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class AlchemyTableRecipesRegister {
    public static ScreenHandlerType<AlchemyTableScreenHandler> ALCHEMY_TABLE_SCREEN_HANDLER;
    public static void registerPotionRecipes()
    {
        ALCHEMY_TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(TCOTS_Main.MOD_ID, "alchemy_table"),
                new ExtendedScreenHandlerType<>(AlchemyTableScreenHandler::new));

        //RecipeSerializer
        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(TCOTS_Main.MOD_ID, AlchemyTableRecipe.Serializer.ID),
                AlchemyTableRecipe.Serializer.INSTANCE);

//        //RecipeType register                                    //Recipe string/id
        Registry.register(Registries.RECIPE_TYPE, new Identifier(TCOTS_Main.MOD_ID, AlchemyTableRecipe.ID_STRING),
                //TypeInstance
                AlchemyTableRecipe.Type.INSTANCE);


    }

}




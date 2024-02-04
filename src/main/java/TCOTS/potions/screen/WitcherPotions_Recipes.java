package TCOTS.potions.screen;

import TCOTS.TCOTS_Main;
import TCOTS.access.BrewingRecipeRegistryAccess;
import TCOTS.items.TCOTS_Items;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Unique;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class WitcherPotions_Recipes {

    public static ScreenHandlerType<AlchemyTableScreenHandler> ALCHEMY_TABLE_SCREEN_HANDLER;
    public static void registerPotionRecipes()
    {
        ALCHEMY_TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, new Identifier(TCOTS_Main.MOD_ID, "alchemy_table"),
                new ExtendedScreenHandlerType<>(AlchemyTableScreenHandler::new));


        Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(TCOTS_Main.MOD_ID, AlchemyTableRecipe.Serializer.ID),
                AlchemyTableRecipe.Serializer.INSTANCE);

        Registry.register(Registries.RECIPE_TYPE, new Identifier(TCOTS_Main.MOD_ID, AlchemyTableRecipe.Type.ID),
                AlchemyTableRecipe.Type.INSTANCE);
    }

}




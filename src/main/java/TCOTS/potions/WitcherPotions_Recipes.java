package TCOTS.potions;

import TCOTS.access.BrewingRecipeRegistryAccess;
import TCOTS.items.TCOTS_Items;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

public class WitcherPotions_Recipes {

    public static List<WitcherPotions_CustomBrewingRecipe<Potion>> WITCHER_RECIPES_INPUT_POTIONS = Lists.newArrayList();

    public static List<WitcherPotions_CustomBrewingRecipe<Item>> WITCHER_RECIPES_INPUT_ITEMS = Lists.newArrayList();

    public static void registerPotionRecipes()
    {

            registerWitcherPotionRecipe_PotionInput(Potions.AWKWARD, TCOTS_Items.DROWNER_BRAIN, TCOTS_Items.SWALLOW_POTION);
            registerWitcherPotionRecipe_PotionInput(Potions.AWKWARD, TCOTS_Items.DROWNER_TONGUE, TCOTS_Items.KILLER_WHALE_POTION);

            registerWitcherPotionRecipe_ItemInput(TCOTS_Items.SWALLOW_POTION, Items.GUNPOWDER, TCOTS_Items.SWALLOW_SPLASH);
            registerWitcherPotionRecipe_ItemInput(TCOTS_Items.KILLER_WHALE_POTION, Items.GUNPOWDER, TCOTS_Items.KILLER_WHALE_SPLASH);

    }

    public static void registerWitcherPotionRecipe_PotionInput(Potion input, Item item, Item output) {
        WITCHER_RECIPES_INPUT_POTIONS.add(new WitcherPotions_CustomBrewingRecipe<Potion>(input, Ingredient.ofItems(new ItemConvertible[]{item}), output));
    }

    public static void registerWitcherPotionRecipe_ItemInput(Item input, Item item, Item output) {
        WITCHER_RECIPES_INPUT_ITEMS.add(new WitcherPotions_CustomBrewingRecipe<Item>(input, Ingredient.ofItems(new ItemConvertible[]{item}), output));
    }

//    public void getWitcherRecipesInputItems(BrewingRecipeRegistry instance) {
//
//        ((BrewingRecipeRegistryAccess)instance).registerWitcherPotionRecipe_PotionInput(Potions.AWKWARD, TCOTS_Items.DROWNER_BRAIN, TCOTS_Items.SWALLOW_POTION);
//    }


}




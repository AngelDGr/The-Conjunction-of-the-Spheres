package TCOTS.potions;

import TCOTS.access.BrewingRecipeRegistryAccess;
import TCOTS.items.TCOTS_Items;
import com.google.common.collect.Lists;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Unique;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class WitcherPotions_Recipes {

//    public static List<WitcherPotions_CustomBrewingRecipe<Potion>> WITCHER_RECIPES_INPUT_POTIONS = Lists.newArrayList();
//
//    public static List<WitcherPotions_CustomBrewingRecipe<Item>> WITCHER_RECIPES_INPUT_ITEMS = Lists.newArrayList();
//
//    public static List<Ingredient> WITCHER_POTION_TYPES = Lists.newArrayList();
//
//    public static final Predicate<ItemStack> POTION_TYPE_PREDICATE = (stack) -> {
//        Iterator var1 = WITCHER_POTION_TYPES.iterator();
//
//        Ingredient ingredient;
//        do {
//            if (!var1.hasNext()) {
//                return false;
//            }
//            ingredient = (Ingredient)var1.next();
//        } while(!ingredient.test(stack));
//
//        return true;
//    };

    public static void registerPotionRecipes()
    {
//            registerWitcherPotions(TCOTS_Items.DWARVEN_SPIRIT);
//
//
//            registerWitcherPotions(TCOTS_Items.SWALLOW_POTION);
//            registerWitcherPotionRecipe_ItemInput(TCOTS_Items.DWARVEN_SPIRIT, TCOTS_Items.DROWNER_BRAIN, TCOTS_Items.SWALLOW_POTION);
//            registerWitcherPotions(TCOTS_Items.KILLER_WHALE_POTION);
//            registerWitcherPotionRecipe_ItemInput(TCOTS_Items.DWARVEN_SPIRIT, TCOTS_Items.DROWNER_TONGUE, TCOTS_Items.KILLER_WHALE_POTION);
//
//            registerWitcherPotions(TCOTS_Items.SWALLOW_SPLASH);
//            registerWitcherPotionRecipe_ItemInput(TCOTS_Items.SWALLOW_POTION, Items.GUNPOWDER, TCOTS_Items.SWALLOW_SPLASH);
//            registerWitcherPotions(TCOTS_Items.KILLER_WHALE_SPLASH);
//            registerWitcherPotionRecipe_ItemInput(TCOTS_Items.KILLER_WHALE_POTION, Items.GUNPOWDER, TCOTS_Items.KILLER_WHALE_SPLASH);
    }

//    public static void registerWitcherPotionRecipe_PotionInput(Potion input, Item item, Item output) {
//        WITCHER_RECIPES_INPUT_POTIONS.add(new WitcherPotions_CustomBrewingRecipe<Potion>(input, Ingredient.ofItems(new ItemConvertible[]{item}), output));
//    }
//
//    public static void registerWitcherPotionRecipe_ItemInput(Item input, Item item, Item output) {
//        WITCHER_RECIPES_INPUT_ITEMS.add(new WitcherPotions_CustomBrewingRecipe<Item>(input, Ingredient.ofItems(new ItemConvertible[]{item}), output));
//    }
//
//    public static void registerWitcherPotions(Item item) {
//        WITCHER_POTION_TYPES.add(Ingredient.ofItems(new ItemConvertible[]{item}));
//    }

}




package TCOTS.utils;

import TCOTS.items.TCOTS_Items;
import TCOTS.items.components.RecipeTeacherComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class AlchemyFormulaUtil {

    /**
    Return an Alchemy Formula with a recipe
    @param id ID of the recipe to apply
     */
    public static ItemStack setFormula(Identifier id) {
        return setFormula(id, false);
    }

    /**
     Return an Alchemy Formula with a recipe
     @param id ID of the recipe to apply
     @param decoction If it's a decoction applies extra rarity
     */
    public static ItemStack setFormula(Identifier id, boolean decoction) {
        ItemStack stack = new ItemStack(TCOTS_Items.ALCHEMY_FORMULA);

        RecipeTeacherComponent.set(stack, id.toString(), decoction);

        return stack;
    }


    /**
        Check if [Item] it's one from the category of misc alchemy recipes
       @param item The item to check
    */
    public static boolean isMiscItem(Item item){
        return
                item == TCOTS_Items.DWARVEN_SPIRIT || item == TCOTS_Items.ALCOHEST
                || item == TCOTS_Items.WHITE_GULL || item == TCOTS_Items.STAMMELFORDS_DUST
                        || item == TCOTS_Items.AETHER     || item == TCOTS_Items.HYDRAGENUM || item == TCOTS_Items.NIGREDO    || item == TCOTS_Items.QUEBRITH
                        || item == TCOTS_Items.REBIS      || item == TCOTS_Items.RUBEDO     || item == TCOTS_Items.VERMILION  || item == TCOTS_Items.VITRIOL;
    }
}

package TCOTS.utils;

import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.components.RecipeTeacherComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AlchemyFormulaUtil {

    /**
    Return an Alchemy Formula with a recipe
    @param id ID of the recipe to apply
     */
    public static ItemStack setFormula(ResourceLocation id) {
        return setFormula(id, false);
    }

    /**
     Return an Alchemy Formula with a recipe
     @param id ID of the recipe to apply
     @param decoction If it's a decoction applies extra rarity
     */
    public static ItemStack setFormula(ResourceLocation id, boolean decoction) {
        ItemStack stack = new ItemStack(TCOTS_Items_Fabric.ALCHEMY_FORMULA);

        RecipeTeacherComponent.set(stack, id.toString(), decoction);

        return stack;
    }


    /**
        Check if [Item] it's one from the category of misc alchemy recipes
       @param item The item to check
    */
    public static boolean isMiscItem(Item item){
        return
                item == TCOTS_Items_Fabric.DWARVEN_SPIRIT || item == TCOTS_Items_Fabric.ALCOHEST
                || item == TCOTS_Items_Fabric.WHITE_GULL || item == TCOTS_Items_Fabric.STAMMELFORDS_DUST
                        || item == TCOTS_Items_Fabric.AETHER     || item == TCOTS_Items_Fabric.HYDRAGENUM || item == TCOTS_Items_Fabric.NIGREDO    || item == TCOTS_Items_Fabric.QUEBRITH
                        || item == TCOTS_Items_Fabric.REBIS      || item == TCOTS_Items_Fabric.RUBEDO     || item == TCOTS_Items_Fabric.VERMILION  || item == TCOTS_Items_Fabric.VITRIOL;
    }
}

package TCOTS.utils;

import TCOTS.registry.TCOTS_Items;
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
        ItemStack stack = new ItemStack(TCOTS_Items.ALCHEMY_FORMULA.get());

        RecipeTeacherComponent.set(stack, id.toString(), decoction);

        return stack;
    }


    /**
        Check if [Item] it's one from the category of misc alchemy recipes
       @param item The item to check
    */
    @SuppressWarnings("all")
    public static boolean isMiscItem(Item item){
        return
                item == TCOTS_Items.DWARVEN_SPIRIT.get() || item == TCOTS_Items.ALCOHEST.get()
                || item == TCOTS_Items.WHITE_GULL.get() || item == TCOTS_Items.STAMMELFORDS_DUST.get()
                        || item == TCOTS_Items.AETHER.get()     || item == TCOTS_Items.VITRIOL.get() || item == TCOTS_Items.VERMILION.get()    || item == TCOTS_Items.HYDRAGENUM.get()
                        || item == TCOTS_Items.QUEBRITH.get()      || item == TCOTS_Items.RUBEDO.get()     || item == TCOTS_Items.REBIS.get()  || item == TCOTS_Items.NIGREDO.get();
    }
}

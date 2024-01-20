package TCOTS.access;

import TCOTS.potions.WitcherPotions_CustomBrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

public interface BrewingRecipeRegistryAccess {

    public void registerWitcherPotionRecipe_PotionInput(Potion input, Item item, Item output);


    public void registerWitcherPotionRecipe_ItemInput(Item input, Item item, Item output);


}

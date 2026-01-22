package mors.fabric.tcots.compat;

import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.registry.TCOTS_ItemsAttributes;
import dev.emi.trinkets.api.TrinketsApi;
import mors.fabric.tcots.items.WitcherMedallionItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class TCOTS_TrinketsCompat {

    public static Item MANTICORE_MEDALLION = new WitcherMedallionItem(
            new Item.Properties().stacksTo(1).rarity(Rarity.EPIC),
            ArmorSet.MANTICORE, TCOTS_ItemsAttributes.Accessories.MANTICORE_MEDALLION.modifiers
    );

    public static boolean isWearingAccessoryItem(final LivingEntity player, final Item medallion){
        return TrinketsApi.getTrinketComponent(player).get().isEquipped(stack -> ItemStack.isSameItem(stack, medallion.getDefaultInstance()));
    }
}

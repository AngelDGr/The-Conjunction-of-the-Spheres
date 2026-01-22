package mors.tcots.items;

import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.items.armor.set.IsArmorSet;
import net.minecraft.world.item.Item;

public class WitcherMedallionItem extends Item implements IsArmorSet {

    private final ArmorSet set;

    public WitcherMedallionItem(final Properties properties, final ArmorSet set) {
        super(properties);
        this.set=set;
    }

    @Override
    public ArmorSet getSet() {
        return set;
    }
}

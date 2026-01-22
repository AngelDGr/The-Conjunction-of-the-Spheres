package mors.tcots.client.geo.model.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.HerbalTableBlock;
import mors.tcots.block.entity.HerbalTableBlockEntity;
import mors.tcots.client.geo.model.defaulted.DefaultedBlockModelNoAnimation;
import net.minecraft.resources.ResourceLocation;

public class HerbalTableModel extends DefaultedBlockModelNoAnimation<HerbalTableBlockEntity> {
    public HerbalTableModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "herbal_table"));
    }

    public static class Item extends DefaultedBlockModelNoAnimation<HerbalTableBlock.Item> {
        public Item() {
            super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "herbal_table"));
        }
    }
}

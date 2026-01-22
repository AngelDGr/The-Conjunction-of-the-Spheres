package mors.tcots.client.geo.model.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.GiantAnchorBlock;
import mors.tcots.block.entity.GiantAnchorBlockEntity;
import mors.tcots.client.geo.model.defaulted.DefaultedBlockModelNoAnimation;
import net.minecraft.resources.ResourceLocation;

public class GiantAnchorModel extends DefaultedBlockModelNoAnimation<GiantAnchorBlockEntity> {
    public GiantAnchorModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "giant_anchor"));
    }

    public static class Item extends DefaultedBlockModelNoAnimation<GiantAnchorBlock.Item> {

        public Item() {
            super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "giant_anchor"));
        }
    }
}

package mors.tcots.client.geo.model.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.MonsterNestBlock;
import mors.tcots.block.entity.MonsterNestBlockEntity;
import mors.tcots.client.geo.model.defaulted.DefaultedBlockModelNoAnimation;
import net.minecraft.resources.ResourceLocation;

public class MonsterNestModel extends DefaultedBlockModelNoAnimation<MonsterNestBlockEntity> {
    public MonsterNestModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "monster_nest"));
    }

    public static class Item extends DefaultedBlockModelNoAnimation<MonsterNestBlock.Item> {
        public Item() {
            super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "monster_nest"));
        }
    }
}

package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.items.blocks.MonsterNestItem;
import TCOTS.items.blocks.NestSkullItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MonsterNestItemModel  extends GeoModel<MonsterNestItem> {
    @Override
    public Identifier getModelResource(MonsterNestItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/monster_nest.geo.json");
    }

    @Override
    public Identifier getTextureResource(MonsterNestItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/monster_nest.png");
    }

    @Override
    public Identifier getAnimationResource(MonsterNestItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}

package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MonsterNestModel extends GeoModel<MonsterNestBlockEntity> {
    @Override
    public Identifier getModelResource(MonsterNestBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/block/monster_nest.geo.json");
    }

    @Override
    public Identifier getTextureResource(MonsterNestBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/monster_nest.png");
    }

    @Override
    public Identifier getAnimationResource(MonsterNestBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}

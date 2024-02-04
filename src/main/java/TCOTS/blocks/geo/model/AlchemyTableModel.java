package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class AlchemyTableModel extends GeoModel<AlchemyTableBlockEntity> {
    @Override
    public Identifier getModelResource(AlchemyTableBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/alchemy_table.geo.json");
    }

    @Override
    public Identifier getTextureResource(AlchemyTableBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/alchemy_table.png");
    }

    @Override
    public Identifier getAnimationResource(AlchemyTableBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderLayer getRenderType(AlchemyTableBlockEntity animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }
}

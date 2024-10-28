package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.AlchemyTableItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AlchemyTableItemModel extends GeoModel<AlchemyTableItem> {
    @Override
    public Identifier getModelResource(AlchemyTableItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/block/alchemy_table.geo.json");
    }

    @Override
    public Identifier getTextureResource(AlchemyTableItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/alchemy_table.png");
    }

    @Override
    public Identifier getAnimationResource(AlchemyTableItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderLayer getRenderType(AlchemyTableItem animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(AlchemyTableItem animatable, long instanceId, AnimationState<AlchemyTableItem> animationState) {
        CoreGeoBone book = getAnimationProcessor().getBone("Book");
        book.setHidden(true);
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}

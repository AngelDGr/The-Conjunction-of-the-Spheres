package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.AlchemyTableItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AlchemyTableItemModel extends GeoModel<AlchemyTableItem> {
    @Override
    public ResourceLocation getModelResource(AlchemyTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/alchemy_table.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AlchemyTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/alchemy_table.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AlchemyTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderType getRenderType(AlchemyTableItem animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(AlchemyTableItem animatable, long instanceId, AnimationState<AlchemyTableItem> animationState) {
        GeoBone book = getAnimationProcessor().getBone("Book");
        book.setHidden(true);
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}

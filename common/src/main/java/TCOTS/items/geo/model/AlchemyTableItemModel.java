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
    public ResourceLocation getModelResource(final AlchemyTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/alchemy_table.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final AlchemyTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/alchemy_table.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final AlchemyTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderType getRenderType(final AlchemyTableItem animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(final AlchemyTableItem animatable, final long instanceId, final AnimationState<AlchemyTableItem> animationState) {
        final GeoBone book = getAnimationProcessor().getBone("Book");
        book.setHidden(true);
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}

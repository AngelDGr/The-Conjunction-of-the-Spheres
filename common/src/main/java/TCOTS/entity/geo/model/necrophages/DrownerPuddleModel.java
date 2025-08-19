package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DrownerPuddleModel extends GeoModel<DrownerPuddleEntity> {
    @Override
    public ResourceLocation getModelResource(final DrownerPuddleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/drowner_puddle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final DrownerPuddleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner_puddle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final DrownerPuddleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/drowner_puddle.animation.json");
    }

    @Override
    public RenderType getRenderType(final DrownerPuddleEntity animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}

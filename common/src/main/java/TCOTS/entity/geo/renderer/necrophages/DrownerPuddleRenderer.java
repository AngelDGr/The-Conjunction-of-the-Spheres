package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DrownerPuddleModel;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class DrownerPuddleRenderer extends GeoEntityRenderer<DrownerPuddleEntity> {
    public DrownerPuddleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DrownerPuddleModel());
    }

    @Override
    public ResourceLocation getTextureLocation(DrownerPuddleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner_puddle.png");
    }

    @Override
    public Color getRenderColor(DrownerPuddleEntity animatable, float partialTick, int packedLight) {
        int waterColor = BiomeColors.getAverageWaterColor(animatable.level(), animatable.blockPosition());

        return Color.ofOpaque(waterColor);
    }
}

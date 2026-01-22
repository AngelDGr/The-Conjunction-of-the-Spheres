package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.misc.DrownerPuddleModel;
import mors.tcots.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class DrownerPuddleRenderer extends GeoEntityRenderer<DrownerPuddleEntity> {
    public DrownerPuddleRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new DrownerPuddleModel());
    }

    @Override
    public Color getRenderColor(final DrownerPuddleEntity animatable, final float partialTick, final int packedLight) {
        final int waterColor = BiomeColors.getAverageWaterColor(animatable.level(), animatable.blockPosition());

        return Color.ofOpaque(waterColor);
    }
}

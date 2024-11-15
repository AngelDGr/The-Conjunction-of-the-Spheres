package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DrownerPuddleModel;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class DrownerPuddleRenderer extends GeoEntityRenderer<DrownerPuddleEntity> {
    public DrownerPuddleRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DrownerPuddleModel());
    }

    @Override
    public Identifier getTextureLocation(DrownerPuddleEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner_puddle.png");
    }

    @Override
    public Color getRenderColor(DrownerPuddleEntity animatable, float partialTick, int packedLight) {
        int waterColor = BiomeColors.getWaterColor(animatable.getWorld(), animatable.getBlockPos());

        return Color.ofOpaque(waterColor);
    }
}

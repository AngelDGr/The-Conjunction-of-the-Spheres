package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DrownerPuddleModel;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrownerPuddleRenderer extends GeoEntityRenderer<DrownerPuddleEntity> {
    public DrownerPuddleRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DrownerPuddleModel());
    }

    @Override
    public Identifier getTextureLocation(DrownerPuddleEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner_puddle.png");
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, DrownerPuddleEntity animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        int waterColor = BiomeColors.getWaterColor(animatable.getWorld(), new BlockPos((int) animatable.getX(), (int) animatable.getY(), (int) animatable.getZ()));
        float redWater = ((waterColor >> 16) & 255) / 255.0F;
        float greenWater = ((waterColor >> 8) & 255) / 255.0F;
        float blueWater = (waterColor & 255) / 255.0F;

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, redWater, greenWater, blueWater, alpha);
    }

}

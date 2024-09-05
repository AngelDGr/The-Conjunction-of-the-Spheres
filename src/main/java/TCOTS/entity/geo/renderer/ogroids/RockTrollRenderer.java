package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.ogroids.RockTrollModel;
import TCOTS.entity.ogroids.RockTrollEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class RockTrollRenderer extends GeoEntityRenderer<RockTrollEntity> {
    public RockTrollRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new RockTrollModel());

        //Rabid eyes
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/rock_rabid_eyes.png");

            @Override
            public void render(MatrixStack poseStack, RockTrollEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if(animatable.isRabid()){
                    RenderLayer armorRenderType = RenderLayer.getArmorCutoutNoCull(TEXTURE);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                            1, 1, 1, 1f);
                }
            }
        });
    }
}

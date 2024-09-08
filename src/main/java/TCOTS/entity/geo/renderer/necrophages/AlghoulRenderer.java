package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.AlghoulModel;
import TCOTS.entity.necrophages.AlghoulEntity;
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

public class AlghoulRenderer extends GeoEntityRenderer<AlghoulEntity> {
    public AlghoulRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AlghoulModel());

        this.shadowRadius = 0.9f;

        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul_regen_layer.png");

            @Override
            public void render(MatrixStack poseStack, AlghoulEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.getIsRegenerating()) {
                    float f = (float) animatable.age + partialTick;
                    RenderLayer armorRenderType = RenderLayer.getEnergySwirl(TEXTURE, this.getEnergySwirlX(f) % 1.0f, f * 0.01f % 1.0f);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                            1, 1, 1, 0.5f);
                }
            }

            private float getEnergySwirlX(float partialAge) {
                return partialAge * 0.00001f;
            }
        });

        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul_spikes_layer.png");

            @Override
            public void render(MatrixStack poseStack, AlghoulEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.getIsSpiked()) {
                    RenderLayer armorRenderType = RenderLayer.getArmorCutoutNoCull(TEXTURE);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                            1, 1, 1, 1f);
                }
            }
        });
    }
}

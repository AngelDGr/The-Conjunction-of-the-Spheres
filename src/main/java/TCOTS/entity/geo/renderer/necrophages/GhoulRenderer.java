package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.GhoulModel;
import TCOTS.entity.necrophages.GhoulEntity;
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

public class GhoulRenderer extends GeoEntityRenderer<GhoulEntity> {
    public GhoulRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GhoulModel());

        this.shadowRadius = 0.7f;

        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/ghoul/ghoul_regen_layer.png");

            @Override
            public void render(MatrixStack poseStack, GhoulEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
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
    }
}

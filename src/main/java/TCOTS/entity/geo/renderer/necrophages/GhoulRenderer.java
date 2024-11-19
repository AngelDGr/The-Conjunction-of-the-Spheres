package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.GhoulModel;
import TCOTS.entity.necrophages.GhoulEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

import java.util.Objects;

public class GhoulRenderer extends GeoEntityRenderer<GhoulEntity> {
    private static final String MOUTH = "mouth_item";

    protected ItemStack mouthItem;

    public GhoulRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GhoulModel());

        this.shadowRadius = 0.7f;

        //Regen effect layer
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/ghoul/ghoul_regen_layer.png");

            @Override
            public void render(MatrixStack poseStack, GhoulEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.getIsRegenerating()) {
                    float f = (float) animatable.age + partialTick;
                    RenderLayer armorRenderType = RenderLayer.getEnergySwirl(TEXTURE, this.getEnergySwirlX(f) % 1.0f, f * 0.01f % 1.0f);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                            Color.ofRGBA(1f, 1f, 1f, 0.5f).argbInt()
                    );
                }
            }

            private float getEnergySwirlX(float partialAge) {
                return partialAge * 0.00001f;
            }
        });

        //Item on mouth renderer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, GhoulEntity animatable) {
                // Retrieve the items in the entity's mouth for the relevant bone
                return Objects.equals(bone.getName(), MOUTH) ?  GhoulRenderer.this.mouthItem : null;
            }

            @Override
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, GhoulEntity animatable) {
                // Apply the camera transform for the mouth
                return Objects.equals(bone.getName(), MOUTH) ? ModelTransformationMode.THIRD_PERSON_RIGHT_HAND: ModelTransformationMode.NONE;
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, GhoulEntity troll,
                                              VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == GhoulRenderer.this.mouthItem) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));
                }

                super.renderStackForBone(poseStack, bone, stack, troll, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public void preRender(MatrixStack poseStack, GhoulEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        this.mouthItem = animatable.getMainHandStack();
    }
}

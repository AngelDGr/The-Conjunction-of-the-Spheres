package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.AlghoulModel;
import TCOTS.entity.necrophages.AlghoulEntity;
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

public class AlghoulRenderer extends GeoEntityRenderer<AlghoulEntity> {
    private static final String MOUTH = "mouth_item";

    protected ItemStack mouthItem;

    public AlghoulRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AlghoulModel());

        this.shadowRadius = 0.9f;

        //Regen effect layer
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul_regen_layer.png");

            @Override
            public void render(MatrixStack poseStack, AlghoulEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.getIsRegenerating()) {
                    float f = (float) animatable.age + partialTick;
                    RenderLayer armorRenderType = RenderLayer.getEnergySwirl(TEXTURE, this.getEnergySwirlX(f) % 1.0f, f * 0.01f % 1.0f);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                    1
//                            1,
//                            1,
//                            1, 0.5f
                            );
                }
            }

            private float getEnergySwirlX(float partialAge) {
                return partialAge * 0.00001f;
            }
        });

        //Spikes layer
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul_spikes_layer.png");

            @Override
            public void render(MatrixStack poseStack, AlghoulEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.getIsSpiked()) {
                    RenderLayer armorRenderType = RenderLayer.getArmorCutoutNoCull(TEXTURE);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                            Color.ofARGB(1, 1, 1, 0.5f).argbInt()
//                            1,
//                            1,
//                            1,
//                            1f
                    );
                }
            }
        });

        //Item on mouth renderer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, AlghoulEntity animatable) {
                // Retrieve the items in the entity's mouth for the relevant bone
                return Objects.equals(bone.getName(), MOUTH) ?  AlghoulRenderer.this.mouthItem : null;
            }

            @Override
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, AlghoulEntity animatable) {
                // Apply the camera transform for the mouth
                return Objects.equals(bone.getName(), MOUTH) ? ModelTransformationMode.THIRD_PERSON_RIGHT_HAND: ModelTransformationMode.NONE;
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, AlghoulEntity troll,
                                              VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == AlghoulRenderer.this.mouthItem) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));
                }

                super.renderStackForBone(poseStack, bone, stack, troll, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });

    }

    @Override
    public void preRender(MatrixStack poseStack, AlghoulEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        this.mouthItem = animatable.getMainHandStack();
    }
}

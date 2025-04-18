package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.AlghoulModel;
import TCOTS.entity.necrophages.AlghoulEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

import java.util.Objects;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AlghoulRenderer extends GeoEntityRenderer<AlghoulEntity> {
    private static final String MOUTH = "mouth_item";

    protected ItemStack mouthItem;

    public AlghoulRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AlghoulModel());

        this.shadowRadius = 0.9f;

        //Regen effect layer
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul_regen_layer.png");

            @Override
            public void render(PoseStack poseStack, AlghoulEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.getIsRegenerating()) {
                    float f = (float) animatable.tickCount + partialTick;
                    RenderType armorRenderType = RenderType.energySwirl(TEXTURE, this.getEnergySwirlX(f) % 1.0f, f * 0.01f % 1.0f);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                            Color.ofRGBA(1, 1f, 1f, 0.5f).argbInt()
                    );
                }
            }

            private float getEnergySwirlX(float partialAge) {
                return partialAge * 0.00001f;
            }
        });

        //Spikes layer
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul_spikes_layer.png");

            @Override
            public void render(PoseStack poseStack, AlghoulEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if (animatable.getIsSpiked()) {
                    RenderType armorRenderType = RenderType.armorCutoutNoCull(TEXTURE);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                            Color.ofRGB(1f, 1f, 1f).argbInt()
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
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, AlghoulEntity animatable) {
                // Apply the camera transform for the mouth
                return Objects.equals(bone.getName(), MOUTH) ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND: ItemDisplayContext.NONE;
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, AlghoulEntity troll,
                                              MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == AlghoulRenderer.this.mouthItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
                }

                super.renderStackForBone(poseStack, bone, stack, troll, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });

    }

    @Override
    public void preRender(PoseStack poseStack, AlghoulEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        this.mouthItem = animatable.getMainHandItem();
    }
}

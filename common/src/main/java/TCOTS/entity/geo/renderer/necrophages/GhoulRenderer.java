package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.GhoulModel;
import TCOTS.entity.monsters.necrophages.GhoulEntity;
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

public class GhoulRenderer extends GeoEntityRenderer<GhoulEntity> {
    private static final String MOUTH = "mouth_item";

    protected ItemStack mouthItem;

    public GhoulRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new GhoulModel());

        this.shadowRadius = 0.7f;

        //Regen effect layer
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/ghoul/ghoul_regen_layer.png");

            @Override
            public void render(final PoseStack poseStack, final GhoulEntity animatable, final BakedGeoModel bakedModel, final RenderType renderType, final MultiBufferSource bufferSource, final VertexConsumer buffer, final float partialTick, final int packedLight, final int packedOverlay) {
                if (animatable.getIsRegenerating()) {
                    final float f = (float) animatable.tickCount + partialTick;
                    final RenderType armorRenderType = RenderType.energySwirl(TEXTURE, this.getEnergySwirlX(f) % 1.0f, f * 0.01f % 1.0f);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                            Color.ofRGBA(1f, 1f, 1f, 0.5f).argbInt()
                    );
                }
            }

            private float getEnergySwirlX(final float partialAge) {
                return partialAge * 0.00001f;
            }
        });

        //Item on mouth renderer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(final GeoBone bone, final GhoulEntity animatable) {
                // Retrieve the items in the entity's mouth for the relevant bone
                return Objects.equals(bone.getName(), MOUTH) ?  GhoulRenderer.this.mouthItem : null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(final GeoBone bone, final ItemStack stack, final GhoulEntity animatable) {
                // Apply the camera transform for the mouth
                return Objects.equals(bone.getName(), MOUTH) ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND: ItemDisplayContext.NONE;
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(final PoseStack poseStack, final GeoBone bone, final ItemStack stack, final GhoulEntity troll,
                                              final MultiBufferSource bufferSource, final float partialTick, final int packedLight, final int packedOverlay) {
                if (stack == GhoulRenderer.this.mouthItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));
                }

                super.renderStackForBone(poseStack, bone, stack, troll, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });
    }

    @Override
    public void preRender(final PoseStack poseStack, final GhoulEntity animatable, final BakedGeoModel model, @Nullable final MultiBufferSource bufferSource, @Nullable final VertexConsumer buffer, final boolean isReRender, final float partialTick, final int packedLight, final int packedOverlay, final int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        this.mouthItem = animatable.getMainHandItem();
    }
}

package mors.tcots.client.geo.renderer.entity.ogroids;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.entity.ogroid.IceGiantModel;
import mors.tcots.entity.monsters.ogroids.IceGiantEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class IceGiantRenderer extends GeoEntityRenderer<IceGiantEntity> {
    private static final String LEFT_HAND = "left_hand";
    private static final String RIGHT_HAND = "right_hand";
    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;
    public IceGiantRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new IceGiantModel());
        this.shadowRadius = 1.2f;

        //Item on hand renderer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(final GeoBone bone, final IceGiantEntity animatable) {
                // Retrieve the items in the entity's hands for the relevant bone
                return switch (bone.getName()) {
                    case LEFT_HAND -> animatable.isLeftHanded() ?
                            IceGiantRenderer.this.mainHandItem : IceGiantRenderer.this.offhandItem;
                    case RIGHT_HAND -> animatable.isLeftHanded() ?
                            IceGiantRenderer.this.offhandItem : IceGiantRenderer.this.mainHandItem;
                    default -> null;
                };
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(final GeoBone bone, final ItemStack stack, final IceGiantEntity animatable) {
                // Apply the camera transform for the given hand
                return switch (bone.getName()) {
                    case LEFT_HAND, RIGHT_HAND -> ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                    default -> ItemDisplayContext.NONE;
                };
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(final PoseStack poseStack, final GeoBone bone, final ItemStack stack, final IceGiantEntity troll,
                                              final MultiBufferSource bufferSource, final float partialTick, final int packedLight, final int packedOverlay) {
                if (stack == IceGiantRenderer.this.mainHandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem)
                        poseStack.translate(0, 0.125, -0.25);

                    poseStack.scale(2, 2, 2);
                }
                else if (stack == IceGiantRenderer.this.offhandItem) {
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0, 0.125, 0.25);
                        poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    }

                    poseStack.scale(2, 2, 2);
                }

                super.renderStackForBone(poseStack, bone, stack, troll, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });

        //Sleep layer
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/monster/ogroid/ice_giant_sleep.png");

            @Override
            public void render(final PoseStack poseStack, final IceGiantEntity animatable, final BakedGeoModel bakedModel, @Nullable final RenderType renderType, final MultiBufferSource bufferSource, @Nullable final VertexConsumer buffer, final float partialTick, final int packedLight, final int packedOverlay) {
                if (!animatable.hasPose(Pose.SITTING) || animatable.isInvisible() || animatable.isGiantWakingUp())
                    return;

                final RenderType sleepRenderType = RenderType.entityCutoutNoCull(TEXTURE);
                this.getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, sleepRenderType, bufferSource.getBuffer(sleepRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, Color.WHITE.argbInt());
            }
        });
    }

    @Override
    public void preRender(final PoseStack poseStack, final IceGiantEntity animatable, final BakedGeoModel model, @Nullable final MultiBufferSource bufferSource, @Nullable final VertexConsumer buffer, final boolean isReRender, final float partialTick, final int packedLight, final int packedOverlay, final int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        this.mainHandItem = animatable.getMainHandItem();
        this.offhandItem = animatable.getOffhandItem();
    }
}

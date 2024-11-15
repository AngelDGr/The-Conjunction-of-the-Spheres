package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.ogroids.ForestTrollModel;
import TCOTS.entity.ogroids.ForestTrollEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.Color;

public class ForestTrollRenderer extends GeoEntityRenderer<ForestTrollEntity> {
    private static final String LEFT_HAND = "left_hand";
    private static final String RIGHT_HAND = "right_hand";
    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;
    public ForestTrollRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ForestTrollModel());
        this.shadowRadius = 0.75f;

        //Rabid eyes
        addRenderLayer(new GeoRenderLayer<>(this) {
            private static final Identifier TEXTURE = Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/forest_rabid_eyes.png");

            @Override
            public void render(MatrixStack poseStack, ForestTrollEntity animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                if(animatable.isRabid()){
                    RenderLayer armorRenderType = RenderLayer.getArmorCutoutNoCull(TEXTURE);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                            bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.DEFAULT_UV,
                            Color.ofARGB(1f,1f,1f,1f).argbInt());
                }
            }
        });

        //Item on hand renderer
        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, ForestTrollEntity animatable) {
                // Retrieve the items in the entity's hands for the relevant bone
                return switch (bone.getName()) {
                    case LEFT_HAND -> animatable.isLeftHanded() ?
                            ForestTrollRenderer.this.mainHandItem : ForestTrollRenderer.this.offhandItem;
                    case RIGHT_HAND -> animatable.isLeftHanded() ?
                            ForestTrollRenderer.this.offhandItem : ForestTrollRenderer.this.mainHandItem;
                    default -> null;
                };
            }

            @Override
            protected ModelTransformationMode getTransformTypeForStack(GeoBone bone, ItemStack stack, ForestTrollEntity animatable) {
                // Apply the camera transform for the given hand
                return switch (bone.getName()) {
                    case LEFT_HAND, RIGHT_HAND -> ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
                    default -> ModelTransformationMode.NONE;
                };
            }

            // Do some quick render modifications depending on what the item is
            @Override
            protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, ForestTrollEntity troll,
                                              VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
                if (stack == ForestTrollRenderer.this.mainHandItem) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem)
                        poseStack.translate(0, 0.125, -0.25);
                }
                else if (stack == ForestTrollRenderer.this.offhandItem) {
                    poseStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90f));

                    if (stack.getItem() instanceof ShieldItem) {
                        poseStack.translate(0, 0.125, 0.25);
                        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                    }
                }

                super.renderStackForBone(poseStack, bone, stack, troll, bufferSource, partialTick, packedLight, packedOverlay);
            }
        });

    }

    @Override
    public void preRender(MatrixStack poseStack, ForestTrollEntity animatable, BakedGeoModel model, @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);

        this.mainHandItem = animatable.getMainHandStack();
        this.offhandItem = animatable.getOffHandStack();
    }
}

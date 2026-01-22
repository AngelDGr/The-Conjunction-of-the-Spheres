package mors.tcots.client.geo.renderer.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.SkeletonBlock;
import mors.tcots.block.WintersBladeSkeletonBlock;
import mors.tcots.block.entity.WintersBladeSkeletonBlockEntity;
import mors.tcots.client.geo.model.block.WintersBladeSkeletonModel;
import mors.tcots.registry.TCOTS_Items;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.specialty.DynamicGeoBlockRenderer;
import software.bernie.geckolib.renderer.specialty.DynamicGeoItemRenderer;

@SuppressWarnings("unused")
public class WintersBladeSkeletonRenderer extends DynamicGeoBlockRenderer<WintersBladeSkeletonBlockEntity> {
    private final ItemRenderer itemRenderer;
    public WintersBladeSkeletonRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new WintersBladeSkeletonModel());
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(final WintersBladeSkeletonBlockEntity animatable, final float partialTick, final PoseStack poseStack, final MultiBufferSource bufferSource, final int packedLight, final int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

        poseStack.pushPose();

        final int rot = animatable.getBlockState().getValue(SkeletonBlock.ROTATION);
        final float angle = rot * 22.5F;

        //Move to block center
        poseStack.translate(0.5, 0.5, 0.5);

        //Apply block rotation (Y axis)
        poseStack.mulPose(Axis.YP.rotationDegrees(-angle));

        //Move item relative to facing
        poseStack.translate(0.0, -0.2, -0.2); // forward offset

        //Item-specific rotation
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(135.0F));

        //Scale
        poseStack.scale(0.75f, 0.75f, 0.75f);
        this.itemRenderer.renderStatic(TCOTS_Items.WINTERS_BLADE.get().getDefaultInstance(),
                ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferSource,
                animatable.getLevel(), 1);
        poseStack.popPose();
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(final GeoBone bone, final WintersBladeSkeletonBlockEntity animatable, final float partialTick) {
        return getArmorBones(bone);
    }

    public static class Item extends DynamicGeoItemRenderer<WintersBladeSkeletonBlock.Item> {
        public Item() {
            super(new WintersBladeSkeletonModel.Item());
        }

        @Override
        protected @Nullable ResourceLocation getTextureOverrideForBone(final GeoBone bone, final WintersBladeSkeletonBlock.Item animatable, final float partialTick) {
            return getArmorBones(bone);
        }
    }

    private static ResourceLocation getArmorBones(final GeoBone bone){
        return switch (bone.getName()){
            case "armorHead",
                 "armorBody", "armorLeftArm", "armorRightArm",
                 "armorRightLeg", "armorRightBoot",
                 "armorLeftLeg", "armorLeftBoot" ->
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/models/armor/warriors_leather_decayed.png");
            default -> null;
        };
    }
}

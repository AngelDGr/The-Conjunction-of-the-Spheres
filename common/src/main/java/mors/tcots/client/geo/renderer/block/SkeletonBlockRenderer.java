package mors.tcots.client.geo.renderer.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.SkeletonBlock;
import mors.tcots.block.entity.SkeletonBlockEntity;
import mors.tcots.client.geo.model.block.SkeletonBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.specialty.DynamicGeoBlockRenderer;

@SuppressWarnings("unused")
public class SkeletonBlockRenderer extends DynamicGeoBlockRenderer<SkeletonBlockEntity> {
    public SkeletonBlockRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new SkeletonBlockModel());
    }

    @Override
    protected @Nullable ResourceLocation getTextureOverrideForBone(final GeoBone bone, final SkeletonBlockEntity animatable, final float partialTick) {
        return switch (bone.getName()){
            case "armorHead",
                 "armorBody", "armorLeftArm", "armorRightArm",
                 "armorRightLeg", "armorRightBoot",
                 "armorLeftLeg", "armorLeftBoot" ->
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/models/armor/warriors_leather_decayed.png");
            default -> null;
        };
    }

    public static class Item extends GeoItemRenderer<SkeletonBlock.Item> {
        public Item() {
            super(new SkeletonBlockModel.Item());
        }
    }
}

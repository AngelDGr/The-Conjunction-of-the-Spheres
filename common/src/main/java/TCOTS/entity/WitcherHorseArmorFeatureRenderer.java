package TCOTS.entity;

import TCOTS.items.armor.WitcherHorseArmorItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.util.Color;

public class WitcherHorseArmorFeatureRenderer extends RenderLayer<Horse, HorseModel<Horse>> {
    private final HorseModel<Horse> model;
    public WitcherHorseArmorFeatureRenderer(RenderLayerParent<Horse, HorseModel<Horse>> context) {
        super(context);
        this.model=new HorseArmorModel<>(HorseArmorModel.createBodyMesh(new CubeDeformation(0.1f)).getRoot().bake(64,96));
    }

    @Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, Horse horseEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        ItemStack itemStack = horseEntity.getBodyArmorItem();
        if (!(itemStack.getItem() instanceof WitcherHorseArmorItem horseArmorItem)) {
            return;
        }

        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(horseEntity, limbAngle, limbDistance, tickDelta);
        this.model.setupAnim(horseEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(horseArmorItem.getOuterTexture()));
        this.model.renderToBuffer(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, Color.ofARGB(1.0f, 1.0f, 1.0f, 1.0f).argbInt());
    }

    private static class HorseArmorModel<T extends AbstractHorse> extends HorseModel<T> {

        protected static final String HEAD_PARTS = "head_parts";
        private static final String RIGHT_HIND_BABY_LEG = "right_hind_baby_leg";
        private static final String LEFT_HIND_BABY_LEG = "left_hind_baby_leg";
        private static final String LEFT_FRONT_BABY_LEG = "left_front_baby_leg";
        private static final String RIGHT_FRONT_BABY_LEG = "right_front_baby_leg";
        private static final String SADDLE = "saddle";
        private static final String LEFT_SADDLE_MOUTH = "left_saddle_mouth";
        private static final String LEFT_SADDLE_LINE = "left_saddle_line";
        private static final String RIGHT_SADDLE_MOUTH = "right_saddle_mouth";
        private static final String RIGHT_SADDLE_LINE = "right_saddle_line";
        private static final String HEAD_SADDLE = "head_saddle";
        private static final String MOUTH_SADDLE_WRAP = "mouth_saddle_wrap";
        public HorseArmorModel(ModelPart root) {
            super(root);
        }

        public static MeshDefinition createBodyMesh(CubeDeformation dilation) {
            MeshDefinition modelData = new MeshDefinition();
            PartDefinition modelPartData = modelData.getRoot();
            PartDefinition modelPartData2 = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(0, 32).addBox(-5.0f, -8.0f, -17.0f, 10.0f, 13.0f, 22.0f, new CubeDeformation(0.45f)), PartPose.offset(0.0f, 11.0f, 5.0f));




            PartDefinition modelPartData3 = modelPartData.addOrReplaceChild(HEAD_PARTS, CubeListBuilder.create().texOffs(0, 70).addBox(-2.05f, -11.0f, -2.0f, 4.0f, 17.0f, 9.0f, new CubeDeformation(0.05f)), PartPose.offsetAndRotation(0.0f, 4.0f, -12.0f, 0.5235988f, 0.0f, 0.0f));

            PartDefinition modelPartData4 = modelPartData3.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(0, 13).addBox(-3.0f, -11.0f, -2.0f, 6.0f, 5.0f, 7.0f, dilation), PartPose.ZERO);
            modelPartData3.addOrReplaceChild(PartNames.MANE, CubeListBuilder.create().texOffs(56, 36).addBox(-1.0f, -11.0f, 5.01f, 2.0f, 16.0f, 2.0f, dilation), PartPose.ZERO);
            modelPartData3.addOrReplaceChild("upper_mouth", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0f, -11.0f, -7.0f, 4.0f, 5.0f, 5.0f, dilation), PartPose.ZERO);
            modelPartData.addOrReplaceChild(PartNames.LEFT_HIND_LEG, CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation), PartPose.offset(4.0f, 14.0f, 7.0f));
            modelPartData.addOrReplaceChild(PartNames.RIGHT_HIND_LEG, CubeListBuilder.create().texOffs(48, 21).addBox(-1.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation), PartPose.offset(-4.0f, 14.0f, 7.0f));
            modelPartData.addOrReplaceChild(PartNames.LEFT_FRONT_LEG, CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation), PartPose.offset(4.0f, 14.0f, -12.0f));
            modelPartData.addOrReplaceChild(PartNames.RIGHT_FRONT_LEG, CubeListBuilder.create().texOffs(48, 21).addBox(-1.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation), PartPose.offset(-4.0f, 14.0f, -12.0f));
            CubeDeformation dilation2 = dilation.extend(0.0f, 5.5f, 0.0f);
            modelPartData.addOrReplaceChild(LEFT_HIND_BABY_LEG, CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation2), PartPose.offset(4.0f, 14.0f, 7.0f));
            modelPartData.addOrReplaceChild(RIGHT_HIND_BABY_LEG, CubeListBuilder.create().texOffs(48, 21).addBox(-1.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation2), PartPose.offset(-4.0f, 14.0f, 7.0f));
            modelPartData.addOrReplaceChild(LEFT_FRONT_BABY_LEG, CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation2), PartPose.offset(4.0f, 14.0f, -12.0f));
            modelPartData.addOrReplaceChild(RIGHT_FRONT_BABY_LEG, CubeListBuilder.create().texOffs(48, 21).addBox(-1.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation2), PartPose.offset(-4.0f, 14.0f, -12.0f));
            modelPartData2.addOrReplaceChild(PartNames.TAIL, CubeListBuilder.create().texOffs(42, 36).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 14.0f, 4.0f, dilation), PartPose.offsetAndRotation(0.0f, -5.0f, 2.0f, 0.5235988f, 0.0f, 0.0f));
            modelPartData2.addOrReplaceChild(SADDLE, CubeListBuilder.create().texOffs(26, 0).addBox(-5.0f, -8.0f, -9.0f, 10.0f, 9.0f, 9.0f, new CubeDeformation(0.5f)), PartPose.ZERO);
            modelPartData3.addOrReplaceChild(LEFT_SADDLE_MOUTH, CubeListBuilder.create().texOffs(29, 5).addBox(2.0f, -9.0f, -6.0f, 1.0f, 2.0f, 2.0f, dilation), PartPose.ZERO);
            modelPartData3.addOrReplaceChild(RIGHT_SADDLE_MOUTH, CubeListBuilder.create().texOffs(29, 5).addBox(-3.0f, -9.0f, -6.0f, 1.0f, 2.0f, 2.0f, dilation), PartPose.ZERO);
            modelPartData3.addOrReplaceChild(LEFT_SADDLE_LINE, CubeListBuilder.create().texOffs(32, 2).addBox(3.1f, -6.0f, -8.0f, 0.0f, 3.0f, 16.0f), PartPose.rotation(-0.5235988f, 0.0f, 0.0f));
            modelPartData3.addOrReplaceChild(RIGHT_SADDLE_LINE, CubeListBuilder.create().texOffs(32, 2).addBox(-3.1f, -6.0f, -8.0f, 0.0f, 3.0f, 16.0f), PartPose.rotation(-0.5235988f, 0.0f, 0.0f));
            modelPartData3.addOrReplaceChild(HEAD_SADDLE, CubeListBuilder.create().texOffs(1, 1).addBox(-3.0f, -11.0f, -1.9f, 6.0f, 5.0f, 6.0f, new CubeDeformation(0.22f)), PartPose.ZERO);
            modelPartData3.addOrReplaceChild(MOUTH_SADDLE_WRAP, CubeListBuilder.create().texOffs(19, 0).addBox(-2.0f, -11.0f, -4.0f, 4.0f, 5.0f, 2.0f, new CubeDeformation(0.2f)), PartPose.ZERO);
            modelPartData4.addOrReplaceChild(PartNames.LEFT_EAR, CubeListBuilder.create().texOffs(19, 16).addBox(0.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, new CubeDeformation(-0.001f)), PartPose.ZERO);
            modelPartData4.addOrReplaceChild(PartNames.RIGHT_EAR, CubeListBuilder.create().texOffs(19, 16).addBox(-2.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, new CubeDeformation(-0.001f)), PartPose.ZERO);
            return modelData;
        }
    }
}

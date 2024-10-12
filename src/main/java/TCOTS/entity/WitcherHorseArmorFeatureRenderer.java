package TCOTS.entity;

import TCOTS.items.armor.WitcherHorseArmorItem;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;

public class WitcherHorseArmorFeatureRenderer extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    private final HorseEntityModel<HorseEntity> model;
    public WitcherHorseArmorFeatureRenderer(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context, EntityModelLoader loader) {
        super(context);
//        this.model = new HorseEntityModel<>(loader.getModelPart(EntityModelLayers.HORSE));

//        this.model = new HorseEntityModel<>(HorseEntityModel.getModelData(new Dilation(0.8f)).getRoot().createPart(64, 64));
//        this.model = new HorseEntityModel<>(HorseEntityModel.getModelData(new Dilation(0.8f))
//                .getRoot().getChild(EntityModelPartNames.BODY).createPart(64,64));
//        loader.getModelPart(EntityModelLayers.HORSE).getChild(EntityModelPartNames.BODY);
//        HorseEntityModel.getModelData(new Dilation(1f));

//        this.model= new HorseEntityModel<>(modelPartData2.createPart(64,64));
        this.model=new HorseArmorModel<>(HorseArmorModel.getModelData(new Dilation(0.1f)).getRoot().createPart(64,96));
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, HorseEntity horseEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        ItemStack itemStack = horseEntity.getArmorType();
        if (!(itemStack.getItem() instanceof WitcherHorseArmorItem horseArmorItem)) {
            return;
        }

        this.getContextModel().copyStateTo(this.model);
        this.model.animateModel(horseEntity, limbAngle, limbDistance, tickDelta);
        this.model.setAngles(horseEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(horseArmorItem.getOuterTexture()));
        this.model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    private static class HorseArmorModel<T extends AbstractHorseEntity> extends HorseEntityModel<T> {

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

        public static ModelData getModelData(Dilation dilation) {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 32).cuboid(-5.0f, -8.0f, -17.0f, 10.0f, 13.0f, 22.0f, new Dilation(0.45f)), ModelTransform.pivot(0.0f, 11.0f, 5.0f));




            ModelPartData modelPartData3 = modelPartData.addChild(HEAD_PARTS, ModelPartBuilder.create().uv(0, 75).cuboid(-2.05f, -6.0f, -2.0f, 4.0f, 12.0f, 9.0f, new Dilation(0.05f)), ModelTransform.of(0.0f, 4.0f, -12.0f, 0.5235988f, 0.0f, 0.0f));
            ModelPartData modelPartData4 = modelPartData3.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 13).cuboid(-3.0f, -11.0f, -2.0f, 6.0f, 5.0f, 7.0f, dilation), ModelTransform.NONE);
            modelPartData3.addChild(EntityModelPartNames.MANE, ModelPartBuilder.create().uv(56, 36).cuboid(-1.0f, -11.0f, 5.01f, 2.0f, 16.0f, 2.0f, dilation), ModelTransform.NONE);
            modelPartData3.addChild("upper_mouth", ModelPartBuilder.create().uv(0, 25).cuboid(-2.0f, -11.0f, -7.0f, 4.0f, 5.0f, 5.0f, dilation), ModelTransform.NONE);
            modelPartData.addChild(EntityModelPartNames.LEFT_HIND_LEG, ModelPartBuilder.create().uv(48, 21).mirrored().cuboid(-3.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation), ModelTransform.pivot(4.0f, 14.0f, 7.0f));
            modelPartData.addChild(EntityModelPartNames.RIGHT_HIND_LEG, ModelPartBuilder.create().uv(48, 21).cuboid(-1.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation), ModelTransform.pivot(-4.0f, 14.0f, 7.0f));
            modelPartData.addChild(EntityModelPartNames.LEFT_FRONT_LEG, ModelPartBuilder.create().uv(48, 21).mirrored().cuboid(-3.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation), ModelTransform.pivot(4.0f, 14.0f, -12.0f));
            modelPartData.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, ModelPartBuilder.create().uv(48, 21).cuboid(-1.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation), ModelTransform.pivot(-4.0f, 14.0f, -12.0f));
            Dilation dilation2 = dilation.add(0.0f, 5.5f, 0.0f);
            modelPartData.addChild(LEFT_HIND_BABY_LEG, ModelPartBuilder.create().uv(48, 21).mirrored().cuboid(-3.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation2), ModelTransform.pivot(4.0f, 14.0f, 7.0f));
            modelPartData.addChild(RIGHT_HIND_BABY_LEG, ModelPartBuilder.create().uv(48, 21).cuboid(-1.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, dilation2), ModelTransform.pivot(-4.0f, 14.0f, 7.0f));
            modelPartData.addChild(LEFT_FRONT_BABY_LEG, ModelPartBuilder.create().uv(48, 21).mirrored().cuboid(-3.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation2), ModelTransform.pivot(4.0f, 14.0f, -12.0f));
            modelPartData.addChild(RIGHT_FRONT_BABY_LEG, ModelPartBuilder.create().uv(48, 21).cuboid(-1.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, dilation2), ModelTransform.pivot(-4.0f, 14.0f, -12.0f));
            modelPartData2.addChild(EntityModelPartNames.TAIL, ModelPartBuilder.create().uv(42, 36).cuboid(-1.5f, 0.0f, 0.0f, 3.0f, 14.0f, 4.0f, dilation), ModelTransform.of(0.0f, -5.0f, 2.0f, 0.5235988f, 0.0f, 0.0f));
            modelPartData2.addChild(SADDLE, ModelPartBuilder.create().uv(26, 0).cuboid(-5.0f, -8.0f, -9.0f, 10.0f, 9.0f, 9.0f, new Dilation(0.5f)), ModelTransform.NONE);
            modelPartData3.addChild(LEFT_SADDLE_MOUTH, ModelPartBuilder.create().uv(29, 5).cuboid(2.0f, -9.0f, -6.0f, 1.0f, 2.0f, 2.0f, dilation), ModelTransform.NONE);
            modelPartData3.addChild(RIGHT_SADDLE_MOUTH, ModelPartBuilder.create().uv(29, 5).cuboid(-3.0f, -9.0f, -6.0f, 1.0f, 2.0f, 2.0f, dilation), ModelTransform.NONE);
            modelPartData3.addChild(LEFT_SADDLE_LINE, ModelPartBuilder.create().uv(32, 2).cuboid(3.1f, -6.0f, -8.0f, 0.0f, 3.0f, 16.0f), ModelTransform.rotation(-0.5235988f, 0.0f, 0.0f));
            modelPartData3.addChild(RIGHT_SADDLE_LINE, ModelPartBuilder.create().uv(32, 2).cuboid(-3.1f, -6.0f, -8.0f, 0.0f, 3.0f, 16.0f), ModelTransform.rotation(-0.5235988f, 0.0f, 0.0f));
            modelPartData3.addChild(HEAD_SADDLE, ModelPartBuilder.create().uv(1, 1).cuboid(-3.0f, -11.0f, -1.9f, 6.0f, 5.0f, 6.0f, new Dilation(0.22f)), ModelTransform.NONE);
            modelPartData3.addChild(MOUTH_SADDLE_WRAP, ModelPartBuilder.create().uv(19, 0).cuboid(-2.0f, -11.0f, -4.0f, 4.0f, 5.0f, 2.0f, new Dilation(0.2f)), ModelTransform.NONE);
            modelPartData4.addChild(EntityModelPartNames.LEFT_EAR, ModelPartBuilder.create().uv(19, 16).cuboid(0.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, new Dilation(-0.001f)), ModelTransform.NONE);
            modelPartData4.addChild(EntityModelPartNames.RIGHT_EAR, ModelPartBuilder.create().uv(19, 16).cuboid(-2.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, new Dilation(-0.001f)), ModelTransform.NONE);
            return modelData;
        }
    }
}

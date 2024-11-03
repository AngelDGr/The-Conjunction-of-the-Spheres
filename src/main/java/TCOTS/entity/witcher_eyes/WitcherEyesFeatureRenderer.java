package TCOTS.entity.witcher_eyes;

import TCOTS.TCOTS_Client;
import TCOTS.TCOTS_Main;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WitcherEyesFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final WitcherEyesModel<AbstractClientPlayerEntity> eyesModel;
    private final PlayerEntityModel<AbstractClientPlayerEntity> playerModel;

    public WitcherEyesFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureContext, EntityRendererFactory.Context rendererContext) {
        super(featureContext);

       this.eyesModel = new WitcherEyesModel<>(rendererContext.getPart(TCOTS_Client.WITCHER_EYES_LAYER));
       this.playerModel = this.getContextModel();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       AbstractClientPlayerEntity player,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(!player.theConjunctionOfTheSpheres$getWitcherEyesActivated()){
            return;
        }


        VertexConsumer buffer = vertexConsumers.getBuffer(getEyeSeparationAndShape(player));

        playerModel.copyStateTo(eyesModel);

        eyesModel.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        this.setModelPose(player);

        eyesModel.animateModel(player, limbAngle, limbDistance, tickDelta);

        eyesModel.render(matrices, buffer,
                0xF00000,
                OverlayTexture.DEFAULT_UV,
                1.0f,
                1.0f,
                1.0f,
                1.0f);
    }

    private RenderLayer getEyeSeparationAndShape(AbstractClientPlayerEntity player){
        int separation = player.theConjunctionOfTheSpheres$getEyeSeparation();
        int shape = player.theConjunctionOfTheSpheres$getEyeShape();

        String shapeKey =
        switch (shape) {
            default -> "normal";
            case 1 -> "tall";
            case 2 -> "long";
            case 3 -> "tall_shadow";
            case 4 -> "big";
        };

        return
        RenderLayer.getEyes(new Identifier(TCOTS_Main.MOD_ID,
                "textures/entity/player/witcher_eyes/"+shapeKey+"/"+(Math.min(separation, 6))+ "px.png"));
    }


    private void setModelPose(AbstractClientPlayerEntity player) {
        if (player.isSpectator()) {
            this.eyesModel.setVisible(false);
            this.eyesModel.head.visible = true;
            this.eyesModel.hat.visible = true;
        } else {
            this.eyesModel.setVisible(true);
            this.eyesModel.sneaking = player.isInSneakingPose();
        }
    }

}

package TCOTS.entity.witcher_cosmetics.witcher_eyes;

import TCOTS.TCOTS_Client;
import TCOTS.TCOTS_Main;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(value= EnvType.CLIENT)
public class WitcherEyesFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    private final WitcherEyesModel eyesModel;

    public WitcherEyesFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity,
                                      PlayerEntityModel<AbstractClientPlayerEntity>> featureContext,
                                      EntityRendererFactory.Context rendererContext) {
        super(featureContext);
       this.eyesModel = new WitcherEyesModel(rendererContext.getPart(TCOTS_Client.WITCHER_EYES_LAYER));
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       AbstractClientPlayerEntity player,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(!player.theConjunctionOfTheSpheres$getWitcherEyesActivated() || player.isInvisible()){
            return;
        }

        VertexConsumer buffer = vertexConsumers.getBuffer(getEyeSeparationAndShape(player));

        this.getContextModel().copyBipedStateTo(eyesModel);

        eyesModel.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

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
        RenderLayer.getEyes(Identifier.of(TCOTS_Main.MOD_ID,
                "textures/entity/player/witcher_eyes/"+shapeKey+"/"+(Math.min(separation, 6))+ "px.png"));
    }
}

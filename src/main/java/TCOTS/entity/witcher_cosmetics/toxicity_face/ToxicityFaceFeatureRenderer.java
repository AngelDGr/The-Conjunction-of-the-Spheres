package TCOTS.entity.witcher_cosmetics.toxicity_face;

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
import net.minecraft.util.math.MathHelper;

@Environment(value= EnvType.CLIENT)
public class ToxicityFaceFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final ToxicityFaceModel toxicityFaceModel;
    public ToxicityFaceFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity,
            PlayerEntityModel<AbstractClientPlayerEntity>> featureContext,
                                       EntityRendererFactory.Context rendererContext) {
        super(featureContext);
        this.toxicityFaceModel = new ToxicityFaceModel(rendererContext.getPart(TCOTS_Client.TOXICITY_FACE_LAYER));
    }

    float transparency=0;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        boolean isOver50= player.theConjunctionOfTheSpheres$getAllToxicity() > player.theConjunctionOfTheSpheres$getMaxToxicity()*0.5f;

        if(!player.theConjunctionOfTheSpheres$getToxicityActivated() || player.isInvisible()){
            return;
        }

        VertexConsumer buffer = vertexConsumers.getBuffer(getEyeSeparationAndShape(player));

        this.getContextModel().copyBipedStateTo(toxicityFaceModel);

        toxicityFaceModel.setAngles(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        if(isOver50){
            transparency=        MathHelper.clamp(
                    ((float) player.theConjunctionOfTheSpheres$getAllToxicity()-(player.theConjunctionOfTheSpheres$getMaxToxicity()*0.45f))
                            / ((float) player.theConjunctionOfTheSpheres$getMaxToxicity()*0.9f),
                    0.0f, 1.0f)*2;
        } else {
            transparency=transparency-0.02f;
        }

        transparency = MathHelper.clamp(transparency, 0f, 1.0f);

        toxicityFaceModel.render(matrices, buffer,
                light,
                OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f, transparency
        );
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
                RenderLayer.getEntityTranslucent(Identifier.of(TCOTS_Main.MOD_ID,
                        "textures/entity/player/toxicity_face/"+shapeKey+"/"+(Math.min(separation, 6))+ "px.png"));
    }
}

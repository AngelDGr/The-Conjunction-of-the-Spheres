package TCOTS.entity.witcher_cosmetics.toxicity_face;

import TCOTS.TCOTS_Client;
import TCOTS.TCOTS_Main;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.util.Color;

public class ToxicityFaceFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final ToxicityFaceModel toxicityFaceModel;
    public ToxicityFaceFeatureRenderer(RenderLayerParent<AbstractClientPlayer,
                                       PlayerModel<AbstractClientPlayer>> featureContext,
                                       EntityRendererProvider.Context rendererContext) {
        super(featureContext);
        this.toxicityFaceModel = new ToxicityFaceModel(rendererContext.bakeLayer(TCOTS_Client.TOXICITY_FACE_LAYER));
    }

    float transparency=0;

    @Override
    public void render(@NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light, AbstractClientPlayer player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        boolean isOver50= player.theConjunctionOfTheSpheres$getAllToxicity() > player.theConjunctionOfTheSpheres$getMaxToxicity()*0.5f;

        if(!player.theConjunctionOfTheSpheres$getToxicityActivated() || player.isInvisible()){
            return;
        }

        VertexConsumer buffer = vertexConsumers.getBuffer(getEyeSeparationAndShape(player));

        this.getParentModel().copyPropertiesTo(toxicityFaceModel);

        toxicityFaceModel.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        if(isOver50){
            transparency=        Mth.clamp(
                    ((float) player.theConjunctionOfTheSpheres$getAllToxicity()-(player.theConjunctionOfTheSpheres$getMaxToxicity()*0.45f))
                            / ((float) player.theConjunctionOfTheSpheres$getMaxToxicity()*0.9f),
                    0.0f, 1.0f)*2;
        } else {
            transparency=transparency-0.02f;
        }

        transparency = Mth.clamp(transparency, 0f, 1.0f);

        toxicityFaceModel.renderToBuffer(matrices, buffer,
                light,
                OverlayTexture.NO_OVERLAY,
                Color.ofRGBA(1f, 1f, 1f, transparency).argbInt()
        );
    }

    private RenderType getEyeSeparationAndShape(AbstractClientPlayer player){
        int separation = player.theConjunctionOfTheSpheres$getEyeSeparation();
        int shape = player.theConjunctionOfTheSpheres$getEyeShape();

        String shapeKey =
                switch (shape) {
                    case 1 -> "tall";
                    case 2 -> "long";
                    case 3 -> "tall_shadow";
                    case 4 -> "big";
                    default -> "normal";
                };

        return
                RenderType.entityTranslucent(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,
                        "textures/entity/player/toxicity_face/"+shapeKey+"/"+(Math.min(separation, 6))+ "px.png"));
    }
}

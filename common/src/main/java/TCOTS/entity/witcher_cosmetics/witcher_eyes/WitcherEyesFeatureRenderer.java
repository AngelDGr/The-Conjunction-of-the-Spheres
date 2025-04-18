package TCOTS.entity.witcher_cosmetics.witcher_eyes;

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
import org.jetbrains.annotations.NotNull;

public class WitcherEyesFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final WitcherEyesModel eyesModel;

    public WitcherEyesFeatureRenderer(RenderLayerParent<AbstractClientPlayer,
                                      PlayerModel<AbstractClientPlayer>> featureContext,
                                      EntityRendererProvider.Context rendererContext) {
        super(featureContext);
       this.eyesModel = new WitcherEyesModel(rendererContext.bakeLayer(TCOTS_Client.WITCHER_EYES_LAYER));
    }

    @Override
    public void render(@NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light,
                       AbstractClientPlayer player,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(!player.theConjunctionOfTheSpheres$getWitcherEyesActivated() || player.isInvisible()){
            return;
        }

        VertexConsumer buffer = vertexConsumers.getBuffer(getEyeSeparationAndShape(player));

        this.getParentModel().copyPropertiesTo(eyesModel);

        eyesModel.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        eyesModel.renderToBuffer(matrices, buffer,
                0xF00000,
                OverlayTexture.NO_OVERLAY);
    }


    private RenderType getEyeSeparationAndShape(AbstractClientPlayer player){
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
        RenderType.eyes(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,
                "textures/entity/player/witcher_eyes/"+shapeKey+"/"+(Math.min(separation, 6))+ "px.png"));
    }
}

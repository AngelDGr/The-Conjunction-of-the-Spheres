package TCOTS.entity.witcher_cosmetics.witcher_eyes;

import TCOTS.TCOTS_Client;
import TCOTS.TCOTS_Main;
import TCOTS.utils.MiscUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.util.Color;

public class WitcherEyesFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final WitcherEyesModel eyesModel;

    public WitcherEyesFeatureRenderer(final RenderLayerParent<AbstractClientPlayer,
                                      PlayerModel<AbstractClientPlayer>> featureContext,
                                      final EntityRendererProvider.Context rendererContext) {
        super(featureContext);
       this.eyesModel = new WitcherEyesModel(rendererContext.bakeLayer(TCOTS_Client.WITCHER_EYES_LAYER));
    }

    @Override
    public void render(@NotNull final PoseStack matrices, @NotNull final MultiBufferSource vertexConsumers, final int light,
                       @NotNull final AbstractClientPlayer player,
                       final float limbAngle, final float limbDistance, final float tickDelta, final float animationProgress, final float headYaw, final float headPitch) {

        if(!player.theConjunctionOfTheSpheres$getWitcherEyesActivated() || player.isInvisible()){
            return;
        }

        final VertexConsumer buffer = vertexConsumers.getBuffer(
                RenderType.eyes(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,
                        "textures/entity/player/witcher_eyes/witcher_eyes"+ (player.theConjunctionOfTheSpheres$getEyeShape()==2?"_shadow": "") +".png")));

        this.getParentModel().copyPropertiesTo(eyesModel);

        eyesModel.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        //Animates Eyes
        if(hasExpressiveModel()){
            if(player.theConjunctionOfTheSpheres$getEyeMoves()){
                copyExpressiveEyes("3", "_2x1");
                copyExpressiveEyes("6", "_2x2");
                copyExpressiveEyes("9", "_2x3");
                copyExpressiveEyes("11", "_enderman");
                copyExpressiveEyes("14", "_derp");

            } else {
                resetExpressiveEyes("_2x1");
                resetExpressiveEyes("_2x2");
                resetExpressiveEyes("_2x3");
                resetExpressiveEyes("_enderman");
                resetExpressiveEyes("_derp");
            }
        }

        changeEyesShape(player);

        // Default Color
        // e5bc10
        final float r = 229f / 255f;
        final float g = 188f / 255f;
        final float b = 16f  / 255f;

        eyesModel.renderToBuffer(matrices, buffer,
                0xF00000,
                OverlayTexture.NO_OVERLAY,
                Color.ofRGBA(r, g, b, 1f).argbInt()
                );
    }

    private void changeEyesShape(final Player player){
        final int separation = player.theConjunctionOfTheSpheres$getEyeSeparation();
        final int shape = player.theConjunctionOfTheSpheres$getEyeShape();

        switch (shape){
            //Normal (1x1)
            case 0 -> {
                changeEyesVisibility("_2x1",  true);
                changeEyesVisibility(false, "_2x2", "_2x3", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x1");
            }
            //Tall (1x2) - With Shadow
            case 1, 2 -> {
                changeEyesVisibility("_2x2",  true);
                changeEyesVisibility(false, "_2x1", "_2x3", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x2");
            }
            //Tall (1x2)-For 2x3 Eyes
            case 3 -> {
                changeEyesVisibility("_2x3",  true);
                changeEyesVisibility(false, "_2x1", "_2x2", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x3");
            }
            //Derp (1x1)-On Sides
            case 4 -> {
                changeEyesVisibility("_derp",  true);
                changeEyesVisibility(false, "_2x1", "_2x3", "_enderman", "_2x2", "_immobile");

                if(!hasExpressiveModel() || !player.theConjunctionOfTheSpheres$getEyeMoves()){
                    getEye("left_eye_derp").x=getEye("left_eye_derp").getInitialPose().x+1;

                    getEye("right_eye_derp").x=getEye("right_eye_derp").getInitialPose().x-1;
                }

                separateEyes(separation, "_derp", +1, -1, false);
            }
            //Enderman (1x1)-Center
            case 5 -> {
                changeEyesVisibility("_enderman",  true);
                changeEyesVisibility(false, "_2x1","_2x2","_2x3","_derp","_immobile");

                separateEyes(separation, "_enderman");
            }
            //Allay (1x3)
            case 6 -> {
                changeEyesVisibility("_immobile", true);
                changeEyesVisibility(false, "_2x1", "_2x2", "_2x3", "_enderman", "_derp");

                //3x1 Preset
                {
                    if(hasExpressiveModel()) getEye("left_eye_immobile").x = getEye("left_eye_immobile").getInitialPose().x - 1;
                    getEye("left_eye_immobile").y = getEye("left_eye_immobile").getInitialPose().y + 1.9945f;

                    getEye("left_eye_immobile").yScale = 2.9745f;
                    getEye("left_eye_immobile").xScale = 1f;

                    if(hasExpressiveModel()) getEye("right_eye_immobile").x = getEye("right_eye_immobile").getInitialPose().x + 1;
                    getEye("right_eye_immobile").y = getEye("right_eye_immobile").getInitialPose().y + 1.9945f;


                    getEye("right_eye_immobile").yScale = 2.9745f;
                    getEye("right_eye_immobile").xScale = 1f;
                }

                separateEyes(separation, "_immobile", -1.0f, +1.0f, true);
            }
            //Long (2x1)
            case 7 -> {
                changeEyesVisibility("_immobile", true);
                changeEyesVisibility(false, "_2x1", "_2x2", "_2x3", "_enderman", "_derp");

                //2x1 Preset
                {
                    if(hasExpressiveModel()) getEye("left_eye_immobile").x = getEye("left_eye_immobile").getInitialPose().x - 0.5f;
                    getEye("left_eye_immobile").y = getEye("left_eye_immobile").getInitialPose().y + 1.0f;

                    getEye("left_eye_immobile").yScale = 1f;
                    getEye("left_eye_immobile").xScale = 2f;

                    if(hasExpressiveModel()) getEye("right_eye_immobile").x = getEye("right_eye_immobile").getInitialPose().x + 0.5f;
                    getEye("right_eye_immobile").y = getEye("right_eye_immobile").getInitialPose().y + 1.0f;

                    getEye("right_eye_immobile").yScale = 1f;
                    getEye("right_eye_immobile").xScale = 2f;
                }

                separateEyes(separation, "_immobile",-0.5f,+0.5f, true);
            }
            //Big (2x2)
            case 8 -> {
                changeEyesVisibility("_immobile", true);
                changeEyesVisibility(false, "_2x1", "_2x2", "_2x3", "_enderman", "_derp");

                //2x2 Preset
                {
                    if(hasExpressiveModel()) getEye("left_eye_immobile").x = getEye("left_eye_immobile").getInitialPose().x - 0.5f;
                    getEye("left_eye_immobile").y = getEye("left_eye_immobile").getInitialPose().y + 1.0f;

                    getEye("left_eye_immobile").yScale = 2f;
                    getEye("left_eye_immobile").xScale = 2f;

                    if(hasExpressiveModel()) getEye("right_eye_immobile").x = getEye("right_eye_immobile").getInitialPose().x + 0.5f;
                    getEye("right_eye_immobile").y = getEye("right_eye_immobile").getInitialPose().y + 1.0f;

                    getEye("right_eye_immobile").yScale = 2f;
                    getEye("right_eye_immobile").xScale = 2f;
                }

                separateEyes(separation, "_immobile", -0.5f, +0.5f, true);
            }
            default -> {
                changeEyesVisibility("_2x1",  true);
                changeEyesVisibility(false, "_2x2", "_2x3", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x1");
            }
        }
    }

    private void separateEyes(final int separation, final String eyeName, final float extraXLeft, final float extraXRight, final boolean mustBeFlat){

        final int[] leftOffsets =  { -1,  0,  0,  1,  1,  2,  2 };
        final int[] rightOffsets = {  1,  1,  0,  0, -1, -1, -2 };
        // Clamp separation to valid range (0 to 6)
        final int s = Math.max(0, Math.min(separation, 6));

        if(hasExpressiveModel()){
            getEye("left_eye"  + eyeName).x += leftOffsets[s];
            getEye("right_eye" + eyeName).x += rightOffsets[s];
        } else {
            getEye("left_eye"  + eyeName).x = getEye("left_eye"  + eyeName).getInitialPose().x + extraXLeft  + leftOffsets[s];
            getEye("right_eye" + eyeName).x = getEye("right_eye" + eyeName).getInitialPose().x + extraXRight + rightOffsets[s];
        }

        //Makes the eye 2D
        if(mustBeFlat){


            getEye("left_eye" + eyeName).z=getEye("left_eye" + eyeName).getInitialPose().z + (eyeName.equals("_enderman")? -0: -0.99f);
            getEye("left_eye" + eyeName).zScale=0.01f;

            getEye("right_eye" + eyeName).z=getEye("right_eye" + eyeName).getInitialPose().z + (eyeName.equals("_enderman")? -0f: -0.99f);
            getEye("right_eye" + eyeName).zScale=0.01f;

            getEye("right_eye" + eyeName).xScale=1f-0.0055F;
            getEye("right_eye" + eyeName).yScale=1f-0.0055F;

            getEye("left_eye" + eyeName).xScale= 1f-0.0055F;
            getEye("left_eye" + eyeName).yScale= 1f-0.0055F;

        }
    }

    private void separateEyes(final int separation, final String eyeName){
        separateEyes(separation, eyeName, 0, 0, !hasExpressiveModel());
    }

    private void copyExpressiveEyes(final String eyeNumberFrom, final String eyeNumberTo){
        //Left
        {
            getEye("left_eye"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_l_eye_pupil"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_l_eye_pupil"+eyeNumberFrom).getChild("EMF_l_eye_pupil_in"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo).getChild("l_eye_pupil_ou"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_l_eye_pupil"+eyeNumberFrom).getChild("EMF_l_eye_pupil_in"+eyeNumberFrom).getChild("EMF_l_eye_pupil_ou"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo).getChild("l_eye_pupil_ou"+eyeNumberTo).getChild("l_eye_pupil_do"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_l_eye_pupil"+eyeNumberFrom).getChild("EMF_l_eye_pupil_in"+eyeNumberFrom).getChild("EMF_l_eye_pupil_ou"+eyeNumberFrom).getChild("EMF_l_eye_pupil_do"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo).getChild("l_eye_pupil_ou"+eyeNumberTo).getChild("l_eye_pupil_do"+eyeNumberTo).getChild("l_eye_pupil_up"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_l_eye_pupil"+eyeNumberFrom).getChild("EMF_l_eye_pupil_in"+eyeNumberFrom).getChild("EMF_l_eye_pupil_ou"+eyeNumberFrom).getChild("EMF_l_eye_pupil_do"+eyeNumberFrom).getChild("EMF_l_eye_pupil_up"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("ctrl_l_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_ctrl_l_pupil"+eyeNumberFrom));
        }

        //Right
        {
            getEye("right_eye"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_r_eye_pupil"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_r_eye_pupil"+eyeNumberFrom).getChild("EMF_r_eye_pupil_in"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo).getChild("r_eye_pupil_ou"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_r_eye_pupil"+eyeNumberFrom).getChild("EMF_r_eye_pupil_in"+eyeNumberFrom).getChild("EMF_r_eye_pupil_ou"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo).getChild("r_eye_pupil_ou"+eyeNumberTo).getChild("r_eye_pupil_do"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_r_eye_pupil"+eyeNumberFrom).getChild("EMF_r_eye_pupil_in"+eyeNumberFrom).getChild("EMF_r_eye_pupil_ou"+eyeNumberFrom).getChild("EMF_r_eye_pupil_do"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo).getChild("r_eye_pupil_ou"+eyeNumberTo).getChild("r_eye_pupil_do"+eyeNumberTo).getChild("r_eye_pupil_up"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_r_eye_pupil"+eyeNumberFrom).getChild("EMF_r_eye_pupil_in"+eyeNumberFrom).getChild("EMF_r_eye_pupil_ou"+eyeNumberFrom).getChild("EMF_r_eye_pupil_do"+eyeNumberFrom).getChild("EMF_r_eye_pupil_up"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("ctrl_r_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_ctrl_r_pupil"+eyeNumberFrom));
        }
    }

    private void resetExpressiveEyes(final String eyeName){
        //Left
        {
            getEye("left_eye"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("l_eye_pupil"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("l_eye_pupil"+eyeName).getChild("l_eye_pupil_in"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("l_eye_pupil"+eyeName).getChild("l_eye_pupil_in"+eyeName).getChild("l_eye_pupil_ou"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("l_eye_pupil"+eyeName).getChild("l_eye_pupil_in"+eyeName).getChild("l_eye_pupil_ou"+eyeName).getChild("l_eye_pupil_do"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("l_eye_pupil"+eyeName).getChild("l_eye_pupil_in"+eyeName).getChild("l_eye_pupil_ou"+eyeName).getChild("l_eye_pupil_do"+eyeName).getChild("l_eye_pupil_up"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("ctrl_l_pupil"+eyeName).resetPose();
        }


        //Right
        {
            getEye("right_eye"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("r_eye_pupil"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("r_eye_pupil"+eyeName).getChild("r_eye_pupil_in"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("r_eye_pupil"+eyeName).getChild("r_eye_pupil_in"+eyeName).getChild("r_eye_pupil_ou"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("r_eye_pupil"+eyeName).getChild("r_eye_pupil_in"+eyeName).getChild("r_eye_pupil_ou"+eyeName).getChild("r_eye_pupil_do"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("r_eye_pupil"+eyeName).getChild("r_eye_pupil_in"+eyeName).getChild("r_eye_pupil_ou"+eyeName).getChild("r_eye_pupil_do"+eyeName).getChild("r_eye_pupil_up"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("ctrl_r_pupil"+eyeName).resetPose();
        }
    }

    private ModelPart getEye(final String eye){
        return eyesModel.eyes.getChild(eye);
    }

    private ModelPart getEMFEye(final String eye){
        return this.getParentModel().head.getChild("EMF_head").getChild("EMF_eyes").getChild(eye);
    }

    private void changeEyesVisibility(final String eyeName, final boolean visible){
        getEye("right_eye"+eyeName).visible=visible;
        getEye("left_eye"+eyeName).visible=visible;
    }

    private void changeEyesVisibility(final boolean visible, final String... eyeNames){
        for (final String name: eyeNames){
            changeEyesVisibility(name, visible);
        }
    }

    private boolean hasExpressiveModel(){
        return MiscUtil.hasExpressiveModel(this.getParentModel());
    }
}

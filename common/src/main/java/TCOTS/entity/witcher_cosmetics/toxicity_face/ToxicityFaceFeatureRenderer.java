package TCOTS.entity.witcher_cosmetics.toxicity_face;

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
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.util.Color;

public class ToxicityFaceFeatureRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private final ToxicityFaceModel toxicityFaceModel;
    public ToxicityFaceFeatureRenderer(final RenderLayerParent<AbstractClientPlayer,
                                       PlayerModel<AbstractClientPlayer>> featureContext,
                                       final EntityRendererProvider.Context rendererContext) {
        super(featureContext);
        this.toxicityFaceModel = new ToxicityFaceModel(rendererContext.bakeLayer(TCOTS_Client.TOXICITY_FACE_LAYER));
    }

    float transparency=0;

    //TODO: Finally, end with this, adding the rest of the eyes
    @Override
    public void render(@NotNull final PoseStack matrices, @NotNull final MultiBufferSource vertexConsumers, final int light, final AbstractClientPlayer player, final float limbAngle, final float limbDistance, final float tickDelta, final float animationProgress, final float headYaw, final float headPitch) {
        final boolean isOver50= player.theConjunctionOfTheSpheres$getAllToxicity() > player.theConjunctionOfTheSpheres$getMaxToxicity()*0.5f;

        if(!player.theConjunctionOfTheSpheres$getToxicityActivated() || player.isInvisible()){
            return;
        }

        final VertexConsumer buffer = vertexConsumers.getBuffer(RenderType.entityTranslucent(getTexture(player)));

        this.getParentModel().copyPropertiesTo(toxicityFaceModel);

        toxicityFaceModel.setupAnim(player, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        //Animated Eyes
        if(hasExpressiveModel()){
            if(player.theConjunctionOfTheSpheres$getEyeMoves()){
                copyExpressiveEyes("3", "_2x1");
                copyExpressiveEyes("6", "_2x2");
                copyExpressiveEyes("9", "_2x3");

                copyExpressiveEyesDerp();

                copyExpressiveEyes("11", "_enderman");
            } else {
                resetExpressiveEyes("_2x1");
                resetExpressiveEyes("_2x2");
                resetExpressiveEyes("_2x3");

                resetExpressiveEyes("_derp");

                resetExpressiveEyes("_enderman");
            }

            changeEyesShape(player);
            setEyeOffsets();
        } else {
            changeEyesVisibility(false, "_2x1", "_2x2", "_2x3", "_enderman", "_derp", "_immobile");
        }


        //Actual Render
        if(isOver50){
            transparency=
                    Mth.clamp(
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

    private ResourceLocation getTexture(Player player){
        final int separation = player.theConjunctionOfTheSpheres$getEyeSeparation();
        final int shape = player.theConjunctionOfTheSpheres$getEyeShape();

        return switch (shape){
            case 0 -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/player/toxicity_face/normal/"+separation+"px.png");
            default -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/player/toxicity_face/tall/"+separation+"px.png");
        };
    }

    private void separateEyes(final int separation, final String eyeName){
        separateEyes(separation, eyeName, 0, 0, !hasExpressiveModel());
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
        }
    }

    private void changeEyesShape(final Player player){
        final int separation = player.theConjunctionOfTheSpheres$getEyeSeparation();
        final int shape = player.theConjunctionOfTheSpheres$getEyeShape();

        switch (shape){
            //Normal (1x1)
            case 0 -> {
                changeEyesVisibility( true, "_2x1");
                changeEyesVisibility(false, "_2x2", "_2x3", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x1");
            }
            //Tall (1x2) - With Shadow
            case 1, 2 -> {
                changeEyesVisibility(true, "_2x2");
                changeEyesVisibility(false, "_2x1", "_2x3", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x2");
            }
            //Tall (1x2)-For 2x3 Eyes
            case 3 -> {
                changeEyesVisibility(true, "_2x3");
                changeEyesVisibility(false, "_2x1", "_2x2", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x3");
            }
            //Derp (1x1)-On Sides
            case 4 -> {
                changeEyesVisibility(true, "_derp");
                changeEyesVisibility(false, "_2x1", "_2x3", "_enderman", "_2x2", "_immobile");

                if(!hasExpressiveModel() || !player.theConjunctionOfTheSpheres$getEyeMoves()){
                    getEye("left_eye_derp").x=getEye("left_eye_derp").getInitialPose().x+1;

                    getEye("right_eye_derp").x=getEye("right_eye_derp").getInitialPose().x-1;
                }

                separateEyes(separation, "_derp", +1, -1, false);
            }
            //Enderman (1x1)-Center
            case 5 -> {
                changeEyesVisibility(true, "_enderman");
                changeEyesVisibility(false, "_2x1","_2x2","_2x3","_derp","_immobile");

                separateEyes(separation, "_enderman");
            }
            //Allay (1x3)
            case 6 -> {
                changeEyesVisibility(true, "_immobile");
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
                changeEyesVisibility(true, "_immobile");
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
                changeEyesVisibility(true, "_immobile");
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
                changeEyesVisibility(true, "_2x1");
                changeEyesVisibility(false, "_2x2", "_2x3", "_enderman", "_derp", "_immobile");

                separateEyes(separation, "_2x1");
            }
        }
    }

    private void changeEyesVisibility(final boolean visible, final String... eyeNames){
        for (final String name: eyeNames){
            getEye("right_eye"+name).visible=visible;
            getEye("left_eye"+name).visible=visible;
        }
    }

    private void copyExpressiveEyesDerp(){
        final String eyeNumberTo="_derp";

        //Left
        {
            getEye("left_eye"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"3"));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"14").getChild("EMF_l_eye_pupil"+"14"));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"14").getChild("EMF_l_eye_pupil"+"14").getChild("EMF_l_eye_pupil_in"+"14"));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo).getChild("l_eye_pupil_ou"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"14").getChild("EMF_l_eye_pupil"+"14").getChild("EMF_l_eye_pupil_in"+"14").getChild("EMF_l_eye_pupil_ou"+"14"));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo).getChild("l_eye_pupil_ou"+eyeNumberTo).getChild("l_eye_pupil_do"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"14").getChild("EMF_l_eye_pupil"+"14").getChild("EMF_l_eye_pupil_in"+"14").getChild("EMF_l_eye_pupil_ou"+"14").getChild("EMF_l_eye_pupil_do"+"14"));

            getEye("left_eye"+eyeNumberTo).getChild("l_eye_pupil"+eyeNumberTo).getChild("l_eye_pupil_in"+eyeNumberTo).getChild("l_eye_pupil_ou"+eyeNumberTo).getChild("l_eye_pupil_do"+eyeNumberTo).getChild("l_eye_pupil_up"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"14").getChild("EMF_l_eye_pupil"+"14").getChild("EMF_l_eye_pupil_in"+"14").getChild("EMF_l_eye_pupil_ou"+"14").getChild("EMF_l_eye_pupil_do"+"14").getChild("EMF_l_eye_pupil_up"+"14"));

            getEye("left_eye"+eyeNumberTo).getChild("ctrl_l_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"14").getChild("EMF_ctrl_l_pupil"+"14"));


            getEye("left_eye"+eyeNumberTo).getChild("left_eye_white"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"3").getChild("EMF_left_eye_white"+"3"));

            getEye("left_eye"+eyeNumberTo).getChild("left_eyelid"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"3").getChild("EMF_left_eyelid"+"3"));

            getEye("left_eye"+eyeNumberTo).getChild("left_eyelid"+eyeNumberTo).getChild("left_blink"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+"3").getChild("EMF_left_eyelid"+"3").getChild("EMF_left_blink"+"3"));
        }

        //Right
        {
            getEye("right_eye"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"3"));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"14").getChild("EMF_r_eye_pupil"+"14"));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"14").getChild("EMF_r_eye_pupil"+"14").getChild("EMF_r_eye_pupil_in"+"14"));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo).getChild("r_eye_pupil_ou"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"14").getChild("EMF_r_eye_pupil"+"14").getChild("EMF_r_eye_pupil_in"+"14").getChild("EMF_r_eye_pupil_ou"+"14"));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo).getChild("r_eye_pupil_ou"+eyeNumberTo).getChild("r_eye_pupil_do"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"14").getChild("EMF_r_eye_pupil"+"14").getChild("EMF_r_eye_pupil_in"+"14").getChild("EMF_r_eye_pupil_ou"+"14").getChild("EMF_r_eye_pupil_do"+"14"));

            getEye("right_eye"+eyeNumberTo).getChild("r_eye_pupil"+eyeNumberTo).getChild("r_eye_pupil_in"+eyeNumberTo).getChild("r_eye_pupil_ou"+eyeNumberTo).getChild("r_eye_pupil_do"+eyeNumberTo).getChild("r_eye_pupil_up"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"14").getChild("EMF_r_eye_pupil"+"14").getChild("EMF_r_eye_pupil_in"+"14").getChild("EMF_r_eye_pupil_ou"+"14").getChild("EMF_r_eye_pupil_do"+"14").getChild("EMF_r_eye_pupil_up"+"14"));

            getEye("right_eye"+eyeNumberTo).getChild("ctrl_r_pupil"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"14").getChild("EMF_ctrl_r_pupil"+"14"));


            getEye("right_eye"+eyeNumberTo).getChild("right_eye_white"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"3").getChild("EMF_right_eye_white"+"3"));

            getEye("right_eye"+eyeNumberTo).getChild("right_eyelid"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"3").getChild("EMF_right_eyelid"+"3"));

            getEye("right_eye"+eyeNumberTo).getChild("right_eyelid"+eyeNumberTo).getChild("right_blink"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+"3").getChild("EMF_right_eyelid"+"3").getChild("EMF_right_blink"+"3"));
        }
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


            getEye("left_eye"+eyeNumberTo).getChild("left_eye_white"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_left_eye_white"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("left_eyelid"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_left_eyelid"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("left_eyelid"+eyeNumberTo).getChild("left_blink"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_left_eyelid"+eyeNumberFrom).getChild("EMF_left_blink"+eyeNumberFrom));
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


            getEye("right_eye"+eyeNumberTo).getChild("right_eye_white"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_right_eye_white"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("right_eyelid"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_right_eyelid"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("right_eyelid"+eyeNumberTo).getChild("right_blink"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_right_eyelid"+eyeNumberFrom).getChild("EMF_right_blink"+eyeNumberFrom));
        }
    }

    private void copyEyelids(final String eyeNumberFrom, final String eyeNumberTo){
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


            getEye("left_eye"+eyeNumberTo).getChild("left_eye_white"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_left_eye_white"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("left_eyelid"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_left_eyelid"+eyeNumberFrom));

            getEye("left_eye"+eyeNumberTo).getChild("left_eyelid"+eyeNumberTo).getChild("left_blink"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_left_eye"+eyeNumberFrom).getChild("EMF_left_eyelid"+eyeNumberFrom).getChild("EMF_left_blink"+eyeNumberFrom));
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


            getEye("right_eye"+eyeNumberTo).getChild("right_eye_white"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_right_eye_white"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("right_eyelid"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_right_eyelid"+eyeNumberFrom));

            getEye("right_eye"+eyeNumberTo).getChild("right_eyelid"+eyeNumberTo).getChild("right_blink"+eyeNumberTo)
                    .copyFrom(getEMFEye("EMF_right_eye"+eyeNumberFrom).getChild("EMF_right_eyelid"+eyeNumberFrom).getChild("EMF_right_blink"+eyeNumberFrom));
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


            getEye("left_eye"+eyeName).getChild("left_eye_white"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("left_eyelid"+eyeName).resetPose();

            getEye("left_eye"+eyeName).getChild("left_eyelid"+eyeName).getChild("left_blink"+eyeName).resetPose();
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


            getEye("right_eye"+eyeName).getChild("right_eye_white"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("right_eyelid"+eyeName).resetPose();

            getEye("right_eye"+eyeName).getChild("right_eyelid"+eyeName).getChild("right_blink"+eyeName).resetPose();
        }
    }

    private void setEyeOffsets(){
        setEyeOffset("_2x1");
        setEyeOffset("_2x2");
        setEyeOffset("_2x3");
        setEyeOffset("_derp");
        setEyeOffset("_enderman");
        setEyeOffset("_immobile");
    }

    private void setEyeOffset(final String eyeName){
        getEye("left_eye"+eyeName).y=getEye("left_eye"+eyeName).getInitialPose().y+24;
        getEye("right_eye"+eyeName).y=getEye("right_eye"+eyeName).getInitialPose().y+24;

        getEye("left_eye"+eyeName).z=getEye("left_eye"+eyeName).getInitialPose().z;
        getEye("right_eye"+eyeName).z=getEye("right_eye"+eyeName).getInitialPose().z;
    }

    private ModelPart getEye(final String eye){
        return toxicityFaceModel.face.getChild(eye);
    }

    private ModelPart getEMFEye(final String eye){
        return this.getParentModel().head.getChild("EMF_head").getChild("EMF_eyes").getChild(eye);
    }

    private boolean hasExpressiveModel(){
        return MiscUtil.hasExpressiveModel(this.getParentModel());
    }
}

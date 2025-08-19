package TCOTS.entity.witcher_cosmetics.witcher_eyes;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import org.joml.Vector3f;

public class WitcherEyesModel extends PlayerModel<AbstractClientPlayer> {
    public static final String EYES = "eyes";

    public final ModelPart eyes;
    public WitcherEyesModel(final ModelPart root) {
        super(root, false);
        this.eyes= root.getChild(PartNames.HEAD).getChild(EYES);
    }


    @Override
    public void setupAnim(final AbstractClientPlayer playerEntity, final float limbAngle, final float limbDistance, final float animationProgress, final float headYaw, final float headPitch) {

        final Vector3f Eyes_Pivot = playerEntity.theConjunctionOfTheSpheres$getEyesPivot();

        this.eyes.setPos(Eyes_Pivot.x, Eyes_Pivot.y+24, Eyes_Pivot.z);
    }

    public static MeshDefinition getModelData(final CubeDeformation dilation) {
        final MeshDefinition modelData = PlayerModel.createMesh(dilation, false);
        final PartDefinition root = modelData.getRoot();

        final PartDefinition eyes = root.getChild(PartNames.HEAD)
                .addOrReplaceChild(EYES,
                        CubeListBuilder.create()
                                .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f,
                                        new CubeDeformation(0.00f)), PartPose.offset(0.0F, 24.0F, 0.0F));

        //Eye/Eye2/Eye3/Eye4      -> 2x1 Eyes              -> _2x1
        //Eye5/Eye6/Eye7/Eye8     -> 2x2 Eyes              -> _2x2
        //Eye9                    -> 2x3 Eyes              -> _2x3
        //Eye10/Eye11             -> 3x1 Eyes Enderman     -> _enderman
        //Eye12/Eye13/Eye14/Eye15 -> 2x1 Eyes (Derp)       -> _derp
        //Eye16                   -> ???????????????       -> _????
        //Eye17/..../Eye24        -> 1x1 Eyes (Dont move)  -> _immobile
        //Eye17/..../Eye24        -> 1x1 Eyes (Only Blink) -> _immobile

        //2x1 Eyes (Steve Eyes)
        {
            final PartDefinition right_eye_2x1 = eyes.addOrReplaceChild("right_eye_2x1", CubeListBuilder.create(), PartPose.offset(-2.0F, -28.0F, -3.0F));

            final PartDefinition r_eye_pupil_2x1 = right_eye_2x1.addOrReplaceChild("r_eye_pupil_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_2x1 = r_eye_pupil_2x1.addOrReplaceChild("r_eye_pupil_in_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_2x1 = r_eye_pupil_in_2x1.addOrReplaceChild("r_eye_pupil_ou_2x1", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_2x1 = r_eye_pupil_ou_2x1.addOrReplaceChild("r_eye_pupil_do_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_2x1.addOrReplaceChild("r_eye_pupil_up_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_2x1.addOrReplaceChild("ctrl_r_pupil_2x1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

            final PartDefinition left_eye_2x1 = eyes.addOrReplaceChild("left_eye_2x1", CubeListBuilder.create(), PartPose.offset(2.0F, -28.0F, -3.0F));

            final PartDefinition l_eye_pupil_2x1 = left_eye_2x1.addOrReplaceChild("l_eye_pupil_2x1", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_2x1 = l_eye_pupil_2x1.addOrReplaceChild("l_eye_pupil_in_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_2x1 = l_eye_pupil_in_2x1.addOrReplaceChild("l_eye_pupil_ou_2x1", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_2x1 = l_eye_pupil_ou_2x1.addOrReplaceChild("l_eye_pupil_do_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_2x1.addOrReplaceChild("l_eye_pupil_up_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_2x1.addOrReplaceChild("ctrl_l_pupil_2x1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        }

        //2x2 Eyes (Anime Eyes)
        {
            final PartDefinition right_eye_2x2 = eyes.addOrReplaceChild("right_eye_2x2", CubeListBuilder.create(), PartPose.offset(-2.0F, -27.0F, -3.0F));

            final PartDefinition r_eye_pupil_2x2 = right_eye_2x2.addOrReplaceChild("r_eye_pupil_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_2x2 = r_eye_pupil_2x2.addOrReplaceChild("r_eye_pupil_in_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_2x2 = r_eye_pupil_in_2x2.addOrReplaceChild("r_eye_pupil_ou_2x2", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_2x2 = r_eye_pupil_ou_2x2.addOrReplaceChild("r_eye_pupil_do_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_2x2.addOrReplaceChild("r_eye_pupil_up_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_2x2.addOrReplaceChild("ctrl_r_pupil_2x2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

            final PartDefinition left_eye_2x2 = eyes.addOrReplaceChild("left_eye_2x2", CubeListBuilder.create(), PartPose.offset(2.0F, -27.0F, -3.0F));

            final PartDefinition l_eye_pupil_2x2 = left_eye_2x2.addOrReplaceChild("l_eye_pupil_2x2", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_2x2 = l_eye_pupil_2x2.addOrReplaceChild("l_eye_pupil_in_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_2x2 = l_eye_pupil_in_2x2.addOrReplaceChild("l_eye_pupil_ou_2x2", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_2x2 = l_eye_pupil_ou_2x2.addOrReplaceChild("l_eye_pupil_do_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_2x2.addOrReplaceChild("l_eye_pupil_up_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_2x2.addOrReplaceChild("ctrl_l_pupil_2x2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        }

        //2x3 Eyes (Big Anime Eyes)
        {
            final PartDefinition right_eye_2x3 = eyes.addOrReplaceChild("right_eye_2x3", CubeListBuilder.create(), PartPose.offset(-2.0F, -26.5F, -3.0F));

            final PartDefinition r_eye_pupil_2x3 = right_eye_2x3.addOrReplaceChild("r_eye_pupil_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_in_2x3 = r_eye_pupil_2x3.addOrReplaceChild("r_eye_pupil_in_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_2x3 = r_eye_pupil_in_2x3.addOrReplaceChild("r_eye_pupil_ou_2x3", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_2x3 = r_eye_pupil_ou_2x3.addOrReplaceChild("r_eye_pupil_do_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_2x3.addOrReplaceChild("r_eye_pupil_up_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_2x3.addOrReplaceChild("ctrl_r_pupil_2x3", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));

            final PartDefinition left_eye_2x3 = eyes.addOrReplaceChild("left_eye_2x3", CubeListBuilder.create(), PartPose.offset(2.0F, -26.5F, -3.0F));

            final PartDefinition l_eye_pupil_2x3 = left_eye_2x3.addOrReplaceChild("l_eye_pupil_2x3", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_in_2x3 = l_eye_pupil_2x3.addOrReplaceChild("l_eye_pupil_in_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_2x3 = l_eye_pupil_in_2x3.addOrReplaceChild("l_eye_pupil_ou_2x3", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_2x3 = l_eye_pupil_ou_2x3.addOrReplaceChild("l_eye_pupil_do_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_2x3.addOrReplaceChild("l_eye_pupil_up_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_2x3.addOrReplaceChild("ctrl_l_pupil_2x3", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));
        }

        //3x1 Eyes Enderman
        {
            final PartDefinition right_eye_enderman = eyes.addOrReplaceChild("right_eye_enderman", CubeListBuilder.create(), PartPose.offset(-2.0F, -28.0F, -4.0F));

            final PartDefinition r_eye_pupil_enderman = right_eye_enderman.addOrReplaceChild("r_eye_pupil_enderman", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_enderman = r_eye_pupil_enderman.addOrReplaceChild("r_eye_pupil_in_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_enderman = r_eye_pupil_in_enderman.addOrReplaceChild("r_eye_pupil_ou_enderman", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_enderman = r_eye_pupil_ou_enderman.addOrReplaceChild("r_eye_pupil_do_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_enderman.addOrReplaceChild("r_eye_pupil_up_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -0.055F, 1.0F, 1.0F, 0.05F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_enderman.addOrReplaceChild("ctrl_r_pupil_enderman", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

            final PartDefinition left_eye_enderman = eyes.addOrReplaceChild("left_eye_enderman", CubeListBuilder.create(), PartPose.offset(2.0F, -28.0F, -4.0F));

            final PartDefinition l_eye_pupil_enderman = left_eye_enderman.addOrReplaceChild("l_eye_pupil_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_enderman = l_eye_pupil_enderman.addOrReplaceChild("l_eye_pupil_in_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_enderman = l_eye_pupil_in_enderman.addOrReplaceChild("l_eye_pupil_ou_enderman", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_enderman = l_eye_pupil_ou_enderman.addOrReplaceChild("l_eye_pupil_do_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_enderman.addOrReplaceChild("l_eye_pupil_up_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -0.055F, 1.0F, 1.0F, 0.05F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_enderman.addOrReplaceChild("ctrl_l_pupil_enderman", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        }

        //2x1 Eyes Derp
        {
            final PartDefinition right_eye_derp = eyes.addOrReplaceChild("right_eye_derp", CubeListBuilder.create(), PartPose.offset(-2.0F, -28.0F, -3.0F));

            final PartDefinition r_eye_pupil_derp = right_eye_derp.addOrReplaceChild("r_eye_pupil_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_derp = r_eye_pupil_derp.addOrReplaceChild("r_eye_pupil_in_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_derp = r_eye_pupil_in_derp.addOrReplaceChild("r_eye_pupil_ou_derp", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_derp = r_eye_pupil_ou_derp.addOrReplaceChild("r_eye_pupil_do_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_derp.addOrReplaceChild("r_eye_pupil_up_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_derp.addOrReplaceChild("ctrl_r_pupil_derp", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

            final PartDefinition left_eye_derp = eyes.addOrReplaceChild("left_eye_derp", CubeListBuilder.create(), PartPose.offset(2.0F, -28.0F, -3.0F));

            final PartDefinition l_eye_pupil_derp = left_eye_derp.addOrReplaceChild("l_eye_pupil_derp", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_derp = l_eye_pupil_derp.addOrReplaceChild("l_eye_pupil_in_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_derp = l_eye_pupil_in_derp.addOrReplaceChild("l_eye_pupil_ou_derp", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_derp = l_eye_pupil_ou_derp.addOrReplaceChild("l_eye_pupil_do_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_derp.addOrReplaceChild("l_eye_pupil_up_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_derp.addOrReplaceChild("ctrl_l_pupil_derp", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        }

        //Immobile eyes (don't follow the pupil)
        {
            final PartDefinition right_eye17 = eyes.addOrReplaceChild("right_eye_immobile", CubeListBuilder.create(), PartPose.offset(-2.5F, -28.0F, -3.0F));

            right_eye17.addOrReplaceChild("r_eye_pupil_immobile",
                    CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            final PartDefinition left_eye17 = eyes.addOrReplaceChild("left_eye_immobile", CubeListBuilder.create(), PartPose.offset(2.5F, -28.0F, -3.0F));

            left_eye17.addOrReplaceChild("l_eye_pupil_immobile",
                    CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

        }

        return modelData;
    }
}



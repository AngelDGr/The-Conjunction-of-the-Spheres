package mors.tcots.entity.witcher_cosmetics.toxicity_face;

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

public class ToxicityFaceModel extends PlayerModel<AbstractClientPlayer> {
    public static final String FACE = "face";
    public final ModelPart face;
    public ToxicityFaceModel(final ModelPart root) {
        super(root, false);
        this.face= root.getChild(PartNames.HEAD).getChild(FACE);
    }

    @Override
    public void setupAnim(final AbstractClientPlayer livingEntity, final float f, final float g, final float h, final float i, final float j) {
        final Vector3f Eyes_Pivot = livingEntity.tcots$getEyesPivot();

        this.face.setPos(Eyes_Pivot.x, Eyes_Pivot.y, Eyes_Pivot.z);
    }

    //TODO: Finally, end with this, adding the rest of the eyes
    public static MeshDefinition getModelData(final CubeDeformation dilation) {
        final MeshDefinition modelData = PlayerModel.createMesh(dilation, false);
        final PartDefinition root = modelData.getRoot();

        final PartDefinition face = root.getChild(PartNames.HEAD)
                .addOrReplaceChild(FACE,
                        CubeListBuilder.create().texOffs(64, 0)
                                .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f,
                                        new CubeDeformation(0.0015f)), PartPose.offset(0.0f, 0.0F, 0.0f));

        //2x1 Eyes (Steve Eyes)
        {
            final PartDefinition right_eye_2x1 = face.addOrReplaceChild("right_eye_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.0155F, 2.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-2.0F, -28.0F, -3.0F));

            final PartDefinition r_eye_pupil_2x1 = right_eye_2x1.addOrReplaceChild("r_eye_pupil_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_2x1 = r_eye_pupil_2x1.addOrReplaceChild("r_eye_pupil_in_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_2x1 = r_eye_pupil_in_2x1.addOrReplaceChild("r_eye_pupil_ou_2x1", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_2x1 = r_eye_pupil_ou_2x1.addOrReplaceChild("r_eye_pupil_do_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_2x1.addOrReplaceChild("r_eye_pupil_up_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_2x1.addOrReplaceChild("ctrl_r_pupil_2x1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            right_eye_2x1.addOrReplaceChild("right_eye_white_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -1.0F, -1.03F, 1.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-1.0F, 1.0F, 0.0F));

            final PartDefinition right_eyelid_2x1 = right_eye_2x1.addOrReplaceChild("right_eyelid_2x1", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            right_eyelid_2x1.addOrReplaceChild("right_blink_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 1.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));


            final PartDefinition left_eye_2x1 = face.addOrReplaceChild("left_eye_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.0155F, 2.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(2.0F, -28.0F, -3.0F));

            final PartDefinition l_eye_pupil_2x1 = left_eye_2x1.addOrReplaceChild("l_eye_pupil_2x1", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_2x1 = l_eye_pupil_2x1.addOrReplaceChild("l_eye_pupil_in_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_2x1 = l_eye_pupil_in_2x1.addOrReplaceChild("l_eye_pupil_ou_2x1", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_2x1 = l_eye_pupil_ou_2x1.addOrReplaceChild("l_eye_pupil_do_2x1", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_2x1.addOrReplaceChild("l_eye_pupil_up_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_2x1.addOrReplaceChild("ctrl_l_pupil_2x1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            left_eye_2x1.addOrReplaceChild("left_eye_white_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -1.03F, 1.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(1.0F, 1.0F, 0.0F));

            final PartDefinition left_eyelid_2x1 = left_eye_2x1.addOrReplaceChild("left_eyelid_2x1", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            left_eyelid_2x1.addOrReplaceChild("left_blink_2x1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 1.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));
        }

        //2x2 Eyes (Anime Eyes)
        {
            final PartDefinition right_eye_2x2 = face.addOrReplaceChild("right_eye_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -1.0155F, 2.0F, 2.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-2.0F, -27.0F, -3.0F));

            final PartDefinition r_eye_pupil_2x2 = right_eye_2x2.addOrReplaceChild("r_eye_pupil_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_2x2 = r_eye_pupil_2x2.addOrReplaceChild("r_eye_pupil_in_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_2x2 = r_eye_pupil_in_2x2.addOrReplaceChild("r_eye_pupil_ou_2x2", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_2x2 = r_eye_pupil_ou_2x2.addOrReplaceChild("r_eye_pupil_do_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_2x2.addOrReplaceChild("r_eye_pupil_up_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_2x2.addOrReplaceChild("ctrl_r_pupil_2x2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            right_eye_2x2.addOrReplaceChild("right_eye_white_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -2.0F, -1.03F, 1.0F, 2.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-1.0F, 1.0F, 0.0F));

            final PartDefinition right_eyelid_2x2 = right_eye_2x2.addOrReplaceChild("right_eyelid_2x2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            right_eyelid_2x2.addOrReplaceChild("right_blink_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 2.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -2.0F, 0.0F));
            

            final PartDefinition left_eye_2x2 = face.addOrReplaceChild("left_eye_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -1.0155F, 2.0F, 2.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(2.0F, -27.0F, -3.0F));

            final PartDefinition l_eye_pupil_2x2 = left_eye_2x2.addOrReplaceChild("l_eye_pupil_2x2", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_2x2 = l_eye_pupil_2x2.addOrReplaceChild("l_eye_pupil_in_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_2x2 = l_eye_pupil_in_2x2.addOrReplaceChild("l_eye_pupil_ou_2x2", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_2x2 = l_eye_pupil_ou_2x2.addOrReplaceChild("l_eye_pupil_do_2x2", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_2x2.addOrReplaceChild("l_eye_pupil_up_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_2x2.addOrReplaceChild("ctrl_l_pupil_2x2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            left_eye_2x2.addOrReplaceChild("left_eye_white_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -2.0F, -1.03F, 1.0F, 2.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(1.0F, 1.0F, 0.0F));

            final PartDefinition left_eyelid_2x2 = left_eye_2x2.addOrReplaceChild("left_eyelid_2x2", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            left_eyelid_2x2.addOrReplaceChild("left_blink_2x2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 2.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -2.0F, 0.0F));
        }

        //2x3 Eyes (Big Anime Eyes)
        {
            final PartDefinition right_eye_2x3 = face.addOrReplaceChild("right_eye_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.5F, -1.0155F, 2.0F, 3.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-2.0F, -26.5F, -3.0F));

            final PartDefinition r_eye_pupil_2x3 = right_eye_2x3.addOrReplaceChild("r_eye_pupil_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_in_2x3 = r_eye_pupil_2x3.addOrReplaceChild("r_eye_pupil_in_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_2x3 = r_eye_pupil_in_2x3.addOrReplaceChild("r_eye_pupil_ou_2x3", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_2x3 = r_eye_pupil_ou_2x3.addOrReplaceChild("r_eye_pupil_do_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_2x3.addOrReplaceChild("r_eye_pupil_up_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_2x3.addOrReplaceChild("ctrl_r_pupil_2x3", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));
            //Extra
            right_eye_2x3.addOrReplaceChild("right_eye_white_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -3.0F, -1.03F, 1.0F, 3.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-1.0F, 1.5F, 0.0F));

            final PartDefinition right_eyelid_2x3 = right_eye_2x3.addOrReplaceChild("right_eyelid_2x3", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 1.0F));

            right_eyelid_2x3.addOrReplaceChild("right_blink_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 3.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -3.0F, 0.0F));


            final PartDefinition left_eye_2x3 = face.addOrReplaceChild("left_eye_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.5F, -1.0155F, 2.0F, 3.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(2.0F, -26.5F, -3.0F));

            final PartDefinition l_eye_pupil_2x3 = left_eye_2x3.addOrReplaceChild("l_eye_pupil_2x3", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_in_2x3 = l_eye_pupil_2x3.addOrReplaceChild("l_eye_pupil_in_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_2x3 = l_eye_pupil_in_2x3.addOrReplaceChild("l_eye_pupil_ou_2x3", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_2x3 = l_eye_pupil_ou_2x3.addOrReplaceChild("l_eye_pupil_do_2x3", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_2x3.addOrReplaceChild("l_eye_pupil_up_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, -1.0F, -1.07F, 1.0F, 2.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_2x3.addOrReplaceChild("ctrl_l_pupil_2x3", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));
            //Extra
            left_eye_2x3.addOrReplaceChild("left_eye_white_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -3.0F, -1.03F, 1.0F, 3.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(1.0F, 1.5F, 0.0F));

            final PartDefinition left_eyelid_2x3 = left_eye_2x3.addOrReplaceChild("left_eyelid_2x3", CubeListBuilder.create(), PartPose.offset(0.0F, 1.5F, 1.0F));

            left_eyelid_2x3.addOrReplaceChild("left_blink_2x3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 3.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -3.0F, 0.0F));

        }

        //3x1 Eyes Enderman
        {
            final PartDefinition right_eye_enderman = face.addOrReplaceChild("right_eye_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.0F, 0.0F, -0.001F, 3.0F, 1.0F, 0.015F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(-2.0F, -28.0F, -4.0F));

            final PartDefinition r_eye_pupil_enderman = right_eye_enderman.addOrReplaceChild("r_eye_pupil_enderman", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_enderman = r_eye_pupil_enderman.addOrReplaceChild("r_eye_pupil_in_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_enderman = r_eye_pupil_in_enderman.addOrReplaceChild("r_eye_pupil_ou_enderman", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_enderman = r_eye_pupil_ou_enderman.addOrReplaceChild("r_eye_pupil_do_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_enderman.addOrReplaceChild("r_eye_pupil_up_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -0.055F, 1.0F, 1.0F, 0.05F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_enderman.addOrReplaceChild("ctrl_r_pupil_enderman", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            right_eye_enderman.addOrReplaceChild("right_eye_white_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-3.0F, -1.0F, -0.03F, 3.0F, 1.0F, 0.03F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(1.0F, 1.0F, 0.0F));

            final PartDefinition right_eyelid_enderman = right_eye_enderman.addOrReplaceChild("right_eyelid_enderman", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            right_eyelid_enderman.addOrReplaceChild("right_blink_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.025F, 0.015F, -0.08F, 3.025F, 1.0F, 0.075F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -1.015F, 0.0F));


            final PartDefinition left_eye_enderman = face.addOrReplaceChild("left_eye_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -0.001F, 3.0F, 1.0F, 0.015F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(2.0F, -28.0F, -4.0F));

            final PartDefinition l_eye_pupil_enderman = left_eye_enderman.addOrReplaceChild("l_eye_pupil_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_enderman = l_eye_pupil_enderman.addOrReplaceChild("l_eye_pupil_in_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_enderman = l_eye_pupil_in_enderman.addOrReplaceChild("l_eye_pupil_ou_enderman", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_enderman = l_eye_pupil_ou_enderman.addOrReplaceChild("l_eye_pupil_do_enderman", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_enderman.addOrReplaceChild("l_eye_pupil_up_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -0.055F, 1.0F, 1.0F, 0.05F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_enderman.addOrReplaceChild("ctrl_l_pupil_enderman", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            left_eye_enderman.addOrReplaceChild("left_eye_white_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -1.0F, -0.03F, 3.0F, 1.0F, 0.03F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-1.0F, 1.0F, 0.0F));

            final PartDefinition left_eyelid_enderman = left_eye_enderman.addOrReplaceChild("left_eyelid_enderman", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            left_eyelid_enderman.addOrReplaceChild("left_blink_enderman", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.015F, -0.08F, 3.025F, 1.0F, 0.075F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -1.015F, 0.0F));
        }

        //2x1 Eyes Derp
        {
            final PartDefinition right_eye_derp = face.addOrReplaceChild("right_eye_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.0155F, 2.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-2.0F, -28.0F, -3.0F));

            final PartDefinition r_eye_pupil_derp = right_eye_derp.addOrReplaceChild("r_eye_pupil_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            final PartDefinition r_eye_pupil_in_derp = r_eye_pupil_derp.addOrReplaceChild("r_eye_pupil_in_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_ou_derp = r_eye_pupil_in_derp.addOrReplaceChild("r_eye_pupil_ou_derp", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition r_eye_pupil_do_derp = r_eye_pupil_ou_derp.addOrReplaceChild("r_eye_pupil_do_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            r_eye_pupil_do_derp.addOrReplaceChild("r_eye_pupil_up_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            right_eye_derp.addOrReplaceChild("ctrl_r_pupil_derp", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            right_eye_derp.addOrReplaceChild("right_eye_white_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -1.0F, -1.03F, 1.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(-1.0F, 1.0F, 0.0F));

            final PartDefinition right_eyelid_derp = right_eye_derp.addOrReplaceChild("right_eyelid_derp", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            right_eyelid_derp.addOrReplaceChild("right_blink_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 1.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));


            final PartDefinition left_eye_derp = face.addOrReplaceChild("left_eye_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.0155F, 2.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(2.0F, -28.0F, -3.0F));

            final PartDefinition l_eye_pupil_derp = left_eye_derp.addOrReplaceChild("l_eye_pupil_derp", CubeListBuilder.create(), PartPose.offset(-0.5F, 0.5F, 0.0F));

            final PartDefinition l_eye_pupil_in_derp = l_eye_pupil_derp.addOrReplaceChild("l_eye_pupil_in_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_ou_derp = l_eye_pupil_in_derp.addOrReplaceChild("l_eye_pupil_ou_derp", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

            final PartDefinition l_eye_pupil_do_derp = l_eye_pupil_ou_derp.addOrReplaceChild("l_eye_pupil_do_derp", CubeListBuilder.create(), PartPose.offset(0.5F, 0.5F, 0.0F));

            l_eye_pupil_do_derp.addOrReplaceChild("l_eye_pupil_up_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0056F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            left_eye_derp.addOrReplaceChild("ctrl_l_pupil_derp", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
            //Extra
            left_eye_derp.addOrReplaceChild("left_eye_white_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -1.0F, -1.03F, 1.0F, 1.0F, 0.25F, new CubeDeformation(0.001F)).mirror(false), PartPose.offset(1.0F, 1.0F, 0.0F));

            final PartDefinition left_eyelid_derp = left_eye_derp.addOrReplaceChild("left_eyelid_derp", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 1.0F));

            left_eyelid_derp.addOrReplaceChild("left_blink_derp", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, 0.0F, -1.08F, 2.0F, 1.0F, 0.3F, new CubeDeformation(0.016F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));
        }

        //Immobile eyes (don't follow the pupil)
        {
            final PartDefinition right_eye17 = face.addOrReplaceChild("right_eye_immobile", CubeListBuilder.create(), PartPose.offset(-2.5F, -28.0F, -3.0F));

            right_eye17.addOrReplaceChild("r_eye_pupil_immobile",
                    CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

            final PartDefinition left_eye17 = face.addOrReplaceChild("left_eye_immobile", CubeListBuilder.create(), PartPose.offset(2.5F, -28.0F, -3.0F));

            left_eye17.addOrReplaceChild("l_eye_pupil_immobile",
                    CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-0.5F, 0.0F, -1.07F, 1.0F, 1.0F, 0.3F, new CubeDeformation(0.0055F)).mirror(false), PartPose.offset(0.0F, -1.0F, 0.0F));

        }

        //Special Eyelids
        {
        }

        return modelData;
    }
}

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
    public WitcherEyesModel(ModelPart root) {
        super(root, false);
        this.eyes= root.getChild(PartNames.HEAD).getChild(EYES);
    }


    @Override
    public void setupAnim(AbstractClientPlayer playerEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        Vector3f Eyes_Pivot = playerEntity.theConjunctionOfTheSpheres$getEyesPivot();

        this.eyes.setPos(Eyes_Pivot.x, Eyes_Pivot.y, Eyes_Pivot.z);
    }

    public static MeshDefinition getModelData(CubeDeformation dilation) {
        MeshDefinition modelData = PlayerModel.createMesh(dilation, false);
        PartDefinition root = modelData.getRoot();

        root.getChild(PartNames.HEAD)
                .addOrReplaceChild(EYES,
                        CubeListBuilder.create().texOffs(64, 0)
                                .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f,
                                        new CubeDeformation(0.001f)),
                        PartPose.offset(0.0f, 0.0f, 0.0f));

        return modelData;
    }



}



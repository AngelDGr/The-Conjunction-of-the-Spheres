package TCOTS.entity.witcher_cosmetics.toxicity_face;

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
    public ToxicityFaceModel(ModelPart root) {
        super(root, false);
        this.face= root.getChild(PartNames.HEAD).getChild(FACE);
    }

    @Override
    public void setupAnim(AbstractClientPlayer livingEntity, float f, float g, float h, float i, float j) {
        Vector3f Eyes_Pivot = livingEntity.theConjunctionOfTheSpheres$getEyesPivot();

        this.face.setPos(Eyes_Pivot.x, Eyes_Pivot.y, Eyes_Pivot.z);
    }

    public static MeshDefinition getModelData(CubeDeformation dilation) {
        MeshDefinition modelData = PlayerModel.createMesh(dilation, false);
        PartDefinition root = modelData.getRoot();

        root.getChild(PartNames.HEAD)
                .addOrReplaceChild(FACE,
                        CubeListBuilder.create().texOffs(64, 0)
                                .addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f,
                                        new CubeDeformation(0.0015f)),
                        PartPose.offset(0.0f, 0.0f, 0.0f));

        return modelData;
    }

}

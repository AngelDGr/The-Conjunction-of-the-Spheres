package TCOTS.entity.witcher_eyes;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.joml.Vector3f;

public class WitcherEyesModel extends PlayerEntityModel<AbstractClientPlayerEntity> {
    public static final String EYES = "eyes";

    public final ModelPart eyes;
    public WitcherEyesModel(ModelPart root) {
        super(root, false);
        this.eyes= root.getChild(EntityModelPartNames.HEAD).getChild(EYES);
    }


    @Override
    public void setAngles(AbstractClientPlayerEntity playerEntity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

        Vector3f Eyes_Pivot = playerEntity.theConjunctionOfTheSpheres$getEyesPivot();

        this.eyes.setPivot(Eyes_Pivot.x, Eyes_Pivot.y, Eyes_Pivot.z);
    }

    public static ModelData getModelData(Dilation dilation) {
        ModelData modelData = PlayerEntityModel.getTexturedModelData(dilation, false);
        ModelPartData root = modelData.getRoot();

        root.getChild(EntityModelPartNames.HEAD)
                .addChild(EYES,
                        ModelPartBuilder.create().uv(64, 0)
                                .cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f,
                                        new Dilation(0.001f)),
                        ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        return modelData;
    }


    public static EntityModelLayerRegistry.TexturedModelDataProvider createModelData(){

        return () -> TexturedModelData.of(
                getModelData(new Dilation(0)),
                96, 64);
    }


}



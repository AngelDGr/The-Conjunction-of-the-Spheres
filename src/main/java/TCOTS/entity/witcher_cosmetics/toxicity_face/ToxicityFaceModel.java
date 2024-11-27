package TCOTS.entity.witcher_cosmetics.toxicity_face;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.*;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.joml.Vector3f;

@Environment(value= EnvType.CLIENT)
public class ToxicityFaceModel extends PlayerEntityModel<AbstractClientPlayerEntity> {
    public static final String FACE = "face";
    public final ModelPart face;
    public ToxicityFaceModel(ModelPart root) {
        super(root, false);
        this.face= root.getChild(EntityModelPartNames.HEAD).getChild(FACE);
    }

    @Override
    public void setAngles(AbstractClientPlayerEntity livingEntity, float f, float g, float h, float i, float j) {
        Vector3f Eyes_Pivot = livingEntity.theConjunctionOfTheSpheres$getEyesPivot();

        this.face.setPivot(Eyes_Pivot.x, Eyes_Pivot.y, Eyes_Pivot.z);
    }

    public static ModelData getModelData(Dilation dilation) {
        ModelData modelData = PlayerEntityModel.getTexturedModelData(dilation, false);
        ModelPartData root = modelData.getRoot();

        root.getChild(EntityModelPartNames.HEAD)
                .addChild(FACE,
                        ModelPartBuilder.create().uv(64, 0)
                                .cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f,
                                        new Dilation(0.0015f)),
                        ModelTransform.pivot(0.0f, 0.0f, 0.0f));

        return modelData;
    }

    public static EntityModelLayerRegistry.TexturedModelDataProvider createModelData(){
        return () -> TexturedModelData.of(
                getModelData(new Dilation(0)),
                96, 64);
    }
}

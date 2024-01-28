package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.entity.necrophages.RotfiendEntity;
import TCOTS.entity.ogroids.NekkerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class NekkerModel extends GeoModel<NekkerEntity> {
    @Override
    public Identifier getModelResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/ogroids/nekker.geo.json");
    }

    @Override
    public Identifier getTextureResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/nekker/nekker.png");
    }

    @Override
    public Identifier getAnimationResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/ogroids/nekker.animation.json");
    }

    @Override
    public void setCustomAnimations(NekkerEntity entity, long instanceId, AnimationState<NekkerEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");


        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}

package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.AlghoulEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AlghoulModel extends GeoModel<AlghoulEntity> {

    @Override
    public Identifier getModelResource(AlghoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/alghoul.geo.json");
    }

    @Override
    public Identifier getTextureResource(AlghoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul.png");
    }

    @Override
    public Identifier getAnimationResource(AlghoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/alghoul.animation.json");
    }

    @Override
    public void setCustomAnimations(AlghoulEntity entity, long instanceId, AnimationState<AlghoulEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        CoreGeoBone large_spikes_head = getAnimationProcessor().getBone("spikes_large");
        CoreGeoBone large_spikes_body = getAnimationProcessor().getBone("spikes_large_body");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX((entityData.headPitch()+90f) * MathHelper.RADIANS_PER_DEGREE);
            head.setRotZ((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
        }

        if(large_spikes_head != null){
            large_spikes_head.setHidden(true);
        }
        if(large_spikes_body!=null){
            large_spikes_body.setHidden(true);
        }
    }
}

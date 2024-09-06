package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.ogroids.RockTrollEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class RockTrollModel extends BipedGeoModelBase<RockTrollEntity> {
    @Override
    public Identifier getModelResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/ogroids/rock_troll.geo.json");
    }

    @Override
    public Identifier getTextureResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/rock_troll.png");
    }

    @Override
    public Identifier getAnimationResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/ogroids/rock_troll.animation.json");
    }

    protected boolean hasArmZMovement(){
        return false;
    }

    @Override
    protected boolean hasNormalHead() {
        return false;
    }

    @Override
    public void setCustomAnimations(RockTrollEntity troll, long instanceId, AnimationState<RockTrollEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if(troll.hasBarteringItem()){
                head.setRotX(((-22f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY((( 12f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(((-10f) * MathHelper.RADIANS_PER_DEGREE));
            }
            else if(troll.isBlocking()){
                head.setRotX(((-15f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY(((-22.5f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(0);
            }
            else if(animationState.isMoving() && troll.isAttacking()){
                head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking()) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(0);
            }
            else {
                head.setRotX((entityData.headPitch()) * MathHelper.RADIANS_PER_DEGREE);
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotZ(0);
            }
        }

        super.setCustomAnimations(troll, instanceId, animationState);
    }
}

package mors.tcots.client.geo.animation.entity.ogroid;

import mors.tcots.entity.monsters.ogroids.IceGiantEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class IceGiantAnimations {

    public static final RawAnimation BIG_ATTACK = RawAnimation.begin().thenPlay("attack.big_swing");
    public static final RawAnimation ANCHOR_LAUNCH = RawAnimation.begin().thenPlay("attack.anchor_launch");

    public static final RawAnimation START_SLEEP = RawAnimation.begin().thenPlay("special.start_sleep");
    public static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("special.sleep");
    public static final RawAnimation END_SLEEP = RawAnimation.begin().thenPlay("special.end_sleep");

    public static AnimationController<IceGiantEntity> mainController(final IceGiantEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.setControllerSpeed(2);
                if(state.getController().getTriggeredAnimation()!=null
                        && (state.getController().getTriggeredAnimation().equals(START_SLEEP)
                        || state.getController().getTriggeredAnimation().equals(END_SLEEP)
                        || state.getController().getTriggeredAnimation().equals(BIG_ATTACK))) state.setControllerSpeed(1);
                return PlayState.CONTINUE;
            }

            switch (animatable.getPose()) {
                case SITTING:
                    state.getController().transitionLength(5);
                    return state.setAndContinue(SLEEP);
                default:
                    break;
            }

            //Move animations
            if(state.isMoving()){
                //Charge
                if (animatable.isCharging()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                    return state.setAndContinue(GeckoAnimationsUtil.CHARGE);
                }

                //Run
                if (animatable.isAggressive()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * 3f);
                    return state.setAndContinue(DefaultAnimations.RUN);
                }

                //Default-Walk
                state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            state.getController().transitionLength(5);
            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("big_attack", BIG_ATTACK)
                .triggerableAnim("anchor_launch", ANCHOR_LAUNCH)

                .triggerableAnim("start_sleep", START_SLEEP)
                .triggerableAnim("end_sleep", END_SLEEP);

//        .setParticleKeyframeHandler(event -> {
//            final BlockState blockState = this.getBlockStateOn();
//            if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
//                for (int i = 0; i < 32; ++i) {
//
//                    final Vec3 vec3dVelocity = new Vec3(
//                            ((double)this.random.nextFloat()) * 0.3,
//                            0.0,
//                            ((double)this.random.nextFloat()) * 0.3)
//                            .xRot(-this.getXRot() * ((float)Math.PI / 180))
//                            .yRot(-this.getYRot() * ((float)Math.PI / 180));
//
//                    final Vec3 vec3dPos = new Vec3((
//                            (double)this.random.nextFloat() - 0.5) * 0.6,
//                            (double)(-this.random.nextFloat()) * 0.01,
//                            1.6 + ((double)this.random.nextFloat() - 0.5) * 0.6)
//                            .yRot(-this.yBodyRot * ((float)Math.PI / 180))
//                            .add(this.getX(), this.getY(), this.getZ());
//
//                    this.level().addParticle(
//                            new BlockParticleOption(ParticleTypes.BLOCK, blockState),
//                            //Position
//                            vec3dPos.x,
//                            vec3dPos.y,
//                            vec3dPos.z,
//                            //Velocity
//                            vec3dVelocity.x,
//                            vec3dVelocity.y + 0.1,
//                            vec3dVelocity.z);
//                }
//            }
//        }).setSoundKeyframeHandler(event -> this.playSound(TCOTS_Sounds.getSoundEvent("big_impact"), 1, 1))
    }
}

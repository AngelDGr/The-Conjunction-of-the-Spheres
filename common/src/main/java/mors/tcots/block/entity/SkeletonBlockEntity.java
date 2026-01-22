package mors.tcots.block.entity;

import mors.tcots.block.SkeletonBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SkeletonBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SkeletonBlockEntity(final BlockEntityType<?> entityType, final BlockPos pos, final BlockState state) {
        super(entityType, pos, state);
    }

    public static final RawAnimation SITTING = RawAnimation.begin().thenPlayAndHold("pose.sitting");
    public static final RawAnimation REACHING = RawAnimation.begin().thenPlayAndHold("pose.reaching");
    public static final RawAnimation HALF_BODY_UP = RawAnimation.begin().thenPlayAndHold("pose.half_body_up");
    public static final RawAnimation HALF_BODY_DOWN = RawAnimation.begin().thenPlayAndHold("pose.half_body_down");
    public static final RawAnimation CROSSED_ARMS = RawAnimation.begin().thenPlayAndHold("pose.crossed_arms");
    public static final RawAnimation LEGS = RawAnimation.begin().thenPlayAndHold("pose.legs");
    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 0, state -> {
                    state.getController().transitionLength(0);
                    state.setControllerSpeed(1);

                    return switch (this.getBlockState().getValue(SkeletonBlock.SHAPE)){
                        case 0 -> state.setAndContinue(HALF_BODY_DOWN);
                        case 1 -> state.setAndContinue(LEGS);
                        case 2 -> state.setAndContinue(SITTING);
                        case 3 -> state.setAndContinue(HALF_BODY_UP);
                        case 4 -> state.setAndContinue(CROSSED_ARMS);
                        default -> state.setAndContinue(REACHING);
                    };
                }
                )
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

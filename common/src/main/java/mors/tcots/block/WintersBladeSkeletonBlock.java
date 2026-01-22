package mors.tcots.block;

import mors.tcots.block.entity.WintersBladeSkeletonBlockEntity;
import mors.tcots.client.geo.renderer.block.WintersBladeSkeletonRenderer;
import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.utils.GeckoAnimationsUtil;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class WintersBladeSkeletonBlock extends SkeletonBlock {
    private static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);

    public WintersBladeSkeletonBlock() {
        super();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(final @NotNull BlockState state, final @NotNull Level level, final @NotNull BlockPos pos, final RandomSource random) {
        if (random.nextInt(3) != 0) return;

        final double x = pos.getX() + 0.5;
        final double y = pos.getY() + 0.7;
        final double z = pos.getZ() + 0.5;

        final double dx = (random.nextDouble() - 0.5) * 0.2;
        final double dy = random.nextDouble() * 0.3;
        final double dz = (random.nextDouble() - 0.5) * 0.2;

        level.addParticle(ParticleTypes.GLOW,
                x + dx,
                y + dy,
                z + dz,
                0,
                0.1,
                0
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new WintersBladeSkeletonBlockEntity(TCOTS_Blocks.WintersBladeSkeletonBlockEntity(), pos, state);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    public static class Item extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        public Item(final Block block, final Properties settings) {
            super(block, settings);
        }

        @Override
        public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new GeoRenderProvider() {

                private final WintersBladeSkeletonRenderer.Item renderer = new WintersBladeSkeletonRenderer.Item();

                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                    return this.renderer;
                }
            });
        }

        @Override
        public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(GeckoAnimationsUtil.genericIdleController(this));
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }
    }
}

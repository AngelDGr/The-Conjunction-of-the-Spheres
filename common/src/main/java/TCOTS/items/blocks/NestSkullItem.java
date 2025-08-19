package TCOTS.items.blocks;

import TCOTS.items.geo.renderer.NestSkullItemRenderer;
import TCOTS.utils.GeoControllersUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class NestSkullItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected final Block wallBlock;
    private final Direction verticalAttachmentDirection;
    public NestSkullItem(final Block block, final Block wallBlock, final Properties settings, final Direction verticalAttachmentDirection) {
        super(block, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.wallBlock = wallBlock;
        this.verticalAttachmentDirection = verticalAttachmentDirection;
    }

    @Override
    public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {

            private final NestSkullItemRenderer renderer = new NestSkullItemRenderer();

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeoControllersUtil.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    protected boolean canPlaceAt(final LevelReader world, final BlockState state, final BlockPos pos) {
        return state.canSurvive(world, pos);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(@NotNull final BlockPlaceContext context) {
        final BlockState blockState = this.wallBlock.getStateForPlacement(context);
        BlockState blockState2 = null;
        final Level worldView = context.getLevel();
        final BlockPos blockPos = context.getClickedPos();
        for (final Direction direction : context.getNearestLookingDirections()) {
            final BlockState blockState3;
            if (direction == this.verticalAttachmentDirection.getOpposite()) continue;
            blockState3 = direction == this.verticalAttachmentDirection ? this.getBlock().getStateForPlacement(context) : blockState;
            if (blockState3 == null || !this.canPlaceAt(worldView, blockState3, blockPos)) continue;
            blockState2 = blockState3;
            break;
        }
        return blockState2 != null && worldView.isUnobstructed(blockState2, blockPos, CollisionContext.empty()) ? blockState2 : null;
    }

    @Override
    public void registerBlocks(@NotNull final Map<Block, Item> map, @NotNull final Item item) {
        super.registerBlocks(map, item);
        map.put(this.wallBlock, item);
    }


}

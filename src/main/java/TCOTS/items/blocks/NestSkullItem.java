package TCOTS.items.blocks;

import TCOTS.items.geo.renderer.NestSkullItemRenderer;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
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

public class NestSkullItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected final Block wallBlock;
    private final Direction verticalAttachmentDirection;
    public NestSkullItem(Block block, Block wallBlock, Settings settings, Direction verticalAttachmentDirection) {
        super(block, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.wallBlock = wallBlock;
        this.verticalAttachmentDirection = verticalAttachmentDirection;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {

            private final NestSkullItemRenderer renderer = new NestSkullItemRenderer();

            @Override
            public @NotNull BuiltinModelItemRenderer getGeoItemRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeoControllersUtil.genericIdleController(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    protected boolean canPlaceAt(WorldView world, BlockState state, BlockPos pos) {
        return state.canPlaceAt(world, pos);
    }

    @Override
    @Nullable
    protected BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = this.wallBlock.getPlacementState(context);
        BlockState blockState2 = null;
        World worldView = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        for (Direction direction : context.getPlacementDirections()) {
            BlockState blockState3;
            if (direction == this.verticalAttachmentDirection.getOpposite()) continue;
            blockState3 = direction == this.verticalAttachmentDirection ? this.getBlock().getPlacementState(context) : blockState;
            if (blockState3 == null || !this.canPlaceAt(worldView, blockState3, blockPos)) continue;
            blockState2 = blockState3;
            break;
        }
        return blockState2 != null && worldView.canPlace(blockState2, blockPos, ShapeContext.absent()) ? blockState2 : null;
    }

    @Override
    public void appendBlocks(Map<Block, Item> map, Item item) {
        super.appendBlocks(map, item);
        map.put(this.wallBlock, item);
    }


}

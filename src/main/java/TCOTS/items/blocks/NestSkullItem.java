package TCOTS.items.blocks;

import TCOTS.blocks.geo.NestSkullBlockRenderer;
import TCOTS.blocks.skull.NestSkullBlockEntity;
import TCOTS.items.geo.NestSkullItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NestSkullItem extends BlockItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final Supplier<Object> renderProvider= GeoItem.makeRenderer(this);

    protected final Block wallBlock;
    private final Direction verticalAttachmentDirection;
    public NestSkullItem(Block block, Block wallBlock, Settings settings, Direction verticalAttachmentDirection) {
        super(block, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.wallBlock = wallBlock;
        this.verticalAttachmentDirection = verticalAttachmentDirection;
    }

//    public NestSkullItem(Block block, Settings settings) {
//        super(block, settings);
//        SingletonGeoAnimatable.registerSyncedAnimatable(this);
////        this.wallBlock = wallBlock;
////        this.verticalAttachmentDirection = verticalAttachmentDirection;
//    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {

            private final NestSkullItemRenderer renderer = new NestSkullItemRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }



    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState<NestSkullItem> nestSkullItemAnimationState) {
        nestSkullItemAnimationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
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
            BlockState blockState4 = blockState3 = direction == this.verticalAttachmentDirection ? this.getBlock().getPlacementState(context) : blockState;
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

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }


}

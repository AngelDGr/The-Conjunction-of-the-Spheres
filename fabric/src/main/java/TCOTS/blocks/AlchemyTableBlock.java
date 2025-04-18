package TCOTS.blocks;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.items.AlchemyBookItem;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.concoctions.EmptyWitcherPotionItem;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil_Fabric;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class AlchemyTableBlock extends BaseEntityBlock implements EntityBlock {
    public static final MapCodec<AlchemyTableBlock> CODEC = AlchemyTableBlock.simpleCodec(AlchemyTableBlock::new);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty HAS_ALCHEMY_BOOK = BooleanProperty.create("has_alchemy_book");

    public @NotNull MapCodec<AlchemyTableBlock> codec() {
        return CODEC;
    }

    protected AlchemyTableBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
        this.registerDefaultState(defaultBlockState().setValue(HAS_ALCHEMY_BOOK, false));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state){
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AlchemyTableBlockEntity(TCOTS_Blocks_Fabric.ALCHEMY_TABLE_ENTITY, pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag options) {
        super.appendHoverText(stack, context, tooltip, options);
        tooltip.add(Component.translatable("block.tcots_witcher.alchemy_table.tooltip").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("block.tcots_witcher.alchemy_table.tooltip_book").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_ALCHEMY_BOOK);
    }


    //Crafting stuff

    @Override
    public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AlchemyTableBlockEntity) {
                world.updateNeighbourForOutputSignal(pos,this);
            }
            if (state.getValue(HAS_ALCHEMY_BOOK)) {
                this.dropBook(state, world, pos);
            }

            super.onRemove(state, world, pos, newState, moved);
        }
    }

    private void dropBook(BlockState state, Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AlchemyTableBlockEntity) {
            ItemStack book = new ItemStack(TCOTS_Items_Fabric.ALCHEMY_BOOK,1);
            Direction direction = state.getValue(FACING);
            float f = 0.25f * (float)direction.getStepX();
            float g = 0.25f * (float)direction.getStepZ();
            ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5 + (double)f, pos.getY() + 1, (double)pos.getZ() + 0.5 + (double)g, book);
            itemEntity.setDefaultPickUpDelay();
            world.addFreshEntity(itemEntity);
        }
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {

        if (!world.isClientSide) {
            //If the player have an alcohol in the hand refill the potion
            if(player.getMainHandItem().getItem() instanceof WitcherAlcohol_Base alcohol){

                int loopP= EntitiesUtil_Fabric.isWearingManticoreArmor(player)? alcohol.getRefillQuantity()+2:alcohol.getRefillQuantity();
                boolean refilled=false;

                //Makes a loop across all the inventory
                for(int i=0; i<player.getInventory().getContainerSize(); i++){
                    //If found an Empty Potion with a component
                    if(player.getInventory().getItem(i).getItem() instanceof EmptyWitcherPotionItem && player.getInventory().getItem(i).has(TCOTS_Items_Fabric.REFILL_RECIPE)){
                        String refillItem= player.getInventory().getItem(i).get(TCOTS_Items_Fabric.REFILL_RECIPE);
                        if(refillItem!=null){
                            //Save the potion type
                            Item PotionI = BuiltInRegistries.ITEM.get(ResourceLocation.parse(refillItem));

                            //Saves the count of empty bottles
                            int countI = player.getInventory().getItem(i).getCount();

                            //Erases the slot
                            player.getInventory().getItem(i).shrink(player.getInventory().getItem(i).getCount());
                            //Put the potion in the slot
                            player.getInventory().setItem(i,new ItemStack(PotionI, countI));

                            //Increases in 1
                            loopP=loopP-1;

                            //Put the refilled boolean in true
                            refilled=true;

                            //If it has already filled the slots, it stops
                            if(loopP < 1){
                                //Decrements the alcohol in hand
                                if(!player.isCreative()){
                                    player.getMainHandItem().shrink(1);
                                }
                                //Triggers the advancement
                                if(player instanceof ServerPlayer serverPlayer) TCOTS_Criteria.REFILL_CONCOCTION.trigger(serverPlayer);
                                //Play a sound
                                world.playSound(null, pos, TCOTS_Sounds.POTION_REFILLED, SoundSource.BLOCKS, 1.0f, 1.0f);
                                //Success
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }

                    //If it doesn't fulfill all the maximum potions
                    if(i == player.getInventory().getContainerSize()-1 && refilled){
                        //Decrements the alcohol in hand
                        if(!player.isCreative()){
                            player.getMainHandItem().shrink(1);
                        }
                        //Triggers the advancement
                        if(player instanceof ServerPlayer serverPlayer) TCOTS_Criteria.REFILL_CONCOCTION.trigger(serverPlayer);
                        //Play a sound
                        world.playSound(null, pos, TCOTS_Sounds.POTION_REFILLED, SoundSource.BLOCKS, 3.0f, 1.0f);
                        //Success
                        return InteractionResult.SUCCESS;
                    }
                }
            }


            //For the Alchemy Book
            if(world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof AlchemyTableBlockEntity){
                //If the player has an alchemy book in hand
                if(player.getMainHandItem().getItem() instanceof AlchemyBookItem &&
                        !(world.getBlockState(pos).getValue(HAS_ALCHEMY_BOOK)) && !(player.isShiftKeyDown())) {

                    player.getMainHandItem().shrink(1);

                    world.playSound(null, pos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0f, 1.0f);

                    world.setBlockAndUpdate(pos, state.setValue(HAS_ALCHEMY_BOOK, true));

                    return InteractionResult.SUCCESS;
                }

                //If the table has already a book
                if(world.getBlockState(pos).getValue(HAS_ALCHEMY_BOOK)
                        && player.getMainHandItem().isEmpty() && player.isShiftKeyDown()){

                    ItemStack book = new ItemStack(TCOTS_Items_Fabric.ALCHEMY_BOOK,1);

                    player.getInventory().add(book);
                    world.setBlockAndUpdate(pos, state.setValue(HAS_ALCHEMY_BOOK, false));

                    return InteractionResult.SUCCESS;
                }
            }


            //Screen opener
            MenuProvider screenHandlerFactory = ((AlchemyTableBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null) {
                player.openMenu(screenHandlerFactory);
            }
        }

        return InteractionResult.SUCCESS;
    }

}

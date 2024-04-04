package TCOTS.blocks;

import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.items.AlchemyBookItem;
import TCOTS.items.TCOTS_Items;
import TCOTS.potions.EmptyWitcherPotionItem;
import TCOTS.potions.WitcherAlcohol_Base;
import TCOTS.sounds.TCOTS_Sounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class AlchemyTableBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<AlchemyTableBlock> CODEC = AlchemyTableBlock.createCodec(AlchemyTableBlock::new);

    public MapCodec<AlchemyTableBlock> getCodec() {
        return CODEC;
    }

    //Properties

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public static final BooleanProperty HAS_ALCHEMY_BOOK = BooleanProperty.of("has_alchemy_book");

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    protected AlchemyTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
        this.setDefaultState(getDefaultState().with(HAS_ALCHEMY_BOOK, false));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyTableBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
            tooltip.add(Text.translatable("block.tcots-witcher.alchemy_table.tooltip").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("block.tcots-witcher.alchemy_table.tooltip_book").formatted(Formatting.GRAY));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HAS_ALCHEMY_BOOK);
    }


    //Crafting stuff
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AlchemyTableBlockEntity) {
                ItemScatterer.spawn(world, pos, (AlchemyTableBlockEntity)blockEntity);
                world.updateComparators(pos,this);
            }
            if (state.get(HAS_ALCHEMY_BOOK)) {
                this.dropBook(state, world, pos);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    private void dropBook(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof AlchemyTableBlockEntity) {
            ItemStack book = new ItemStack(TCOTS_Items.ALCHEMY_BOOK,1);
            Direction direction = state.get(FACING);
            float f = 0.25f * (float)direction.getOffsetX();
            float g = 0.25f * (float)direction.getOffsetZ();
            ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5 + (double)f, pos.getY() + 1, (double)pos.getZ() + 0.5 + (double)g, book);
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!world.isClient) {
            //If the player have an alcohol in the hand refill the potion
            if(player.getMainHandStack().getItem() instanceof WitcherAlcohol_Base alcohol){

                int loopP=alcohol.getRefillQuantity();
                boolean refilled=false;

                //Makes a loop across all the inventory
                for(int i=0; i<player.getInventory().size(); i++){
                    //If found an Empty Potion with NBT
                    if(player.getInventory().getStack(i).getItem() instanceof EmptyWitcherPotionItem && player.getInventory().getStack(i).hasNbt()){
                        NbtCompound nbtCompoundI= player.getInventory().getStack(i).getNbt();
                        //Checks if the NBT contains the "Potion" string
                        assert nbtCompoundI != null;
                        if(nbtCompoundI.contains("Potion")){
                            //Save the potion type
                            Item PotionI = Registries.ITEM.get(new Identifier(nbtCompoundI.getString("Potion")));
                            //Saves the count of empty bottles
                            int countI = player.getInventory().getStack(i).getCount();

                            //Erases the slot
                            player.getInventory().getStack(i).decrement(player.getInventory().getStack(i).getCount());
                            //Put the potion in the slot
                            player.getInventory().setStack(i,new ItemStack(PotionI, countI));

                            //Increases in 1
                            loopP=loopP-1;

                            //Put the refilled boolean in true
                            refilled=true;

                            //If it has already filled the slots, it stops
                            if(loopP < 1){
                                //Decrements the alcohol in hand
                                player.getMainHandStack().decrement(1);
                                //Play a sound
                                world.playSound(null, pos, TCOTS_Sounds.POTION_REFILLED, SoundCategory.BLOCKS, 3.0f, 1.0f);
                                //Success
                                return ActionResult.SUCCESS;
                            }
                        }
                    }

                    //If it doesn't fulfill all the maximum potions
                    if(i == player.getInventory().size()-1 && refilled){
                        //Decrements the alcohol in hand
                        player.getMainHandStack().decrement(1);
                        //Play a sound
                        world.playSound(null, pos, TCOTS_Sounds.POTION_REFILLED, SoundCategory.BLOCKS, 3.0f, 1.0f);
                        //Success
                        return ActionResult.SUCCESS;
                    }
                }
            }


            //For the Alchemy Book
            if(world.getBlockEntity(pos) != null && world.getBlockEntity(pos) instanceof AlchemyTableBlockEntity){
                //If the player has an alchemy book in hand
                if(player.getMainHandStack().getItem() instanceof AlchemyBookItem &&
                        !(world.getBlockState(pos).get(HAS_ALCHEMY_BOOK)) && !(player.isSneaking())) {

                    player.getMainHandStack().decrement(1);

                    world.playSound(null, pos, SoundEvents.ITEM_BOOK_PUT, SoundCategory.BLOCKS, 1.0f, 1.0f);

                    world.setBlockState(pos, state.with(HAS_ALCHEMY_BOOK, true));

                    return ActionResult.SUCCESS;
                }

                //If the table has already a book
                if(world.getBlockState(pos).get(HAS_ALCHEMY_BOOK)
                        && player.getMainHandStack().isEmpty() && player.isSneaking()){

                    ItemStack book = new ItemStack(TCOTS_Items.ALCHEMY_BOOK,1);

                    player.getInventory().insertStack(book);
                    world.setBlockState(pos, state.with(HAS_ALCHEMY_BOOK, false));

                    return ActionResult.SUCCESS;
                }
            }


            //Screen opener
            NamedScreenHandlerFactory screenHandlerFactory = ((AlchemyTableBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : AlchemyTableBlock.validateTicker(type, TCOTS_Blocks.ALCHEMY_TABLE_ENTITY, AlchemyTableBlockEntity::tick);
    }

}

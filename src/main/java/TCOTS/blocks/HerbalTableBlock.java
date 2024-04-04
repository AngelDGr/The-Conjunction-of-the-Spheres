package TCOTS.blocks;

import TCOTS.blocks.entity.HerbalTableBlockEntity;
import TCOTS.items.OrganicPasteItem;
import TCOTS.items.TCOTS_Items;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
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

import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class HerbalTableBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final MapCodec<HerbalTableBlock> CODEC = HerbalTableBlock.createCodec(HerbalTableBlock::new);

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    protected HerbalTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient){
            ItemStack mainStack = player.getMainHandStack();
            Item mainItem = mainStack.getItem();
            ItemStack organicPaste;

            if (mainItem instanceof BlockItem){
                Block mainBlock = ((BlockItem) mainItem).getBlock();
                if(mainBlock instanceof PlantBlock || mainBlock instanceof LeavesBlock || mainBlock instanceof AbstractPlantStemBlock
                        || mainBlock instanceof BigDripleafBlock || mainBlock instanceof CocoaBlock || mainBlock instanceof CarvedPumpkinBlock

                        || mainBlock.getDefaultState().isIn(BlockTags.WART_BLOCKS)
                ){

                    organicPaste = new ItemStack(TCOTS_Items.ORGANIC_PASTE,1);
                    if(!player.isCreative()){
                        mainStack.decrement(1);
                    }
                    world.playSound(null, pos, SoundEvents.BLOCK_SLIME_BLOCK_BREAK, SoundCategory.BLOCKS, 3.0f, 1.0f);

                    //Leaves = Luck
                    if(mainBlock instanceof LeavesBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, Collections.singletonList((new SuspiciousStewIngredient.StewEffect(StatusEffects.LUCK, 1600))));
                    }

                    //Mushroom = Poison & Nausea
                    if(mainBlock instanceof MushroomPlantBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, List.of((new SuspiciousStewIngredient.StewEffect(StatusEffects.POISON, 1600)), (new SuspiciousStewIngredient.StewEffect(StatusEffects.NAUSEA, 1600)) ));
                    }

                    //Pickles/LilyPad = Water Breathing
                    if(mainBlock instanceof SeaPickleBlock || mainBlock instanceof LilyPadBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, List.of((new SuspiciousStewIngredient.StewEffect(StatusEffects.WATER_BREATHING, 1600)), (new SuspiciousStewIngredient.StewEffect(StatusEffects.DOLPHINS_GRACE, 1600)) ));
                    }

                    //WartBlocks = Fire Resistance/Resistance
                    if(mainBlock.getDefaultState().isIn(BlockTags.WART_BLOCKS)){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, List.of((new SuspiciousStewIngredient.StewEffect(StatusEffects.FIRE_RESISTANCE, 1600)), (new SuspiciousStewIngredient.StewEffect(StatusEffects.RESISTANCE, 1600)) ));
                    }

                    //WartBlocks = Fire Resistance/Resistance
                    if(mainBlock instanceof FungusBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, List.of((new SuspiciousStewIngredient.StewEffect(StatusEffects.FIRE_RESISTANCE, 1600)) ));
                    }

                    //Glow Berries = Glowing
                    if(mainBlock instanceof CaveVinesHeadBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, List.of((new SuspiciousStewIngredient.StewEffect(StatusEffects.GLOWING, 1600))));
                    }

                    //Drip leaf =  SlowFalling/Jump boost
                    if(mainBlock instanceof BigDripleafBlock || mainBlock instanceof BigDripleafStemBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, List.of((new SuspiciousStewIngredient.StewEffect(StatusEffects.SLOW_FALLING, 1600)), (new SuspiciousStewIngredient.StewEffect(StatusEffects.JUMP_BOOST, 1600)) ));
                    }

                    //Cocoa = Speed
                    if(mainBlock instanceof CocoaBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, List.of((new SuspiciousStewIngredient.StewEffect(StatusEffects.SPEED, 1600))));
                    }

                    //Flowers = Stew Effects
                    if(mainBlock instanceof FlowerBlock){
                        OrganicPasteItem.writeEffectsToPaste(organicPaste, ((FlowerBlock) mainBlock).getStewEffects());
                    }

                    //If the player inventory it's full
                    if(player.getInventory().getEmptySlot() == -1){
                        player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), organicPaste));
                    } else{
                        player.getInventory().insertStack(organicPaste);
                    }

                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.CONSUME_PARTIAL;
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
        tooltip.add(Text.translatable("block.tcots-witcher.herbal_table.tooltip").formatted(Formatting.GRAY));
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
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new HerbalTableBlockEntity(pos, state);
    }
}

package TCOTS.blocks;

import TCOTS.blocks.entity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class TCOTS_Blocks {
    //Skeleton Body
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final IntegerProperty SHAPE = IntegerProperty.create("shape", 0, 5);
    public static final BooleanProperty HAS_ARMOR = BooleanProperty.create("armor");
    public static final BooleanProperty HIDE_HEAD = BooleanProperty.create("hide_head");
    //Skull
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    public static final Block WINTERS_BLADE_SKELETON = new WintersBladeSkeletonBlock();
    public static final Block SKELETON_BLOCK = new SkeletonBlock();
    public static BlockEntityType<NestSkullBlockEntity> SKULL_NEST_ENTITY;

    public static BlockEntityType<HerbalTableBlockEntity> HERBAL_TABLE_ENTITY;
    public static BlockEntityType<GiantAnchorBlockEntity> GIANT_ANCHOR_ENTITY;
    public static BlockEntityType<WintersBladeSkeletonBlockEntity> WINTERS_BLADE_SKELETON_ENTITY;
    public static BlockEntityType<SkeletonBlockEntity> SKELETON_BLOCK_ENTITY;
}

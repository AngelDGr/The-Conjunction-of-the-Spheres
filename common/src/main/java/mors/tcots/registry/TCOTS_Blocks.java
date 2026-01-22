package mors.tcots.registry;

import mors.tcots.TCOTS_Registries;
import mors.tcots.block.*;
import mors.tcots.block.entity.*;
import mors.tcots.block.plants.*;
import mors.tcots.block.skull.NestSkullBlock;
import mors.tcots.block.skull.NestWallSkullBlock;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class TCOTS_Blocks {
    //Blocks
    public static  final Supplier<Block> ARENARIA_BUSH = TCOTS_Registries.BLOCKS.register("arenaria_bush",
            ()-> new ArenariaBush(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> CELANDINE_PLANT =TCOTS_Registries.BLOCKS.register("celandine_plant",
            ()->new CelandinePlant(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> POTTED_CELANDINE_FLOWER = TCOTS_Registries.BLOCKS.register("potted_celandine_flower",
            () -> flowerPot(CELANDINE_PLANT.get()));

    public static  final Supplier<Block> CROWS_EYE_FERN = TCOTS_Registries.BLOCKS.register("crows_eye_fern",
            () -> new CrowsEyeFern(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static  final Supplier<Block> VERBENA_FLOWER = TCOTS_Registries.BLOCKS.register("verbena_flower",
            () -> new VerbenaFlower(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static  final Supplier<Block> POTTED_VERBENA_FLOWER = TCOTS_Registries.BLOCKS.register("potted_verbena_flower",
            () -> flowerPot(VERBENA_FLOWER.get()));

    public static  final Supplier<Block> BRYONIA_VINE =TCOTS_Registries.BLOCKS.register("bryonia_vine",
            ()->new BryoniaVine(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> POTTED_BRYONIA_FLOWER = TCOTS_Registries.BLOCKS.register("potted_bryonia_flower",
            () -> flowerPot(BRYONIA_VINE.get()));

    public static  final Supplier<Block> HAN_FIBER_PLANT =TCOTS_Registries.BLOCKS.register("han_fiber_plant",
            ()->new HanFiberPlant(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static  final Supplier<Block> POTTED_HAN_FIBER = TCOTS_Registries.BLOCKS.register("potted_han_fiber",
            () -> flowerPot(HAN_FIBER_PLANT.get()));

    public static  final Supplier<Block> PUFFBALL_MUSHROOM =TCOTS_Registries.BLOCKS.register("puffball_mushroom",
            ()->new PuffballMushroomPlant(TCOTS_WorldGen.HUGE_PUFFBALL_MUSHROOM_CF, BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel(state -> 1).hasPostProcess(((arg, arg2, arg3) -> true)).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> POTTED_PUFFBALL_MUSHROOM = TCOTS_Registries.BLOCKS.register("potted_puffball_mushroom",
            () -> flowerPot(PUFFBALL_MUSHROOM.get()));

    public static  final Supplier<Block> PUFFBALL_MUSHROOM_BLOCK = TCOTS_Registries.BLOCKS.register("puffball_mushroom_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD).ignitedByLava()));

    public static  final Supplier<Block> SEWANT_MUSHROOMS_PLANT = TCOTS_Registries.BLOCKS.register("sewant_mushrooms_plant",
            () -> new SewantMushroomsPlant(TCOTS_WorldGen.HUGE_SEWANT_MUSHROOMS_CF, BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel(state -> 1).hasPostProcess((arg, arg2, arg3) -> true).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> POTTED_SEWANT_MUSHROOMS = TCOTS_Registries.BLOCKS.register("potted_sewant_mushrooms",
            () -> flowerPot(SEWANT_MUSHROOMS_PLANT.get()));

    public static  final Supplier<Block> SEWANT_MUSHROOM_BLOCK = TCOTS_Registries.BLOCKS.register("sewant_mushroom_block",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD).ignitedByLava()));

    public static  final Supplier<Block> SEWANT_MUSHROOM_STEM = TCOTS_Registries.BLOCKS.register("sewant_mushroom_stem",
            () -> new HugeMushroomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD).ignitedByLava()));

    public static  final Supplier<Block> WINTERS_BLADE_SKELETON =TCOTS_Registries.BLOCKS.register("winters_blade_skeleton",
            WintersBladeSkeletonBlock::new);

    public static  final Supplier<Block> SKELETON_BLOCK =TCOTS_Registries.BLOCKS.register("skeleton_block",
            SkeletonBlock::new);

    public static  final Supplier<Block> FROSTED_SNOW  = TCOTS_Registries.BLOCKS.register("frosted_snow",
            ()->new FrostedSnowBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).replaceable().forceSolidOff().randomTicks().strength(0.1f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion().noCollission().friction(0.98f).pushReaction(PushReaction.DESTROY)));

    public static  final Supplier<Block> NEST_SLAB  = TCOTS_Registries.BLOCKS.register("nest_slab",
            ()->new SlabBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.GRAVEL).pushReaction(PushReaction.DESTROY).mapColor(MapColor.DIRT)));

    public static  final Supplier<Block> NEST_SKULL  =TCOTS_Registries.BLOCKS.register("nest_skull",
            ()->new NestSkullBlock(BlockBehaviour.Properties.of().strength(0.4f).sound(SoundType.BONE_BLOCK).instrument(NoteBlockInstrument.SKELETON).pushReaction(PushReaction.DESTROY)));
    public static  final Supplier<Block> NEST_WALL_SKULL =TCOTS_Registries.BLOCKS.register("nest_wall_skull",
            ()->new NestWallSkullBlock(BlockBehaviour.Properties.of().strength(0.4f).sound(SoundType.BONE_BLOCK).instrument(NoteBlockInstrument.SKELETON).pushReaction(PushReaction.DESTROY).dropsLike(NEST_SKULL.get())));

    public static  final Supplier<Block> MONSTER_NEST  =TCOTS_Registries.BLOCKS.register("monster_nest",
            ()->new MonsterNestBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.GRAVEL).mapColor(MapColor.DIRT)));

    public static  final Supplier<Block> ALCHEMY_TABLE  = TCOTS_Registries.BLOCKS.register("alchemy_table",
            ()->new AlchemyTableBlock(BlockBehaviour.Properties.of().strength(2.5F).sound(SoundType.WOOD).mapColor(MapColor.PLANT).noOcclusion()));

    public static  final RegistrySupplier<Block> HERBAL_TABLE  = TCOTS_Registries.BLOCKS.register("herbal_table",
            ()->new HerbalTableBlock(BlockBehaviour.Properties.of().strength(2.5F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_CYAN).ignitedByLava().noOcclusion()));

    public static  final Supplier<Block> GIANT_ANCHOR =TCOTS_Registries.BLOCKS.register("giant_anchor",
            ()->new GiantAnchorBlock(BlockBehaviour.Properties.of().strength(8.0f, 600.0f).sound(SoundType.ANVIL).mapColor(MapColor.COLOR_BROWN).ignitedByLava().noOcclusion()));

    public static Block flowerPot(final Block arg) {
        return new FlowerPotBlock(arg, BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));
    }

    //BlockEntities
    public static RegistrySupplier<BlockEntityType<NestSkullBlockEntity>> SKULL_NEST_ENTITY;
    public static RegistrySupplier<BlockEntityType<HerbalTableBlockEntity>> HERBAL_TABLE_ENTITY;
    public static RegistrySupplier<BlockEntityType<GiantAnchorBlockEntity>> GIANT_ANCHOR_ENTITY;
    public static RegistrySupplier<BlockEntityType<WintersBladeSkeletonBlockEntity>> WINTERS_BLADE_SKELETON_ENTITY;
    public static RegistrySupplier<BlockEntityType<SkeletonBlockEntity>> SKELETON_BLOCK_ENTITY;
    public static RegistrySupplier<BlockEntityType<MonsterNestBlockEntity>> MONSTER_NEST_ENTITY;
    public static RegistrySupplier<BlockEntityType<? extends AlchemyTableBlockEntity>> ALCHEMY_TABLE_ENTITY;

    public static void initBlocks(){

    }

    public static void initBlockEntities(){
        //Block Entities
        {
            SKULL_NEST_ENTITY = TCOTS_Registries.BLOCK_ENTITY_TYPES.register(
                    "nest_skull",
                    ()-> BlockEntityType.Builder.of(
                            (pos, state) -> new NestSkullBlockEntity(SKULL_NEST_ENTITY.get(), pos, state),
                            NEST_SKULL.get(), NEST_WALL_SKULL.get()).build(null));

            MONSTER_NEST_ENTITY = TCOTS_Registries.BLOCK_ENTITY_TYPES.register(
                    "monster_nest",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new MonsterNestBlockEntity(MONSTER_NEST_ENTITY.get(), pos, state),
                            MONSTER_NEST.get()).build(null));

            ALCHEMY_TABLE_ENTITY = TCOTS_Registries.BLOCK_ENTITY_TYPES.register(
                    "alchemy_table",
                    TCOTS_Blocks::createAlchemyTableEntity);

            HERBAL_TABLE_ENTITY = TCOTS_Registries.BLOCK_ENTITY_TYPES.register(
                    "herbal_table",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new HerbalTableBlockEntity(HERBAL_TABLE_ENTITY.get(), pos, state),
                            HERBAL_TABLE.get()).build(null));

            GIANT_ANCHOR_ENTITY = TCOTS_Registries.BLOCK_ENTITY_TYPES.register(
                    "giant_anchor",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new GiantAnchorBlockEntity(GIANT_ANCHOR_ENTITY.get(), pos, state),
                            GIANT_ANCHOR.get()).build(null));

            WINTERS_BLADE_SKELETON_ENTITY = TCOTS_Registries.BLOCK_ENTITY_TYPES.register(
                    "winters_blade_skeleton",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new WintersBladeSkeletonBlockEntity(WINTERS_BLADE_SKELETON_ENTITY.get(), pos, state),
                            WINTERS_BLADE_SKELETON.get()).build(null));

            SKELETON_BLOCK_ENTITY = TCOTS_Registries.BLOCK_ENTITY_TYPES.register(
                    "skeleton_block",
                    () -> BlockEntityType.Builder.of(
                            (pos, state) -> new SkeletonBlockEntity(SKELETON_BLOCK_ENTITY.get(), pos, state),
                            SKELETON_BLOCK.get()).build(null));
        }
    }

    public static BlockEntityType<? extends AlchemyTableBlockEntity> createAlchemyTableEntity(){
        return BlockEntityType.Builder.of(
                (pos, state) -> new AlchemyTableBlockEntity(TCOTS_Blocks.ALCHEMY_TABLE_ENTITY.get(), pos, state),
                TCOTS_Blocks.ALCHEMY_TABLE.get()).build(null);
    }

    public static Block ArenariaBush() { return ARENARIA_BUSH.get(); }
    public static Block CelandinePlant() { return CELANDINE_PLANT.get(); }
    public static Block PottedCelandineFlower() { return POTTED_CELANDINE_FLOWER.get(); }
    public static Block CrowsEyeFern() { return CROWS_EYE_FERN.get(); }
    public static Block VerbenaFlower() { return VERBENA_FLOWER.get(); }
    public static Block PottedVerbenaFlower() { return POTTED_VERBENA_FLOWER.get(); }
    public static Block BryoniaVine() { return BRYONIA_VINE.get(); }
    public static Block PottedBryoniaFlower() { return POTTED_BRYONIA_FLOWER.get(); }
    public static Block HanFiberPlant() { return HAN_FIBER_PLANT.get(); }
    public static Block PottedHanFiber() { return POTTED_HAN_FIBER.get(); }
    public static Block PuffballMushroom() { return PUFFBALL_MUSHROOM.get(); }
    public static Block PottedPuffballMushroom() { return POTTED_PUFFBALL_MUSHROOM.get(); }
    public static Block PuffballMushroomBlock() { return PUFFBALL_MUSHROOM_BLOCK.get(); }
    public static Block SewantMushroomsPlant() { return SEWANT_MUSHROOMS_PLANT.get(); }
    public static Block PottedSewantMushrooms() { return POTTED_SEWANT_MUSHROOMS.get(); }
    public static Block SewantMushroomBlock() { return SEWANT_MUSHROOM_BLOCK.get(); }
    public static Block SewantMushroomStem() { return SEWANT_MUSHROOM_STEM.get(); }

    public static Block WintersBladeSkeleton() { return WINTERS_BLADE_SKELETON.get(); }
    public static Block SkeletonBlock() { return SKELETON_BLOCK.get(); }
    public static Block FrostedSnow() { return FROSTED_SNOW.get(); }
    public static Block NestSlab() { return NEST_SLAB.get(); }
    public static Block NestSkull() { return NEST_SKULL.get(); }
    public static Block NestWallSkull() { return NEST_WALL_SKULL.get(); }
    public static Block MonsterNest() { return MONSTER_NEST.get(); }
    public static Block AlchemyTable() { return ALCHEMY_TABLE.get(); }
    public static Block HerbalTable() { return HERBAL_TABLE.get(); }
    public static Block GiantAnchor() { return GIANT_ANCHOR.get(); }

    // BlockEntities
    public static BlockEntityType<NestSkullBlockEntity> SkullNestEntity() { return SKULL_NEST_ENTITY.get(); }
    public static BlockEntityType<HerbalTableBlockEntity> HerbalTableEntity() { return HERBAL_TABLE_ENTITY.get(); }
    public static BlockEntityType<GiantAnchorBlockEntity> GiantAnchorEntity() { return GIANT_ANCHOR_ENTITY.get(); }
    public static BlockEntityType<WintersBladeSkeletonBlockEntity> WintersBladeSkeletonBlockEntity() { return WINTERS_BLADE_SKELETON_ENTITY.get(); }
    public static BlockEntityType<SkeletonBlockEntity> SkeletonBlockEntity() { return SKELETON_BLOCK_ENTITY.get(); }
    public static BlockEntityType<MonsterNestBlockEntity> MonsterNestBlockEntity() { return MONSTER_NEST_ENTITY.get(); }
    public static BlockEntityType<? extends AlchemyTableBlockEntity> AlchemyTableBlockEntity() { return ALCHEMY_TABLE_ENTITY.get(); }

}

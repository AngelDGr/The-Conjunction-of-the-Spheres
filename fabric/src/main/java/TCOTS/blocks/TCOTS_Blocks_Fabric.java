package TCOTS.blocks;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.*;
import TCOTS.blocks.plants.*;
import TCOTS.blocks.skull.NestSkullBlock;
import TCOTS.blocks.skull.NestWallSkullBlock;
import TCOTS.world.TCOTS_ConfiguredFeatures;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

@SuppressWarnings("deprecation")
public class TCOTS_Blocks_Fabric {
    public static final Block ARENARIA_BUSH = new ArenariaBush(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY));
    public static final Block CELANDINE_PLANT = new CelandinePlant(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY));
    public static final Block CROWS_EYE_FERN = new CrowsEyeFern(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final Block VERBENA_FLOWER = new VerbenaFlower(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final Block BRYONIA_VINE = new BryoniaVine(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY));
    public static final Block HAN_FIBER_PLANT = new HanFiberPlant(BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.FLOWERING_AZALEA).pushReaction(PushReaction.DESTROY).offsetType(BlockBehaviour.OffsetType.XZ));
    public static final Block PUFFBALL_MUSHROOM = new PuffballMushroom(TCOTS_ConfiguredFeatures.HUGE_PUFFBALL_MUSHROOM, BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel(state -> 1).hasPostProcess(Blocks::always).pushReaction(PushReaction.DESTROY));
    public static final Block PUFFBALL_MUSHROOM_BLOCK = new HugeMushroomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD).ignitedByLava());
    public static final Block SEWANT_MUSHROOMS_PLANT = new SewantMushroomsPlant(TCOTS_ConfiguredFeatures.HUGE_SEWANT_MUSHROOMS, BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).lightLevel(state -> 1).hasPostProcess(Blocks::always).pushReaction(PushReaction.DESTROY));
    public static final Block SEWANT_MUSHROOM_BLOCK = new HugeMushroomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD).ignitedByLava());
    public static final Block SEWANT_MUSHROOM_STEM = new HugeMushroomBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD).ignitedByLava());

    //Potted
    public static final Block POTTED_VERBENA_FLOWER = Blocks.flowerPot(VERBENA_FLOWER);
    public static final Block POTTED_CELANDINE_FLOWER = Blocks.flowerPot(CELANDINE_PLANT);
    public static final Block POTTED_HAN_FIBER = Blocks.flowerPot(HAN_FIBER_PLANT);
    public static final Block POTTED_PUFFBALL_MUSHROOM = Blocks.flowerPot(PUFFBALL_MUSHROOM);
    public static final Block POTTED_SEWANT_MUSHROOMS = Blocks.flowerPot(SEWANT_MUSHROOMS_PLANT);
    public static final Block POTTED_BRYONIA_FLOWER = Blocks.flowerPot(BRYONIA_VINE);

    public static final Block FROSTED_SNOW  = new FrostedSnowBlock(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).replaceable()
            .forceSolidOff().randomTicks().strength(0.1f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion().noCollission().friction(0.98f)
            .pushReaction(PushReaction.DESTROY));
    public static final Block NEST_SLAB  = new SlabBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.GRAVEL).pushReaction(PushReaction.DESTROY).mapColor(MapColor.DIRT));
    public static final Block NEST_SKULL  = new NestSkullBlock(BlockBehaviour.Properties.of().strength(0.4f).sound(SoundType.BONE_BLOCK).instrument(NoteBlockInstrument.SKELETON).pushReaction(PushReaction.DESTROY));
    public static final Block NEST_WALL_SKULL = new NestWallSkullBlock(BlockBehaviour.Properties.of().strength(0.4f).sound(SoundType.BONE_BLOCK).instrument(NoteBlockInstrument.SKELETON).pushReaction(PushReaction.DESTROY).dropsLike(NEST_SKULL));
    public static final Block MONSTER_NEST  = new MonsterNestBlock(BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.GRAVEL).mapColor(MapColor.DIRT));
    public static final Block ALCHEMY_TABLE  = new AlchemyTableBlock(BlockBehaviour.Properties.of().strength(2.5F).sound(SoundType.WOOD).mapColor(MapColor.PLANT).noOcclusion());
    public static final Block HERBAL_TABLE  = new HerbalTableBlock(BlockBehaviour.Properties.of().strength(2.5F).sound(SoundType.WOOD).mapColor(MapColor.COLOR_CYAN).ignitedByLava().noOcclusion());

    public static final Block GIANT_ANCHOR = new GiantAnchorBlock(
            BlockBehaviour.Properties.of().strength(8.0f, 600.0f).sound(SoundType.ANVIL).mapColor(MapColor.COLOR_BROWN).ignitedByLava().noOcclusion());

    public static BlockEntityType<MonsterNestBlockEntity> MONSTER_NEST_ENTITY;
    public static BlockEntityType<AlchemyTableBlockEntity> ALCHEMY_TABLE_ENTITY;

    public static void registerBlocks(){

        //Blocks
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "frosted_snow"), FROSTED_SNOW);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nest_slab"), NEST_SLAB);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nest_skull"), NEST_SKULL);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nest_wall_skull"), NEST_WALL_SKULL);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "monster_nest"), MONSTER_NEST);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_table"), ALCHEMY_TABLE);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "herbal_table"), HERBAL_TABLE);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "giant_anchor"), GIANT_ANCHOR);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade_skeleton"), TCOTS_Blocks.WINTERS_BLADE_SKELETON);
        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "skeleton_block"), TCOTS_Blocks.SKELETON_BLOCK);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "celandine_plant"), CELANDINE_PLANT);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "arenaria_bush"), ARENARIA_BUSH);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "crows_eye_fern"), CROWS_EYE_FERN);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bryonia_vine"), BRYONIA_VINE);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "verbena_flower"), VERBENA_FLOWER);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "han_fiber_plant"), HAN_FIBER_PLANT);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "puffball_mushroom"), PUFFBALL_MUSHROOM);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "puffball_mushroom_block"), PUFFBALL_MUSHROOM_BLOCK);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "sewant_mushrooms_plant"), SEWANT_MUSHROOMS_PLANT);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "sewant_mushroom_block"), SEWANT_MUSHROOM_BLOCK);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "sewant_mushroom_stem"), SEWANT_MUSHROOM_STEM);


        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "potted_verbena_flower"), POTTED_VERBENA_FLOWER);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "potted_celandine_flower"), POTTED_CELANDINE_FLOWER);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "potted_han_fiber"), POTTED_HAN_FIBER);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "potted_bryonia_flower"), POTTED_BRYONIA_FLOWER);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "potted_puffball_mushroom"), POTTED_PUFFBALL_MUSHROOM);

        Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "potted_sewant_mushrooms"), POTTED_SEWANT_MUSHROOMS);


        //Block Entity
        TCOTS_Blocks.SKULL_NEST_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nest_skull"),
                FabricBlockEntityTypeBuilder.create(
                        (pos, state) -> new NestSkullBlockEntity(TCOTS_Blocks.SKULL_NEST_ENTITY, pos, state),
                        NEST_SKULL, NEST_WALL_SKULL).build());

        TCOTS_Blocks_Fabric.MONSTER_NEST_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "monster_nest"),
                FabricBlockEntityTypeBuilder.create(
                        (pos, state) -> new MonsterNestBlockEntity(TCOTS_Blocks_Fabric.MONSTER_NEST_ENTITY, pos, state),
                        MONSTER_NEST).build());

        TCOTS_Blocks_Fabric.ALCHEMY_TABLE_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_table"),
                FabricBlockEntityTypeBuilder.create(
                        (pos, state) -> new AlchemyTableBlockEntity(TCOTS_Blocks_Fabric.ALCHEMY_TABLE_ENTITY, pos, state),
                        ALCHEMY_TABLE).build());

        TCOTS_Blocks.HERBAL_TABLE_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "herbal_table"),
                FabricBlockEntityTypeBuilder.create(
                        (pos, state)-> new HerbalTableBlockEntity(TCOTS_Blocks.HERBAL_TABLE_ENTITY, pos, state),
                        HERBAL_TABLE).build());

        TCOTS_Blocks.GIANT_ANCHOR_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "giant_anchor"),
                FabricBlockEntityTypeBuilder.create(
                        (pos, block)-> new GiantAnchorBlockEntity(TCOTS_Blocks.GIANT_ANCHOR_ENTITY, pos, block),
                        GIANT_ANCHOR).build());

        TCOTS_Blocks.WINTERS_BLADE_SKELETON_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade_skeleton"),
                BlockEntityType.Builder.of(
                        (pos, state)-> new WintersBladeSkeletonBlockEntity(TCOTS_Blocks.WINTERS_BLADE_SKELETON_ENTITY, pos, state),
                        TCOTS_Blocks.WINTERS_BLADE_SKELETON).build());

        TCOTS_Blocks.SKELETON_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "skeleton_block"),
                BlockEntityType.Builder.of(
                        (pos, state)-> new SkeletonBlockEntity(TCOTS_Blocks.SKELETON_BLOCK_ENTITY, pos, state),
                        TCOTS_Blocks.SKELETON_BLOCK).build());
    }
}

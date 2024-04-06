package TCOTS.blocks;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.blocks.entity.HerbalTableBlockEntity;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.plants.*;
import TCOTS.blocks.skull.NestSkullBlock;
import TCOTS.blocks.skull.NestWallSkullBlock;
import TCOTS.world.TCOTS_ConfiguredFeatures;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class TCOTS_Blocks {

    public static final Block ARENARIA_BUSH = new ArenariaBush(FabricBlockSettings.create().nonOpaque().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().sounds(BlockSoundGroup.SWEET_BERRY_BUSH).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block CELANDINE_PLANT = new CelandinePlant(FabricBlockSettings.create().nonOpaque().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block CROWS_EYE_FERN = new CrowsEyeFern(FabricBlockSettings.create().nonOpaque().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA).pistonBehavior(PistonBehavior.DESTROY).offset(AbstractBlock.OffsetType.XZ));
    public static final Block VERBENA_FLOWER = new VerbenaFlower(FabricBlockSettings.create().nonOpaque().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA).pistonBehavior(PistonBehavior.DESTROY).offset(AbstractBlock.OffsetType.XZ));
    public static final Block BRYONIA_VINE = new BryoniaVine(FabricBlockSettings.create().nonOpaque().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block HAN_FIBER_PLANT = new HanFiberPlant(FabricBlockSettings.create().nonOpaque().mapColor(MapColor.DARK_GREEN).ticksRandomly().noCollision().sounds(BlockSoundGroup.FLOWERING_AZALEA).pistonBehavior(PistonBehavior.DESTROY).offset(AbstractBlock.OffsetType.XZ));
    public static final Block PUFFBALL_MUSHROOM = new PuffballMushroom(TCOTS_ConfiguredFeatures.HUGE_PUFFBALL_MUSHROOM, AbstractBlock.Settings.create().mapColor(MapColor.WHITE).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS).luminance(state -> 1).postProcess(Blocks::always).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block PUFFBALL_MUSHROOM_BLOCK = new MushroomBlock(AbstractBlock.Settings.create().mapColor(MapColor.WHITE_GRAY).instrument(Instrument.BASS).strength(0.2f).sounds(BlockSoundGroup.WOOD).burnable());


    //Potted
    public static final Block POTTED_VERBENA_FLOWER = Blocks.createFlowerPotBlock(VERBENA_FLOWER);
    public static final Block POTTED_CELANDINE_FLOWER = Blocks.createFlowerPotBlock(CELANDINE_PLANT);
    public static final Block POTTED_HAN_FIBER = Blocks.createFlowerPotBlock(HAN_FIBER_PLANT);
    public static final Block POTTED_PUFFBALL_MUSHROOM = Blocks.createFlowerPotBlock(PUFFBALL_MUSHROOM);
    public static final Block POTTED_BRYONIA_FLOWER = Blocks.createFlowerPotBlock(BRYONIA_VINE);


    public static final Block NEST_SLAB  = new SlabBlock(FabricBlockSettings.create().strength(1.0f).sounds(BlockSoundGroup.GRAVEL).pistonBehavior(PistonBehavior.DESTROY).mapColor(MapColor.DIRT_BROWN));
    public static final Block NEST_SKULL  = new NestSkullBlock(FabricBlockSettings.create().strength(0.4f).sounds(BlockSoundGroup.BONE).instrument(Instrument.SKELETON).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block NEST_WALL_SKULL = new NestWallSkullBlock(FabricBlockSettings.create().strength(0.4f).sounds(BlockSoundGroup.BONE).instrument(Instrument.SKELETON).pistonBehavior(PistonBehavior.DESTROY).dropsLike(NEST_SKULL));
    public static final Block MONSTER_NEST  = new MonsterNestBlock(FabricBlockSettings.create().strength(1.0f).sounds(BlockSoundGroup.GRAVEL).mapColor(MapColor.DIRT_BROWN));
    public static final Block ALCHEMY_TABLE  = new AlchemyTableBlock(FabricBlockSettings.create().strength(1.0f).sounds(BlockSoundGroup.WOOD).mapColor(MapColor.DARK_GREEN));
    public static final Block HERBAL_TABLE  = new HerbalTableBlock(FabricBlockSettings.create().strength(1.0f).sounds(BlockSoundGroup.WOOD).mapColor(MapColor.CYAN).burnable());

    public static BlockEntityType<NestSkullBlockEntity> SKULL_NEST_ENTITY;
    public static BlockEntityType<MonsterNestBlockEntity> MONSTER_NEST_ENTITY;
    public static BlockEntityType<AlchemyTableBlockEntity> ALCHEMY_TABLE_ENTITY;
    public static BlockEntityType<HerbalTableBlockEntity> HERBAL_TABLE_ENTITY;



    public static void registerBlocks(){

        //Blocks
        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_slab"), NEST_SLAB);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_skull"), NEST_SKULL);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_wall_skull"), NEST_WALL_SKULL);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "monster_nest"), MONSTER_NEST);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "alchemy_table"), ALCHEMY_TABLE);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "herbal_table"), HERBAL_TABLE);


        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "celandine_plant"), CELANDINE_PLANT);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "arenaria_bush"), ARENARIA_BUSH);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "crows_eye_fern"), CROWS_EYE_FERN);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "bryonia_vine"), BRYONIA_VINE);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "verbena_flower"), VERBENA_FLOWER);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "han_fiber_plant"), HAN_FIBER_PLANT);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "puffball_mushroom"), PUFFBALL_MUSHROOM);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "puffball_mushroom_block"), PUFFBALL_MUSHROOM_BLOCK);


        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "potted_verbena_flower"), POTTED_VERBENA_FLOWER);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "potted_celandine_flower"), POTTED_CELANDINE_FLOWER);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "potted_han_fiber"), POTTED_HAN_FIBER);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "potted_bryonia_flower"), POTTED_BRYONIA_FLOWER);


        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "potted_puffball_mushroom"), POTTED_PUFFBALL_MUSHROOM);


        //Block Entity
        SKULL_NEST_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TCOTS_Main.MOD_ID, "nest_skull"),
                FabricBlockEntityTypeBuilder.create(NestSkullBlockEntity::new, NEST_SKULL, NEST_WALL_SKULL).build());

        MONSTER_NEST_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TCOTS_Main.MOD_ID, "monster_nest"),
                FabricBlockEntityTypeBuilder.create(MonsterNestBlockEntity::new, MONSTER_NEST).build());

        ALCHEMY_TABLE_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TCOTS_Main.MOD_ID, "alchemy_table"),
                FabricBlockEntityTypeBuilder.create(AlchemyTableBlockEntity::new, ALCHEMY_TABLE).build());

        HERBAL_TABLE_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TCOTS_Main.MOD_ID, "herbal_table"),
                FabricBlockEntityTypeBuilder.create(HerbalTableBlockEntity::new, HERBAL_TABLE).build());
    }
}

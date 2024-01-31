package TCOTS.blocks;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.blocks.skull.NestSkullBlock;
import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.skull.NestWallSkullBlock;
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
    public static final Block NEST_SLAB  = new SlabBlock(FabricBlockSettings.create().strength(0.2f).sounds(BlockSoundGroup.GRAVEL).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block NEST_SKULL  = new NestSkullBlock(FabricBlockSettings.create().strength(0.2f).sounds(BlockSoundGroup.BONE).instrument(Instrument.SKELETON).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block NEST_WALL_SKULL = new NestWallSkullBlock(FabricBlockSettings.create().strength(0.2f).sounds(BlockSoundGroup.BONE).instrument(Instrument.SKELETON).dropsLike(NEST_SKULL).pistonBehavior(PistonBehavior.DESTROY));
    public static final Block MONSTER_NEST  = new MonsterNestBlock(FabricBlockSettings.create().strength(0.2f).sounds(BlockSoundGroup.GRAVEL));

    public static BlockEntityType<NestSkullBlockEntity> SKULL_NEST_ENTITY;

    public static BlockEntityType<MonsterNestBlockEntity> MONSTER_NEST_ENTITY;

    public static void registerBlocks(){
        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_slab"), NEST_SLAB);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_skull"), NEST_SKULL);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_wall_skull"), NEST_WALL_SKULL);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "monster_nest"), MONSTER_NEST);


        SKULL_NEST_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TCOTS_Main.MOD_ID, "nest_skull"),
                FabricBlockEntityTypeBuilder.create(NestSkullBlockEntity::new, NEST_SKULL, NEST_WALL_SKULL).build());

        MONSTER_NEST_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TCOTS_Main.MOD_ID, "monster_nest"),
                FabricBlockEntityTypeBuilder.create(MonsterNestBlockEntity::new, MONSTER_NEST).build());

    }
}

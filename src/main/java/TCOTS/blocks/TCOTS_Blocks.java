package TCOTS.blocks;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.skull.NestSkullBlock;
import TCOTS.blocks.skull.NestSkullBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class TCOTS_Blocks {

    public static final Block NEST_SLAB  = new SlabBlock(FabricBlockSettings.create().strength(0.2f).sounds(BlockSoundGroup.GRAVEL).pistonBehavior(PistonBehavior.DESTROY));

    public static final Block NEST_SKULL  = new NestSkullBlock(SkullBlock.Type.SKELETON, FabricBlockSettings.create().strength(0.2f).sounds(BlockSoundGroup.BONE).pistonBehavior(PistonBehavior.DESTROY));
//    public static final Block NEST_WALL_SKULL = Blocks.register("nest_wall_skull", new WallSkullBlock(SkullBlock.Type.SKELETON, FabricBlockSettings.create().strength(0.2f).dropsLike(NEST_SKULL).pistonBehavior(PistonBehavior.DESTROY)));

    public static BlockEntityType<SkullBlockEntity> SKULL;

    public static void registerBlocks(){
        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_slab"), NEST_SLAB);

        Registry.register(Registries.BLOCK, new Identifier(TCOTS_Main.MOD_ID, "nest_skull"), NEST_SKULL);

        SKULL = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(TCOTS_Main.MOD_ID, "skull"),
                FabricBlockEntityTypeBuilder.create(SkullBlockEntity::new, NEST_SKULL).build());

    }
}

package TCOTS.world.village;

import TCOTS.TCOTS_Main;
import TCOTS.world.TCOTS_ProcessorList;
import fzzyhmstrs.structurized_reborn.impl.FabricStructurePoolRegistry;
import net.minecraft.util.Identifier;

public class TCOTS_VillageAdditions {
    // Using https://github.com/fzzyhmstrs/structurized-reborn (Under MIT License)


    // Normal weight: 1-3
    public static void registerNewVillageStructures() {
        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/plains/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/plains/houses/herbalist_hut_1"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_PLAINS
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/plains/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/plains/houses/herbalist_hut_2"),
                3,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_PLAINS
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/taiga/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/taiga/houses/herbalist_taiga_hut_1"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_TAIGA
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/taiga/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/taiga/houses/herbalist_taiga_hut_2"),
                3,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_TAIGA
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/snowy/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/snowy/houses/herbalist_snowy_hut_1"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_SNOWY
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/snowy/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/snowy/houses/herbalist_snowy_hut_2"),
                3,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_SNOWY
        );


        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/desert/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/desert/houses/herbalist_desert_hut_1"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_DESERT
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/desert/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/desert/houses/herbalist_desert_hut_2"),
                3,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_DESERT
        );
    }

}

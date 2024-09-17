package TCOTS.world.village;

import TCOTS.TCOTS_Main;
import TCOTS.world.TCOTS_ProcessorList;
import fzzyhmstrs.structurized_reborn.impl.FabricStructurePoolRegistry;
import net.minecraft.util.Identifier;

public class TCOTS_VillageAdditions {
    // Using https://github.com/fzzyhmstrs/structurized-reborn (Under MIT License)

    // Normal weight: 1-3

    //Plains
    // hut_1 -> ^9 x11          ---> 5 [JIGSAW] <--- 6
    // hut_2 -> ^9  x10         ---> 4 [JIGSAW] <--- 5

    //Taiga
    // hut_1 -> ^10 x11        ---> 5 [JIGSAW] <--- 5
    // hut_2 -> ^11 x8         ---> 2 [JIGSAW] <--- 5

    //xSnowy
    // hut_1 -> ^9 x10         ---> 3 [JIGSAW] <--- 6
    // hut_2 -> ^6 x10         ---> 3 [JIGSAW] <--- 6

    //xDesert
    // hut_1 -> ^10 x9         ---> 3 [JIGSAW] <--- 5
    // hut_2 -> ^10 x7         ---> 3 [JIGSAW] <--- 3

    //xSavanna
    // hut_1 -> ^9  x10         ---> 3 [JIGSAW] <--- 6
    // hut_2 -> ^10 x9          ---> 4 [JIGSAW] <--- 4

//    corner_01     -> ^ 7  x16 ---> 6 [JIGSAW] 9  <--- //ONLY -> SNOWY 2
//    corner_02     -> ^ 12 x13 ---> 6 [JIGSAW] 6  <--- // PLAINS 1
//    crossroad_01  -> ^ 7  x16 ---> 8 [JIGSAW] 7  <--- // X X
//    crossroad_03  -> ^ 7  x10 ---> 5 [JIGSAW] 4 <---  // X X
//    straight_02   -> ^ 13 x16 ---> 8 [JIGSAW] 7 <---  // PLAINS 1 x
//    straight_03   -> ^ 10 x11 ---> 5 [JIGSAW] 5 <---  // X PLAINS 2
//    straight_04   -> ^ 8  x9  ---> 4 [JIGSAW] 4 <---  // X
//    straight_05   -> ^ 17 x17 ---> 8 [JIGSAW] 8 <---  // PLAINS 1 / PLAINS 2
//    straight_06   -> ^ 11 x18 ---> 9 [JIGSAW] 8 <---  // PLAINS 1/ PLAINS 2
//                     ^ 10 x6 ---> 2 [JIGSAW] 3 <---   // X X
//    turn_01       -> ^ 7  x5 ---> 1 [JIGSAW] 3 <---   // X
    public static void registerNewVillageStructures() {
        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/plains/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/plains/houses/herbalist_hut_1"),
                1,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_PLAINS
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/plains/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/plains/houses/herbalist_hut_2"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_PLAINS
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/taiga/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/taiga/houses/herbalist_taiga_hut_1"),
                1,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_TAIGA
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/taiga/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/taiga/houses/herbalist_taiga_hut_2"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_TAIGA
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/snowy/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/snowy/houses/herbalist_snowy_hut_1"),
                1,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_SNOWY
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/snowy/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/snowy/houses/herbalist_snowy_hut_2"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_SNOWY
        );


        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/desert/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/desert/houses/herbalist_desert_hut_1"),
                1,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_DESERT
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/desert/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/desert/houses/herbalist_desert_hut_2"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_DESERT
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/savanna/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/savanna/houses/herbalist_savanna_hut_1"),
                1,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_SAVANNA
        );

        FabricStructurePoolRegistry.register(
                new Identifier("minecraft:village/savanna/houses"),
                new Identifier(TCOTS_Main.MOD_ID, "village/savanna/houses/herbalist_savanna_hut_2"),
                2,
                TCOTS_ProcessorList.RANDOM_HERBALIST_HERBS_SAVANNA
        );
    }

    public static void registerNewStructures(){

    }

}

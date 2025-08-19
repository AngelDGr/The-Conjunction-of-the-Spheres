package neoforge.TCOTS.world.village;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_WorldGen;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;

public class TCOTS_VillageAdditions {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(
            Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath("minecraft", "empty"));


    private static void addBuildingToPool(final Registry<StructureTemplatePool> templatePoolRegistry,
                                          final Registry<StructureProcessorList> processorListRegistry,
                                          final ResourceLocation poolRL,
                                          final String nbtPieceRL,
                                          final int weight){
        addBuildingToPool(templatePoolRegistry, processorListRegistry, poolRL, nbtPieceRL, weight, EMPTY_PROCESSOR_LIST_KEY);
    }

    /**
     * Adds the building to the targeted pool.
     * We will call this in addNewVillageBuilding method further down to add to every village.
     *
     * Note: This is an additive operation which means multiple mods can do this and they stack with each other safely.
     */
    private static void addBuildingToPool(final Registry<StructureTemplatePool> templatePoolRegistry,
                                          final Registry<StructureProcessorList> processorListRegistry,
                                          final ResourceLocation poolRL,
                                          final String nbtPieceRL,
                                          final int weight,
                                          final ResourceKey<StructureProcessorList> processor) {
        // Grabs the processor list we want to use along with our piece.
        // This is a requirement as using the ProcessorLists.EMPTY field will cause the game to throw errors.
        // The reason why is the empty processor list in the world's registry is not the same instance as in that field once the world is started up.
        final Holder<StructureProcessorList> emptyProcessorList = processorListRegistry.getHolderOrThrow(processor);

        // Grab the pool we want to add to
        final StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        // Grabs the nbt piece and creates a SinglePoolElement of it that we can add to a structure's pool.
        // Use .legacy( for villages/outposts and .single( for everything else
        final SinglePoolElement piece = SinglePoolElement.legacy(nbtPieceRL,
                emptyProcessorList)
                .apply(StructureTemplatePool.Projection.RIGID);

        // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's templates field public for us to see.
        // Weight is handled by how many times the entry appears in this list.
        // We do not need to worry about immutability as this field is created using Lists.newArrayList(); which makes a mutable list.
        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        // Use AccessTransformer or Accessor Mixin to make StructureTemplatePool's rawTemplates field public for us to see.
        // This list of pairs of pieces and weights is not used by vanilla by default but another mod may need it for efficiency.
        // So lets add to this list for completeness. We need to make a copy of the array as it can be an immutable list.
        final List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }

    /**
     * We use FMLServerAboutToStartEvent as the dynamic registry exists now and all JSON worldgen files were parsed.
     * Mod compat is best done here.
     */
    public static void registerNewVillageStructures() {
        NeoForge.EVENT_BUS.addListener((final ServerAboutToStartEvent event) -> {
            final Registry<StructureProcessorList> processorListRegistry = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();
            final Registry<StructureTemplatePool> templatePoolRegistry = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow();

            // Adds our piece to all village houses pool
            // Note, the resourcelocation is getting the pool files from the data folder. Not assets folder.
            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/plains/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/plains/houses/herbalist_hut_1").toString(),
                    1,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_PLAINS);

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/plains/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/plains/houses/herbalist_hut_2").toString(),
                    2,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_PLAINS
            );

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/taiga/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/taiga/houses/herbalist_taiga_hut_1").toString(),
                    1,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_TAIGA
            );

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/taiga/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/taiga/houses/herbalist_taiga_hut_2").toString(),
                    2,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_TAIGA
            );

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/snowy/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/snowy/houses/herbalist_snowy_hut_1").toString(),
                    1,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_SNOWY
            );

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/snowy/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/snowy/houses/herbalist_snowy_hut_2").toString(),
                    2,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_SNOWY
            );


            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/desert/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/desert/houses/herbalist_desert_hut_1").toString(),
                    1,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_DESERT
            );

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/desert/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/desert/houses/herbalist_desert_hut_2").toString(),
                    2,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_DESERT
            );

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/savanna/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/savanna/houses/herbalist_savanna_hut_1").toString(),
                    1,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_SAVANNA
            );

            addBuildingToPool(
                    templatePoolRegistry,
                    processorListRegistry,
                    ResourceLocation.parse("minecraft:village/savanna/houses"),
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "village/savanna/houses/herbalist_savanna_hut_2").toString(),
                    2,
                    TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_SAVANNA
            );
        });
    }
}

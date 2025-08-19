package neoforge.TCOTS.datagen;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_WorldGen;
import neoforge.TCOTS.datagen.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = TCOTS_Main.MOD_ID)
public class TCOTS_DataGenerator {

    private static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder()
                    .add(Registries.CONFIGURED_FEATURE, TCOTS_WorldGen::boostrapConfiguredFeature)
                    .add(Registries.PLACED_FEATURE, TCOTS_WorldGen::boostrapPlacedFeature)
                    .add(Registries.PROCESSOR_LIST, TCOTS_WorldGen::boostrapProcessorList)

                    .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, TCOTS_BiomeModifiersGenerator::bootstrap);

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //Client
        {
            //Item Model
            generator.addProvider(event.includeClient(), new TCOTS_ItemModelGenerator(output, existingFileHelper));

            //Sounds
            generator.addProvider(event.includeClient(), new TCOTS_SoundsJsonGenerator(output, existingFileHelper));
        }


        //Server
        {
            //World Gen
            generator.addProvider(true, new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of(TCOTS_Main.MOD_ID)));

            //NeoForge Datamaps
            generator.addProvider(event.includeServer(), new TCOTS_DataMapGenerator(output, lookupProvider));

            //Tags
            {
                final TCOTS_BlockTagsGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                        new TCOTS_BlockTagsGenerator(output, lookupProvider, existingFileHelper));

                generator.addProvider(event.includeServer(), new TCOTS_ItemTagGenerator(output, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));

                generator.addProvider(event.includeServer(), new TCOTS_BiomeTagGenerator(output, lookupProvider, existingFileHelper));

                generator.addProvider(event.includeServer(), new TCOTS_EntityTagGenerator(output, lookupProvider, existingFileHelper));

                generator.addProvider(event.includeServer(), new TCOTS_DamageTypeTagsGenerator(output, lookupProvider, existingFileHelper));

                generator.addProvider(event.includeServer(), new TCOTS_POITypeTagGenerator(output, lookupProvider, existingFileHelper));
            }

            //Loot Tables
            generator.addProvider(event.includeServer(), createLootTableProviders(output, lookupProvider));

            //Recipes
            generator.addProvider(event.includeServer(), new TCOTS_RecipesGenerator(output, lookupProvider));

            //Advancements
            generator.addProvider(event.includeServer(), new TCOTS_AdvancementsGenerator(output, lookupProvider, existingFileHelper));
        }
    }

    public static LootTableProvider createLootTableProviders(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(output, Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(TCOTS_LootTablesBlocksGenerator::new, LootContextParamSets.BLOCK),
                        new LootTableProvider.SubProviderEntry(TCOTS_LootTablesEntitiesGenerator::new, LootContextParamSets.ENTITY),
                        new LootTableProvider.SubProviderEntry(TCOTS_LootTablesChestsGenerator::new, LootContextParamSets.CHEST),
                        new LootTableProvider.SubProviderEntry(TCOTS_LootTablesGameplayGenerator::new, LootContextParamSets.PIGLIN_BARTER)),
                lookupProvider
        );
    }
}

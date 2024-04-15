package TCOTS;

import TCOTS.items.OrganicPasteItem;
import TCOTS.items.TCOTS_Items;
import TCOTS.world.TCOTS_ConfiguredFeatures;
import TCOTS.world.TCOTS_PlacedFeature;
import TCOTS.world.TCOTS_ProcessorList;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TCOTS_DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack main = fabricDataGenerator.createPack();

        main.addProvider(ModWorldGenerator::new);
        main.addProvider(LootTablesHerbalistGenerator::new);
        main.addProvider(POIProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, TCOTS_ConfiguredFeatures::boostrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, TCOTS_PlacedFeature::boostrap);
        registryBuilder.addRegistry(RegistryKeys.PROCESSOR_LIST, TCOTS_ProcessorList::boostrap);
    }

    public static class ModWorldGenerator extends FabricDynamicRegistryProvider {
        public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
            entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
            entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
            entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PROCESSOR_LIST));
        }

        @Override
        public String getName() {
            return "World Gen";
        }
    }

    private static class LootTablesHerbalistGenerator extends SimpleFabricLootTableProvider {

        public static final Identifier PLAINS_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/plains_herbalist");
        public static final Identifier TAIGA_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/taiga_herbalist");
        public static final Identifier SNOWY_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/snowy_herbalist");
        public static final Identifier DESERT_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/desert_herbalist");
        public static final Identifier SAVANNA_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/savanna_herbalist");

        public LootTablesHerbalistGenerator(FabricDataOutput output) {
            super(output, LootContextTypes.CHEST);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {

            //Plains
            exporter.accept(PLAINS_HERBALIST_CHEST,
                    LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(ItemEntry.builder(Items.DANDELION).weight(4)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.CELANDINE).weight(4))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(TCOTS_Items.POPPY_PETALS).weight(5)
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.VERBENA).weight(3))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(TCOTS_Items.ERGOT_SEEDS).weight(1))

                            .with(ItemEntry.builder(TCOTS_Items.ALLSPICE).weight(1))

                            .with(ItemEntry.builder(Items.FLOWERING_AZALEA).weight(1))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.SEWANT_MUSHROOMS).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetNbtLootFunction.builder(
                                            OrganicPasteItem.writeEffectsToPasteNBT(new NbtCompound(), Collections.singletonList((
                                                    new SuspiciousStewIngredient.StewEffect(StatusEffects.SATURATION, 1600))))
                                    )))


                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.OXEYE_DAISY_PETALS).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 5.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ARENARIA).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(1))

                            .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(1))

                    )
            );

            exporter.accept(TAIGA_HERBALIST_CHEST,
                    LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(ItemEntry.builder(Items.FERN).weight(4))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.BROWN_MUSHROOM).weight(3))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.AZURE_BLUET_PETALS).weight(5))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.RED_MUSHROOM).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.PUFFBALL).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.SEWANT_MUSHROOMS).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 5.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.SWEET_BERRIES).weight(4))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Blocks.SPRUCE_SAPLING).weight(4))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ALLIUM_PETALS).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 8.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.CROWS_EYE).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ARENARIA).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetNbtLootFunction.builder(
                                            OrganicPasteItem.writeEffectsToPasteNBT(new NbtCompound(), Collections.singletonList((
                                                    new SuspiciousStewIngredient.StewEffect(StatusEffects.FIRE_RESISTANCE, 1600))))
                                    )))

                            .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(1))

                            .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(1))

                            .with(ItemEntry.builder(Items.MOSS_BLOCK).weight(1))
                    ));

            exporter.accept(SNOWY_HERBALIST_CHEST,
                    LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(2))

                            .with(ItemEntry.builder(Items.FERN).weight(2))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.BROWN_MUSHROOM).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.RED_MUSHROOM).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Blocks.SPRUCE_SAPLING).weight(3))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.PUFFBALL).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.VERBENA).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.AZURE_BLUET_PETALS).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.CORNFLOWER).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ARENARIA).weight(3))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.CROWS_EYE).weight(3))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.SWEET_BERRIES).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetNbtLootFunction.builder(
                                            OrganicPasteItem.writeEffectsToPasteNBT(new NbtCompound(), Collections.singletonList((
                                                    new SuspiciousStewIngredient.StewEffect(StatusEffects.JUMP_BOOST, 1600))))
                                    )))


                            .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(1))

                    ));

            exporter.accept(DESERT_HERBALIST_CHEST,
                    LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(ItemEntry.builder(Items.CACTUS).weight(4))

                            .with(ItemEntry.builder(Items.DEAD_BUSH).weight(4))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.HAN_FIBER).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(Items.BROWN_MUSHROOM).weight(2))

                            .with(ItemEntry.builder(Items.RED_MUSHROOM).weight(1))

                            .with(ItemEntry.builder(TCOTS_Items.PUFFBALL).weight(2))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(2))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.AZURE_BLUET_PETALS).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.VERBENA).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetNbtLootFunction.builder(
                                            OrganicPasteItem.writeEffectsToPasteNBT(new NbtCompound(), Collections.singletonList((
                                                    new SuspiciousStewIngredient.StewEffect(StatusEffects.NIGHT_VISION, 1600))))
                                    )))

                            .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(1))

                    ));

            exporter.accept(SAVANNA_HERBALIST_CHEST,
                    LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.EMERALD).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.DANDELION).weight(3))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.HAN_FIBER).weight(3))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.ACACIA_SAPLING).weight(4))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(Items.BROWN_MUSHROOM).weight(1))

                            .with(ItemEntry.builder(Items.RED_MUSHROOM).weight(1))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(1))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.SHORT_GRASS).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(Items.MOSS_CARPET).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))


                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.AZURE_BLUET_PETALS).weight(2))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                            .with(((LeafEntry.Builder<?>)ItemEntry.builder(TCOTS_Items.ORGANIC_PASTE).weight(1))
                                    .apply(SetNbtLootFunction.builder(
                                            OrganicPasteItem.writeEffectsToPasteNBT(new NbtCompound(), Collections.singletonList((
                                                    new SuspiciousStewIngredient.StewEffect(StatusEffects.GLOWING, 1600))))
                                    )))

                            .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(1))

                    ));
        }
    }

    private static class POIProvider extends TagProvider<PointOfInterestType>{

        public POIProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
            super(output, RegistryKeys.POINT_OF_INTEREST_TYPE, registryLookupFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            this.getOrCreateTagBuilder(PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE)
                    .addOptional(new Identifier(TCOTS_Main.MOD_ID, "herbal_poi"))
//                    .add(TCOTS_PointOfInterest.HERBAL_POI_KEY)
            ;

        }
    }

}



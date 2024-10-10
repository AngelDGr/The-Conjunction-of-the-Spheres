package TCOTS;

import TCOTS.advancements.criterion.DestroyMultipleMonsterNestsCriterion;
import TCOTS.advancements.criterion.GetTrollFollowerCriterion;
import TCOTS.advancements.criterion.TCOTS_CustomCriterion;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.AlchemyRecipeRandomlyLootFunction;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipeCategory;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipeJsonBuilder;
import TCOTS.items.concoctions.recipes.HerbalTableRecipeJsonBuilder;
import TCOTS.world.TCOTS_ConfiguredFeatures;
import TCOTS.world.TCOTS_DamageTypes;
import TCOTS.world.TCOTS_PlacedFeature;
import TCOTS.world.TCOTS_ProcessorList;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.TypeSpecificPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings({"unused", "deprecated"})
public class TCOTS_DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack main = fabricDataGenerator.createPack();

        main.addProvider(ModWorldGenerator::new);
        main.addProvider(LootTablesChestsGenerator::new);
        main.addProvider(POIProvider::new);
        main.addProvider(DamageTypeTagsGenerator::new);
        main.addProvider(BlockTagsGenerator::new);
        main.addProvider(EntityTagGenerator::new);
        main.addProvider(ItemTagGenerator::new);
        main.addProvider(RecipesGenerator::new);
        main.addProvider(AdvancementsGenerator::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, TCOTS_ConfiguredFeatures::boostrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, TCOTS_PlacedFeature::boostrap);
        registryBuilder.addRegistry(RegistryKeys.PROCESSOR_LIST, TCOTS_ProcessorList::boostrap);
        registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, TCOTS_DamageTypes::boostrap);
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

    private static class LootTablesChestsGenerator extends SimpleFabricLootTableProvider {

        public static final Identifier PLAINS_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/plains_herbalist");
        public static final Identifier TAIGA_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/taiga_herbalist");
        public static final Identifier SNOWY_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/snowy_herbalist");
        public static final Identifier DESERT_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/desert_herbalist");
        public static final Identifier SAVANNA_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/savanna_herbalist");

        public static final Identifier TROLL_BARREL = new Identifier(TCOTS_Main.MOD_ID, "chests/troll/troll_barrel");
        public static final Identifier ICE_TROLL_BARREL = new Identifier(TCOTS_Main.MOD_ID, "chests/troll/ice_troll_barrel");
        public static final Identifier FOREST_TROLL_BARREL = new Identifier(TCOTS_Main.MOD_ID, "chests/troll/forest_troll_barrel");

        public LootTablesChestsGenerator(FabricDataOutput output) {
            super(output, LootContextTypes.CHEST);
        }

        @Override
        public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {

            //Plains
            {
                exporter.accept(PLAINS_HERBALIST_CHEST,
                        LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_BOOK).weight(1))
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_FORMULA).weight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCOHEST).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.DWARVEN_SPIRIT).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(5))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.EMERALD).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(ItemEntry.builder(Items.DANDELION).weight(8)
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.CELANDINE).weight(8))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(ItemEntry.builder(Items.POPPY).weight(10)
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 3.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.VERBENA).weight(6))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(ItemEntry.builder(TCOTS_Items.ERGOT_SEEDS).weight(2))

                                .with(ItemEntry.builder(TCOTS_Items.ALLSPICE).weight(2))

                                .with(ItemEntry.builder(Items.FLOWERING_AZALEA).weight(2))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.SEWANT_MUSHROOMS).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.LILY_OF_THE_VALLEY).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.OXEYE_DAISY).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ARENARIA).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(2))

                                .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(2))

                        )
                );
            }

            //Taiga
            {
                exporter.accept(TAIGA_HERBALIST_CHEST,
                        LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_BOOK).weight(1))
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_FORMULA).weight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCOHEST).weight(3))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.DWARVEN_SPIRIT).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.EMERALD).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(ItemEntry.builder(Items.FERN).weight(8))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.BROWN_MUSHROOM).weight(6))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.AZURE_BLUET).weight(10))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RED_MUSHROOM).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.PUFFBALL).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.SEWANT_MUSHROOMS).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 5.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.SWEET_BERRIES).weight(8))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Blocks.SPRUCE_SAPLING).weight(8))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.ALLIUM).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.CROWS_EYE).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ARENARIA).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))


                                .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(2))

                                .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(2))

                                .with(ItemEntry.builder(Items.MOSS_BLOCK).weight(2))

                        ));
            }

            //Snowy
            {
                exporter.accept(SNOWY_HERBALIST_CHEST,
                        LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_BOOK).weight(1))
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_FORMULA).weight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCOHEST).weight(3))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.DWARVEN_SPIRIT).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.EMERALD).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(7))

                                .with(ItemEntry.builder(Items.FERN).weight(7))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.BROWN_MUSHROOM).weight(7))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RED_MUSHROOM).weight(3))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Blocks.SPRUCE_SAPLING).weight(10))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.PUFFBALL).weight(7))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.VERBENA).weight(7))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.AZURE_BLUET).weight(7))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.CORNFLOWER).weight(3))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ARENARIA).weight(10))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.CROWS_EYE).weight(10))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.SWEET_BERRIES).weight(7))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(3))


                        ));
            }

            //Desert
            {
                exporter.accept(DESERT_HERBALIST_CHEST,
                        LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_BOOK).weight(1))
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_FORMULA).weight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCOHEST).weight(3))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.DWARVEN_SPIRIT).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(4))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.EMERALD).weight(2))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                .with(ItemEntry.builder(Items.CACTUS).weight(10))

                                .with(ItemEntry.builder(Items.DEAD_BUSH).weight(10))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.HAN_FIBER).weight(5))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(ItemEntry.builder(Items.BROWN_MUSHROOM).weight(5))

                                .with(ItemEntry.builder(Items.RED_MUSHROOM).weight(3))

                                .with(ItemEntry.builder(TCOTS_Items.PUFFBALL).weight(5))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.LILY_OF_THE_VALLEY).weight(5))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(5))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.AZURE_BLUET).weight(3))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.VERBENA).weight(3))
                                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(3))


                        ));
            }

            //Savanna
            {
                {
                    exporter.accept(SAVANNA_HERBALIST_CHEST,
                            LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 8.0f))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_BOOK).weight(1))
                                            .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCHEMY_FORMULA).weight(3))
                                            .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ALCOHEST).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.DWARVEN_SPIRIT).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.EMERALD).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.DANDELION).weight(8))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.HAN_FIBER).weight(8))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.ACACIA_SAPLING).weight(10))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(ItemEntry.builder(Items.BROWN_MUSHROOM).weight(3))

                                    .with(ItemEntry.builder(Items.RED_MUSHROOM).weight(3))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.LILY_OF_THE_VALLEY).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(ItemEntry.builder(TCOTS_Items.BRYONIA).weight(3))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.SHORT_GRASS).weight(5))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.MOSS_CARPET).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.AZURE_BLUET).weight(5))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(ItemEntry.builder(Items.GLOW_LICHEN).weight(3))

                            ));
                }
            }


            //Trolls barrels
            {
                //Rock Troll Barrel
                {
                    exporter.accept(TROLL_BARREL,
                            LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(3.0f, 5.0f))

                                    //Rocks
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COBBLESTONE).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0f, 8.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.ANDESITE).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0f, 8.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.GRANITE).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0f, 8.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.TUFF).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0f, 8.0f))))

                                    //Ores
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RAW_COPPER).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 6.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RAW_IRON).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RAW_GOLD).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                    //Meat
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.ROTTEN_FLESH).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 8.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.BEEF).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.MUTTON).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.PORKCHOP).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RABBIT).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.CHICKEN).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    //Alcohol-Cooked Meat
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.VILLAGE_HERBAL).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_BEEF).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_MUTTON).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_PORKCHOP).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_RABBIT).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_CHICKEN).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(ItemEntry.builder(Items.RABBIT_STEW).weight(1))

                                    //Amethyst-RawBlocks
                                    .with(ItemEntry.builder(Items.AMETHYST_SHARD).weight(1))
                                    .with(ItemEntry.builder(Items.RAW_COPPER).weight(1))
                                    .with(ItemEntry.builder(Items.RAW_IRON_BLOCK).weight(1))
                                    .with(ItemEntry.builder(Items.RAW_GOLD_BLOCK).weight(1))


                            ));
                }

                //Ice Troll Barrel
                {
                    exporter.accept(ICE_TROLL_BARREL,
                            LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(4.0f, 6.0f))

                                    //Rocks-Ice
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COBBLESTONE).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0f, 8.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.SNOWBALL).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 12.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.SNOW_BLOCK).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 8.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.ICE).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 6.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.PACKED_ICE).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 6.0f))))

                                    .with(ItemEntry.builder(Items.BLUE_ICE).weight(2))

                                    //Ores

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RAW_IRON).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RAW_COPPER).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 6.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.EMERALD).weight(1))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))

                                    //Meat
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.ROTTEN_FLESH).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 8.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.MUTTON).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RABBIT).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))

                                    //Alcohol-Cooked Meat
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.VILLAGE_HERBAL).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.MANDRAKE_CORDIAL).weight(1))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_MUTTON).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_RABBIT).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(ItemEntry.builder(Items.RABBIT_STEW).weight(1))

                                    //Amethyst-RawBlocks
                                    .with(ItemEntry.builder(Items.LAPIS_LAZULI).weight(1))
                                    .with(ItemEntry.builder(Items.RAW_COPPER).weight(1))
                                    .with(ItemEntry.builder(Items.RAW_IRON_BLOCK).weight(1))

                            ));
                }

                //Forest Troll Barrel
                {
                    exporter.accept(FOREST_TROLL_BARREL,
                            LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(4.0f, 6.0f))

                                    //Wood
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.OAK_LOG).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 6.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.BIRCH_LOG).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 6.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.SPRUCE_LOG).weight(4))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 6.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.DARK_OAK_LOG).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 6.0f))))

                                    //Saplings
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.OAK_SAPLING).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.BIRCH_SAPLING).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.SPRUCE_SAPLING).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.DARK_OAK_SAPLING).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    //Ores
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RAW_IRON).weight(1))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RAW_COPPER).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 6.0f))))


                                    //Meat
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.ROTTEN_FLESH).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(6.0f, 10.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.MUTTON).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 5.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.RABBIT).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.CHICKEN).weight(3))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.PORKCHOP).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.BEEF).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 4.0f))))

                                    //Alcohol-Cooked Meat
                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.VILLAGE_HERBAL).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.ICY_SPIRIT).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(TCOTS_Items.MANDRAKE_CORDIAL).weight(1))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_MUTTON).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 3.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_RABBIT).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_CHICKEN).weight(2))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_BEEF).weight(1))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(((LeafEntry.Builder<?>) ItemEntry.builder(Items.COOKED_PORKCHOP).weight(1))
                                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))

                                    .with(ItemEntry.builder(Items.RABBIT_STEW).weight(1))

                                    //CopperIngot-RawBlocks
                                    .with(ItemEntry.builder(Items.COPPER_INGOT).weight(1))
                                    .with(ItemEntry.builder(Items.RAW_COPPER).weight(1))
                                    .with(ItemEntry.builder(Items.RAW_IRON_BLOCK).weight(1))

                            ));
                }
            }

        }
    }

    private static class POIProvider extends TagProvider<PointOfInterestType>{

        public POIProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
            super(output, RegistryKeys.POINT_OF_INTEREST_TYPE, registryLookupFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            this.getOrCreateTagBuilder(PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE)
                    .addOptional(new Identifier(TCOTS_Main.MOD_ID, "herbal_poi"));

        }
    }

    private static class DamageTypeTagsGenerator extends TagProvider<DamageType> {


        /**
         * Constructs a new {@link FabricTagProvider} with the default computed path.
         *
         * <p>Common implementations of this class are provided.
         *
         * @param output           the {@link FabricDataOutput} instance
         * @param registriesFuture the backing registry for the tag type
         */
        public DamageTypeTagsGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, RegistryKeys.DAMAGE_TYPE, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            this.getOrCreateTagBuilder(DamageTypeTags.BYPASSES_ARMOR).add(TCOTS_DamageTypes.POTION_TOXICITY).add(TCOTS_DamageTypes.BLEEDING).add(TCOTS_DamageTypes.CADAVERINE);
        }

    }

    private static class BlockTagsGenerator extends FabricTagProvider.BlockTagProvider {
        public BlockTagsGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup lookup) {
            this.getOrCreateTagBuilder(TCOTS_Blocks.IGNITING_BLOCKS)
                    .add(Blocks.FIRE)
                    .add(Blocks.SOUL_FIRE)
                    .add(Blocks.MAGMA_BLOCK);

            this.getOrCreateTagBuilder(TCOTS_Blocks.DESTROYABLE_MAGIC_BLOCKS)
                    .add(Blocks.NETHER_PORTAL)
                    .add(Blocks.END_GATEWAY)
                    .add(Blocks.SOUL_FIRE);

            this.getOrCreateTagBuilder(TCOTS_Blocks.NEGATES_DEVOURER_JUMP)
                    .add(Blocks.HAY_BLOCK)
                    .add(Blocks.MOSS_BLOCK)
                    .add(Blocks.COBWEB)
                    .add(Blocks.SCULK)
                    .add(Blocks.SLIME_BLOCK)
                    .add(Blocks.HONEY_BLOCK)

                    .addOptionalTag(BlockTags.BEDS)
                    .addOptionalTag(BlockTags.WOOL)
                    .addOptionalTag(BlockTags.LEAVES)
                    .addOptionalTag(BlockTags.WART_BLOCKS);
        }
    }
    private static class EntityTagGenerator extends FabricTagProvider.EntityTypeTagProvider {
        public EntityTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            this.getOrCreateTagBuilder(TCOTS_Entities.IGNITING_ENTITIES)
                    .add(EntityType.BLAZE)
                    .add(EntityType.FIREBALL)
                    .add(EntityType.SMALL_FIREBALL)
                    .add(EntityType.FIREWORK_ROCKET);

            this.getOrCreateTagBuilder(TCOTS_Entities.DIMERITIUM_REMOVAL)
                    .add(EntityType.EVOKER_FANGS)
                    .add(EntityType.AREA_EFFECT_CLOUD)
                    .add(EntityType.SHULKER_BULLET);

            this.getOrCreateTagBuilder(TCOTS_Entities.DIMERITIUM_DAMAGE)
                    .add(EntityType.END_CRYSTAL)
                    .add(TCOTS_Entities.FOGLING);

            this.getOrCreateTagBuilder(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                    .add(TCOTS_Entities.ICE_TROLL);

            this.getOrCreateTagBuilder(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                    .add(TCOTS_Entities.ICE_TROLL)
                    .add(TCOTS_Entities.CYCLOPS);
        }
    }
    private static class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            this.getOrCreateTagBuilder(TCOTS_Items.DECAYING_FLESH)
                    .add(Items.BEEF)
                    .add(Items.PORKCHOP)
                    .add(Items.MUTTON)
                    .add(Items.CHICKEN)
                    .add(Items.RABBIT);

            this.getOrCreateTagBuilder(TCOTS_Items.MONSTER_BLOOD)
                    .add(TCOTS_Items.ROTFIEND_BLOOD)
                    .add(TCOTS_Items.GHOUL_BLOOD);
        }
    }


    private static class RecipesGenerator extends FabricRecipeProvider{

        public RecipesGenerator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(RecipeExporter exporter) {
            //Crafting Table
            {
                //Alchemy Table
                {
                    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, TCOTS_Items.ALCHEMY_TABLE_ITEM)
                            .pattern("B B")
                            .pattern("CWC")
                            .pattern("WWW")
                            .input('B', Items.GLASS_BOTTLE)
                            .input('C', Items.COBBLESTONE)
                            .input('W', ItemTags.PLANKS)

                            .criterion(FabricRecipeProvider.hasItem(Items.GLASS_BOTTLE), FabricRecipeProvider.conditionsFromItem(Items.GLASS_BOTTLE))
                            .offerTo(exporter);
                }

                //Herbal Table
                {
                    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, TCOTS_Items.HERBAL_TABLE_ITEM)
                            .pattern("FF")
                            .pattern("WW")
                            .pattern("WW")
                            .input('F', ItemTags.SMALL_FLOWERS)
                            .input('W', ItemTags.PLANKS)

                            .criterion(FabricRecipeProvider.hasItem(Items.DANDELION), FabricRecipeProvider.conditionsFromItem(Items.DANDELION))
                            .criterion(FabricRecipeProvider.hasItem(Items.POPPY), FabricRecipeProvider.conditionsFromItem(Items.POPPY))
                            .criterion(FabricRecipeProvider.hasItem(Items.CORNFLOWER), FabricRecipeProvider.conditionsFromItem(Items.CORNFLOWER))
                            .offerTo(exporter);
                }

                //G'valchir
                {
                    ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.GVALCHIR)
                            .pattern("FHD")
                            .pattern("HSH")
                            .pattern("DHF")
                            .input('F', TCOTS_Items.FOGLET_TEETH)
                            .input('H', TCOTS_Items.BULLVORE_HORN_FRAGMENT)
                            .input('D', TCOTS_Items.DEVOURER_TEETH)
                            .input('S', Items.IRON_SWORD)



                            .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.BULLVORE_HORN_FRAGMENT), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.BULLVORE_HORN_FRAGMENT))
                            .criterion(FabricRecipeProvider.hasItem(Items.IRON_SWORD), FabricRecipeProvider.conditionsFromItem(Items.IRON_SWORD))
                            .offerTo(exporter);
                }

                //Moonblade
                {
                    ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.MOONBLADE)
                            .pattern("HBG")
                            .pattern("BSB")
                            .pattern("GBH")
                            .input('G', TCOTS_Items.GRAVEIR_BONE)
                            .input('H', TCOTS_Items.NEKKER_HEART)
                            .input('B', TCOTS_Items.MONSTER_BLOOD)
                            .input('S', Items.GOLDEN_SWORD)



                            .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.GRAVEIR_BONE), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.GRAVEIR_BONE))
                            .criterion(FabricRecipeProvider.hasItem(Items.GOLDEN_SWORD), FabricRecipeProvider.conditionsFromItem(Items.GOLDEN_SWORD))
                            .offerTo(exporter);
                }

                //Knight Crossbow
                {
                    ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.KNIGHT_CROSSBOW)
                            .pattern("IWI")
                            .pattern("LHL")
                            .pattern(" S ")
                            .input('I', Items.IRON_BLOCK)
                            .input('W', ItemTags.WOOL)
                            .input('L', Items.LEATHER)
                            .input('H', Items.TRIPWIRE_HOOK)
                            .input('S', Items.STICK)

                            .criterion(FabricRecipeProvider.hasItem(Items.IRON_BLOCK), FabricRecipeProvider.conditionsFromItem(Items.IRON_BLOCK))
                            .criterion(FabricRecipeProvider.hasItem(Items.LEATHER), FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                            .criterion(FabricRecipeProvider.hasItem(Items.TRIPWIRE_HOOK), FabricRecipeProvider.conditionsFromItem(Items.TRIPWIRE_HOOK))
                            .offerTo(exporter);
                }

                //Crossbow Bolts
                {
                    //Normal Bolt
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.BASE_BOLT, 2)
                                .pattern(" I ")
                                .pattern("FSF")
                                .input('I', Items.IRON_INGOT)
                                .input('F', Items.FEATHER)
                                .input('S', Items.STICK)

                                .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                                .criterion(FabricRecipeProvider.hasItem(Items.FEATHER), FabricRecipeProvider.conditionsFromItem(Items.FEATHER))
                                .offerTo(exporter);
                    }

                    //Blunt Bolt
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.BLUNT_BOLT, 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("TL ")
                                .input('P', Items.GOLD_BLOCK)
                                .input('L', Items.IRON_INGOT)
                                .input('T', Items.IRON_BLOCK)
                                .input('#', TCOTS_Items.BASE_BOLT)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.BASE_BOLT), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.BASE_BOLT))
                                .offerTo(exporter);
                    }

                    //Precision Bolt
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.PRECISION_BOLT, 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("LL ")
                                .input('P', Items.IRON_NUGGET)
                                .input('L', Items.FEATHER)
                                .input('#', TCOTS_Items.BASE_BOLT)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.BASE_BOLT), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.BASE_BOLT))
                                .offerTo(exporter);
                    }

                    //Exploding Bolt
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.EXPLODING_BOLT, 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("TL ")
                                .input('P', TCOTS_Items.STAMMELFORDS_DUST)
                                .input('L', Items.STRING)
                                .input('T', Items.PAPER)
                                .input('#', TCOTS_Items.BASE_BOLT)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.BASE_BOLT), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.BASE_BOLT))
                                .offerTo(exporter);
                    }

                    //Broadhead Bolt
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.BROADHEAD_BOLT, 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("TL ")
                                .input('P', TCOTS_Items.FOGLET_TEETH)
                                .input('L', Items.IRON_NUGGET)
                                .input('T', Items.FEATHER)
                                .input('#', TCOTS_Items.BASE_BOLT)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.BASE_BOLT), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.BASE_BOLT))
                                .offerTo(exporter);
                    }
                }

                //Warrior's Leather Armor
                {
                    //Jacket
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_JACKET)
                                .pattern("I I")
                                .pattern("LLL")
                                .pattern("NIN")
                                .input('L', Items.LEATHER)
                                .input('I', Items.IRON_INGOT)
                                .input('N', Items.IRON_NUGGET)

                                .criterion(FabricRecipeProvider.hasItem(Items.LEATHER), FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                                .offerTo(exporter);
                    }

                    //Trousers
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_TROUSERS)
                                .pattern("NIN")
                                .pattern("L L")
                                .pattern("L L")
                                .input('L', Items.LEATHER)
                                .input('I', Items.IRON_INGOT)
                                .input('N', Items.IRON_NUGGET)

                                .criterion(FabricRecipeProvider.hasItem(Items.LEATHER), FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                                .offerTo(exporter);
                    }

                    //Boots
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_BOOTS)
                                .pattern("L L")
                                .pattern("I I")
                                .input('L', Items.LEATHER)
                                .input('I', Items.IRON_INGOT)

                                .criterion(FabricRecipeProvider.hasItem(Items.LEATHER), FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                                .offerTo(exporter);
                    }
                }

                //Manticore Armor
                {
                    //Chestplate
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_ARMOR)
                                .pattern("L L")
                                .pattern("IEI")
                                .pattern("ELE")
                                .input('L', TCOTS_Items.CURED_MONSTER_LEATHER)
                                .input('E', TCOTS_Items.NEKKER_EYE)
                                .input('I', Items.IRON_INGOT)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CURED_MONSTER_LEATHER), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CURED_MONSTER_LEATHER))
                                .offerTo(exporter);
                    }

                    //Trousers
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_TROUSERS)
                                .pattern("LIL")
                                .pattern("B B")
                                .pattern("L L")
                                .input('L', TCOTS_Items.CURED_MONSTER_LEATHER)
                                .input('I', Items.IRON_INGOT)
                                .input('B', TCOTS_Items.MONSTER_BLOOD)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CURED_MONSTER_LEATHER), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CURED_MONSTER_LEATHER))
                                .offerTo(exporter);
                    }

                    //Boots
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_BOOTS)
                                .pattern("L L")
                                .pattern("B B")
                                .pattern("I I")
                                .input('L', TCOTS_Items.CURED_MONSTER_LEATHER)
                                .input('I', Items.IRON_INGOT)
                                .input('B', TCOTS_Items.MONSTER_BLOOD)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CURED_MONSTER_LEATHER), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CURED_MONSTER_LEATHER))
                                .offerTo(exporter);
                    }
                }

                //Raven's Armor
                {
                    //Chestplate
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_ARMOR)
                                .pattern("D D")
                                .pattern("LDL")
                                .pattern("GIG")
                                .input('D', Items.DIAMOND)
                                .input('L', TCOTS_Items.CURED_MONSTER_LEATHER)
                                .input('G', TCOTS_Items.GRAVEIR_BONE)
                                .input('I', Items.IRON_INGOT)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CURED_MONSTER_LEATHER), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CURED_MONSTER_LEATHER))
                                .offerTo(exporter);
                    }

                    //Trousers
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_TROUSERS)
                                .pattern("BLB")
                                .pattern("D D")
                                .pattern("L L")
                                .input('D', Items.DIAMOND)
                                .input('L', TCOTS_Items.CURED_MONSTER_LEATHER)
                                .input('B', TCOTS_Items.BULLVORE_HORN_FRAGMENT)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CURED_MONSTER_LEATHER), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CURED_MONSTER_LEATHER))
                                .offerTo(exporter);
                    }

                    //Boots
                    {
                        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_BOOTS)
                                .pattern("D D")
                                .pattern("L L")
                                .pattern("T T")
                                .input('D', Items.DIAMOND)
                                .input('L', TCOTS_Items.CURED_MONSTER_LEATHER)
                                .input('T', TCOTS_Items.DEVOURER_TEETH)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CURED_MONSTER_LEATHER), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CURED_MONSTER_LEATHER))
                                .offerTo(exporter);
                    }
                }

                //Ingredients Crafting
                {
                    ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, TCOTS_Items.CURED_MONSTER_LEATHER, 2)
                            .input(TCOTS_Items.CADAVERINE)
                            .input(TCOTS_Items.MONSTER_BLOOD)
                            .input(Items.ROTTEN_FLESH)
                            .input(Items.LEATHER)

                            .criterion(FabricRecipeProvider.hasItem(Items.LEATHER), FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                            .offerTo(exporter);
                }

                //Bone Meal from bones
                {
                    //Devourer teeth
                    {
                        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BONE_MEAL, 12)
                                .input(TCOTS_Items.DEVOURER_TEETH)
                                .group("bonemeal")

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.DEVOURER_TEETH), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.DEVOURER_TEETH))
                                .offerTo(exporter, new Identifier(TCOTS_Main.MOD_ID, "bone_meal_from_devourer_teeth"));
                    }

                    //Graveir bone
                    {
                        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BONE_MEAL, 16)
                            .input(TCOTS_Items.GRAVEIR_BONE)
                            .group("bonemeal")

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.GRAVEIR_BONE), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.GRAVEIR_BONE))
                                .offerTo(exporter, new Identifier(TCOTS_Main.MOD_ID, "bone_meal_from_graveir_bone"));
                    }
                }

                //Cadaverine crafting
                {

                    //Head
                    {
                        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.SKELETON_SKULL)
                                .input(TCOTS_Items.CADAVERINE)
                                .input(Items.ZOMBIE_HEAD)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CADAVERINE), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CADAVERINE))
                                .offerTo(exporter, new Identifier(TCOTS_Main.MOD_ID, "cadaverine_decay_head"));
                    }

                    //Bone
                    {
                        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BONE, 2)
                                .input(TCOTS_Items.CADAVERINE)
                                .input(TCOTS_Items.DECAYING_FLESH)

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CADAVERINE), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CADAVERINE))
                                .offerTo(exporter, new Identifier(TCOTS_Main.MOD_ID, "cadaverine_decay_flesh"));
                    }
                }


            }

            //Alchemy Table
            {
                float order = 0;
                //Potions
                {

                    //Swallow
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                        order, TCOTS_Items.SWALLOW_POTION,
                                        ingredientsList(
                                                TCOTS_Items.CELANDINE, 5,
                                                TCOTS_Items.DROWNER_BRAIN, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                        order, TCOTS_Items.SWALLOW_POTION_ENHANCED, 1,
                                        ingredientsList(
                                                TCOTS_Items.DROWNER_BRAIN, 5,
                                                TCOTS_Items.CELANDINE, 6,
                                                Items.LILY_OF_THE_VALLEY, 4),
                                        TCOTS_Items.SWALLOW_POTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                        order, TCOTS_Items.SWALLOW_POTION_SUPERIOR, 2,
                                        ingredientsList(
                                                Items.GLOW_BERRIES, 6,
                                                TCOTS_Items.CELANDINE, 4,
                                                TCOTS_Items.CROWS_EYE, 4,
                                                TCOTS_Items.VITRIOL, 2),
                                        TCOTS_Items.SWALLOW_POTION_ENHANCED).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotionSplash(
                                        order, TCOTS_Items.SWALLOW_SPLASH,
                                TCOTS_Items.SWALLOW_POTION).offerTo(exporter);

                    }

                    //Cat
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.CAT_POTION,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 4,
                                        TCOTS_Items.WATER_ESSENCE, 2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.CAT_POTION_ENHANCED, 1,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 5,
                                        Items.BROWN_MUSHROOM, 1,
                                        TCOTS_Items.WATER_ESSENCE, 3),
                                TCOTS_Items.CAT_POTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.CAT_POTION_SUPERIOR, 2,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 4,
                                        Items.BROWN_MUSHROOM, 4,
                                        TCOTS_Items.ALLSPICE, 2,
                                        TCOTS_Items.AETHER, 1),
                                TCOTS_Items.CAT_POTION_ENHANCED).offerTo(exporter);
                    }

                    //White Raffard's
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION,
                                ingredientsList(
                                        Items.OXEYE_DAISY, 2,
                                        TCOTS_Items.NEKKER_HEART, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED, 1,
                                ingredientsList(
                                        Items.OXEYE_DAISY, 4,
                                        TCOTS_Items.BRYONIA, 1,
                                        TCOTS_Items.NEKKER_HEART, 5),
                                TCOTS_Items.WHITE_RAFFARDS_DECOCTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR, 2,
                                ingredientsList(
                                        Items.OXEYE_DAISY, 4,
                                        TCOTS_Items.BRYONIA, 4,
                                        Items.FLOWERING_AZALEA, 4,
                                        TCOTS_Items.VERMILION, 1),
                                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotionSplash(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH,
                                TCOTS_Items.WHITE_RAFFARDS_DECOCTION).offerTo(exporter);
                    }

                    //Killer Whale
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.KILLER_WHALE_POTION,
                                ingredientsList(
                                        Items.KELP, 6,
                                        Items.SWEET_BERRIES, 5,
                                        TCOTS_Items.DROWNER_TONGUE, 5)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotionSplash(
                                order, TCOTS_Items.KILLER_WHALE_SPLASH,
                                TCOTS_Items.KILLER_WHALE_POTION).offerTo(exporter);
                    }

                    //Black Blood
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.BLACK_BLOOD_POTION,
                                ingredientsList(
                                        TCOTS_Items.SEWANT_MUSHROOMS, 2,
                                        TCOTS_Items.GHOUL_BLOOD, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED, 1,
                                ingredientsList(
                                        Items.ALLIUM, 2,
                                        TCOTS_Items.SEWANT_MUSHROOMS, 5,
                                        TCOTS_Items.GHOUL_BLOOD, 5),
                                TCOTS_Items.BLACK_BLOOD_POTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR, 2,
                                ingredientsList(
                                        Items.ALLIUM, 6,
                                        TCOTS_Items.SEWANT_MUSHROOMS, 5,
                                        TCOTS_Items.HAN_FIBER, 2,
                                        TCOTS_Items.REBIS, 1),
                                TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED).offerTo(exporter);
                    }

                    //Maribor Forest
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.MARIBOR_FOREST_POTION,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 3,
                                        TCOTS_Items.DROWNER_TONGUE, 4,
                                        TCOTS_Items.ALGHOUL_BONE_MARROW, 2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED, 1,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 5,
                                        TCOTS_Items.CROWS_EYE, 2,
                                        TCOTS_Items.DROWNER_TONGUE, 2),
                                TCOTS_Items.MARIBOR_FOREST_POTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR, 2,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 4,
                                        TCOTS_Items.CROWS_EYE, 4,
                                        Items.ALLIUM, 6,
                                        TCOTS_Items.VERMILION, 1),
                                TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED).offerTo(exporter);
                    }

                    //Wolf
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WOLF_POTION,
                                ingredientsList(
                                        Items.BONE_MEAL, 12,
                                        TCOTS_Items.DEVOURER_TEETH, 2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WOLF_POTION_ENHANCED, 1,
                                ingredientsList(
                                        Items.BONE_MEAL, 12,
                                        TCOTS_Items.GHOUL_BLOOD, 2,
                                        TCOTS_Items.DEVOURER_TEETH, 8),
                                TCOTS_Items.WOLF_POTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WOLF_POTION_SUPERIOR, 2,
                                ingredientsList(
                                        Items.BONE_MEAL, 16,
                                        TCOTS_Items.GHOUL_BLOOD, 4,
                                        TCOTS_Items.HAN_FIBER, 6,
                                        TCOTS_Items.HYDRAGENUM, 1),
                                TCOTS_Items.WOLF_POTION_ENHANCED).offerTo(exporter);
                    }

                    //Rook
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.ROOK_POTION,
                                ingredientsList(
                                        Items.POPPY, 4,
                                        TCOTS_Items.NEKKER_EYE, 3)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.ROOK_POTION_ENHANCED, 1,
                                ingredientsList(
                                        Items.POPPY, 6,
                                        Items.BROWN_MUSHROOM, 4,
                                        TCOTS_Items.NEKKER_EYE, 6),
                                TCOTS_Items.ROOK_POTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.ROOK_POTION_SUPERIOR, 2,
                                ingredientsList(
                                        Items.POPPY, 12,
                                        Items.BROWN_MUSHROOM, 8,
                                        TCOTS_Items.ALGHOUL_BONE_MARROW, 4,
                                        TCOTS_Items.RUBEDO, 1),
                                TCOTS_Items.ROOK_POTION_ENHANCED).offerTo(exporter);
                    }

                    //White Honey
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_HONEY_POTION,
                                ingredientsList(
                                        Items.HONEY_BOTTLE, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_HONEY_POTION_ENHANCED, 1,
                                ingredientsList(
                                        Items.HONEY_BOTTLE, 2,
                                        Items.LILY_OF_THE_VALLEY, 2),
                                TCOTS_Items.WHITE_HONEY_POTION).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR, 2,
                                ingredientsList(
                                        Items.HONEY_BOTTLE, 4,
                                        Items.LILY_OF_THE_VALLEY, 4,
                                        Items.ALLIUM, 8,
                                        TCOTS_Items.VITRIOL, 1),
                                TCOTS_Items.WHITE_HONEY_POTION_ENHANCED).offerTo(exporter);
                    }

                }

                //Decoctions
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.WATER_HAG_DECOCTION,
                            ingredientsList(
                                    TCOTS_Items.WATER_HAG_MUTAGEN, 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.SWEET_BERRIES, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.GRAVE_HAG_DECOCTION,
                            ingredientsList(
                                    TCOTS_Items.GRAVE_HAG_MUTAGEN, 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.RED_MUSHROOM, 2,
                                    Items.BROWN_MUSHROOM, 3)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.ALGHOUL_DECOCTION,
                            ingredientsList(
                                    TCOTS_Items.ALGHOUL_BONE_MARROW, 2,
                                    Items.ECHO_SHARD, 4,
                                    Items.KELP, 2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.FOGLET_DECOCTION,
                            ingredientsList(
                                    TCOTS_Items.FOGLET_MUTAGEN, 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.AZURE_BLUET, 2,
                                    Items.DANDELION, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.NEKKER_WARRIOR_DECOCTION,
                            ingredientsList(
                                    TCOTS_Items.NEKKER_WARRIOR_MUTAGEN, 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.AZURE_BLUET, 2,
                                    Items.FERN, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.TROLL_DECOCTION,
                            ingredientsList(
                                    TCOTS_Items.TROLL_MUTAGEN, 1,
                                    Items.ECHO_SHARD, 4,
                                    TCOTS_Items.CROWS_EYE, 4,
                                    Items.HONEY_BOTTLE, 8)).offerTo(exporter);
                }

                //Bombs
                {
                    //Grapeshot
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.GRAPESHOT,
                                        ingredientsList(Items.BONE_MEAL,12)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.GRAPESHOT_ENHANCED,
                                ingredientsList(
                                        Items.BONE_MEAL,4,
                                        Items.DANDELION,2,
                                        TCOTS_Items.CROWS_EYE,2,
                                        Items.RED_MUSHROOM, 1),
                                TCOTS_Items.GRAPESHOT).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.GRAPESHOT_SUPERIOR,
                                ingredientsList(
                                        Items.BONE_MEAL,4,
                                        Items.BLAZE_POWDER,2,
                                        Items.RED_MUSHROOM,2,
                                        TCOTS_Items.NIGREDO, 1),
                                TCOTS_Items.GRAPESHOT_ENHANCED).offerTo(exporter);
                    }

                    //Samum
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.SAMUM,
                                ingredientsList(TCOTS_Items.CELANDINE,2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.SAMUM_ENHANCED,
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,2,
                                        TCOTS_Items.FOGLET_TEETH,2,
                                        TCOTS_Items.CELANDINE,1,
                                        Items.DANDELION, 1),
                                TCOTS_Items.SAMUM).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.SAMUM_SUPERIOR,
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,4,
                                        TCOTS_Items.FOGLET_TEETH,4,
                                        TCOTS_Items.CELANDINE,1,
                                        TCOTS_Items.AETHER, 1),
                                TCOTS_Items.SAMUM_ENHANCED).offerTo(exporter);
                    }

                    //Dancing Star
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DANCING_STAR,
                                ingredientsList(Items.BLAZE_POWDER,2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DANCING_STAR_ENHANCED,
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,2,
                                        Items.BLAZE_POWDER,1,
                                        TCOTS_Items.SEWANT_MUSHROOMS,1,
                                        Items.ALLIUM, 4),
                                TCOTS_Items.DANCING_STAR).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DANCING_STAR_SUPERIOR,
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,4,
                                        Items.BLAZE_POWDER,2,
                                        TCOTS_Items.SEWANT_MUSHROOMS,2,
                                        TCOTS_Items.NIGREDO, 1),
                                TCOTS_Items.DANCING_STAR_ENHANCED).offerTo(exporter);
                    }

                    //Devil's Puffball
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DEVILS_PUFFBALL,
                                ingredientsList(TCOTS_Items.SEWANT_MUSHROOMS,2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DEVILS_PUFFBALL_ENHANCED,
                                ingredientsList(
                                        Items.BONE_MEAL,4,
                                        TCOTS_Items.SEWANT_MUSHROOMS,2,
                                        Items.SPIDER_EYE,2,
                                        Items.MOSS_BLOCK, 1),
                                TCOTS_Items.DEVILS_PUFFBALL).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR,
                                ingredientsList(
                                        Items.BONE_MEAL,8,
                                        TCOTS_Items.SEWANT_MUSHROOMS,3,
                                        Items.SPIDER_EYE,2,
                                        TCOTS_Items.REBIS, 1),
                                TCOTS_Items.DEVILS_PUFFBALL_ENHANCED).offerTo(exporter);
                    }

                    //Dragon's Dream
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DRAGONS_DREAM,
                                ingredientsList(Items.GLOWSTONE_DUST,4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DRAGONS_DREAM_ENHANCED,
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,2,
                                        Items.AMETHYST_SHARD,1,
                                        Items.OXEYE_DAISY,2,
                                        TCOTS_Items.BRYONIA, 2),
                                TCOTS_Items.DRAGONS_DREAM).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DRAGONS_DREAM_SUPERIOR,
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,4,
                                        Items.AMETHYST_SHARD,2,
                                        TCOTS_Items.BRYONIA,2,
                                        TCOTS_Items.AETHER, 1),
                                TCOTS_Items.DRAGONS_DREAM_ENHANCED).offerTo(exporter);
                    }

                    //Northern Wind
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.NORTHERN_WIND,
                                ingredientsList(
                                        TCOTS_Items.WATER_ESSENCE,1,
                                        Items.PRISMARINE_CRYSTALS,1,
                                        TCOTS_Items.ALLSPICE,2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.NORTHERN_WIND_ENHANCED,
                                ingredientsList(
                                        TCOTS_Items.WATER_ESSENCE,2,
                                        Items.PRISMARINE_CRYSTALS,1,
                                        TCOTS_Items.VERBENA,1,
                                        TCOTS_Items.ALLSPICE, 2),
                                TCOTS_Items.NORTHERN_WIND).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.create(
                                order, TCOTS_Items.NORTHERN_WIND_SUPERIOR,
                                AlchemyTableRecipeCategory.BOMBS_OILS,
                                ingredientsList(
                                        TCOTS_Items.WATER_ESSENCE,3,
                                        Items.PRISMARINE_CRYSTALS,2,
                                        TCOTS_Items.VERBENA,2,
                                        TCOTS_Items.ALLSPICE, 3,
                                        TCOTS_Items.QUEBRITH, 1),
                                TCOTS_Items.NORTHERN_WIND_ENHANCED).offerTo(exporter);
                    }

                    //Dimeritium Bomb
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.create(
                                order,
                                TCOTS_Items.DIMERITIUM_BOMB,
                                AlchemyTableRecipeCategory.BOMBS_OILS,
                                ingredientsList(Items.AMETHYST_SHARD,2),
                                new ItemStack(Items.GUNPOWDER, 5)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DIMERITIUM_BOMB_ENHANCED,
                                ingredientsList(
                                        Items.AMETHYST_SHARD,2,
                                        Items.PRISMARINE_CRYSTALS,2,
                                        Items.DANDELION,1,
                                        Items.CORNFLOWER, 3),
                                TCOTS_Items.DIMERITIUM_BOMB).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR,
                                ingredientsList(
                                        Items.AMETHYST_SHARD,2,
                                        Items.PRISMARINE_CRYSTALS,4,
                                        TCOTS_Items.PUFFBALL,2,
                                        TCOTS_Items.NIGREDO, 1),
                                TCOTS_Items.DIMERITIUM_BOMB_ENHANCED).offerTo(exporter);
                    }

                    //Moon dust Bomb
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.MOON_DUST,
                                ingredientsList(Items.GHAST_TEAR,2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.MOON_DUST_ENHANCED,
                                ingredientsList(
                                        Items.GHAST_TEAR,1,
                                        Items.BLAZE_POWDER,2,
                                        Items.BEETROOT,4,
                                        Items.HONEY_BOTTLE, 1),
                                TCOTS_Items.MOON_DUST).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.MOON_DUST_SUPERIOR,
                                ingredientsList(
                                        Items.GHAST_TEAR,2,
                                        Items.BLAZE_POWDER,4,
                                        Items.BEETROOT,6,
                                        TCOTS_Items.NIGREDO, 1),
                                TCOTS_Items.MOON_DUST_ENHANCED).offerTo(exporter);
                    }
                }

                //Monster Oils
                {
                    //Necrophage Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.NECROPHAGE_OIL,
                                ingredientsList(Items.DANDELION, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_NECROPHAGE_OIL,
                                ingredientsList(
                                        TCOTS_Items.ROTFIEND_BLOOD, 4,
                                        Items.DANDELION, 4,
                                        TCOTS_Items.ARENARIA, 4,
                                        Items.FLOWERING_AZALEA, 4), 4,
                                        TCOTS_Items.NECROPHAGE_OIL).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_NECROPHAGE_OIL,
                                ingredientsList(
                                        TCOTS_Items.ROTFIEND_BLOOD, 4,
                                        Items.CORNFLOWER, 1,
                                        TCOTS_Items.ARENARIA, 1,
                                        TCOTS_Items.HYDRAGENUM, 1), 5,
                                TCOTS_Items.ENHANCED_NECROPHAGE_OIL).offerTo(exporter);
                    }

                    //Ogroid Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.OGROID_OIL,
                                ingredientsList(Items.CORNFLOWER, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_OGROID_OIL,
                                ingredientsList(
                                        TCOTS_Items.CAVE_TROLL_LIVER, 2,
                                        Items.RED_MUSHROOM, 2,
                                        Items.FERN, 3,
                                        Items.CORNFLOWER, 4), 2,
                                TCOTS_Items.OGROID_OIL).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_OGROID_OIL,
                                ingredientsList(
                                        TCOTS_Items.CAVE_TROLL_LIVER, 3,
                                        TCOTS_Items.ARENARIA, 2,
                                        Items.FERN, 6,
                                        TCOTS_Items.AETHER, 1), 2,
                                TCOTS_Items.ENHANCED_OGROID_OIL).offerTo(exporter);
                    }

                    //Beast Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.BEAST_OIL,
                                ingredientsList(Items.BEEF, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_BEAST_OIL,
                                ingredientsList(
                                        Items.LEATHER, 2,
                                        TCOTS_Items.CELANDINE, 1,
                                        TCOTS_Items.PUFFBALL, 1,
                                        Items.BEETROOT, 4), 5,
                                TCOTS_Items.BEAST_OIL).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_BEAST_OIL,
                                ingredientsList(
                                        Items.ENDER_PEARL, 2,
                                        TCOTS_Items.CELANDINE, 1,
                                        TCOTS_Items.PUFFBALL, 1,
                                        TCOTS_Items.RUBEDO, 1), 2,
                                TCOTS_Items.ENHANCED_BEAST_OIL).offerTo(exporter);
                    }

                    //Hanged Man Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.HANGED_OIL,
                                ingredientsList(TCOTS_Items.ARENARIA, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_HANGED_OIL,
                                ingredientsList(
                                        TCOTS_Items.HAN_FIBER, 1,
                                        TCOTS_Items.NEKKER_EYE, 1,
                                        Items.AZURE_BLUET, 1,
                                        TCOTS_Items.ARENARIA, 1), 2,
                                TCOTS_Items.HANGED_OIL).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_HANGED_OIL,
                                ingredientsList(
                                        Items.MOSS_BLOCK, 1,
                                        TCOTS_Items.ROTFIEND_BLOOD, 2,
                                        Items.AZURE_BLUET, 1,
                                        TCOTS_Items.QUEBRITH, 1), 2,
                                TCOTS_Items.ENHANCED_HANGED_OIL).offerTo(exporter);
                    }
                }

                //Ingredients
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            new ItemStack(TCOTS_Items.DWARVEN_SPIRIT),
                            ingredientsList(
                                    TCOTS_Items.ICY_SPIRIT, 2,
                                    Items.LILY_OF_THE_VALLEY,1),
                            Items.GLASS_BOTTLE).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            new ItemStack(TCOTS_Items.ALCOHEST, 2),
                            ingredientsList(
                                    Items.SWEET_BERRIES, 2,
                                    TCOTS_Items.CHERRY_CORDIAL,1,
                                    TCOTS_Items.MANDRAKE_CORDIAL,1),
                            Items.GLASS_BOTTLE).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            new ItemStack(TCOTS_Items.WHITE_GULL),
                            ingredientsList(
                                    TCOTS_Items.ARENARIA, 1,
                                    TCOTS_Items.VILLAGE_HERBAL,1,
                                    TCOTS_Items.CHERRY_CORDIAL,1,
                                    TCOTS_Items.MANDRAKE_CORDIAL,1),
                            Items.GLASS_BOTTLE).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            new ItemStack(TCOTS_Items.STAMMELFORDS_DUST, 2),
                            ingredientsList(
                                    Items.BONE_MEAL, 8,
                                    Items.GUNPOWDER,4,
                                    Items.GLOWSTONE_DUST,4),
                            Items.BLAZE_POWDER).offerTo(exporter);


                    //Witcher Substances
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.AETHER,
                                ingredientsList(
                                        TCOTS_Items.VERBENA, 1,
                                        TCOTS_Items.ERGOT_SEEDS, 1,
                                        TCOTS_Items.HAN_FIBER, 1,
                                        TCOTS_Items.PUFFBALL, 1,
                                        Items.RED_MUSHROOM, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.HYDRAGENUM,
                                ingredientsList(
                                        TCOTS_Items.VERBENA, 1,
                                        TCOTS_Items.ERGOT_SEEDS, 1,
                                        Items.FERN, 1,
                                        Items.MOSS_BLOCK, 1,
                                        Items.GLOW_LICHEN, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.NIGREDO,
                                ingredientsList(
                                        TCOTS_Items.CROWS_EYE, 1,
                                        TCOTS_Items.HAN_FIBER, 1,
                                        Items.ALLIUM, 1,
                                        Items.GLOW_LICHEN, 1,
                                        Items.SWEET_BERRIES, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.QUEBRITH,
                                ingredientsList(
                                        TCOTS_Items.VERBENA, 1,
                                        TCOTS_Items.PUFFBALL, 1,
                                        Items.RED_MUSHROOM, 1,
                                        Items.GLOW_LICHEN, 1,
                                        Items.FLOWERING_AZALEA, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.REBIS,
                                ingredientsList(
                                        TCOTS_Items.VERBENA, 1,
                                        TCOTS_Items.ERGOT_SEEDS, 1,
                                        TCOTS_Items.ALLSPICE, 1,
                                        Items.OXEYE_DAISY, 1,
                                        Items.FERN, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.RUBEDO,
                                ingredientsList(
                                        TCOTS_Items.CROWS_EYE, 1,
                                        TCOTS_Items.HAN_FIBER, 1,
                                        Items.OXEYE_DAISY, 1,
                                        TCOTS_Items.PUFFBALL, 1,
                                        Items.MOSS_BLOCK, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.VERMILION,
                                ingredientsList(
                                        TCOTS_Items.VERBENA, 1,
                                        TCOTS_Items.ERGOT_SEEDS, 1,
                                        TCOTS_Items.HAN_FIBER, 1,
                                        Items.POPPY, 1,
                                        TCOTS_Items.BRYONIA, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.VITRIOL,
                                ingredientsList(
                                        Items.MOSS_BLOCK, 1,
                                        TCOTS_Items.ALLSPICE, 1,
                                        Items.FERN, 1,
                                        Items.ALLIUM, 1,
                                        Items.GLOW_LICHEN, 1)).offerTo(exporter);
                    }
                }
            }

            //Herbal Table
            {
                {
                    //Witcher Plants
                    {
                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.ARENARIA.getDefaultStack(),
                                        List.of(StatusEffects.STRENGTH, StatusEffects.WITHER), 20)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.BRYONIA.getDefaultStack(),
                                        List.of(StatusEffects.SATURATION, StatusEffects.INSTANT_DAMAGE))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.CELANDINE.getDefaultStack(),
                                        List.of(StatusEffects.REGENERATION, StatusEffects.SLOWNESS), 40, 2)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.VERBENA.getDefaultStack(),
                                        List.of(StatusEffects.HASTE, StatusEffects.SLOWNESS), 80, 3)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.HAN_FIBER.getDefaultStack(),
                                        List.of(StatusEffects.FIRE_RESISTANCE, StatusEffects.BLINDNESS))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.CROWS_EYE.getDefaultStack(),
                                        List.of(StatusEffects.SPEED, StatusEffects.POISON))
                                .offerTo(exporter);
                    }

                    //Mushrooms
                    {
                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.PUFFBALL.getDefaultStack(),
                                        List.of(StatusEffects.SATURATION, StatusEffects.NIGHT_VISION, StatusEffects.POISON))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.SEWANT_MUSHROOMS.getDefaultStack(),
                                        List.of(StatusEffects.SATURATION, StatusEffects.NIGHT_VISION, StatusEffects.POISON))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.RED_MUSHROOM.getDefaultStack(),
                                        List.of(StatusEffects.SATURATION, StatusEffects.NIGHT_VISION, StatusEffects.POISON))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.BROWN_MUSHROOM.getDefaultStack(),
                                        List.of(StatusEffects.SATURATION, StatusEffects.NIGHT_VISION, StatusEffects.POISON))
                                .offerTo(exporter);
                    }



                    //SuspiciousStew
                    {
                        HerbalTableRecipeJsonBuilder
                                .create(Items.ALLIUM.getDefaultStack(),
                                        List.of(StatusEffects.FIRE_RESISTANCE, StatusEffects.HUNGER), 40, 1)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.AZURE_BLUET.getDefaultStack(),
                                        List.of(StatusEffects.INVISIBILITY, StatusEffects.BLINDNESS), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.BLUE_ORCHID.getDefaultStack(),
                                        List.of(StatusEffects.SATURATION, StatusEffects.NAUSEA), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.DANDELION.getDefaultStack(),
                                        List.of(StatusEffects.SATURATION, StatusEffects.NAUSEA), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.CORNFLOWER.getDefaultStack(),
                                        List.of(StatusEffects.JUMP_BOOST, StatusEffects.SLOWNESS), 40, 3)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.LILY_OF_THE_VALLEY.getDefaultStack(),
                                        List.of(StatusEffects.WATER_BREATHING, StatusEffects.POISON), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.OXEYE_DAISY.getDefaultStack(),
                                        List.of(StatusEffects.REGENERATION, StatusEffects.WEAKNESS), 40,2)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.POPPY.getDefaultStack(),
                                        List.of(StatusEffects.NIGHT_VISION, StatusEffects.MINING_FATIGUE), 20)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.TORCHFLOWER.getDefaultStack(),
                                        List.of(StatusEffects.NIGHT_VISION, StatusEffects.MINING_FATIGUE), 20)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.ORANGE_TULIP.getDefaultStack(),
                                        List.of( StatusEffects.SPEED, StatusEffects.WEAKNESS))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.PINK_TULIP.getDefaultStack(),
                                        List.of(StatusEffects.SPEED, StatusEffects.WEAKNESS))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.RED_TULIP.getDefaultStack(),
                                        List.of(StatusEffects.SPEED, StatusEffects.WEAKNESS))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.WHITE_TULIP.getDefaultStack(),
                                        List.of(StatusEffects.SPEED, StatusEffects.WEAKNESS))
                                .offerTo(exporter);
                    }
                }
            }
        }

        private List<ItemStack> ingredientsList(Item itemA, int a){
            return List.of(new ItemStack(itemA, a));
        }

        private List<ItemStack> ingredientsList(Item itemA, int a, Item itemB, int b){
            return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b));
        }

        private List<ItemStack> ingredientsList(Item itemA, int a, Item itemB, int b, Item itemC, int c){
            return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b), new ItemStack(itemC, c));
        }

        private List<ItemStack> ingredientsList(Item itemA, int a, Item itemB, int b, Item itemC, int c, Item itemD, int d){
            return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b), new ItemStack(itemC, c), new ItemStack(itemD, d));
        }

        @SuppressWarnings("all")
        private List<ItemStack> ingredientsList(Item itemA, int a, Item itemB, int b, Item itemC, int c, Item itemD, int d, Item itemE, int e){
            return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b), new ItemStack(itemC, c), new ItemStack(itemD, d), new ItemStack(itemE, e));
        }
    }
    private static class AdvancementsGenerator extends FabricAdvancementProvider {

        //Good names for Advancements
        // Let's Cook!

        //Advancements:
        //Alchemy
        //x Craft the Alchemy Table (The Mother of all Sciences)
        //  x Create a potion (Strong Beverage)
        //      x Create a LV3 potion (Practicum in Advanced Alchemy)
        //      x Create a decoction (Taste of Monstrosity)
        //      x Achieve 100% toxicity (Can Quit Anytime I Want)
        //  x Create an Oil (Honing the Blade)
        //      x Create a LV3 Oil (A Powerful Wax)
        //      x Craft and use a Hanged Man's Venom (...Steel for Humans)
        //  x Create a bomb (Ka-boom!)
        //      x Create a LV3 bomb (Bombastic)
        //      x Destroy a nest using a bomb (Fire in the Hole)
        //          x Destroy 20 different nests (Pest Control)
        //      x Craft all the bombs (Bombardier)
        //      x Ignite a Dragon's Dream bomb using a burning opponent (That Is the Evilest Thing)
        //      x Use a moon dust on a creeper to disable its explosion forever (Successful Gardener)
        //  x Use an alchemy recipe (Let's Cook!)
        //  x Refill a concoction in the table or by sleeping (Deep Meditation)


        //Hunting
        //x Kill a monster (Silver for Monsters...)
        //  x Kill a Bullvore (Moo-rderer)
        //      x Craft the G'valchir (Won't Hurt a Bit)
        //  x Kill a rotfiend/scurver without causing an explosion (Bomb Defusal)
        //  x Get a mutagen (Mutagenic)
        //  x Befriend a troll (Friend of Trolls)
        //      x Befriend an ice troll (Lots eats, lots drink)
        //          > Get the three types of Trolls following you at the same time (Troll Trouble)

        //  > Craft crossbows bolts (Marksman)

        //  > Craft the Raven armor (Tyen'sail)
        //  > Craft a horse armor
        //  > Get Aerondight (Embodiment of the Five Virtues)


        protected AdvancementsGenerator(FabricDataOutput output) {
            super(output);
        }

        private void generateBasicRecipeAdvancement(String id, Consumer<AdvancementEntry> consumer){
            Advancement.Builder.createUntelemetered()
                    .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.ALCHEMY_TABLE_ITEM), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.ALCHEMY_TABLE_ITEM))
                    .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                    .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.ALCHEMY_BOOK), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.ALCHEMY_BOOK))
                    .rewards(AdvancementRewards.Builder.recipe(new Identifier(TCOTS_Main.MOD_ID,id)))
                    .parent(CraftingRecipeJsonBuilder.ROOT)
                    .build(consumer, new Identifier(TCOTS_Main.MOD_ID,"recipes/alchemy")+"/"+id);

        }

        @Override
        public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
            //Start recipes
            this.generateBasicRecipeAdvancement("swallow_potion", consumer);
            this.generateBasicRecipeAdvancement("cat_potion", consumer);

            this.generateBasicRecipeAdvancement("samum", consumer);
            this.generateBasicRecipeAdvancement("grapeshot", consumer);

            this.generateBasicRecipeAdvancement("oil_necrophage", consumer);
            this.generateBasicRecipeAdvancement("oil_specter", consumer);

            this.generateBasicRecipeAdvancement("dwarven_spirit", consumer);
            this.generateBasicRecipeAdvancement("alcohest", consumer);


            AdvancementEntry rootAdvancementWitcher = Advancement.Builder.create()
                    .display(
                            TCOTS_Items.WITCHER_BESTIARY, // The display icon
                            Text.translatable("advancements.witcher.main.title"), // The title
                            Text.translatable("advancements.witcher.main.description"), // The description
                            new Identifier("textures/gui/advancements/backgrounds/stone.png"), // Background image used
                            AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                            false, // Show toast top right
                            false, // Announce to chat
                            false // Hidden in the advancement tab
                    )
                    // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                    .criterion("start_mod", InventoryChangedCriterion.Conditions.items(Blocks.CRAFTING_TABLE))
                    .build(consumer, TCOTS_Main.MOD_ID + "/root");

            //Hunting Branch
            {
                AdvancementEntry rootHunting =
                        requireListedMobsKilled(Advancement.Builder.create(), MONSTERS)
                                .parent(rootAdvancementWitcher)
                                .display(TCOTS_Items.GRAVEIR_BONE, // The display icon
                                        Text.translatable("advancements.witcher.start_killing.title"), // The title
                                        Text.translatable("advancements.witcher.start_killing.description"), // The description
                                        null,
                                        AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                        true, // Show toast top right
                                        true, // Announce to chat
                                        false // Hidden in the advancement tab
                                )
                                .build(consumer, TCOTS_Main.MOD_ID + "/hunting");

                //Bullvore branch
                {
                    AdvancementEntry killBullvore =
                            Advancement.Builder.create()
                                    .parent(rootHunting)
                                    .display(
                                            TCOTS_Items.BULLVORE_HORN_FRAGMENT, // The display icon
                                            Text.translatable("advancements.witcher.kill_bullvore.title"), // The title
                                            Text.translatable("advancements.witcher.kill_bullvore.description"), // The description
                                            null,
                                            AdvancementFrame.GOAL, // Options: TASK, CHALLENGE, GOAL
                                            true, // Show toast top right
                                            true, // Announce to chat
                                            false // Hidden in the advancement tab
                                    )
                                    .criterion("kill_bullvore", OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(TCOTS_Entities.BULLVORE)))
                                    .build(consumer, TCOTS_Main.MOD_ID + "/kill_bullvore");

                    Advancement.Builder.create()
                            .parent(killBullvore)
                            .display(
                                    TCOTS_Items.GVALCHIR, // The display icon
                                    Text.translatable("advancements.witcher.get_gvalchir.title"), // The title
                                    Text.translatable("advancements.witcher.get_gvalchir.description"), // The description
                                    null,
                                    AdvancementFrame.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion("get_gvalchir", InventoryChangedCriterion.Conditions.items(TCOTS_Items.GVALCHIR))
                            .rewards(AdvancementRewards.Builder.experience(100))
                            .build(consumer, TCOTS_Main.MOD_ID + "/get_gvalchir");
                }


                Advancement.Builder.create()
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.ROTFIEND_BLOOD, // The display icon
                                Text.translatable("advancements.witcher.kill_rotfiend.title"), // The title
                                Text.translatable("advancements.witcher.kill_rotfiend.description"), // The description
                                null,
                                AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .criterion("kill_rotfiend", TCOTS_CustomCriterion.Conditions.createKillRotfiendCriterion())
                        .build(consumer, TCOTS_Main.MOD_ID + "/kill_rotfiend");

                setHasItemCriteriaOR(Advancement.Builder.create(), MUTAGEN)
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.FOGLET_MUTAGEN, // The display icon
                                Text.translatable("advancements.witcher.get_mutagen.title"), // The title
                                Text.translatable("advancements.witcher.get_mutagen.description"), // The description
                                null,
                                AdvancementFrame.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .build(consumer, TCOTS_Main.MOD_ID + "/get_mutagen");

                //Troll branch
                {
                    AdvancementEntry befriendTroll =Advancement.Builder.create()
                            .parent(rootHunting)
                            .display(
                                    TCOTS_Items.VILLAGE_HERBAL, // The display icon
                                    Text.translatable("advancements.witcher.befriend_troll.title"), // The title
                                    Text.translatable("advancements.witcher.befriend_troll.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion("befriend_rock", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.create().type(TCOTS_Entities.ROCK_TROLL)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                            .criterion("befriend_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.create().type(TCOTS_Entities.ICE_TROLL)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.OR)
                            .criterion("befriend_forest", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.create().type(TCOTS_Entities.FOREST_TROLL)))
                            .build(consumer, TCOTS_Main.MOD_ID + "/befriend_troll");

                    AdvancementEntry befriendIceTroll = Advancement.Builder.create()
                            .parent(befriendTroll)
                            .display(
                                    Blocks.PACKED_ICE, // The display icon
                                    Text.translatable("advancements.witcher.befriend_troll_ice.title"), // The title
                                    Text.translatable("advancements.witcher.befriend_troll_ice.description"), // The description
                                    null,
                                    AdvancementFrame.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion("befriend_troll_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.create().type(TCOTS_Entities.ICE_TROLL)))
                            .build(consumer, TCOTS_Main.MOD_ID + "/befriend_troll_ice");

                    Advancement.Builder.create()
                            .parent(befriendIceTroll)
                            .display(
                                    Blocks.OAK_SAPLING, // The display icon
                                    Text.translatable("advancements.witcher.befriend_all_troll.title"), // The title
                                    Text.translatable("advancements.witcher.befriend_all_troll.description"), // The description
                                    null,
                                    AdvancementFrame.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion("befriend_rock", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.create().type(TCOTS_Entities.ROCK_TROLL)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.AND)
                            .criterion("befriend_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.create().type(TCOTS_Entities.ICE_TROLL)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.AND)
                            .criterion("befriend_forest", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.create().type(TCOTS_Entities.FOREST_TROLL)))
                            .build(consumer, TCOTS_Main.MOD_ID + "/befriend_all_troll");
                }

                Advancement.Builder.create()
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.RAVENS_ARMOR, // The display icon
                                Text.translatable("advancements.witcher.get_ravens_armor.title"), // The title
                                Text.translatable("advancements.witcher.get_ravens_armor.description"), // The description
                                null,
                                AdvancementFrame.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .criterion("get_ravens_armor", InventoryChangedCriterion.Conditions.items(TCOTS_Items.RAVENS_ARMOR, TCOTS_Items.RAVENS_TROUSERS, TCOTS_Items.RAVENS_BOOTS))
                        .rewards(AdvancementRewards.Builder.experience(100))
                        .build(consumer, TCOTS_Main.MOD_ID + "/get_ravens_armor");
            }


            //Alchemy Branch
            {
                AdvancementEntry rootAlchemy =
                        Advancement.Builder.create()
                                .parent(rootAdvancementWitcher)
                                .display(
                                        TCOTS_Items.ALCHEMY_TABLE_ITEM, // The display icon
                                        Text.translatable("advancements.witcher.start_alchemy.title"), // The title
                                        Text.translatable("advancements.witcher.start_alchemy.description"), // The description
                                        null,
                                        AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                        true, // Show toast top right
                                        true, // Announce to chat
                                        false // Hidden in the advancement tab
                                )
                                .criterion("start_alchemy", InventoryChangedCriterion.Conditions.items(TCOTS_Blocks.ALCHEMY_TABLE))
                                .build(consumer, TCOTS_Main.MOD_ID + "/alchemy");

                //Potions branch
                {
                    AdvancementEntry craftPotion = setHasItemCriteriaOR(Advancement.Builder.create(), POTIONS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.SWALLOW_POTION, // The display icon
                                    Text.translatable("advancements.witcher.craft_potion.title"), // The title
                                    Text.translatable("advancements.witcher.craft_potion.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_potion");


                    setHasItemCriteriaOR(Advancement.Builder.create(), DECOCTIONS)
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.GRAVE_HAG_DECOCTION, // The display icon
                                    Text.translatable("advancements.witcher.craft_decoction.title"), // The title
                                    Text.translatable("advancements.witcher.craft_decoction.description"), // The description
                                    null,
                                    AdvancementFrame.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_decoction");

                    setHasItemCriteriaOR(Advancement.Builder.create(), POTION_LV3)
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.SWALLOW_POTION_SUPERIOR, // The display icon
                                    Text.translatable("advancements.witcher.craft_potion_superior.title"), // The title
                                    Text.translatable("advancements.witcher.craft_potion_superior.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_potion_superior");

                    Advancement.Builder.create()
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.NEST_SKULL_ITEM, // The display icon
                                    Text.translatable("advancements.witcher.max_toxicity.title"), // The title
                                    Text.translatable("advancements.witcher.max_toxicity.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion(
                                    "max_toxicity",
                                    TCOTS_CustomCriterion.Conditions.createMaxToxicityCriterion())
                            .build(consumer, TCOTS_Main.MOD_ID + "/max_toxicity");
                }

                //Oils branch
                {
                    AdvancementEntry craftOil = setHasItemCriteriaOR(Advancement.Builder.create(), OILS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.NECROPHAGE_OIL, // The display icon
                                    Text.translatable("advancements.witcher.craft_oil.title"), // The title
                                    Text.translatable("advancements.witcher.craft_oil.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_oil");

                    setHasItemCriteriaOR(Advancement.Builder.create(), OILS_LV3)
                            .parent(craftOil)
                            .display(
                                    TCOTS_Items.SUPERIOR_NECROPHAGE_OIL, // The display icon
                                    Text.translatable("advancements.witcher.craft_oil_superior.title"), // The title
                                    Text.translatable("advancements.witcher.craft_oil_superior.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_oil_superior");

                    Advancement.Builder.create()
                            .parent(craftOil)
                            .display(
                                    Items.IRON_SWORD, // The display icon
                                    Text.translatable("advancements.witcher.kill_with_hanged.title"), // The title
                                    Text.translatable("advancements.witcher.kill_with_hanged.description"), // The description
                                    null,
                                    AdvancementFrame.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion(
                                    "kill_with_hanged",
                                    TCOTS_CustomCriterion.Conditions.createKillWithHangedCriterion())
                            .build(consumer, TCOTS_Main.MOD_ID + "/kill_with_hanged");
                }

                //Bombs branch
                {
                    AdvancementEntry craftBomb = setHasItemCriteriaOR(Advancement.Builder.create(), BOMBS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.GRAPESHOT, // The display icon
                                    Text.translatable("advancements.witcher.craft_bomb.title"), // The title
                                    Text.translatable("advancements.witcher.craft_bomb.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_bomb");

                    AdvancementEntry craftBombLV3 = setHasItemCriteriaOR(Advancement.Builder.create(), BOMBS_LV3)
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.GRAPESHOT_SUPERIOR, // The display icon
                                    Text.translatable("advancements.witcher.craft_bomb_superior.title"), // The title
                                    Text.translatable("advancements.witcher.craft_bomb_superior.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_bomb_superior");

                    Advancement.Builder.create()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.DIMERITIUM_BOMB, // The display icon
                                    Text.translatable("advancements.witcher.craft_all_bomb.title"), // The title
                                    Text.translatable("advancements.witcher.craft_all_bomb.description"), // The description
                                    null,
                                    AdvancementFrame.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )

                            .criterion(
                                    "craft_grapeshot",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.GRAPESHOT)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.AND)
                            .criterion(
                                    "craft_samum",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.SAMUM)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.AND)
                            .criterion(
                                    "craft_dancing",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.DANCING_STAR)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.AND)
                            .criterion(
                                    "craft_puffball",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.DEVILS_PUFFBALL)))
                            .criteriaMerger(AdvancementRequirements.CriterionMerger.AND)
                            .criterion(
                                    "craft_dragons",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.DRAGONS_DREAM)))
                            .criterion(
                                    "craft_northern",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.NORTHERN_WIND)))
                            .criterion(
                                    "craft_dimeritium",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.DIMERITIUM_BOMB)))
                            .criterion(
                                    "craft_moon_dust",
                                    RecipeCraftedCriterion.Conditions.create(Registries.ITEM.getId(TCOTS_Items.MOON_DUST)))
                            .build(consumer, TCOTS_Main.MOD_ID + "/craft_all_bomb");

                    AdvancementEntry explodeNest = Advancement.Builder.create()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Blocks.MONSTER_NEST, // The display icon
                                    Text.translatable("advancements.witcher.destroy_nest.title"), // The title
                                    Text.translatable("advancements.witcher.destroy_nest.description"), // The description
                                    null,
                                    AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion(
                                    "destroy_nest",
                                    TCOTS_CustomCriterion.Conditions.createDestroyNestCriterion())
                            .build(consumer, TCOTS_Main.MOD_ID + "/destroy_nest");

                    Advancement.Builder.create()
                            .parent(explodeNest)
                            .display(
                                    TCOTS_Blocks.MONSTER_NEST, // The display icon
                                    Text.translatable("advancements.witcher.destroy_nest_multiple.title"), // The title
                                    Text.translatable("advancements.witcher.destroy_nest_multiple.description"), // The description
                                    null,
                                    AdvancementFrame.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion(
                                    "destroy_nest_multiple",
                                    DestroyMultipleMonsterNestsCriterion.Conditions.createMultipleDestroyNestCriterion(30))
                            .rewards(AdvancementRewards.Builder.experience(500))
                            .build(consumer, TCOTS_Main.MOD_ID + "/destroy_nest_multiple");

                    Advancement.Builder.create()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.DRAGONS_DREAM_SUPERIOR, // The display icon
                                    Text.translatable("advancements.witcher.dragons_dream_burning.title"), // The title
                                    Text.translatable("advancements.witcher.dragons_dream_burning.description"), // The description
                                    null,
                                    AdvancementFrame.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion(
                                    "dragons_dream_burning",
                                    TCOTS_CustomCriterion.Conditions.createDragonsDreamBurningCriterion())
                            .rewards(AdvancementRewards.Builder.experience(150))
                            .build(consumer, TCOTS_Main.MOD_ID + "/dragons_dream_burning");

                    Advancement.Builder.create()
                            .parent(craftBombLV3)
                            .display(
                                    TCOTS_Items.MOON_DUST_SUPERIOR, // The display icon
                                    Text.translatable("advancements.witcher.stop_creeper.title"), // The title
                                    Text.translatable("advancements.witcher.stop_creeper.description"), // The description
                                    null,
                                    AdvancementFrame.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .criterion(
                                    "stop_creeper",
                                    TCOTS_CustomCriterion.Conditions.createStopCreeperCriterion())
                            .rewards(AdvancementRewards.Builder.experience(150))
                            .build(consumer, TCOTS_Main.MOD_ID + "/stop_creeper");
                }

                AdvancementEntry useAlchemyFormula = Advancement.Builder.create()
                        .parent(rootAlchemy)
                        .display(
                                TCOTS_Items.ALCHEMY_FORMULA, // The display icon
                                Text.translatable("advancements.witcher.use_formula.title"), // The title
                                Text.translatable("advancements.witcher.use_formula.description"), // The description
                                null, // Background image used
                                AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .criterion(
                                "use_formula",
                                ConsumeItemCriterion.Conditions.item(TCOTS_Items.ALCHEMY_FORMULA))
                        .build(consumer, TCOTS_Main.MOD_ID + "/use_formula");

                Advancement.Builder.create()
                        .parent(rootAlchemy)
                        .display(
                                TCOTS_Items.ALCOHEST, // The display icon
                                Text.translatable("advancements.witcher.refill_concoction.title"), // The title
                                Text.translatable("advancements.witcher.refill_concoction.description"), // The description
                                null, // Background image used
                                AdvancementFrame.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .criterion(
                                "refill_concoction",
                                TCOTS_CustomCriterion.Conditions.createRefillConcoctionCriterion())
                        .build(consumer, TCOTS_Main.MOD_ID + "/refill_concoction");
            }
        }

        private static Advancement.Builder requireAllFrogsOnLeads(Advancement.Builder builder) {
            Registries.FROG_VARIANT.streamEntries().forEach(variant ->
                    builder.criterion(variant.registryKey().getValue().toString(),
                            PlayerInteractedWithEntityCriterion.Conditions.create(ItemPredicate.Builder.create().items(Items.LEAD),
                                    Optional.of(EntityPredicate.contextPredicateFromEntityPredicate(EntityPredicate.Builder.create()
                                            .type(EntityType.FROG).typeSpecific(TypeSpecificPredicate.frog(variant.value())))))));
            return builder;
        }

        protected static final List<Item> MUTAGEN = Arrays.asList(
                TCOTS_Items.FOGLET_MUTAGEN,
                TCOTS_Items.TROLL_MUTAGEN,
                TCOTS_Items.GRAVE_HAG_MUTAGEN,

                TCOTS_Items.WATER_HAG_MUTAGEN,
                TCOTS_Items.NEKKER_WARRIOR_MUTAGEN
        );

        protected static final List<Item> BOMBS_LV3 = Arrays.asList(
                TCOTS_Items.GRAPESHOT_SUPERIOR,

                TCOTS_Items.DANCING_STAR_SUPERIOR,

                TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR,

                TCOTS_Items.SAMUM_SUPERIOR,

                TCOTS_Items.NORTHERN_WIND_SUPERIOR,

                TCOTS_Items.DRAGONS_DREAM_SUPERIOR,

                TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR,

                TCOTS_Items.MOON_DUST_SUPERIOR
        );

        protected static final List<Item> BOMBS = Arrays.asList(
        TCOTS_Items.GRAPESHOT,
        TCOTS_Items.GRAPESHOT_ENHANCED,
        TCOTS_Items.GRAPESHOT_SUPERIOR,

        TCOTS_Items.DANCING_STAR,
        TCOTS_Items.DANCING_STAR_ENHANCED,
        TCOTS_Items.DANCING_STAR_SUPERIOR,

        TCOTS_Items.DEVILS_PUFFBALL,
        TCOTS_Items.DEVILS_PUFFBALL_ENHANCED,
        TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR,

        TCOTS_Items.SAMUM,
        TCOTS_Items.SAMUM_ENHANCED,
        TCOTS_Items.SAMUM_SUPERIOR,

        TCOTS_Items.NORTHERN_WIND,
        TCOTS_Items.NORTHERN_WIND_ENHANCED,
        TCOTS_Items.NORTHERN_WIND_SUPERIOR,

        TCOTS_Items.DRAGONS_DREAM,
        TCOTS_Items.DRAGONS_DREAM_ENHANCED,
        TCOTS_Items.DRAGONS_DREAM_SUPERIOR,

        TCOTS_Items.DIMERITIUM_BOMB,
        TCOTS_Items.DIMERITIUM_BOMB_ENHANCED,
        TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR,

        TCOTS_Items.MOON_DUST,
        TCOTS_Items.MOON_DUST_ENHANCED,
        TCOTS_Items.MOON_DUST_SUPERIOR
        );

        protected static final List<Item> OILS_LV3 = Arrays.asList(
                TCOTS_Items.SUPERIOR_NECROPHAGE_OIL,

                TCOTS_Items.SUPERIOR_OGROID_OIL,

                TCOTS_Items.SUPERIOR_BEAST_OIL,

                TCOTS_Items.SUPERIOR_HANGED_OIL
        );

        protected static final List<Item> OILS = Arrays.asList(
                TCOTS_Items.NECROPHAGE_OIL,
                TCOTS_Items.ENHANCED_NECROPHAGE_OIL,
                TCOTS_Items.SUPERIOR_NECROPHAGE_OIL,

                TCOTS_Items.OGROID_OIL,
                TCOTS_Items.ENHANCED_OGROID_OIL,
                TCOTS_Items.SUPERIOR_OGROID_OIL,

                TCOTS_Items.BEAST_OIL,
                TCOTS_Items.ENHANCED_BEAST_OIL,
                TCOTS_Items.SUPERIOR_BEAST_OIL,

                TCOTS_Items.HANGED_OIL,
                TCOTS_Items.ENHANCED_HANGED_OIL,
                TCOTS_Items.SUPERIOR_HANGED_OIL
        );

        protected static final List<EntityType<?>> MONSTERS = Arrays.asList(
                TCOTS_Entities.DROWNER,
                TCOTS_Entities.ROTFIEND,
                TCOTS_Entities.FOGLET,
                TCOTS_Entities.GRAVE_HAG,
                TCOTS_Entities.WATER_HAG,
                TCOTS_Entities.GHOUL,
                TCOTS_Entities.ALGHOUL,
                TCOTS_Entities.SCURVER,
                TCOTS_Entities.DEVOURER,
                TCOTS_Entities.GRAVEIR,
                TCOTS_Entities.BULLVORE,

                TCOTS_Entities.NEKKER,
                TCOTS_Entities.NEKKER_WARRIOR,
                TCOTS_Entities.CYCLOPS,
                TCOTS_Entities.ROCK_TROLL,
                TCOTS_Entities.ICE_TROLL,
                TCOTS_Entities.FOREST_TROLL
        );

        protected static final List<Item> POTION_LV3= Arrays.asList(
                TCOTS_Items.SWALLOW_POTION_SUPERIOR,
                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR,
                TCOTS_Items.CAT_POTION_SUPERIOR,
                TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR,
                TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR,
                TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR,
                TCOTS_Items.WOLF_POTION_SUPERIOR,
                TCOTS_Items.ROOK_POTION_SUPERIOR
        );

        protected static final List<Item> POTIONS = Arrays.asList(
                TCOTS_Items.SWALLOW_POTION,
                TCOTS_Items.SWALLOW_POTION_ENHANCED,
                TCOTS_Items.SWALLOW_POTION_SUPERIOR,

                TCOTS_Items.WHITE_RAFFARDS_DECOCTION,
                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED,
                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR,

                TCOTS_Items.CAT_POTION,
                TCOTS_Items.CAT_POTION_ENHANCED,
                TCOTS_Items.CAT_POTION_SUPERIOR,

                TCOTS_Items.BLACK_BLOOD_POTION,
                TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED,
                TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR,

                TCOTS_Items.MARIBOR_FOREST_POTION,
                TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED,
                TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR,

                TCOTS_Items.KILLER_WHALE_POTION,

                TCOTS_Items.WHITE_HONEY_POTION,
                TCOTS_Items.WHITE_HONEY_POTION_ENHANCED,
                TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR,

                TCOTS_Items.WOLF_POTION,
                TCOTS_Items.WOLF_POTION_ENHANCED,
                TCOTS_Items.WOLF_POTION_SUPERIOR,

                TCOTS_Items.ROOK_POTION,
                TCOTS_Items.ROOK_POTION_ENHANCED,
                TCOTS_Items.ROOK_POTION_SUPERIOR,

                TCOTS_Items.ALGHOUL_DECOCTION,
                TCOTS_Items.GRAVE_HAG_DECOCTION,
                TCOTS_Items.WATER_HAG_DECOCTION,
                TCOTS_Items.FOGLET_DECOCTION,
                TCOTS_Items.TROLL_DECOCTION,
                TCOTS_Items.NEKKER_WARRIOR_DECOCTION
        );

        protected static final List<Item> DECOCTIONS = Arrays.asList(
                TCOTS_Items.ALGHOUL_DECOCTION,
                TCOTS_Items.GRAVE_HAG_DECOCTION,
                TCOTS_Items.WATER_HAG_DECOCTION,
                TCOTS_Items.FOGLET_DECOCTION,
                TCOTS_Items.TROLL_DECOCTION,
                TCOTS_Items.NEKKER_WARRIOR_DECOCTION
        );

        private static Advancement.Builder requireListedMobsKilled(Advancement.Builder builder, List<EntityType<?>> entityTypes) {
            entityTypes.forEach(type -> builder.criterion(
                    Registries.ENTITY_TYPE.getId(type).toString(),
                    OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(type)))
                    .criteriaMerger(AdvancementRequirements.CriterionMerger.OR));
            return builder;
        }

        private static Advancement.Builder setHasItemCriteriaOR(Advancement.Builder builder, List<Item> entityTypes) {
            entityTypes.forEach(item -> builder.criterion(
                            Registries.ITEM.getId(item).toString(),
                            InventoryChangedCriterion.Conditions.items(item))
                    .criteriaMerger(AdvancementRequirements.CriterionMerger.OR));
            return builder;
        }

        private static Advancement.Builder requireMultipleNests(Advancement.Builder builder) {
            List<Pair<String, AdvancementCriterion<PlayerGeneratesContainerLootCriterion.Conditions>>> list =

                    List.of(Pair.of("desert_pyramid", PlayerGeneratesContainerLootCriterion.Conditions.create(LootTables.DESERT_PYRAMID_ARCHAEOLOGY)),
                            Pair.of("desert_well", PlayerGeneratesContainerLootCriterion.Conditions.create(LootTables.DESERT_WELL_ARCHAEOLOGY)),
                            Pair.of("ocean_ruin_cold", PlayerGeneratesContainerLootCriterion.Conditions.create(LootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)),
                            Pair.of("ocean_ruin_warm", PlayerGeneratesContainerLootCriterion.Conditions.create(LootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)),
                            Pair.of("trail_ruins_rare", PlayerGeneratesContainerLootCriterion.Conditions.create(LootTables.TRAIL_RUINS_RARE_ARCHAEOLOGY)),
                            Pair.of("trail_ruins_common", PlayerGeneratesContainerLootCriterion.Conditions.create(LootTables.TRAIL_RUINS_COMMON_ARCHAEOLOGY)));

            list.forEach(pair -> builder.criterion(pair.getFirst(), pair.getSecond()));
            String string = "has_sherd";
            builder.criterion("has_sherd", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().tag(ItemTags.DECORATED_POT_SHERDS)));

            builder.requirements(new AdvancementRequirements(List.of(list.stream().map(Pair::getFirst).toList(), List.of("has_sherd"))));
            return builder;
        }
    }
}



package TCOTS;

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
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementRequirements;
import net.minecraft.advancement.AdvancementRewards;
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
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TCOTS_DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack main = fabricDataGenerator.createPack();

        main.addProvider(ModWorldGenerator::new);
        main.addProvider(LootTablesHerbalistGenerator::new);
        main.addProvider(POIProvider::new);
        main.addProvider(DamageTypeTagsGenerator::new);
        main.addProvider(BlockTagsGenerator::new);
        main.addProvider(EntityTagGenerator::new);
        main.addProvider(ItemTagGenerator::new);
        main.addProvider(RecipesGenerator::new);
        main.addProvider(AdvancementsRecipesUnlocker::new);
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

    private static class LootTablesHerbalistGenerator extends SimpleFabricLootTableProvider {

        public static final Identifier PLAINS_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/plains_herbalist");
        public static final Identifier TAIGA_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/taiga_herbalist");
        public static final Identifier SNOWY_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/snowy_herbalist");
        public static final Identifier DESERT_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/desert_herbalist");
        public static final Identifier SAVANNA_HERBALIST_CHEST = new Identifier(TCOTS_Main.MOD_ID, "chests/village/savanna_herbalist");

        public LootTablesHerbalistGenerator(FabricDataOutput output) {
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
                            .pattern("HIH")
                            .pattern("HIH")
                            .pattern("NSN")
                            .input('H', TCOTS_Items.BULLVORE_HORN_FRAGMENT)
                            .input('I', Items.IRON_INGOT)
                            .input('N', Items.IRON_NUGGET)
                            .input('S', Items.STICK)

                            .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.BULLVORE_HORN_FRAGMENT), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.BULLVORE_HORN_FRAGMENT))
                            .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
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
                    //Leather
                    {
                        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.LEATHER, 2)
                                .input(TCOTS_Items.CADAVERINE)
                                .input(Items.ROTTEN_FLESH)
                                .group("leather")

                                .criterion(FabricRecipeProvider.hasItem(TCOTS_Items.CADAVERINE), FabricRecipeProvider.conditionsFromItem(TCOTS_Items.CADAVERINE))
                                .offerTo(exporter, new Identifier(TCOTS_Main.MOD_ID, "cadaverine_decay_rotten"));
                    }

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
                        //TODO: Fix recipes when added new drops (Cave troll liver)
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_OGROID_OIL,
                                ingredientsList(
                                        TCOTS_Items.ROTFIEND_BLOOD, 4,
                                        Items.DANDELION, 4,
                                        TCOTS_Items.ARENARIA, 4,
                                        Items.FLOWERING_AZALEA, 4), 4,
                                TCOTS_Items.OGROID_OIL).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_OGROID_OIL,
                                ingredientsList(
                                        TCOTS_Items.ROTFIEND_BLOOD, 4,
                                        Items.CORNFLOWER, 1,
                                        TCOTS_Items.ARENARIA, 1,
                                        TCOTS_Items.HYDRAGENUM, 1), 5,
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
    private static class AdvancementsRecipesUnlocker extends FabricAdvancementProvider {

        protected AdvancementsRecipesUnlocker(FabricDataOutput output) {
            super(output);
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
    }
}



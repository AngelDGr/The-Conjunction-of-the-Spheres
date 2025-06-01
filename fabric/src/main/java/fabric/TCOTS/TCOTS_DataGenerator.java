package fabric.TCOTS;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Tags;
import TCOTS.advancements.criterion.DestroyMultipleMonsterNestsCriterion;
import TCOTS.advancements.criterion.GetTrollFollowerCriterion;
import TCOTS.advancements.criterion.TCOTS_CustomCriterion;

import TCOTS.registry.*;
import TCOTS.blocks.plants.SewantMushroomsPlant;
import TCOTS.items.AlchemyRecipeRandomlyLootFunction;
import TCOTS.recipes.AlchemyTableRecipeCategory;
import TCOTS.recipes.AlchemyTableRecipeJsonBuilder;
import TCOTS.recipes.HerbalTableRecipeJsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LootTableTrigger;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@SuppressWarnings({"unused", "deprecated"})
public class TCOTS_DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack main = fabricDataGenerator.createPack();

//        main.addProvider(ModWorldGenerator::new);
//        main.addProvider(LootTablesChestsGenerator::new);
//        main.addProvider(LootTablesEntitiesGenerator::new);
//        main.addProvider(LootTablesBlocksGenerator::new);
//        main.addProvider(POIProvider::new);
//        main.addProvider(DamageTypeTagsGenerator::new);
//        main.addProvider(BlockTagsGenerator::new);
//        main.addProvider(EntityTagGenerator::new);
//        main.addProvider(ItemTagGenerator::new);
        main.addProvider(BiomeTagGenerator::new);
//        main.addProvider(RecipesGenerator::new);
//        main.addProvider(AdvancementsGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
//        registryBuilder.add(Registries.CONFIGURED_FEATURE, TCOTS_WorldGen::boostrapConfiguredFeature);
//        registryBuilder.add(Registries.PLACED_FEATURE, TCOTS_WorldGen::boostrapPlacedFeature);
//        registryBuilder.add(Registries.PROCESSOR_LIST, TCOTS_WorldGen::boostrapProcessorList);
//        registryBuilder.add(Registries.DAMAGE_TYPE, TCOTS_DamageTypes::boostrapDamageTypes);
//        registryBuilder.add(Registries.DAMAGE_TYPE, TCOTS_DamageTypes::boostrapDamageTypes);
    }

    public static class ModWorldGenerator extends FabricDynamicRegistryProvider {
        public ModWorldGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(HolderLookup.Provider registries, Entries entries) {
            entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));
            entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
            entries.addAll(registries.lookupOrThrow(Registries.PROCESSOR_LIST));
        }

        @Override
        public @NotNull String getName() {
            return "World Gen";
        }
    }
    private static class LootTablesEntitiesGenerator extends SimpleFabricLootTableProvider{
        private final CompletableFuture<HolderLookup.Provider> registryLookup;
        public LootTablesEntitiesGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup, LootContextParamSets.ENTITY);

            this.registryLookup=registryLookup;
        }

        @Override
        public void generate(@NotNull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
            generate(registryLookup.join(), (type, builder) -> lootTableBiConsumer.accept(ResourceKey.create(Registries.LOOT_TABLE, EntityType.getKey(type).withPrefix("entities/")), builder));
        }

        @SuppressWarnings("all")
        protected LootPool.Builder cadaverinePool(HolderLookup.Provider registryLookup, int min, int max, float baseProbability, float lootingProbability){
            return LootPool.lootPool().setRolls(UniformGenerator.between(0,1))
                    .add(LootItem.lootTableItem(TCOTS_Items.CADAVERINE.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2)))
                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup, UniformGenerator.between(0,1)))
                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup, baseProbability, lootingProbability))
                    );
        }

        protected LootPool.Builder mutagenPool(HolderLookup.Provider registryLookup, Item mutagen, float lootingProbability){
            return LootPool.lootPool().setRolls(UniformGenerator.between(1, 0))
                    .add(LootItem.lootTableItem(mutagen)
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())
                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                    0.0f, lootingProbability))
                    );
        }

        protected LootPool.Builder mutagenPool(HolderLookup.Provider registryLookup, Item mutagen){
            return mutagenPool(registryLookup, mutagen, 0.1f);
        }

        protected ResourceLocation getRandomSequence(String id){
            return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "entities/"+id);
        }

        protected void generate(HolderLookup.Provider registryLookup, BiConsumer<EntityType<?>, LootTable.Builder> exporter) {

            //Necrophages
            {
                //Drowner
                {
                    exporter.accept(TCOTS_Entities.Drowner(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.DROWNER_TONGUE.get())
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup, UniformGenerator.between(0.0F, 1.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.75f, 0.1f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.DROWNER_BRAIN.get())
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.3f, 0.2f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.WATER_ESSENCE.get())
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.1f, 0.05f))
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 4, 0.3f, 0.1f))
                                    .setRandomSequence(getRandomSequence("drowner"))
                    );
                }

                //Ghoul
                {
                    exporter.accept(TCOTS_Entities.Ghoul(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.GHOUL_BLOOD.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 1.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.3f, 0.2f))
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 3, 0.6f, 0.1f))
                                    .setRandomSequence(getRandomSequence("ghoul"))
                    );
                }

                //Alghoul
                {
                    exporter.accept(TCOTS_Entities.Alghoul(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.ALGHOUL_BONE_MARROW.get())
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.1f, 0.1f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.GHOUL_BLOOD.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.4f, 0.2f))
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 2, 0.4f, 0.1f))
                                    .setRandomSequence(getRandomSequence("alghoul"))
                    );
                }

                //Rotfiend
                {
                    exporter.accept(TCOTS_Entities.Rotfiend(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.ROTFIEND_BLOOD.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 2.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.4f, 0.2f))
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 4, 0.5f, 0.1f))
                                    .setRandomSequence(getRandomSequence("rotfiend"))
                    );
                }

                //Foglet
                {
                    exporter.accept(TCOTS_Entities.Foglet(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.FOGLET_TEETH.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 2.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.7f, 0.1f))
                                            )
                                    )
                                    .withPool(mutagenPool(registryLookup, TCOTS_Items.FOGLET_MUTAGEN.get()))
                                    .withPool(cadaverinePool(registryLookup, 1, 2, 0.4f, 0.1f))
                                    .setRandomSequence(getRandomSequence("foglet"))
                    );
                }

                //Water Hag
                {
                    exporter.accept(TCOTS_Entities.WaterHag(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.WATER_HAG_MUD_BALL.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 2.0F)))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.WATER_ESSENCE.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 1.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.4f, 0.1f))
                                            )
                                    )
                                    .withPool(mutagenPool(registryLookup, TCOTS_Items.WATER_HAG_MUTAGEN.get()))
                                    .setRandomSequence(getRandomSequence("water_hag"))
                    );
                }

                //Grave Hag
                {
                    exporter.accept(TCOTS_Entities.GraveHag(),
                            LootTable.lootTable()
                                    .withPool(mutagenPool(registryLookup, TCOTS_Items.GRAVE_HAG_MUTAGEN.get()))
                                    .setRandomSequence(getRandomSequence("grave_hag"))
                    );
                }

                //Scurver
                {
                    exporter.accept(TCOTS_Entities.Scurver(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.SCURVER_SPINE.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 4.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.6f, 0.1f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.ROTFIEND_BLOOD.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 2.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.4f, 0.2f))
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 4, 0.5f, 0.1f))
                                    .setRandomSequence(getRandomSequence("scurver"))
                    );
                }

                //Devourer
                {
                    exporter.accept(TCOTS_Entities.Devourer(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.DEVOURER_TEETH.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.3f, 0.1f))
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 4, 0.6f, 0.1f))
                                    .setRandomSequence(getRandomSequence("devourer"))
                    );
                }

                //Graveir
                {
                    exporter.accept(TCOTS_Entities.Graveir(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.GRAVEIR_BONE.get())
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 1.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.6f, 0.15f))
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 6, 0.8f, 0.1f))
                                    .setRandomSequence(getRandomSequence("graveir"))
                    );
                }

                //Bullvore
                {
                    exporter.accept(TCOTS_Entities.Bullvore(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get())
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                    .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                            )
                                    )
                                    .withPool(cadaverinePool(registryLookup, 1, 5, 0.4f, 0.1f))
                                    .setRandomSequence(getRandomSequence("bullvore"))
                    );
                }

            }

            //Ogroids
            {
                //Nekker
                {
                    exporter.accept(TCOTS_Entities.Nekker(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(TCOTS_Items.NEKKER_EYE.get())
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                                    0.7f, 0.2f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(TCOTS_Items.NEKKER_HEART.get())
                                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                                    0.3f, 0.1f))
                                            )
                                    )
                                    .setRandomSequence(getRandomSequence("nekker"))
                    );
                }

                //Nekker Warrior
                {
                    exporter.accept(TCOTS_Entities.NekkerWarrior(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(TCOTS_Items.NEKKER_EYE.get())
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                                    0.7f, 0.2f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(TCOTS_Items.NEKKER_HEART.get())
                                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                                    0.4f, 0.1f))
                                            )
                                    )
                                    .withPool(mutagenPool(registryLookup, TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get()))
                                    .setRandomSequence(getRandomSequence("nekker_warrior"))
                    );
                }

                //Cyclops
                {
                    exporter.accept(TCOTS_Entities.Cyclops(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(Items.LEATHER)
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                            .when(LootItemRandomChanceCondition.randomChance(0.4f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(Items.RABBIT_HIDE)
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                                    0.3f, 0.15f))
                                            )
                                    )
                                    .setRandomSequence(getRandomSequence("cyclops"))
                    );
                }

                //Rock Troll
                {
                    exporter.accept(TCOTS_Entities.RockTroll(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.COBBLESTONE)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0,5)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 4.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.7f, 0.1f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(TCOTS_Items.CAVE_TROLL_LIVER.get())
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.7f, 0.1f))
                                            )
                                    )
                                    .withPool(mutagenPool(registryLookup, TCOTS_Items.TROLL_MUTAGEN.get(), 0.05f))
                                    .setRandomSequence(getRandomSequence("rock_troll"))
                    );
                }

                //Ice Troll
                {
                    exporter.accept(TCOTS_Entities.IceTroll(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.BLUE_ICE)
                                                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.2f, 0.1f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.PACKED_ICE)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 2.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.4f, 0.1f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.ICE)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 3.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.7f, 0.1f))
                                            )
                                    )
                                    .withPool(mutagenPool(registryLookup, TCOTS_Items.TROLL_MUTAGEN.get(), 0.05f))
                                    .setRandomSequence(getRandomSequence("ice_troll"))
                    );
                }

                //Forest Troll
                {
                    exporter.accept(TCOTS_Entities.ForestTroll(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.LEATHER)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 3.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.6f, 0.15f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.BONE)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 2.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.4f, 0.15f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.STRING)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                    .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                            UniformGenerator.between(0.0F, 3.0F)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.3f, 0.2f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(LootItem.lootTableItem(Items.WHITE_WOOL)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                    .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                            0.2f, 0.1f))
                                            )
                                    )
                                    .withPool(mutagenPool(registryLookup, TCOTS_Items.TROLL_MUTAGEN.get(), 0.05f))
                                    .setRandomSequence(getRandomSequence("forest_troll"))
                    );
                }

                //Ice Giant
                {
                    exporter.accept(TCOTS_Entities.IceGiant(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(Items.LEATHER)
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8)))
                                                            .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registryLookup,
                                                                    UniformGenerator.between(0.0F, 1.0F)))
                                                            .when(LootItemRandomChanceCondition.randomChance(0.6f))
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                            .add(
                                                    LootItem.lootTableItem(Items.BONE)
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12)))
                                                            .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                                                    0.5f, 0.15f))
                                            )
                                    )
                                    .setRandomSequence(getRandomSequence("ice_giant"))
                    );
                }

            }

        }

    }

    private static class LootTablesBlocksGenerator extends FabricBlockLootTableProvider {

        protected LootTablesBlocksGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, registryLookup);
        }

        @Override
        public void generate() {
            HolderLookup.RegistryLookup<Enchantment> impl = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
            this.dropSelf(TCOTS_Blocks.AlchemyTable());
            this.dropSelf(TCOTS_Blocks.HerbalTable());


            //Plants
            {
                //Arenaria Bush
                {
                    this.add(TCOTS_Blocks.ArenariaBush(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(TCOTS_Items.ARENARIA.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                            .apply(ApplyBonusCount.addUniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.ArenariaBush())
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(CropBlock.AGE, 2)))
                                    )
                            )
                            .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Bryonia
                {
                    this.add(TCOTS_Blocks.BryoniaVine(), LootTable.lootTable()
                            .withPool(bryoniaPool(Direction.NORTH))
                            .withPool(bryoniaPool(Direction.SOUTH))
                            .withPool(bryoniaPool(Direction.EAST))
                            .withPool(bryoniaPool(Direction.WEST))
                            .withPool(bryoniaPool(Direction.DOWN))
                            .withPool(bryoniaPool(Direction.UP))
                            .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Celandine
                {
                    this.add(TCOTS_Blocks.CelandinePlant(), LootTable.lootTable()

                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(TCOTS_Items.CELANDINE.get())
                                            .apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE),
                                                    0.5714286f, 2))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CelandinePlant())
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(CropBlock.AGE, 2)))
                                    )
                            )

                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(TCOTS_Items.CELANDINE.get())
                                            .apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE),
                                                    0.5714286f, 5))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CelandinePlant())
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(CropBlock.AGE, 3)))
                                    )
                            )


                            .apply(ApplyExplosionDecay.explosionDecay()));
                }

                //Crows Eye
                {
                    this.add(TCOTS_Blocks.CrowsEyeFern(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(TCOTS_Items.CROWS_EYE.get())
                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                            .apply(ApplyBonusCount.addUniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CrowsEyeFern())
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(CropBlock.AGE, 2)))
                                    )
                            )

                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(TCOTS_Items.CROWS_EYE.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                            .apply(ApplyBonusCount.addUniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CrowsEyeFern())
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(CropBlock.AGE, 3)))
                                    )
                            )

                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(TCOTS_Items.CROWS_EYE.get())
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                            .apply(ApplyBonusCount.addUniformBonusCount(impl.getOrThrow(Enchantments.FORTUNE)))
                                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CrowsEyeFern())
                                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                                            .hasProperty(CropBlock.AGE, 4)))
                                    )
                            )
                            .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Han Fiber
                {
                    this.add(TCOTS_Blocks.HanFiberPlant(), plantCropLootTable(impl, TCOTS_Items.HAN_FIBER.get(), TCOTS_Blocks.HanFiberPlant(), 2, 3));
                }

                //Puffball Mushroom
                {
                    this.dropSelf(TCOTS_Blocks.PuffballMushroom());
                }

                //Puffball Mushroom Block
                {
                    this.add(TCOTS_Blocks.PuffballMushroomBlock(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                            .add(AlternativesEntry.alternatives(
                                                            LootItem.lootTableItem(TCOTS_Blocks.PuffballMushroomBlock())
                                                                    .when(MatchTool.toolMatches(
                                                                            ItemPredicate.Builder.item()
                                                                                    .withSubPredicate(
                                                                                            ItemSubPredicates.ENCHANTMENTS,
                                                                                            ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(impl.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))
                                                                                    )
                                                                    )),
                                                            LootItem.lootTableItem(TCOTS_Items.PUFFBALL.get())
                                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
                                                    )
                                            )
                                    )
                                    .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Sewant Mushroom Block
                {
                    this.add(TCOTS_Blocks.SewantMushroomBlock(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                            .add(AlternativesEntry.alternatives(
                                                            LootItem.lootTableItem(TCOTS_Blocks.SewantMushroomBlock())
                                                                    .when(MatchTool.toolMatches(
                                                                            ItemPredicate.Builder.item()
                                                                                    .withSubPredicate(
                                                                                            ItemSubPredicates.ENCHANTMENTS,
                                                                                            ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(impl.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))
                                                                                    )
                                                                    )),
                                                            LootItem.lootTableItem(TCOTS_Items.SEWANT_MUSHROOMS.get())
                                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,8)))
                                                    )
                                            )
                                    )
                                    .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Sewant Mushroom Stem
                {
                    this.dropWhenSilkTouch(TCOTS_Blocks.SewantMushroomStem());
                }

                //Sewant Mushrooms
                {
                    this.add(TCOTS_Blocks.SewantMushroomsPlant(),
                            LootTable.lootTable()
                                    .withPool(
                                            LootPool.lootPool()
                                                    .setRolls(ConstantValue.exactly(1.0F))
                                                    .add(
                                                            this.applyExplosionDecay(
                                                                    TCOTS_Blocks.SewantMushroomsPlant(),
                                                                    LootItem.lootTableItem(TCOTS_Items.SEWANT_MUSHROOMS.get())
                                                                            .apply(
                                                                                    IntStream.rangeClosed(1, 4).boxed().toList(),
                                                                                    mushroomAmount -> SetItemCountFunction.setCount(ConstantValue.exactly((float) mushroomAmount))
                                                                                            .when(
                                                                                                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.SewantMushroomsPlant())
                                                                                                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SewantMushroomsPlant.MUSHROOM_AMOUNT, mushroomAmount))
                                                                                            )
                                                                            )
                                                            )
                                                    )
                                    )
                    );
                }

                //Verbena Flower
                {
                    this.add(TCOTS_Blocks.VerbenaFlower(), this.plantCropLootTable(impl, TCOTS_Items.VERBENA.get(), TCOTS_Blocks.VerbenaFlower(),1, 3));
                }
            }

            //Giant/Nests
            {
                //Giant Anchor
                {
                    this.add(TCOTS_Blocks.GiantAnchor(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.OAK_STAIRS)))
                                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                            .add(AlternativesEntry.alternatives(
                                                    LootItem.lootTableItem(TCOTS_Items.GIANT_ANCHOR.get())
                                                            .when(LootItemRandomChanceCondition.randomChance(0.25f)),
                                                    LootItem.lootTableItem(Items.IRON_INGOT)
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6)))
                                                    )
                                            )
                                    )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0,1))
                                            .add(LootItem.lootTableItem(Items.CHAIN)
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                            )
                                    )
                                    .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Nest Slab
                {
                    this.add(TCOTS_Blocks.NestSlab(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(Items.BONE)
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                    )
                            )
                            .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Monster Nest
                {
                    this.add(TCOTS_Blocks.MonsterNest(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(Items.BONE)
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12)))
                                    )
                            )
                            .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0,2))
                                    .add(LootItem.lootTableItem(Items.BOOK)
                                            .apply(EnchantRandomlyFunction.randomApplicableEnchantment(registries))
                                    )
                            )
                            .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Nest Skull
                {
                    this.add(TCOTS_Blocks.NestSkull(),
                            LootTable.lootTable()
                                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                            .add(AlternativesEntry.alternatives(
                                                            LootItem.lootTableItem(TCOTS_Blocks.NestSkull())
                                                                    .when(MatchTool.toolMatches(
                                                                            ItemPredicate.Builder.item()
                                                                                    .withSubPredicate(
                                                                                            ItemSubPredicates.ENCHANTMENTS,
                                                                                            ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(impl.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))
                                                                                    )
                                                                    )),
                                                            LootItem.lootTableItem(Items.BONE_MEAL)
                                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
                                                    )
                                            )
                                    )
                                    .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Skeleton Block
                {
                    this.add(TCOTS_Blocks.SkeletonBlock(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(Items.BONE_MEAL)
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))
                                    )
                            )
                            .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }

                //Winters Blade Skeleton
                {
                    this.add(TCOTS_Blocks.WintersBladeSkeleton(), LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(Items.BONE_MEAL)
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5)))
                                    )
                            )
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(TCOTS_Items.WINTERS_BLADE.get())
                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                    )
                            )
                            .apply(ApplyExplosionDecay.explosionDecay())
                    );
                }
            }

            //Potted plants
            {
                this.dropPottedContents(TCOTS_Blocks.PottedSewantMushrooms());
                this.dropPottedContents(TCOTS_Blocks.PottedHanFiber());
                this.dropPottedContents(TCOTS_Blocks.PottedBryoniaFlower());
                this.dropPottedContents(TCOTS_Blocks.PottedCelandineFlower());
                this.dropPottedContents(TCOTS_Blocks.PottedVerbenaFlower());
                this.dropPottedContents(TCOTS_Blocks.PottedPuffballMushroom());
            }
        }

        protected LootPool.Builder bryoniaPool(Direction direction){
            return LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(TCOTS_Items.BRYONIA.get())
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.BryoniaVine())
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(CropBlock.AGE, 3)))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.BryoniaVine())
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(MultifaceBlock.getFaceProperty(direction), true)))
                    );
        }

        @SuppressWarnings("all")
        protected LootTable.Builder plantCropLootTable(HolderLookup.RegistryLookup<Enchantment> impl, Item drop, Block block, int extra, int age){
            return LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(drop)
                                    .apply(ApplyBonusCount.addBonusBinomialDistributionCount(impl.getOrThrow(Enchantments.FORTUNE),
                                            0.5714286f, extra))
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(CropBlock.AGE, age)))
                            )
                    )
                    .apply(ApplyExplosionDecay.explosionDecay());
        }
    }

    private static class LootTablesChestsGenerator extends SimpleFabricLootTableProvider {

        public static final ResourceKey<LootTable> PLAINS_HERBALIST_CHEST = register( "chests/village/plains_herbalist");
        public static final ResourceKey<LootTable> TAIGA_HERBALIST_CHEST = register( "chests/village/taiga_herbalist");
        public static final ResourceKey<LootTable> SNOWY_HERBALIST_CHEST = register("chests/village/snowy_herbalist");
        public static final ResourceKey<LootTable> DESERT_HERBALIST_CHEST = register("chests/village/desert_herbalist");
        public static final ResourceKey<LootTable> SAVANNA_HERBALIST_CHEST = register("chests/village/savanna_herbalist");

        public static final ResourceKey<LootTable> TROLL_BARREL = register( "chests/troll/troll_barrel");
        public static final ResourceKey<LootTable> ICE_TROLL_BARREL = register( "chests/troll/ice_troll_barrel");
        public static final ResourceKey<LootTable> FOREST_TROLL_BARREL = register("chests/troll/forest_troll_barrel");

        public static final ResourceKey<LootTable> ICE_GIANT_CAVE_NEST = register("chests/ice_giant_treasure");

        public LootTablesChestsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup, LootContextParamSets.CHEST);
        }

        private static ResourceKey<LootTable> register(String id) {
            return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id));

        }

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {

            //Plains
            {
                exporter.accept(PLAINS_HERBALIST_CHEST,
                        LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_BOOK.get()).setWeight(1))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()).setWeight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(5))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(LootItem.lootTableItem(Items.DANDELION).setWeight(8)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.CELANDINE.get()).setWeight(8))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(LootItem.lootTableItem(Items.POPPY).setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 3.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.VERBENA.get()).setWeight(6))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(LootItem.lootTableItem(TCOTS_Items.ERGOT_SEEDS.get()).setWeight(2))

                                .add(LootItem.lootTableItem(TCOTS_Items.ALLSPICE.get()).setWeight(2))

                                .add(LootItem.lootTableItem(Items.FLOWERING_AZALEA).setWeight(2))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.SEWANT_MUSHROOMS.get()).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.LILY_OF_THE_VALLEY).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.OXEYE_DAISY).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ARENARIA.get()).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(LootItem.lootTableItem(TCOTS_Items.BRYONIA.get()).setWeight(2))

                                .add(LootItem.lootTableItem(Items.GLOW_LICHEN).setWeight(2))

                        )
                );
            }

            //Taiga
            {
                exporter.accept(TAIGA_HERBALIST_CHEST,
                        LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_BOOK.get()).setWeight(1))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()).setWeight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(3))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(LootItem.lootTableItem(Items.FERN).setWeight(8))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(6))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.AZURE_BLUET).setWeight(10))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.PUFFBALL.get()).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.SEWANT_MUSHROOMS.get()).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 5.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(8))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Blocks.SPRUCE_SAPLING).setWeight(8))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ALLIUM).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.CROWS_EYE.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ARENARIA.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))


                                .add(LootItem.lootTableItem(TCOTS_Items.BRYONIA.get()).setWeight(2))

                                .add(LootItem.lootTableItem(Items.GLOW_LICHEN).setWeight(2))

                                .add(LootItem.lootTableItem(Items.MOSS_BLOCK).setWeight(2))

                        ));
            }

            //Snowy
            {
                exporter.accept(SNOWY_HERBALIST_CHEST,
                        LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_BOOK.get()).setWeight(1))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()).setWeight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(3))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(LootItem.lootTableItem(TCOTS_Items.BRYONIA.get()).setWeight(7))

                                .add(LootItem.lootTableItem(Items.FERN).setWeight(7))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(7))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(3))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Blocks.SPRUCE_SAPLING).setWeight(10))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.PUFFBALL.get()).setWeight(7))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.VERBENA.get()).setWeight(7))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.AZURE_BLUET).setWeight(7))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.CORNFLOWER).setWeight(3))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ARENARIA.get()).setWeight(10))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.CROWS_EYE.get()).setWeight(10))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 6.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.SWEET_BERRIES).setWeight(7))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                .add(LootItem.lootTableItem(Items.GLOW_LICHEN).setWeight(3))


                        ));
            }

            //Desert
            {
                exporter.accept(DESERT_HERBALIST_CHEST,
                        LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_BOOK.get()).setWeight(1))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()).setWeight(3))
                                        .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(3))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(4))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                .add(LootItem.lootTableItem(Items.CACTUS).setWeight(10))

                                .add(LootItem.lootTableItem(Items.DEAD_BUSH).setWeight(10))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.HAN_FIBER.get()).setWeight(5))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(5))

                                .add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(3))

                                .add(LootItem.lootTableItem(TCOTS_Items.PUFFBALL.get()).setWeight(5))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.LILY_OF_THE_VALLEY).setWeight(5))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(LootItem.lootTableItem(TCOTS_Items.BRYONIA.get()).setWeight(5))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.AZURE_BLUET).setWeight(3))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.VERBENA.get()).setWeight(3))
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                .add(LootItem.lootTableItem(Items.GLOW_LICHEN).setWeight(3))


                        ));
            }

            //Savanna
            {
                {
                    exporter.accept(SAVANNA_HERBALIST_CHEST,
                            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 8.0f))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_BOOK.get()).setWeight(1))
                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()).setWeight(3))
                                            .apply(AlchemyRecipeRandomlyLootFunction.builder()))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.EMERALD).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.DANDELION).setWeight(8))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.HAN_FIBER.get()).setWeight(8))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ACACIA_SAPLING).setWeight(10))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(LootItem.lootTableItem(Items.BROWN_MUSHROOM).setWeight(3))

                                    .add(LootItem.lootTableItem(Items.RED_MUSHROOM).setWeight(3))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.LILY_OF_THE_VALLEY).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(LootItem.lootTableItem(TCOTS_Items.BRYONIA.get()).setWeight(3))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.SHORT_GRASS).setWeight(5))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.MOSS_CARPET).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.AZURE_BLUET).setWeight(5))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(LootItem.lootTableItem(Items.GLOW_LICHEN).setWeight(3))

                            ));
                }
            }


            //Trolls barrels
            {
                //Rock Troll Barrel
                {
                    exporter.accept(TROLL_BARREL,
                            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 5.0f))

                                    //Rocks
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COBBLESTONE).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 8.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ANDESITE).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 8.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.GRANITE).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 8.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.TUFF).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 8.0f))))

                                    //Ores
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RAW_COPPER).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 6.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RAW_IRON).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RAW_GOLD).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                    //Meat
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 8.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.BEEF).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.MUTTON).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.PORKCHOP).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RABBIT).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.CHICKEN).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    //Alcohol-Cooked Meat
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_BEEF).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_MUTTON).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_PORKCHOP).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_RABBIT).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_CHICKEN).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(LootItem.lootTableItem(Items.RABBIT_STEW).setWeight(1))

                                    //Amethyst-RawBlocks
                                    .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.RAW_IRON_BLOCK).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.RAW_GOLD_BLOCK).setWeight(1))


                            ));
                }

                //Ice Troll Barrel
                {
                    exporter.accept(ICE_TROLL_BARREL,
                            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(4.0f, 6.0f))

                                    //Rocks-Ice
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COBBLESTONE).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0f, 8.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.SNOWBALL).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 12.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.SNOW_BLOCK).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 8.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ICE).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.PACKED_ICE).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))

                                    .add(LootItem.lootTableItem(Items.BLUE_ICE).setWeight(2))

                                    //Ores

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RAW_IRON).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RAW_COPPER).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 6.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.EMERALD).setWeight(1))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 6.0f))))

                                    //Meat
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 8.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.MUTTON).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RABBIT).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f))))

                                    //Alcohol-Cooked Meat
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(1))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_MUTTON).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_RABBIT).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(LootItem.lootTableItem(Items.RABBIT_STEW).setWeight(1))

                                    //Amethyst-RawBlocks
                                    .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.RAW_IRON_BLOCK).setWeight(1))

                            ));
                }

                //Forest Troll Barrel
                {
                    exporter.accept(FOREST_TROLL_BARREL,
                            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(4.0f, 6.0f))

                                    //Wood
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.OAK_LOG).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.BIRCH_LOG).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(4))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 6.0f))))

                                    //Saplings
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.OAK_SAPLING).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.BIRCH_SAPLING).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.SPRUCE_SAPLING).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.DARK_OAK_SAPLING).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    //Ores
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RAW_IRON).setWeight(1))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RAW_COPPER).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 6.0f))))


                                    //Meat
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(6.0f, 10.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.MUTTON).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0f, 5.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.RABBIT).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.CHICKEN).setWeight(3))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.PORKCHOP).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.BEEF).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))

                                    //Alcohol-Cooked Meat
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(1))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_MUTTON).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 3.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_RABBIT).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_CHICKEN).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_BEEF).setWeight(1))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_PORKCHOP).setWeight(1))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                                    .add(LootItem.lootTableItem(Items.RABBIT_STEW).setWeight(1))

                                    //CopperIngot-RawBlocks
                                    .add(LootItem.lootTableItem(Items.COPPER_INGOT).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(1))
                                    .add(LootItem.lootTableItem(Items.RAW_IRON_BLOCK).setWeight(1))

                            ));
                }


                //Ice Giant
                {
                    exporter.accept(ICE_GIANT_CAVE_NEST,
                            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(3.0f, 6.0f))
                                    //Treasure
                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.DIAMOND).setWeight(5))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 6.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.IRON_INGOT).setWeight(15))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 12.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.GOLD_INGOT).setWeight(15))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 16.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.EMERALD).setWeight(5))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 8.0f))))

                                            .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.AMETHYST_SHARD).setWeight(5))
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 12.0f))))

                                    .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.ENCHANTED_GOLDEN_APPLE).setWeight(2))
                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))

                            )
                                    .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(2.0f, 4.0f))
                                            //Food
                                            .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_BEEF).setWeight(4))
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 4.0f))))

                                            .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_MUTTON).setWeight(6))
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 6.0f))))

                                            .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.COOKED_COD).setWeight(5))
                                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 6.0f))))

                                            .add(((LootPoolSingletonContainer.Builder<?>) LootItem.lootTableItem(Items.LEATHER).setWeight(3))
                                                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f))))
                                    ));
                }
            }

        }

    }

    private static class POIProvider extends TagsProvider<PoiType>{

        public POIProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
            super(output, Registries.POINT_OF_INTEREST_TYPE, registryLookupFuture);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookup) {
            this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                    .addOptional(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "herbal_poi"));

        }
    }

    private static class DamageTypeTagsGenerator extends TagsProvider<DamageType> {


        /**
         * Constructs a new {@link FabricTagProvider} with the default computed path.
         *
         * <p>Common implementations of this class are provided.
         *
         * @param output           the {@link FabricDataOutput} instance
         * @param registriesFuture the backing registry for the tag type
         */
        public DamageTypeTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.DAMAGE_TYPE, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookup) {
            this.tag(DamageTypeTags.BYPASSES_ARMOR)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS)
                    .add(TCOTS_DamageTypes.ANCHOR);

            this.tag(DamageTypeTags.IS_PROJECTILE)
                    .add(TCOTS_DamageTypes.ANCHOR);


            this.tag(DamageTypeTags.NO_KNOCKBACK)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.PANIC_CAUSES)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);
        }

    }

    @SuppressWarnings("deprecation")
    private static class BlockTagsGenerator extends IntrinsicHolderTagsProvider<Block> {
        public BlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.BLOCK, registriesFuture, (block) -> block.builtInRegistryHolder().key());
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookup) {
            this.tag(TCOTS_Tags.IGNITING_BLOCKS)
                    .add(Blocks.FIRE)
                    .add(Blocks.SOUL_FIRE)
                    .add(Blocks.MAGMA_BLOCK);

            this.tag(TCOTS_Tags.DESTROYABLE_MAGIC_BLOCKS)
                    .add(Blocks.NETHER_PORTAL)
                    .add(Blocks.END_GATEWAY)
                    .add(Blocks.SOUL_FIRE);

            this.tag(TCOTS_Tags.NEGATES_DEVOURER_JUMP)
                    .add(Blocks.HAY_BLOCK)
                    .add(Blocks.MOSS_BLOCK)
                    .add(Blocks.COBWEB)
                    .add(Blocks.SCULK)
                    .add(Blocks.SLIME_BLOCK)
                    .add(Blocks.HONEY_BLOCK)

                    .addOptionalTag(BlockTags.BEDS.location())
                    .addOptionalTag(BlockTags.WOOL.location())
                    .addOptionalTag(BlockTags.LEAVES.location())
                    .addOptionalTag(BlockTags.WART_BLOCKS.location());

            this.tag(BlockTags.MINEABLE_WITH_AXE)
                    .add(TCOTS_Blocks.GiantAnchor())
                    .add(TCOTS_Blocks.AlchemyTable())
                    .add(TCOTS_Blocks.HerbalTable())
                    .add(TCOTS_Blocks.SewantMushroomBlock())
                    .add(TCOTS_Blocks.SewantMushroomStem())
                    .add(TCOTS_Blocks.PuffballMushroomBlock());

            this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                    .add(TCOTS_Blocks.NestSlab())
                    .add(TCOTS_Blocks.MonsterNest());

            this.tag(BlockTags.BEE_GROWABLES)
                    .add(TCOTS_Blocks.ArenariaBush())
                    .add(TCOTS_Blocks.CelandinePlant())
                    .add(TCOTS_Blocks.BryoniaVine())
                    .add(TCOTS_Blocks.VerbenaFlower())
                    .add(TCOTS_Blocks.HanFiberPlant())
                    .add(TCOTS_Blocks.CrowsEyeFern());

            this.tag(BlockTags.FLOWERS)
                    .add(TCOTS_Blocks.ArenariaBush())
                    .add(TCOTS_Blocks.CelandinePlant())
                    .add(TCOTS_Blocks.BryoniaVine())
                    .add(TCOTS_Blocks.VerbenaFlower())
                    .add(TCOTS_Blocks.HanFiberPlant());
        }
    }

    @SuppressWarnings("deprecation")
    private static class EntityTagGenerator extends IntrinsicHolderTagsProvider<EntityType<?>> {

        public EntityTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, Registries.ENTITY_TYPE, completableFuture, (entityType) -> entityType.builtInRegistryHolder().key());
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider arg) {
            this.tag(TCOTS_Tags.NECROPHAGES)
                    .add(
                            TCOTS_Entities.Devourer(),
                            TCOTS_Entities.GraveHag(),
                            TCOTS_Entities.Drowner(),
                            TCOTS_Entities.Ghoul(),
                            TCOTS_Entities.Alghoul(),
                            TCOTS_Entities.Foglet(),
                            TCOTS_Entities.Bullvore(),
                            TCOTS_Entities.WaterHag(),
                            TCOTS_Entities.Graveir(),
                            TCOTS_Entities.Rotfiend(),
                            TCOTS_Entities.Scurver());

            this.tag(TCOTS_Tags.OGROIDS)
                    .add(
                            TCOTS_Entities.IceGiant(),
                            TCOTS_Entities.Nekker(),
                            TCOTS_Entities.NekkerWarrior(),
                            TCOTS_Entities.Cyclops(),
                            TCOTS_Entities.RockTroll(),
                            TCOTS_Entities.IceTroll(),
                            TCOTS_Entities.ForestTroll());



            this.tag(TCOTS_Tags.IGNITING_ENTITIES)
                    .add(EntityType.BLAZE)
                    .add(EntityType.FIREBALL)
                    .add(EntityType.SMALL_FIREBALL)
                    .add(EntityType.FIREWORK_ROCKET);

            this.tag(TCOTS_Tags.DIMERITIUM_REMOVAL)
                    .add(EntityType.EVOKER_FANGS)
                    .add(EntityType.AREA_EFFECT_CLOUD)
                    .add(EntityType.SHULKER_BULLET);

            this.tag(TCOTS_Tags.DIMERITIUM_DAMAGE)
                    .add(EntityType.END_CRYSTAL)
                    .add(EntityType.VEX)
                    .add(EntityType.ALLAY)
                    .add(TCOTS_Entities.Fogling());

            this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                    .add(TCOTS_Entities.IceTroll());

            this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                    .add(TCOTS_Entities.IceTroll())
                    .add(TCOTS_Entities.Cyclops())
                    .add(TCOTS_Entities.IceGiant());

            this.tag(TCOTS_Tags.BOSS_TAG)
                    .add(TCOTS_Entities.IceGiant());


            this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                    .add(TCOTS_Entities.Drowner());

            this.tag(EntityTypeTags.SENSITIVE_TO_IMPALING)
                    .add(TCOTS_Entities.Drowner());
        }
    }

    @SuppressWarnings("deprecation")
    private static class ItemTagGenerator extends IntrinsicHolderTagsProvider<Item> {

        public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
            super(output, Registries.ITEM, completableFuture, (item) -> item.builtInRegistryHolder().key());
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider arg) {
            this.tag(TCOTS_Tags.DECAYING_FLESH)
                    .add(Items.BEEF)
                    .add(Items.PORKCHOP)
                    .add(Items.MUTTON)
                    .add(Items.CHICKEN)
                    .add(Items.RABBIT);

            this.tag(TCOTS_Tags.MONSTER_BLOOD)
                    .add(TCOTS_Items.ROTFIEND_BLOOD.get())
                    .add(TCOTS_Items.GHOUL_BLOOD.get());

            this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES)
                    .add(TCOTS_Items.WARRIORS_LEATHER_BOOTS.get(), TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get(), TCOTS_Items.WARRIORS_LEATHER_JACKET.get())

                    .add(TCOTS_Items.MANTICORE_BOOTS.get(), TCOTS_Items.MANTICORE_TROUSERS.get(), TCOTS_Items.MANTICORE_ARMOR.get())

                    .add(TCOTS_Items.RAVENS_BOOTS.get(), TCOTS_Items.RAVENS_TROUSERS.get(), TCOTS_Items.RAVENS_ARMOR.get())

                    .add(TCOTS_Items.TUNDRA_HORSE_ARMOR.get());

            this.tag(ItemTags.DYEABLE)
                    .add(TCOTS_Items.KNIGHT_CROSSBOW.get());


            //Tags for Enchanting
            this.tag(ItemTags.SWORDS)
                    .add(TCOTS_Items.GVALCHIR.get())
                    .add(TCOTS_Items.MOONBLADE.get())
                    .add(TCOTS_Items.DYAEBL.get())
                    .add(TCOTS_Items.WINTERS_BLADE.get())
                    .add(TCOTS_Items.ARDAENYE.get());

            this.tag(ItemTags.CROSSBOW_ENCHANTABLE)
                    .add(TCOTS_Items.KNIGHT_CROSSBOW.get());

            this.tag(ItemTags.CHEST_ARMOR)
                    .add(TCOTS_Items.WARRIORS_LEATHER_JACKET.get(), TCOTS_Items.RAVENS_ARMOR.get(), TCOTS_Items.MANTICORE_ARMOR.get());

            this.tag(ItemTags.LEG_ARMOR)
                    .add(TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get(), TCOTS_Items.RAVENS_TROUSERS.get(), TCOTS_Items.MANTICORE_TROUSERS.get());

            this.tag(ItemTags.FOOT_ARMOR)
                    .add(TCOTS_Items.WARRIORS_LEATHER_BOOTS.get(), TCOTS_Items.RAVENS_BOOTS.get(), TCOTS_Items.MANTICORE_BOOTS.get());

            this.tag(ItemTags.DURABILITY_ENCHANTABLE)
                    .add(TCOTS_Items.GIANT_ANCHOR.get());

            this.tag(ItemTags.FLOWERS)
                    .add(TCOTS_Items.ARENARIA.get())
                    .add(TCOTS_Items.CELANDINE.get())
                    .add(TCOTS_Items.BRYONIA.get())
                    .add(TCOTS_Items.VERBENA.get())
                    .add(TCOTS_Items.HAN_FIBER.get());

        }
    }

    private static class BiomeTagGenerator extends BiomeTagsProvider {

        public BiomeTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider arg) {

            //Has_Flora
            {
                this.tag(TCOTS_Tags.CELANDINE_SPAWN)
                        .add(Biomes.PLAINS).add(Biomes.MEADOW)
                        .add(Biomes.BIRCH_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                ;

                this.tag(TCOTS_Tags.VERBENA_SPAWN)
                        .add(Biomes.PLAINS).add(Biomes.MEADOW)
                        .add(Biomes.BIRCH_FOREST).add(Biomes.OLD_GROWTH_BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                        .add(Biomes.FLOWER_FOREST)
                ;

                this.tag(TCOTS_Tags.HAN_FIBER_SPAWN)
                        .addOptionalTag(BiomeTags.IS_JUNGLE.location())
                        .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location())
                        .addOptionalTag(BiomeTags.HAS_VILLAGE_SAVANNA.location())
                ;

                this.tag(TCOTS_Tags.CROWS_EYE_SPAWN)
                        .addOptionalTag(BiomeTags.IS_TAIGA.location())
                        .add(Biomes.GROVE)
                ;

                this.tag(TCOTS_Tags.ARENARIA_SPAWN)
                        .addOptionalTag(BiomeTags.IS_TAIGA.location())
                        .add(Biomes.FLOWER_FOREST)
                ;

                this.tag(TCOTS_Tags.PUFFBALL_SPAWN_NORMAL)
                        .add(Biomes.PLAINS)
                        .add(Biomes.SUNFLOWER_PLAINS)
                        .add(Biomes.SNOWY_PLAINS)
                        .add(Biomes.ICE_SPIKES)
                        .add(Biomes.DESERT)
                        .add(Biomes.SWAMP)
                        .add(Biomes.MANGROVE_SWAMP)
                        .add(Biomes.FOREST)
                        .add(Biomes.FLOWER_FOREST)
                        .add(Biomes.BIRCH_FOREST)
                        .add(Biomes.DARK_FOREST)
                        .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                        .add(Biomes.SAVANNA)
                        .add(Biomes.SAVANNA_PLATEAU)
                        .add(Biomes.WINDSWEPT_HILLS)
                        .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                        .add(Biomes.WINDSWEPT_FOREST)
                        .add(Biomes.WINDSWEPT_SAVANNA)
                        .add(Biomes.JUNGLE)
                        .add(Biomes.SPARSE_JUNGLE)
                        .add(Biomes.BAMBOO_JUNGLE)
                        .add(Biomes.BADLANDS)
                        .add(Biomes.ERODED_BADLANDS)
                        .add(Biomes.WOODED_BADLANDS)
                        .add(Biomes.MEADOW)
                        .add(Biomes.CHERRY_GROVE)
                        .add(Biomes.RIVER)
                        .add(Biomes.FROZEN_RIVER)
                        .add(Biomes.BEACH)
                        .add(Biomes.SNOWY_BEACH)
                        .add(Biomes.STONY_SHORE)
                        .add(Biomes.WARM_OCEAN)
                        .add(Biomes.LUKEWARM_OCEAN)
                        .add(Biomes.DEEP_LUKEWARM_OCEAN)
                        .add(Biomes.OCEAN)
                        .add(Biomes.DEEP_OCEAN)
                        .add(Biomes.COLD_OCEAN)
                        .add(Biomes.DEEP_COLD_OCEAN)
                        .add(Biomes.FROZEN_OCEAN)
                        .add(Biomes.DEEP_FROZEN_OCEAN)
                        .add(Biomes.DRIPSTONE_CAVES)
                ;

                this.tag(TCOTS_Tags.PUFFBALL_SPAWN_TAIGA)
                        .add(Biomes.TAIGA).add(Biomes.SNOWY_TAIGA)
                ;

                this.tag(TCOTS_Tags.PUFFBALL_SPAWN_SWAMP)
                        .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location())
                ;

                this.tag(TCOTS_Tags.MUSHROOM_SPAWN_OLD_GROWTH)
                        .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA)
                ;

                this.tag(TCOTS_Tags.SEWANT_SPAWN_NORMAL)
                        .add(Biomes.PLAINS)
                        .add(Biomes.SUNFLOWER_PLAINS)
                        .add(Biomes.SNOWY_PLAINS)
                        .add(Biomes.ICE_SPIKES)
                        .add(Biomes.DESERT)
                        .add(Biomes.SWAMP)
                        .add(Biomes.MANGROVE_SWAMP)
                        .add(Biomes.FOREST)
                        .add(Biomes.FLOWER_FOREST)
                        .add(Biomes.BIRCH_FOREST)
                        .add(Biomes.DARK_FOREST)
                        .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                        .add(Biomes.SNOWY_TAIGA)
                        .add(Biomes.SAVANNA)
                        .add(Biomes.SAVANNA_PLATEAU)
                        .add(Biomes.WINDSWEPT_HILLS)
                        .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                        .add(Biomes.WINDSWEPT_FOREST)
                        .add(Biomes.WINDSWEPT_SAVANNA)
                        .add(Biomes.JUNGLE)
                        .add(Biomes.SPARSE_JUNGLE)
                        .add(Biomes.BAMBOO_JUNGLE)
                        .add(Biomes.BADLANDS)
                        .add(Biomes.ERODED_BADLANDS)
                        .add(Biomes.WOODED_BADLANDS)
                        .add(Biomes.CHERRY_GROVE)
                        .add(Biomes.RIVER)
                        .add(Biomes.FROZEN_RIVER)
                        .add(Biomes.BEACH)
                        .add(Biomes.SNOWY_BEACH)
                        .add(Biomes.STONY_SHORE)
                        .add(Biomes.WARM_OCEAN)
                        .add(Biomes.LUKEWARM_OCEAN)
                        .add(Biomes.DEEP_LUKEWARM_OCEAN)
                        .add(Biomes.OCEAN)
                        .add(Biomes.DEEP_OCEAN)
                        .add(Biomes.COLD_OCEAN)
                        .add(Biomes.DEEP_COLD_OCEAN)
                        .add(Biomes.FROZEN_OCEAN)
                        .add(Biomes.DEEP_FROZEN_OCEAN)
                        .add(Biomes.MUSHROOM_FIELDS)
                        .add(Biomes.DRIPSTONE_CAVES)
                ;

                this.tag(TCOTS_Tags.SEWANT_SPAWN_TAIGA)
                        .add(Biomes.TAIGA)
                ;

                this.tag(TCOTS_Tags.SEWANT_SPAWN_DARK)
                        .add(Biomes.DARK_FOREST)
                ;
            }

            //Has_Monster
            {
                //Necrophages
                {
                    //Drowners
                    {
                        this.tag(TCOTS_Tags.DROWNER_SWAMP)
                                .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location());

                        this.tag(TCOTS_Tags.DROWNER_BEACH)
                                .addOptionalTag(BiomeTags.IS_BEACH.location());

                        this.tag(TCOTS_Tags.DROWNER_WATER)
                                .addOptionalTag(BiomeTags.IS_OCEAN.location())
                                .addOptionalTag(BiomeTags.IS_DEEP_OCEAN.location())
                                .add(Biomes.RIVER);
                    }

                    //Rotfiends
                    {
                        this.tag(TCOTS_Tags.ROTFIEND)
                                .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                                .add(Biomes.DRIPSTONE_CAVES)
                                .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                                .add(Biomes.PLAINS).add(Biomes.TAIGA);
                    }

                    //Foglets
                    {
                        this.tag(TCOTS_Tags.FOGLET_SWAMP)
                                .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location())
                                .add(Biomes.RIVER);

                        this.tag(TCOTS_Tags.FOGLET_DARK)
                                .addOptionalTag(BiomeTags.HAS_WOODLAND_MANSION.location());

                        this.tag(TCOTS_Tags.FOGLET_HILLS_FORESTS)
                                .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                                .add(Biomes.JAGGED_PEAKS).add(Biomes.STONY_PEAKS)
                                .addOptionalTag(BiomeTags.IS_HILL.location());
                    }

                    //Water Hags
                    {
                        this.tag(TCOTS_Tags.WATER_HAG_SWAMP)
                                .addOptionalTag(BiomeTags.HAS_RUINED_PORTAL_SWAMP.location());

                        this.tag(TCOTS_Tags.WATER_HAG_RIVER)
                                .add(Biomes.RIVER);
                    }

                    //Grave Hags
                    {

                        this.tag(TCOTS_Tags.GRAVE_HAG)
                                .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                                .add(Biomes.DRIPSTONE_CAVES)
                                .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                                .add(Biomes.PLAINS).add(Biomes.SAVANNA).add(Biomes.TAIGA);
                    }

                    //Ghouls & Alghouls
                    {
                        this.tag(TCOTS_Tags.GHOUL)
                                .add(Biomes.SAVANNA).add(Biomes.PLAINS)
                                .add(Biomes.TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                                .add(Biomes.FOREST).add(Biomes.FLOWER_FOREST).add(Biomes.BIRCH_FOREST)
                                .add(Biomes.DARK_FOREST);
                    }

                    //Scurvers
                    {
                        this.tag(TCOTS_Tags.SCURVER)
                                .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST).add(Biomes.DARK_FOREST)
                                .addOptionalTag(BiomeTags.HAS_VILLAGE_SAVANNA.location())
                                .addOptionalTag(BiomeTags.IS_JUNGLE.location());
                    }

                    //Devourer
                    {
                        this.tag(TCOTS_Tags.DEVOURER)
                                .add(Biomes.BIRCH_FOREST).add(Biomes.FOREST)
                                .add(Biomes.SWAMP).add(Biomes.RIVER)
                                .add(Biomes.PLAINS);
                    }

                    //Graveir
                    {
                        this.tag(TCOTS_Tags.GRAVEIR)
                                .add(Biomes.PLAINS)
                                .add(Biomes.SUNFLOWER_PLAINS)
                                .add(Biomes.SNOWY_PLAINS)
                                .add(Biomes.ICE_SPIKES)
                                .add(Biomes.DESERT)
                                .add(Biomes.SWAMP)
                                .add(Biomes.MANGROVE_SWAMP)
                                .add(Biomes.FOREST)
                                .add(Biomes.FLOWER_FOREST)
                                .add(Biomes.BIRCH_FOREST)
                                .add(Biomes.DARK_FOREST)
                                .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                                .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                                .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                                .add(Biomes.TAIGA)
                                .add(Biomes.SNOWY_TAIGA)
                                .add(Biomes.SAVANNA)
                                .add(Biomes.SAVANNA_PLATEAU)
                                .add(Biomes.WINDSWEPT_HILLS)
                                .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
                                .add(Biomes.WINDSWEPT_FOREST)
                                .add(Biomes.WINDSWEPT_SAVANNA)
                                .add(Biomes.JUNGLE)
                                .add(Biomes.SPARSE_JUNGLE)
                                .add(Biomes.BAMBOO_JUNGLE)
                                .add(Biomes.BADLANDS)
                                .add(Biomes.ERODED_BADLANDS)
                                .add(Biomes.WOODED_BADLANDS)
                                .add(Biomes.MEADOW)
                                .add(Biomes.CHERRY_GROVE)
                                .add(Biomes.GROVE)
                                .add(Biomes.SNOWY_SLOPES)
                                .add(Biomes.FROZEN_PEAKS)
                                .add(Biomes.JAGGED_PEAKS)
                                .add(Biomes.STONY_PEAKS)
                                .add(Biomes.RIVER)
                                .add(Biomes.FROZEN_RIVER)
                                .add(Biomes.BEACH)
                                .add(Biomes.SNOWY_BEACH)
                                .add(Biomes.STONY_SHORE)
                                .add(Biomes.WARM_OCEAN)
                                .add(Biomes.LUKEWARM_OCEAN)
                                .add(Biomes.DEEP_LUKEWARM_OCEAN)
                                .add(Biomes.OCEAN)
                                .add(Biomes.DEEP_OCEAN)
                                .add(Biomes.COLD_OCEAN)
                                .add(Biomes.DEEP_COLD_OCEAN)
                                .add(Biomes.FROZEN_OCEAN)
                                .add(Biomes.DEEP_FROZEN_OCEAN)
                                .add(Biomes.DRIPSTONE_CAVES);
                    }

                }

                //Ogroids
                {
                    //Nekkers
                    {
                        this.tag(TCOTS_Tags.NEKKER)
                                .add(Biomes.SAVANNA).add(Biomes.PLAINS)
                                .addOptionalTag(BiomeTags.IS_JUNGLE.location())
                                .addOptionalTag(BiomeTags.IS_FOREST.location());
                    }

                    //Cyclops
                    {
                        this.tag(TCOTS_Tags.CYCLOPS)
                                .add(Biomes.SNOWY_PLAINS)
                                .add(Biomes.STONY_SHORE)
                                .addOptionalTag(BiomeTags.IS_HILL.location())
                                .add(Biomes.MEADOW).add(Biomes.FROZEN_PEAKS).add(Biomes.JAGGED_PEAKS).add(Biomes.STONY_PEAKS).add(Biomes.SNOWY_SLOPES)
                                .add(Biomes.OLD_GROWTH_PINE_TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
                    }

                    //Rock Troll
                    {
                        this.tag(TCOTS_Tags.ROCK_TROLL)
                                .add(Biomes.STONY_PEAKS).add(Biomes.MEADOW)
                                .addOptionalTag(BiomeTags.IS_HILL.location())
                                .add(Biomes.TAIGA).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA).add(Biomes.OLD_GROWTH_PINE_TAIGA)
                                .add(Biomes.DRIPSTONE_CAVES).add(Biomes.STONY_SHORE);
                    }

                    //Ice Troll
                    {
                        this.tag(TCOTS_Tags.ICE_TROLL)
                                .add(Biomes.SNOWY_PLAINS)
                                .add(Biomes.ICE_SPIKES)
                                .add(Biomes.SNOWY_TAIGA)
                                .add(Biomes.FROZEN_PEAKS)
                                .add(Biomes.JAGGED_PEAKS)
                                .add(Biomes.SNOWY_SLOPES)
                                .add(Biomes.GROVE);
                    }


                    //Forest Troll
                    {
                        this.tag(TCOTS_Tags.FOREST_TROLL)
                                .addOptionalTag(BiomeTags.IS_FOREST.location());
                    }
                }
            }

//            All overworld biomes
//                    .add(Biomes.PLAINS)
//                    .add(Biomes.SUNFLOWER_PLAINS)
//                    .add(Biomes.SNOWY_PLAINS)
//                    .add(Biomes.ICE_SPIKES)
//                    .add(Biomes.DESERT)
//                    .add(Biomes.SWAMP)
//                    .add(Biomes.MANGROVE_SWAMP)
//                    .add(Biomes.FOREST)
//                    .add(Biomes.FLOWER_FOREST)
//                    .add(Biomes.BIRCH_FOREST)
//                    .add(Biomes.DARK_FOREST)
//                    .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
//                    .add(Biomes.OLD_GROWTH_PINE_TAIGA)
//                    .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
//                    .add(Biomes.TAIGA)
//                    .add(Biomes.SNOWY_TAIGA)
//                    .add(Biomes.SAVANNA)
//                    .add(Biomes.SAVANNA_PLATEAU)
//                    .add(Biomes.WINDSWEPT_HILLS)
//                    .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
//                    .add(Biomes.WINDSWEPT_FOREST)
//                    .add(Biomes.WINDSWEPT_SAVANNA)
//                    .add(Biomes.JUNGLE)
//                    .add(Biomes.SPARSE_JUNGLE)
//                    .add(Biomes.BAMBOO_JUNGLE)
//                    .add(Biomes.BADLANDS)
//                    .add(Biomes.ERODED_BADLANDS)
//                    .add(Biomes.WOODED_BADLANDS)
//                    .add(Biomes.MEADOW)
//                    .add(Biomes.CHERRY_GROVE)
//                    .add(Biomes.GROVE)
//                    .add(Biomes.SNOWY_SLOPES)
//                    .add(Biomes.FROZEN_PEAKS)
//                    .add(Biomes.JAGGED_PEAKS)
//                    .add(Biomes.STONY_PEAKS)
//                    .add(Biomes.RIVER)
//                    .add(Biomes.FROZEN_RIVER)
//                    .add(Biomes.BEACH)
//                    .add(Biomes.SNOWY_BEACH)
//                    .add(Biomes.STONY_SHORE)
//                    .add(Biomes.WARM_OCEAN)
//                    .add(Biomes.LUKEWARM_OCEAN)
//                    .add(Biomes.DEEP_LUKEWARM_OCEAN)
//                    .add(Biomes.OCEAN)
//                    .add(Biomes.DEEP_OCEAN)
//                    .add(Biomes.COLD_OCEAN)
//                    .add(Biomes.DEEP_COLD_OCEAN)
//                    .add(Biomes.FROZEN_OCEAN)
//                    .add(Biomes.DEEP_FROZEN_OCEAN)
//                    .add(Biomes.MUSHROOM_FIELDS)
//                    .add(Biomes.DRIPSTONE_CAVES)
//                    .add(Biomes.LUSH_CAVES)
//                    .add(Biomes.DEEP_DARK);
//            ;

        }
    }


    private static class RecipesGenerator extends FabricRecipeProvider{


        public RecipesGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        public void buildRecipes(RecipeOutput exporter) {
            //Crafting Table
            {
                //Alchemy Table
                {
                    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TCOTS_Items.ALCHEMY_TABLE_ITEM.get())
                            .pattern("B B")
                            .pattern("CWC")
                            .pattern("WWW")
                            .define('B', Items.GLASS_BOTTLE)
                            .define('C', Items.COBBLESTONE)
                            .define('W', ItemTags.PLANKS)

                            .unlockedBy(FabricRecipeProvider.getHasName(Items.GLASS_BOTTLE), FabricRecipeProvider.has(Items.GLASS_BOTTLE))
                            .save(exporter);
                }

                //Herbal Table
                {
                    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TCOTS_Items.HERBAL_TABLE_ITEM.get())
                            .pattern("FF")
                            .pattern("WW")
                            .pattern("WW")
                            .define('F', ItemTags.SMALL_FLOWERS)
                            .define('W', ItemTags.PLANKS)

                            .unlockedBy(FabricRecipeProvider.getHasName(Items.DANDELION), FabricRecipeProvider.has(Items.DANDELION))
                            .unlockedBy(FabricRecipeProvider.getHasName(Items.POPPY), FabricRecipeProvider.has(Items.POPPY))
                            .unlockedBy(FabricRecipeProvider.getHasName(Items.CORNFLOWER), FabricRecipeProvider.has(Items.CORNFLOWER))
                            .save(exporter);
                }

                //Ingredients Crafting
                {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TCOTS_Items.CURED_MONSTER_LEATHER.get(), 2)
                            .requires(TCOTS_Items.CADAVERINE.get())
                            .requires(TCOTS_Tags.MONSTER_BLOOD)
                            .requires(Items.ROTTEN_FLESH)
                            .requires(Items.LEATHER)

                            .unlockedBy(FabricRecipeProvider.getHasName(Items.LEATHER), FabricRecipeProvider.has(Items.LEATHER))
                            .save(exporter);
                }

                //Crossbows
                {
                    //Knight Crossbow
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.KNIGHT_CROSSBOW.get())
                                .pattern("IWI")
                                .pattern("LHL")
                                .pattern(" S ")
                                .define('I', Items.IRON_BLOCK)
                                .define('W', ItemTags.WOOL)
                                .define('L', Items.LEATHER)
                                .define('H', Items.TRIPWIRE_HOOK)
                                .define('S', Items.STICK)

                                .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_BLOCK), FabricRecipeProvider.has(Items.IRON_BLOCK))
                                .unlockedBy(FabricRecipeProvider.getHasName(Items.LEATHER), FabricRecipeProvider.has(Items.LEATHER))
                                .unlockedBy(FabricRecipeProvider.getHasName(Items.TRIPWIRE_HOOK), FabricRecipeProvider.has(Items.TRIPWIRE_HOOK))
                                .save(exporter);
                    }

                    //Crossbow Bolts
                    {
                        //Normal Bolt
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.BASE_BOLT.get(), 2)
                                    .pattern(" I ")
                                    .pattern("FSF")
                                    .define('I', Items.IRON_INGOT)
                                    .define('F', Items.FEATHER)
                                    .define('S', Items.STICK)

                                    .unlockedBy(FabricRecipeProvider.getHasName(Items.IRON_INGOT), FabricRecipeProvider.has(Items.IRON_INGOT))
                                    .unlockedBy(FabricRecipeProvider.getHasName(Items.FEATHER), FabricRecipeProvider.has(Items.FEATHER))
                                    .save(exporter);
                        }

                        //Blunt Bolt
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.BLUNT_BOLT.get(), 2)
                                    .pattern("  P")
                                    .pattern("L# ")
                                    .pattern("TL ")
                                    .define('P', Items.GOLD_BLOCK)
                                    .define('L', Items.IRON_INGOT)
                                    .define('T', Items.IRON_BLOCK)
                                    .define('#', TCOTS_Items.BASE_BOLT.get())

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), FabricRecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                    .save(exporter);
                        }

                        // Precision Bolt
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.PRECISION_BOLT.get(), 2)
                                    .pattern("  P")
                                    .pattern("L# ")
                                    .pattern("LL ")
                                    .define('P', Items.IRON_NUGGET)
                                    .define('L', Items.FEATHER)
                                    .define('#', TCOTS_Items.BASE_BOLT.get())

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), FabricRecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                    .save(exporter);
                        }

                        // Exploding Bolt
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.EXPLODING_BOLT.get(), 2)
                                    .pattern("  P")
                                    .pattern("L# ")
                                    .pattern("TL ")
                                    .define('P', TCOTS_Items.STAMMELFORDS_DUST.get())
                                    .define('L', Items.STRING)
                                    .define('T', Items.PAPER)
                                    .define('#', TCOTS_Items.BASE_BOLT.get())

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), FabricRecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                    .save(exporter);
                        }

                        // Broadhead Bolt
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.BROADHEAD_BOLT.get(), 2)
                                    .pattern("  P")
                                    .pattern("L# ")
                                    .pattern("TL ")
                                    .define('P', TCOTS_Items.FOGLET_TEETH.get())
                                    .define('L', Items.IRON_NUGGET)
                                    .define('T', Items.FEATHER)
                                    .define('#', TCOTS_Items.BASE_BOLT.get())

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), FabricRecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                    .save(exporter);
                        }

                    }
                }

                //Armors
                {
                    //Warrior's Leather Armor
                    {
                        //Jacket
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_JACKET.get())
                                    .pattern("I I")
                                    .pattern("LLL")
                                    .pattern("NIN")
                                    .define('L', Items.LEATHER)
                                    .define('I', Items.IRON_INGOT)
                                    .define('N', Items.IRON_NUGGET)

                                    .unlockedBy(FabricRecipeProvider.getHasName(Items.LEATHER), FabricRecipeProvider.has(Items.LEATHER))
                                    .save(exporter);
                        }

                        //Trousers
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get())
                                    .pattern("NIN")
                                    .pattern("L L")
                                    .pattern("L L")
                                    .define('L', Items.LEATHER)
                                    .define('I', Items.IRON_INGOT)
                                    .define('N', Items.IRON_NUGGET)

                                    .unlockedBy(FabricRecipeProvider.getHasName(Items.LEATHER), FabricRecipeProvider.has(Items.LEATHER))
                                    .save(exporter);
                        }

                        //Boots
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_BOOTS.get())
                                    .pattern("L L")
                                    .pattern("I I")
                                    .define('L', Items.LEATHER)
                                    .define('I', Items.IRON_INGOT)

                                    .unlockedBy(FabricRecipeProvider.getHasName(Items.LEATHER), FabricRecipeProvider.has(Items.LEATHER))
                                    .save(exporter);
                        }
                    }

                    //Manticore Armor
                    {
                        //Chestplate
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_ARMOR.get())
                                    .pattern("L L")
                                    .pattern("IEI")
                                    .pattern("ELE")
                                    .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                    .define('E', TCOTS_Items.NEKKER_EYE.get())
                                    .define('I', Items.IRON_INGOT)

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), FabricRecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                    .save(exporter);
                        }

                        //Trousers
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_TROUSERS.get())
                                    .pattern("LIL")
                                    .pattern("B B")
                                    .pattern("L L")
                                    .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                    .define('I', Items.IRON_INGOT)
                                    .define('B', TCOTS_Tags.MONSTER_BLOOD)

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), FabricRecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                    .save(exporter);
                        }

                        //Boots
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_BOOTS.get())
                                    .pattern("L L")
                                    .pattern("B B")
                                    .pattern("I I")
                                    .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                    .define('I', Items.IRON_INGOT)
                                    .define('B', TCOTS_Tags.MONSTER_BLOOD)

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), FabricRecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                    .save(exporter);
                        }
                    }

                    //Raven's Armor
                    {
                        //Chestplate
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_ARMOR.get())
                                    .pattern("D D")
                                    .pattern("LDL")
                                    .pattern("GIG")
                                    .define('D', Items.DIAMOND)
                                    .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                    .define('G', TCOTS_Items.GRAVEIR_BONE.get())
                                    .define('I', Items.IRON_INGOT)

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), FabricRecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                    .save(exporter);
                        }

                        //Trousers
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_TROUSERS.get())
                                    .pattern("BLB")
                                    .pattern("D D")
                                    .pattern("L L")
                                    .define('D', Items.DIAMOND)
                                    .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                    .define('B', TCOTS_Items.BULLVORE_HORN_FRAGMENT.get())

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), FabricRecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                    .save(exporter);
                        }

                        //Boots
                        {
                            ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_BOOTS.get())
                                    .pattern("D D")
                                    .pattern("L L")
                                    .pattern("T T")
                                    .define('D', Items.DIAMOND)
                                    .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                    .define('T', TCOTS_Items.DEVOURER_TEETH.get())

                                    .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), FabricRecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                    .save(exporter);
                        }
                    }
                }


                //Horse Armors
                {
                    //Tundra Armor
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCOTS_Items.TUNDRA_HORSE_ARMOR.get())
                                .pattern("WWI")
                                .pattern("LAI")
                                .pattern("LLL")
                                .define('L', Items.LEATHER)
                                .define('I', Items.IRON_INGOT)
                                .define('W', ItemTags.WOOL)
                                .define('A', Items.LEATHER_HORSE_ARMOR)

                                .unlockedBy(FabricRecipeProvider.getHasName(Items.LEATHER_HORSE_ARMOR), FabricRecipeProvider.has(Items.LEATHER_HORSE_ARMOR))
                                .save(exporter);
                    }
                }

                //Bone Meal from bones
                {
                    //Devourer teeth
                    {
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 12)
                                .requires(TCOTS_Items.DEVOURER_TEETH.get())
                                .group("bonemeal")

                                .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.DEVOURER_TEETH.get()), FabricRecipeProvider.has(TCOTS_Items.DEVOURER_TEETH.get()))
                                .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bone_meal_from_devourer_teeth"));
                    }

                    //Graveir bone
                    {
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 16)
                                .requires(TCOTS_Items.GRAVEIR_BONE.get())
                                .group("bonemeal")

                                .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.GRAVEIR_BONE.get()), FabricRecipeProvider.has(TCOTS_Items.GRAVEIR_BONE.get()))
                                .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bone_meal_from_graveir_bone"));
                    }
                }

                //Cadaverine crafting
                {
                    //Head
                    {
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SKELETON_SKULL)
                                .requires(TCOTS_Items.CADAVERINE.get())
                                .requires(Items.ZOMBIE_HEAD)

                                .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CADAVERINE.get()), FabricRecipeProvider.has(TCOTS_Items.CADAVERINE.get()))
                                .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cadaverine_decay_head"));
                    }

                    //Bone
                    {
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE, 2)
                                .requires(TCOTS_Items.CADAVERINE.get())
                                .requires(TCOTS_Tags.DECAYING_FLESH)

                                .unlockedBy(FabricRecipeProvider.getHasName(TCOTS_Items.CADAVERINE.get()), FabricRecipeProvider.has(TCOTS_Items.CADAVERINE.get()))
                                .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cadaverine_decay_flesh"));
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
                                order, TCOTS_Items.SWALLOW_POTION.get(),
                                ingredientsList(
                                        of(TCOTS_Items.CELANDINE.get(), 5),
                                        of(TCOTS_Items.DROWNER_BRAIN.get(), 1)))
                                .offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.SWALLOW_POTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        of(TCOTS_Items.DROWNER_BRAIN.get(), 5),
                                        of(TCOTS_Items.CELANDINE.get(), 6),
                                        of(Items.LILY_OF_THE_VALLEY, 4)),
                                TCOTS_Items.SWALLOW_POTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.SWALLOW_POTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 6,
                                        TCOTS_Items.CELANDINE.get(), 4,
                                        TCOTS_Items.CROWS_EYE.get(), 4,
                                        TCOTS_Items.VITRIOL.get(), 2),
                                TCOTS_Items.SWALLOW_POTION_ENHANCED.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotionSplash(
                                order, TCOTS_Items.SWALLOW_SPLASH.get(),
                                TCOTS_Items.SWALLOW_POTION.get()).offerTo(exporter);

                    }

                    //Cat
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.CAT_POTION.get(),
                                ingredientsList(
                                        Items.GLOW_BERRIES, 4,
                                        TCOTS_Items.WATER_ESSENCE.get(), 2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.CAT_POTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 5,
                                        Items.BROWN_MUSHROOM, 1,
                                        TCOTS_Items.WATER_ESSENCE.get(), 3),
                                TCOTS_Items.CAT_POTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.CAT_POTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 4,
                                        Items.BROWN_MUSHROOM, 4,
                                        TCOTS_Items.ALLSPICE.get(), 2,
                                        TCOTS_Items.AETHER.get(), 1),
                                TCOTS_Items.CAT_POTION_ENHANCED.get()).offerTo(exporter);
                    }

                    //White Raffard's
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get(),
                                ingredientsList(
                                        Items.OXEYE_DAISY, 2,
                                        TCOTS_Items.NEKKER_HEART.get(), 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        Items.OXEYE_DAISY, 4,
                                        TCOTS_Items.BRYONIA.get(), 1,
                                        TCOTS_Items.NEKKER_HEART.get(), 5),
                                TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.OXEYE_DAISY, 4,
                                        TCOTS_Items.BRYONIA.get(), 4,
                                        Items.FLOWERING_AZALEA, 4,
                                        TCOTS_Items.VERMILION.get(), 1),
                                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotionSplash(
                                order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.get(),
                                TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get()).offerTo(exporter);
                    }

                    //Killer Whale
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.KILLER_WHALE_POTION.get(),
                                ingredientsList(
                                        Items.KELP, 6,
                                        Items.SWEET_BERRIES, 5,
                                        TCOTS_Items.DROWNER_TONGUE.get(), 5)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotionSplash(
                                order, TCOTS_Items.KILLER_WHALE_SPLASH.get(),
                                TCOTS_Items.KILLER_WHALE_POTION.get()).offerTo(exporter);
                    }

                    //Black Blood
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.BLACK_BLOOD_POTION.get(),
                                ingredientsList(
                                        TCOTS_Items.SEWANT_MUSHROOMS.get(), 2,
                                        TCOTS_Items.GHOUL_BLOOD.get(), 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        Items.ALLIUM, 2,
                                        TCOTS_Items.SEWANT_MUSHROOMS.get(), 5,
                                        TCOTS_Items.GHOUL_BLOOD.get(), 5),
                                TCOTS_Items.BLACK_BLOOD_POTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.ALLIUM, 6,
                                        TCOTS_Items.SEWANT_MUSHROOMS.get(), 5,
                                        TCOTS_Items.HAN_FIBER.get(), 2,
                                        TCOTS_Items.REBIS.get(), 1),
                                TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.get()).offerTo(exporter);
                    }

                    //Maribor Forest
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.MARIBOR_FOREST_POTION.get(),
                                ingredientsList(
                                        Items.GLOW_BERRIES, 3,
                                        TCOTS_Items.DROWNER_TONGUE.get(), 4,
                                        TCOTS_Items.ALGHOUL_BONE_MARROW.get(), 2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 5,
                                        TCOTS_Items.CROWS_EYE.get(), 2,
                                        TCOTS_Items.DROWNER_TONGUE.get(), 2),
                                TCOTS_Items.MARIBOR_FOREST_POTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.GLOW_BERRIES, 4,
                                        TCOTS_Items.CROWS_EYE.get(), 4,
                                        Items.ALLIUM, 6,
                                        TCOTS_Items.VERMILION.get(), 1),
                                TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.get()).offerTo(exporter);
                    }

                    //Wolf
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WOLF_POTION.get(),
                                ingredientsList(
                                        Items.BONE_MEAL, 12,
                                        TCOTS_Items.DEVOURER_TEETH.get(), 2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WOLF_POTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        Items.BONE_MEAL, 12,
                                        TCOTS_Items.GHOUL_BLOOD.get(), 2,
                                        TCOTS_Items.DEVOURER_TEETH.get(), 8),
                                TCOTS_Items.WOLF_POTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WOLF_POTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.BONE_MEAL, 16,
                                        TCOTS_Items.GHOUL_BLOOD.get(), 4,
                                        TCOTS_Items.HAN_FIBER.get(), 6,
                                        TCOTS_Items.HYDRAGENUM.get(), 1),
                                TCOTS_Items.WOLF_POTION_ENHANCED.get()).offerTo(exporter);
                    }

                    //Rook
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.ROOK_POTION.get(),
                                ingredientsList(
                                        Items.POPPY, 4,
                                        TCOTS_Items.NEKKER_EYE.get(), 3)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.ROOK_POTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        Items.POPPY, 6,
                                        Items.BROWN_MUSHROOM, 4,
                                        TCOTS_Items.NEKKER_EYE.get(), 6),
                                TCOTS_Items.ROOK_POTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.ROOK_POTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.POPPY, 12,
                                        Items.BROWN_MUSHROOM, 8,
                                        TCOTS_Items.ALGHOUL_BONE_MARROW.get(), 4,
                                        TCOTS_Items.RUBEDO.get(), 1),
                                TCOTS_Items.ROOK_POTION_ENHANCED.get()).offerTo(exporter);
                    }

                    //White Honey
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_HONEY_POTION.get(),
                                ingredientsList(
                                        Items.HONEY_BOTTLE, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.get(), 1,
                                ingredientsList(
                                        Items.HONEY_BOTTLE, 2,
                                        Items.LILY_OF_THE_VALLEY, 2),
                                TCOTS_Items.WHITE_HONEY_POTION.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createPotion(
                                order, TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.get(), 2,
                                ingredientsList(
                                        Items.HONEY_BOTTLE, 4,
                                        Items.LILY_OF_THE_VALLEY, 4,
                                        Items.ALLIUM, 8,
                                        TCOTS_Items.VITRIOL.get(), 1),
                                TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.get()).offerTo(exporter);
                    }

                }


                //Decoctions
                {
                    order = order + 0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.WATER_HAG_DECOCTION.get(),
                            ingredientsList(
                                    TCOTS_Items.WATER_HAG_MUTAGEN.get(), 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.SWEET_BERRIES, 1)).offerTo(exporter);

                    order = order + 0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.GRAVE_HAG_DECOCTION.get(),
                            ingredientsList(
                                    TCOTS_Items.GRAVE_HAG_MUTAGEN.get(), 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.RED_MUSHROOM, 2,
                                    Items.BROWN_MUSHROOM, 3)).offerTo(exporter);

                    order = order + 0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.ALGHOUL_DECOCTION.get(),
                            ingredientsList(
                                    TCOTS_Items.ALGHOUL_BONE_MARROW.get(), 2,
                                    Items.ECHO_SHARD, 4,
                                    Items.KELP, 2)).offerTo(exporter);

                    order = order + 0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.FOGLET_DECOCTION.get(),
                            ingredientsList(
                                    TCOTS_Items.FOGLET_MUTAGEN.get(), 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.AZURE_BLUET, 2,
                                    Items.DANDELION, 1)).offerTo(exporter);

                    order = order + 0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.NEKKER_WARRIOR_DECOCTION.get(),
                            ingredientsList(
                                    TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get(), 1,
                                    Items.ECHO_SHARD, 1,
                                    Items.AZURE_BLUET, 2,
                                    Items.FERN, 1)).offerTo(exporter);

                    order = order + 0.01f;
                    AlchemyTableRecipeJsonBuilder.createDecoction(
                            order, TCOTS_Items.TROLL_DECOCTION.get(),
                            ingredientsList(
                                    TCOTS_Items.TROLL_MUTAGEN.get(), 1,
                                    Items.ECHO_SHARD, 4,
                                    TCOTS_Items.CROWS_EYE.get(), 4,
                                    Items.HONEY_BOTTLE, 8)).offerTo(exporter);
                }


                //Bombs
                {
                    //Grapeshot
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.GRAPESHOT.get(),
                                ingredientsList(Items.BONE_MEAL,12)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.GRAPESHOT_ENHANCED.get(),
                                ingredientsList(
                                        Items.BONE_MEAL,4,
                                        Items.DANDELION,2,
                                        TCOTS_Items.CROWS_EYE.get(),2,
                                        Items.RED_MUSHROOM, 1),
                                TCOTS_Items.GRAPESHOT.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.GRAPESHOT_SUPERIOR.get(),
                                ingredientsList(
                                        Items.BONE_MEAL,4,
                                        Items.BLAZE_POWDER,2,
                                        Items.RED_MUSHROOM,2,
                                        TCOTS_Items.NIGREDO.get(), 1),
                                TCOTS_Items.GRAPESHOT_ENHANCED.get()).offerTo(exporter);
                    }

                    //Samum
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                        order, TCOTS_Items.SAMUM.get(),
                                        ingredientsList(TCOTS_Items.CELANDINE.get(),2))
                                .offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.SAMUM_ENHANCED.get(),
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,2,
                                        TCOTS_Items.FOGLET_TEETH.get(),2,
                                        TCOTS_Items.CELANDINE.get(),1,
                                        Items.DANDELION, 1),
                                TCOTS_Items.SAMUM.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.SAMUM_SUPERIOR.get(),
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,4,
                                        TCOTS_Items.FOGLET_TEETH.get(),4,
                                        TCOTS_Items.CELANDINE.get(),1,
                                        TCOTS_Items.AETHER.get(), 1),
                                TCOTS_Items.SAMUM_ENHANCED.get()).offerTo(exporter);
                    }

                    //Dancing Star
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DANCING_STAR.get(),
                                ingredientsList(Items.BLAZE_POWDER,2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DANCING_STAR_ENHANCED.get(),
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,2,
                                        Items.BLAZE_POWDER,1,
                                        TCOTS_Items.SEWANT_MUSHROOMS.get(),1,
                                        Items.ALLIUM, 4),
                                TCOTS_Items.DANCING_STAR.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DANCING_STAR_SUPERIOR.get(),
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,4,
                                        Items.BLAZE_POWDER,2,
                                        TCOTS_Items.SEWANT_MUSHROOMS.get(),2,
                                        TCOTS_Items.NIGREDO.get(), 1),
                                TCOTS_Items.DANCING_STAR_ENHANCED.get()).offerTo(exporter);
                    }

                    //Devil's Puffball
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DEVILS_PUFFBALL.get(),
                                ingredientsList(TCOTS_Items.SEWANT_MUSHROOMS.get(),2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DEVILS_PUFFBALL_ENHANCED.get(),
                                ingredientsList(
                                        Items.BONE_MEAL,4,
                                        TCOTS_Items.SEWANT_MUSHROOMS.get(),2,
                                        Items.SPIDER_EYE,2,
                                        Items.MOSS_BLOCK, 1),
                                TCOTS_Items.DEVILS_PUFFBALL.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR.get(),
                                ingredientsList(
                                        Items.BONE_MEAL,8,
                                        TCOTS_Items.SEWANT_MUSHROOMS.get(),3,
                                        Items.SPIDER_EYE,2,
                                        TCOTS_Items.REBIS.get(), 1),
                                TCOTS_Items.DEVILS_PUFFBALL_ENHANCED.get()).offerTo(exporter);
                    }

                    //Dragon's Dream
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DRAGONS_DREAM.get(),
                                ingredientsList(Items.GLOWSTONE_DUST,4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DRAGONS_DREAM_ENHANCED.get(),
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,2,
                                        Items.AMETHYST_SHARD,1,
                                        Items.OXEYE_DAISY,2,
                                        TCOTS_Items.BRYONIA.get(), 2),
                                TCOTS_Items.DRAGONS_DREAM.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get(),
                                ingredientsList(
                                        Items.GLOWSTONE_DUST,4,
                                        Items.AMETHYST_SHARD,2,
                                        TCOTS_Items.BRYONIA.get(),2,
                                        TCOTS_Items.AETHER.get(), 1),
                                TCOTS_Items.DRAGONS_DREAM_ENHANCED.get()).offerTo(exporter);
                    }

                    //Northern Wind
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.NORTHERN_WIND.get(),
                                ingredientsList(
                                        TCOTS_Items.WATER_ESSENCE.get(),1,
                                        Items.PRISMARINE_CRYSTALS,1,
                                        TCOTS_Items.ALLSPICE.get(),2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.NORTHERN_WIND_ENHANCED.get(),
                                ingredientsList(
                                        TCOTS_Items.WATER_ESSENCE.get(),2,
                                        Items.PRISMARINE_CRYSTALS,1,
                                        TCOTS_Items.VERBENA.get(),1,
                                        TCOTS_Items.ALLSPICE.get(), 2),
                                TCOTS_Items.NORTHERN_WIND.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.create(
                                order, TCOTS_Items.NORTHERN_WIND_SUPERIOR.get(),
                                AlchemyTableRecipeCategory.BOMBS_OILS,
                                ingredientsList(
                                        TCOTS_Items.WATER_ESSENCE.get(),3,
                                        Items.PRISMARINE_CRYSTALS,2,
                                        TCOTS_Items.VERBENA.get(),2,
                                        TCOTS_Items.ALLSPICE.get(), 3,
                                        TCOTS_Items.QUEBRITH.get(), 1),
                                TCOTS_Items.NORTHERN_WIND_ENHANCED.get()).offerTo(exporter);
                    }

                    //Dimeritium Bomb
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.create(
                                order,
                                TCOTS_Items.DIMERITIUM_BOMB.get(),
                                AlchemyTableRecipeCategory.BOMBS_OILS,
                                ingredientsList(Items.AMETHYST_SHARD,2),
                                new ItemStack(Items.GUNPOWDER, 5)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DIMERITIUM_BOMB_ENHANCED.get(),
                                ingredientsList(
                                        Items.AMETHYST_SHARD,2,
                                        Items.PRISMARINE_CRYSTALS,2,
                                        Items.DANDELION,1,
                                        Items.CORNFLOWER, 3),
                                TCOTS_Items.DIMERITIUM_BOMB.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR.get(),
                                ingredientsList(
                                        Items.AMETHYST_SHARD,2,
                                        Items.PRISMARINE_CRYSTALS,4,
                                        TCOTS_Items.PUFFBALL.get(),2,
                                        TCOTS_Items.NIGREDO.get(), 1),
                                TCOTS_Items.DIMERITIUM_BOMB_ENHANCED.get()).offerTo(exporter);
                    }

                    //Moon dust Bomb
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.MOON_DUST.get(),
                                ingredientsList(Items.GHAST_TEAR,2)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.MOON_DUST_ENHANCED.get(),
                                ingredientsList(
                                        Items.GHAST_TEAR,1,
                                        Items.BLAZE_POWDER,2,
                                        Items.BEETROOT,4,
                                        Items.HONEY_BOTTLE, 1),
                                TCOTS_Items.MOON_DUST.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createBomb(
                                order, TCOTS_Items.MOON_DUST_SUPERIOR.get(),
                                ingredientsList(
                                        Items.GHAST_TEAR,2,
                                        Items.BLAZE_POWDER,4,
                                        Items.BEETROOT,6,
                                        TCOTS_Items.NIGREDO.get(), 1),
                                TCOTS_Items.MOON_DUST_ENHANCED.get()).offerTo(exporter);
                    }
                }


                //Monster Oils
                {
                    //Necrophage Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.NECROPHAGE_OIL.get(),
                                ingredientsList(Items.DANDELION, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_NECROPHAGE_OIL.get(),
                                ingredientsList(
                                        TCOTS_Items.ROTFIEND_BLOOD.get(), 4,
                                        Items.DANDELION, 4,
                                        TCOTS_Items.ARENARIA.get(), 4,
                                        Items.FLOWERING_AZALEA, 4), 4,
                                TCOTS_Items.NECROPHAGE_OIL.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get(),
                                ingredientsList(
                                        TCOTS_Items.ROTFIEND_BLOOD.get(), 4,
                                        Items.CORNFLOWER, 1,
                                        TCOTS_Items.ARENARIA.get(), 1,
                                        TCOTS_Items.HYDRAGENUM.get(), 1), 5,
                                TCOTS_Items.ENHANCED_NECROPHAGE_OIL.get()).offerTo(exporter);
                    }

                    //Ogroid Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.OGROID_OIL.get(),
                                ingredientsList(Items.CORNFLOWER, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_OGROID_OIL.get(),
                                ingredientsList(
                                        TCOTS_Items.CAVE_TROLL_LIVER.get(), 2,
                                        Items.RED_MUSHROOM, 2,
                                        Items.FERN, 3,
                                        Items.CORNFLOWER, 4), 2,
                                TCOTS_Items.OGROID_OIL.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_OGROID_OIL.get(),
                                ingredientsList(
                                        TCOTS_Items.CAVE_TROLL_LIVER.get(), 3,
                                        TCOTS_Items.ARENARIA.get(), 2,
                                        Items.FERN, 6,
                                        TCOTS_Items.AETHER.get(), 1), 2,
                                TCOTS_Items.ENHANCED_OGROID_OIL.get()).offerTo(exporter);
                    }

                    //Beast Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.BEAST_OIL.get(),
                                ingredientsList(Items.BEEF, 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_BEAST_OIL.get(),
                                ingredientsList(
                                        Items.LEATHER, 2,
                                        TCOTS_Items.CELANDINE.get(), 1,
                                        TCOTS_Items.PUFFBALL.get(), 1,
                                        Items.BEETROOT, 4), 5,
                                TCOTS_Items.BEAST_OIL.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_BEAST_OIL.get(),
                                ingredientsList(
                                        Items.ENDER_PEARL, 2,
                                        TCOTS_Items.CELANDINE.get(), 1,
                                        TCOTS_Items.PUFFBALL.get(), 1,
                                        TCOTS_Items.RUBEDO.get(), 1), 2,
                                TCOTS_Items.ENHANCED_BEAST_OIL.get()).offerTo(exporter);
                    }

                    //Hanged Man Oil
                    {
                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.HANGED_OIL.get(),
                                ingredientsList(TCOTS_Items.ARENARIA.get(), 4)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.ENHANCED_HANGED_OIL.get(),
                                ingredientsList(
                                        TCOTS_Items.HAN_FIBER.get(), 1,
                                        TCOTS_Items.NEKKER_EYE.get(), 1,
                                        Items.AZURE_BLUET, 1,
                                        TCOTS_Items.ARENARIA.get(), 1), 2,
                                TCOTS_Items.HANGED_OIL.get()).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createOil(
                                order, TCOTS_Items.SUPERIOR_HANGED_OIL.get(),
                                ingredientsList(
                                        Items.MOSS_BLOCK, 1,
                                        TCOTS_Items.ROTFIEND_BLOOD.get(), 2,
                                        Items.AZURE_BLUET, 1,
                                        TCOTS_Items.QUEBRITH.get(), 1), 2,
                                TCOTS_Items.ENHANCED_HANGED_OIL.get()).offerTo(exporter);
                    }
                }

                //Ingredients
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            new ItemStack(TCOTS_Items.DWARVEN_SPIRIT.get()),
                            ingredientsList(
                                    TCOTS_Items.ICY_SPIRIT.get(), 2,
                                    Items.LILY_OF_THE_VALLEY,1),
                            Items.GLASS_BOTTLE).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            new ItemStack(TCOTS_Items.ALCOHEST.get(), 2),
                            ingredientsList(
                                    Items.SWEET_BERRIES, 2,
                                    TCOTS_Items.CHERRY_CORDIAL.get(),1,
                                    TCOTS_Items.MANDRAKE_CORDIAL.get(),1),
                            Items.GLASS_BOTTLE).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            new ItemStack(TCOTS_Items.WHITE_GULL.get()),
                            ingredientsList(
                                    TCOTS_Items.ARENARIA.get(), 1,
                                    TCOTS_Items.VILLAGE_HERBAL.get(),1,
                                    TCOTS_Items.CHERRY_CORDIAL.get(),1,
                                    TCOTS_Items.MANDRAKE_CORDIAL.get(),1),
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
                                TCOTS_Items.AETHER.get(),
                                ingredientsList(
                                        TCOTS_Items.VERBENA.get(), 1,
                                        TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                        TCOTS_Items.HAN_FIBER.get(), 1,
                                        TCOTS_Items.PUFFBALL.get(), 1,
                                        Items.RED_MUSHROOM, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.HYDRAGENUM.get(),
                                ingredientsList(
                                        TCOTS_Items.VERBENA.get(), 1,
                                        TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                        Items.FERN, 1,
                                        Items.MOSS_BLOCK, 1,
                                        Items.GLOW_LICHEN, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.NIGREDO.get(),
                                ingredientsList(
                                        TCOTS_Items.CROWS_EYE.get(), 1,
                                        TCOTS_Items.HAN_FIBER.get(), 1,
                                        Items.ALLIUM, 1,
                                        Items.GLOW_LICHEN, 1,
                                        Items.SWEET_BERRIES, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.QUEBRITH.get(),
                                ingredientsList(
                                        TCOTS_Items.VERBENA.get(), 1,
                                        TCOTS_Items.PUFFBALL.get(), 1,
                                        Items.RED_MUSHROOM, 1,
                                        Items.GLOW_LICHEN, 1,
                                        Items.FLOWERING_AZALEA, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.REBIS.get(),
                                ingredientsList(
                                        TCOTS_Items.VERBENA.get(), 1,
                                        TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                        TCOTS_Items.ALLSPICE.get(), 1,
                                        Items.OXEYE_DAISY, 1,
                                        Items.FERN, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.RUBEDO.get(),
                                ingredientsList(
                                        TCOTS_Items.CROWS_EYE.get(), 1,
                                        TCOTS_Items.HAN_FIBER.get(), 1,
                                        Items.OXEYE_DAISY, 1,
                                        TCOTS_Items.PUFFBALL.get(), 1,
                                        Items.MOSS_BLOCK, 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.VERMILION.get(),
                                ingredientsList(
                                        TCOTS_Items.VERBENA.get(), 1,
                                        TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                        TCOTS_Items.HAN_FIBER.get(), 1,
                                        Items.POPPY, 1,
                                        TCOTS_Items.BRYONIA.get(), 1)).offerTo(exporter);

                        order=order+0.01f;
                        AlchemyTableRecipeJsonBuilder.createMisc(
                                order,
                                TCOTS_Items.VITRIOL.get(),
                                ingredientsList(
                                        Items.MOSS_BLOCK, 1,
                                        TCOTS_Items.ALLSPICE.get(), 1,
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
                                .create(TCOTS_Items.ARENARIA.get().getDefaultInstance(),
                                        List.of(MobEffects.DAMAGE_BOOST.value(), MobEffects.WITHER.value()), 20)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.BRYONIA.get().getDefaultInstance(),
                                        List.of(MobEffects.SATURATION.value(), MobEffects.HARM.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.CELANDINE.get().getDefaultInstance(),
                                        List.of(MobEffects.REGENERATION.value(), MobEffects.MOVEMENT_SLOWDOWN.value()), 40, 2)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.VERBENA.get().getDefaultInstance(),
                                        List.of(MobEffects.DIG_SPEED.value(), MobEffects.MOVEMENT_SLOWDOWN.value()), 80, 3)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.HAN_FIBER.get().getDefaultInstance(),
                                        List.of(MobEffects.FIRE_RESISTANCE.value(), MobEffects.BLINDNESS.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.CROWS_EYE.get().getDefaultInstance(),
                                        List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.POISON.value()))
                                .offerTo(exporter);
                    }

                    //Mushrooms
                    {
                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.PUFFBALL.get().getDefaultInstance(),
                                        List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(TCOTS_Items.SEWANT_MUSHROOMS.get().getDefaultInstance(),
                                        List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.RED_MUSHROOM.getDefaultInstance(),
                                        List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.BROWN_MUSHROOM.getDefaultInstance(),
                                        List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                                .offerTo(exporter);
                    }

                    //SuspiciousStew
                    {
                        HerbalTableRecipeJsonBuilder
                                .create(Items.ALLIUM.getDefaultInstance(),
                                        List.of(MobEffects.FIRE_RESISTANCE.value(), MobEffects.HUNGER.value()), 40, 1)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.AZURE_BLUET.getDefaultInstance(),
                                        List.of(MobEffects.INVISIBILITY.value(), MobEffects.BLINDNESS.value()), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.BLUE_ORCHID.getDefaultInstance(),
                                        List.of(MobEffects.SATURATION.value(), MobEffects.CONFUSION.value()), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.DANDELION.getDefaultInstance(),
                                        List.of(MobEffects.SATURATION.value(), MobEffects.CONFUSION.value()), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.CORNFLOWER.getDefaultInstance(),
                                        List.of(MobEffects.JUMP.value(), MobEffects.MOVEMENT_SLOWDOWN.value()), 40, 3)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.LILY_OF_THE_VALLEY.getDefaultInstance(),
                                        List.of(MobEffects.WATER_BREATHING.value(), MobEffects.POISON.value()), 80)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.OXEYE_DAISY.getDefaultInstance(),
                                        List.of(MobEffects.REGENERATION.value(), MobEffects.WEAKNESS.value()), 40, 2)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.POPPY.getDefaultInstance(),
                                        List.of(MobEffects.NIGHT_VISION.value(), MobEffects.DIG_SLOWDOWN.value()), 20)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.TORCHFLOWER.getDefaultInstance(),
                                        List.of(MobEffects.NIGHT_VISION.value(), MobEffects.DIG_SLOWDOWN.value()), 20)
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.ORANGE_TULIP.getDefaultInstance(),
                                        List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.PINK_TULIP.getDefaultInstance(),
                                        List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.RED_TULIP.getDefaultInstance(),
                                        List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
                                .offerTo(exporter);

                        HerbalTableRecipeJsonBuilder
                                .create(Items.WHITE_TULIP.getDefaultInstance(),
                                        List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
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

        private List<ItemStack> ingredientsList(ItemStack... items){
            return new ArrayList<>(Arrays.asList(items));
        }

        private ItemStack of(Item item, int quantity){
            return new ItemStack(item, quantity);
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
        //x Find and defeat an Ice Giant (The Lord of Ice)
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

        protected AdvancementsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, registryLookup);
        }

        @SuppressWarnings("all")
        private void generateBasicRecipeAdvancement(String id, Consumer<AdvancementHolder> consumer){
            Advancement.Builder.recipeAdvancement()
                    .addCriterion(FabricRecipeProvider.getHasName(TCOTS_Items.ALCHEMY_TABLE_ITEM.get()), FabricRecipeProvider.has(TCOTS_Items.ALCHEMY_TABLE_ITEM.get()))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .addCriterion(FabricRecipeProvider.getHasName(TCOTS_Items.ALCHEMY_BOOK.get()), FabricRecipeProvider.has(TCOTS_Items.ALCHEMY_BOOK.get()))
                    .rewards(AdvancementRewards.Builder.recipe(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id)))
                    .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"recipes/alchemy")+"/"+id);

        }

        @Override
        public void generateAdvancement(HolderLookup.Provider wrapperLookup, Consumer<AdvancementHolder> consumer) {
            //Start recipes
            this.generateBasicRecipeAdvancement("swallow_potion", consumer);
            this.generateBasicRecipeAdvancement("cat_potion", consumer);

            this.generateBasicRecipeAdvancement("samum", consumer);
            this.generateBasicRecipeAdvancement("grapeshot", consumer);

            this.generateBasicRecipeAdvancement("oil_necrophage", consumer);
            this.generateBasicRecipeAdvancement("oil_specter", consumer);

            this.generateBasicRecipeAdvancement("dwarven_spirit", consumer);
            this.generateBasicRecipeAdvancement("alcohest", consumer);


            AdvancementHolder rootAdvancementWitcher = Advancement.Builder.advancement()
                    .display(
                            TCOTS_Items.WITCHER_BESTIARY.get(), // The display icon
                            Component.translatable("advancements.witcher.main.title"), // The title
                            Component.translatable("advancements.witcher.main.description"), // The description
                            ResourceLocation.parse("textures/gui/advancements/backgrounds/stone.png"), // Background image used
                            AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                            false, // Show toast top right
                            false, // Announce to chat
                            false // Hidden in the advancement tab
                    )
                    // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                    .addCriterion("start_mod", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE))
                    .save(consumer, TCOTS_Main.MOD_ID + "/root");

            //Hunting Branch
            {
                AdvancementHolder rootHunting =
                        requireListedMobsKilled(Advancement.Builder.advancement(), MONSTERS)
                                .parent(rootAdvancementWitcher)
                                .display(TCOTS_Items.GRAVEIR_BONE.get(), // The display icon
                                        Component.translatable("advancements.witcher.start_killing.title"), // The title
                                        Component.translatable("advancements.witcher.start_killing.description"), // The description
                                        null,
                                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                        true, // Show toast top right
                                        true, // Announce to chat
                                        false // Hidden in the advancement tab
                                )
                                .save(consumer, TCOTS_Main.MOD_ID + "/hunting");


                Advancement.Builder.advancement()
                                .parent(rootHunting)
                                .display(
                                        TCOTS_Items.WINTERS_BLADE.get(), // The display icon
                                        Component.translatable("advancements.witcher.kill_giant.title"), // The title
                                        Component.translatable("advancements.witcher.kill_giant.description"), // The description
                                        null,
                                        AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                        true, // Show toast top right
                                        true, // Announce to chat
                                        false // Hidden in the advancement tab
                                )
                                .addCriterion("kill_giant", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceGiant())))
                                .rewards(AdvancementRewards.Builder.experience(100))
                                .save(consumer, TCOTS_Main.MOD_ID + "/kill_giant");

                //Bullvore branch
                {
                    AdvancementHolder killBullvore =
                            Advancement.Builder.advancement()
                                    .parent(rootHunting)
                                    .display(
                                            TCOTS_Items.BULLVORE_HORN_FRAGMENT.get(), // The display icon
                                            Component.translatable("advancements.witcher.kill_bullvore.title"), // The title
                                            Component.translatable("advancements.witcher.kill_bullvore.description"), // The description
                                            null,
                                            AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                            true, // Show toast top right
                                            true, // Announce to chat
                                            false // Hidden in the advancement tab
                                    )
                                    .addCriterion("kill_bullvore", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TCOTS_Entities.Bullvore())))
                                    .save(consumer, TCOTS_Main.MOD_ID + "/kill_bullvore");

                    Advancement.Builder.advancement()
                            .parent(killBullvore)
                            .display(
                                    TCOTS_Items.GVALCHIR.get(), // The display icon
                                    Component.translatable("advancements.witcher.get_gvalchir.title"), // The title
                                    Component.translatable("advancements.witcher.get_gvalchir.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("get_gvalchir", InventoryChangeTrigger.TriggerInstance.hasItems(TCOTS_Items.GVALCHIR.get()))
                            .rewards(AdvancementRewards.Builder.experience(100))
                            .save(consumer, TCOTS_Main.MOD_ID + "/get_gvalchir");
                }


                Advancement.Builder.advancement()
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.ROTFIEND_BLOOD.get(), // The display icon
                                Component.translatable("advancements.witcher.kill_rotfiend.title"), // The title
                                Component.translatable("advancements.witcher.kill_rotfiend.description"), // The description
                                null,
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("kill_rotfiend", TCOTS_CustomCriterion.Conditions.createKillRotfiendCriterion())
                        .save(consumer, TCOTS_Main.MOD_ID + "/kill_rotfiend");

                setHasItemCriteriaOR(Advancement.Builder.advancement(), MUTAGEN)
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.FOGLET_MUTAGEN.get(), // The display icon
                                Component.translatable("advancements.witcher.get_mutagen.title"), // The title
                                Component.translatable("advancements.witcher.get_mutagen.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .save(consumer, TCOTS_Main.MOD_ID + "/get_mutagen");

                //Troll branch
                {
                    AdvancementHolder befriendTroll =Advancement.Builder.advancement()
                            .parent(rootHunting)
                            .display(
                                    TCOTS_Items.VILLAGE_HERBAL.get(), // The display icon
                                    Component.translatable("advancements.witcher.befriend_troll.title"), // The title
                                    Component.translatable("advancements.witcher.befriend_troll.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("befriend_rock", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.RockTroll())))
                            .requirements(AdvancementRequirements.Strategy.OR)
                            .addCriterion("befriend_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceTroll())))
                            .requirements(AdvancementRequirements.Strategy.OR)
                            .addCriterion("befriend_forest", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.ForestTroll())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/befriend_troll");

                    AdvancementHolder befriendIceTroll = Advancement.Builder.advancement()
                            .parent(befriendTroll)
                            .display(
                                    Blocks.PACKED_ICE, // The display icon
                                    Component.translatable("advancements.witcher.befriend_troll_ice.title"), // The title
                                    Component.translatable("advancements.witcher.befriend_troll_ice.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("befriend_troll_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceTroll())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/befriend_troll_ice");

                    Advancement.Builder.advancement()
                            .parent(befriendIceTroll)
                            .display(
                                    Blocks.OAK_SAPLING, // The display icon
                                    Component.translatable("advancements.witcher.befriend_all_troll.title"), // The title
                                    Component.translatable("advancements.witcher.befriend_all_troll.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("befriend_rock", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.RockTroll())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion("befriend_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceTroll())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion("befriend_forest", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.ForestTroll())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/befriend_all_troll");
                }

                Advancement.Builder.advancement()
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.RAVENS_ARMOR.get(), // The display icon
                                Component.translatable("advancements.witcher.get_ravens_armor.title"), // The title
                                Component.translatable("advancements.witcher.get_ravens_armor.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("get_ravens_armor", InventoryChangeTrigger.TriggerInstance.hasItems(TCOTS_Items.RAVENS_ARMOR.get(), TCOTS_Items.RAVENS_TROUSERS.get(), TCOTS_Items.RAVENS_BOOTS.get()))
                        .rewards(AdvancementRewards.Builder.experience(100))
                        .save(consumer, TCOTS_Main.MOD_ID + "/get_ravens_armor");
            }


            //Alchemy Branch
            {
                AdvancementHolder rootAlchemy =
                        Advancement.Builder.advancement()
                                .parent(rootAdvancementWitcher)
                                .display(
                                        TCOTS_Items.ALCHEMY_TABLE_ITEM.get(), // The display icon
                                        Component.translatable("advancements.witcher.start_alchemy.title"), // The title
                                        Component.translatable("advancements.witcher.start_alchemy.description"), // The description
                                        null,
                                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                        true, // Show toast top right
                                        true, // Announce to chat
                                        false // Hidden in the advancement tab
                                )
                                .addCriterion("start_alchemy", InventoryChangeTrigger.TriggerInstance.hasItems(TCOTS_Blocks.AlchemyTable()))
                                .save(consumer, TCOTS_Main.MOD_ID + "/alchemy");

                //Potions branch
                {
                    AdvancementHolder craftPotion = setHasItemCriteriaOR(Advancement.Builder.advancement(), POTIONS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.SWALLOW_POTION.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_potion.title"), // The title
                                    Component.translatable("advancements.witcher.craft_potion.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_potion");


                    setHasItemCriteriaOR(Advancement.Builder.advancement(), DECOCTIONS)
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.GRAVE_HAG_DECOCTION.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_decoction.title"), // The title
                                    Component.translatable("advancements.witcher.craft_decoction.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_decoction");

                    setHasItemCriteriaOR(Advancement.Builder.advancement(), POTION_LV3)
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.SWALLOW_POTION_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_potion_superior.title"), // The title
                                    Component.translatable("advancements.witcher.craft_potion_superior.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_potion_superior");

                    Advancement.Builder.advancement()
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.NEST_SKULL_ITEM.get(), // The display icon
                                    Component.translatable("advancements.witcher.max_toxicity.title"), // The title
                                    Component.translatable("advancements.witcher.max_toxicity.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "max_toxicity",
                                    TCOTS_CustomCriterion.Conditions.createMaxToxicityCriterion())
                            .save(consumer, TCOTS_Main.MOD_ID + "/max_toxicity");
                }

                //Oils branch
                {
                    AdvancementHolder craftOil = setHasItemCriteriaOR(Advancement.Builder.advancement(), OILS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.NECROPHAGE_OIL.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_oil.title"), // The title
                                    Component.translatable("advancements.witcher.craft_oil.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_oil");

                    setHasItemCriteriaOR(Advancement.Builder.advancement(), OILS_LV3)
                            .parent(craftOil)
                            .display(
                                    TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_oil_superior.title"), // The title
                                    Component.translatable("advancements.witcher.craft_oil_superior.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_oil_superior");

                    Advancement.Builder.advancement()
                            .parent(craftOil)
                            .display(
                                    Items.IRON_SWORD, // The display icon
                                    Component.translatable("advancements.witcher.kill_with_hanged.title"), // The title
                                    Component.translatable("advancements.witcher.kill_with_hanged.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "kill_with_hanged",
                                    TCOTS_CustomCriterion.Conditions.createKillWithHangedCriterion())
                            .save(consumer, TCOTS_Main.MOD_ID + "/kill_with_hanged");
                }

                //Bombs branch
                {
                    AdvancementHolder craftBomb = setHasItemCriteriaOR(Advancement.Builder.advancement(), BOMBS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.GRAPESHOT.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_bomb.title"), // The title
                                    Component.translatable("advancements.witcher.craft_bomb.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_bomb");

                    AdvancementHolder craftBombLV3 = setHasItemCriteriaOR(Advancement.Builder.advancement(), BOMBS_LV3)
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.GRAPESHOT_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_bomb_superior.title"), // The title
                                    Component.translatable("advancements.witcher.craft_bomb_superior.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_bomb_superior");

                    Advancement.Builder.advancement()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.DIMERITIUM_BOMB.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_all_bomb.title"), // The title
                                    Component.translatable("advancements.witcher.craft_all_bomb.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )

                            .addCriterion(
                                    "craft_grapeshot",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.GRAPESHOT.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_samum",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.SAMUM.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_dancing",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DANCING_STAR.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_puffball",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DEVILS_PUFFBALL.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_dragons",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DRAGONS_DREAM.get())))
                            .addCriterion(
                                    "craft_northern",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.NORTHERN_WIND.get())))
                            .addCriterion(
                                    "craft_dimeritium",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DIMERITIUM_BOMB.get())))
                            .addCriterion(
                                    "craft_moon_dust",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.MOON_DUST.get())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_all_bomb");

                    AdvancementHolder explodeNest = Advancement.Builder.advancement()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Blocks.MonsterNest(), // The display icon
                                    Component.translatable("advancements.witcher.destroy_nest.title"), // The title
                                    Component.translatable("advancements.witcher.destroy_nest.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "destroy_nest",
                                    TCOTS_CustomCriterion.Conditions.createDestroyNestCriterion())
                            .save(consumer, TCOTS_Main.MOD_ID + "/destroy_nest");

                    Advancement.Builder.advancement()
                            .parent(explodeNest)
                            .display(
                                    TCOTS_Blocks.MonsterNest(), // The display icon
                                    Component.translatable("advancements.witcher.destroy_nest_multiple.title"), // The title
                                    Component.translatable("advancements.witcher.destroy_nest_multiple.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "destroy_nest_multiple",
                                    DestroyMultipleMonsterNestsCriterion.Conditions.createMultipleDestroyNestCriterion(30))
                            .rewards(AdvancementRewards.Builder.experience(500))
                            .save(consumer, TCOTS_Main.MOD_ID + "/destroy_nest_multiple");

                    Advancement.Builder.advancement()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.dragons_dream_burning.title"), // The title
                                    Component.translatable("advancements.witcher.dragons_dream_burning.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "dragons_dream_burning",
                                    TCOTS_CustomCriterion.Conditions.createDragonsDreamBurningCriterion())
                            .rewards(AdvancementRewards.Builder.experience(150))
                            .save(consumer, TCOTS_Main.MOD_ID + "/dragons_dream_burning");

                    Advancement.Builder.advancement()
                            .parent(craftBombLV3)
                            .display(
                                    TCOTS_Items.MOON_DUST_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.stop_creeper.title"), // The title
                                    Component.translatable("advancements.witcher.stop_creeper.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "stop_creeper",
                                    TCOTS_CustomCriterion.Conditions.createStopCreeperCriterion())
                            .rewards(AdvancementRewards.Builder.experience(150))
                            .save(consumer, TCOTS_Main.MOD_ID + "/stop_creeper");
                }

                AdvancementHolder useAlchemyFormula = Advancement.Builder.advancement()
                        .parent(rootAlchemy)
                        .display(
                                TCOTS_Items.ALCHEMY_FORMULA.get(), // The display icon
                                Component.translatable("advancements.witcher.use_formula.title"), // The title
                                Component.translatable("advancements.witcher.use_formula.description"), // The description
                                null, // Background image used
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion(
                                "use_formula",
                                ConsumeItemTrigger.TriggerInstance.usedItem(TCOTS_Items.ALCHEMY_FORMULA.get()))
                        .save(consumer, TCOTS_Main.MOD_ID + "/use_formula");

                Advancement.Builder.advancement()
                        .parent(rootAlchemy)
                        .display(
                                TCOTS_Items.ALCOHEST.get(), // The display icon
                                Component.translatable("advancements.witcher.refill_concoction.title"), // The title
                                Component.translatable("advancements.witcher.refill_concoction.description"), // The description
                                null, // Background image used
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion(
                                "refill_concoction",
                                TCOTS_CustomCriterion.Conditions.createRefillConcoctionCriterion())
                        .save(consumer, TCOTS_Main.MOD_ID + "/refill_concoction");
            }
        }

        protected static final List<Item> MUTAGEN = Arrays.asList(
                TCOTS_Items.FOGLET_MUTAGEN.get(),
                TCOTS_Items.TROLL_MUTAGEN.get(),
                TCOTS_Items.GRAVE_HAG_MUTAGEN.get(),

                TCOTS_Items.WATER_HAG_MUTAGEN.get(),
                TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get()
        );

        protected static final List<Item> BOMBS_LV3 = Arrays.asList(
                TCOTS_Items.GRAPESHOT_SUPERIOR.get(),

                TCOTS_Items.DANCING_STAR_SUPERIOR.get(),

                TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR.get(),

                TCOTS_Items.SAMUM_SUPERIOR.get(),

                TCOTS_Items.NORTHERN_WIND_SUPERIOR.get(),

                TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get(),

                TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR.get(),

                TCOTS_Items.MOON_DUST_SUPERIOR.get()
        );

        protected static final List<Item> BOMBS = Arrays.asList(
        TCOTS_Items.GRAPESHOT.get(),
        TCOTS_Items.GRAPESHOT_ENHANCED.get(),
        TCOTS_Items.GRAPESHOT_SUPERIOR.get(),

        TCOTS_Items.DANCING_STAR.get(),
        TCOTS_Items.DANCING_STAR_ENHANCED.get(),
        TCOTS_Items.DANCING_STAR_SUPERIOR.get(),

        TCOTS_Items.DEVILS_PUFFBALL.get(),
        TCOTS_Items.DEVILS_PUFFBALL_ENHANCED.get(),
        TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR.get(),

        TCOTS_Items.SAMUM.get(),
        TCOTS_Items.SAMUM_ENHANCED.get(),
        TCOTS_Items.SAMUM_SUPERIOR.get(),

        TCOTS_Items.NORTHERN_WIND.get(),
        TCOTS_Items.NORTHERN_WIND_ENHANCED.get(),
        TCOTS_Items.NORTHERN_WIND_SUPERIOR.get(),

        TCOTS_Items.DRAGONS_DREAM.get(),
        TCOTS_Items.DRAGONS_DREAM_ENHANCED.get(),
        TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get(),

        TCOTS_Items.DIMERITIUM_BOMB.get(),
        TCOTS_Items.DIMERITIUM_BOMB_ENHANCED.get(),
        TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR.get(),

        TCOTS_Items.MOON_DUST.get(),
        TCOTS_Items.MOON_DUST_ENHANCED.get(),
        TCOTS_Items.MOON_DUST_SUPERIOR.get()
        );

        protected static final List<Item> OILS_LV3 = Arrays.asList(
                TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get(),

                TCOTS_Items.SUPERIOR_OGROID_OIL.get(),

                TCOTS_Items.SUPERIOR_BEAST_OIL.get(),

                TCOTS_Items.SUPERIOR_HANGED_OIL.get()
        );

        protected static final List<Item> OILS = Arrays.asList(
                TCOTS_Items.NECROPHAGE_OIL.get(),
                TCOTS_Items.ENHANCED_NECROPHAGE_OIL.get(),
                TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get(),

                TCOTS_Items.OGROID_OIL.get(),
                TCOTS_Items.ENHANCED_OGROID_OIL.get(),
                TCOTS_Items.SUPERIOR_OGROID_OIL.get(),

                TCOTS_Items.BEAST_OIL.get(),
                TCOTS_Items.ENHANCED_BEAST_OIL.get(),
                TCOTS_Items.SUPERIOR_BEAST_OIL.get(),

                TCOTS_Items.HANGED_OIL.get(),
                TCOTS_Items.ENHANCED_HANGED_OIL.get(),
                TCOTS_Items.SUPERIOR_HANGED_OIL.get()
        );

        protected static final List<EntityType<?>> MONSTERS = Arrays.asList(
                TCOTS_Entities.Drowner(),
                TCOTS_Entities.Rotfiend(),
                TCOTS_Entities.Foglet(),
                TCOTS_Entities.GraveHag(),
                TCOTS_Entities.WaterHag(),
                TCOTS_Entities.Ghoul(),
                TCOTS_Entities.Alghoul(),
                TCOTS_Entities.Scurver(),
                TCOTS_Entities.Devourer(),
                TCOTS_Entities.Graveir(),
                TCOTS_Entities.Bullvore(),

                TCOTS_Entities.Nekker(),
                TCOTS_Entities.NekkerWarrior(),
                TCOTS_Entities.Cyclops(),
                TCOTS_Entities.RockTroll(),
                TCOTS_Entities.IceTroll(),
                TCOTS_Entities.ForestTroll(),
                TCOTS_Entities.IceGiant()
        );

        protected static final List<Item> POTION_LV3= Arrays.asList(
                TCOTS_Items.SWALLOW_POTION_SUPERIOR.get(),
                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.get(),
                TCOTS_Items.CAT_POTION_SUPERIOR.get(),
                TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.get(),
                TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.get(),
                TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.get(),
                TCOTS_Items.WOLF_POTION_SUPERIOR.get(),
                TCOTS_Items.ROOK_POTION_SUPERIOR.get()
        );

        protected static final List<Item> POTIONS = Arrays.asList(
                TCOTS_Items.SWALLOW_POTION.get(),
                TCOTS_Items.SWALLOW_POTION_ENHANCED.get(),
                TCOTS_Items.SWALLOW_POTION_SUPERIOR.get(),

                TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get(),
                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.get(),
                TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.get(),

                TCOTS_Items.CAT_POTION.get(),
                TCOTS_Items.CAT_POTION_ENHANCED.get(),
                TCOTS_Items.CAT_POTION_SUPERIOR.get(),

                TCOTS_Items.BLACK_BLOOD_POTION.get(),
                TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.get(),
                TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.get(),

                TCOTS_Items.MARIBOR_FOREST_POTION.get(),
                TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.get(),
                TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.get(),

                TCOTS_Items.KILLER_WHALE_POTION.get(),

                TCOTS_Items.WHITE_HONEY_POTION.get(),
                TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.get(),
                TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.get(),

                TCOTS_Items.WOLF_POTION.get(),
                TCOTS_Items.WOLF_POTION_ENHANCED.get(),
                TCOTS_Items.WOLF_POTION_SUPERIOR.get(),

                TCOTS_Items.ROOK_POTION.get(),
                TCOTS_Items.ROOK_POTION_ENHANCED.get(),
                TCOTS_Items.ROOK_POTION_SUPERIOR.get(),

                TCOTS_Items.ALGHOUL_DECOCTION.get(),
                TCOTS_Items.GRAVE_HAG_DECOCTION.get(),
                TCOTS_Items.WATER_HAG_DECOCTION.get(),
                TCOTS_Items.FOGLET_DECOCTION.get(),
                TCOTS_Items.TROLL_DECOCTION.get(),
                TCOTS_Items.NEKKER_WARRIOR_DECOCTION.get()
        );

        protected static final List<Item> DECOCTIONS = Arrays.asList(
                TCOTS_Items.ALGHOUL_DECOCTION.get(),
                TCOTS_Items.GRAVE_HAG_DECOCTION.get(),
                TCOTS_Items.WATER_HAG_DECOCTION.get(),
                TCOTS_Items.FOGLET_DECOCTION.get(),
                TCOTS_Items.TROLL_DECOCTION.get(),
                TCOTS_Items.NEKKER_WARRIOR_DECOCTION.get()
        );

        @SuppressWarnings("all")
        private static Advancement.Builder requireListedMobsKilled(Advancement.Builder builder, List<EntityType<?>> entityTypes) {
            entityTypes.forEach(type -> builder.addCriterion(
                    BuiltInRegistries.ENTITY_TYPE.getKey(type).toString(),
                    KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(type)))
                    .requirements(AdvancementRequirements.Strategy.OR));
            return builder;
        }

        private static Advancement.Builder setHasItemCriteriaOR(Advancement.Builder builder, List<Item> entityTypes) {
            entityTypes.forEach(item -> builder.addCriterion(
                            BuiltInRegistries.ITEM.getKey(item).toString(),
                            InventoryChangeTrigger.TriggerInstance.hasItems(item))
                    .requirements(AdvancementRequirements.Strategy.OR));
            return builder;
        }

        private static Advancement.Builder requireMultipleNests(Advancement.Builder builder) {
            List<Pair<String, Criterion<LootTableTrigger.TriggerInstance>>> list =

                    List.of(Pair.of("desert_pyramid", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY)),
                            Pair.of("desert_well", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY)),
                            Pair.of("ocean_ruin_cold", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)),
                            Pair.of("ocean_ruin_warm", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)),
                            Pair.of("trail_ruins_rare", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE)),
                            Pair.of("trail_ruins_common", LootTableTrigger.TriggerInstance.lootTableUsed(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON)));

            list.forEach(pair -> builder.addCriterion(pair.getFirst(), pair.getSecond()));
            String string = "has_sherd";
            builder.addCriterion("has_sherd", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.DECORATED_POT_SHERDS)));

            builder.requirements(new AdvancementRequirements(List.of(list.stream().map(Pair::getFirst).toList(), List.of("has_sherd"))));
            return builder;
        }
    }
}



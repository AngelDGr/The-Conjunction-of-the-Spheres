package mors.neoforge.tcots.datagen.providers;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.plants.SewantMushroomsPlant;
import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.registry.TCOTS_Items;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class TCOTS_LootTablesBlocksGenerator extends BlockLootSubProvider {

    public TCOTS_LootTablesBlocksGenerator(final HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID)).map(Map.Entry::getValue).toList();
    }

    @Override
    public void generate() {
        final HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        this.add(TCOTS_Blocks.FrostedSnow(), noDrop());

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
                                        .apply(ApplyBonusCount.addUniformBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE)))
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
                                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE),
                                                0.5714286f, 2))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CelandinePlant())
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(CropBlock.AGE, 2)))
                                )
                        )

                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items.CELANDINE.get())
                                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE),
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
                                        .apply(ApplyBonusCount.addUniformBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CrowsEyeFern())
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(CropBlock.AGE, 2)))
                                )
                        )

                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items.CROWS_EYE.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(ApplyBonusCount.addUniformBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE)))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TCOTS_Blocks.CrowsEyeFern())
                                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                                        .hasProperty(CropBlock.AGE, 3)))
                                )
                        )

                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items.CROWS_EYE.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                        .apply(ApplyBonusCount.addUniformBonusCount(enchantmentRegistryLookup.getOrThrow(Enchantments.FORTUNE)))
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
                this.add(TCOTS_Blocks.HanFiberPlant(), plantCropLootTable(enchantmentRegistryLookup, TCOTS_Items.HAN_FIBER.get(), TCOTS_Blocks.HanFiberPlant(), 2, 3));
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
                                                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchantmentRegistryLookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))
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
                                                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchantmentRegistryLookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))
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
                this.add(TCOTS_Blocks.VerbenaFlower(), this.plantCropLootTable(enchantmentRegistryLookup, TCOTS_Items.VERBENA.get(), TCOTS_Blocks.VerbenaFlower(),1, 3));
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
                                                                                        ItemEnchantmentsPredicate.enchantments(List.of(new EnchantmentPredicate(enchantmentRegistryLookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1))))
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
                this.add(TCOTS_Blocks.SKELETON_BLOCK.get(), LootTable.lootTable()
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

    protected LootPool.Builder bryoniaPool(final Direction direction){
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

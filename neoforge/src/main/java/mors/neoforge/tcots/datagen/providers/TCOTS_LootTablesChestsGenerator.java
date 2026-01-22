package mors.neoforge.tcots.datagen.providers;

import mors.tcots.TCOTS_Main;
import mors.tcots.items.AlchemyRecipeRandomlyLootFunction;
import mors.tcots.registry.TCOTS_Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class TCOTS_LootTablesChestsGenerator implements LootTableSubProvider {

    public static final ResourceKey<LootTable> PLAINS_HERBALIST_CHEST = register( "chests/village/plains_herbalist");
    public static final ResourceKey<LootTable> TAIGA_HERBALIST_CHEST = register( "chests/village/taiga_herbalist");
    public static final ResourceKey<LootTable> SNOWY_HERBALIST_CHEST = register("chests/village/snowy_herbalist");
    public static final ResourceKey<LootTable> DESERT_HERBALIST_CHEST = register("chests/village/desert_herbalist");
    public static final ResourceKey<LootTable> SAVANNA_HERBALIST_CHEST = register("chests/village/savanna_herbalist");

    public static final ResourceKey<LootTable> TROLL_BARREL = register( "chests/troll/troll_barrel");
    public static final ResourceKey<LootTable> ICE_TROLL_BARREL = register( "chests/troll/ice_troll_barrel");
    public static final ResourceKey<LootTable> FOREST_TROLL_BARREL = register("chests/troll/forest_troll_barrel");

    public static final ResourceKey<LootTable> ICE_GIANT_CAVE_NEST = register("chests/ice_giant_treasure");


    public TCOTS_LootTablesChestsGenerator(final HolderLookup.Provider lookupProvider) {
        // Store the lookupProvider in a field
    }

    private static ResourceKey<LootTable> register(final String id) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id));
    }

    @Override
    public void generate(final BiConsumer<ResourceKey<LootTable>, LootTable.Builder> exporter) {

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
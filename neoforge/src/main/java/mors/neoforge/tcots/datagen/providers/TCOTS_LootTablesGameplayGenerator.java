package mors.neoforge.tcots.datagen.providers;

import mors.tcots.registry.TCOTS_Entities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.SequentialEntry;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class TCOTS_LootTablesGameplayGenerator implements LootTableSubProvider {

    private final HolderLookup.Provider lookupProvider;

    public TCOTS_LootTablesGameplayGenerator(final HolderLookup.Provider lookupProvider) {
        this.lookupProvider=lookupProvider;
    }

    @Override
    public void generate(@NotNull final BiConsumer<ResourceKey<LootTable>, LootTable.Builder> biConsumer) {
        final HolderLookup.RegistryLookup<Enchantment> enchantmentRegistryLookup = this.lookupProvider.lookupOrThrow(Registries.ENCHANTMENT);

        biConsumer.accept(TCOTS_Entities.ROCK_TROLL_BARTERING,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))

                                .add(LootItem.lootTableItem(Items.AMETHYST_BLOCK).setWeight(5)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(10)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.SILK_TOUCH))))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(10)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.UNBREAKING))))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(10)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.EFFICIENCY))))

                                .add(LootItem.lootTableItem(Items.COAL).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 24))))

                                .add(LootItem.lootTableItem(Items.FLINT).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12))))

                                .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 24))))

                                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 24))))

                                .add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(25)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12))))

                                .add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(25)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12))))

                                .add(LootItem.lootTableItem(Items.RAW_GOLD).setWeight(25)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12))))

                                .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(30)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 8))))

                                .add(LootItem.lootTableItem(Items.COBBLESTONE).setWeight(40)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(12, 64))))

                                .add(SequentialEntry.sequential(
                                        LootItem.lootTableItem(Items.TUFF).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 64))),
                                        LootItem.lootTableItem(Items.CALCITE).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 64))),
                                        LootItem.lootTableItem(Items.ANDESITE).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 64))),
                                        LootItem.lootTableItem(Items.DIORITE).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 64))),
                                        LootItem.lootTableItem(Items.GRANITE).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 64)))
                                        )
                                )
                        ));

        biConsumer.accept(TCOTS_Entities.ICE_TROLL_BARTERING,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(10)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.FROST_WALKER))))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(10)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.UNBREAKING))))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(10)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.EFFICIENCY))))

                                .add(LootItem.lootTableItem(Items.COAL).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 16))))

                                .add(LootItem.lootTableItem(Items.SNOWBALL).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6))))

                                .add(LootItem.lootTableItem(Items.RAW_COPPER).setWeight(25)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 16))))

                                .add(LootItem.lootTableItem(Items.RAW_IRON).setWeight(25)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 16))))

                                .add(LootItem.lootTableItem(Items.SNOW_BLOCK).setWeight(35)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 8))))

                                .add(LootItem.lootTableItem(Items.ICE).setWeight(35)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12))))

                                .add(LootItem.lootTableItem(Items.PACKED_ICE).setWeight(30)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4))))

                                .add(LootItem.lootTableItem(Items.BLUE_ICE).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))

                                .add(LootItem.lootTableItem(Items.COBBLESTONE).setWeight(40)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 32))))
                        )
        );


        biConsumer.accept(TCOTS_Entities.FOREST_TROLL_BARTERING,
                LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.KNOCKBACK))))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.UNBREAKING))))

                                .add(LootItem.lootTableItem(Items.BOOK).setWeight(5)
                                        .apply((new EnchantRandomlyFunction.Builder())
                                                .withEnchantment(enchantmentRegistryLookup.getOrThrow(Enchantments.EFFICIENCY))))

                                .add(LootItem.lootTableItem(Items.CHARCOAL).setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 8))))

                                .add(LootItem.lootTableItem(Items.BONE).setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 12))))

                                .add(LootItem.lootTableItem(Items.LEATHER).setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 8))))

                                .add(LootItem.lootTableItem(Items.APPLE).setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6))))

                                .add(LootItem.lootTableItem(Items.STICK).setWeight(20)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 12))))

                                .add(SequentialEntry.sequential(
                                        LootItem.lootTableItem(Items.OAK_LEAVES).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16))),
                                        LootItem.lootTableItem(Items.BIRCH_LEAVES).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16))),
                                        LootItem.lootTableItem(Items.DARK_OAK_LEAVES).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16))),
                                        LootItem.lootTableItem(Items.SPRUCE_LEAVES).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 16)))
                                ))
                                
                                .add(SequentialEntry.sequential(
                                        LootItem.lootTableItem(Items.OAK_LOG).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 32))),
                                        LootItem.lootTableItem(Items.BIRCH_LOG).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 32))),
                                        LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 32))),
                                        LootItem.lootTableItem(Items.SPRUCE_LOG).setWeight(40)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 32)))
                                ))
                        )
        );

    }
}

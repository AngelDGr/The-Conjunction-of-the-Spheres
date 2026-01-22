package mors.neoforge.tcots.datagen.providers;

import mors.tcots.TCOTS_Main;
import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.registry.TCOTS_Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

public class TCOTS_LootTablesEntitiesGenerator extends EntityLootSubProvider {

    public TCOTS_LootTablesEntitiesGenerator(final HolderLookup.Provider lookupProvider) {
        super(FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID)).map(Map.Entry::getValue);
    }

    @Override
    public void generate() {
        //Necrophages
        {
            //Drowner
            {
                this.add(TCOTS_Entities.Drowner(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.DROWNER_TONGUE.get())
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.75f, 0.1f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.DROWNER_BRAIN.get())
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.3f, 0.2f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.WATER_ESSENCE.get())
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.1f, 0.05f))
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 4, 0.3f, 0.1f))
                                .setRandomSequence(getRandomSequence("drowner"))
                );
            }

            //Ghoul
            {
                this.add(TCOTS_Entities.Ghoul(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.GHOUL_BLOOD.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 1.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.3f, 0.2f))
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 3, 0.6f, 0.1f))
                                .setRandomSequence(getRandomSequence("ghoul"))
                );
            }

            //Alghoul
            {
                this.add(TCOTS_Entities.Alghoul(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.ALGHOUL_BONE_MARROW.get())
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.1f, 0.1f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.GHOUL_BLOOD.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.4f, 0.2f))
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 2, 0.4f, 0.1f))
                                .setRandomSequence(getRandomSequence("alghoul"))
                );
            }

            //Rotfiend
            {
                this.add(TCOTS_Entities.Rotfiend(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.ROTFIEND_BLOOD.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 2.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.4f, 0.2f))
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 4, 0.5f, 0.1f))
                                .setRandomSequence(getRandomSequence("rotfiend"))
                );
            }

            //Foglet
            {
                this.add(TCOTS_Entities.Foglet(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.FOGLET_TEETH.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 2.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.7f, 0.1f))
                                        )
                                )
                                .withPool(mutagenPool(this.registries, TCOTS_Items.FOGLET_MUTAGEN.get()))
                                .withPool(cadaverinePool(this.registries, 1, 2, 0.4f, 0.1f))
                                .setRandomSequence(getRandomSequence("foglet"))
                );

                this.add(TCOTS_Entities.Fogling(), LootTable.lootTable());
            }

            //Water Hag
            {
                this.add(TCOTS_Entities.WaterHag(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.WATER_HAG_MUD_BALL.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 2.0F)))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.WATER_ESSENCE.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 1.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.4f, 0.1f))
                                        )
                                )
                                .withPool(mutagenPool(this.registries, TCOTS_Items.WATER_HAG_MUTAGEN.get()))
                                .setRandomSequence(getRandomSequence("water_hag"))
                );
            }

            //Grave Hag
            {
                this.add(TCOTS_Entities.GraveHag(),
                        LootTable.lootTable()
                                .withPool(mutagenPool(this.registries, TCOTS_Items.GRAVE_HAG_MUTAGEN.get()))
                                .setRandomSequence(getRandomSequence("grave_hag"))
                );
            }

            //Scurver
            {
                this.add(TCOTS_Entities.Scurver(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.SCURVER_SPINE.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 4.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.6f, 0.1f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.ROTFIEND_BLOOD.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 2.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.4f, 0.2f))
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 4, 0.5f, 0.1f))
                                .setRandomSequence(getRandomSequence("scurver"))
                );
            }

            //Devourer
            {
                this.add(TCOTS_Entities.Devourer(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.DEVOURER_TEETH.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.3f, 0.1f))
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 4, 0.6f, 0.1f))
                                .setRandomSequence(getRandomSequence("devourer"))
                );
            }

            //Graveir
            {
                this.add(TCOTS_Entities.Graveir(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.GRAVEIR_BONE.get())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 1.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.6f, 0.15f))
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 6, 0.8f, 0.1f))
                                .setRandomSequence(getRandomSequence("graveir"))
                );
            }

            //Bullvore
            {
                this.add(TCOTS_Entities.Bullvore(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get())
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        )
                                )
                                .withPool(cadaverinePool(this.registries, 1, 5, 0.4f, 0.1f))
                                .setRandomSequence(getRandomSequence("bullvore"))
                );
            }

            //Bloedzuiger
            {
                this.add(TCOTS_Entities.Bloedzuiger(), LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(TCOTS_Items.BLOEDZUIGER_BLOOD.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 2.0F)))
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                0.5f, 0.1f))
                                )
                        )
                        .withPool(cadaverinePool(this.registries, 1, 2, 0.7f, 0.1f))
                        .setRandomSequence(getRandomSequence("bloedzuiger")));
            }

        }

        //Ogroids
        {
            //Nekker
            {
                this.add(TCOTS_Entities.Nekker(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(
                                                LootItem.lootTableItem(TCOTS_Items.NEKKER_EYE.get())
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                                0.7f, 0.2f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(
                                                LootItem.lootTableItem(TCOTS_Items.NEKKER_HEART.get())
                                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                                0.3f, 0.1f))
                                        )
                                )
                                .setRandomSequence(getRandomSequence("nekker"))
                );
            }

            //Nekker Warrior
            {
                this.add(TCOTS_Entities.NekkerWarrior(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(
                                                LootItem.lootTableItem(TCOTS_Items.NEKKER_EYE.get())
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                                0.7f, 0.2f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(
                                                LootItem.lootTableItem(TCOTS_Items.NEKKER_HEART.get())
                                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                                0.4f, 0.1f))
                                        )
                                )
                                .withPool(mutagenPool(this.registries, TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get()))
                                .setRandomSequence(getRandomSequence("nekker_warrior"))
                );
            }

            //Cyclops
            {
                this.add(TCOTS_Entities.Cyclops(),
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
                                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                                0.3f, 0.15f))
                                        )
                                )
                                .setRandomSequence(getRandomSequence("cyclops"))
                );
            }

            //Rock Troll
            {
                this.add(TCOTS_Entities.RockTroll(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.COBBLESTONE)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0,5)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 4.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.7f, 0.1f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(TCOTS_Items.CAVE_TROLL_LIVER.get())
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.7f, 0.1f))
                                        )
                                )
                                .withPool(mutagenPool(this.registries, TCOTS_Items.TROLL_MUTAGEN.get(), 0.05f))
                                .setRandomSequence(getRandomSequence("rock_troll"))
                );
            }

            //Ice Troll
            {
                this.add(TCOTS_Entities.IceTroll(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.BLUE_ICE)
                                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.2f, 0.1f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.PACKED_ICE)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 2.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.4f, 0.1f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.ICE)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 3.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.7f, 0.1f))
                                        )
                                )
                                .withPool(mutagenPool(this.registries, TCOTS_Items.TROLL_MUTAGEN.get(), 0.05f))
                                .setRandomSequence(getRandomSequence("ice_troll"))
                );
            }

            //Forest Troll
            {
                this.add(TCOTS_Entities.ForestTroll(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.LEATHER)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 6)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 3.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.6f, 0.15f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.BONE)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 2.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.4f, 0.15f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.STRING)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                        UniformGenerator.between(0.0F, 3.0F)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.3f, 0.2f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(LootItem.lootTableItem(Items.WHITE_WOOL)
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                        0.2f, 0.1f))
                                        )
                                )
                                .withPool(mutagenPool(this.registries, TCOTS_Items.TROLL_MUTAGEN.get(), 0.05f))
                                .setRandomSequence(getRandomSequence("forest_troll"))
                );
            }

            //Ice Giant
            {
                this.add(TCOTS_Entities.IceGiant(),
                        LootTable.lootTable()
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(
                                                LootItem.lootTableItem(Items.LEATHER)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 8)))
                                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries,
                                                                UniformGenerator.between(0.0F, 1.0F)))
                                                        .when(LootItemRandomChanceCondition.randomChance(0.6f))
                                        )
                                )
                                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                        .add(
                                                LootItem.lootTableItem(Items.BONE)
                                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 12)))
                                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries,
                                                                0.5f, 0.15f))
                                        )
                                )
                                .setRandomSequence(getRandomSequence("ice_giant"))
                );
            }

        }
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

    protected LootPool.Builder mutagenPool(final HolderLookup.Provider registryLookup, final Item mutagen, final float lootingProbability){
        return LootPool.lootPool().setRolls(UniformGenerator.between(1, 0))
                .add(LootItem.lootTableItem(mutagen)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registryLookup,
                                0.0f, lootingProbability))
                );
    }

    protected LootPool.Builder mutagenPool(final HolderLookup.Provider registryLookup, final Item mutagen){
        return mutagenPool(registryLookup, mutagen, 0.1f);
    }

    protected ResourceLocation getRandomSequence(final String id){
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "entities/"+id);
    }
}
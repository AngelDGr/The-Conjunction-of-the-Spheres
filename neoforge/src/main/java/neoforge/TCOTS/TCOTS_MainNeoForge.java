package neoforge.TCOTS;

import TCOTS.TCOTS_Main;

import TCOTS.entity.misc.FoglingEntity;
import TCOTS.entity.necrophages.*;
import TCOTS.entity.ogroids.*;
import TCOTS.items.AlchemyRecipeRandomlyLootFunction;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import TCOTS.registry.TCOTS_MapIcons;
import TCOTS.registry.TCOTS_Villagers;
import TCOTS.utils.AlchemyFormulaUtil;
import TCOTS.world.spawn.BullvoreSpawner;

import dev.architectury.event.events.common.LootEvent;
import neoforge.TCOTS.world.village.TCOTS_VillageAdditions;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.level.ModifyCustomSpawnersEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.Optional;
import java.util.function.Supplier;

@EventBusSubscriber(modid = TCOTS_Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
@Mod(value = TCOTS_Main.MOD_ID)
public class TCOTS_MainNeoForge {
    /**
     * registerCompostableItems() done with Data_Maps -> {@link neoforge.TCOTS.TCOTS_DataGenerator.DataMapGenerator}
     * registerBiomeModificationSpawn() done with Biome_Modifiers -> {@link neoforge.TCOTS.TCOTS_DataGenerator}
     *  */
    public TCOTS_MainNeoForge(IEventBus eventBus) {
        //Init common package
        TCOTS_Main.initCommon();

////	registerCommonEvent(); //Subscribed Event

///     TCOTS_MainNeoForge.registerCompostableItems(); //DataMaps

////    TCOTS_MainNeoForge.setEntitiesAttributes(); //Subscribed Event

////    TCOTS_MainNeoForge.registerSpawnPlacements(); //Subscribed Event
///     TCOTS_MainNeoForge.registerBiomeModificationSpawn(); //DataMaps

        TCOTS_MainNeoForge.modifyLootTables();

        TCOTS_VillageAdditions.registerNewVillageStructures();
        VillagerCustomTrades.registerTrades();

        registerCustomSpawners();

        specificLoaderStuff(eventBus);
    }

    @SubscribeEvent
    public static void registerCommonEvent(FMLCommonSetupEvent event){

        //Dispense Behaviors
        {
            //Dispenser with splash potions
            DispenserBlock.registerProjectileBehavior(TCOTS_Items.KILLER_WHALE_SPLASH.get());
            DispenserBlock.registerProjectileBehavior(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.get());
            DispenserBlock.registerProjectileBehavior(TCOTS_Items.SWALLOW_SPLASH.get());
            DispenserBlock.registerProjectileBehavior(TCOTS_Items.WATER_HAG_MUD_BALL.get());

            DispenserBlock.registerProjectileBehavior(TCOTS_Items.SCURVER_SPINE.get());
        }

        TCOTS_Items.owoItemGroup.get().initialize();
    }

    @SubscribeEvent
    public static void setEntitiesAttributes(EntityAttributeCreationEvent event) {
        //Necrophages
        {
            //Drowner
            event.put(TCOTS_Entities.DROWNER.get(), DrownerEntity.setAttributes().build());

            //Rotfiend
            event.put(TCOTS_Entities.ROTFIEND.get(), RotfiendEntity.setAttributes().build());

            //Grave Hag
            event.put(TCOTS_Entities.GRAVE_HAG.get(), GraveHagEntity.setAttributes().build());

            //Water Hag
            event.put(TCOTS_Entities.WATER_HAG.get(), WaterHagEntity.setAttributes().build());

            //Foglet
            event.put(TCOTS_Entities.FOGLET.get(), FogletEntity.setAttributes().build());
            event.put(TCOTS_Entities.FOGLING.get(), FoglingEntity.setAttributes().build());

            //Ghoul
            event.put(TCOTS_Entities.GHOUL.get(), GhoulEntity.setAttributes().build());

            //Alghoul
            event.put(TCOTS_Entities.ALGHOUL.get(), AlghoulEntity.setAttributes().build());

            //Scurver
            event.put(TCOTS_Entities.SCURVER.get(), ScurverEntity.setAttributes().build());

            //Devourer
            event.put(TCOTS_Entities.DEVOURER.get(), DevourerEntity.setAttributes().build());

            //Graveir
            event.put(TCOTS_Entities.GRAVEIR.get(), GraveirEntity.setAttributes().build());

            //Bullvore
            event.put(TCOTS_Entities.BULLVORE.get(), BullvoreEntity.setAttributes().build());
        }

        //Ogroids
        {
            //Nekker
            event.put(TCOTS_Entities.NEKKER.get(), NekkerEntity.setAttributes().build());

            //Nekker Warrior
            event.put(TCOTS_Entities.NEKKER_WARRIOR.get(), NekkerWarriorEntity.setAttributes().build());

            //Cyclops
            event.put(TCOTS_Entities.CYCLOPS.get(), CyclopsEntity.setAttributes().build());

            //Rock Troll
            event.put(TCOTS_Entities.ROCK_TROLL.get(), RockTrollEntity.setAttributes().build());

            //Ice Troll
            event.put(TCOTS_Entities.ICE_TROLL.get(), IceTrollEntity.setAttributes().build());

            //Forest Troll
            event.put(TCOTS_Entities.FOREST_TROLL.get(), ForestTrollEntity.setAttributes().build());

            //Ice Giant
            event.put(TCOTS_Entities.ICE_GIANT.get(), IceGiantEntity.setAttributes().build());

        }
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event){
        //Necrophages
        {
            //Drowners
            event.register(TCOTS_Entities.DROWNER.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    DrownerEntity::canSpawnDrowner, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Rotfiends
            event.register(TCOTS_Entities.ROTFIEND.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    RotfiendEntity::canSpawnInDarkW, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Foglets
            event.register(TCOTS_Entities.FOGLET.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    FogletEntity::canSpawnInDark_NotCaves, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Water Hags
            event.register(TCOTS_Entities.WATER_HAG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    DrownerEntity::canSpawnDrowner, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Grave Hags
            event.register(TCOTS_Entities.GRAVE_HAG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    GraveHagEntity::canSpawnInDarkNotBelowDeepslate, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Ghouls & Alghouls
            event.register(TCOTS_Entities.GHOUL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    GhoulEntity::canSpawnGhoul, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Scurvers
            event.register(TCOTS_Entities.SCURVER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    ScurverEntity::canSpawnInDarkW, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Devourer
            event.register(TCOTS_Entities.DEVOURER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    DevourerEntity::canSpawnInDarkW, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Graveir
            event.register(TCOTS_Entities.GRAVEIR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    GraveirEntity::canSpawnGraveir, RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Bullvore
            event.register(TCOTS_Entities.BULLVORE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    BullvoreEntity::canSpawnInDarkW, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        }

        //Ogroids
        {
            //Nekkers
            event.register(TCOTS_Entities.NEKKER.get(), SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NekkerEntity::canSpawnNekker,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Cyclops
            event.register(TCOTS_Entities.CYCLOPS.get(), SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CyclopsEntity::canCyclopsSpawn,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Rock Troll
            event.register(TCOTS_Entities.ROCK_TROLL.get(), SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RockTrollEntity::canSpawnInDarkNotBelowDeepslate,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Ice Troll
            event.register(TCOTS_Entities.ICE_TROLL.get(), SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IceTrollEntity::canSpawnInDarkNotBelowDeepslate,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);

            //Forest Troll
            event.register(TCOTS_Entities.FOREST_TROLL.get(), SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ForestTrollEntity::canSpawnInDarkNotBelowDeepslate,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE);
        }

    }

    public static void modifyLootTables(){

        LootEvent.MODIFY_LOOT_TABLE.register((id, tableBuilder, isBuiltin) -> {

            if(Blocks.WHEAT.getLootTable().equals(id) && isBuiltin){
                LootPool.Builder ergotSeeds = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items.ERGOT_SEEDS.get()))
                        .when(LootItemRandomChanceCondition.randomChance(0.05f))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(CropBlock.AGE,7)))
                        .apply(ApplyExplosionDecay.explosionDecay());

                tableBuilder.addPool(ergotSeeds);
            }

            if(EntityType.RAVAGER.getDefaultLootTable().equals(id) && isBuiltin){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3f,8f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
//                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup,0.8f, 0.1f))
                        .when(LootItemRandomChanceCondition.randomChance(0.8f))
                        ;

                tableBuilder.addPool(monsterFat);
            }

            if((EntityType.HOGLIN.getDefaultLootTable().equals(id) || EntityType.ZOGLIN.getDefaultLootTable().equals(id)) && isBuiltin){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2f,4f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
//                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup ,0.4f, 0.1f))
                        .when(LootItemRandomChanceCondition.randomChance(0.4f))
                        ;

                tableBuilder.addPool(monsterFat);
            }

            if(EntityType.POLAR_BEAR.getDefaultLootTable().equals(id) && isBuiltin){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2f,3f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
//                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup,0.3f, 0.1f))
                        .when(LootItemRandomChanceCondition.randomChance(0.3f))
                        ;

                tableBuilder.addPool(monsterFat);
            }

            if(EntityType.PIGLIN_BRUTE.getDefaultLootTable().equals(id) && isBuiltin){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1f,2f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
//                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup, 0.25f, 0.1f))
                        .when(LootItemRandomChanceCondition.randomChance(0.25f))
                        ;

                tableBuilder.addPool(monsterFat);
            }

            //Formulae/Alcohol
            {
                if (BuiltInLootTables.ABANDONED_MINESHAFT.equals(id) && isBuiltin) {

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .when(LootItemRandomChanceCondition.randomChance(0.15f));

                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                if (BuiltInLootTables.ANCIENT_CITY.equals(id) && isBuiltin) {

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(new AlchemyRecipeRandomlyLootFunction.Builder().add(1))
                            .when(LootItemRandomChanceCondition.randomChance(0.9f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                // Bastion
                {
                    if (BuiltInLootTables.BASTION_BRIDGE.equals(id) && isBuiltin) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()))
                                .apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.4f));

                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.05f));

                        tableBuilder.addPool(witcher_alcohol);
                        tableBuilder.addPool(alchemy_formulae);
                    }

                    if (BuiltInLootTables.BASTION_OTHER.equals(id) && isBuiltin) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()))
                                .apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.3f));

                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.05f));

                        tableBuilder.addPool(witcher_alcohol);
                        tableBuilder.addPool(alchemy_formulae);
                    }
                }


                if(BuiltInLootTables.DESERT_PYRAMID.equals(id) && isBuiltin){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.1f));

                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                if(BuiltInLootTables.IGLOO_CHEST.equals(id) && isBuiltin){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(1f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,3))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,5))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3))))
                            .when(LootItemRandomChanceCondition.randomChance(0.8f));

                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                if(BuiltInLootTables.JUNGLE_TEMPLE.equals(id) && isBuiltin){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.8f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                if(BuiltInLootTables.NETHER_BRIDGE.equals(id) && isBuiltin){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,1))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.6f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                if(BuiltInLootTables.PILLAGER_OUTPOST.equals(id) && isBuiltin){

                    LootPool.Builder extra_loot_oils = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items.HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items.ENHANCED_HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.05f));

                    LootPool.Builder crossbow_bolts = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,4))
                            .add(LootItem.lootTableItem(TCOTS_Items.BASE_BOLT.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,12))))
                            .add(LootItem.lootTableItem(TCOTS_Items.BLUNT_BOLT.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6))))
                            .add(LootItem.lootTableItem(TCOTS_Items.PRECISION_BOLT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6))))
                            .add(LootItem.lootTableItem(TCOTS_Items.BROADHEAD_BOLT.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items.EXPLODING_BOLT.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.5f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1, 2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
                            .add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.25f));

                    tableBuilder.addPool(extra_loot_oils);
                    tableBuilder.addPool(crossbow_bolts);
                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                //Shipwreck
                {
                    if (BuiltInLootTables.SHIPWRECK_SUPPLY.equals(id) && isBuiltin) {
                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 4))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
                                .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
                                .add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 4))))
                                .add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 4))))
                                .add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.6f));

                        tableBuilder.addPool(witcher_alcohol);
                    }

                    if (BuiltInLootTables.SHIPWRECK_MAP.equals(id) && isBuiltin) {
                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.6f));

                        tableBuilder.addPool(alchemy_formulae);
                    }
                }

                if(BuiltInLootTables.SIMPLE_DUNGEON.equals(id) && isBuiltin){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,3))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.7f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }


                //Stronghold
                {
                    if (BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(id) && isBuiltin) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 3))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.6f));

                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1,2))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                                .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.3f));

                        tableBuilder.addPool(witcher_alcohol);
                        tableBuilder.addPool(alchemy_formulae);
                    }

                    if (BuiltInLootTables.STRONGHOLD_CROSSING.equals(id) && isBuiltin) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.4f));

                        tableBuilder.addPool(alchemy_formulae);
                    }

                    if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(id) && isBuiltin) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 4))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(1f));

                        tableBuilder.addPool(alchemy_formulae);
                    }
                }

                //Underwater
                {
                    if (BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(id) && isBuiltin) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.3f));

                        tableBuilder.addPool(alchemy_formulae);
                    }

                    if (BuiltInLootTables.UNDERWATER_RUIN_SMALL.equals(id) && isBuiltin) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.2f));

                        tableBuilder.addPool(alchemy_formulae);
                    }
                }

                if(BuiltInLootTables.WOODLAND_MANSION.equals(id) && isBuiltin){
                    LootPool.Builder extra_loot_oils = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items.HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items.ENHANCED_HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items.SUPERIOR_HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.05f));

                    LootPool.Builder witcher_books = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,3))
                            .add(LootItem.lootTableItem(TCOTS_Items.WITCHER_BESTIARY.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_BOOK.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.05f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                            .add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(18).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.1f));

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 2))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.6f));

                    tableBuilder.addPool(extra_loot_oils);
                    tableBuilder.addPool(witcher_books);
                    tableBuilder.addPool(witcher_alcohol);
                    tableBuilder.addPool(alchemy_formulae);
                }

                //Village
                {
                    if(BuiltInLootTables.FARMER_GIFT.equals(id) && isBuiltin){
                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(15).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                                .add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.5f));

                        tableBuilder.addPool(witcher_alcohol);
                    }

                    if((BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_DESERT_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_SAVANNA_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_TAIGA_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_SNOWY_HOUSE.equals(id))
                            && isBuiltin){
                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                                .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                .add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                                .when(LootItemRandomChanceCondition.randomChance(0.15f));

                        LootPool.Builder witcher_bestiary = LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items.WITCHER_BESTIARY.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                                .when(LootItemRandomChanceCondition.randomChance(0.05f));

                        tableBuilder.addPool(witcher_bestiary);
                        tableBuilder.addPool(witcher_alcohol);
                    }
                }

//                if(BuiltInLootTables.SPAWN_BONUS_CHEST.equals(id) && isBuiltin){
//
//                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
//                            .setRolls(UniformGenerator.between(0,2))
//                            .add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
//                            .when(LootItemRandomChanceCondition.randomChance(0.5f));
//
//                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
//                            .setRolls(UniformGenerator.between(1,3))
//                            .add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
//                            .add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
//                            .add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(18).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,5))))
//                            .when(LootItemRandomChanceCondition.randomChance(0.3f));
//                }

            }

            //Sniffer Digging
            {
                if (BuiltInLootTables.SNIFFER_DIGGING.equals(id) && isBuiltin) {

                    LootPool.Builder allspice = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items.ALLSPICE.get()))
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    tableBuilder.addPool(allspice);
                }
            }
        });


    }

    public static class VillagerCustomTrades {
        public static void registerTrades(){
            //Uses            --> 16/12/3
            //Experience      --> 1/2/5/10/15/20/30
            //PriceMultiplier --> 0.05/0.2

            NeoForge.EVENT_BUS.addListener((VillagerTradesEvent event) -> {

                //Herbalist
                if(event.getType().equals(TCOTS_Villagers.HERBALIST.get())){
                    //Level 1
                    {
                        int level = 1;
                        //Sell
                        {
                            event.getTrades().get(level)
                                    .add(new VillagerTrades.ItemsForEmeralds(
                                            //Gives
                                            TCOTS_Items.ALCHEMY_BOOK.get(),
                                            6,
                                            1,
                                            3,
                                            10
                                    ));

                            event.getTrades().get(level)
                                    .add(new VillagerTrades.ItemsForEmeralds(
                                            //Gives
                                            TCOTS_Items.ICY_SPIRIT.get(),
                                            4,
                                            1,
                                            16,
                                            1
                                    ));

                        }

                        //Buys
                        {
                            event.getTrades().get(level)
                                    .add(new VillagerTrades.EmeraldForItems(
                                            Items.DANDELION,
                                            12,
                                            16,
                                            2

                                    ));
                        }
                    }

                    //Level 2
                    {
                        int level = 2;
                        //Sell
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 16+ random.nextIntBetweenInclusive(0,32)),
                                            //Gives
                                            AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items.ALCHEMY_FORMULA.get().getDefaultInstance(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
                                            3,
                                            60,
                                            0.2f));

                            event.getTrades().get(level)
                                            .add(new VillagerTrades.ItemsForEmeralds(
                                                    TCOTS_Items.ALLSPICE.get(),
                                                    6,
                                                    1,
                                                    5));

                            event.getTrades().get(level)
                                            .add(new VillagerTrades.ItemsForEmeralds(
                                                    Items.HONEYCOMB,
                                                    8,
                                                    1,
                                                    5));

                        }

                        //Buys
                        {
                            event.getTrades().get(level)
                                            .add(new BasicItemListing(
                                                    //Wants
                                                    new ItemStack(Items.ALLIUM, 4),
                                                    //Gives
                                                    new ItemStack(Items.EMERALD, 6),
                                                    12,
                                                    10,
                                                    0.05f));
                        }
                    }

                    //Level 3
                    {
                        int level = 3;
                        //Sell
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 8),
                                            //Gives
                                            new ItemStack(TCOTS_Items.CHERRY_CORDIAL.get(), 1),
                                            12,
                                            10,
                                            0.05f));

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 12),
                                            //Gives
                                            new ItemStack(TCOTS_Items.MANDRAKE_CORDIAL.get(), 1),
                                            12,
                                            10,
                                            0.05f));


                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 8),
                                            //Gives
                                            new ItemStack(TCOTS_Items.PUFFBALL, 2),
                                            12,
                                            10,
                                            0.05f));

                        }

                        //Buys
                        {

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(TCOTS_Items.MONSTER_FAT.get(), 4),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 8),
                                            12,
                                            20,
                                            0.05f));

                        }
                    }


                    //Level 4
                    {
                        int level = 4;
                        //Sells
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 16 + random.nextIntBetweenInclusive(0, 32)),
                                            //Gives
                                            AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items.ALCHEMY_FORMULA.get().getDefaultInstance(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
                                            3,
                                            100,
                                            0.2f));

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 6),
                                            //Gives
                                            new ItemStack(TCOTS_Items.ALCHEMY_PASTE, 1),
                                            16,
                                            15,
                                            0.05f));

                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(Items.EMERALD, 8),
                                            //Gives
                                            new ItemStack(TCOTS_Items.ALCHEMISTS_POWDER, 2),
                                            16,
                                            15,
                                            0.05f));

                        }

                        //Buys
                        {
                            event.getTrades().get(level)
                                    .add((entity, random) -> new MerchantOffer(
                                            //Wants
                                            new ItemCost(TCOTS_Items.WATER_ESSENCE.get(), 2),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 8),
                                            12,
                                            15,
                                            0.05f));

                        }

                    }

                    //Level 5
                    {
                        int level = 5;
                        //Sell
                        {
                            {
                                //Potions
                                {
                                    //Enhanced
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SWALLOW_POTION, 32, TCOTS_Items.SWALLOW_POTION_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.CAT_POTION, 32, TCOTS_Items.CAT_POTION_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_RAFFARDS_DECOCTION, 32, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BLACK_BLOOD_POTION, 32, TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MARIBOR_FOREST_POTION, 32, TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WOLF_POTION, 32, TCOTS_Items.WOLF_POTION_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ROOK_POTION, 32, TCOTS_Items.ROOK_POTION_ENHANCED));

                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_HONEY_POTION, 16, TCOTS_Items.WHITE_HONEY_POTION_ENHANCED));


                                    //Superior
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SWALLOW_POTION_ENHANCED, 48, TCOTS_Items.SWALLOW_POTION_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.CAT_POTION_ENHANCED, 48, TCOTS_Items.CAT_POTION_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED, 48, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED, 48, TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED, 48, TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WOLF_POTION_ENHANCED, 48, TCOTS_Items.WOLF_POTION_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ROOK_POTION_ENHANCED, 48, TCOTS_Items.ROOK_POTION_SUPERIOR));

                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED, 32, TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR));
                                }

                                //Bombs
                                {
                                    //Enhanced
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.GRAPESHOT, 32, TCOTS_Items.GRAPESHOT_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SAMUM, 32, TCOTS_Items.SAMUM_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DANCING_STAR, 32, TCOTS_Items.DANCING_STAR_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DEVILS_PUFFBALL, 32, TCOTS_Items.DEVILS_PUFFBALL_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DRAGONS_DREAM, 32, TCOTS_Items.DRAGONS_DREAM_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NORTHERN_WIND, 32, TCOTS_Items.NORTHERN_WIND_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DIMERITIUM_BOMB, 32, TCOTS_Items.DIMERITIUM_BOMB_ENHANCED));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MOON_DUST, 32, TCOTS_Items.MOON_DUST_ENHANCED));

                                    //Superior
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.GRAPESHOT_ENHANCED, 56, TCOTS_Items.GRAPESHOT_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SAMUM_ENHANCED, 56, TCOTS_Items.SAMUM_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DANCING_STAR_ENHANCED, 56, TCOTS_Items.DANCING_STAR_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED, 56, TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DRAGONS_DREAM_ENHANCED, 56, TCOTS_Items.DRAGONS_DREAM_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NORTHERN_WIND_ENHANCED, 56, TCOTS_Items.NORTHERN_WIND_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED, 56, TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MOON_DUST_ENHANCED, 56, TCOTS_Items.MOON_DUST_SUPERIOR));
                                }

                                //Oils
                                {
                                    //Enhanced
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NECROPHAGE_OIL, 16, TCOTS_Items.ENHANCED_NECROPHAGE_OIL));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.OGROID_OIL, 16, TCOTS_Items.ENHANCED_OGROID_OIL));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BEAST_OIL, 16, TCOTS_Items.ENHANCED_BEAST_OIL));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.HANGED_OIL, 16, TCOTS_Items.ENHANCED_HANGED_OIL));

                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_NECROPHAGE_OIL, 36, TCOTS_Items.SUPERIOR_NECROPHAGE_OIL));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_OGROID_OIL, 36, TCOTS_Items.SUPERIOR_OGROID_OIL));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_BEAST_OIL, 36, TCOTS_Items.SUPERIOR_BEAST_OIL));
                                    event.getTrades().get(level).add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_HANGED_OIL, 36, TCOTS_Items.SUPERIOR_HANGED_OIL));

                                }

                                //Ingredients
                                {
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.WHITE_GULL));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.STAMMELFORDS_DUST));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.AETHER));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.HYDRAGENUM));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.NIGREDO));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.QUEBRITH));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.REBIS));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.RUBEDO));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.VERMILION));
                                    event.getTrades().get(level).add((entity, random) -> miscRecipeTrade(TCOTS_Items.VITRIOL));
                                }
                            }
                        }
                    }
                }

                //Farmer
                //Alcohol & Ergot Seeds
                if(event.getType().equals(VillagerProfession.FARMER)){
                    event.getTrades().get(2)
                            .add(new BasicItemListing(
                                    //Wants
                                    new ItemStack(Items.EMERALD, 1),
                                    //Gives
                                    new ItemStack(TCOTS_Items.ERGOT_SEEDS.get(), 1),
                                    16,
                                    5,
                                    0.05f)
                            );

                    event.getTrades().get(3)
                            .add(new BasicItemListing(
                                    //Wants
                                    new ItemStack(Items.EMERALD, 6),
                                    //Gives
                                    new ItemStack(TCOTS_Items.VILLAGE_HERBAL.get(), 1),
                                    12,
                                    5,
                                    0.05f)
                            );
                }

                //Butcher
                //Monster Fat
                if(event.getType().equals(VillagerProfession.BUTCHER)){
                    event.getTrades().get(3)
                            .add(new BasicItemListing(
                                            //Wants
                                            new ItemStack(Items.EMERALD, 12),
                                            //Gives
                                            new ItemStack(TCOTS_Items.MONSTER_FAT.get(), 1),
                                            12,
                                            15,
                                            0.2f)
                            );
                }

                //Librarian
                //Bestiary
                if(event.getType().equals(VillagerProfession.LIBRARIAN)){

                    event.getTrades().get(1)
                            .add(new BasicItemListing(
                                    //Wants
                                    new ItemStack(Items.EMERALD, 12),
                                    //Gives
                                    new ItemStack(TCOTS_Items.WITCHER_BESTIARY.get(), 1),
                                    3,
                                    10,
                                    0.05f)
                            );
                }

                //Cleric
                //Monster Parts
                if(event.getType().equals(VillagerProfession.CLERIC)){
                    event.getTrades().get(1)
                            .add(new BasicItemListing(
                                            //Wants
                                            new ItemStack(TCOTS_Items.DEVOURER_TEETH.get(), 8),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 16),
                                            3,
                                            15,
                                            0.2f)
                            );

                    event.getTrades().get(1)
                            .add(new BasicItemListing(
                                            //Wants
                                            new ItemStack(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get(), 1),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 16),
                                            3,
                                            20,
                                            0.2f)
                            );

                    event.getTrades().get(1)
                            .add(new BasicItemListing(
                                            //Wants
                                            new ItemStack(TCOTS_Items.GRAVEIR_BONE.get(), 2),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 16),
                                            3,
                                            15,
                                            0.2f)
                            );

                    event.getTrades().get(1)
                            .add(new BasicItemListing(
                                            //Wants
                                            new ItemStack(TCOTS_Items.CADAVERINE.get(), 16),
                                            //Gives
                                            new ItemStack(Items.EMERALD, 2),
                                            12,
                                            5,
                                            0.05f)
                            );
                }

                //Cartographer
                if(event.getType().equals(VillagerProfession.CARTOGRAPHER)){
                    event.getTrades().get(3)
                                    .add(new VillagerTrades.TreasureMapForEmeralds(
                                            16,
                                            TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "on_ice_giant_maps")),
                                            "filled_map.giant_cave",
                                            TCOTS_MapIcons.GiantCave(),
                                            12,
                                            10));
                }

            });
        }

        private static MerchantOffer upgradeRecipeTrade(Supplier<Item> recipeToUpgrade, int Cost, Supplier<Item> upgradedRecipe){
            return new MerchantOffer(
                    //Wants
                    new ItemCost(TCOTS_Items.ALCHEMY_FORMULA.get())
                            .withComponents(builder -> builder.expect(TCOTS_Items.RecipeTeacher(),
                                    new RecipeTeacherComponent(BuiltInRegistries.ITEM.getKey(recipeToUpgrade.get()).toString(), false))),
                    Optional.of(new ItemCost(Items.EMERALD, Cost)),
                    //Gives
                    AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(upgradedRecipe.get())),
                    3,
                    30,
                    0.2f);
        }

        private static MerchantOffer miscRecipeTrade(Supplier<? extends Item> item){
            return new MerchantOffer(
                    //Wants
                    new ItemCost(Items.EMERALD, 16),
                    //Gives
                    AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(item.get())),
                    3,
                    30,
                    0.2f);
        }
    }

    @SuppressWarnings("all")
    public static void registerCustomSpawners(){
        NeoForge.EVENT_BUS.addListener((ModifyCustomSpawnersEvent event)->{
            event.addCustomSpawner(new BullvoreSpawner());
        });
    }

    public static void specificLoaderStuff(IEventBus eventBus){
        TCOTS_Registries.SOUND_EVENTS.register(eventBus);

        TCOTS_Items.initDataComponents(); TCOTS_Registries.DATA_COMPONENTS.register(eventBus);
    }

}

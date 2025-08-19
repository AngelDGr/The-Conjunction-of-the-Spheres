package fabric.TCOTS;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Tags;
import TCOTS.entity.misc.FoglingEntity;
import TCOTS.entity.monsters.necrophages.*;
import TCOTS.entity.monsters.ogroids.*;
import TCOTS.items.AlchemyRecipeRandomlyLootFunction;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.registry.*;
import TCOTS.mixin.ServerWorldAccessor;
import TCOTS.utils.AlchemyFormulaUtil;
import TCOTS.world.spawn.BullvoreSpawner;
import fabric.TCOTS.world.village.TCOTS_VillageAdditions;
import io.wispforest.lavender.book.LavenderBookItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TCOTS_MainFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		//Init common package
		TCOTS_Main.initCommon();

		registerCommonEvent();

		TCOTS_MainFabric.registerCompostableItems();

		TCOTS_MainFabric.setEntitiesAttributes();

		TCOTS_MainFabric.registerSpawnPlacements();
		TCOTS_MainFabric.registerBiomeModificationSpawn();

		TCOTS_MainFabric.modifyLootTables();

		TCOTS_VillageAdditions.registerNewVillageStructures();
		VillagerCustomTrades.registerTrades();

		registerCustomSpawners();

		specificLoaderStuff();
	}

	public static class ServerWorldSpawnersUtil {
		public static void register(final ServerLevel world, final CustomSpawner spawner) {
			final List<CustomSpawner> spawnerList = new ArrayList<>(((ServerWorldAccessor) world).getCustomSpawners());
			spawnerList.add(spawner);
			((ServerWorldAccessor) world).setCustomSpawners(spawnerList);
		}
	}

	public static void registerCommonEvent(){
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

	public static void setEntitiesAttributes(){
		//Necrophages
		{
			//Drowner
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.DROWNER.get(), DrownerEntity.setAttributes());

			//Rotfiend
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.ROTFIEND.get(), RotfiendEntity.setAttributes());

			//Grave Hag
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.GRAVE_HAG.get(), GraveHagEntity.setAttributes());

			//Water Hag
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.WATER_HAG.get(), WaterHagEntity.setAttributes());

			//Foglet
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.FOGLET.get(), FogletEntity.setAttributes());
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.FOGLING.get(), FoglingEntity.setAttributes());

			//Ghoul
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.GHOUL.get(), GhoulEntity.setAttributes());

			//Alghoul
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.ALGHOUL.get(), AlghoulEntity.setAttributes());

			//Scurver
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.SCURVER.get(), ScurverEntity.setAttributes());

			//Devourer
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.DEVOURER.get(), DevourerEntity.setAttributes());

			//Bloedzuiger
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.BLOEDZUIGER.get(), BloedzuigerEntity.setAttributes());

			//Graveir
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.GRAVEIR.get(), GraveirEntity.setAttributes());

			//Bullvore
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.BULLVORE.get(), BullvoreEntity.setAttributes());
		}

		//Ogroids
		{
			//Nekker
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.NEKKER.get(), NekkerEntity.setAttributes());

			//Nekker Warrior
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.NEKKER_WARRIOR.get(), NekkerWarriorEntity.setAttributes());

			//Cyclops
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.CYCLOPS.get(), CyclopsEntity.setAttributes());

			//Rock Troll
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.ROCK_TROLL.get(), RockTrollEntity.setAttributes());

			//Ice Troll
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.ICE_TROLL.get(), IceTrollEntity.setAttributes());

			//Forest Troll
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.FOREST_TROLL.get(), ForestTrollEntity.setAttributes());

			//Ice Giant
			FabricDefaultAttributeRegistry.register(TCOTS_Entities.ICE_GIANT.get(), IceGiantEntity.setAttributes());

		}
	}

	public static void registerSpawnPlacements() {
		//Necrophages
		{
			//Drowners
			SpawnPlacements.register(TCOTS_Entities.DROWNER.get(), SpawnPlacementTypes.NO_RESTRICTIONS,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);


			//Rotfiends
			SpawnPlacements.register(TCOTS_Entities.ROTFIEND.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RotfiendEntity::canSpawnInDarkW);


			//Foglets
			SpawnPlacements.register(TCOTS_Entities.FOGLET.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FogletEntity::canSpawnInDark_NotCaves);


			//Water Hags
			SpawnPlacements.register(TCOTS_Entities.WATER_HAG.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);


			//Grave Hags
			SpawnPlacements.register(TCOTS_Entities.GRAVE_HAG.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GraveHagEntity::canSpawnInDarkNotBelowDeepslate);


			//Ghouls & Alghouls
			SpawnPlacements.register(TCOTS_Entities.GHOUL.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhoulEntity::canSpawnGhoul);


			//Scurvers
			SpawnPlacements.register(TCOTS_Entities.SCURVER.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScurverEntity::canSpawnInDarkW);


			//Devourer
			SpawnPlacements.register(TCOTS_Entities.DEVOURER.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DevourerEntity::canSpawnInDarkW);

			//Bloedzuiger
			SpawnPlacements.register(TCOTS_Entities.BLOEDZUIGER.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BloedzuigerEntity::canSpawnBloedzuiger);

			//Graveir
			SpawnPlacements.register(TCOTS_Entities.GRAVEIR.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GraveirEntity::canSpawnGraveir);

			//Bullvore
			SpawnPlacements.register(TCOTS_Entities.BULLVORE.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BullvoreEntity::canSpawnInDarkW);

		}

		//Ogroids
		{
			//Nekkers
			SpawnPlacements.register(TCOTS_Entities.NEKKER.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NekkerEntity::canSpawnNekker);


			//Cyclops
			SpawnPlacements.register(TCOTS_Entities.CYCLOPS.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CyclopsEntity::canCyclopsSpawn);


			//Rock Troll
			SpawnPlacements.register(TCOTS_Entities.ROCK_TROLL.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RockTrollEntity::canSpawnInDarkNotBelowDeepslate);


			//Ice Troll
			SpawnPlacements.register(TCOTS_Entities.ICE_TROLL.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IceTrollEntity::canSpawnInDarkNotBelowDeepslate);



			//Forest Troll
			SpawnPlacements.register(TCOTS_Entities.FOREST_TROLL.get(), SpawnPlacementTypes.ON_GROUND,
					Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ForestTrollEntity::canSpawnInDarkNotBelowDeepslate);

		}
	}

	public static void modifyLootTables(){
		LootTableEvents.MODIFY.register( (id, tableBuilder, source, wrapperLookup) -> {

			if(Blocks.WHEAT.getLootTable().equals(id) && source.isBuiltin()){
				final LootPool.Builder ergotSeeds = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(TCOTS_Items.ERGOT_SEEDS.get()))
						.when(LootItemRandomChanceCondition.randomChance(0.05f))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT)
								.setProperties(StatePropertiesPredicate.Builder.properties()
										.hasProperty(CropBlock.AGE,7)))
						.apply(ApplyExplosionDecay.explosionDecay());

				tableBuilder.pool(ergotSeeds.build());
			}

			if(EntityType.RAVAGER.getDefaultLootTable().equals(id) && source.isBuiltin()){
				final LootPool.Builder monsterFat = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(3f,8f))))
						.when(LootItemKilledByPlayerCondition.killedByPlayer())
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup,0.8f, 0.1f));

				tableBuilder.pool(monsterFat.build());
			}

			if((EntityType.HOGLIN.getDefaultLootTable().equals(id) || EntityType.ZOGLIN.getDefaultLootTable().equals(id)) && source.isBuiltin()){
				final LootPool.Builder monsterFat = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2f,4f))))
						.when(LootItemKilledByPlayerCondition.killedByPlayer())
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup ,0.4f, 0.1f));

				tableBuilder.pool(monsterFat.build());
			}

			if(EntityType.POLAR_BEAR.getDefaultLootTable().equals(id) && source.isBuiltin()){
				final LootPool.Builder monsterFat = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2f,3f))))
						.when(LootItemKilledByPlayerCondition.killedByPlayer())
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup,0.3f, 0.1f));

				tableBuilder.pool(monsterFat.build());
			}

			if(EntityType.PIGLIN_BRUTE.getDefaultLootTable().equals(id) && source.isBuiltin()){
				final LootPool.Builder monsterFat = LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(TCOTS_Items.MONSTER_FAT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1f,2f))))
						.when(LootItemKilledByPlayerCondition.killedByPlayer())
						.when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup, 0.25f, 0.1f));

				tableBuilder.pool(monsterFat.build());
			}

			//Formulae/Alcohol
			{
				if (BuiltInLootTables.ABANDONED_MINESHAFT.equals(id) && source.isBuiltin()) {

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0, 2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.4f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
							.when(LootItemRandomChanceCondition.randomChance(0.15f));

					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				if (BuiltInLootTables.ANCIENT_CITY.equals(id) && source.isBuiltin()) {

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0, 1))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(new AlchemyRecipeRandomlyLootFunction.Builder().add(1))
							.when(LootItemRandomChanceCondition.randomChance(0.9f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
							.when(LootItemRandomChanceCondition.randomChance(0.3f));

					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				// Bastion
				{
					if (BuiltInLootTables.BASTION_BRIDGE.equals(id) && source.isBuiltin()) {

						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 1))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()))
								.apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(0.4f));

						final LootPool.Builder witcher_alcohol = LootPool.lootPool()
								.setRolls(UniformGenerator.between(1, 2))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
								.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
								.when(LootItemRandomChanceCondition.randomChance(0.05f));

						tableBuilder.pool(witcher_alcohol.build());
						tableBuilder.pool(alchemy_formulae.build());
					}

					if (BuiltInLootTables.BASTION_OTHER.equals(id) && source.isBuiltin()) {

						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 1))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()))
								.apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(0.3f));

						final LootPool.Builder witcher_alcohol = LootPool.lootPool()
								.setRolls(UniformGenerator.between(1, 2))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
								.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12)
										.apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
								.when(LootItemRandomChanceCondition.randomChance(0.05f));

						tableBuilder.pool(witcher_alcohol.build());
						tableBuilder.pool(alchemy_formulae.build());
					}
				}


				if(BuiltInLootTables.DESERT_PYRAMID.equals(id) && source.isBuiltin()){

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.4f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
							.when(LootItemRandomChanceCondition.randomChance(0.1f));

					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				if(BuiltInLootTables.IGLOO_CHEST.equals(id) && source.isBuiltin()){

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(1f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,3))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,5))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3))))
							.add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3))))
							.when(LootItemRandomChanceCondition.randomChance(0.8f));

					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				if(BuiltInLootTables.JUNGLE_TEMPLE.equals(id) && source.isBuiltin()){

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.8f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
							.when(LootItemRandomChanceCondition.randomChance(0.3f));

					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				if(BuiltInLootTables.NETHER_BRIDGE.equals(id) && source.isBuiltin()){

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0,1))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.6f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
							.when(LootItemRandomChanceCondition.randomChance(0.3f));

					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				if(BuiltInLootTables.PILLAGER_OUTPOST.equals(id) && source.isBuiltin()){

					final LootPool.Builder extra_loot_oils = LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(TCOTS_Items.HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
							.add(LootItem.lootTableItem(TCOTS_Items.ENHANCED_HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
							.when(LootItemRandomChanceCondition.randomChance(0.05f));

					final LootPool.Builder crossbow_bolts = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,4))
							.add(LootItem.lootTableItem(TCOTS_Items.BASE_BOLT.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,12))))
							.add(LootItem.lootTableItem(TCOTS_Items.BLUNT_BOLT.get()).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6))))
							.add(LootItem.lootTableItem(TCOTS_Items.PRECISION_BOLT.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6))))
							.add(LootItem.lootTableItem(TCOTS_Items.BROADHEAD_BOLT.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4))))
							.add(LootItem.lootTableItem(TCOTS_Items.EXPLODING_BOLT.get()).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2))))
							.when(LootItemRandomChanceCondition.randomChance(0.4f));

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.5f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1, 2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
							.add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
							.add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
							.add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
							.add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
							.when(LootItemRandomChanceCondition.randomChance(0.25f));

					tableBuilder.pool(extra_loot_oils.build());
					tableBuilder.pool(crossbow_bolts.build());
					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				//Shipwreck
				{
					if (BuiltInLootTables.SHIPWRECK_SUPPLY.equals(id) && source.isBuiltin()) {
						final LootPool.Builder witcher_alcohol = LootPool.lootPool()
								.setRolls(UniformGenerator.between(1, 4))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
								.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
								.add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
								.add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 4))))
								.add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 4))))
								.add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
								.when(LootItemRandomChanceCondition.randomChance(0.6f));

						tableBuilder.pool(witcher_alcohol.build());
					}

					if (BuiltInLootTables.SHIPWRECK_MAP.equals(id) && source.isBuiltin()) {
						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 2))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(0.6f));

						tableBuilder.pool(alchemy_formulae.build());
					}
				}

				if(BuiltInLootTables.SIMPLE_DUNGEON.equals(id) && source.isBuiltin()){

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,3))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.7f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
							.when(LootItemRandomChanceCondition.randomChance(0.3f));

					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}


				//Stronghold
				{
					if (BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(id) && source.isBuiltin()) {

						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 3))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(0.6f));

						final LootPool.Builder witcher_alcohol = LootPool.lootPool()
								.setRolls(UniformGenerator.between(1,2))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
								.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
								.when(LootItemRandomChanceCondition.randomChance(0.3f));

						tableBuilder.pool(witcher_alcohol.build());
						tableBuilder.pool(alchemy_formulae.build());
					}

					if (BuiltInLootTables.STRONGHOLD_CROSSING.equals(id) && source.isBuiltin()) {

						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 2))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(0.4f));

						tableBuilder.pool(alchemy_formulae.build());
					}

					if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(id) && source.isBuiltin()) {

						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 4))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(1f));

						tableBuilder.pool(alchemy_formulae.build());
					}
				}

				//Underwater
				{
					if (BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(id) && source.isBuiltin()) {

						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 2))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(0.3f));

						tableBuilder.pool(alchemy_formulae.build());
					}

					if (BuiltInLootTables.UNDERWATER_RUIN_SMALL.equals(id) && source.isBuiltin()) {

						final LootPool.Builder alchemy_formulae = LootPool.lootPool()
								.setRolls(UniformGenerator.between(0, 1))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
								.when(LootItemRandomChanceCondition.randomChance(0.2f));

						tableBuilder.pool(alchemy_formulae.build());
					}
				}

				if(BuiltInLootTables.WOODLAND_MANSION.equals(id) && source.isBuiltin()){
					final LootPool.Builder extra_loot_oils = LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(TCOTS_Items.HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
							.add(LootItem.lootTableItem(TCOTS_Items.ENHANCED_HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
							.add(LootItem.lootTableItem(TCOTS_Items.SUPERIOR_HANGED_OIL.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
							.when(LootItemRandomChanceCondition.randomChance(0.05f));

					final LootPool.Builder witcher_books = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,3))
							.add(LootItem.lootTableItem(TCOTS_Items.WITCHER_BESTIARY.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_BOOK.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
							.when(LootItemRandomChanceCondition.randomChance(0.05f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
							.add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
							.add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
							.add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
							.add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(18).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
							.when(LootItemRandomChanceCondition.randomChance(0.1f));

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0, 2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.6f));

					tableBuilder.pool(extra_loot_oils.build());
					tableBuilder.pool(witcher_books.build());
					tableBuilder.pool(witcher_alcohol.build());
					tableBuilder.pool(alchemy_formulae.build());
				}

				//Village
				{
					if(BuiltInLootTables.FARMER_GIFT.equals(id) && source.isBuiltin()){
						final LootPool.Builder witcher_alcohol = LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(15).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
								.add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
								.when(LootItemRandomChanceCondition.randomChance(0.5f));

						tableBuilder.pool(witcher_alcohol.build());
					}

					if((BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(id)
							|| BuiltInLootTables.VILLAGE_DESERT_HOUSE.equals(id)
							|| BuiltInLootTables.VILLAGE_SAVANNA_HOUSE.equals(id)
							|| BuiltInLootTables.VILLAGE_TAIGA_HOUSE.equals(id)
							|| BuiltInLootTables.VILLAGE_SNOWY_HOUSE.equals(id))
							&& source.isBuiltin()){
						final LootPool.Builder witcher_alcohol = LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
								.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
								.add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
								.add(LootItem.lootTableItem(TCOTS_Items.CHERRY_CORDIAL.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
								.add(LootItem.lootTableItem(TCOTS_Items.VILLAGE_HERBAL.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
								.add(LootItem.lootTableItem(TCOTS_Items.MANDRAKE_CORDIAL.get()).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
								.when(LootItemRandomChanceCondition.randomChance(0.15f));

						final LootPool.Builder witcher_bestiary = LootPool.lootPool()
								.setRolls(ConstantValue.exactly(1))
								.add(LootItem.lootTableItem(TCOTS_Items.WITCHER_BESTIARY.get()).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
								.when(LootItemRandomChanceCondition.randomChance(0.05f));

						tableBuilder.pool(witcher_bestiary.build());
						tableBuilder.pool(witcher_alcohol.build());
					}
				}

				if(BuiltInLootTables.SPAWN_BONUS_CHEST.equals(id) && source.isBuiltin()){

					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0,2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get())).apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.5f));

					final LootPool.Builder witcher_alcohol = LootPool.lootPool()
							.setRolls(UniformGenerator.between(1,3))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get()).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get()).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
							.add(LootItem.lootTableItem(TCOTS_Items.ICY_SPIRIT.get()).setWeight(18).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,5))))
							.when(LootItemRandomChanceCondition.randomChance(0.3f));
				}

			}

			//Sniffer Digging
			{
				if (BuiltInLootTables.SNIFFER_DIGGING.equals(id) && source.isBuiltin()) {

					final LootPool.Builder allspice = LootPool.lootPool()
							.setRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(TCOTS_Items.ALLSPICE.get()))
							.when(LootItemRandomChanceCondition.randomChance(0.4f));

					tableBuilder.pool(allspice.build());
				}
			}
		});


		LootTableEvents.REPLACE.register((id, lootTable, source, wrapperLookup) -> {

			//Witcher Grave - Disable Winters Blade
			{
				if(id.location().equals(ResourceLocation.fromNamespaceAndPath("witcher_rpg","chests/witcher_grave"))){

					final LootTable.Builder newLootTable = new LootTable.Builder();

					final LootPool.Builder ingredientsPool = LootPool.lootPool().setRolls(UniformGenerator.between(3,5))
							.setBonusRolls(ConstantValue.exactly(1))
							.add(LootItem.lootTableItem(Items.LEATHER)
									.setWeight(10)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1,5)))
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("more_rpg_classes", "hardened_leather")))
									.setWeight(7)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2)))
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "meteorite_ingot")))
									.setWeight(5)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_iron_ingot")))
									.setWeight(5)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
							)
							.add(LootItem.lootTableItem(TCOTS_Items.CURED_MONSTER_LEATHER.get())
									.setWeight(2)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2)))
							)
							.add(LootItem.lootTableItem(TCOTS_Items.DWARVEN_SPIRIT.get())
									.setWeight(1)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4)))
							)
							.add(LootItem.lootTableItem(TCOTS_Items.ALCOHEST.get())
									.setWeight(1)
									.apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4)))
							);

					final LootPool.Builder diagramsPool = LootPool.lootPool().setRolls(UniformGenerator.between(1,1))
							.setBonusRolls(ConstantValue.exactly(1))
							.add(EmptyLootItem.emptyItem()
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_feline_diagram")))
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_griffin_diagram")))
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_ursine_diagram")))
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_wolven_diagram")))
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_feline_diagram")))
									.setWeight(1)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_griffin_diagram")))
									.setWeight(1)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_ursine_diagram")))
									.setWeight(1)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_wolven_diagram")))
									.setWeight(1)
							);

					final LootPool.Builder weaponsPool = LootPool.lootPool().setRolls(UniformGenerator.between(1,1))
							.setBonusRolls(ConstantValue.exactly(1))
							.add(EmptyLootItem.emptyItem()
									.setWeight(10)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "silver_witcher_sword")))
									.setWeight(10)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "iron_witcher_sword")))
									.setWeight(10)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_witcher_sword")))
									.setWeight(10)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "diamond_witcher_sword")))
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "meteorite_witcher_sword")))
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_iron_witcher_sword")))
									.setWeight(5)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "ultimatum_sword")))
									.setWeight(3)
							)
							.add(LootItem.lootTableItem(TCOTS_Items.DYAEBL.get())
									.setWeight(3)
							)
							.add(LootItem.lootTableItem(TCOTS_Items.ARDAENYE.get())
									.setWeight(1)
							)
							.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "aerondight_sword")))
									.setWeight(1)
							);


					final LootPool.Builder alchemy_formulae = LootPool.lootPool()
							.setRolls(UniformGenerator.between(0, 2))
							.add(LootItem.lootTableItem(TCOTS_Items.ALCHEMY_FORMULA.get()))
							.apply(AlchemyRecipeRandomlyLootFunction.builder())
							.when(LootItemRandomChanceCondition.randomChance(0.45f));


					newLootTable.withPool(ingredientsPool);
					newLootTable.withPool(diagramsPool);
					newLootTable.withPool(weaponsPool);
					newLootTable.withPool(alchemy_formulae);

					if(FabricLoader.getInstance().isModLoaded("witcher_medallions")){
						final LootPool.Builder medallionsPool = LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
								.add(EmptyLootItem.emptyItem()
										.setWeight(8)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "wolf-medallion-off")))
										.setWeight(5)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "cat-medallion-off")))
										.setWeight(5)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "bear-medallion-off")))
										.setWeight(5)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "griffin-medallion-off")))
										.setWeight(5)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "viper-medallion-off")))
										.setWeight(5)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "manticore-medallion-off")))
										.setWeight(5)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "wolf-medallion")))
										.setWeight(1)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "cat-medallion")))
										.setWeight(1)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "bear-medallion")))
										.setWeight(1)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "griffin-medallion")))
										.setWeight(1)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "viper-medallion")))
										.setWeight(1)
								)
								.add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "manticore-medallion")))
										.setWeight(1)
								);

						newLootTable.withPool(medallionsPool);
					}

					return newLootTable.build();
				}
			}

			return lootTable;
		});
	}

	public static class VillagerCustomTrades {
		@SuppressWarnings("all")
		public static void registerTrades(){
			//Uses            --> 16/12/3
			//Experience      --> 1/2/5/10/15/20/30
			//PriceMultiplier --> 0.05/0.2


			//Herbalist
			{
				TradeOfferHelper.registerVillagerOffers(TCOTS_Villagers.HERBALIST.get(), 1,
						factories -> {
							//Sell
							{
								factories.add(new VillagerTrades.ItemsForEmeralds(
										//Gives
										TCOTS_Items.ALCHEMY_BOOK.get(),
										6,
										1,
										3,
										10
								));


								factories.add(new VillagerTrades.ItemsForEmeralds(
										//Gives
										TCOTS_Items.ICY_SPIRIT.get(),
										4,
										1,
										16,
										1)
								);
							}

							//Buys
							{
								factories.add(new VillagerTrades.EmeraldForItems(
										Items.DANDELION,
										12,
										16,
										2)
								);
							}
						});

				TradeOfferHelper.registerVillagerOffers(TCOTS_Villagers.HERBALIST.get(), 2,
						factories -> {
							//Sell
							{
								factories.add((entity, random) ->
										new MerchantOffer(
												//Wants
												new ItemCost(Items.EMERALD, 16+ random.nextIntBetweenInclusive(0,32)),
												//Gives
												AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items.ALCHEMY_FORMULA.get().getDefaultInstance(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
												3,
												60,
												0.2f));

								factories.add(new VillagerTrades.ItemsForEmeralds(
										TCOTS_Items.ALLSPICE.get(),
										6,
										1,
										5)
								);

								factories.add(new VillagerTrades.ItemsForEmeralds(
										Items.HONEYCOMB,
										8,
										1,
										5)
								);

							}

							//Buys
							{

								factories.add((entity, random) -> new MerchantOffer(
										//Wants
										new ItemCost(Items.ALLIUM, 4),
										//Gives
										new ItemStack(Items.EMERALD, 6),
										12,
										10,
										0.05f));
							}

						});

				TradeOfferHelper.registerVillagerOffers(TCOTS_Villagers.HERBALIST.get(), 3,
						factories -> {

							//Sell
							{
								factories.add((entity, random) -> new MerchantOffer(
										//Wants
										new ItemCost(Items.EMERALD, 8),
										//Gives
										new ItemStack(TCOTS_Items.CHERRY_CORDIAL.get(), 1),
										12,
										10,
										0.05f));

								factories.add((entity, random) -> new MerchantOffer(
										//Wants
										new ItemCost(Items.EMERALD, 12),
										//Gives
										new ItemStack(TCOTS_Items.MANDRAKE_CORDIAL.get(), 1),
										12,
										10,
										0.05f));


								factories.add((entity, random) -> new MerchantOffer(
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

								factories.add((entity, random) -> new MerchantOffer(
										//Wants
										new ItemCost(TCOTS_Items.MONSTER_FAT.get(), 4),
										//Gives
										new ItemStack(Items.EMERALD, 8),
										12,
										20,
										0.05f));

							}

						});

				TradeOfferHelper.registerVillagerOffers(TCOTS_Villagers.HERBALIST.get(), 4,
						factories -> {
							//Sells
							{
								factories.add((entity, random) -> new MerchantOffer(
										//Wants
										new ItemCost(Items.EMERALD, 16 + random.nextIntBetweenInclusive(0, 32)),
										//Gives
										AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items.ALCHEMY_FORMULA.get().getDefaultInstance(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
										3,
										100,
										0.2f));

								factories.add((entity, random) -> new MerchantOffer(
										//Wants
										new ItemCost(Items.EMERALD, 6),
										//Gives
										new ItemStack(TCOTS_Items.ALCHEMY_PASTE, 1),
										16,
										15,
										0.05f));

								factories.add((entity, random) -> new MerchantOffer(
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
								factories.add((entity, random) -> new MerchantOffer(
										//Wants
										new ItemCost(TCOTS_Items.WATER_ESSENCE.get(), 2),
										//Gives
										new ItemStack(Items.EMERALD, 8),
										12,
										15,
										0.05f));

							}

						});

				TradeOfferHelper.registerVillagerOffers(TCOTS_Villagers.HERBALIST.get(), 5,
						factories -> {

							//Sell
							{
								{
									//Potions
									{
										//Enhanced
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SWALLOW_POTION, 32, TCOTS_Items.SWALLOW_POTION_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.CAT_POTION, 32, TCOTS_Items.CAT_POTION_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_RAFFARDS_DECOCTION, 32, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BLACK_BLOOD_POTION, 32, TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MARIBOR_FOREST_POTION, 32, TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WOLF_POTION, 32, TCOTS_Items.WOLF_POTION_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BINDWEED_POTION, 32, TCOTS_Items.BINDWEED_POTION_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ROOK_POTION, 32, TCOTS_Items.ROOK_POTION_ENHANCED));

										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_HONEY_POTION, 16, TCOTS_Items.WHITE_HONEY_POTION_ENHANCED));


										//Superior
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SWALLOW_POTION_ENHANCED, 48, TCOTS_Items.SWALLOW_POTION_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.CAT_POTION_ENHANCED, 48, TCOTS_Items.CAT_POTION_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED, 48, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED, 48, TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED, 48, TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WOLF_POTION_ENHANCED, 48, TCOTS_Items.WOLF_POTION_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BINDWEED_POTION_ENHANCED, 48, TCOTS_Items.BINDWEED_POTION_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ROOK_POTION_ENHANCED, 48, TCOTS_Items.ROOK_POTION_SUPERIOR));

										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED, 32, TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR));
									}

									//Bombs
									{
										//Enhanced
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.GRAPESHOT, 32, TCOTS_Items.GRAPESHOT_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SAMUM, 32, TCOTS_Items.SAMUM_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DANCING_STAR, 32, TCOTS_Items.DANCING_STAR_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DEVILS_PUFFBALL, 32, TCOTS_Items.DEVILS_PUFFBALL_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DRAGONS_DREAM, 32, TCOTS_Items.DRAGONS_DREAM_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NORTHERN_WIND, 32, TCOTS_Items.NORTHERN_WIND_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DIMERITIUM_BOMB, 32, TCOTS_Items.DIMERITIUM_BOMB_ENHANCED));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MOON_DUST, 32, TCOTS_Items.MOON_DUST_ENHANCED));

										//Superior
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.GRAPESHOT_ENHANCED, 56, TCOTS_Items.GRAPESHOT_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SAMUM_ENHANCED, 56, TCOTS_Items.SAMUM_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DANCING_STAR_ENHANCED, 56, TCOTS_Items.DANCING_STAR_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED, 56, TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DRAGONS_DREAM_ENHANCED, 56, TCOTS_Items.DRAGONS_DREAM_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NORTHERN_WIND_ENHANCED, 56, TCOTS_Items.NORTHERN_WIND_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED, 56, TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MOON_DUST_ENHANCED, 56, TCOTS_Items.MOON_DUST_SUPERIOR));
									}

									//Oils
									{
										//Enhanced
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NECROPHAGE_OIL, 16, TCOTS_Items.ENHANCED_NECROPHAGE_OIL));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.OGROID_OIL, 16, TCOTS_Items.ENHANCED_OGROID_OIL));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BEAST_OIL, 16, TCOTS_Items.ENHANCED_BEAST_OIL));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.HANGED_OIL, 16, TCOTS_Items.ENHANCED_HANGED_OIL));

										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_NECROPHAGE_OIL, 36, TCOTS_Items.SUPERIOR_NECROPHAGE_OIL));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_OGROID_OIL, 36, TCOTS_Items.SUPERIOR_OGROID_OIL));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_BEAST_OIL, 36, TCOTS_Items.SUPERIOR_BEAST_OIL));
										factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_HANGED_OIL, 36, TCOTS_Items.SUPERIOR_HANGED_OIL));

									}

									//Ingredients
									{
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.WHITE_GULL));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.STAMMELFORDS_DUST));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.AETHER));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.HYDRAGENUM));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.NIGREDO));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.QUEBRITH));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.REBIS));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.RUBEDO));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.VERMILION));
										factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.VITRIOL));
									}
								}
							}

						});
			}

			//Farmer
			//Alcohol & Ergot Seeds
			{
				TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(Items.EMERALD, 1),
									//Gives
									new ItemStack(TCOTS_Items.ERGOT_SEEDS.get(), 1),
									16,
									5,
									0.05f));
						}
				);

				TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(Items.EMERALD, 6),
									//Gives
									new ItemStack(TCOTS_Items.VILLAGE_HERBAL.get(), 1),
									12,
									5,
									0.05f));
						}
				);
			}

			//Butcher
			//Monster Fat
			{
				TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 3,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(Items.EMERALD, 12),
									//Gives
									new ItemStack(TCOTS_Items.MONSTER_FAT.get(), 1),
									12,
									15,
									0.2f));
						}
				);
			}

			//Librarian
			//Bestiary
			{
				TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 1,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(Items.EMERALD, 12),
									//Gives
									new ItemStack(TCOTS_Items.WITCHER_BESTIARY.get(), 1),
									3,
									10,
									0.05f));
						}
				);
			}

			//Cleric
			//Monster Parts
			{
				TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(TCOTS_Items.DEVOURER_TEETH.get(), 8),
									//Gives
									new ItemStack(Items.EMERALD, 16),
									3,
									15,
									0.2f));
						}
				);

				TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get(), 1),
									//Gives
									new ItemStack(Items.EMERALD, 16),
									3,
									20,
									0.2f));
						}
				);

				TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(TCOTS_Items.GRAVEIR_BONE.get(), 2),
									//Gives
									new ItemStack(Items.EMERALD, 16),
									3,
									15,
									0.2f));
						}
				);

				TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
						factories -> {
							factories.add((entity, random) -> new MerchantOffer(
									//Wants
									new ItemCost(TCOTS_Items.CADAVERINE.get(), 16),
									//Gives
									new ItemStack(Items.EMERALD, 2),
									12,
									5,
									0.05f));
						}
				);
			}

			//Cartographer
			{
				TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 3,
						factories -> factories.add(
								new VillagerTrades.TreasureMapForEmeralds(
										16,
										TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "on_ice_giant_maps")),
										"filled_map.giant_cave",
										TCOTS_MapIcons.GiantCave(),
										12,
										10))
				);
			}
		}

		private static MerchantOffer upgradeRecipeTrade(final Supplier<Item> recipeToUpgrade, final int Cost, final Supplier<Item> upgradedRecipe){
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

		private static MerchantOffer miscRecipeTrade(final Supplier<? extends Item> item){
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

	public static void registerCustomSpawners(){
		ServerWorldEvents.LOAD.register(((server, world) -> {
			if (world.isClientSide()) {
				return;
			}

			ServerWorldSpawnersUtil.register(world, new BullvoreSpawner());
		}));
	}

	@SuppressWarnings("unused")
	public static void registerCompostableItems(){
		final float f = 0.3f;
		final float g = 0.5f;
		final float h = 0.65f;
		final float i = 0.85f;
		final float j = 1.0f;
		ComposterBlock.add(0.65f, TCOTS_Items.ARENARIA.get());
		ComposterBlock.add(0.3f, TCOTS_Items.ALLSPICE.get());

		ComposterBlock.add(0.65f, TCOTS_Items.BRYONIA.get());

		ComposterBlock.add(0.65f, TCOTS_Items.CELANDINE.get());
		ComposterBlock.add(0.65f, TCOTS_Items.CROWS_EYE.get());
		ComposterBlock.add(0.85f, TCOTS_Items.CADAVERINE.get());

		ComposterBlock.add(0.65f, TCOTS_Items.HAN_FIBER.get());


		ComposterBlock.add(0.65f, TCOTS_Items.PUFFBALL.get());
		ComposterBlock.add(0.85f, TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM.get());


		ComposterBlock.add(0.65f, TCOTS_Items.SEWANT_MUSHROOMS.get());
		ComposterBlock.add(0.85f, TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM.get());
		ComposterBlock.add(0.85f, TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM.get());

		ComposterBlock.add(0.65f, TCOTS_Items.VERBENA.get());
	}

	public static void registerBiomeModificationSpawn(){
		//Necrophages
		{
			//Drowners
			{
				//In swamps
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.DROWNER_SWAMP), MobCategory.MONSTER,
						TCOTS_Entities.DROWNER.get(), 130, 3, 5);

				//In beaches
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.DROWNER_BEACH), MobCategory.MONSTER,
						TCOTS_Entities.DROWNER.get(), 50, 2, 4);

				//Swimming in oceans/rivers
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.DROWNER_WATER), MobCategory.MONSTER,
						TCOTS_Entities.DROWNER.get(), 8, 2, 3);
			}

			//Rotfiends
			{
				//In night
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.ROTFIEND), MobCategory.MONSTER,
						TCOTS_Entities.ROTFIEND.get(), 80, 4, 6);
			}

			//Foglets
			{
				//In swamps/rivers
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.FOGLET_SWAMP), MobCategory.MONSTER,
						TCOTS_Entities.FOGLET.get(), 80, 1, 3);

				//In dark forests
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.FOGLET_DARK), MobCategory.MONSTER,
						TCOTS_Entities.FOGLET.get(), 120, 1, 2);

				//In forests/mountains
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.FOGLET_HILLS_FORESTS), MobCategory.MONSTER,
						TCOTS_Entities.FOGLET.get(), 50, 1, 2);
			}

			//Water Hags
			{
				//In swamps
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.WATER_HAG_SWAMP), MobCategory.MONSTER,
						TCOTS_Entities.WATER_HAG.get(), 80, 1, 2);

				//In rivers
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.WATER_HAG_RIVER), MobCategory.MONSTER,
						TCOTS_Entities.WATER_HAG.get(), 20, 1, 2);
			}

			//Grave Hags
			{
				//In night
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.GRAVE_HAG), MobCategory.MONSTER,
						TCOTS_Entities.GRAVE_HAG.get(), 80, 1, 2);
			}

			//Ghouls & Alghouls
			{
				//In night
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.GHOUL), MobCategory.MONSTER,
						TCOTS_Entities.GHOUL.get(), 10, 3, 5);
			}

			//Scurvers
			{
				//In night
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.SCURVER), MobCategory.MONSTER,
						TCOTS_Entities.SCURVER.get(), 40, 2, 3);
			}

			//Devourer
			{
				//In night
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.DEVOURER), MobCategory.MONSTER,
						TCOTS_Entities.DEVOURER.get(), 60, 3, 4);
			}

			//Bloedzuiger
			{
				//In night
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.BLOEDZUIGER), MobCategory.MONSTER,
						TCOTS_Entities.BLOEDZUIGER.get(), 100, 3, 6);
			}

			//Graveir
			{
				//In Caves
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.GRAVEIR), MobCategory.MONSTER,
						TCOTS_Entities.GRAVEIR.get(), 60, 1, 2);
			}
		}

		//Ogroids
		{
			//Nekkers
			{
				//In night
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.NEKKER), MobCategory.MONSTER,
						TCOTS_Entities.NEKKER.get(), 5, 4, 6);
			}

			//Cyclops
			{
				//In snowy plains/mountains/taigas
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.CYCLOPS), MobCategory.MONSTER,
						TCOTS_Entities.CYCLOPS.get(), 15, 1, 1);
			}

			//Rock Troll
			{
				//In mountains/taigas
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.ROCK_TROLL), MobCategory.MONSTER,
						TCOTS_Entities.ROCK_TROLL.get(), 5, 1, 1);
			}

			//Ice Troll
			{
				//In snowy plains/mountains/taigas
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.ICE_TROLL), MobCategory.MONSTER,
						TCOTS_Entities.ICE_TROLL.get(), 2, 1, 1);
			}


			//Forest Troll
			{
				//In forests
				BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Tags.FOREST_TROLL), MobCategory.MONSTER,
						TCOTS_Entities.FOREST_TROLL.get(), 5, 1, 1);
			}
		}
	}

	public static void specificLoaderStuff(){
		//Registers Lavender Books to be readable
		LavenderBookItem.registerForBook((LavenderBookItem) TCOTS_Items.ALCHEMY_BOOK.get());
		LavenderBookItem.registerForBook((LavenderBookItem) TCOTS_Items.WITCHER_BESTIARY.get());

		TCOTS_Items.initDataComponents();
		TCOTS_MapIcons.initMapIcons();
	}

}
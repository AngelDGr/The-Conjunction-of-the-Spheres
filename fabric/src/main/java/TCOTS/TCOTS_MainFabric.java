package TCOTS;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.entity.TCOTS_Entities_Fabric;
import TCOTS.items.TCOTS_DynamicRecipes_Fabric;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.TCOTS_ItemsGroups;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.mixin.ServerWorldAccessor;
import TCOTS.particles.TCOTS_Particles_Fabric;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.world.TCOTS_Features;
import TCOTS.world.TCOTS_PlacedFeature;
import TCOTS.world.spawn.BullvoreSpawner;
import TCOTS.world.village.TCOTS_PointOfInterest;
import TCOTS.world.village.TCOTS_VillageAdditions;
import TCOTS.world.village.VillagerCustomTrades;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.ArrayList;
import java.util.List;

public class TCOTS_MainFabric implements ModInitializer {

	public record WitcherEyesFullPacket(Boolean activate, int shape, int separation, float eyePosX, float eyePosY) {}

	public record RetrieveAnchorPacket() {}

	public record ToxicityFacePacket(Boolean activate){}


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		TCOTS_Main.init();

		TCOTS_Blocks_Fabric.registerBlocks();
		TCOTS_Effects.registerEffects();
		TCOTS_Items_Fabric.registerAlchemyIngredients();
		TCOTS_Items_Fabric.registerItemsMisc();
		TCOTS_Items_Fabric.registerDrops();
		TCOTS_Items_Fabric.registerAlchemyConcoctions();
		TCOTS_Items_Fabric.registerWeapons_Armors();
		TCOTS_ItemsGroups.registerGroupItems();
		TCOTS_Items_Fabric.modifyLootTables();
		TCOTS_Items_Fabric.registerCompostableItems();
		TCOTS_DynamicRecipes_Fabric.registerDynamicRecipes();
		ScreenHandlersAndRecipesRegister.registerScreenHandlersAndRecipes();
		TCOTS_Sounds.registerSounds();
		TCOTS_Entities_Fabric.addSpawns();
		TCOTS_Entities_Fabric.setEntitiesAttributes();
		TCOTS_Particles_Fabric.registerParticles();
		TCOTS_Features.registerFeatures();

		TCOTS_PlacedFeature.generateVegetation();
		TCOTS_VillageAdditions.registerNewVillageStructures();
		TCOTS_PointOfInterest.registerVillagers();
		VillagerCustomTrades.registerTrades();
		TCOTS_VillageAdditions.registerNewStructures();
		TCOTS_Criteria.registerCriteria();

		//OwO Networking receivers
		{
			//Witcher Eyes
			{
				//Receive the packet from the client
				TCOTS_Main.PACKETS_CHANNEL.registerServerbound(WitcherEyesFullPacket.class, ((message, access) ->
				{
					ServerPlayer player = access.player();

					player.theConjunctionOfTheSpheres$setWitcherEyesActivated(message.activate);

					player.theConjunctionOfTheSpheres$setEyeShape(message.shape);

					player.theConjunctionOfTheSpheres$setEyeSeparation(message.separation);

					player.theConjunctionOfTheSpheres$getEyesPivot().setComponent(0, message.eyePosX);

					player.theConjunctionOfTheSpheres$getEyesPivot().setComponent(1, -1*message.eyePosY);

					//Send another packet to the client, for full sync ->
					TCOTS_Main.PACKETS_CHANNEL.serverHandle(player).send(new WitcherEyesFullPacket(
							message.activate, message.shape, message.separation, message.eyePosX, message.eyePosY));
				}));


				//Receive the packet from the server
				TCOTS_Main.PACKETS_CHANNEL.registerClientbound(WitcherEyesFullPacket.class, ((message, access) ->
				{
					LocalPlayer player = access.player();

					player.theConjunctionOfTheSpheres$setWitcherEyesActivated(message.activate);

					player.theConjunctionOfTheSpheres$setEyeShape(message.shape);

					player.theConjunctionOfTheSpheres$setEyeSeparation(message.separation);

					player.theConjunctionOfTheSpheres$getEyesPivot().setComponent(0, message.eyePosX);

					player.theConjunctionOfTheSpheres$getEyesPivot().setComponent(1, -1*message.eyePosY);

				}));
			}

			//Toxicity Face
			{

				//Receive the packet from the client
				TCOTS_Main.PACKETS_CHANNEL.registerServerbound(ToxicityFacePacket.class, ((message, access) ->
				{
					ServerPlayer player = access.player();

					player.theConjunctionOfTheSpheres$setToxicityActivated(message.activate);


					//Send another packet to the client, for full sync ->
					TCOTS_Main.PACKETS_CHANNEL.serverHandle(player).send(new ToxicityFacePacket(message.activate));
				}));


				//Receive the packet from the server
				TCOTS_Main.PACKETS_CHANNEL.registerClientbound(ToxicityFacePacket.class, ((message, access) ->
				{
					LocalPlayer player = access.player();

					player.theConjunctionOfTheSpheres$setToxicityActivated(message.activate);

				}));
			}

			//Anchor
			{
				TCOTS_Main.PACKETS_CHANNEL.registerServerbound(RetrieveAnchorPacket.class, ((message, access) ->
						GiantAnchorItem.retrieveAnchor(access.player())));
			}
		}

		//Dispense Behaviors
		{
			//Dispenser with splash potions
			DispenserBlock.registerProjectileBehavior(TCOTS_Items_Fabric.KILLER_WHALE_SPLASH);
			DispenserBlock.registerProjectileBehavior(TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION_SPLASH);
			DispenserBlock.registerProjectileBehavior(TCOTS_Items_Fabric.SWALLOW_SPLASH);
			DispenserBlock.registerProjectileBehavior(TCOTS_Items_Fabric.WATER_HAG_MUD_BALL);

			DispenserBlock.registerProjectileBehavior(TCOTS_Items_Fabric.SCURVER_SPINE);
		}


		//Bullvore Spawner
		{
			ServerWorldEvents.LOAD.register(((server, world) -> {
				if (world.isClientSide()) {
					return;
				}

				ServerWorldSpawnersUtil.register(world, new BullvoreSpawner());
			}));
		}
	}

	public static class ServerWorldSpawnersUtil {
		public static void register(ServerLevel world, CustomSpawner spawner) {
			List<CustomSpawner> spawnerList = new ArrayList<>(((ServerWorldAccessor) world).getCustomSpawners());
			spawnerList.add(spawner);
			((ServerWorldAccessor) world).setCustomSpawners(spawnerList);
		}
	}

}
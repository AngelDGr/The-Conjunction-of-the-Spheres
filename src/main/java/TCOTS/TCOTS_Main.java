package TCOTS;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.config.TCOTS_Config;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.TCOTS_ItemsGroups;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.mixin.ServerWorldAccessor;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.screen.ToxicityHudOverlay;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.world.TCOTS_Features;
import TCOTS.world.TCOTS_PlacedFeature;
import TCOTS.world.spawn.BullvoreSpawner;
import TCOTS.world.village.TCOTS_PointOfInterest;
import TCOTS.world.village.TCOTS_VillageAdditions;
import TCOTS.world.village.VillagerCustomTrades;
import com.mojang.logging.LogUtils;
import io.wispforest.owo.network.OwoNetChannel;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.spawner.SpecialSpawner;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TCOTS_Main implements ModInitializer {
	public static final Logger LOGGER = LogUtils.getLogger();
	public static String MOD_ID = "tcots-witcher";
	public static final TCOTS_Config CONFIG = TCOTS_Config.createAndLoad();
	public static final OwoNetChannel PACKETS_CHANNEL = OwoNetChannel.create(Identifier.of(TCOTS_Main.MOD_ID, "main"));


	public record WitcherEyesFullPacket(Boolean activate, int shape, int separation, float eyePosX, float eyePosY) {}

	public record RetrieveAnchorPacket() {}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		TCOTS_Blocks.registerBlocks();
		TCOTS_Effects.registerEffects();
		TCOTS_Items.registerAlchemyIngredients();
		TCOTS_Items.registerItemsMisc();
		TCOTS_Items.registerDrops();
		TCOTS_Items.registerAlchemyConcoctions();
		TCOTS_Items.registerWeapons_Armors();
		TCOTS_ItemsGroups.registerGroupItems();
		TCOTS_Items.modifyLootTables();
		TCOTS_Items.registerCompostableItems();
		ScreenHandlersAndRecipesRegister.registerScreenHandlersAndRecipes();
		TCOTS_Sounds.registerSounds();
		TCOTS_Entities.addSpawns();
		TCOTS_Entities.setEntitiesAttributes();
		TCOTS_Particles.registerParticles();
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
				//Receive the packet from the client <-
				TCOTS_Main.PACKETS_CHANNEL.registerServerbound(WitcherEyesFullPacket.class, ((message, access) ->
				{
					ServerPlayerEntity player = access.player();

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
				TCOTS_Main.PACKETS_CHANNEL.registerClientbound(TCOTS_Main.WitcherEyesFullPacket.class, ((message, access) ->
				{
					ClientPlayerEntity player = access.player();

					player.theConjunctionOfTheSpheres$setWitcherEyesActivated(message.activate);

					player.theConjunctionOfTheSpheres$setEyeShape(message.shape);

					player.theConjunctionOfTheSpheres$setEyeSeparation(message.separation);

					player.theConjunctionOfTheSpheres$getEyesPivot().setComponent(0, message.eyePosX);

					player.theConjunctionOfTheSpheres$getEyesPivot().setComponent(1, -1*message.eyePosY);

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
			DispenserBlock.registerProjectileBehavior(TCOTS_Items.KILLER_WHALE_SPLASH);
			DispenserBlock.registerProjectileBehavior(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH);
			DispenserBlock.registerProjectileBehavior(TCOTS_Items.SWALLOW_SPLASH);
			DispenserBlock.registerProjectileBehavior(TCOTS_Items.WATER_HAG_MUD_BALL);

			DispenserBlock.registerProjectileBehavior(TCOTS_Items.SCURVER_SPINE);
		}


		//Bullvore Spawner
		{
			ServerWorldEvents.LOAD.register(((server, world) -> {
				if (world.isClient()) {
					return;
				}

				ServerWorldSpawnersUtil.register(world, new BullvoreSpawner());
			}));
		}
	}

	public static class ServerWorldSpawnersUtil {
		public static void register(ServerWorld world, SpecialSpawner spawner) {
			List<SpecialSpawner> spawnerList = new ArrayList<>(((ServerWorldAccessor) world).getSpawners());
			spawnerList.add(spawner);
			((ServerWorldAccessor) world).setSpawners(spawnerList);
		}
	}

}
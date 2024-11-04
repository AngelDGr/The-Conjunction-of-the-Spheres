package TCOTS;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.config.TCOTS_Config;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.entity.misc.WaterHag_MudBallEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.TCOTS_ItemsGroups;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import TCOTS.mixin.ServerWorldAccessor;
import TCOTS.particles.TCOTS_Particles;
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
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.minecraft.world.spawner.SpecialSpawner;
import org.joml.Vector3f;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.ArrayList;
import java.util.List;

public class TCOTS_Main implements ModInitializer {
	public static final Logger LOGGER = LogUtils.getLogger();
	public static String MOD_ID = "tcots-witcher";
	public static final TCOTS_Config CONFIG = TCOTS_Config.createAndLoad();
	public static final OwoNetChannel MY_CHANNEL = OwoNetChannel.create(new Identifier(TCOTS_Main.MOD_ID, "main"));
	public record WitcherEyesPacket(int someData, String otherData, Identifier aMinecraftClass) {}


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		GeckoLib.initialize();
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

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			player.theConjunctionOfTheSpheres$setWitcherEyesActivated(TCOTS_Main.CONFIG.witcher_eyes.activate());

			player.theConjunctionOfTheSpheres$setEyeSeparation(TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal());

			player.theConjunctionOfTheSpheres$setEyeShape(TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal());


			player.theConjunctionOfTheSpheres$setEyesPivot(
					new Vector3f(
							TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
							-TCOTS_Main.CONFIG.witcher_eyes.YEyePos(),
							0));
		});

		//OwO Networking
//		MY_CHANNEL.registerServerbound(WitcherEyesPacket.class, ((message, access) -> {
//			ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
//
//			MY_CHANNEL.serverHandle(clientPlayer).send(new WitcherEyesPacket());
//		}));


		//Dispense Behaviors
		{
			//Dispenser with splash potions
			DispenserBlock.registerBehavior(TCOTS_Items.KILLER_WHALE_SPLASH, TCOTS_Items.getSplashBehavior());
			DispenserBlock.registerBehavior(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH, TCOTS_Items.getSplashBehavior());
			DispenserBlock.registerBehavior(TCOTS_Items.SWALLOW_SPLASH, TCOTS_Items.getSplashBehavior());

			DispenserBlock.registerBehavior(TCOTS_Items.WATER_HAG_MUD_BALL, new ProjectileDispenserBehavior() {
				@Override
				protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
					return Util.make(new WaterHag_MudBallEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
				}
			});

			DispenserBlock.registerBehavior(TCOTS_Items.SCURVER_SPINE, new ProjectileDispenserBehavior() {
				@Override
				protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
					ScurverSpineEntity scurverSpine = new ScurverSpineEntity(world, position.getX(), position.getY(), position.getZ(), stack.copyWithCount(1));
					scurverSpine.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
					return scurverSpine;
				}
			});
		}


		//Bullvore Spawner
		ServerWorldEvents.LOAD.register(((server, world) -> {
			if (world.isClient()) {
				return;
			}

			ServerWorldSpawnersUtil.register(world, new BullvoreSpawner());
		}));
	}

	public static class ServerWorldSpawnersUtil {
		public static void register(ServerWorld world, SpecialSpawner spawner) {
			List<SpecialSpawner> spawnerList = new ArrayList<>(((ServerWorldAccessor) world).getSpawners());
			spawnerList.add(spawner);
			((ServerWorldAccessor) world).setSpawners(spawnerList);
		}
	}

}
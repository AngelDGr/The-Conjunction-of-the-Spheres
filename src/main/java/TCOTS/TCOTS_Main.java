package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.config.TCOTS_Config;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.entity.misc.WaterHag_MudBallEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.TCOTS_ItemsGroups;
import TCOTS.items.potions.TCOTS_Effects;
import TCOTS.items.potions.recipes.AlchemyTableRecipesRegister;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.world.TCOTS_Features;
import TCOTS.world.TCOTS_PlacedFeature;
import TCOTS.world.village.TCOTS_PointOfInterest;
import TCOTS.world.village.TCOTS_VillageAdditions;
import TCOTS.world.village.VillagerCustomTrades;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import software.bernie.geckolib.GeckoLib;

public class TCOTS_Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.


	public static String MOD_ID = "tcots-witcher";
	public static final TCOTS_Config CONFIG = TCOTS_Config.createAndLoad();
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
		AlchemyTableRecipesRegister.registerPotionRecipes();
		TCOTS_Sounds.registerSounds();
		TCOTS_Entities.addSpawns();
		TCOTS_Entities.addAttributes();
		TCOTS_Particles.registerParticles();
		TCOTS_Features.registerFeatures();

		TCOTS_PlacedFeature.generateVegetation();
		TCOTS_VillageAdditions.registerNewVillageStructures();
		TCOTS_PointOfInterest.registerVillagers();
		VillagerCustomTrades.registerTrades();

		//Dispenser with splash potions
		DispenserBlock.registerBehavior(TCOTS_Items.KILLER_WHALE_SPLASH, TCOTS_Items.getSplashBehavior());
		DispenserBlock.registerBehavior(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH, TCOTS_Items.getSplashBehavior());
		DispenserBlock.registerBehavior(TCOTS_Items.SWALLOW_SPLASH, TCOTS_Items.getSplashBehavior());

		DispenserBlock.registerBehavior(TCOTS_Items.WATER_HAG_MUD_BALL, new ProjectileDispenserBehavior(){
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				return Util.make(new WaterHag_MudBallEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
			}
		});

		DispenserBlock.registerBehavior(TCOTS_Items.SCURVER_SPINE, new ProjectileDispenserBehavior(){
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				ScurverSpineEntity scurverSpine = new ScurverSpineEntity(world, position.getX(), position.getY(), position.getZ(), stack.copyWithCount(1));
				scurverSpine.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
				return scurverSpine;
			}
		});
	}



}
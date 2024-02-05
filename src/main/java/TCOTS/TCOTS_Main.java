package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.entity.necrophages.RotfiendEntity;
import TCOTS.entity.ogroids.NekkerEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.TCOTS_ItemsGroups;
import TCOTS.potions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.potions.recipes.AlchemyTableRecipesRegister;
import TCOTS.sounds.TCOTS_Sounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import TCOTS.entity.TCOTS_Entities;
import software.bernie.geckolib.GeckoLib;

public class TCOTS_Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.


	public static String MOD_ID = "tcots-witcher";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		GeckoLib.initialize();
		TCOTS_Blocks.registerBlocks();
		TCOTS_Effects.registerEffects();
		TCOTS_Items.registerItemsMisc();
		TCOTS_Items.registerDrops();
		TCOTS_Items.registerPotions();
		TCOTS_ItemsGroups.registerGroupItems();
		AlchemyTableRecipesRegister.registerPotionRecipes();
		TCOTS_Sounds.init();
		TCOTS_Entities.addSpawns();
		TCOTS_Particles.registerParticles();
//		LOGGER.info("Hello Fabric world!");

//		getWitcherRecipesInputItems(new BrewingRecipeRegistry());

		//Drowner
		FabricDefaultAttributeRegistry.register(TCOTS_Entities.DROWNER, DrownerEntity.setAttributes());

		//Rotfiend
		FabricDefaultAttributeRegistry.register(TCOTS_Entities.ROTFIEND, RotfiendEntity.setAttributes());

		//Nekker
		FabricDefaultAttributeRegistry.register(TCOTS_Entities.NEKKER, NekkerEntity.setAttributes());

//		WitcherPotions_Recipes.getWitcherRecipesInputItems();
	}
//	public void getWitcherRecipesInputItems(BrewingRecipeRegistry instance) {
//
//		((BrewingRecipeRegistryAccess)instance).registerWitcherPotionRecipe_PotionInput(Potions.AWKWARD, TCOTS_Items.DROWNER_BRAIN, TCOTS_Items.SWALLOW_POTION);
//	}

}
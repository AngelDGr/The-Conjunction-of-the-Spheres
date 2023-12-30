package TCOTS;

import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.TCOTS_ItemsGroups;
import TCOTS.sounds.TCOTS_Sounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import TCOTS.entity.TCOTS_Entities;

public class TCOTS_Main implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	public static String MOD_ID = "tcots-witcher";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		TCOTS_Items.registerModItems();
		TCOTS_ItemsGroups.registerGroupItems();
		TCOTS_Sounds.init();
//		LOGGER.info("Hello Fabric world!");

		//Drowner
		FabricDefaultAttributeRegistry.register(TCOTS_Entities.DROWNER, DrownerEntity.setAttributes());
	}
}
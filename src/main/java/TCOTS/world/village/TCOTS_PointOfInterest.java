package TCOTS.world.village;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

@SuppressWarnings("unused")
public class TCOTS_PointOfInterest {

    public static final RegistryKey<PointOfInterestType> HERBAL_POI_KEY = registerKey("herbal_poi");
    public static final PointOfInterestType HERBAL_POI = registerPoi("herbal_poi", TCOTS_Blocks.HERBAL_TABLE);
    public static final VillagerProfession HERBALIST = registerProfession("herbalist_witcher", HERBAL_POI_KEY);

    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type) {
        return Registry.register(Registries.VILLAGER_PROFESSION, Identifier.of(TCOTS_Main.MOD_ID, name),
                new VillagerProfession(name,
                        entry -> entry.matchesKey(type),
                        entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(),
                        SoundEvents.ENTITY_VILLAGER_WORK_CLERIC));
    }

    private static PointOfInterestType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(Identifier.of(TCOTS_Main.MOD_ID, name), 1, 1, block);
    }

    public static RegistryKey<PointOfInterestType> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, Identifier.of(TCOTS_Main.MOD_ID, name));
    }

    public static void registerVillagers() {
    }
}

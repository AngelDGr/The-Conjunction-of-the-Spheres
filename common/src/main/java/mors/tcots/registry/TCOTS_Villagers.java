package mors.tcots.registry;

import mors.tcots.TCOTS_Main;
import mors.tcots.TCOTS_Registries;
import com.google.common.collect.ImmutableSet;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;

public class TCOTS_Villagers {

    public static final ResourceKey<PoiType> HERBAL_POI_KEY = TCOTS_Villagers.registerKey("herbal_poi");
    public static RegistrySupplier<VillagerProfession> HERBALIST = TCOTS_Villagers.registerProfession("herbalist_witcher", HERBAL_POI_KEY, "work_herbalist");

    @ExpectPlatform
    public static void registerVillagers() {
        throw new AssertionError();
    }

    @SuppressWarnings("all")
    private static RegistrySupplier<VillagerProfession> registerProfession(String name, ResourceKey<PoiType> type, String sound) {
        return TCOTS_Registries.VILLAGER_PROFESSIONS.register(name, ()->
                new VillagerProfession(name,
                        entry -> entry.is(type),
                        entry -> entry.is(type),
                        ImmutableSet.of(), ImmutableSet.of(),
                        TCOTS_Registries.SOUND_EVENTS.getRegistrar().get(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, sound)))
        );
    }

    @SuppressWarnings("all")
    private static RegistrySupplier<VillagerProfession> registerProfession(String name, ResourceKey<PoiType> type, SoundEvent sound) {
        return TCOTS_Registries.VILLAGER_PROFESSIONS.register(name, ()->
                        new VillagerProfession(name,
                                entry -> entry.is(type),
                                entry -> entry.is(type),
                                ImmutableSet.of(), ImmutableSet.of(),
                                sound)
        );
    }

    public static ResourceKey<PoiType> registerKey(final String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

}

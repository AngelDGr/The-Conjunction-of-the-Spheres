package TCOTS.world.village;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.sounds.TCOTS_Sounds;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class TCOTS_PointOfInterest {

    public static final ResourceKey<PoiType> HERBAL_POI_KEY = registerKey("herbal_poi");
    public static final PoiType HERBAL_POI = registerPoi("herbal_poi", TCOTS_Blocks_Fabric.HERBAL_TABLE);
    public static final VillagerProfession HERBALIST = registerProfession("herbalist_witcher", HERBAL_POI_KEY, TCOTS_Sounds.HERBALIST_WORKS);

    @SuppressWarnings("all")
    private static VillagerProfession registerProfession(String name, ResourceKey<PoiType> type, SoundEvent sound) {
        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name),
                new VillagerProfession(name,
                        entry -> entry.is(type),
                        entry -> entry.is(type),
                        ImmutableSet.of(), ImmutableSet.of(),
                        sound));
    }

    @SuppressWarnings("all")
    private static PoiType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name), 1, 1, block);
    }

    public static ResourceKey<PoiType> registerKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    public static void registerVillagers() {
    }
}

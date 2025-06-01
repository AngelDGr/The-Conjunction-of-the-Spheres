package TCOTS.registry.neoforge;


import TCOTS.TCOTS_Registries;
import TCOTS.registry.TCOTS_Blocks;
import com.google.common.collect.ImmutableSet;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class TCOTS_VillagersImpl {

    public static void registerVillagers() {
        registerPoi("herbal_poi", TCOTS_Blocks.HERBAL_TABLE);
    }

    @SuppressWarnings("all")
    public static RegistrySupplier<PoiType> registerPoi(String id, RegistrySupplier<Block> blockRegistrySupplier){
        return TCOTS_Registries.POI_TYPES.register(id, () -> new PoiType(ImmutableSet.copyOf(blockRegistrySupplier.get().getStateDefinition().getPossibleStates()), 1, 1));

    }
}

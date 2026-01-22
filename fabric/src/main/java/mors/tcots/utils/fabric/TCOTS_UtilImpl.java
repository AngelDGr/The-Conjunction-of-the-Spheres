package mors.tcots.utils.fabric;

import mors.tcots.compat.TCOTS_AccessoriesCompat;
import mors.tcots.utils.TCOTS_Util;
import mors.fabric.tcots.compat.TCOTS_TrinketsCompat;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

@SuppressWarnings("unused")
public class TCOTS_UtilImpl {

    public static boolean isModLoaded(final String id){
        return FabricLoader.getInstance().isModLoaded(id);
    }

    public static boolean isWearingAccessoryItem(final LivingEntity player, final Item medallion) {
        if(TCOTS_Util.isTrinketsLoaded())
            return TCOTS_TrinketsCompat.isWearingAccessoryItem(player, medallion);
        if(TCOTS_Util.isAccessoriesLoaded())
            return TCOTS_AccessoriesCompat.isWearingAccessoryItem(player, medallion);
        else
            return false;
    }
}

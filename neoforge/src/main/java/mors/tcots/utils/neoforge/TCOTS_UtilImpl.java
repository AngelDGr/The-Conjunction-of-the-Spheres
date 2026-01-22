package mors.tcots.utils.neoforge;

import mors.tcots.compat.TCOTS_AccessoriesCompat;
import mors.tcots.utils.TCOTS_Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

@SuppressWarnings("unused")
public class TCOTS_UtilImpl {

    public static boolean isModLoaded(final String id){
        return ModList.get().isLoaded(id);
    }

    public static boolean isWearingAccessoryItem(final LivingEntity player, final Item medallion) {
        if(TCOTS_Util.isAccessoriesLoaded())
            return TCOTS_AccessoriesCompat.isWearingAccessoryItem(player, medallion);
        else
            return false;
    }
}

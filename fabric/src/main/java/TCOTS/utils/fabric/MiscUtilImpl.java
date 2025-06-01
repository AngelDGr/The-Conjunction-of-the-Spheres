package TCOTS.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;

@SuppressWarnings("unused")
public class MiscUtilImpl {

    public static boolean isWitcherRPGLoaded() {
        return FabricLoader.getInstance().isModLoaded("witcher_rpg");
    }
}

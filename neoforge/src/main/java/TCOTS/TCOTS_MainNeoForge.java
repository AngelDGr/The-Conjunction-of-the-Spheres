package TCOTS;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = TCOTS_Main.MOD_ID)
public class TCOTS_MainNeoForge {

    public TCOTS_MainNeoForge(IEventBus eventBus) {
        TCOTS_Main.init();
        eventBus.addListener(this::clientSetup);
    }

    private void  clientSetup(final FMLClientSetupEvent evt) {

    }
}

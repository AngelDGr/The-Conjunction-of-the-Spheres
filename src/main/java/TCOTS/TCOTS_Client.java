package TCOTS;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.geo.renderer.necrophages.DrownerRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TCOTS_Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(TCOTS_Entities.DROWNER, DrownerRenderer::new);
    }
}

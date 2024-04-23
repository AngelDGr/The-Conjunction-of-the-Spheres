package TCOTS.screen;

import TCOTS.TCOTS_Main;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class ToxicityHudOverlay implements HudRenderCallback {

    private final Identifier TOXICITY_BAR = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/toxicity_bar.png");

    int x = 0;
    int y = 0;
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height / 2;
        }


//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, TOXICITY_BAR);
        drawContext.drawGuiTexture(TOXICITY_BAR, x, y,116,10);

//        RenderSystem.setShaderTexture(0, FILLED_THIRST);
//        for(int i = 0; i < 10; i++) {
//            if(((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getInt("thirst") > i) {
//                DrawableHelper.drawTexture(matrixStack,x - 94 + (i * 9),y - 54,0,0,12,12,
//                        12,12);
//            } else {
//                break;
//            }
//        }
    }
}

package TCOTS.screen;

import TCOTS.TCOTS_Main;
import TCOTS.config.TCOTS_OwOConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ToxicityHudOverlay implements HudRenderCallback {
    private final Identifier TOXICITY_BAR_SPRITE = new Identifier(TCOTS_Main.MOD_ID, "hud/toxicity_bar");

    //TODO: Fix chat transparency

    int x = 0;
    int y = 0;

    private int getXAnchor(int scaledWith, TCOTS_OwOConfig.ANCHORS anchor ){
        return switch (anchor){
            case CENTER_DOWN,CENTER_UP -> scaledWith/2 - 61;
            case RIGHT_DOWN, RIGHT_UP -> scaledWith - 120;
            case LEFT_UP,LEFT_DOWN -> 4;
        };
    }

    private int getYAnchor(int scaledHeight, TCOTS_OwOConfig.ANCHORS anchor){
        return switch (anchor){
            case CENTER_DOWN -> scaledHeight-71;
            case CENTER_UP -> 22;

            case RIGHT_UP, LEFT_UP -> 4;
            case LEFT_DOWN,RIGHT_DOWN -> scaledHeight-12;
        };
    }

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {

        MinecraftClient client = MinecraftClient.getInstance();

        if (client != null) {
            int scaledWidth = context.getScaledWindowWidth();
            int scaledHeight = context.getScaledWindowHeight();

            x = (getXAnchor(scaledWidth,TCOTS_Main.CONFIG.hud.anchor())) + TCOTS_Main.CONFIG.hud.Hud_X();
            y = (getYAnchor(scaledHeight,TCOTS_Main.CONFIG.hud.anchor())) + TCOTS_Main.CONFIG.hud.Hud_Y();

            if(client.player!=null){

                if(client.player.theConjunctionOfTheSpheres$getHudActive()) {
//                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    context.getMatrices().push();
                    context.getMatrices().translate(0.0f, 0.0f, 20);

                    context.setShaderColor(1,1,1,client.player.theConjunctionOfTheSpheres$getHudTransparency());
                    context.drawGuiTexture(TOXICITY_BAR_SPRITE, x, y, 116, 10);
                    context.setShaderColor(1,1,1,1);

                    context.getMatrices().pop();
                    RenderSystem.disableBlend();
//                    RenderSystem.enableDepthTest();
                }
            }
        }


    }
}

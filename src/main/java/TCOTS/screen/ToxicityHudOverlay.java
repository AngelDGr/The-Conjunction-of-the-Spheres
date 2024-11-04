package TCOTS.screen;

import TCOTS.TCOTS_Main;
import TCOTS.config.TCOTS_OwOConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

@SuppressWarnings("unused")
public class ToxicityHudOverlay {
    private static final Identifier TOXICITY_BAR_EMPTY = new Identifier(TCOTS_Main.MOD_ID, "hud/toxicity_bar");
    private static final Identifier TOXICITY_BAR_FULL = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/hud/toxicity_bar_filled.png");
    private static final Identifier TOXICITY_BAR_FULL_DECOCTION = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/hud/toxicity_bar_filled_decoction.png");
    private static final Identifier TOXICITY_OVERLAY = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/hud/toxicity_overlay.png");


    //xTODO: Fix chat transparency
    //xTODO: Change the toxicity overlay texture

    private static float transparency;

    private static int timerHud;

    private static boolean activeHud=false;

    private static int getXAnchor(int scaledWith, TCOTS_OwOConfig.ANCHORS anchor){
        return switch (anchor){
            case CENTER_DOWN,CENTER_UP -> scaledWith/2 - 61;
            case RIGHT_DOWN, RIGHT_UP -> scaledWith - 120;
            case LEFT_UP,LEFT_DOWN -> 4;
        };
    }

    private static int getYAnchor(int scaledHeight, TCOTS_OwOConfig.ANCHORS anchor){
        return switch (anchor){
            case CENTER_DOWN -> scaledHeight-71;
            case CENTER_UP -> 22;

            case RIGHT_UP, LEFT_UP -> 4;
            case LEFT_DOWN,RIGHT_DOWN -> scaledHeight-12;
        };
    }

    public static void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client != null) {
            int scaledWidth = context.getScaledWindowWidth();
            int scaledHeight = context.getScaledWindowHeight();

            int x = (getXAnchor(scaledWidth, TCOTS_Main.CONFIG.hud.anchor())) + TCOTS_Main.CONFIG.hud.Hud_X();
            int y = (getYAnchor(scaledHeight, TCOTS_Main.CONFIG.hud.anchor())) + TCOTS_Main.CONFIG.hud.Hud_Y();

            if(client.player!=null){
                PlayerEntity player=client.player;

                //For fade out
                if(player.theConjunctionOfTheSpheres$getAllToxicity()>0){
                    activeHud=true;
                    transparency=1;
                    timerHud=0;
                }
                else if(player.theConjunctionOfTheSpheres$getAllToxicity()==0){
                    if(timerHud<20){
                        transparency=transparency-0.05f;
                        ++timerHud;
                    } else {
                        activeHud=false;
                    }
                }

                if(activeHud) {
                    int allToxicity=player.theConjunctionOfTheSpheres$getAllToxicity();
                    int maxToxicity=player.theConjunctionOfTheSpheres$getMaxToxicity();
                    RenderSystem.enableBlend();

                    context.setShaderColor(1,1,1, transparency);
                    //EmptyBar
                    context.drawGuiTexture(TOXICITY_BAR_EMPTY, x, y, 116, 10);
                    //Normal Toxicity Bar
                    context.drawTexture(TOXICITY_BAR_FULL, x +10, y,
                            10.0f, 0.0f,
                            (int)(106*((float)allToxicity/(float)maxToxicity)), 10,
                            116,10);

                    //Decoction Toxicity Bar
                    context.drawTexture(TOXICITY_BAR_FULL_DECOCTION, x +10, y,
                            10.0f, 0.0f,
                            (int)(106*((float)player.theConjunctionOfTheSpheres$getDecoctionToxicity()/(float)maxToxicity)), 10,
                            116,10);

                    context.setShaderColor(1,1,1,1);

                    //Toxicity Overlay
                    float toxicityThreshold= maxToxicity*0.5f;
                    if(allToxicity>=toxicityThreshold){
                        context.setShaderColor(1,1,1,(allToxicity-toxicityThreshold)/toxicityThreshold);
                        context.drawTexture(TOXICITY_OVERLAY, 0, 0, -90, 0.0f, 0.0f, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
                        context.setShaderColor(1,1,1,1);
                    }

                    RenderSystem.disableBlend();
                }
            }
        }
    }
}

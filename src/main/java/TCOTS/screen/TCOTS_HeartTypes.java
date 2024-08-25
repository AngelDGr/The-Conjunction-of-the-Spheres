package TCOTS.screen;

import TCOTS.TCOTS_Main;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public enum TCOTS_HeartTypes {
    TOXIC(new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_full"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_full_blinking"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_half"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_half_blinking"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_full"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_full_blinking"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_half"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_half_blinking")),

    CADAVERINE(new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_full"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_full_blinking"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_half"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_half_blinking"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_full"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_full_blinking"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_half"),
            new Identifier(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_half_blinking"));

    private final Identifier fullTexture;
    private final Identifier fullBlinkingTexture;
    private final Identifier halfTexture;
    private final Identifier halfBlinkingTexture;
    private final Identifier hardcoreFullTexture;
    private final Identifier hardcoreFullBlinkingTexture;
    private final Identifier hardcoreHalfTexture;
    private final Identifier hardcoreHalfBlinkingTexture;

    TCOTS_HeartTypes(Identifier fullTexture, Identifier fullBlinkingTexture, Identifier halfTexture, Identifier halfBlinkingTexture, Identifier hardcoreFullTexture, Identifier hardcoreFullBlinkingTexture, Identifier hardcoreHalfTexture, Identifier hardcoreHalfBlinkingTexture) {
        this.fullTexture = fullTexture;
        this.fullBlinkingTexture = fullBlinkingTexture;
        this.halfTexture = halfTexture;
        this.halfBlinkingTexture = halfBlinkingTexture;
        this.hardcoreFullTexture = hardcoreFullTexture;
        this.hardcoreFullBlinkingTexture = hardcoreFullBlinkingTexture;
        this.hardcoreHalfTexture = hardcoreHalfTexture;
        this.hardcoreHalfBlinkingTexture = hardcoreHalfBlinkingTexture;
    }

    public Identifier getTexture(boolean hardcore, boolean half, boolean blinking) {
        if (!hardcore) {
            if (half) {
                return blinking ? this.halfBlinkingTexture : this.halfTexture;
            }
            return blinking ? this.fullBlinkingTexture : this.fullTexture;
        }
        if (half) {
            return blinking ? this.hardcoreHalfBlinkingTexture : this.hardcoreHalfTexture;
        }
        return blinking ? this.hardcoreFullBlinkingTexture : this.hardcoreFullTexture;
    }

}
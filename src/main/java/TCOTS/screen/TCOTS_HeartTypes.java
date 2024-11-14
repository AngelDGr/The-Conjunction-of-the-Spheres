package TCOTS.screen;

import TCOTS.TCOTS_Main;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public enum TCOTS_HeartTypes {
    TOXIC(Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_full"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_full_blinking"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_half"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_half_blinking"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_full"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_full_blinking"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_half"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_half_blinking")),

    CADAVERINE(Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_full"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_full_blinking"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_half"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_half_blinking"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_full"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_full_blinking"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_half"),
            Identifier.of(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_half_blinking"));

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
package mors.tcots.client.screen;

import mors.tcots.TCOTS_Main;
import net.minecraft.resources.ResourceLocation;


public enum TCOTS_HeartTypes {
    TOXIC(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_full"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_half"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_half_blinking"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_full"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_half"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/toxic_hardcore_half_blinking")),

    CADAVERINE(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_full"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_half"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_half_blinking"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_full"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_full_blinking"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_half"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"hud/heart/cadaverine_hardcore_half_blinking"));

    private final ResourceLocation fullTexture;
    private final ResourceLocation fullBlinkingTexture;
    private final ResourceLocation halfTexture;
    private final ResourceLocation halfBlinkingTexture;
    private final ResourceLocation hardcoreFullTexture;
    private final ResourceLocation hardcoreFullBlinkingTexture;
    private final ResourceLocation hardcoreHalfTexture;
    private final ResourceLocation hardcoreHalfBlinkingTexture;

    TCOTS_HeartTypes(final ResourceLocation fullTexture, final ResourceLocation fullBlinkingTexture, final ResourceLocation halfTexture, final ResourceLocation halfBlinkingTexture, final ResourceLocation hardcoreFullTexture, final ResourceLocation hardcoreFullBlinkingTexture, final ResourceLocation hardcoreHalfTexture, final ResourceLocation hardcoreHalfBlinkingTexture) {
        this.fullTexture = fullTexture;
        this.fullBlinkingTexture = fullBlinkingTexture;
        this.halfTexture = halfTexture;
        this.halfBlinkingTexture = halfBlinkingTexture;
        this.hardcoreFullTexture = hardcoreFullTexture;
        this.hardcoreFullBlinkingTexture = hardcoreFullBlinkingTexture;
        this.hardcoreHalfTexture = hardcoreHalfTexture;
        this.hardcoreHalfBlinkingTexture = hardcoreHalfBlinkingTexture;
    }

    public ResourceLocation getTexture(final boolean hardcore, final boolean half, final boolean blinking) {
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
package mors.tcots.effects.potions;

import mors.tcots.TCOTS_Main;
import mors.tcots.effects.WitcherPotionEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;

public class CatEffect extends WitcherPotionEffect {
    public static ResourceLocation CatShader = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "shaders/cat_shader.json");

    public CatEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

}

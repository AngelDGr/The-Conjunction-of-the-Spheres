package TCOTS.effects.potions;

import TCOTS.TCOTS_Main;
import TCOTS.effects.WitcherPotionEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;

public class CatEffect extends WitcherPotionEffect {
    public static ResourceLocation CatShader = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "shaders/cat_shader.json");

    public CatEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

}

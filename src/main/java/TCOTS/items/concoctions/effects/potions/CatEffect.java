package TCOTS.items.concoctions.effects.potions;

import TCOTS.TCOTS_Main;
import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class CatEffect extends WitcherPotionEffect {
    public static Identifier CatShader = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader.json");

    public CatEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }


}

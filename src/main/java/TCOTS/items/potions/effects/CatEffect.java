package TCOTS.items.potions.effects;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class CatEffect extends WitcherEffect {
    public static Identifier CatShader = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader.json");


    public CatEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
    }
}

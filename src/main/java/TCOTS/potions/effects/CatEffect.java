package TCOTS.potions.effects;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class CatEffect extends StatusEffect {
    public static Identifier CatShader_00 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_00.json");
    public static Identifier CatShader_01 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_01.json");
    public static Identifier CatShader_02 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_02.json");
    public static Identifier CatShader_03 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_03.json");
    public static Identifier CatShader_04 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_04.json");
    public static Identifier CatShader_05 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_05.json");
    public static Identifier CatShader_06 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_06.json");
    public static Identifier CatShader_07 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_07.json");
    public static Identifier CatShader_08 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_08.json");
    public static Identifier CatShader_09 = new Identifier(TCOTS_Main.MOD_ID, "shaders/cat_shader_09.json");


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

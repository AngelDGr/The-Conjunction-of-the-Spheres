package TCOTS.potions;

import TCOTS.TCOTS_Main;
import TCOTS.potions.effects.CatEffect;
import TCOTS.potions.effects.KillerWhaleEffect;
import TCOTS.potions.effects.SwallowEffect;
import TCOTS.potions.effects.WhiteRaffardsEffect;
import TCOTS.potions.effects.decoctions.FogletDecoctionEffect;
import TCOTS.potions.effects.decoctions.GraveHagDecoctionEffect;
import TCOTS.potions.effects.decoctions.WaterHagDecoctionEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Effects {
    public static StatusEffect KILLER_WHALE_EFFECT;
    public static StatusEffect SWALLOW_EFFECT;
    public static StatusEffect WHITE_RAFFARDS_EFFECT;
    public static StatusEffect CAT_EFFECT;

    public static StatusEffect GRAVE_HAG_DECOCTION_EFFECT;
    public static StatusEffect WATER_HAG_DECOCTION_EFFECT;
    public static StatusEffect FOGLET_DECOCTION_EFFECT;

    static final int decoctionColor=0x0b7000;

    public static void registerEffects() {

        //Effects
        KILLER_WHALE_EFFECT=registerStatusEffect_Modifier("killer_whale", KillerWhaleEffect.class, StatusEffectCategory.BENEFICIAL, 0xe9b044,1);

        SWALLOW_EFFECT = registerStatusEffect("swallow", SwallowEffect.class, StatusEffectCategory.BENEFICIAL, 0xcc624a);

        WHITE_RAFFARDS_EFFECT = registerStatusEffect("white_raffards", WhiteRaffardsEffect.class, StatusEffectCategory.BENEFICIAL, 0xb4b093);

        CAT_EFFECT = registerStatusEffect("cat_effect", CatEffect.class, StatusEffectCategory.BENEFICIAL, 0x595959);

        GRAVE_HAG_DECOCTION_EFFECT = registerStatusEffect("grave_hag_decoction", GraveHagDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor);

        WATER_HAG_DECOCTION_EFFECT = registerStatusEffect_Modifier("water_hag_decoction", WaterHagDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor,1);

        FOGLET_DECOCTION_EFFECT = registerStatusEffect("foglet_decoction", FogletDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor);
    }

    public static StatusEffect registerStatusEffect(String name, Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color) {
        try {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name),
                    effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color));
        } catch (Exception e) {
            throw new IllegalArgumentException("Effect didn't create");
        }
    }

    public static StatusEffect registerStatusEffect_Modifier(String name, Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color, double modifier) {
        try {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name),
                    effectClass.getConstructor(StatusEffectCategory.class, int.class, double.class).newInstance(category, color, modifier));
        } catch (Exception e) {
            throw new IllegalArgumentException("Effect didn't create");
        }
    }

}
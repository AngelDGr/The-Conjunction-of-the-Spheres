package TCOTS.potions;

import TCOTS.TCOTS_Main;
import TCOTS.potions.effects.KillerWhaleEffect;
import TCOTS.potions.effects.SwallowEffect;
import TCOTS.potions.effects.WhiteRaffardsEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Effects {
    public static StatusEffect KILLER_WHALE_EFFECT;

    public static StatusEffect SWALLOW_EFFECT;

    public static StatusEffect WHITE_RAFFARDS_EFFECT;

    public static void registerEffects() {
        //Effects

        KILLER_WHALE_EFFECT=registerStatusEffect_Modifier("killer_whale", KillerWhaleEffect.class, StatusEffectCategory.BENEFICIAL, 0xe9b044,1);

        SWALLOW_EFFECT = registerStatusEffect("swallow", SwallowEffect.class, StatusEffectCategory.BENEFICIAL, 0xcc624a);

        WHITE_RAFFARDS_EFFECT = registerStatusEffect("white_raffards", WhiteRaffardsEffect.class, StatusEffectCategory.BENEFICIAL, 0xb4b093);

    }

    public static StatusEffect registerStatusEffect(String name, Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color) {
        try {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name),
                    effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color));
        } catch (Exception e) {
            System.out.println("EFFECT DON'T CREATED");
            e.printStackTrace();
            return null;
        }
    }

    public static StatusEffect registerStatusEffect_Modifier(String name, Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color, double modifier) {
        try {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name),
                    effectClass.getConstructor(StatusEffectCategory.class, int.class, double.class).newInstance(category, color, modifier));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static StatusEffect register(String id, StatusEffect entry) {
        return (StatusEffect)Registry.register(Registries.STATUS_EFFECT, id, entry);
    }
}

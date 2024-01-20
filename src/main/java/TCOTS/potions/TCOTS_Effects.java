package TCOTS.potions;

import TCOTS.TCOTS_Main;
import TCOTS.potions.effects.KillerWhaleEffect;
import TCOTS.potions.effects.SwallowEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Effects {
    public static StatusEffect KILLER_WHALE_EFFECT;

    public static StatusEffect SWALLOW_EFFECT;

    public static StatusEffect registerStatusEffect(String name, Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color) {
        try {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name),
                    effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerEffects() {
        //Effects
        KILLER_WHALE_EFFECT=registerStatusEffect("killer_whale", KillerWhaleEffect.class, StatusEffectCategory.BENEFICIAL, 0xe9b044);

        SWALLOW_EFFECT=registerStatusEffect("swallow", SwallowEffect.class, StatusEffectCategory.BENEFICIAL, 0xcc624a);

    }
}

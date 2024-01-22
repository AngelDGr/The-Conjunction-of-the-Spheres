package TCOTS.potions;

import TCOTS.TCOTS_Main;
import TCOTS.potions.effects.KillerWhaleEffect;
import TCOTS.potions.effects.SwallowEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.DamageModifierStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Effects {
    public static StatusEffect KILLER_WHALE_EFFECT;

    public static StatusEffect SWALLOW_EFFECT;

    public static void registerEffects() {
        //Effects

//        KILLER_WHALE_EFFECT = register( "killer_whale", (new
//                KillerWhaleEffect(StatusEffectCategory.BENEFICIAL, 0xe9b044, 2.0))
//                .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 0.0, EntityAttributeModifier.Operation.ADDITION));


        KILLER_WHALE_EFFECT=registerStatusEffect_Modifier("killer_whale", KillerWhaleEffect.class, StatusEffectCategory.BENEFICIAL, 0xe9b044,1);

        SWALLOW_EFFECT = registerStatusEffect("swallow", SwallowEffect.class, StatusEffectCategory.BENEFICIAL, 0xcc624a);

    }

    public static StatusEffect registerStatusEffect(String name, Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color) {
        try {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name),
                    effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color));
        } catch (Exception e) {
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

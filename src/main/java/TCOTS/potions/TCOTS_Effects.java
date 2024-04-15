package TCOTS.potions;

import TCOTS.TCOTS_Main;
import TCOTS.potions.effects.*;
import TCOTS.potions.effects.decoctions.FogletDecoctionEffect;
import TCOTS.potions.effects.decoctions.GraveHagDecoctionEffect;
import TCOTS.potions.effects.decoctions.WaterHagDecoctionEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
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
    public static StatusEffect BLACK_BLOOD_EFFECT;
    public static StatusEffect BLEEDING_BLACK_BLOOD_EFFECT;

    public static StatusEffect GRAVE_HAG_DECOCTION_EFFECT;
    public static StatusEffect WATER_HAG_DECOCTION_EFFECT;
    public static StatusEffect FOGLET_DECOCTION_EFFECT;

    static final int decoctionColor=0x0b7000;

    public static void registerEffects() {

        //Effects

        KILLER_WHALE_EFFECT = registerEffect("killer_whale",
                new KillerWhaleEffect(StatusEffectCategory.BENEFICIAL, 0xe9b044)
                .addAttributeModifier(
                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                        "3F9D6A72-8C4E-4B17-A8ED-5D2C1F8B913D", 4.0,
                        EntityAttributeModifier.Operation.ADDITION));

        SWALLOW_EFFECT = registerStatusEffect("swallow", SwallowEffect.class, StatusEffectCategory.BENEFICIAL, 0xcc624a);

        WHITE_RAFFARDS_EFFECT = registerStatusEffect("white_raffards", WhiteRaffardsEffect.class, StatusEffectCategory.BENEFICIAL, 0xb4b093);

        CAT_EFFECT = registerStatusEffect("cat_effect", CatEffect.class, StatusEffectCategory.BENEFICIAL, 0x595959);

        BLACK_BLOOD_EFFECT = registerStatusEffect("black_blood", BlackBloodEffect.class, StatusEffectCategory.BENEFICIAL, 0x272727);
        BLEEDING_BLACK_BLOOD_EFFECT = registerStatusEffect("bleeding_black_blood", BleedingBlackBloodEffect.class, StatusEffectCategory.HARMFUL, 0x272727);

        GRAVE_HAG_DECOCTION_EFFECT = registerStatusEffect("grave_hag_decoction", GraveHagDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor);

        WATER_HAG_DECOCTION_EFFECT = registerEffect("water_hag_decoction",
                new WaterHagDecoctionEffect(StatusEffectCategory.BENEFICIAL, decoctionColor)
                        .addAttributeModifier(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                "7B3F8E12-9A6D-4C5F-B2A9-1E7C9D4A6B8F", 5.0,
                                EntityAttributeModifier.Operation.ADDITION));

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

    private static StatusEffect registerEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name), statusEffect);
    }
}
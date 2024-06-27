package TCOTS.items.potions;

import TCOTS.TCOTS_Main;
import TCOTS.items.potions.effects.*;
import TCOTS.items.potions.effects.bombs.DimeritiumBombEffect;
import TCOTS.items.potions.effects.bombs.MoonDustEffect;
import TCOTS.items.potions.effects.bombs.NorthernWindEffect;
import TCOTS.items.potions.effects.bombs.SamumEffect;
import TCOTS.items.potions.effects.decoctions.AlghoulDecoctionEffect;
import TCOTS.items.potions.effects.decoctions.FogletDecoctionEffect;
import TCOTS.items.potions.effects.decoctions.GraveHagDecoctionEffect;
import TCOTS.items.potions.effects.decoctions.WaterHagDecoctionEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Effects {

    //W1
    public static StatusEffect WOLF_EFFECT;

    //W2
    public static StatusEffect ROOK_EFFECT;


    //W3
    public static StatusEffect KILLER_WHALE_EFFECT;
    public static StatusEffect SWALLOW_EFFECT;
    public static StatusEffect WHITE_RAFFARDS_EFFECT;
    public static StatusEffect CAT_EFFECT;
    public static StatusEffect BLACK_BLOOD_EFFECT;
    public static StatusEffect BLEEDING_BLACK_BLOOD_EFFECT;
    public static StatusEffect MARIBOR_FOREST_EFFECT;

        //Decoctions
        public static StatusEffect GRAVE_HAG_DECOCTION_EFFECT;
        public static StatusEffect WATER_HAG_DECOCTION_EFFECT;
        public static StatusEffect FOGLET_DECOCTION_EFFECT;
        public static StatusEffect ALGHOUL_DECOCTION_EFFECT;

    static final int decoctionColor=0x0b7000;

    //Bombs
    public static StatusEffect SAMUM_EFFECT;
    public static StatusEffect NORTHERN_WIND_EFFECT;
    public static StatusEffect DIMERITIUM_BOMB_EFFECT;
    public static StatusEffect MOON_DUST_EFFECT;

    public static StatusEffect BLEEDING;

    public static void registerEffects() {

        //Effects
        //W1
        WOLF_EFFECT = registerStatusEffect("wolf_effect", WolfEffect.class, StatusEffectCategory.BENEFICIAL, 0xdd531d);

        //W2
        ROOK_EFFECT = registerStatusEffect("rook_effect", RookEffect.class, StatusEffectCategory.BENEFICIAL, 0x268e26);


        // W3
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

        MARIBOR_FOREST_EFFECT = registerStatusEffect("maribor_forest", MariborForestEffect.class, StatusEffectCategory.BENEFICIAL, 0xb9b9b9);



        //Decoctions
        GRAVE_HAG_DECOCTION_EFFECT = registerStatusEffect("grave_hag_decoction", GraveHagDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor);

        WATER_HAG_DECOCTION_EFFECT = registerEffect("water_hag_decoction",
                new WaterHagDecoctionEffect(StatusEffectCategory.BENEFICIAL, decoctionColor)
                        .addAttributeModifier(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                "7B3F8E12-9A6D-4C5F-B2A9-1E7C9D4A6B8F", 5.0,
                                EntityAttributeModifier.Operation.ADDITION));

        ALGHOUL_DECOCTION_EFFECT = registerStatusEffect("alghoul_decoction", AlghoulDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor);


        FOGLET_DECOCTION_EFFECT = registerStatusEffect("foglet_decoction", FogletDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor);


        //Bomb Effects
        SAMUM_EFFECT = registerEffect("samum",
                new SamumEffect(StatusEffectCategory.HARMFUL, 0x6c777b)
                        .addAttributeModifier(
                                EntityAttributes.GENERIC_MOVEMENT_SPEED,
                                "7107DE5E-7CE8-4030-940E-514C1F160890", -1.0f,
                                EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

        NORTHERN_WIND_EFFECT = registerEffect("northern_wind",
                new NorthernWindEffect(StatusEffectCategory.HARMFUL, 0x007b77)
                        .addAttributeModifier(
                                EntityAttributes.GENERIC_MOVEMENT_SPEED,
                                "7107DE5E-7CE8-4030-940E-514C1F160890", -1.0f,
                                EntityAttributeModifier.Operation.MULTIPLY_TOTAL));

        DIMERITIUM_BOMB_EFFECT = registerStatusEffect("dimeritium_bomb", DimeritiumBombEffect.class, StatusEffectCategory.HARMFUL, 0x25882f);

        MOON_DUST_EFFECT = registerStatusEffect("moon_dust", MoonDustEffect.class, StatusEffectCategory.HARMFUL, 0x87b8b8);

        BLEEDING = registerStatusEffect("bleeding", BleedingEffect.class, StatusEffectCategory.HARMFUL, 0xab0000);

    }

    public static StatusEffect registerStatusEffect(String name, Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color) {
        try {
            return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name),
                    effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color));
        } catch (Exception e) {
            throw new IllegalArgumentException("The effect was not created");
        }
    }

    private static StatusEffect registerEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(TCOTS_Main.MOD_ID, name), statusEffect);
    }
}
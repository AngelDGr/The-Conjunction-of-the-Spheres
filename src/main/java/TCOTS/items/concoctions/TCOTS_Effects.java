package TCOTS.items.concoctions;

import TCOTS.TCOTS_Main;
import TCOTS.items.concoctions.effects.*;
import TCOTS.items.concoctions.effects.bombs.DimeritiumBombEffect;
import TCOTS.items.concoctions.effects.bombs.MoonDustEffect;
import TCOTS.items.concoctions.effects.bombs.NorthernWindEffect;
import TCOTS.items.concoctions.effects.bombs.SamumEffect;
import TCOTS.items.concoctions.effects.decoctions.*;
import TCOTS.items.concoctions.effects.potions.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TCOTS_Effects {

    //W1
    public static RegistryEntry<StatusEffect> WOLF_EFFECT;

    //W2
    public static RegistryEntry<StatusEffect>  ROOK_EFFECT;

    //W3
    public static RegistryEntry<StatusEffect>  KILLER_WHALE_EFFECT;
    public static RegistryEntry<StatusEffect>  SWALLOW_EFFECT;
    public static RegistryEntry<StatusEffect>  WHITE_RAFFARDS_EFFECT;
    public static RegistryEntry<StatusEffect>  CAT_EFFECT;
    public static RegistryEntry<StatusEffect>  BLACK_BLOOD_EFFECT;
    public static RegistryEntry<StatusEffect>  BLEEDING_BLACK_BLOOD_EFFECT;
    public static RegistryEntry<StatusEffect>  MARIBOR_FOREST_EFFECT;

        //Decoctions
        public static RegistryEntry<StatusEffect>  GRAVE_HAG_DECOCTION_EFFECT;
        public static RegistryEntry<StatusEffect>  WATER_HAG_DECOCTION_EFFECT;
        public static RegistryEntry<StatusEffect>  FOGLET_DECOCTION_EFFECT;
        public static RegistryEntry<StatusEffect>  ALGHOUL_DECOCTION_EFFECT;
        public static RegistryEntry<StatusEffect>  NEKKER_WARRIOR_DECOCTION_EFFECT;
        public static RegistryEntry<StatusEffect>  TROLL_DECOCTION_EFFECT;

    static final int decoctionColor=0x0b7000;

    //Bombs
    public static RegistryEntry<StatusEffect>  SAMUM_EFFECT;
    public static RegistryEntry<StatusEffect>  NORTHERN_WIND_EFFECT;
    public static RegistryEntry<StatusEffect>  DIMERITIUM_BOMB_EFFECT;
    public static RegistryEntry<StatusEffect>  MOON_DUST_EFFECT;

    //Misc
    public static RegistryEntry<StatusEffect>  BLEEDING;
    public static RegistryEntry<StatusEffect>  CADAVERINE;

    public static void registerEffects() {

        //Effects
        //Potions
        {
            //W1
            WOLF_EFFECT = registerRegistryKey("wolf_effect",
                    registerStatusEffect(WolfEffect.class, StatusEffectCategory.BENEFICIAL, 0xdd531d));

            //W2
            ROOK_EFFECT = registerRegistryKey("rook_effect",
                    registerStatusEffect(RookEffect.class, StatusEffectCategory.BENEFICIAL, 0x268e26));


            // W3
            KILLER_WHALE_EFFECT = registerRegistryKey("killer_whale",
                    new KillerWhaleEffect(StatusEffectCategory.BENEFICIAL, 0xe9b044)
                            .addAttributeModifier(
                                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                    Identifier.of(TCOTS_Main.MOD_ID, "killer_whale_attack"),
                                    4.0,
                                    EntityAttributeModifier.Operation.ADD_VALUE));

            SWALLOW_EFFECT = registerRegistryKey("swallow",
                    registerStatusEffect(SwallowEffect.class, StatusEffectCategory.BENEFICIAL, 0xcc624a));

            WHITE_RAFFARDS_EFFECT = registerRegistryKey("white_raffards",
                    registerStatusEffect(WhiteRaffardsEffect.class, StatusEffectCategory.BENEFICIAL, 0xb4b093));

            CAT_EFFECT = registerRegistryKey("cat_effect",
                    registerStatusEffect(CatEffect.class, StatusEffectCategory.BENEFICIAL, 0x595959));

            BLACK_BLOOD_EFFECT = registerRegistryKey("black_blood",
                    registerStatusEffect(BlackBloodEffect.class, StatusEffectCategory.BENEFICIAL, 0x272727));
            BLEEDING_BLACK_BLOOD_EFFECT = registerRegistryKey("bleeding_black_blood",
                    registerStatusEffect(BleedingBlackBloodEffect.class, StatusEffectCategory.HARMFUL, 0x272727));

            MARIBOR_FOREST_EFFECT = registerRegistryKey("maribor_forest",
                    registerStatusEffect(MariborForestEffect.class, StatusEffectCategory.BENEFICIAL, 0xb9b9b9)) ;

            //Decoctions
            {
                GRAVE_HAG_DECOCTION_EFFECT = registerRegistryKey("grave_hag_decoction",
                        registerStatusEffect(GraveHagDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor));

                WATER_HAG_DECOCTION_EFFECT = registerRegistryKey("water_hag_decoction",
                        new WaterHagDecoctionEffect(StatusEffectCategory.BENEFICIAL, decoctionColor)
                        .addAttributeModifier(
                                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                Identifier.of(TCOTS_Main.MOD_ID, "water_hag_decoction_attack"),
                                5.0,
                                EntityAttributeModifier.Operation.ADD_VALUE));

                ALGHOUL_DECOCTION_EFFECT = registerRegistryKey("alghoul_decoction",
                        registerStatusEffect(AlghoulDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor));

                FOGLET_DECOCTION_EFFECT = registerRegistryKey("foglet_decoction",
                        registerStatusEffect(FogletDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor)) ;

                NEKKER_WARRIOR_DECOCTION_EFFECT = registerRegistryKey("nekker_warrior_decoction",
                        new NekkerWarriorDecoctionEffect(StatusEffectCategory.BENEFICIAL, decoctionColor)
                                .addAttributeModifier(
                                        EntityAttributes.GENERIC_ATTACK_DAMAGE,
                                        Identifier.of(TCOTS_Main.MOD_ID, "nekker_warrior_decoction_attack"),
                                        4.0,
                                        EntityAttributeModifier.Operation.ADD_VALUE));

                TROLL_DECOCTION_EFFECT = registerRegistryKey("troll_decoction",
                        registerStatusEffect(TrollDecoctionEffect.class, StatusEffectCategory.BENEFICIAL, decoctionColor));

            }
        }

        //Bomb Effects
        {
            SAMUM_EFFECT = registerRegistryKey("samum",
                    new SamumEffect(StatusEffectCategory.HARMFUL, 0x6c777b)
                    .addAttributeModifier(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED,
                            Identifier.of(TCOTS_Main.MOD_ID, "samum_slowness"), -1.0f,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
                    ;

            NORTHERN_WIND_EFFECT = registerRegistryKey("northern_wind",
                    new NorthernWindEffect(StatusEffectCategory.HARMFUL, 0x007b77)
                            .addAttributeModifier(
                                    EntityAttributes.GENERIC_MOVEMENT_SPEED,
                                    Identifier.of(TCOTS_Main.MOD_ID, "northern_wind_slowness"), -1.0f,
                                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

            DIMERITIUM_BOMB_EFFECT = registerRegistryKey("dimeritium_bomb",
                    registerStatusEffect(DimeritiumBombEffect.class, StatusEffectCategory.HARMFUL, 0x25882f));

            MOON_DUST_EFFECT = registerRegistryKey("moon_dust",
                    registerStatusEffect(MoonDustEffect.class, StatusEffectCategory.HARMFUL, 0x87b8b8));
        }

        //Misc Effects
        {
            BLEEDING = registerRegistryKey("bleeding",
                    registerStatusEffect(BleedingEffect.class, StatusEffectCategory.HARMFUL, 0xab0000));

            CADAVERINE = registerRegistryKey("cadaverine",
                    new CadaverineEffect(StatusEffectCategory.HARMFUL,  0x00bd13)
                    .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE,
                            Identifier.of(TCOTS_Main.MOD_ID, "weakness_cadaverine"),
                            -2.0F,
                            EntityAttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                            Identifier.of(TCOTS_Main.MOD_ID, "slowness_cadaverine"),
                            -0.15F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }

    }

    public static StatusEffect registerStatusEffect(Class<? extends StatusEffect> effectClass, StatusEffectCategory category, int color) {
        try {
//            return Registry.register(Registries.STATUS_EFFECT, Identifier.of(TCOTS_Main.MOD_ID, name),
//                    effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color));
            return  effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color);
        } catch (Exception e) {
            throw new IllegalArgumentException("The effect was not created");
        }
    }

    private static StatusEffect registerEffect(String name, StatusEffect statusEffect) {
        return Registry.register(Registries.STATUS_EFFECT, Identifier.of(TCOTS_Main.MOD_ID, name), statusEffect);
    }

    private static RegistryEntry<StatusEffect> registerRegistryKey(String id, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(TCOTS_Main.MOD_ID, id), statusEffect);
    }
}
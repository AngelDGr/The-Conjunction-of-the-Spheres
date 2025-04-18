package TCOTS.items.concoctions;

import TCOTS.TCOTS_Main;
import TCOTS.effects.BleedingBlackBloodEffect;
import TCOTS.effects.BleedingEffect;
import TCOTS.effects.CadaverineEffect;
import TCOTS.effects.potions.*;
import TCOTS.effects.bombs.DimeritiumBombEffect;
import TCOTS.items.concoctions.effects.bombs.MoonDustEffect;
import TCOTS.effects.bombs.NorthernWindEffect;
import TCOTS.effects.bombs.SamumEffect;
import TCOTS.items.concoctions.effects.decoctions.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class TCOTS_Effects {

    //W1
    public static Holder<MobEffect> WOLF_EFFECT;

    //W2
    public static Holder<MobEffect>  ROOK_EFFECT;

    //W3
    public static Holder<MobEffect>  KILLER_WHALE_EFFECT;
    public static Holder<MobEffect>  SWALLOW_EFFECT;
    public static Holder<MobEffect>  WHITE_RAFFARDS_EFFECT;
    public static Holder<MobEffect>  CAT_EFFECT;
    public static Holder<MobEffect>  BLACK_BLOOD_EFFECT;
    public static Holder<MobEffect>  BLEEDING_BLACK_BLOOD_EFFECT;
    public static Holder<MobEffect>  MARIBOR_FOREST_EFFECT;

        //Decoctions
        public static Holder<MobEffect>  GRAVE_HAG_DECOCTION_EFFECT;
        public static Holder<MobEffect>  WATER_HAG_DECOCTION_EFFECT;
        public static Holder<MobEffect>  FOGLET_DECOCTION_EFFECT;
        public static Holder<MobEffect>  ALGHOUL_DECOCTION_EFFECT;
        public static Holder<MobEffect>  NEKKER_WARRIOR_DECOCTION_EFFECT;
        public static Holder<MobEffect>  TROLL_DECOCTION_EFFECT;

    static final int decoctionColor=0x0b7000;

    //Bombs
    public static Holder<MobEffect>  SAMUM_EFFECT;
    public static Holder<MobEffect>  NORTHERN_WIND_EFFECT;
    public static Holder<MobEffect>  DIMERITIUM_BOMB_EFFECT;
    public static Holder<MobEffect>  MOON_DUST_EFFECT;

    //Misc
    public static Holder<MobEffect>  BLEEDING;
    public static Holder<MobEffect>  CADAVERINE;

    public static void registerEffects() {

        //Effects
        //Potions
        {
            //W1
            WOLF_EFFECT = registerRegistryKey("wolf_effect",
                    registerStatusEffect(WolfEffect.class, MobEffectCategory.BENEFICIAL, 0xdd531d));

            //W2
            ROOK_EFFECT = registerRegistryKey("rook_effect",
                    registerStatusEffect(RookEffect.class, MobEffectCategory.BENEFICIAL, 0x268e26));


            // W3
            KILLER_WHALE_EFFECT = registerRegistryKey("killer_whale",
                    new KillerWhaleEffect(MobEffectCategory.BENEFICIAL, 0xe9b044)
                            .addAttributeModifier(
                                    Attributes.ATTACK_DAMAGE,
                                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "killer_whale_attack"),
                                    4.0,
                                    AttributeModifier.Operation.ADD_VALUE));

            SWALLOW_EFFECT = registerRegistryKey("swallow",
                    registerStatusEffect(SwallowEffect.class, MobEffectCategory.BENEFICIAL, 0xcc624a));

            WHITE_RAFFARDS_EFFECT = registerRegistryKey("white_raffards",
                    registerStatusEffect(WhiteRaffardsEffect.class, MobEffectCategory.BENEFICIAL, 0xb4b093));

            CAT_EFFECT = registerRegistryKey("cat_effect",
                    registerStatusEffect(CatEffect.class, MobEffectCategory.BENEFICIAL, 0x595959));

            BLACK_BLOOD_EFFECT = registerRegistryKey("black_blood",
                    registerStatusEffect(BlackBloodEffect.class, MobEffectCategory.BENEFICIAL, 0x272727));
            BLEEDING_BLACK_BLOOD_EFFECT = registerRegistryKey("bleeding_black_blood",
                    registerStatusEffect(BleedingBlackBloodEffect.class, MobEffectCategory.HARMFUL, 0x272727));

            MARIBOR_FOREST_EFFECT = registerRegistryKey("maribor_forest",
                    registerStatusEffect(MariborForestEffect.class, MobEffectCategory.BENEFICIAL, 0xb9b9b9)) ;

            //Decoctions
            {
                GRAVE_HAG_DECOCTION_EFFECT = registerRegistryKey("grave_hag_decoction",
                        registerStatusEffect(GraveHagDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor));

                WATER_HAG_DECOCTION_EFFECT = registerRegistryKey("water_hag_decoction",
                        new WaterHagDecoctionEffect(MobEffectCategory.BENEFICIAL, decoctionColor)
                        .addAttributeModifier(
                                Attributes.ATTACK_DAMAGE,
                                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "water_hag_decoction_attack"),
                                5.0,
                                AttributeModifier.Operation.ADD_VALUE));

                ALGHOUL_DECOCTION_EFFECT = registerRegistryKey("alghoul_decoction",
                        registerStatusEffect(AlghoulDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor));

                FOGLET_DECOCTION_EFFECT = registerRegistryKey("foglet_decoction",
                        registerStatusEffect(FogletDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor)) ;

                NEKKER_WARRIOR_DECOCTION_EFFECT = registerRegistryKey("nekker_warrior_decoction",
                        new NekkerWarriorDecoctionEffect(MobEffectCategory.BENEFICIAL, decoctionColor)
                                .addAttributeModifier(
                                        Attributes.ATTACK_DAMAGE,
                                        ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nekker_warrior_decoction_attack"),
                                        4.0,
                                        AttributeModifier.Operation.ADD_VALUE));

                TROLL_DECOCTION_EFFECT = registerRegistryKey("troll_decoction",
                        registerStatusEffect(TrollDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor));

            }
        }

        //Bomb Effects
        {
            SAMUM_EFFECT = registerRegistryKey("samum",
                    new SamumEffect(MobEffectCategory.HARMFUL, 0x6c777b)
                    .addAttributeModifier(
                            Attributes.MOVEMENT_SPEED,
                            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "samum_slowness"), -1.0f,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))
                    ;

            NORTHERN_WIND_EFFECT = registerRegistryKey("northern_wind",
                    new NorthernWindEffect(MobEffectCategory.HARMFUL, 0x007b77)
                            .addAttributeModifier(
                                    Attributes.MOVEMENT_SPEED,
                                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "northern_wind_slowness"), -1.0f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

            DIMERITIUM_BOMB_EFFECT = registerRegistryKey("dimeritium_bomb",
                    registerStatusEffect(DimeritiumBombEffect.class, MobEffectCategory.HARMFUL, 0x25882f));

            MOON_DUST_EFFECT = registerRegistryKey("moon_dust",
                    registerStatusEffect(MoonDustEffect.class, MobEffectCategory.HARMFUL, 0x87b8b8));
        }

        //Misc Effects
        {
            BLEEDING = registerRegistryKey("bleeding",
                    registerStatusEffect(BleedingEffect.class, MobEffectCategory.HARMFUL, 0xab0000));

            CADAVERINE = registerRegistryKey("cadaverine",
                    new CadaverineEffect(MobEffectCategory.HARMFUL,  0x00bd13)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE,
                            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "weakness_cadaverine"),
                            -2.0F,
                            AttributeModifier.Operation.ADD_VALUE)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "slowness_cadaverine"),
                            -0.15F,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }

    }

    public static MobEffect registerStatusEffect(Class<? extends MobEffect> effectClass, MobEffectCategory category, int color) {
        try {
//            return Registry.register(Registries.STATUS_EFFECT, Identifier.of(TCOTS_Main.MOD_ID, name),
//                    effectClass.getConstructor(StatusEffectCategory.class, int.class).newInstance(category, color));
            return  effectClass.getConstructor(MobEffectCategory.class, int.class).newInstance(category, color);
        } catch (Exception e) {
            throw new IllegalArgumentException("The effect was not created");
        }
    }

    private static MobEffect registerEffect(String name, MobEffect statusEffect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name), statusEffect);
    }

    private static Holder<MobEffect> registerRegistryKey(String id, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id), statusEffect);
    }
}
package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import TCOTS.effects.BleedingBlackBloodEffect;
import TCOTS.effects.BleedingEffect;
import TCOTS.effects.CadaverineEffect;
import TCOTS.effects.bombs.DimeritiumBombEffect;
import TCOTS.effects.bombs.MoonDustEffect;
import TCOTS.effects.bombs.NorthernWindEffect;
import TCOTS.effects.bombs.SamumEffect;
import TCOTS.effects.decoctions.*;
import TCOTS.effects.potions.*;
import dev.architectury.registry.registries.Registrar;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.function.Supplier;

public class TCOTS_Effects {

    static final int decoctionColor=0x0b7000;
    //W1
    public static final ResourceLocation WOLF_EFFECT= id("wolf_effect");
    //W2
    public static final ResourceLocation ROOK_EFFECT = id("rook_effect");
    //W3
    public static final ResourceLocation KILLER_WHALE_EFFECT = id("killer_whale");
    public static final ResourceLocation SWALLOW_EFFECT = id("swallow");
    public static final ResourceLocation WHITE_RAFFARDS_EFFECT = id("white_raffards");
    public static final ResourceLocation CAT_EFFECT = id("cat_effect");
    public static final ResourceLocation BLACK_BLOOD_EFFECT = id("black_blood");
    public static final ResourceLocation BLEEDING_BLACK_BLOOD_EFFECT = id("bleeding_black_blood");
    public static final ResourceLocation MARIBOR_FOREST_EFFECT = id("maribor_forest");
    //Decoctions
    public static final ResourceLocation GRAVE_HAG_DECOCTION_EFFECT = id("grave_hag_decoction");
    public static final ResourceLocation WATER_HAG_DECOCTION_EFFECT = id("water_hag_decoction");
    public static final ResourceLocation FOGLET_DECOCTION_EFFECT = id("foglet_decoction");
    public static final ResourceLocation ALGHOUL_DECOCTION_EFFECT = id("alghoul_decoction");
    public static final ResourceLocation NEKKER_WARRIOR_DECOCTION_EFFECT = id("nekker_warrior_decoction");
    public static final ResourceLocation TROLL_DECOCTION_EFFECT = id("troll_decoction");
    //Bombs
    public static final ResourceLocation SAMUM_EFFECT = id("samum");
    public static final ResourceLocation NORTHERN_WIND_EFFECT = id("northern_wind");
    public static final ResourceLocation DIMERITIUM_BOMB_EFFECT = id("dimeritium_bomb");
    public static final ResourceLocation MOON_DUST_EFFECT = id("moon_dust");
    //Misc
    public static final ResourceLocation BLEEDING = id("bleeding");
    public static final ResourceLocation CADAVERINE = id("cadaverine");

    public static void initEffects(){
        //Effects
        //Potions
        {
            //W1
            registerEffect(WOLF_EFFECT,
                    () -> createStatusEffect(WolfEffect.class, MobEffectCategory.BENEFICIAL, 0xdd531d));

            //W2
            registerEffect(ROOK_EFFECT,
                    () -> createStatusEffect(RookEffect.class, MobEffectCategory.BENEFICIAL, 0x268e26));

            //W3
            registerEffect(KILLER_WHALE_EFFECT,
                    () -> new KillerWhaleEffect(MobEffectCategory.BENEFICIAL, 0xe9b044)
                            .addAttributeModifier(
                                    Attributes.ATTACK_DAMAGE,
                                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "killer_whale_attack"),
                                    4.0,
                                    AttributeModifier.Operation.ADD_VALUE));

            registerEffect(SWALLOW_EFFECT,
                    () -> createStatusEffect(SwallowEffect.class, MobEffectCategory.BENEFICIAL, 0xcc624a));

            registerEffect(WHITE_RAFFARDS_EFFECT,
                    () -> createStatusEffect(WhiteRaffardsEffect.class, MobEffectCategory.BENEFICIAL, 0xb4b093));

            registerEffect(CAT_EFFECT,
                    () -> createStatusEffect(CatEffect.class, MobEffectCategory.BENEFICIAL, 0x595959));

            registerEffect(BLACK_BLOOD_EFFECT,
                    () -> createStatusEffect(BlackBloodEffect.class, MobEffectCategory.BENEFICIAL, 0x272727));

            registerEffect(BLEEDING_BLACK_BLOOD_EFFECT,
                    () -> createStatusEffect(BleedingBlackBloodEffect.class, MobEffectCategory.HARMFUL, 0x272727));

            registerEffect(MARIBOR_FOREST_EFFECT,
                    () -> createStatusEffect(MariborForestEffect.class, MobEffectCategory.BENEFICIAL, 0xb9b9b9));

            //Decoctions
            {
                registerEffect(GRAVE_HAG_DECOCTION_EFFECT,
                        () -> createStatusEffect(GraveHagDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor));

                registerEffect(WATER_HAG_DECOCTION_EFFECT,
                        () -> new WaterHagDecoctionEffect(MobEffectCategory.BENEFICIAL, decoctionColor)
                                .addAttributeModifier(
                                        Attributes.ATTACK_DAMAGE,
                                        ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "water_hag_decoction_attack"),
                                        5.0,
                                        AttributeModifier.Operation.ADD_VALUE));

                registerEffect(ALGHOUL_DECOCTION_EFFECT,
                        () -> createStatusEffect(AlghoulDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor));

                registerEffect(FOGLET_DECOCTION_EFFECT,
                        () -> createStatusEffect(FogletDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor));

                registerEffect(NEKKER_WARRIOR_DECOCTION_EFFECT,
                        () -> new NekkerWarriorDecoctionEffect(MobEffectCategory.BENEFICIAL, decoctionColor)
                                .addAttributeModifier(
                                        Attributes.ATTACK_DAMAGE,
                                        ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nekker_warrior_decoction_attack"),
                                        4.0,
                                        AttributeModifier.Operation.ADD_VALUE));

                registerEffect(TROLL_DECOCTION_EFFECT,
                        () -> createStatusEffect(TrollDecoctionEffect.class, MobEffectCategory.BENEFICIAL, decoctionColor));
            }
        }

        //Bomb Effects
        {
            registerEffect(SAMUM_EFFECT,
                    () -> new SamumEffect(MobEffectCategory.HARMFUL, 0x6c777b)
                            .addAttributeModifier(
                                    Attributes.MOVEMENT_SPEED,
                                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "samum_slowness"),
                                    -1.0f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

            registerEffect(NORTHERN_WIND_EFFECT,
                    () -> new NorthernWindEffect(MobEffectCategory.HARMFUL, 0x007b77)
                            .addAttributeModifier(
                                    Attributes.MOVEMENT_SPEED,
                                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "northern_wind_slowness"),
                                    -1.0f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

            registerEffect(DIMERITIUM_BOMB_EFFECT,
                    () -> createStatusEffect(DimeritiumBombEffect.class, MobEffectCategory.HARMFUL, 0x25882f));

            registerEffect(MOON_DUST_EFFECT,
                    () -> createStatusEffect(MoonDustEffect.class, MobEffectCategory.HARMFUL, 0x87b8b8));
        }

        //Misc Effects
        {
            registerEffect(BLEEDING,
                    () -> createStatusEffect(BleedingEffect.class, MobEffectCategory.HARMFUL, 0xab0000));

            registerEffect(CADAVERINE,
                    () -> new CadaverineEffect(MobEffectCategory.HARMFUL,  0x00bd13)
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

    // W1
    public static Holder<MobEffect> WolfEffect() {
        return getHolder(WOLF_EFFECT);
    }

    // W2
    public static Holder<MobEffect> RookEffect() {
        return getHolder(ROOK_EFFECT);
    }

    // W3
    public static Holder<MobEffect> KillerWhaleEffect() {
        return getHolder(KILLER_WHALE_EFFECT);
    }

    public static Holder<MobEffect> SwallowEffect() {
        return getHolder(SWALLOW_EFFECT);
    }

    public static Holder<MobEffect> WhiteRaffardsEffect() {
        return getHolder(WHITE_RAFFARDS_EFFECT);
    }

    public static Holder<MobEffect> CatEffect() {
        return getHolder(CAT_EFFECT);
    }

    public static Holder<MobEffect> BlackBloodEffect() {
        return getHolder(BLACK_BLOOD_EFFECT);
    }

    public static Holder<MobEffect> BleedingBlackBloodEffect() {
        return getHolder(BLEEDING_BLACK_BLOOD_EFFECT);
    }

    public static Holder<MobEffect> MariborForestEffect() {
        return getHolder(MARIBOR_FOREST_EFFECT);
    }

    // Decoctions
    public static Holder<MobEffect> GraveHagDecoctionEffect() {
        return getHolder(GRAVE_HAG_DECOCTION_EFFECT);
    }

    public static Holder<MobEffect> WaterHagDecoctionEffect() {
        return getHolder(WATER_HAG_DECOCTION_EFFECT);
    }

    public static Holder<MobEffect> FogletDecoctionEffect() {
        return getHolder(FOGLET_DECOCTION_EFFECT);
    }

    public static Holder<MobEffect> AlghoulDecoctionEffect() {
        return getHolder(ALGHOUL_DECOCTION_EFFECT);
    }

    public static Holder<MobEffect> NekkerWarriorDecoctionEffect() {
        return getHolder(NEKKER_WARRIOR_DECOCTION_EFFECT);
    }

    public static Holder<MobEffect> TrollDecoctionEffect() {
        return getHolder(TROLL_DECOCTION_EFFECT);
    }

    // Bombs
    public static Holder<MobEffect> SamumEffect() {
        return getHolder(SAMUM_EFFECT);
    }

    public static Holder<MobEffect> NorthernWindEffect() {
        return getHolder(NORTHERN_WIND_EFFECT);
    }

    public static Holder<MobEffect> DimeritiumBombEffect() {
        return getHolder(DIMERITIUM_BOMB_EFFECT);
    }

    public static Holder<MobEffect> MoonDustEffect() {
        return getHolder(MOON_DUST_EFFECT);
    }

    // Misc
    public static Holder<MobEffect> Bleeding() {
        return getHolder(BLEEDING);
    }

    public static Holder<MobEffect> Cadaverine() {
        return getHolder(CADAVERINE);
    }

    public static MobEffect createStatusEffect(Class<? extends MobEffect> effectClass, MobEffectCategory category, int color) {
        try {
            return  effectClass.getConstructor(MobEffectCategory.class, int.class).newInstance(category, color);
        } catch (Exception e) {
            throw new IllegalArgumentException("The effect was not created");
        }
    }

    private static ResourceLocation id(String name){
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name);
    }

    private static void registerEffect(ResourceLocation id, Supplier<MobEffect> statusEffect) {
        TCOTS_Registries.MOB_EFFECTS.register(id, statusEffect);
    }

    public static Holder<MobEffect> getHolder(ResourceLocation id) {
        Holder<MobEffect> holder = TCOTS_Registries.MOB_EFFECTS.getRegistrar().getHolder(id);
        if (holder == null) {
            throw new IllegalArgumentException("MobEffect with id " + id + " does not exist");
        }
        return holder;
    }
}

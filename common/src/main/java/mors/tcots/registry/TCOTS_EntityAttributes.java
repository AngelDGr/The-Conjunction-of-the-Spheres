package mors.tcots.registry;

import mors.tcots.TCOTS_Main;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class TCOTS_EntityAttributes {
    public static final Holder<Attribute> WITCHER_MAX_TOXICITY = register(
            "witcher_toxicity",
            new RangedAttribute("attribute.name.tcots_witcher.max_witcher_toxicity", 100.0, 10.0, 1000.0).setSyncable(true)
    );

    public static final Holder<Attribute> RESISTANCE_AGAINST_MONSTERS = register(
            "resistance_against_monsters",
            new RangedAttribute("attribute.name.tcots_witcher.resistance_against_monsters", 1.0, 0.0, 100.0).setSyncable(true)
    );

    public static final Holder<Attribute> DAMAGE_AGAINST_MONSTERS = register(
            "damage_against_monsters",
            new RangedAttribute("attribute.name.tcots_witcher.damage_against_monsters", 1.0, 0.0, 100.0).setSyncable(true)
    );

    public static final Holder<Attribute> BOMB_COOLDOWN = register(
            "bomb_cooldown",
            new RangedAttribute("attribute.name.tcots_witcher.bomb_cooldown", 1.0, 0.0, 10.0).setSyncable(true)
    );

    public static final Holder<Attribute> POTION_DRINK_TIME = register(
            "potion_drink_time",
            new RangedAttribute("attribute.name.tcots_witcher.potion_drink_time", 1.0, 0.0, 10.0).setSyncable(true)
    );

    public static final Holder<Attribute> EXTRA_ALCOHOL_REFILL = register(
            "extra_alcohol_refill",
            new RangedAttribute("attribute.name.tcots_witcher.extra_alcohol_refill", 0.0, 0.0, 30.0).setSyncable(true)
    );

    public static final Holder<Attribute> ADRENALINE_GAIN = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "adrenaline_modifier")));
    public static final Holder<Attribute> SIGN_INTENSITY = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "sign_intensity")));
    public static final Holder<Attribute> AARD_INTENSITY = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "aard_intensity")));
    public static final Holder<Attribute> SPELL_CRITICAL_DAMAGE = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(BuiltInRegistries.ATTRIBUTE.get(ResourceLocation.fromNamespaceAndPath("spell_power", "critical_damage")));

    @SuppressWarnings("all")
    private static Holder<Attribute> register(String id, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id), attribute);
    }
}

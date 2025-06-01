package TCOTS.entity;

import TCOTS.TCOTS_Main;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class TCOTS_EntityAttributes {
    public static final Holder<Attribute> GENERIC_WITCHER_MAX_TOXICITY = register(
            "generic.witcher_toxicity",
            new RangedAttribute("attribute.name.generic.max_witcher_toxicity", 100.0, 10.0, 1000.0).setSyncable(true)
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

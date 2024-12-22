package TCOTS.entity;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TCOTS_EntityAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_WITCHER_MAX_TOXICITY = register(
            "generic.witcher_toxicity",
            new ClampedEntityAttribute("attribute.name.generic.max_witcher_toxicity", 100.0, 10.0, 1000.0).setTracked(true)
    );

    public static final RegistryEntry<EntityAttribute> ADRENALINE_GAIN = Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(Identifier.of("witcher_rpg", "adrenaline_modifier")));
    public static final RegistryEntry<EntityAttribute> SIGN_INTENSITY = Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(Identifier.of("witcher_rpg", "sign_intensity")));
    public static final RegistryEntry<EntityAttribute> AARD_INTENSITY = Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(Identifier.of("witcher_rpg", "aard_intensity")));
    public static final RegistryEntry<EntityAttribute> SPELL_CRITICAL_DAMAGE = Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(Identifier.of("spell_power", "critical_damage")));



    @SuppressWarnings("all")
    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(TCOTS_Main.MOD_ID,id), attribute);
    }
}

package TCOTS.entity;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class TCOTS_EntityAttributes {
    public static final RegistryEntry<EntityAttribute> GENERIC_WITCHER_TOXICITY = register(
            "generic.witcher_toxicity",
            new ClampedEntityAttribute("attribute.name.generic.witcher_toxicity", 100.0, 10.0, 1000.0).setTracked(true)
    );

    @SuppressWarnings("all")
    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(TCOTS_Main.MOD_ID,id), attribute);
    }
}

package TCOTS.entity;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class TCOTS_EntityAttributes {
    public static final EntityAttribute GENERIC_WITCHER_MAX_TOXICITY = register(
            "generic.witcher_toxicity",
            new ClampedEntityAttribute("attribute.name.generic.max_witcher_toxicity", 100.0, 10.0, 1000.0).setTracked(true)
    );

    @SuppressWarnings("all")
    private static EntityAttribute register(String id, EntityAttribute attribute) {
        return Registry.register(Registries.ATTRIBUTE, id, attribute);
    }
}

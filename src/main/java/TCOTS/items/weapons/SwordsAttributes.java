package TCOTS.items.weapons;

import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.items.TCOTS_ToolMaterials;
import com.google.common.collect.ImmutableMultimap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

import java.util.UUID;

public class SwordsAttributes {
    protected static final UUID ADRENALINE_GAIN = UUID.fromString("78166baa-0606-47c7-a8ae-d89935a1cb63");
    protected static final UUID AARD_INTENSITY = UUID.fromString("627fe661-e00a-4097-8e50-200a72d37af8");
    protected static final UUID SPELL_CRITICAL = UUID.fromString("20ef940b-3ba9-425a-b34d-5b668d0e90ea");
    protected static final UUID SIGN_INTENSITY = UUID.fromString("91d3096d-aff2-4a39-b3e2-598bd0946127");

    private static ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createMainSwordAttributeModifiers(ToolMaterial material, int baseAttackDamage, float attackSpeed) {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();

        builder.put(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(Item.ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", (double) baseAttackDamage + material.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));

        builder.put(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(Item.ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", attackSpeed, EntityAttributeModifier.Operation.ADDITION)
        );

        return builder;
    }

    public static ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createWintersBladeAttributeModifiers() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder =createMainSwordAttributeModifiers(TCOTS_ToolMaterials.WINTERS_BLADE, 4, -2.4f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .put(
                            TCOTS_EntityAttributes.AARD_INTENSITY.value(),
                            new EntityAttributeModifier(AARD_INTENSITY, "Weapon modifier",4, EntityAttributeModifier.Operation.ADDITION)
                    )
                    .put(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN.value(),
                            new EntityAttributeModifier(ADRENALINE_GAIN, "Weapon modifier", 0.05, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    )
                    .put(
                            TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE.value(),
                            new EntityAttributeModifier(SPELL_CRITICAL, "Weapon modifier",0.10, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    );
        }

        return builder;
    }

    public static ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createArdaenyeAttributeModifiers() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.ARDAENYE, 3, -2.6f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .put(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN.value(),
                            new EntityAttributeModifier(ADRENALINE_GAIN, "Weapon modifier", 0.15, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            );
        }

        return builder;
    }

    public static ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createDyaeblAttributeModifiers() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.DYAEBL,  2, -2.4f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .put(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN.value(),
                            new EntityAttributeModifier(ADRENALINE_GAIN,"Weapon modifier", 0.075, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    );
        }

        return builder;
    }

    public static ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createMoonbladeAttributeModifiers() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.MOONBLADE, 2, -2.2f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .put(
                            TCOTS_EntityAttributes.SIGN_INTENSITY.value(),
                            new EntityAttributeModifier(SIGN_INTENSITY, "Weapon modifier", 2.5, EntityAttributeModifier.Operation.ADDITION)
                    )
                    .put(
                            TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE.value(),
                            new EntityAttributeModifier(SPELL_CRITICAL,"Weapon modifier", 0.05, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    );
        }

        return builder;
    }

    public static ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> createGvalchirAttributeModifiers() {
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.GVALCHIR, 3, -2.2f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .put(
                            TCOTS_EntityAttributes.SIGN_INTENSITY.value(),
                            new EntityAttributeModifier(SIGN_INTENSITY,"Weapon modifier",2.5, EntityAttributeModifier.Operation.ADDITION)
                    )
                    .put(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN.value(),
                            new EntityAttributeModifier(ADRENALINE_GAIN,"Weapon modifier", 0.05, EntityAttributeModifier.Operation.MULTIPLY_BASE)
                    );
        }

        return builder;
    }

}

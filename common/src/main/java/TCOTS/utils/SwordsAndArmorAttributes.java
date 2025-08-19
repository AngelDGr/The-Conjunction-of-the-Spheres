package TCOTS.utils;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.registry.TCOTS_ItemsMaterials;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SwordsAndArmorAttributes {
    // It subtracts from 4 to get final speed
    static float slowSwordSpeed   =   MiscUtil.isWitcherRPGLoaded()? -2.6f : -2.6f; // W(RPG) = 2.2 / Vanilla = 1.4
    static float normalSwordSpeed =   MiscUtil.isWitcherRPGLoaded()? -2.4f : -2.4f; // W(RPG) = 2.4 / Vanilla = 1.6
    static float fastSwordSpeed   =   MiscUtil.isWitcherRPGLoaded()? -2.2f : -2.2f; // W(RPG) = 2.6 / Vanilla = 1.8


    public static final ResourceLocation BASE_ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.withDefaultNamespace("base_attack_damage");
    public static final ResourceLocation BASE_ATTACK_SPEED_MODIFIER_ID = ResourceLocation.withDefaultNamespace("base_attack_speed");
    public static ItemAttributeModifiers.Builder createMainSwordAttributeModifiers(final Tier material, final float baseAttackDamage, final float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_MODIFIER_ID, baseAttackDamage + material.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                );
    }

    public static ItemAttributeModifiers createWintersBladeAttributeModifiers() {
        final ItemAttributeModifiers.Builder builder = SwordsAndArmorAttributes.createMainSwordAttributeModifiers(TCOTS_ItemsMaterials.WintersBlade(), 3, normalSwordSpeed);

        if(MiscUtil.isWitcherRPGLoaded()){
            builder
                    .add(
                            TCOTS_EntityAttributes.AARD_INTENSITY,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade-aard"),
                                    3, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade-adrenaline"),
                                    0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade-spell_critical"),
                                    0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createArdaenyeAttributeModifiers() {
        final ItemAttributeModifiers.Builder builder = SwordsAndArmorAttributes.createMainSwordAttributeModifiers(TCOTS_ItemsMaterials.Ardaenye(), 2.5f, normalSwordSpeed);

        if(MiscUtil.isWitcherRPGLoaded()){
            builder
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "ardaenye-adrenaline"),
                                    0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createDyaeblAttributeModifiers() {
        final ItemAttributeModifiers.Builder builder = SwordsAndArmorAttributes.createMainSwordAttributeModifiers(TCOTS_ItemsMaterials.Dyaebl(),  2, normalSwordSpeed);

        if(MiscUtil.isWitcherRPGLoaded()){
            builder
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "dyaebl-adrenaline"),
                                    0.075, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createMoonbladeAttributeModifiers() {
        final ItemAttributeModifiers.Builder builder = SwordsAndArmorAttributes.createMainSwordAttributeModifiers(TCOTS_ItemsMaterials.Moonblade(), 2, normalSwordSpeed);

        if(MiscUtil.isWitcherRPGLoaded()){
            builder
                    .add(
                            TCOTS_EntityAttributes.SIGN_INTENSITY,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "moonblade-sign"),
                                    2.5, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "moonblade-spell_critical"),
                                    0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createGvalchirAttributeModifiers() {
        final ItemAttributeModifiers.Builder builder = SwordsAndArmorAttributes.createMainSwordAttributeModifiers(TCOTS_ItemsMaterials.Gvalchir(), 3, fastSwordSpeed);

        if(MiscUtil.isWitcherRPGLoaded()){
            builder
                    .add(
                            TCOTS_EntityAttributes.SIGN_INTENSITY,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "gvalchir-sign"),
                                    2.5, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "gvalchir-adrenaline"),
                                    0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }


    @SuppressWarnings("unused")
    public static ItemAttributeModifiers addExtraToxicity(final double toxicity, final EquipmentSlotGroup slot, final String id){
        return ItemAttributeModifiers.builder()
                .add(
                        TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id),
                                toxicity,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        slot
                )
                .build();
    }

    public static ItemAttributeModifiers addArmorWithAdrenalineAttributes(final Holder<ArmorMaterial> material, final ArmorItem.Type type, final double adrenaline){
        final int i = material.value().getDefense(type);
        final float f = material.value().toughness();
        final ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        final EquipmentSlotGroup attributeModifierSlot = EquipmentSlotGroup.bySlot(type.getSlot());
        final ResourceLocation identifier = ResourceLocation.withDefaultNamespace("armor." + type.getName());

        builder.add(
                Attributes.ARMOR, new AttributeModifier(identifier, i, AttributeModifier.Operation.ADD_VALUE), attributeModifierSlot
        );
        builder.add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(identifier, f, AttributeModifier.Operation.ADD_VALUE),
                attributeModifierSlot
        );
        final float g = material.value().knockbackResistance();
        if (g > 0.0F) {
            builder.add(
                    Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(identifier, g, AttributeModifier.Operation.ADD_VALUE),
                    attributeModifierSlot
            );
        }

        if(MiscUtil.isWitcherRPGLoaded()){
            builder.add(
                    TCOTS_EntityAttributes.ADRENALINE_GAIN,
                    new AttributeModifier(
                            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "armor."+ type.getName()),
                            adrenaline,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    attributeModifierSlot
            );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers addManticoreArmorAttributes(final RegistrySupplier<ArmorMaterial> material, final ArmorItem.Type type, final int toxicity, final double adrenaline){
        final int i = material.value().getDefense(type);
        final float f = material.value().toughness();
        final ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        final EquipmentSlotGroup attributeModifierSlot = EquipmentSlotGroup.bySlot(type.getSlot());
        final ResourceLocation identifier = ResourceLocation.withDefaultNamespace("armor." + type.getName());

        builder.add(
                Attributes.ARMOR, new AttributeModifier(identifier, i, AttributeModifier.Operation.ADD_VALUE), attributeModifierSlot
        );
        builder.add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(identifier, f, AttributeModifier.Operation.ADD_VALUE),
                attributeModifierSlot
        );
        final float g = material.value().knockbackResistance();
        if (g > 0.0F) {
            builder.add(
                    Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(identifier, g, AttributeModifier.Operation.ADD_VALUE),
                    attributeModifierSlot
            );
        }
        builder.add(
                TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY,
                new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "armor."+ type.getName()),
                        toxicity,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                attributeModifierSlot
        );

        if(MiscUtil.isWitcherRPGLoaded()){
            builder.add(
                    TCOTS_EntityAttributes.ADRENALINE_GAIN,
                    new AttributeModifier(
                            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "armor."+ type.getName()),
                            adrenaline,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    attributeModifierSlot
            );
        }

        return builder.build();
    }
}

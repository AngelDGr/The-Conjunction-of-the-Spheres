package mors.tcots.registry;

import mors.tcots.TCOTS_Main;
import mors.tcots.utils.AttributeModifiersMapBuilder;
import mors.tcots.utils.TCOTS_Util;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class TCOTS_ItemsAttributes {

    public static class Weapons {
        public static final ResourceLocation BASE_ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.withDefaultNamespace("base_attack_damage");
        public static final ResourceLocation BASE_ATTACK_SPEED_MODIFIER_ID = ResourceLocation.withDefaultNamespace("base_attack_speed");

        // It subtracts from 4 to get final speed
        static float slowSwordSpeed   = -2.6f; // W(RPG) = 2.2 / Vanilla = 1.4
        static float normalSwordSpeed = -2.4f; // W(RPG) = 2.4 / Vanilla = 1.6
        static float fastSwordSpeed   = -2.2f; // W(RPG) = 2.6 / Vanilla = 1.8

        public static ItemAttributeModifiers createWintersBladeAttributeModifiers() {
            final ItemAttributeModifiers.Builder builder = genericSwordAttributes(TCOTS_ItemsMaterials.Tool.WINTERS_BLADE, 3, normalSwordSpeed);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addSwordAttribute(builder, TCOTS_EntityAttributes.AARD_INTENSITY, 3, TCOTS_ItemsMaterials.Tool.WINTERS_BLADE, AttributeModifier.Operation.ADD_VALUE);
                addSwordAttribute(builder, TCOTS_EntityAttributes.ADRENALINE_GAIN, 0.05, TCOTS_ItemsMaterials.Tool.WINTERS_BLADE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                addSwordAttribute(builder, TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE, 0.15, TCOTS_ItemsMaterials.Tool.WINTERS_BLADE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers createArdaenyeAttributeModifiers() {
            final ItemAttributeModifiers.Builder builder = genericSwordAttributes(TCOTS_ItemsMaterials.Tool.ARDAENYE, 2.5f, normalSwordSpeed);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addSwordAttribute(builder, TCOTS_EntityAttributes.ADRENALINE_GAIN, 0.15, TCOTS_ItemsMaterials.Tool.ARDAENYE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers createDyaeblAttributeModifiers() {
            final ItemAttributeModifiers.Builder builder = genericSwordAttributes(TCOTS_ItemsMaterials.Tool.DYAEBL,  2, normalSwordSpeed);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addSwordAttribute(builder, TCOTS_EntityAttributes.SIGN_INTENSITY, 0.075, TCOTS_ItemsMaterials.Tool.DYAEBL, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers createMoonbladeAttributeModifiers() {
            final ItemAttributeModifiers.Builder builder = genericSwordAttributes(TCOTS_ItemsMaterials.Tool.MOONBLADE, 2, normalSwordSpeed);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addSwordAttribute(builder, TCOTS_EntityAttributes.SIGN_INTENSITY, 2.5, TCOTS_ItemsMaterials.Tool.MOONBLADE, AttributeModifier.Operation.ADD_VALUE);
                addSwordAttribute(builder, TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE, 0.05, TCOTS_ItemsMaterials.Tool.MOONBLADE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers createGvalchirAttributeModifiers() {
            final ItemAttributeModifiers.Builder builder = genericSwordAttributes(TCOTS_ItemsMaterials.Tool.GVALCHIR, 3, fastSwordSpeed);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addSwordAttribute(builder, TCOTS_EntityAttributes.SIGN_INTENSITY, 2.5, TCOTS_ItemsMaterials.Tool.GVALCHIR, AttributeModifier.Operation.ADD_VALUE);
                addSwordAttribute(builder, TCOTS_EntityAttributes.ADRENALINE_GAIN, 0.05, TCOTS_ItemsMaterials.Tool.GVALCHIR, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers.Builder genericSwordAttributes(final Tier material, final float baseAttackDamage, final float attackSpeed) {
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
    }

    public static class Armors {
        public static ItemAttributeModifiers warriorLeathersAttributes(final ArmorItem.Type type){
            final ItemAttributeModifiers.Builder builder = genericArmorAttributes(TCOTS_ItemsMaterials.Armor.WARRIORS_LEATHER, type);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addArmorAttribute(builder, TCOTS_EntityAttributes.SIGN_INTENSITY, 0.10, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                addArmorAttribute(builder, TCOTS_EntityAttributes.ADRENALINE_GAIN, 0.04, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                addArmorAttribute(builder, Attributes.ATTACK_DAMAGE, 0.04, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers ravensAttributes(final ArmorItem.Type type){
            final ItemAttributeModifiers.Builder builder = genericArmorAttributes(TCOTS_ItemsMaterials.Armor.RAVEN, type);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addArmorAttribute(builder, TCOTS_EntityAttributes.SIGN_INTENSITY, 0.05, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                addArmorAttribute(builder, TCOTS_EntityAttributes.ADRENALINE_GAIN, 0.15, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                addArmorAttribute(builder, Attributes.ATTACK_DAMAGE, 0.04, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers manticoreArmorAttributes(final ArmorItem.Type type){
            final ItemAttributeModifiers.Builder builder = genericArmorAttributes(TCOTS_ItemsMaterials.Armor.MANTICORE, type);

            addArmorAttribute(builder, TCOTS_EntityAttributes.WITCHER_MAX_TOXICITY, 15, type, AttributeModifier.Operation.ADD_VALUE);

            if(TCOTS_Util.isWitcherRPGLoaded()){
                addArmorAttribute(builder, TCOTS_EntityAttributes.SIGN_INTENSITY, 0.075, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
                addArmorAttribute(builder, TCOTS_EntityAttributes.ADRENALINE_GAIN, 0.10, type, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            }

            return builder.build();
        }

        public static ItemAttributeModifiers.Builder genericArmorAttributes(final Holder<ArmorMaterial> material,
                                                                                final ArmorItem.Type type){
            final var builder = ItemAttributeModifiers.builder();
            final ResourceLocation identifier = ResourceLocation.withDefaultNamespace("armor." + type.getName());
            final EquipmentSlotGroup attributeModifierSlot = EquipmentSlotGroup.bySlot(type.getSlot());

            builder.add(
                    Attributes.ARMOR, new AttributeModifier(identifier, material.value().getDefense(type), AttributeModifier.Operation.ADD_VALUE), attributeModifierSlot
            );
            builder.add(
                    Attributes.ARMOR_TOUGHNESS,
                    new AttributeModifier(identifier, material.value().toughness(), AttributeModifier.Operation.ADD_VALUE),
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

            return builder;
        }
    }

    public enum Accessories {

        MANTICORE_MEDALLION(
                AttributeModifiersMapBuilder.create()
                        .attribute(TCOTS_EntityAttributes.WITCHER_MAX_TOXICITY,
                                "medallion.toxicity", 20, AttributeModifier.Operation.ADD_VALUE
                        )
                        .attribute(TCOTS_EntityAttributes.SIGN_INTENSITY,
                                "medallion.sign_intensity", 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                TCOTS_Util.isWitcherRPGLoaded()
                        )
                        .attribute(TCOTS_EntityAttributes.ADRENALINE_GAIN,
                                "medallion.adrenaline_gain", 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE,
                                TCOTS_Util.isWitcherRPGLoaded())
                        .getModifiers(),
                "spell_trinket", true);

        public final Multimap<Holder<Attribute>, AttributeModifier> modifiers;
        public final String accessorySlotName;
        public final boolean isStackable;

        Accessories(final Multimap<Holder<Attribute>, AttributeModifier> modifiers, final String accessorySlotName, final boolean isStackable){
            this.modifiers=modifiers;
            this.accessorySlotName=accessorySlotName;
            this.isStackable=isStackable;
        }
    }

    public static void addArmorAttribute(final ItemAttributeModifiers.Builder builder,
                                         final Holder<Attribute> attribute,
                                         final double value,
                                         final ArmorItem.Type type,
                                         final AttributeModifier.Operation operation){
        builder.add(
                attribute,
                new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "armor."+ type.getName()),
                        value,
                        operation
                ),
                EquipmentSlotGroup.bySlot(type.getSlot())
        );
    }

    public static void addSwordAttribute(final ItemAttributeModifiers.Builder builder,
                                         final Holder<Attribute> attribute,
                                         final double value,
                                         final TCOTS_ItemsMaterials.Tool material,
                                         final AttributeModifier.Operation operation){
        final var attributeName=  ResourceLocation.parse(attribute.getRegisteredName());
        builder
                .add(
                        attribute,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, material.getName()+"."+ attributeName.getPath()),
                                value,
                                operation),
                        EquipmentSlotGroup.MAINHAND
                );
    }
}

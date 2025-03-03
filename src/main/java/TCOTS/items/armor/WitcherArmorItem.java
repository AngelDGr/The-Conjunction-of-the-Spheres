package TCOTS.items.armor;

import TCOTS.entity.TCOTS_EntityAttributes;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.Util;

import java.util.EnumMap;
import java.util.UUID;

public class WitcherArmorItem extends ArmorItem {
    private static final EnumMap<Type, UUID> MODIFIERS = Util.make(new EnumMap<>(ArmorItem.Type.class), uuidMap -> {
        uuidMap.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        uuidMap.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        uuidMap.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        uuidMap.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public WitcherArmorItem(ArmorMaterial material, Type type, Settings settings, double adrenaline) {
        super(material, type, settings);

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        UUID uUID = MODIFIERS.get(type);
        builder.put(
                EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(uUID, "Armor modifier", this.getProtection(), EntityAttributeModifier.Operation.ADDITION)
        );
        builder.put(
                EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
                new EntityAttributeModifier(uUID, "Armor toughness", this.getToughness(), EntityAttributeModifier.Operation.ADDITION)
        );

        builder.put(
                EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                new EntityAttributeModifier(uUID, "Armor knockback resistance", this.knockbackResistance, EntityAttributeModifier.Operation.ADDITION)
        );

        if(this instanceof ManticoreArmorItem){
            builder.put(
                    TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY, new EntityAttributeModifier(uUID, "Armor toxicity", 10, EntityAttributeModifier.Operation.ADDITION)
            );
        }


        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder.put(
                    TCOTS_EntityAttributes.ADRENALINE_GAIN.value(),
                    new EntityAttributeModifier(uUID, "Armor adrenaline gain", adrenaline, EntityAttributeModifier.Operation.MULTIPLY_BASE)
            );
        }

        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == this.type.getEquipmentSlot() ? this.attributeModifiers : ImmutableMultimap.of();
    }
}

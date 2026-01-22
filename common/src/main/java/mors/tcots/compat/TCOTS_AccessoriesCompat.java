package mors.tcots.compat;

import mors.tcots.items.WitcherMedallionItem;
import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.registry.TCOTS_ItemsAttributes;
import com.google.common.collect.Multimap;
import io.wispforest.accessories.api.components.AccessoriesDataComponents;
import io.wispforest.accessories.api.components.AccessoryItemAttributeModifiers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.List;

public class TCOTS_AccessoriesCompat {

    public static Item MANTICORE_MEDALLION = new WitcherMedallionItem(
            new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)
                    .component(AccessoriesDataComponents.ATTRIBUTES, convertMap(TCOTS_ItemsAttributes.Accessories.MANTICORE_MEDALLION)),
            ArmorSet.MANTICORE
    );

    private static AccessoryItemAttributeModifiers convertMap(final TCOTS_ItemsAttributes.Accessories attribute){

        final Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = attribute.modifiers;
        final List<AccessoryItemAttributeModifiers.Entry> attributes = new ArrayList<>();

        for (final Holder<Attribute> attributeHolder : modifierMultimap.keys()){
            for(final var modifier : modifierMultimap.get(attributeHolder)){
                attributes.add(new AccessoryItemAttributeModifiers.Entry(
                        attributeHolder, modifier, attribute.accessorySlotName, attribute.isStackable
                        ));
            }
        }

        return new AccessoryItemAttributeModifiers(attributes, true);
    }

    public static boolean isWearingAccessoryItem(final LivingEntity player, final Item medallion){
        if(player.accessoriesCapability()==null) return false;

        return player.accessoriesCapability().isEquipped(medallion);
    }
}

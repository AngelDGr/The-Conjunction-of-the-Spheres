package mors.fabric.tcots.items;

import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.items.armor.set.IsArmorSet;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class WitcherMedallionItem extends TrinketItem implements IsArmorSet {

    private final ArmorSet armorSet;

    private final Multimap<Holder<Attribute>, AttributeModifier> attributes;

    public WitcherMedallionItem(final Properties properties, @Nullable final ArmorSet armorSet, final Multimap<Holder<Attribute>, AttributeModifier> attributes) {
        super(properties);
        this.armorSet=armorSet;
        this.attributes=attributes;
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getModifiers(final ItemStack stack, final SlotReference slot, final LivingEntity entity, final ResourceLocation slotIdentifier) {
        final var map = super.getModifiers(stack, slot, entity, slotIdentifier);
        map.putAll(attributes);
        return map;
    }

    @Override
    public ArmorSet getSet() {
        return armorSet;
    }
}

package mors.tcots.items.armor.set;

import mors.tcots.registry.TCOTS_EntityAttributes;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.utils.AttributeModifiersMapBuilder;
import mors.tcots.utils.TCOTS_Util;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public enum ArmorSet {

    WARRIORS_LEATHER("warriors_leather", Map.of(
            EquipmentSlot.HEAD, TCOTS_Items.WARRIORS_LEATHER_HEAD.get(),
            EquipmentSlot.CHEST, TCOTS_Items.WARRIORS_LEATHER_JACKET.get(),
            EquipmentSlot.LEGS, TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get(),
            EquipmentSlot.FEET, TCOTS_Items.WARRIORS_LEATHER_BOOTS.get()),
            null, -1, -1,
            null,
            null,
            //+25% Resistance Against Monsters
            AttributeModifiersMapBuilder.create()
                    .attribute(TCOTS_EntityAttributes.RESISTANCE_AGAINST_MONSTERS,
                            "set.warriors_leather_resistance",
                            0.25d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .getModifiers()),

    RAVEN("raven", Map.of(
            EquipmentSlot.HEAD, TCOTS_Items.RAVENS_HEAD.get(),
            EquipmentSlot.CHEST, TCOTS_Items.RAVENS_ARMOR.get(),
            EquipmentSlot.LEGS, TCOTS_Items.RAVENS_TROUSERS.get(),
            EquipmentSlot.FEET, TCOTS_Items.RAVENS_BOOTS.get()),
            null, 2, 3,
            //+10% Speed
            AttributeModifiersMapBuilder.create()
            .attribute(Attributes.MOVEMENT_SPEED,
                    "set.raven_speed",
                    0.1d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            .getModifiers(),
            //+50% Resistance Against Monsters / +50% Damage Against Monsters
            AttributeModifiersMapBuilder.create()
                    .attribute(TCOTS_EntityAttributes.RESISTANCE_AGAINST_MONSTERS,
                            "set.raven_resistance",
                            0.5d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(TCOTS_EntityAttributes.DAMAGE_AGAINST_MONSTERS,
                            "set.raven_damage",
                            0.35d, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .getModifiers(),
            null),

    MANTICORE("manticore", Map.of(
            EquipmentSlot.HEAD, TCOTS_Items.MANTICORE_HEAD.get(),
            EquipmentSlot.CHEST, TCOTS_Items.MANTICORE_ARMOR.get(),
            EquipmentSlot.LEGS, TCOTS_Items.MANTICORE_TROUSERS.get(),
            EquipmentSlot.FEET, TCOTS_Items.MANTICORE_BOOTS.get()),
            TCOTS_Items.MANTICORE_SCHOOL_MEDALLION, 2, 4,
            //-75% Bomb Cooldown
            AttributeModifiersMapBuilder.create()
                    .attribute(TCOTS_EntityAttributes.BOMB_COOLDOWN,
                            "set.manticore_bomb_cooldown",
                            -0.75, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .getModifiers(),
            //+2 Refills With Alcohol and 50% Potion Drink Reduction
            AttributeModifiersMapBuilder.create()
                    .attribute(TCOTS_EntityAttributes.EXTRA_ALCOHOL_REFILL,
                            "set.manticore_extra_alcohol",
                            2.0, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(TCOTS_EntityAttributes.POTION_DRINK_TIME,
                            "set.manticore_drink_time",
                            -0.50, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .getModifiers(),
            null);

    public final Map<EquipmentSlot, Item> items;
    private final String setName;
    @Nullable
    public final Supplier<Item> medallion;
    @Nullable
    public final Multimap<Holder<Attribute>, AttributeModifier> firstModifiers;
    private final int firstBonusAmount;
    @Nullable
    public final Multimap<Holder<Attribute>, AttributeModifier> secondModifiers;
    private final int secondBonusAmount;
    @Nullable
    public final Multimap<Holder<Attribute>, AttributeModifier> fullSetModifiers;
    private final int setAmount;


    private static final EquipmentSlot[] TOOLTIP_ORDER = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    ArmorSet(final String setName, final Map<EquipmentSlot, Item> items,
             @Nullable final Supplier<Item> medallion,
             final int firstBonusAmount, final int secondBonusAmount,
             @Nullable final Multimap<Holder<Attribute>, AttributeModifier> firstModifiers,
             @Nullable final Multimap<Holder<Attribute>, AttributeModifier> secondModifiers,
             @Nullable final Multimap<Holder<Attribute>, AttributeModifier> fullSetModifiers){
        this.setName =setName;
        this.items= items;
        this.medallion=medallion;
        this.firstBonusAmount=firstBonusAmount;
        this.secondBonusAmount= secondBonusAmount - (medallion==null? 0: (this.medallionCounts()? 0: 1));
        this.setAmount=(this.items.size() + (this.medallionCounts()? 1: 0));
        this.firstModifiers=firstModifiers;
        this.secondModifiers=secondModifiers;
        this.fullSetModifiers=fullSetModifiers;
    }

    public boolean hasBonus(final LivingEntity player, final int amount){
        if(amount==-1) return false;
        return getSetWearingAmount(player) >= (amount);
    }

    public boolean hasFirstBonus(final LivingEntity player){
        return hasBonus(player, firstBonusAmount);
    }

    public boolean hasSecondBonus(final LivingEntity player){
        return hasBonus(player, secondBonusAmount);
    }

    public boolean hasFullBonus(final LivingEntity player){
        return hasBonus(player, setAmount);
    }

    public String getSetName() {
        return setName;
    }

    public int getSetWearingAmount(final LivingEntity player){

        int amount =0;
        for(final EquipmentSlot slot: items.keySet()){
            if(ItemStack.isSameItem(player.getItemBySlot(slot), items.get(slot).getDefaultInstance()))
                amount++;
        }

        if(this.medallionCounts() && isWearingMedallion(player))
            amount++;

        return amount;
    }

    @Environment(EnvType.CLIENT)
    public void addArmorSetTooltip(final List<Component> list, final Player player, final ItemStack stack){
        if (Minecraft.getInstance() != null && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT))) {
            //Space
            list.add(Component.empty());
            //Set Name
            list.add(Component.translatable("item.tcots_witcher.set."+ this.getSetName(), this.getSetWearingAmount(player), this.setAmount)
                    .withStyle(ChatFormatting.GOLD));


            for (final EquipmentSlot slot : TOOLTIP_ORDER) {
                final Item item = items.get(slot);
                if (item == null) continue;

                list.add(CommonComponents.space().append(item.getName(stack))
                        .withStyle(ItemStack.isSameItem(
                                player.getItemBySlot(slot),
                                item.getDefaultInstance()
                        ) ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY)
                );
            }
            if(this.medallionCounts())
                list.add(CommonComponents.space().append(medallion.get().getName(stack)).withStyle(isWearingMedallion(player)? ChatFormatting.GRAY: ChatFormatting.DARK_GRAY));

            //Bonus set tooltips
            if (firstBonusAmount != -1) {
                addBonusTooltip(
                        list,
                        firstModifiers,
                        firstBonusAmount,
                        hasFirstBonus(player),
                        "item.tcots_witcher.set." + getSetName() + ".bonus_first"
                );
            }

            if (secondBonusAmount != -1) {
                addBonusTooltip(
                        list,
                        secondModifiers,
                        secondBonusAmount,
                        hasSecondBonus(player),
                        "item.tcots_witcher.set." + getSetName() + ".bonus_second"
                );
            }

            addBonusTooltip(
                    list,
                    fullSetModifiers,
                    setAmount,
                    hasFullBonus(player),
                    "item.tcots_witcher.set." + getSetName() + ".bonus_full"
            );

        } else {
            list.add(Component.translatable(
                    "item.tcots_witcher.sword.tooltip.see_more", "Left Alt"
            ).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private void addBonusTooltip(
            final List<Component> list,
            @Nullable final Multimap<Holder<Attribute>, AttributeModifier> modifiers,
            final int amount,
            final boolean hasBonus,
            final String fallbackKey
    ) {
        final ChatFormatting color = hasBonus ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY;

        if (modifiers == null) {
            list.add(Component.translatable(fallbackKey, amount).withStyle(color));
            return;
        }

        final MutableComponent text = Component
                .translatable("item.tcots_witcher.armor_set", amount)
                .withStyle(color);

        boolean first = true;

        for (final var entry : modifiers.entries()) {
            final AttributeModifier mod = entry.getValue();
            final double raw = mod.amount();

            if (!first) {
                text.append(CommonComponents.space());
            }

            double value = (mod.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    || mod.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    ? raw * 100.0
                    : raw;

            if (raw < 0) {
                value *= -1.0;
            }

            text.append(Component.translatable(
                                    (raw > 0 ? "attribute.modifier.plus." : "attribute.modifier.take.") + mod.operation().id(),
                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value),
                                    Component.translatable(entry.getKey().value().getDescriptionId())
                            )
                            .withStyle(color)
            );

            first = false;
        }

        list.add(text);
    }


    public boolean isWearingMedallion(final LivingEntity player){
        if(this.medallion!=null) return TCOTS_Util.isWearingAccessoryItem(player, this.medallion.get());

        return false;
    }

    public static void recalculate(final LivingEntity entity) {
        for (final ArmorSet set : values()) {
            set.apply(entity);
        }
    }

    public void apply(final LivingEntity entity) {
        applySet(firstModifiers,  hasFirstBonus(entity),  entity);
        applySet(secondModifiers, hasSecondBonus(entity), entity);
        applySet(fullSetModifiers,hasFullBonus(entity),  entity);
    }

    public boolean medallionCounts(){
        return this.medallion != null && TCOTS_Util.isWitcherRPGAccessories();
    }

    private static void applySet(
            @Nullable final Multimap<Holder<Attribute>, AttributeModifier> modifiers,
            final boolean active,
            final LivingEntity entity
    ) {
        if (modifiers == null) return;
        if (active) addModifiers(modifiers, entity);
        else removeModifiers(modifiers, entity);
    }

    public static void addModifiers(
            final Multimap<Holder<Attribute>, AttributeModifier> modifiers,
            final LivingEntity entity
    ) {
        for (final var entry : modifiers.asMap().entrySet()) {
            final AttributeInstance instance = entity.getAttribute(entry.getKey());
            if (instance == null) continue;

            for (final AttributeModifier modifier : entry.getValue()) {
                if (!instance.hasModifier(modifier.id())) {
                    instance.addOrReplacePermanentModifier(modifier);
                }
            }
        }
    }

    public static void removeModifiers(
            final Multimap<Holder<Attribute>, AttributeModifier> modifiers,
            final LivingEntity entity
    ) {
        for (final var entry : modifiers.asMap().entrySet()) {
            final AttributeInstance instance = entity.getAttribute(entry.getKey());
            if (instance == null) continue;

            for (final AttributeModifier modifier : entry.getValue()) {
                instance.removeModifier(modifier.id());
            }
        }
    }
}

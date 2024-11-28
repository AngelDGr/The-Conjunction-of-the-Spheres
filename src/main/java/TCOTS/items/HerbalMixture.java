package TCOTS.items;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HerbalMixture extends PotionItem {
    public HerbalMixture(Settings settings) {
        super(settings);
    }

    public static final String EFFECTS_KEY = "effects";

    public static ItemStack writeEffects(ItemStack mixture,  Collection<StatusEffectInstance> effects) {

        if (effects.isEmpty()) {
            return mixture;
        }
        NbtCompound nbtCompound = mixture.getOrCreateNbt();
        NbtList nbtList = nbtCompound.getList(EFFECTS_KEY, NbtElement.LIST_TYPE);
        for (StatusEffectInstance statusEffectInstance : effects) {
            nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
        }
        nbtCompound.put(EFFECTS_KEY, nbtList);
        return mixture;
    }

    public static List<StatusEffectInstance> getPotionEffects(@Nullable NbtCompound nbt) {
        ArrayList<StatusEffectInstance> list = Lists.newArrayList();
        HerbalMixture.getEffects(nbt, list);
        return list;
    }
    private static final Text NONE_TEXT = Text.translatable("effect.none").formatted(Formatting.GRAY);

    public static void getEffects(@Nullable NbtCompound nbt, List<StatusEffectInstance> list) {
        if (nbt != null && nbt.contains(EFFECTS_KEY, NbtElement.LIST_TYPE)) {
            NbtList nbtList = nbt.getList(EFFECTS_KEY, NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound = nbtList.getCompound(i);
                StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromNbt(nbtCompound);
                if (statusEffectInstance == null) continue;
                list.add(statusEffectInstance);
            }
        }
    }

    public static void buildTooltip(List<StatusEffectInstance> statusEffects, List<Text> list, float durationMultiplier) {
        ArrayList<Pair<EntityAttribute, EntityAttributeModifier>> list2 = Lists.newArrayList();
        if (statusEffects.isEmpty()) {
            list.add(NONE_TEXT);
        } else {
            for (StatusEffectInstance statusEffectInstance : statusEffects) {
                MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
                StatusEffect statusEffect = statusEffectInstance.getEffectType();
                Map<EntityAttribute, EntityAttributeModifier> map = statusEffect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entrySet()) {
                        EntityAttributeModifier entityAttributeModifier = entry.getValue();
                        EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(
                                entityAttributeModifier.getName(),
                                statusEffect.adjustModifierAmount(statusEffectInstance.getAmplifier(), entityAttributeModifier),
                                entityAttributeModifier.getOperation()
                        );
                        list2.add(new Pair<>(entry.getKey(), entityAttributeModifier2));
                    }
                }

                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                }

                if (!statusEffectInstance.isDurationBelow(20)) {
                    mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, durationMultiplier));
                }

                list.add(mutableText.formatted(statusEffect.getCategory().getFormatting()));
            }
        }
        if (!list2.isEmpty()) {
            list.add(ScreenTexts.EMPTY);
            list.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));
            for (Pair<?,?> pair : list2) {
                EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)pair.getSecond();
                double d = entityAttributeModifier.getValue();
                double e = entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE || entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? entityAttributeModifier.getValue() * 100.0 : entityAttributeModifier.getValue();
                if (d > 0.0) {
                    list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(((EntityAttribute)pair.getFirst()).getTranslationKey())).formatted(Formatting.BLUE));
                    continue;
                }
                if (!(d < 0.0)) continue;
                list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e * -1.0), Text.translatable(((EntityAttribute)pair.getFirst()).getTranslationKey())).formatted(Formatting.RED));
            }
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (this.isFood()) {
            user.eatFood(world, stack);
        }

        //Applies Effects
        if (!world.isClient) {
            List<StatusEffectInstance> list = HerbalMixture.getPotionEffects(stack.getNbt());
            for (StatusEffectInstance statusEffectInstance : list) {
                if (statusEffectInstance.getEffectType().isInstant()) {
                    statusEffectInstance.getEffectType().applyInstantEffect(user, user, user, statusEffectInstance.getAmplifier(), 1.0);
                    continue;
                }
                user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
            }
        }

        //Return glass bottle
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        if (user instanceof PlayerEntity playerEntity) {
            if (!playerEntity.getAbilities().creativeMode) {
                ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
                if (!playerEntity.getInventory().insertStack(itemStack)) {
                    playerEntity.dropItem(itemStack, false);
                }
            }
        }

        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        ArrayList<StatusEffectInstance> list = new ArrayList<>();

        HerbalMixture.getEffects(stack.getNbt(), list);

        HerbalMixture.buildTooltip(list, tooltip, 1.0f);
    }

    @Override
    public SoundEvent getDrinkSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ITEM_HONEY_BOTTLE_DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 42;
    }
}

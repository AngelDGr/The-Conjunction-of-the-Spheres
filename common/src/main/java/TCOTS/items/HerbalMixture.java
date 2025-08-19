package TCOTS.items;

import TCOTS.items.components.CustomEffectsComponent;
import TCOTS.registry.TCOTS_Items;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HerbalMixture extends PotionItem {
    public HerbalMixture(final Properties settings) {
        super(settings);
    }

    public static ItemStack writeEffects(final ItemStack mixture, final List<MobEffectInstance> effects) {
        CustomEffectsComponent.of(mixture, effects);
        return mixture;
    }

    private static final Component NONE_TEXT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

    public static void buildTooltip(final Iterable<MobEffectInstance> effects, final Consumer<Component> textConsumer, final float durationMultiplier, final float tickRate) {
        final List<Pair<Holder<Attribute>, AttributeModifier>> list = Lists.newArrayList();
        boolean bl = true;

        for (final MobEffectInstance statusEffectInstance : effects) {
            bl = false;
            MutableComponent mutableText = Component.translatable(statusEffectInstance.getDescriptionId());
            final Holder<MobEffect> registryEntry = statusEffectInstance.getEffect();
            registryEntry.value().createModifiers(statusEffectInstance.getAmplifier(), (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
            if (statusEffectInstance.getAmplifier() > 0) {
                mutableText = Component.translatable("potion.withAmplifier", mutableText, Component.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
            }

            if (!statusEffectInstance.endsWithin(20)) {
                mutableText = Component.translatable("potion.withDuration", mutableText, MobEffectUtil.formatDuration(statusEffectInstance, durationMultiplier, tickRate));
            }

            textConsumer.accept(mutableText.withStyle(registryEntry.value().getCategory().getTooltipFormatting()));
        }

        if (bl) {
            textConsumer.accept(NONE_TEXT);
        }

        if (!list.isEmpty()) {
            textConsumer.accept(CommonComponents.EMPTY);
            textConsumer.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

            for (final Pair<Holder<Attribute>, AttributeModifier> pair : list) {
                final AttributeModifier entityAttributeModifier = pair.getSecond();
                final double d = entityAttributeModifier.amount();
                double e;
                if (entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = entityAttributeModifier.amount();
                } else {
                    e = entityAttributeModifier.amount() * 100.0;
                }

                if (d > 0.0) {
                    textConsumer.accept(
                            Component.translatable(
                                            "attribute.modifier.plus." + entityAttributeModifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.BLUE)
                    );
                } else if (d < 0.0) {
                    e *= -1.0;
                    textConsumer.accept(
                            Component.translatable(
                                            "attribute.modifier.take." + entityAttributeModifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.RED)
                    );
                }
            }
        }
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull final UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull final ItemStack stack) {
        return this.getDescriptionId();
    }

    @SuppressWarnings("all")
    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, @NotNull Level world, @NotNull LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player)user : null;
        if (stack.has(DataComponents.FOOD)) {
            user.eat(world, stack, stack.get(DataComponents.FOOD));
        }

        if (playerEntity instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);
        }

        //Applies Effects
        if (!world.isClientSide) {
            CustomEffectsComponent customEffects = stack.getOrDefault(TCOTS_Items.CustomEffects(), CustomEffectsComponent.DEFAULT);
            for(MobEffectInstance effect : customEffects.customEffects()) {
                if(effect.getEffect().value().isInstantenous()){
                    effect.getEffect().value().applyInstantenousEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
                }
                else{
                    user.addEffect(new MobEffectInstance(effect));
                }
            }
        }

        //Return glass bottle
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        if (user instanceof Player playerEntity2) {
            if (!playerEntity2.getAbilities().instabuild) {
                ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
                if (!playerEntity2.getInventory().add(itemStack)) {
                    playerEntity2.drop(itemStack, false);
                }
            }
        }

        return stack;
    }

    @Override
    public void appendHoverText(final ItemStack stack, @NotNull final TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag type) {
        final CustomEffectsComponent customEffectsComponent = stack.get(TCOTS_Items.CustomEffects());
        if (customEffectsComponent != null) {
            buildTooltip(customEffectsComponent.customEffects(),tooltip::add, 1.0F, context.tickRate());
        } else {
            tooltip.add(NONE_TEXT);
        }
    }

    @Override
    public @NotNull SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public int getUseDuration(@NotNull final ItemStack stack, @NotNull final LivingEntity user) {
        return 42;
    }
}

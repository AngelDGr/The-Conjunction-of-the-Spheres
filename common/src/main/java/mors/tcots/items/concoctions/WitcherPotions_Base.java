package mors.tcots.items.concoctions;

import mors.tcots.registry.TCOTS_Effects;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.effects.WitcherPotionEffect;
import mors.tcots.utils.tooltip.FontHelper;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WitcherPotions_Base extends PotionItem {
    private final MobEffectInstance effectInstance;
    private final int toxicity;
    protected final boolean decoction;

    public WitcherPotions_Base(final Properties settings, final Holder<MobEffect> effect, final int toxicity, final int durationInSecs, final int amplifier, final boolean decoction){
        super(settings);
        this.effectInstance=new MobEffectInstance(effect, (int)(durationInSecs/0.05), amplifier);
        this.toxicity=toxicity;
        this.decoction=decoction;
    }

    public WitcherPotions_Base(final Properties settings, final ResourceLocation effect, final int toxicity, final int durationInSecs, final int amplifier, final boolean decoction){
        super(settings);
        this.effectInstance=new MobEffectInstance(TCOTS_Effects.getHolder(effect), (int)(durationInSecs/0.05), amplifier);
        this.toxicity=toxicity;
        this.decoction=decoction;
    }

    public WitcherPotions_Base(final Properties settings, final MobEffectInstance effect, final int toxicity, final boolean decoction){
        super(settings);
        this.effectInstance=effect;
        this.toxicity=toxicity;
        this.decoction=decoction;
    }

    @Override
    public @NotNull Component getName(@NotNull final ItemStack stack) {
        if(this.isDecoction()){
         return Component.translatable(this.getDescriptionId()).withColor(0x41d331);
        }
        else {
            return super.getName(stack);
        }
    }

    public MobEffectInstance getStatusEffect(){
        return effectInstance;
    }

    @Override
    public void onUseTick(@NotNull final Level world, @NotNull final LivingEntity user, @NotNull final ItemStack stack, final int remainingUseTicks) {
        super.onUseTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull final ItemStack stack, @NotNull final Level world, @NotNull final LivingEntity user) {
        final Player playerEntity = user instanceof Player ? (Player)user : null;
        if (playerEntity instanceof ServerPlayer) {
            //Add toxicity
            playerEntity.tcots$addToxicity(getToxicity(),decoction);

            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);
        }

        if (!world.isClientSide) {
            for(final MobEffectInstance effect : this.getPotionEffects()) {
                if(effect.getEffect().value().isInstantenous()){
                    effect.getEffect().value().applyInstantenousEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
                }
                else{
                    user.addEffect(new MobEffectInstance(effect));
                }
            }
        }

        if (playerEntity != null) {
            playerEntity.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, playerEntity);
        }

        user.gameEvent(GameEvent.DRINK);

        final ItemStack stack_Empty = getStackEmptyBottle(this);

        if (stack.isEmpty()) {
            return stack_Empty;
        }

        if (user instanceof Player) {
            if (!playerEntity.getAbilities().instabuild ) {
                if (!playerEntity.getInventory().add(stack_Empty)) {
                    playerEntity.drop(stack_Empty, false);
                }
            }
        }

        return stack;
    }

    @NotNull
    public static ItemStack getStackEmptyBottle(final WitcherPotions_Base item) {
        ItemStack stack_Empty=new ItemStack(TCOTS_Items.EMPTY_WITCHER_POTION.get());

        if(!item.decoction){
            stack_Empty.set(DataComponents.MAX_STACK_SIZE, item.getDefaultMaxStackSize());
        }
        else {
            stack_Empty = new ItemStack(TCOTS_Items.EMPTY_MONSTER_DECOCTION.get());
        }

        stack_Empty.set(TCOTS_Items.RefillRecipe(), BuiltInRegistries.ITEM.getKey(item).toString());

        return stack_Empty;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(final ItemStack stack) {
        if(stack.getItem() instanceof final WitcherPotions_Base potion){
            if(!potion.canBeDrunk){
                return UseAnim.NONE;
            }
        }

        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull final UseOnContext context) {
        return InteractionResult.PASS;
    }
    private boolean canBeDrunk;

    public void setCanBeDrunk(final boolean canBeDrunk) {
        this.canBeDrunk = canBeDrunk;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull final Level world, @NotNull final Player user, @NotNull final InteractionHand hand) {
        if(user instanceof final ServerPlayer player){
            if(player.tcots$setMaxToxicity()<(player.tcots$getAllToxicity()+getToxicity())){
                setCanBeDrunk(false);
                player.displayClientMessage(Component.translatable("gui.tcots_witcher.message.toxicity_warning").withStyle(ChatFormatting.DARK_GREEN), true);
                return InteractionResultHolder.fail(player.getItemInHand(hand));
            }
        }

        setCanBeDrunk(true);
        return super.use(world, user, hand);
    }

    public List<MobEffectInstance> getPotionEffects() {
        final List<MobEffectInstance> list = Lists.newArrayList();
        list.add(getStatusEffect());
        return list;
    }

    private void buildMainTooltip(final List<Component> tooltip, final float tickRate) {
        final ArrayList<Pair<Holder<Attribute>, AttributeModifier>> tooltipAttributes = Lists.newArrayList();
        for (final MobEffectInstance statusEffectInstance : getPotionEffects()) {
            MutableComponent mutableText = Component.translatable(statusEffectInstance.getDescriptionId());
            final Holder<MobEffect> registryEntry = statusEffectInstance.getEffect();
            registryEntry.value().createModifiers(statusEffectInstance.getAmplifier(), (attribute, modifier) ->
                    tooltipAttributes.add(new Pair<>(attribute, modifier)));
            if (statusEffectInstance.getAmplifier() > 0) {
                mutableText = Component.translatable("potion.withAmplifier", mutableText, Component.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
            }

            if (!statusEffectInstance.endsWithin(20)) {
                mutableText = Component.translatable("potion.withDuration", mutableText, MobEffectUtil.formatDuration(statusEffectInstance, 1, tickRate));
            }


            tooltip.add(mutableText.withStyle(registryEntry.value().getCategory().getTooltipFormatting()));
        }

        tooltip.add(Component.translatable("item.tcots_witcher.potion.tooltip.toxicity", getToxicity()).withStyle(ChatFormatting.DARK_GREEN));

        if(effectInstance.getEffect().value() instanceof final WitcherPotionEffect witcherPotionEffect){
            final int amplifier = this.getStatusEffect().getAmplifier();

            tooltip.addAll(FontHelper.cutTextComponent(
                    Component.translatable(
                            this.getStatusEffect().getEffect().value().getDescriptionId()+".tooltip" + (witcherPotionEffect.hasPerLevelDescription()? amplifier: "")
                    ),
                    FontHelper.Palette.GRAY_AND_RED));
        }

        //Attributes tooltip
        if (!tooltipAttributes.isEmpty()) {
            tooltip.add(CommonComponents.EMPTY);
            if(((WitcherPotionEffect)(effectInstance.getEffect().value())).hasCustomApplyTooltip()){
                    tooltip.add(Component.translatable(effectInstance.getEffect().value().getDescriptionId() +".tooltip.when_applied").withStyle(ChatFormatting.DARK_PURPLE));
            }
            else {tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));}

            for (final Pair<Holder<Attribute>, AttributeModifier> pair : tooltipAttributes) {
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
                    tooltip.add(
                            Component.translatable(
                                            "attribute.modifier.plus." + entityAttributeModifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.BLUE)
                    );
                } else if (d < 0.0) {
                    e *= -1.0;
                    tooltip.add(
                            Component.translatable(
                                            "attribute.modifier.take." + entityAttributeModifier.operation().id(),
                                            ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e),
                                            Component.translatable(pair.getFirst().value().getDescriptionId())
                                    )
                                    .withStyle(ChatFormatting.RED)
                    );
                }
            }

            //Special attribute (To mix special and normal attributes)
            if(effectInstance.getEffect().value() instanceof final WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.specialAttributesValue(0)!=-1){
                tooltip.add(Component.translatable(witcherPotionEffect.getDescriptionId()+".tooltip.special_attribute",witcherPotionEffect.specialAttributesValue(effectInstance.getAmplifier())).withStyle(ChatFormatting.BLUE));
            }
        }


        //Special Tooltip
        else if(effectInstance.getEffect().value() instanceof final WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.specialAttributesValue(0)!=-1){
            tooltip.add(CommonComponents.EMPTY);
            if(witcherPotionEffect.hasCustomApplyTooltip()){
                tooltip.add(Component.translatable(witcherPotionEffect.getDescriptionId() +".tooltip.when_applied").withStyle(ChatFormatting.DARK_PURPLE));
            }
            else {tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));}

            tooltip.add(Component.translatable(witcherPotionEffect.getDescriptionId()+".tooltip.special_attribute",witcherPotionEffect.specialAttributesValue(effectInstance.getAmplifier())).withStyle(ChatFormatting.BLUE));
        }

    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(@NotNull final ItemStack stack, final TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag type) {
        buildMainTooltip(tooltip, context.tickRate());
    }

    public int getToxicity(){
        return this.toxicity;
    }

    public boolean isDecoction() {
        return decoction;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    @Override
    public @NotNull String getDescriptionId(@NotNull final ItemStack stack) {
        return this.getDescriptionId();
    }
}

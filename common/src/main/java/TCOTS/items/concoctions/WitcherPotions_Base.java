package TCOTS.items.concoctions;

import TCOTS.registry.TCOTS_Effects;
import TCOTS.registry.TCOTS_Items;
import TCOTS.effects.WitcherPotionEffect;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
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

    public WitcherPotions_Base(Properties settings, Holder<MobEffect> effect, int toxicity, int durationInSecs, int amplifier, boolean decoction){
        super(settings);
        this.effectInstance=new MobEffectInstance(effect, (int)(durationInSecs/0.05), amplifier);
        this.toxicity=toxicity;
        this.decoction=decoction;
    }

    public WitcherPotions_Base(Properties settings, ResourceLocation effect, int toxicity, int durationInSecs, int amplifier, boolean decoction){
        super(settings);
        this.effectInstance=new MobEffectInstance(TCOTS_Effects.getHolder(effect), (int)(durationInSecs/0.05), amplifier);
        this.toxicity=toxicity;
        this.decoction=decoction;
    }

    public WitcherPotions_Base(Properties settings, MobEffectInstance effect, int toxicity, boolean decoction){
        super(settings);
        this.effectInstance=effect;
        this.toxicity=toxicity;
        this.decoction=decoction;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
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
    public void onUseTick(@NotNull Level world, @NotNull LivingEntity user, @NotNull ItemStack stack, int remainingUseTicks) {
        super.onUseTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player)user : null;
        if (playerEntity instanceof ServerPlayer) {
            //Add toxicity
            playerEntity.theConjunctionOfTheSpheres$addToxicity(getToxicity(),decoction);

            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);
        }

        if (!world.isClientSide) {
            for(MobEffectInstance effect : this.getPotionEffects()) {
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

        ItemStack stack_Empty = getStackEmptyBottle(this);

        if (stack.isEmpty()) {
            return stack_Empty;
        }

        if (user instanceof Player) {
            if (!playerEntity.getAbilities().instabuild) {
                if (!playerEntity.getInventory().add(stack_Empty)) {
                    playerEntity.drop(stack_Empty, false);
                }
            }
        }

        return stack;
    }

    @NotNull
    public static ItemStack getStackEmptyBottle(WitcherPotions_Base item) {
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
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        if(stack.getItem() instanceof WitcherPotions_Base potion){
            if(!potion.canBeDrunk){
                return UseAnim.NONE;
            }
        }

        return UseAnim.DRINK;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        return InteractionResult.PASS;
    }
    private boolean canBeDrunk;

    public void setCanBeDrunk(boolean canBeDrunk) {
        this.canBeDrunk = canBeDrunk;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        if(user instanceof ServerPlayer player){
            if(player.theConjunctionOfTheSpheres$getMaxToxicity()<(player.theConjunctionOfTheSpheres$getAllToxicity()+getToxicity())){
                setCanBeDrunk(false);
                player.displayClientMessage(Component.translatable("tcots_witcher.gui.toxicity_warning").withStyle(ChatFormatting.DARK_GREEN), true);
                return InteractionResultHolder.fail(player.getItemInHand(hand));
            }
        }

        setCanBeDrunk(true);
        return super.use(world, user, hand);
    }

    public List<MobEffectInstance> getPotionEffects() {
        List<MobEffectInstance> list = Lists.newArrayList();
        list.add(getStatusEffect());
        return list;
    }

    private void buildMainTooltip(List<Component> tooltip, float tickRate) {
        ArrayList<Pair<Holder<Attribute>, AttributeModifier>> tooltipAttributes = Lists.newArrayList();
        for (MobEffectInstance statusEffectInstance : getPotionEffects()) {
            MutableComponent mutableText = Component.translatable(statusEffectInstance.getDescriptionId());
            Holder<MobEffect> registryEntry = statusEffectInstance.getEffect();
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


        tooltip.add(Component.translatable("tcots_witcher.tooltip.toxicity", getToxicity()).withStyle(ChatFormatting.DARK_GREEN));

        if(effectInstance.getEffect().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasExtraInfo()){
            int n = this.getStatusEffect().getAmplifier() == 0? 0: 1;
            tooltip.add(Component.translatable("tooltip."+this.getStatusEffect().getEffect().value().getDescriptionId()+".first." +n).withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip."+this.getStatusEffect().getEffect().value().getDescriptionId()+".second."+n).withStyle(ChatFormatting.GRAY));
        } else{
            tooltip.add(Component.translatable("tooltip."+this.getStatusEffect().getEffect().value().getDescriptionId()+".first").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip."+this.getStatusEffect().getEffect().value().getDescriptionId()+".second").withStyle(ChatFormatting.GRAY));
        }

        if(effectInstance.getEffect().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasExtraLine(effectInstance.getAmplifier())){
            tooltip.add(Component.translatable("tooltip."+this.getStatusEffect().getEffect().value().getDescriptionId()+".extra").withStyle(ChatFormatting.GRAY));
        }


        //Attributes tooltip
        if (!tooltipAttributes.isEmpty()) {
            tooltip.add(CommonComponents.EMPTY);
            if(((WitcherPotionEffect)(effectInstance.getEffect().value())).hasCustomApplyTooltip()){
                    tooltip.add(Component.translatable("tooltip." + effectInstance.getEffect().value().getDescriptionId() +".applied").withStyle(ChatFormatting.DARK_PURPLE));
            }
            else {tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));}

            for (Pair<Holder<Attribute>, AttributeModifier> pair : tooltipAttributes) {
                AttributeModifier entityAttributeModifier = pair.getSecond();
                double d = entityAttributeModifier.amount();
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
            if(effectInstance.getEffect().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasSpecialAttributes()){
                tooltip.add(Component.translatable("special.attribute."+witcherPotionEffect.getDescriptionId(),witcherPotionEffect.getSpecialAttributesValue(effectInstance.getAmplifier())).withStyle(ChatFormatting.BLUE));
            }
        }


        //Special Tooltip
        else if(effectInstance.getEffect().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasSpecialAttributes()){
            tooltip.add(CommonComponents.EMPTY);
            if(witcherPotionEffect.hasCustomApplyTooltip()){
                tooltip.add(Component.translatable("tooltip." + witcherPotionEffect.getDescriptionId() +".applied").withStyle(ChatFormatting.DARK_PURPLE));
            }
            else {tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));}

            tooltip.add(Component.translatable("special.attribute."+witcherPotionEffect.getDescriptionId(),witcherPotionEffect.getSpecialAttributesValue(effectInstance.getAmplifier())).withStyle(ChatFormatting.BLUE));
        }

    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
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
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return this.getDescriptionId();
    }
}

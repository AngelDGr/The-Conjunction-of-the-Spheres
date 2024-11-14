package TCOTS.items.concoctions;

import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PotionItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class WitcherPotions_Base extends PotionItem {
    private final StatusEffectInstance effectInstance;
    private final int toxicity;
    protected final boolean decoction;

    public WitcherPotions_Base(Settings settings, StatusEffectInstance effect, int toxicity, boolean decoction){
        super(settings);
        this.effectInstance=effect;
        this.toxicity=toxicity;
        this.decoction=decoction;
    }

    @Override
    public Text getName(ItemStack stack) {
        if(this.isDecoction()){
         return Text.translatable(this.getTranslationKey()).withColor(0x41d331);
        }
        else {
            return super.getName(stack);
        }
    }

    public StatusEffectInstance getStatusEffect(){
        return effectInstance;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            //Add toxicity
            playerEntity.theConjunctionOfTheSpheres$addToxicity(getToxicity(),decoction);

            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            for(StatusEffectInstance effect : this.getPotionEffects()) {
                if(effect.getEffectType().value().isInstant()){
                    effect.getEffectType().value().applyInstantEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
                }
                else{
                    user.addStatusEffect(new StatusEffectInstance(effect));
                }
            }
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            stack.decrementUnlessCreative(1, playerEntity);
        }

        user.emitGameEvent(GameEvent.DRINK);

        ItemStack stack_Empty = getStackEmptyBottle(this);

        if (stack.isEmpty()) {
            return stack_Empty;
        }

        if (user instanceof PlayerEntity) {
            if (!playerEntity.getAbilities().creativeMode) {
                if (!playerEntity.getInventory().insertStack(stack_Empty)) {
                    playerEntity.dropItem(stack_Empty, false);
                }
            }
        }

        return stack;
    }

    @NotNull
    public static ItemStack getStackEmptyBottle(WitcherPotions_Base item) {
        ItemStack stack_Empty=new ItemStack(TCOTS_Items.EMPTY_WITCHER_POTION);

        if(!item.decoction){
            stack_Empty.set(DataComponentTypes.MAX_STACK_SIZE, item.getMaxCount());
        }
        else {
            stack_Empty = new ItemStack(TCOTS_Items.EMPTY_MONSTER_DECOCTION);
        }

        stack_Empty.set(TCOTS_Items.REFILL_RECIPE, Registries.ITEM.getId(item).toString());

        return stack_Empty;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if(stack.getItem() instanceof WitcherPotions_Base potion){
            if(!potion.canBeDrunk){
                return UseAction.NONE;
            }
        }

        return UseAction.DRINK;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.PASS;
    }
    private boolean canBeDrunk;

    public void setCanBeDrunk(boolean canBeDrunk) {
        this.canBeDrunk = canBeDrunk;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user instanceof ServerPlayerEntity player){
            if(player.theConjunctionOfTheSpheres$getMaxToxicity()<(player.theConjunctionOfTheSpheres$getAllToxicity()+getToxicity())){
                setCanBeDrunk(false);
                player.sendMessage(Text.translatable("tcots-witcher.gui.toxicity_warning").formatted(Formatting.DARK_GREEN), true);
                return TypedActionResult.fail(player.getStackInHand(hand));
            }
        }

        setCanBeDrunk(true);
        return super.use(world, user, hand);
    }

    public List<StatusEffectInstance> getPotionEffects() {
        List<StatusEffectInstance> list = Lists.newArrayList();
        list.add(getStatusEffect());
        return list;
    }

    private void buildMainTooltip(List<Text> tooltip, float tickRate) {
        ArrayList<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> tooltipAttributes = Lists.newArrayList();
        for (StatusEffectInstance statusEffectInstance : getPotionEffects()) {
            MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
            RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
            registryEntry.value().forEachAttributeModifier(statusEffectInstance.getAmplifier(), (attribute, modifier) ->
                    tooltipAttributes.add(new Pair<>(attribute, modifier)));
            if (statusEffectInstance.getAmplifier() > 0) {
                mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
            }

            if (!statusEffectInstance.isDurationBelow(20)) {
                mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, 1, tickRate));
            }


            tooltip.add(mutableText.formatted(registryEntry.value().getCategory().getFormatting()));
        }


        tooltip.add(Text.translatable("tcots-witcher.tooltip.toxicity", getToxicity()).formatted(Formatting.DARK_GREEN));

        if(effectInstance.getEffectType().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasExtraInfo()){
            int n = this.getStatusEffect().getAmplifier() == 0? 0: 1;
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().value().getTranslationKey()+".first." +n).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().value().getTranslationKey()+".second."+n).formatted(Formatting.GRAY));
        } else{
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().value().getTranslationKey()+".first").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().value().getTranslationKey()+".second").formatted(Formatting.GRAY));
        }

        if(effectInstance.getEffectType().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasExtraLine(effectInstance.getAmplifier())){
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().value().getTranslationKey()+".extra").formatted(Formatting.GRAY));
        }


        //Attributes tooltip
        if (!tooltipAttributes.isEmpty()) {
            tooltip.add(ScreenTexts.EMPTY);
            if(((WitcherPotionEffect)(effectInstance.getEffectType().value())).hasCustomApplyTooltip()){
                    tooltip.add(Text.translatable("tooltip." + effectInstance.getEffectType().value().getTranslationKey() +".applied").formatted(Formatting.DARK_PURPLE));
            }
            else {tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));}

            for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : tooltipAttributes) {
                EntityAttributeModifier entityAttributeModifier = pair.getSecond();
                double d = entityAttributeModifier.value();
                double e;
                if (entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        && entityAttributeModifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                    e = entityAttributeModifier.value();
                } else {
                    e = entityAttributeModifier.value() * 100.0;
                }

                if (d > 0.0) {
                    tooltip.add(
                            Text.translatable(
                                            "attribute.modifier.plus." + entityAttributeModifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(pair.getFirst().value().getTranslationKey())
                                    )
                                    .formatted(Formatting.BLUE)
                    );
                } else if (d < 0.0) {
                    e *= -1.0;
                    tooltip.add(
                            Text.translatable(
                                            "attribute.modifier.take." + entityAttributeModifier.operation().getId(),
                                            AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                            Text.translatable(pair.getFirst().value().getTranslationKey())
                                    )
                                    .formatted(Formatting.RED)
                    );
                }
            }

            //Special attribute (To mix special and normal attributes)
            if(effectInstance.getEffectType().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasSpecialAttributes()){
                tooltip.add(Text.translatable("special.attribute."+witcherPotionEffect.getTranslationKey(),witcherPotionEffect.getSpecialAttributesValue(effectInstance.getAmplifier())).formatted(Formatting.BLUE));
            }
        }


        //Special Tooltip
        else if(effectInstance.getEffectType().value() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasSpecialAttributes()){
            tooltip.add(ScreenTexts.EMPTY);
            if(((WitcherPotionEffect) effectInstance.getEffectType().value()).hasCustomApplyTooltip()){
                tooltip.add(Text.translatable("tooltip." + witcherPotionEffect.getTranslationKey() +".applied").formatted(Formatting.DARK_PURPLE));
            }
            else {tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));}

            tooltip.add(Text.translatable("special.attribute."+witcherPotionEffect.getTranslationKey(),witcherPotionEffect.getSpecialAttributesValue(effectInstance.getAmplifier())).formatted(Formatting.BLUE));
        }

    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        buildMainTooltip(tooltip, context.getUpdateTickRate());
    }

    public int getToxicity(){
        return this.toxicity;
    }

    public boolean isDecoction() {
        return decoction;
    }

    @Override
    public ItemStack getDefaultStack() {
        return new ItemStack(this);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }
}

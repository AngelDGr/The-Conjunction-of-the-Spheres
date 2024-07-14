package TCOTS.items.potions;

import TCOTS.items.TCOTS_Items;
import TCOTS.items.potions.effects.WitcherPotionEffect;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeModifierCreator;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    @Override
    public Rarity getRarity(ItemStack stack) {
        return switch (effectInstance.getAmplifier()) {
            case 0 -> Rarity.COMMON;
            case 1, 2 -> Rarity.UNCOMMON;
            default -> super.getRarity(stack);
        };
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
            if(getStatusEffect().getEffectType().isInstant()){
                this.getStatusEffect().getEffectType().applyInstantEffect(playerEntity, playerEntity, user, this.getStatusEffect().getAmplifier(), 1.0);}
            else{user.addStatusEffect(new StatusEffectInstance(getStatusEffect()));}
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        ItemStack stack_Empty = getStackEmpty();

        stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (playerEntity != null) {
                //If the player inventories its full
                if(playerEntity.getInventory().getEmptySlot() == -1){
                    playerEntity.getWorld().spawnEntity(new ItemEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), stack_Empty));
                } else{
                    playerEntity.getInventory().insertStack(stack_Empty);
                }
            }
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    @NotNull
    private ItemStack getStackEmpty() {
        ItemStack stack_Empty;

        if(!decoction){
            stack_Empty = switch (this.getMaxCount()) {
                default -> new ItemStack(TCOTS_Items.EMPTY_WITCHER_POTION_2);
                case 3 -> new ItemStack(TCOTS_Items.EMPTY_WITCHER_POTION_3);
                case 4 -> new ItemStack(TCOTS_Items.EMPTY_WITCHER_POTION_4);
                case 5 -> new ItemStack(TCOTS_Items.EMPTY_WITCHER_POTION_5);
            };
        }
        else {stack_Empty = new ItemStack(TCOTS_Items.EMPTY_MONSTER_DECOCTION);}
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
        ArrayList<Pair<EntityAttribute, EntityAttributeModifier>> tooltipAttributes = Lists.newArrayList();
            for (StatusEffectInstance statusEffectInstance : getPotionEffects()) {
                MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
                StatusEffect statusEffect = statusEffectInstance.getEffectType();

                Map<EntityAttribute, AttributeModifierCreator> map = statusEffect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : map.entrySet()) {
                        tooltipAttributes.add(new Pair<>(entry.getKey(), entry.getValue().createAttributeModifier(statusEffectInstance.getAmplifier())));
                    }
                }

                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                }
                if (!statusEffectInstance.isDurationBelow(20)) {
                    mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, (float) 1.0, tickRate));
                }
                tooltip.add(mutableText.formatted(statusEffect.getCategory().getFormatting()));
            }


        tooltip.add(Text.translatable("tcots-witcher.tooltip.toxicity", getToxicity()).formatted(Formatting.DARK_GREEN));

        if(effectInstance.getEffectType() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasExtraInfo()){
            int n = this.getStatusEffect().getAmplifier() == 0? 0: 1;
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".first." +n).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".second."+n).formatted(Formatting.GRAY));
        } else{
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".first").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".second").formatted(Formatting.GRAY));
        }

        if(effectInstance.getEffectType() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasExtraLine(effectInstance.getAmplifier())){
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".extra").formatted(Formatting.GRAY));
        }

        //Special Tooltip
        if(effectInstance.getEffectType() instanceof WitcherPotionEffect witcherPotionEffect && witcherPotionEffect.hasSpecialAttributes()){
            tooltip.add(ScreenTexts.EMPTY);
            if(((WitcherPotionEffect) effectInstance.getEffectType()).hasCustomApplyTooltip()){
                tooltip.add(Text.translatable("tooltip." + witcherPotionEffect.getTranslationKey() +".applied").formatted(Formatting.DARK_PURPLE));
            }
            else {tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));}

            tooltip.add(Text.translatable("special.attribute."+witcherPotionEffect.getTranslationKey(),witcherPotionEffect.getSpecialAttributesValue(effectInstance.getAmplifier())).formatted(Formatting.BLUE));
        }


        //Attributes tooltip
        if (!tooltipAttributes.isEmpty()) {
            tooltip.add(ScreenTexts.EMPTY);
            if(((WitcherPotionEffect) effectInstance.getEffectType()).hasCustomApplyTooltip()){
                    tooltip.add(Text.translatable("tooltip." + effectInstance.getEffectType().getTranslationKey() +".applied").formatted(Formatting.DARK_PURPLE));
            }
            else {tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));}

            for (Pair<?,?> pair : tooltipAttributes) {
                EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)pair.getSecond();
                double d = entityAttributeModifier.getValue();
                double e = entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE || entityAttributeModifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? entityAttributeModifier.getValue() * 100.0 : entityAttributeModifier.getValue();
                if (d > 0.0) {
                    tooltip.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(((EntityAttribute)pair.getFirst()).getTranslationKey())).formatted(Formatting.BLUE));
                    continue;
                }
                if (!(d < 0.0)) continue;
                tooltip.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e * -1.0), Text.translatable(((EntityAttribute)pair.getFirst()).getTranslationKey())).formatted(Formatting.RED));
            }
        }

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        buildMainTooltip(tooltip, world == null ? 20.0f : world.getTickManager().getTickRate());
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

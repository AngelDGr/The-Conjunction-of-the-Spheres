package TCOTS.items.potions;

import TCOTS.items.TCOTS_Items;
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
    StatusEffectInstance effectInstance;
    int toxicity;
    boolean decoction;

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

    @SuppressWarnings("unused")
    public void buildTooltip(ItemStack stack, List<Text> list, float durationMultiplier, float tickRate) {
        buildTooltip(getPotionEffects(), list, durationMultiplier, tickRate);
    }

    @SuppressWarnings({"rawtypes", "unused"})
    public void buildTooltip(List<StatusEffectInstance> statusEffects, List<Text> list, float durationMultiplier, float tickRate) {
        ArrayList<Pair<EntityAttribute, EntityAttributeModifier>> list2 = Lists.newArrayList();
            for (StatusEffectInstance statusEffectInstance : statusEffects) {
                MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
                StatusEffect statusEffect = statusEffectInstance.getEffectType();
                Map<EntityAttribute, AttributeModifierCreator> map = statusEffect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : map.entrySet()) {
                        list2.add(new Pair<>(entry.getKey(), entry.getValue().createAttributeModifier(statusEffectInstance.getAmplifier())));
                    }
                }
                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                }
                if (!statusEffectInstance.isDurationBelow(20)) {
                    mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, durationMultiplier, tickRate));
                }
                list.add(mutableText.formatted(statusEffect.getCategory().getFormatting()));
            }
//        }
        if (!list2.isEmpty()) {

            for (Pair pair : list2) {
                list.add(Text.translatable("tooltip." + effectInstance.getEffectType().getTranslationKey() +".applied").formatted(Formatting.DARK_PURPLE));

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


    private final List<StatusEffect> listExtraTooltip=List.of(
            TCOTS_Effects.BLACK_BLOOD_EFFECT,
            TCOTS_Effects.WHITE_RAFFARDS_EFFECT
    );
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        buildTooltip(stack, tooltip, 1.0F,  world == null ? 20.0f : world.getTickManager().getTickRate());
        tooltip.add(Text.translatable("tcots-witcher.tooltip.toxicity", getToxicity()).formatted(Formatting.DARK_GREEN));
        if(listExtraTooltip.contains(effectInstance.getEffectType())){
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".first."+this.getStatusEffect().getAmplifier()).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".second."+this.getStatusEffect().getAmplifier()).formatted(Formatting.GRAY));
        } else{
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".first").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip."+this.getStatusEffect().getEffectType().getTranslationKey()+".second").formatted(Formatting.GRAY));
        }

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
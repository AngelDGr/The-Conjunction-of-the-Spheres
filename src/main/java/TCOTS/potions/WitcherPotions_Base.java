package TCOTS.potions;

import TCOTS.items.TCOTS_Items;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.GiveCommand;


public class WitcherPotions_Base extends PotionItem {

    StatusEffectInstance effectInstance;

    public WitcherPotions_Base(Settings settings, StatusEffectInstance effect){
        super(settings);
        this.effectInstance=effect;
    }

    public StatusEffectInstance getStatusEffect(){
        return effectInstance;
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            List<StatusEffectInstance> list = this.getPotionEffects();
            Iterator var6 = list.iterator();

            while(var6.hasNext()) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var6.next();
                if (statusEffectInstance.getEffectType().isInstant()) {
                    statusEffectInstance.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, statusEffectInstance.getAmplifier(), 1.0);
                } else {
                    user.addStatusEffect(new StatusEffectInstance(statusEffectInstance));
                }
            }
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }

    public List<StatusEffectInstance> getPotionEffects() {
        List<StatusEffectInstance> list = Lists.newArrayList();
        list.add(getStatusEffect());
        return list;
    }

    public void buildTooltip(ItemStack stack, List<Text> list, float durationMultiplier) {
        buildTooltip(getPotionEffects(), list, durationMultiplier);
    }

    public void buildTooltip(List<StatusEffectInstance> statusEffects, List<Text> list, float durationMultiplier) {
        List<Pair<EntityAttribute, EntityAttributeModifier>> list2 = Lists.newArrayList();
        Iterator var4;
        MutableText mutableText;
        StatusEffect statusEffect;
//        if (statusEffects.isEmpty()) {
//            list.add(NONE_TEXT);
//        } else {
            for(var4 = statusEffects.iterator(); var4.hasNext(); list.add(mutableText.formatted(statusEffect.getCategory().getFormatting()))) {
                StatusEffectInstance statusEffectInstance = (StatusEffectInstance)var4.next();
                mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
                statusEffect = statusEffectInstance.getEffectType();
                Map<EntityAttribute, EntityAttributeModifier> map = statusEffect.getAttributeModifiers();
                if (!map.isEmpty()) {
                    Iterator var9 = map.entrySet().iterator();

                    while(var9.hasNext()) {
                        Map.Entry<EntityAttribute, EntityAttributeModifier> entry = (Map.Entry)var9.next();
                        EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)entry.getValue();
                        EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), statusEffect.adjustModifierAmount(statusEffectInstance.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
                        list2.add(new Pair((EntityAttribute)entry.getKey(), entityAttributeModifier2));
                    }
                }

                if (statusEffectInstance.getAmplifier() > 0) {
                    mutableText = Text.translatable("potion.withAmplifier", new Object[]{mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier())});
                }

                if (!statusEffectInstance.isDurationBelow(20)) {
                    mutableText = Text.translatable("potion.withDuration", new Object[]{mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, durationMultiplier)});
                }
            }
//        }

        if (!list2.isEmpty()) {
            list.add(ScreenTexts.EMPTY);
            list.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));
            var4 = list2.iterator();

            while(var4.hasNext()) {
                Pair<EntityAttribute, EntityAttributeModifier> pair = (Pair)var4.next();
                EntityAttributeModifier entityAttributeModifier3 = (EntityAttributeModifier)pair.getSecond();
                double d = entityAttributeModifier3.getValue();
                double e;
                if (entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
                    e = entityAttributeModifier3.getValue();
                } else {
                    e = entityAttributeModifier3.getValue() * 100.0;
                }

                if (d > 0.0) {
                    list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier3.getOperation().getId(), new Object[]{ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(((EntityAttribute)pair.getFirst()).getTranslationKey())}).formatted(Formatting.BLUE));
                } else if (d < 0.0) {
                    e *= -1.0;
                    list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier3.getOperation().getId(), new Object[]{ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(((EntityAttribute)pair.getFirst()).getTranslationKey())}).formatted(Formatting.RED));
                }
            }
        }

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        buildTooltip(stack, tooltip, 1.0F);
    }

    @Override
    public ItemStack getDefaultStack() {
        return setCustomEffects();
    }

    public ItemStack setCustomEffects() {
        ItemStack itemStack = new ItemStack(this);

        NbtCompound potionNBT = itemStack.getOrCreateNbt();
        NbtList nbtList = potionNBT.getList("CustomPotionEffects", 9);
        nbtList.add(this.getStatusEffect().writeNbt(new NbtCompound()));

        itemStack.getOrCreateNbt().putString("Potion","minecraft:water");
        itemStack.getOrCreateNbt().put("CustomPotionEffects", nbtList);

        return itemStack;
    }
    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }
}

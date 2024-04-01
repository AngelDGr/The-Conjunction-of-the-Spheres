package TCOTS.potions;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import java.util.List;

public class WitcherAlcohol_Base extends WitcherPotions_Base{

    private final int refillQuantity;

    public int getRefillQuantity() {
        return refillQuantity;
    }

    private final java.util.List<StatusEffectInstance> effects = Lists.newArrayList();

    public WitcherAlcohol_Base(Settings settings, List<StatusEffectInstance> effects, int refillQuantity) {
        super(settings, new StatusEffectInstance(StatusEffects.BLINDNESS), 0, false);
        this.refillQuantity = refillQuantity;
        this.effects.addAll(effects);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip."+Registries.ITEM.getId(this)).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.tcots-witcher.refill", this.refillQuantity).formatted(Formatting.GRAY));
    }
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            for(StatusEffectInstance effect : this.effects){
                if(effect.getEffectType().isInstant()){
                    effect.getEffectType().applyInstantEffect(playerEntity, playerEntity, user, effect.getAmplifier(), 1.0);
                }
                else{
                    user.addStatusEffect(new StatusEffectInstance(effect));
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
            if (stack.isEmpty()) {return new ItemStack(Items.GLASS_BOTTLE);}

            if (playerEntity != null) {playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));}
        }

        user.emitGameEvent(GameEvent.DRINK);
        return stack;
    }
}

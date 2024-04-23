package TCOTS.potions;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitcherWhiteHoney extends WitcherPotions_Base{
    public WitcherWhiteHoney(Settings settings) {
        super(settings, new StatusEffectInstance(StatusEffects.POISON), 0, false);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            user.clearStatusEffects();
            if(user instanceof PlayerEntity player){
                player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getToxicity(),false);
                player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getDecoctionToxicity(),true);
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.effect.tcots-witcher.white_honey.first").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.effect.tcots-witcher.white_honey.second").formatted(Formatting.GRAY));
    }
}

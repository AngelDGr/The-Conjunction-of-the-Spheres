package TCOTS.items.concoctions;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class WitcherWhiteHoney extends WitcherPotions_Base{
    public WitcherWhiteHoney(Properties settings) {
        super(settings, new MobEffectInstance(MobEffects.POISON), 0, false);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity user) {
        Player playerEntity = user instanceof Player ? (Player)user : null;
        if (playerEntity instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);
        }

        if (!world.isClientSide) {
            user.removeAllEffects();
            if(user instanceof Player player){
                player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getNormalToxicity(),false);
                player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getDecoctionToxicity(),true);
            }
        }

        if (playerEntity != null) {
            playerEntity.awardStat(Stats.ITEM_USED.get(this));
            if (!playerEntity.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        ItemStack stack_Empty = WitcherPotions_Base.getStackEmptyBottle(this);

        if (playerEntity == null || !playerEntity.getAbilities().instabuild) {
            if (playerEntity != null) {
                //If the player inventories its full
                if(playerEntity.getInventory().getFreeSlot() == -1){
                    playerEntity.level().addFreshEntity(new ItemEntity(playerEntity.level(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), stack_Empty));
                } else{
                    playerEntity.getInventory().add(stack_Empty);
                }
            }
        }

        user.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player user, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(world, user, hand);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.effect.tcots_witcher.white_honey.first").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.effect.tcots_witcher.white_honey.second").withStyle(ChatFormatting.GRAY));
    }
}

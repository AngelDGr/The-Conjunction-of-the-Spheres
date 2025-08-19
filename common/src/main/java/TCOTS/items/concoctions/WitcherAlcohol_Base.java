package TCOTS.items.concoctions;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class WitcherAlcohol_Base extends WitcherPotions_Base {

    private final int refillQuantity;

    public int getRefillQuantity() {
        return refillQuantity;
    }

    private final java.util.List<MobEffectInstance> effects = Lists.newArrayList();

    public WitcherAlcohol_Base(final Properties settings, final List<MobEffectInstance> effects, final int refillQuantity) {
        super(settings, new MobEffectInstance(MobEffects.BLINDNESS), 0, false);
        this.refillQuantity = refillQuantity;
        this.effects.addAll(effects);
    }

    @Override
    public void appendHoverText(@NotNull final ItemStack stack, final TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip."+BuiltInRegistries.ITEM.getKey(this)).withStyle(ChatFormatting.GRAY));
        tooltip.add(CommonComponents.EMPTY);
        tooltip.add(Component.translatable("tooltip.tcots_witcher.refill").withStyle(ChatFormatting.GRAY));


        tooltip.add(CommonComponents.space().append(Component.translatable(
                this.refillQuantity >1 && this.refillQuantity <6?
                "tooltip.tcots_witcher.refill.slots":
                        this.refillQuantity>1?
                                "tooltip.tcots_witcher.refill.slots2":
                                "tooltip.tcots_witcher.refill.slot",

                this.refillQuantity).withStyle(ChatFormatting.BLUE)));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull final ItemStack stack, @NotNull final Level world, @NotNull final LivingEntity user) {
        final Player playerEntity = user instanceof Player ? (Player)user : null;
        if (playerEntity instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)playerEntity, stack);
        }

        if (!world.isClientSide) {
            for(final MobEffectInstance effect : this.effects){
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
            if (!playerEntity.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().instabuild) {
            if (stack.isEmpty()) {return new ItemStack(Items.GLASS_BOTTLE);}

            if (playerEntity != null) {playerEntity.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));}
        }

        user.gameEvent(GameEvent.DRINK);
        return stack;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(final ItemStack stack) {
        return UseAnim.DRINK;
    }
}

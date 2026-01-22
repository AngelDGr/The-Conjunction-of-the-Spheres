package mors.tcots.items.weapons;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

public class KnightCrossbow extends WitcherBaseCrossbow{
    //xTODO: Improve this crossbow, extra damage

    public KnightCrossbow(final Properties settings) {
        super(settings);
    }

    @Override
    protected float getShootingPower(final ChargedProjectiles stack) {
        return stack.contains(Items.FIREWORK_ROCKET) ? 4.8f : 6.2f;
    }

    @Override
    public int getCrossbowPullTime(final ItemStack stack, final LivingEntity user) {
        final float f = EnchantmentHelper.modifyCrossbowChargingTime(stack, user, 2.5F);
        return Mth.floor(f * 20.0F);
    }
    @Override
    public void appendHoverText(@NotNull final ItemStack stack, @NotNull final TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag type) {
        super.appendHoverText(stack, context, tooltip, type);
        tooltip.add(Component.translatable("item.tcots_witcher.knight_crossbow.tooltip").withStyle(ChatFormatting.GRAY));

        if(!stack.has(DataComponents.DYED_COLOR)){
            tooltip.add(Component.translatable("item.tcots_witcher.tooltip.dyeable").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}

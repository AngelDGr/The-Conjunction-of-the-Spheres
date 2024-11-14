package TCOTS.items.weapons;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class KnightCrossbow extends WitcherBaseCrossbow {
    //xTODO: Improve this crossbow, extra damage

    public KnightCrossbow(Settings settings) {
        super(settings);
    }

    @Override
    protected float getSpeed(ChargedProjectilesComponent stack) {
        return stack.contains(Items.FIREWORK_ROCKET) ? 4.8f : 6.2f;
    }

    @Override
    public int getCrossbowPullTime(ItemStack stack, LivingEntity user) {
        float f = EnchantmentHelper.getCrossbowChargeTime(stack, user, 2.5F);
        return MathHelper.floor(f * 20.0F);
    }
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable("tooltip.tcots-witcher.knight_crossbow").formatted(Formatting.GRAY));

        if(!stack.contains(DataComponentTypes.DYED_COLOR)){
            tooltip.add(Text.translatable("tooltip.knight_crossbow.dyeable").formatted(Formatting.GRAY, Formatting.ITALIC));
        }
    }
}

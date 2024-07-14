package TCOTS.items.weapons;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KnightCrossbow extends WitcherBaseCrossbow implements DyeableItem {
    //xTODO: Improve this crossbow, extra damage

    public KnightCrossbow(Settings settings) {
        super(settings);
    }

    @Override
    protected float getProjectileSpeed(ItemStack stack) {
        if (CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET)) {
            return 4.8f;
        }
        return 6.2f;
    }

    @Override
    public int getCrossbowPullTime(ItemStack stack) {
        return KnightCrossbow.getPullTime(stack);
    }

    public static int getPullTime(ItemStack stack) {
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 50 : 50 - 5 * i;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(!hasColor(stack)){
            tooltip.add(Text.translatable("tooltip.knight_crossbow.dyeable").formatted(Formatting.GRAY, Formatting.ITALIC));
        }

        tooltip.add(Text.translatable("tooltip.tcots-witcher.knight_crossbow").formatted(Formatting.GRAY));
    }
}

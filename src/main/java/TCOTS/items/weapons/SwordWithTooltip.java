package TCOTS.items.weapons;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SwordWithTooltip extends SwordItem {
    private final List<MutableText> tooltip;

    public SwordWithTooltip(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, List<MutableText> tooltip) {
        super(toolMaterial, attackDamage, attackSpeed, settings);

        this.tooltip=tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        tooltip.addAll(this.tooltip);

        if(stack.hasEnchantments()) tooltip.add(ScreenTexts.EMPTY);
    }

}

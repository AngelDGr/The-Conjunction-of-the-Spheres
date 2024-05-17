package TCOTS.items.weapons;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KnightCrossbow extends CrossbowItem implements DyeableItem {
    //TODO: Improve this crossbow, extra damage?

    public KnightCrossbow(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if(!hasColor(stack)){
            tooltip.add(Text.translatable("tooltip.knight_crossbow.dyeable").formatted(Formatting.GRAY, Formatting.ITALIC));
        }
    }

}

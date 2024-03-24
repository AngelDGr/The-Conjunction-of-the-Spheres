package TCOTS.potions;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DwarvenSpiritItem extends WitcherAlcohol_Base {

    public DwarvenSpiritItem(Settings settings, StatusEffectInstance effect, int toxicity, int refillQuantity) {
        super(settings, effect, toxicity, refillQuantity);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.dwarven_spirit").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}

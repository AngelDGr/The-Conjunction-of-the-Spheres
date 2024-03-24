package TCOTS.potions;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlcohestItem extends WitcherAlcohol_Base{


    public AlcohestItem(Settings settings, StatusEffectInstance effect, int toxicity, int refillQuantity) {
        super(settings, effect, toxicity, refillQuantity);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.alcohest").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}

package fabric.TCOTS.items;

import io.wispforest.lavender.book.LavenderBookItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class WitcherBestiaryItem extends LavenderBookItem {

    public WitcherBestiaryItem(@NotNull ResourceLocation bookId, Properties settings) {
        super(settings, bookId);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.tcots_witcher.witcher_bestiary").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, type);
    }
}

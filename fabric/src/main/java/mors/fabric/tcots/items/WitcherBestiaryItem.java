package mors.fabric.tcots.items;

import io.wispforest.lavender.book.LavenderBookItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class WitcherBestiaryItem extends LavenderBookItem {

    public WitcherBestiaryItem(@NotNull final ResourceLocation bookId, final Properties settings) {
        super(settings, bookId);
    }

    @Override
    public void appendHoverText(final ItemStack stack, final TooltipContext context, final List<Component> tooltip, final TooltipFlag type) {
        tooltip.add(Component.translatable("item.tcots_witcher.witcher_bestiary.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, type);
    }
}

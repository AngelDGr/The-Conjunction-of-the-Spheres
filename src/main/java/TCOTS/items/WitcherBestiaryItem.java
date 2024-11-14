package TCOTS.items;

import io.wispforest.lavender.book.LavenderBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WitcherBestiaryItem extends LavenderBookItem {
    protected WitcherBestiaryItem(@NotNull Identifier bookId, Settings settings) {
        super(settings, bookId);
    }

    public static LavenderBookItem registerForBook(@NotNull Identifier bookId, Settings settings) {
        return registerForBook(bookId, bookId, settings);
    }

    public static WitcherBestiaryItem registerForBook(@NotNull Identifier bookId, @NotNull Identifier itemId, Settings settings) {
        return (WitcherBestiaryItem) registerForBook(Registry.register(Registries.ITEM, itemId, new WitcherBestiaryItem(bookId, settings)));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.witcher_bestiary").formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}

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

public class AlchemyBookItem extends LavenderBookItem {

    protected AlchemyBookItem(@NotNull Identifier bookId, Settings settings) {
        super(settings, bookId);
    }


    public static LavenderBookItem registerForBook(@NotNull Identifier bookId, Settings settings) {
        return registerForBook(bookId, bookId, settings);
    }

    public static AlchemyBookItem registerForBook(@NotNull Identifier bookId, @NotNull Identifier itemId, Settings settings) {
        return (AlchemyBookItem) registerForBook(Registry.register(Registries.ITEM, itemId, new AlchemyBookItem(bookId, settings)));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.alchemy_book").formatted(Formatting.GRAY));
        super.appendTooltip(stack, context, tooltip, type);
    }
}


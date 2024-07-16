package TCOTS.items;

import io.wispforest.lavender.book.LavenderBookItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlchemyBookItem extends LavenderBookItem {

    //TODO: Remake the Alchemy Almanac, adding the formulae information
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.alchemy_book").formatted(Formatting.GRAY));
        super.appendTooltip(stack, world, tooltip, context);
    }
}


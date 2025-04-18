package TCOTS.items;

import io.wispforest.lavender.book.LavenderBookItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class AlchemyBookItem extends LavenderBookItem {

    protected AlchemyBookItem(@NotNull ResourceLocation bookId, Properties settings) {
        super(settings, bookId);
    }


    public static LavenderBookItem registerForBook(@NotNull ResourceLocation bookId, Properties settings) {
        return registerForBook(bookId, bookId, settings);
    }

    public static AlchemyBookItem registerForBook(@NotNull ResourceLocation bookId, @NotNull ResourceLocation itemId, Properties settings) {
        return (AlchemyBookItem) registerForBook(Registry.register(BuiltInRegistries.ITEM, itemId, new AlchemyBookItem(bookId, settings)));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.tcots_witcher.alchemy_book").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, type);
    }
}


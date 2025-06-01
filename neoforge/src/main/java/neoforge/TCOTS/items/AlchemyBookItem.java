package neoforge.TCOTS.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.common.item.ItemModBook;

import java.util.List;

public class AlchemyBookItem extends ItemModBook {

    public @NotNull Component getName(ItemStack arg) {
        return Component.translatable(this.getDescriptionId(arg));
    }

    public AlchemyBookItem(Item.Properties arg) {
        this.components = arg.buildAndValidateComponents();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltip.tcots_witcher.alchemy_book").withStyle(ChatFormatting.GRAY));
    }

}
package mors.neoforge.tcots.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.common.item.ItemModBook;

import java.util.List;

public class WitcherBestiaryItem extends ItemModBook {

    public @NotNull Component getName(final ItemStack arg) {
        return Component.translatable(this.getDescriptionId(arg));
    }

    public WitcherBestiaryItem(final Item.Properties arg) {
        this.components = arg.buildAndValidateComponents();
    }

    @Override
    public void appendHoverText(final ItemStack stack, final Item.TooltipContext context, final List<Component> tooltip, final TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.tcots_witcher.witcher_bestiary.tooltip").withStyle(ChatFormatting.GRAY));
    }

}

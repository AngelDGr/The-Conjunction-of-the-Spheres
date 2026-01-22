package mors.tcots.items.concoctions;

import java.util.List;

import mors.tcots.registry.TCOTS_Items;
import mors.tcots.utils.tooltip.FontHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

public class EmptyBombPowderItem extends EmptyWitcherPotionItem {
    public EmptyBombPowderItem(final Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull Component getName(final ItemStack stack) {
        return Component.translatable("item.tcots_witcher.bomb_powder");
    }


    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(@NotNull final ItemStack stack, @NotNull final TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag type) {
        if(!stack.has(TCOTS_Items.RefillRecipe())){
            return;
        }

        final String refillItem = stack.get(TCOTS_Items.RefillRecipe());

        if(refillItem != null){
            final Item bomb = BuiltInRegistries.ITEM.get(ResourceLocation.parse(refillItem));
            if(bomb!=null){
                tooltip.add(Component.translatable("item.tcots_witcher.bomb_powder.tooltip.name", bomb.getDescription().getString()).withStyle(ChatFormatting.GRAY,ChatFormatting.ITALIC));
            }

        } else{tooltip.add(Component.translatable("item.tcots_witcher.bomb_powder.tooltip.name", "Missigno").withStyle(ChatFormatting.GRAY,ChatFormatting.ITALIC));}

        tooltip.addAll(FontHelper.cutTextComponent(Component.translatable("item.tcots_witcher.bomb_powder.tooltip"), FontHelper.Palette.ALL_GRAY));
    }
}

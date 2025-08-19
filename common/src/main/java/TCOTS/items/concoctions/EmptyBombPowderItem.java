package TCOTS.items.concoctions;

import java.util.List;

import TCOTS.registry.TCOTS_Items;
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

    @SuppressWarnings("all")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        if(!stack.has(TCOTS_Items.RefillRecipe())){
            return;
        }

        String refillItem = stack.get(TCOTS_Items.RefillRecipe());

        if(refillItem != null){
            Item bomb = BuiltInRegistries.ITEM.get(ResourceLocation.parse(refillItem));
            if(bomb!=null){
                tooltip.add(Component.translatable("item.tcots_witcher.bomb_powder_tooltip", bomb.getDescription().getString()).withStyle(ChatFormatting.GRAY,ChatFormatting.ITALIC));
            }

        } else{tooltip.add(Component.translatable("item.tcots_witcher.bomb_powder_tooltip", "Missigno").withStyle(ChatFormatting.GRAY,ChatFormatting.ITALIC));}
        tooltip.add(Component.translatable("tooltip.tcots_witcher.bomb_powder_1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.tcots_witcher.bomb_powder_2").withStyle(ChatFormatting.GRAY));
    }
}

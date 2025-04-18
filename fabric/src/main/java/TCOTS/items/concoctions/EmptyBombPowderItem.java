package TCOTS.items.concoctions;

import java.util.List;

import TCOTS.items.TCOTS_Items_Fabric;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class EmptyBombPowderItem extends EmptyWitcherPotionItem {
    public EmptyBombPowderItem(Properties settings) {
        super(settings);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.tcots_witcher.bomb_powder");
    }

    @SuppressWarnings("all")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        if(!stack.has(TCOTS_Items_Fabric.REFILL_RECIPE)){
            return;
        }

        String refillItem = stack.get(TCOTS_Items_Fabric.REFILL_RECIPE);

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

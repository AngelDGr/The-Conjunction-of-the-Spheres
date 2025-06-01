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

public class EmptyWitcherPotionItem extends Item {


    public EmptyWitcherPotionItem(Properties settings) {
        super(settings);
    }

    @SuppressWarnings("all")
    @Override
    public Component getName(ItemStack stack) {
        if(!stack.has(TCOTS_Items.RefillRecipe())){
            return Component.translatable("item.tcots_witcher.empty_witcher_potion", "Missigno");
        }
        String potionToRefill = stack.get(TCOTS_Items.RefillRecipe());

        if(potionToRefill != null){
            Item potion = BuiltInRegistries.ITEM.get(ResourceLocation.parse(potionToRefill));
            if(potion!=null){
                return Component.translatable("item.tcots_witcher.empty_witcher_potion", potion.getDescription().getString());}
            else{
                return Component.translatable("item.tcots_witcher.empty_witcher_potion", "Missigno");}
        }
        else{
            return Component.translatable("item.tcots_witcher.empty_witcher_potion", "Missigno");}
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.tcots_witcher.empty_witcher_bottle_1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.tcots_witcher.empty_witcher_bottle_2").withStyle(ChatFormatting.GRAY));
    }
}

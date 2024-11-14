package TCOTS.items.concoctions;

import TCOTS.items.TCOTS_Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class EmptyWitcherPotionItem extends Item {


    public EmptyWitcherPotionItem(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("all")
    @Override
    public Text getName(ItemStack stack) {
        if(!stack.contains(TCOTS_Items.REFILL_RECIPE)){
            return Text.translatable("item.tcots-witcher.empty_witcher_potion", "Missigno");
        }
        String potionToRefill = stack.get(TCOTS_Items.REFILL_RECIPE);

        if(potionToRefill != null){
            Item potion = Registries.ITEM.get(Identifier.of(potionToRefill));
            if(potion!=null){
                return Text.translatable("item.tcots-witcher.empty_witcher_potion", potion.getName().getString());}
            else{
                return Text.translatable("item.tcots-witcher.empty_witcher_potion", "Missigno");}
        }
        else{
            return Text.translatable("item.tcots-witcher.empty_witcher_potion", "Missigno");}
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.empty_witcher_bottle_1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.tcots-witcher.empty_witcher_bottle_2").formatted(Formatting.GRAY));
    }
}

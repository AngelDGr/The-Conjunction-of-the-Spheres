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

public class EmptyBombPowderItem extends EmptyWitcherPotionItem {
    public EmptyBombPowderItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item.tcots-witcher.bomb_powder");
    }

    @SuppressWarnings("all")
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(!stack.contains(TCOTS_Items.REFILL_RECIPE)){
            return;
        }

        String refillItem = stack.get(TCOTS_Items.REFILL_RECIPE);

        if(refillItem != null){
            Item bomb = Registries.ITEM.get(Identifier.of(refillItem));
            if(bomb!=null){
                tooltip.add(Text.translatable("item.tcots-witcher.bomb_powder_tooltip", bomb.getName().getString()).formatted(Formatting.GRAY,Formatting.ITALIC));
            }

        } else{tooltip.add(Text.translatable("item.tcots-witcher.bomb_powder_tooltip", "Missigno").formatted(Formatting.GRAY,Formatting.ITALIC));}
        tooltip.add(Text.translatable("tooltip.tcots-witcher.bomb_powder_1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.tcots-witcher.bomb_powder_2").formatted(Formatting.GRAY));
    }
}

package TCOTS.potions;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmptyWitcherPotionItem extends Item {


    public EmptyWitcherPotionItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName() {
        return super.getName();
    }

    @Override
    public ItemStack getDefaultStack() {
        return super.getDefaultStack();
    }

    @Override
    public Text getName(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();

        if(nbtCompound != null && nbtCompound.contains("Potion")){
            Item potion = Registries.ITEM.get(new Identifier(nbtCompound.getString("Potion")));

            if(potion!=null){
                return Text.translatable("item.tcots-witcher.empty_witcher_potion", potion.getName().getString());}
            else{return Text.translatable("item.tcots-witcher.empty_witcher_potion", "Missigno");}
        }
        else{
            return Text.translatable("item.tcots-witcher.empty_witcher_potion", "Missigno");}
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.empty_witcher_bottle_1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.tcots-witcher.empty_witcher_bottle_2").formatted(Formatting.GRAY));
    }

}

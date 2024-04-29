package TCOTS.items.potions;

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

public class EmptyBombPowderItem extends EmptyWitcherPotionItem {
    public EmptyBombPowderItem(Settings settings) {
        super(settings);
    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable("item.tcots-witcher.bomb_powder");
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbtCompound = stack.getNbt();

        if(nbtCompound != null && nbtCompound.contains("Potion")){
            Item bomb = Registries.ITEM.get(new Identifier(nbtCompound.getString("Potion")));
            if(bomb!=null){
                tooltip.add(Text.translatable("item.tcots-witcher.bomb_powder_tooltip", bomb.getName().getString()).formatted(Formatting.GRAY,Formatting.ITALIC));
            }

        } else{tooltip.add(Text.translatable("item.tcots-witcher.bomb_powder_tooltip", "Missigno").formatted(Formatting.GRAY,Formatting.ITALIC));}
        tooltip.add(Text.translatable("tooltip.tcots-witcher.bomb_powder_1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.tcots-witcher.bomb_powder_2").formatted(Formatting.GRAY));
    }
}

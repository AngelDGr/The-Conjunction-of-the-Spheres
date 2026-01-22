package mors.tcots.items.concoctions;

import java.util.List;


import mors.tcots.registry.TCOTS_Items;
import mors.tcots.utils.tooltip.FontHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

public class EmptyWitcherPotionItem extends Item {


    public EmptyWitcherPotionItem(final Properties settings) {
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
    @Environment(EnvType.CLIENT)
    public void appendHoverText(final @NotNull ItemStack stack, final @NotNull TooltipContext context, final @NotNull List<Component> tooltip, final @NotNull TooltipFlag type) {
        tooltip.addAll(FontHelper.cutTextComponent(Component.translatable("item.tcots_witcher.empty_witcher_bottle.tooltip"), FontHelper.Palette.ALL_GRAY));
    }
}

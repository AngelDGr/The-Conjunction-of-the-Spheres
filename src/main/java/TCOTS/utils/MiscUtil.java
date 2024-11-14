package TCOTS.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.compress.utils.Lists;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class MiscUtil {
    /**
    Puts a dynamic tooltip to an armor item that it can be open with shift
     @param stack The stack that it's going to have the tooltip
     @param mainTooltip The main tooltip to change
     @param bonusTooltip The tooltip that it's going to be added
     */
    public static void setFullSetBonusTooltip(ItemStack stack, List<Text> mainTooltip, List<MutableText> bonusTooltip){
        List<MutableText> bonusTooltipGreen = Lists.newArrayList();

        bonusTooltip.forEach(text -> {
            text.formatted(Formatting.DARK_GREEN, Formatting.ITALIC);
            bonusTooltipGreen.add(text);
        });



        MiscUtil.setSpecialTooltip(Text.translatable("tooltip.tcots-witcher.generic_tooltip.full_set_bonus"), stack, mainTooltip, bonusTooltipGreen);
    }

    /**
     Puts a dynamic tooltip to an item that it can be open with shift
     @param stack The stack that it's going to have the tooltip
     @param mainTooltip The main tooltip to change
     @param bonusTooltip The tooltip that it's going to be added
     */
    public static void setSpecialTooltip(MutableText mainText,ItemStack stack, List<Text> mainTooltip, List<MutableText> bonusTooltip){
        if(MinecraftClient.getInstance()!=null && (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT))){

            mainTooltip.add(mainText.formatted(Formatting.DARK_GREEN));

            bonusTooltip.forEach(
                    text -> mainTooltip.add(ScreenTexts.space().append(text))
            );

        } else {
            mainTooltip.add(mainText.formatted(Formatting.GRAY));
            mainTooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.generic_tooltip.see_more").formatted(Formatting.GRAY, Formatting.ITALIC)));
        }

        if(stack.hasEnchantments()) mainTooltip.add(ScreenTexts.EMPTY);
    }

    public static int getTimeInTicks(int seconds){
        return seconds*20;
    }

    /**
    Get the enchantment level from a specific enchantment
     @param enchantment The Enchantment to check
     @param stack The stack to check
     */
    public static int getEnchantmentLevel(RegistryKey<Enchantment> enchantment, ItemStack stack) {
        int level = 0;
        ItemEnchantmentsComponent itemEnchantmentsComponent = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        for (RegistryEntry<Enchantment> entry : itemEnchantmentsComponent.getEnchantments()) {
            if (entry.matchesKey(enchantment)) {
                level = itemEnchantmentsComponent.getLevel(entry);
            }
        }

        return level;
    }
}

package TCOTS.utils;

import org.apache.commons.compress.utils.Lists;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class MiscUtil {
    /**
    Puts a dynamic tooltip to an armor item that it can be open with shift
     @param stack The stack that it's going to have the tooltip
     @param mainTooltip The main tooltip to change
     @param bonusTooltip The tooltip that it's going to be added
     */
    public static void setFullSetBonusTooltip(ItemStack stack, List<Component> mainTooltip, List<MutableComponent> bonusTooltip){
        List<MutableComponent> bonusTooltipGreen = Lists.newArrayList();

        bonusTooltip.forEach(text -> {
            text.withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.ITALIC);
            bonusTooltipGreen.add(text);
        });



        MiscUtil.setSpecialTooltip(Component.translatable("tooltip.tcots_witcher.generic_tooltip.full_set_bonus"), stack, mainTooltip, bonusTooltipGreen);
    }

    /**
     Puts a dynamic tooltip to an item that it can be open with shift
     @param stack The stack that it's going to have the tooltip
     @param mainTooltip The main tooltip to change
     @param bonusTooltip The tooltip that it's going to be added
     */
    public static void setSpecialTooltip(MutableComponent mainText,ItemStack stack, List<Component> mainTooltip, List<MutableComponent> bonusTooltip){
        if(Minecraft.getInstance()!=null && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_RIGHT_SHIFT))){

            mainTooltip.add(mainText.withStyle(ChatFormatting.DARK_GREEN));

            bonusTooltip.forEach(
                    text -> mainTooltip.add(CommonComponents.space().append(text))
            );

        } else {
            mainTooltip.add(mainText.withStyle(ChatFormatting.GRAY));
            mainTooltip.add(CommonComponents.space().append(Component.translatable("tooltip.tcots_witcher.generic_tooltip.see_more").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
        }

        if(stack.isEnchanted()) mainTooltip.add(CommonComponents.EMPTY);
    }

    public static int getTimeInTicks(int seconds){
        return seconds*20;
    }

    /**
    Get the enchantment level from a specific enchantment
     @param enchantment The Enchantment to check
     @param stack The stack to check
     */
    public static int getEnchantmentLevel(ResourceKey<Enchantment> enchantment, ItemStack stack) {
        int level = 0;
        ItemEnchantments itemEnchantmentsComponent = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (Holder<Enchantment> entry : itemEnchantmentsComponent.keySet()) {
            if (entry.is(enchantment)) {
                level = itemEnchantmentsComponent.getLevel(entry);
            }
        }

        return level;
    }
}

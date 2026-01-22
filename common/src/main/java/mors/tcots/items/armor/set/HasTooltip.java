package mors.tcots.items.armor.set;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public interface HasTooltip {
    List<MutableComponent> getTooltip();

    @Environment(EnvType.CLIENT)
    static void addTooltip(final List<Component> list, final Player player, final ItemStack stack, final List<MutableComponent> tooltip){
        if (Minecraft.getInstance() != null && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT))) {
            //Space
            list.add(Component.empty());

            //Item Name
            list.add(stack.getItem().getName(stack).plainCopy().withStyle(ChatFormatting.GOLD));

            for(final MutableComponent mutableComponent: tooltip)
                list.add(mutableComponent.withStyle(ChatFormatting.DARK_GRAY));

        } else {
            list.add(Component.translatable(
                    "item.tcots_witcher.sword.tooltip.see_more", "Left Alt"
            ).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}

package mors.tcots.client.screen;

import mors.tcots.TCOTS_Main;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class HerbalTableScreen extends AbstractContainerScreen<HerbalTableScreenHandler> {
    public HerbalTableScreen(final HerbalTableScreenHandler handler, final Inventory inventory, final Component title) {
        super(handler, inventory, title);
        this.titleLabelX = 50;
        this.titleLabelY = 25;
    }

    public static final ResourceLocation SCREEN_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/gui/herbal_table.png");

    @Override
    protected void renderBg(final GuiGraphics context, final float delta, final int mouseX, final int mouseY) {
        final int i = this.leftPos;
        final int j =(this.height - this.imageHeight) / 2;
        context.blit(SCREEN_BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull final GuiGraphics context, final int mouseX, final int mouseY, final float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }
}

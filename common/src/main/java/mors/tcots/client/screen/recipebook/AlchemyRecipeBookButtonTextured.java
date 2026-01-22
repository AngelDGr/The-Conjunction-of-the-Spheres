package mors.tcots.client.screen.recipebook;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AlchemyRecipeBookButtonTextured extends AlchemyRecipeBookButton{
    protected final WidgetSprites textures;

    public AlchemyRecipeBookButtonTextured(final int x, final int y, final int width, final int height, final WidgetSprites textures, final AlchemyRecipeBookButton.PressAction pressAction) {
        this(x, y, width, height, textures, pressAction, CommonComponents.EMPTY);
    }

    public AlchemyRecipeBookButtonTextured(final int x, final int y, final int width, final int height, final WidgetSprites textures, final AlchemyRecipeBookButton.PressAction pressAction, final Component text) {
        super(x, y, width, height, text, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.textures = textures;
    }

    @Override
    public void renderWidget(final GuiGraphics context, final int mouseX, final int mouseY, final float delta) {
        final ResourceLocation identifier = this.textures.get(this.isActive(), this.isHoveredOrFocused());
        context.blitSprite(identifier, this.getX(), this.getY(), this.width, this.height);
    }
}

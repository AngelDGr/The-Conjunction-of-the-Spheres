package TCOTS.screen.recipebook;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class AlchemyRecipeBookButtonTextured extends AlchemyRecipeBookButton{
    protected final WidgetSprites textures;

    public AlchemyRecipeBookButtonTextured(int x, int y, int width, int height, WidgetSprites textures, AlchemyRecipeBookButton.PressAction pressAction) {
        this(x, y, width, height, textures, pressAction, CommonComponents.EMPTY);
    }

    public AlchemyRecipeBookButtonTextured(int x, int y, int width, int height, WidgetSprites textures, AlchemyRecipeBookButton.PressAction pressAction, Component text) {
        super(x, y, width, height, text, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.textures = textures;
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        ResourceLocation identifier = this.textures.get(this.isActive(), this.isHoveredOrFocused());
        context.blitSprite(identifier, this.getX(), this.getY(), this.width, this.height);
    }
}

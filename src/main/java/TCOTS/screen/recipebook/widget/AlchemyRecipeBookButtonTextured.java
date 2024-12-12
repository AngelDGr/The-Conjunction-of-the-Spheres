package TCOTS.screen.recipebook.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AlchemyRecipeBookButtonTextured extends AlchemyRecipeBookButton {
    protected final Identifier texture;
    protected final int u;
    protected final int v;
    protected final int hoveredVOffset;
    protected final int textureWidth;
    protected final int textureHeight;

    public AlchemyRecipeBookButtonTextured(
            int x,
            int y,
            int width,
            int height,
            int u,
            int v,
            int hoveredVOffset,
            Identifier texture,
            int textureWidth,
            int textureHeight,
            AlchemyRecipeBookButton.PressAction pressAction
    ) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction, ScreenTexts.EMPTY);
    }

    public AlchemyRecipeBookButtonTextured(
            int x,
            int y,
            int width,
            int height,
            int u,
            int v,
            int hoveredVOffset,
            Identifier texture,
            int textureWidth,
            int textureHeight,
            AlchemyRecipeBookButton.PressAction pressAction,
            Text message
    ) {
        super(x, y, width, height, message, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.hoveredVOffset = hoveredVOffset;
        this.texture = texture;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        this.drawTexture(
                context, this.texture,
                this.getX(), this.getY(),
                this.u, this.v, this.hoveredVOffset, this.width, this.height,
                this.textureWidth, this.textureHeight
        );
    }
}

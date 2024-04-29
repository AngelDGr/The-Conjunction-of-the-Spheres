package TCOTS.items.potions.recipes.recipebook.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AlchemyRecipeBookButtonTextured extends AlchemyRecipeBookButton{
    protected final ButtonTextures textures;

    public AlchemyRecipeBookButtonTextured(int x, int y, int width, int height, ButtonTextures textures, AlchemyRecipeBookButton.PressAction pressAction) {
        this(x, y, width, height, textures, pressAction, ScreenTexts.EMPTY);
    }

    public AlchemyRecipeBookButtonTextured(int x, int y, int width, int height, ButtonTextures textures, AlchemyRecipeBookButton.PressAction pressAction, Text text) {
        super(x, y, width, height, text, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.textures = textures;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        Identifier identifier = this.textures.get(this.isNarratable(), this.isSelected());
        context.drawGuiTexture(identifier, this.getX(), this.getY(), this.width, this.height);
    }
}

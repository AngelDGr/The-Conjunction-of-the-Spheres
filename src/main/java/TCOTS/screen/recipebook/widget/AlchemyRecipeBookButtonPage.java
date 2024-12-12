package TCOTS.screen.recipebook.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvents;

public class AlchemyRecipeBookButtonPage extends ToggleButtonWidget {
    public AlchemyRecipeBookButtonPage(int x, int y, int width, int height, boolean toggled) {
        super(x, y, width, height, toggled);
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0f));
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        RenderSystem.disableDepthTest();
        int i = this.u;
        int j = this.v;
        if (this.toggled) {
            i += this.pressedUOffset;
        }

        if (this.isSelected()) {
            j += this.hoverVOffset;
        }

        context.drawTexture(this.texture, this.getX(), this.getY(), i, j, this.width, this.height,
                18,
                20);
        RenderSystem.enableDepthTest();
    }
}

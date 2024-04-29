package TCOTS.items.potions.recipes.recipebook.widget;

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
}

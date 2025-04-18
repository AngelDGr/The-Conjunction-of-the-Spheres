package TCOTS.screen.recipebook;

import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public class AlchemyRecipeBookButtonPage extends StateSwitchingButton {
    public AlchemyRecipeBookButtonPage(int x, int y, int width, int height, boolean toggled) {
        super(x, y, width, height, toggled);
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0f));
    }
}

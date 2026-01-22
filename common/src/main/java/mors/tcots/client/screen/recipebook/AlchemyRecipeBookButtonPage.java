package mors.tcots.client.screen.recipebook;

import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvents;

public class AlchemyRecipeBookButtonPage extends StateSwitchingButton {
    public AlchemyRecipeBookButtonPage(final int x, final int y, final int width, final int height, final boolean toggled) {
        super(x, y, width, height, toggled);
    }

    public void playDownSound(final SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0f));
    }
}

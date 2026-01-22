package mors.tcots.client.screen.recipebook;

import mors.tcots.TCOTS_Main;
import mors.tcots.recipes.AlchemyTableRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class AlchemyRecipeGroupButton extends StateSwitchingButton {
    private final AlchemyTableRecipeCategory category;
    ItemStack icon;

    public AlchemyRecipeGroupButton(final ItemStack icon, final AlchemyTableRecipeCategory category) {
        super(0, 0, 39, 27, false);

        final WidgetSprites BUTTON_TEXTURES = new WidgetSprites(
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/button_group_highlighted"),
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"buttons/button_group"),
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/button_group_highlighted"));

        this.icon=icon;
        this.category = category;

        this.initTextureValues(BUTTON_TEXTURES);
    }

    public void checkForNewRecipes(final Minecraft client) {
        assert client.player != null;
    }

    public AlchemyTableRecipeCategory getCategory(){
        return this.category;
    }

    @Override
    public void renderWidget(final GuiGraphics context, final int mouseX, final int mouseY, final float delta) {

        context.pose().pushPose();
        context.pose().translate(0,0,500);
        this.renderIcons(context);
        context.pose().popPose();

        if (this.sprites == null) {
            return;
        }

        context.blitSprite(this.sprites.get(this.isStateTriggered, this.isHoveredOrFocused()), this.getX(), this.getY(), this.width, this.height);
    }

    private void renderIcons(final GuiGraphics context) {

        context.renderFakeItem(this.icon, this.getX() + 18, this.getY() + 6);
    }


    public void playDownSound(final SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 0.7f));
    }
}

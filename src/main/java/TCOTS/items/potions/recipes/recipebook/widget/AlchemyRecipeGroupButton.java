package TCOTS.items.potions.recipes.recipebook.widget;

import TCOTS.TCOTS_Main;
import TCOTS.items.potions.recipes.AlchemyTableRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.ToggleButtonWidget;

import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;


public class AlchemyRecipeGroupButton extends ToggleButtonWidget {
    private final AlchemyTableRecipeCategory category;
    ItemStack icon;

    public AlchemyRecipeGroupButton(ItemStack icon, AlchemyTableRecipeCategory category) {
        super(0, 0, 39, 27, false);

        ButtonTextures BUTTON_TEXTURES = new ButtonTextures(
                new Identifier(TCOTS_Main.MOD_ID, "buttons/button_group_highlighted"),
                new Identifier(TCOTS_Main.MOD_ID,"buttons/button_group"),
                new Identifier(TCOTS_Main.MOD_ID, "buttons/button_group_highlighted"));

        this.icon=icon;
        this.category = category;

        this.setTextures(BUTTON_TEXTURES);
    }

    public void checkForNewRecipes(MinecraftClient client) {
        assert client.player != null;
//        ClientRecipeBook clientRecipeBook = client.player.getRecipeBook();
    }

    public AlchemyTableRecipeCategory getCategory(){
        return this.category;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {

        context.getMatrices().push();
        context.getMatrices().translate(0,0,500);
        this.renderIcons(context);
        context.getMatrices().pop();

        if (this.textures == null) {
            return;
        }

        context.drawGuiTexture(this.textures.get(this.toggled, this.isSelected()), this.getX(), this.getY(), this.width, this.height);
    }

    private void renderIcons(DrawContext context) {

        context.drawItemWithoutEntity(this.icon, this.getX() + 18, this.getY() + 6);
    }


    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 0.7f));
    }
}

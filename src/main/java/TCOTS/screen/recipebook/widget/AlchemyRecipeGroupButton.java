package TCOTS.screen.recipebook.widget;

import TCOTS.TCOTS_Main;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.List;


public class AlchemyRecipeGroupButton extends ToggleButtonWidget {
    private final AlchemyTableRecipeCategory category;
    ItemStack icon;

    private final List<Identifier> BUTTON_TEXTURES = List.of(
            new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/buttons/button_group_highlighted.png"),
            new Identifier(TCOTS_Main.MOD_ID,"textures/gui/sprites/buttons/button_group.png"),
            new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/buttons/button_group_highlighted.png"));

    public AlchemyRecipeGroupButton(ItemStack icon, AlchemyTableRecipeCategory category) {
        super(0, 0, 39, 27, false);

        this.icon=icon;
        this.category = category;

        this.setTextureUV(
                0,
                0,
                0,
                0,
                new Identifier(
                TCOTS_Main.MOD_ID,
                "textures/gui/sprites/buttons/button_group_old.png"));
    }

    public void checkForNewRecipes(MinecraftClient client) {
        assert client.player != null;
//        ClientRecipeBook clientRecipeBook = client.player.getRecipeBook();
    }

    public AlchemyTableRecipeCategory getCategory(){
        return this.category;
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {

        context.getMatrices().push();
        context.getMatrices().translate(0,0,500);
        this.renderIcons(context);
        context.getMatrices().pop();

        context.drawTexture(
                this.toggled?
                        BUTTON_TEXTURES.get(0)
                        : BUTTON_TEXTURES.get(1),
                this.getX(), this.getY(),
                0 ,
                0,
                this.width, this.height,
                39,27);
    }

    private void renderIcons(DrawContext context) {

        context.drawItemWithoutEntity(this.icon, this.getX() + 18, this.getY() + 6);
    }


    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 0.7f));
    }
}

package TCOTS.screen;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.AlchemyTableBlock;
import TCOTS.screen.recipebook.widget.AlchemyRecipeBookButtonTextured;
import TCOTS.screen.recipebook.widget.AlchemyRecipeBookWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class AlchemyTableScreen extends HandledScreen<AlchemyTableScreenHandler> {

    public static final ButtonTextures BUTTON_TEXTURES =
            new ButtonTextures(
                    //Enabled
                    Identifier.of(TCOTS_Main.MOD_ID, "buttons/recipe_book_button"),
                    //Disabled
                    Identifier.of(TCOTS_Main.MOD_ID, "buttons/recipe_book_disabled"),
                    //EnabledFocus
                    Identifier.of(TCOTS_Main.MOD_ID, "buttons/recipe_book_button_highlighted"),
                    //DisableFocus
                    Identifier.of(TCOTS_Main.MOD_ID, "buttons/recipe_book_disabled")

            );
    public static final Identifier SCREEN_BACKGROUND =
            Identifier.of(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table.png");

    private final AlchemyRecipeBookWidget recipeBook = new AlchemyRecipeBookWidget();

    private AlchemyRecipeBookButtonTextured buttonWidget;

    public AlchemyTableScreen(AlchemyTableScreenHandler handler,  PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight=189;
        this.playerInventoryTitleY=this.backgroundHeight-94;
    }

    @Override
    protected void init() {
        super.init();
        assert this.client != null;


        this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
        this.buttonWidget =
                new AlchemyRecipeBookButtonTextured(
                        this.x + 5, this.height / 2 - 39,
                        20, 18,
                        BUTTON_TEXTURES,

                        //Action when press
                        button -> {
                            recipeBook.toggleOpen();
                            this.x = recipeBook.findLeftEdge(this.width, this.backgroundWidth);
                            button.setPosition(this.x + 5, this.height / 2 - 39);
                        });

        addDrawableChild(buttonWidget);
        recipeBook.init(this.height,this.width, this.textRenderer, this.client, this.handler);


        this.addSelectableChild(this.recipeBook);
        this.setInitialFocus(this.recipeBook);
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        recipeBook.drawBackground(context, delta, mouseX, mouseY);
        int i = this.x;
        int j =(this.height - this.backgroundHeight) / 2;

        context.drawTexture(SCREEN_BACKGROUND, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        recipeBook.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.recipeBook.update();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if(conditionForBook()){
        buttonWidget.active = true;
        } else {
            if(recipeBook.isOpen()){
                this.x = recipeBook.findLeftEdge(this.width, this.backgroundWidth);
                buttonWidget.setPosition(this.x + 5, this.height / 2 - 39);
            }
            buttonWidget.active = false;
        }

        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private boolean conditionForBook(){
        return Objects.requireNonNull(this.getScreenHandler().getBlockEntity().getWorld()).getBlockState(getScreenHandler().getBlockEntity().getPos()).get(AlchemyTableBlock.HAS_ALCHEMY_BOOK);
    }

}

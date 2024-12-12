package TCOTS.screen;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.AlchemyTableBlock;
import TCOTS.screen.recipebook.widget.AlchemyRecipeBookButtonTextured;
import TCOTS.screen.recipebook.widget.AlchemyRecipeBookWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class AlchemyTableScreen extends HandledScreen<AlchemyTableScreenHandler> {

    public static final Identifier recipeBookIcon = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/buttons/recipe_book_button_old.png");
    public static final Identifier recipeBookIconDisabled = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/buttons/recipe_book_disabled.png");

    public static final Identifier SCREEN_BACKGROUND =
            new Identifier(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table.png");

    private final AlchemyRecipeBookWidget recipeBook = new AlchemyRecipeBookWidget();

    private AlchemyRecipeBookButtonTextured buttonWidgetActive;
    private AlchemyRecipeBookButtonTextured buttonWidgetDisabled;

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
        this.buttonWidgetActive =
                new AlchemyRecipeBookButtonTextured(
                        this.x + 5, this.height / 2 - 39,
                        20, 18,
                        0,0,
                        18,
                        recipeBookIcon,

                        20,36,

                        //Action when press
                        button -> {
                            recipeBook.toggleOpen();
                            this.x = recipeBook.findLeftEdge(this.width, this.backgroundWidth);
                            button.setPosition(this.x + 5, this.height / 2 - 39);
                        });

        this.buttonWidgetDisabled =
                new AlchemyRecipeBookButtonTextured(
                        this.x + 5, this.height / 2 - 39,
                        20, 18,
                        0,0,
                        0,
                        recipeBookIconDisabled,

                        20,18,

                        //Action when press
                        button -> {

                        }
                );

        addDrawableChild(buttonWidgetActive);
        addDrawableChild(buttonWidgetDisabled);

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
        if(conditionForBook()){
        buttonWidgetActive.active = true;

        buttonWidgetActive.visible=true;
        buttonWidgetDisabled.visible=false;
        } else {
            if(recipeBook.isOpen()){
                this.x = recipeBook.findLeftEdge(this.width, this.backgroundWidth);
                buttonWidgetActive.setPosition(this.x + 5, this.height / 2 - 39);
            }

            buttonWidgetActive.active = false;

            buttonWidgetActive.visible=false;
            buttonWidgetDisabled.visible=true;
        }

        buttonWidgetDisabled.active=false;

        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private boolean conditionForBook(){
        return Objects.requireNonNull(this.getScreenHandler().getBlockEntity().getWorld()).getBlockState(getScreenHandler().getBlockEntity().getPos()).get(AlchemyTableBlock.HAS_ALCHEMY_BOOK);
    }

}

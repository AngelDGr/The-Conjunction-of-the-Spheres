package TCOTS.screen;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.AlchemyTableBlock;
import TCOTS.screen.recipebook.AlchemyRecipeBookButtonTextured;
import TCOTS.screen.recipebook.widget.AlchemyRecipeBookWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableScreenHandler> {

    public static final WidgetSprites BUTTON_TEXTURES =
            new WidgetSprites(
                    //Enabled
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/recipe_book_button"),
                    //Disabled
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/recipe_book_disabled"),
                    //EnabledFocus
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/recipe_book_button_highlighted"),
                    //DisableFocus
                    ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/recipe_book_disabled")

            );
    public static final ResourceLocation SCREEN_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table.png");

    private final AlchemyRecipeBookWidget recipeBook = new AlchemyRecipeBookWidget();

    private AlchemyRecipeBookButtonTextured buttonWidget;

    public AlchemyTableScreen(AlchemyTableScreenHandler handler,  Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageHeight=189;
        this.inventoryLabelY=this.imageHeight-94;
    }

    @Override
    protected void init() {
        super.init();
        assert this.minecraft != null;


        this.leftPos = this.recipeBook.findLeftEdge(this.width, this.imageWidth);
        this.buttonWidget =
                new AlchemyRecipeBookButtonTextured(
                        this.leftPos + 5, this.height / 2 - 39,
                        20, 18,
                        BUTTON_TEXTURES,

                        //Action when press
                        button -> {
                            recipeBook.toggleOpen();
                            this.leftPos = recipeBook.findLeftEdge(this.width, this.imageWidth);
                            button.setPosition(this.leftPos + 5, this.height / 2 - 39);
                        });

        addRenderableWidget(buttonWidget);
        recipeBook.init(this.height,this.width, this.font, this.minecraft, this.menu);


        this.addWidget(this.recipeBook);
        this.setInitialFocus(this.recipeBook);
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics context, float delta, int mouseX, int mouseY) {
        recipeBook.drawBackground(context, delta, mouseX, mouseY);
        int i = this.leftPos;
        int j =(this.height - this.imageHeight) / 2;

        context.blit(SCREEN_BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
        recipeBook.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.recipeBook.update();
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if(conditionForBook()){
        buttonWidget.active = true;
        } else {
            if(recipeBook.isOpen()){
                this.leftPos = recipeBook.findLeftEdge(this.width, this.imageWidth);
                buttonWidget.setPosition(this.leftPos + 5, this.height / 2 - 39);
            }
            buttonWidget.active = false;
        }

        renderTooltip(context, mouseX, mouseY);
    }

    private boolean conditionForBook(){
        return Objects.requireNonNull(this.getMenu().getBlockEntity().getLevel()).getBlockState(getMenu().getBlockEntity().getBlockPos()).getValue(AlchemyTableBlock.HAS_ALCHEMY_BOOK);
    }

}

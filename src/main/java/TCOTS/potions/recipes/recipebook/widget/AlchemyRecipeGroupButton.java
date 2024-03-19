package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.potions.recipes.AlchemyTableRecipeCategory;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;


public class AlchemyRecipeGroupButton extends ToggleButtonWidget {
    //TODO: Fix this
    private final AlchemyTableRecipeCategory category;
    ItemStack icon;
    private float bounce;

    public AlchemyRecipeGroupButton(ItemStack icon, AlchemyTableRecipeCategory category) {
        super(0, 0, 39, 27, false);

        this.icon=icon;
        this.category = category;
//        this.setTextureUV(153, 2, 39, 0, AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE);

    }

    public void checkForNewRecipes(MinecraftClient client) {
        ClientRecipeBook clientRecipeBook = client.player.getRecipeBook();
    }

    public AlchemyTableRecipeCategory getCategory(){
        return this.category;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
//        if (this.bounce > 0.0f) {
//            float f = 1.0f + 0.1f * (float)Math.sin(this.bounce / 15.0f * (float)Math.PI);
//            context.getMatrices().push();
//            context.getMatrices().translate(this.getX() + 8, this.getY() + 12, 0.0f);
//            context.getMatrices().scale(1.0f, f, 1.0f);
//            context.getMatrices().translate(-(this.getX() + 8), -(this.getY() + 12), 0.0f);
//        }
//        MinecraftClient minecraftClient = MinecraftClient.getInstance();
////        RenderSystem.disableDepthTest();
//        int i = this.u;
//        int j = this.v;
//        if (this.toggled) {
//            i += this.pressedUOffset;
//        }
//        if (this.isSelected()) {
//            j += this.hoverVOffset;
//        }
//        int k = this.getX();
//        if (this.toggled) {
//            k -= 2;
//        }
//
//        context.drawTexture(this.texture, k, this.getY(), i, j, this.width, this.height);
//
////        RenderSystem.enableDepthTest();
//        this.renderIcons(context, minecraftClient.getItemRenderer());
//        if (this.bounce > 0.0f) {
//            context.getMatrices().pop();
//            this.bounce -= delta;
//        }
        if (this.textures == null) {
            return;
        }
        RenderSystem.disableDepthTest();
        context.drawGuiTexture(this.textures.get(this.toggled, this.isSelected()), this.getX(), this.getY(), this.width, this.height);
        RenderSystem.enableDepthTest();
    }

    private void renderIcons(DrawContext context, ItemRenderer itemRenderer) {
        int i;
//        List<ItemStack> list = this.category.getIcons();
        int n = i = this.toggled ? -2 : 0;
//        if (list.size() == 1) {
            context.drawItemWithoutEntity(this.icon, this.getX() + 17 + i, this.getY() + 5);
////        } else if (list.size() == 2) {
//            context.drawItemWithoutEntity(iconslist.get(0), this.getX() + 3 + i, this.getY() + 5);
//            context.drawItemWithoutEntity(iconslist.get(1), this.getX() + 14 + i, this.getY() + 5);
//        }
    }


    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 0.7f));
    }
}

package TCOTS.items.potions.recipes.recipebook.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class AlchemyRecipeBookButton extends PressableWidget {
    public static final int DEFAULT_WIDTH_SMALL = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 20;
    protected static final AlchemyRecipeBookButton.NarrationSupplier DEFAULT_NARRATION_SUPPLIER = Supplier::get;
    protected final AlchemyRecipeBookButton.PressAction onPress;
    protected final AlchemyRecipeBookButton.NarrationSupplier narrationSupplier;

    public static AlchemyRecipeBookButton.Builder builder(Text message, AlchemyRecipeBookButton.PressAction onPress) {
        return new AlchemyRecipeBookButton.Builder(message, onPress);
    }

    protected AlchemyRecipeBookButton(int x, int y, int width, int height, Text message, AlchemyRecipeBookButton.PressAction onPress, AlchemyRecipeBookButton.NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message);
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return this.narrationSupplier.createNarrationMessage(super::getNarrationMessage);
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Environment(value=EnvType.CLIENT)
    public static class Builder {
        private final Text message;
        private final AlchemyRecipeBookButton.PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private AlchemyRecipeBookButton.NarrationSupplier narrationSupplier = DEFAULT_NARRATION_SUPPLIER;

        public Builder(Text message, AlchemyRecipeBookButton.PressAction onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public AlchemyRecipeBookButton.Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public AlchemyRecipeBookButton.Builder width(int width) {
            this.width = width;
            return this;
        }

        public AlchemyRecipeBookButton.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public AlchemyRecipeBookButton.Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public AlchemyRecipeBookButton.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public AlchemyRecipeBookButton.Builder narrationSupplier(AlchemyRecipeBookButton.NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public AlchemyRecipeBookButton build() {
            AlchemyRecipeBookButton AlchemyRecipeBookButton = new AlchemyRecipeBookButton(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
            AlchemyRecipeBookButton.setTooltip(this.tooltip);
            return AlchemyRecipeBookButton;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public interface PressAction {
        void onPress(AlchemyRecipeBookButton var1);
    }

    @Environment(value=EnvType.CLIENT)
    public interface NarrationSupplier {
        MutableText createNarrationMessage(Supplier<MutableText> var1);
    }
}
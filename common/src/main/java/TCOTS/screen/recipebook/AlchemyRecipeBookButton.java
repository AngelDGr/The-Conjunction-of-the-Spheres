package TCOTS.screen.recipebook;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class AlchemyRecipeBookButton extends AbstractButton {
    public static final int DEFAULT_WIDTH_SMALL = 120;
    public static final int DEFAULT_WIDTH = 150;
    public static final int DEFAULT_HEIGHT = 20;
    protected static final AlchemyRecipeBookButton.NarrationSupplier DEFAULT_NARRATION_SUPPLIER = Supplier::get;
    protected final AlchemyRecipeBookButton.PressAction onPress;
    protected final AlchemyRecipeBookButton.NarrationSupplier narrationSupplier;

    public static AlchemyRecipeBookButton.Builder builder(final Component message, final AlchemyRecipeBookButton.PressAction onPress) {
        return new AlchemyRecipeBookButton.Builder(message, onPress);
    }

    protected AlchemyRecipeBookButton(final int x, final int y, final int width, final int height, final Component message, final AlchemyRecipeBookButton.PressAction onPress, final AlchemyRecipeBookButton.NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message);
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    protected @NotNull MutableComponent createNarrationMessage() {
        return this.narrationSupplier.createNarrationMessage(super::createNarrationMessage);
    }

    @Override
    public void updateWidgetNarration(@NotNull final NarrationElementOutput builder) {
        this.defaultButtonNarrationText(builder);
    }

    public static class Builder {
        private final Component message;
        private final AlchemyRecipeBookButton.PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private AlchemyRecipeBookButton.NarrationSupplier narrationSupplier = DEFAULT_NARRATION_SUPPLIER;

        public Builder(final Component message, final AlchemyRecipeBookButton.PressAction onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public AlchemyRecipeBookButton.Builder position(final int x, final int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public AlchemyRecipeBookButton.Builder width(final int width) {
            this.width = width;
            return this;
        }

        public AlchemyRecipeBookButton.Builder size(final int width, final int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public AlchemyRecipeBookButton.Builder dimensions(final int x, final int y, final int width, final int height) {
            return this.position(x, y).size(width, height);
        }

        public AlchemyRecipeBookButton.Builder tooltip(@Nullable final Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public AlchemyRecipeBookButton.Builder narrationSupplier(final AlchemyRecipeBookButton.NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public AlchemyRecipeBookButton build() {
            final AlchemyRecipeBookButton AlchemyRecipeBookButton = new AlchemyRecipeBookButton(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
            AlchemyRecipeBookButton.setTooltip(this.tooltip);
            return AlchemyRecipeBookButton;
        }
    }

    public interface PressAction {
        void onPress(AlchemyRecipeBookButton var1);
    }

    public interface NarrationSupplier {
        MutableComponent createNarrationMessage(Supplier<MutableComponent> var1);
    }
}
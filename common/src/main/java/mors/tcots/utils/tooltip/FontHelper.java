package mors.tcots.utils.tooltip;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.google.common.base.Strings;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
public final class FontHelper {

    public static final int MAX_WIDTH_PER_LINE = 200;

    private FontHelper() {}

    public static Style styleFromColor(final ChatFormatting color) {
        return Style.EMPTY.applyFormat(color);
    }

    public static Style styleFromColor(final int hex) {
        return Style.EMPTY.withColor(hex);
    }

    public static List<Component> cutStringTextComponent(final String s, final Palette palette) {
        return cutTextComponent(Component.literal(s), palette);
    }

    public static List<Component> cutTextComponent(final Component c, final Palette palette) {
        return cutTextComponent(c, palette.primary(), palette.highlight());
    }

    public static List<Component> cutStringTextComponent(final String s, final Style primaryStyle,
                                                         final Style highlightStyle) {
        return cutTextComponent(Component.literal(s), primaryStyle, highlightStyle);
    }

    public static List<Component> cutTextComponent(final Component c, final Style primaryStyle,
                                                   final Style highlightStyle) {
        return cutTextComponent(c, primaryStyle, highlightStyle, 0);
    }

    public static List<Component> cutStringTextComponent(final String c, final Style primaryStyle,
                                                         final Style highlightStyle, final int indent) {
        return cutTextComponent(Component.literal(c), primaryStyle, highlightStyle, indent);
    }

    public static List<Component> cutTextComponent(final Component c, final Style primaryStyle,
                                                   final Style highlightStyle, final int indent) {
        final String s = c.getString();

        // Split words
        final List<String> words = new LinkedList<>();


        final BreakIterator iterator = BreakIterator.getLineInstance(getCurrentLocale(Minecraft.getInstance().getLanguageManager().getSelected()));
        iterator.setText(s);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            final String word = s.substring(start, end);
            words.add(word);
        }

        // Apply hard wrap
        final Font font = Minecraft.getInstance().font;
        final List<String> lines = new LinkedList<>();
        StringBuilder currentLine = new StringBuilder();
        int width = 0;
        for (final String word : words) {
            final int newWidth = font.width(word.replaceAll("_", ""));
            if (width + newWidth > MAX_WIDTH_PER_LINE) {
                if (width > 0) {
                    final String line = currentLine.toString();
                    lines.add(line);
                    currentLine = new StringBuilder();
                    width = 0;
                } else {
                    lines.add(word);
                    continue;
                }
            }
            currentLine.append(word);
            width += newWidth;
        }
        if (width > 0) {
            lines.add(currentLine.toString());
        }

        // Format
        final MutableComponent lineStart = Component.literal(Strings.repeat(" ", indent));
        lineStart.withStyle(primaryStyle);
        final List<Component> formattedLines = new ArrayList<>(lines.size());
        final Couple<Style> styles = Couple.create(highlightStyle, primaryStyle);

        boolean currentlyHighlighted = false;
        for (final String string : lines) {
            final MutableComponent currentComponent = lineStart.plainCopy();
            final String[] split = string.split("_");
            for (final String part : split) {
                currentComponent.append(Component.literal(part).withStyle(styles.get(currentlyHighlighted)));
                currentlyHighlighted = !currentlyHighlighted;
            }

            formattedLines.add(currentComponent);
            currentlyHighlighted = !currentlyHighlighted;
        }

        return formattedLines;
    }

    public record Palette(Style primary, Style highlight) {
        public static final Palette STANDARD_CREATE = new Palette(styleFromColor(0xC9974C), styleFromColor(0xF1DD79));

        public static final Palette BLUE = ofColors(ChatFormatting.BLUE, ChatFormatting.AQUA);
        public static final Palette GREEN = ofColors(ChatFormatting.DARK_GREEN, ChatFormatting.GREEN);
        public static final Palette YELLOW = ofColors(ChatFormatting.GOLD, ChatFormatting.YELLOW);
        public static final Palette RED = ofColors(ChatFormatting.DARK_RED, ChatFormatting.RED);
        public static final Palette PURPLE = ofColors(ChatFormatting.DARK_PURPLE, ChatFormatting.LIGHT_PURPLE);
        public static final Palette GRAY = ofColors(ChatFormatting.DARK_GRAY, ChatFormatting.GRAY);

        public static final Palette ALL_GRAY = ofColors(ChatFormatting.GRAY, ChatFormatting.GRAY);
        public static final Palette ALL_GRAY_ITALIC = ofColors(ChatFormatting.GRAY, ChatFormatting.GRAY, true, true);
        public static final Palette GRAY_AND_BLUE = ofColors(ChatFormatting.GRAY, ChatFormatting.BLUE);
        public static final Palette GRAY_AND_WHITE = ofColors(ChatFormatting.GRAY, ChatFormatting.WHITE);
        public static final Palette GRAY_AND_GOLD = ofColors(ChatFormatting.GRAY, ChatFormatting.GOLD);
        public static final Palette GRAY_AND_RED = ofColors(ChatFormatting.GRAY, ChatFormatting.RED);

        public static Palette ofColors(final ChatFormatting primary, final ChatFormatting highlight) {
            return new Palette(styleFromColor(primary), styleFromColor(highlight));
        }

        public static Palette ofColors(final ChatFormatting primary, final ChatFormatting highlight, final boolean primaryItalic, final boolean highlightItalic) {
            return new Palette(styleFromColor(primary).withItalic(primaryItalic), styleFromColor(highlight).withItalic(highlightItalic));
        }
    }

    public static Locale getCurrentLocale(final String selected) {
        final String[] langSplit = selected.split("_", 2);
        final java.util.Locale javaLocale = langSplit.length == 1 ? new java.util.Locale(langSplit[0]) : new java.util.Locale(langSplit[0], langSplit[1]);
        return javaLocale;
    }
}

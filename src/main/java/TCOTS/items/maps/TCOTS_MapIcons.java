package TCOTS.items.maps;

import com.mojang.serialization.Codec;
import net.minecraft.block.MapColor;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public record TCOTS_MapIcons(net.minecraft.item.map.MapIcon.Type type, byte x, byte z, byte rotation, @Nullable Text text) {
    public byte getTypeId() {
        return this.type.getId();
    }

    public boolean isAlwaysRendered() {
        return this.type.isAlwaysRendered();
    }

    @Nullable
    public Text text() {
        return this.text;
    }

    public enum Type implements StringIdentifiable
    {
        GIANT_CAVE("giant_cave", true, 3830373, false, true);

        public static final Codec<net.minecraft.item.map.MapIcon.Type> CODEC;
        private final String name;
        private final byte id;
        private final boolean alwaysRender;
        private final int tintColor;
        private final boolean structure;
        private final boolean useIconCountLimit;

        private Type(String name, boolean alwaysRender, boolean useIconCountLimit) {
            this(name, alwaysRender, -1, useIconCountLimit, false);
        }

        private Type(String name, boolean alwaysRender, int tintColor, boolean useIconCountLimit, boolean structure) {
            this.name = name;
            this.useIconCountLimit = useIconCountLimit;
            this.id = (byte)this.ordinal();
            this.alwaysRender = alwaysRender;
            this.tintColor = tintColor;
            this.structure = structure;
        }

        public byte getId() {
            return this.id;
        }

        public boolean isStructure() {
            return this.structure;
        }

        public boolean isAlwaysRendered() {
            return this.alwaysRender;
        }

        public boolean hasTintColor() {
            return this.tintColor >= 0;
        }

        public int getTintColor() {
            return this.tintColor;
        }

        public static net.minecraft.item.map.MapIcon.Type byId(byte id) {
            return net.minecraft.item.map.MapIcon.Type.values()[MathHelper.clamp(id, 0, net.minecraft.item.map.MapIcon.Type.values().length - 1)];
        }

        public boolean shouldUseIconCountLimit() {
            return this.useIconCountLimit;
        }

        @Override
        public String asString() {
            return this.name;
        }

        static {
            CODEC = StringIdentifiable.createCodec(net.minecraft.item.map.MapIcon.Type::values);
        }
    }
}

package TCOTS.items.maps;

import TCOTS.TCOTS_Main;
import com.mojang.serialization.Codec;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapIcon;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public record TCOTS_MapIcons(Type type, byte x, byte z, byte rotation, @Nullable Text text) {
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
        GIANT_CAVE("giant_cave", true, 9615870, false, true);

        public static final Codec<Type> CODEC;
        private final String name;
        private final byte id;
        private final boolean alwaysRender;
        private final int tintColor;
        private final boolean structure;
        private final boolean useIconCountLimit;

        Type(String name, boolean alwaysRender, boolean useIconCountLimit) {
            this(name, alwaysRender, -1, useIconCountLimit, false);
        }

        Type(String name, boolean alwaysRender, int tintColor, boolean useIconCountLimit, boolean structure) {
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

        public Identifier getTexture(){
            return new Identifier(TCOTS_Main.MOD_ID, "textures/gui/sprites/map_icons/"+name+".png");
        }

        public boolean hasTintColor() {
            return this.tintColor >= 0;
        }

        public int getTintColor() {
            return this.tintColor;
        }

        public static Type byId(byte id) {
            return Type.values()[MathHelper.clamp(id, 0, Type.values().length - 1)];
        }

        public boolean shouldUseIconCountLimit() {
            return this.useIconCountLimit;
        }

        @Override
        public String asString() {
            return this.name;
        }

        static {
            CODEC = StringIdentifiable.createCodec(Type::values);
        }
    }

    public static void addCustomStructureIconsNbt(@NotNull ItemStack stack, BlockPos pos, String id, TCOTS_MapIcons.Type customType){
        addCustomIconsNbt(stack, pos, id, MapIcon.Type.MANSION, customType);
    }

    public static void addCustomIconsNbt(@NotNull ItemStack stack, BlockPos pos, String id, MapIcon.Type type, TCOTS_MapIcons.Type customType) {
        NbtList nbtList;
        if (stack.hasNbt() && Objects.requireNonNull(stack.getNbt()).contains("CustomIcons", NbtElement.LIST_TYPE)) {
            nbtList = stack.getNbt().getList("CustomIcons", NbtElement.COMPOUND_TYPE);
        } else {
            nbtList = new NbtList();
            stack.setSubNbt("CustomIcons", nbtList);
        }

        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putByte("customType", customType.getId());
        nbtCompound.putByte("type", type.getId());
        nbtCompound.putString("id", id);
        nbtCompound.putDouble("x", pos.getX());
        nbtCompound.putDouble("z", pos.getZ());
        nbtCompound.putDouble("rot", 180.0);
        nbtList.add(nbtCompound);

        if(customType.hasTintColor()) {
            NbtCompound nbtCompound2 = stack.getOrCreateSubNbt("display");
            nbtCompound2.putInt("MapColor", customType.getTintColor());
        }
    }
}

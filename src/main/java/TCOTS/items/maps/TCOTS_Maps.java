package TCOTS.items.maps;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class TCOTS_Maps {

    public static void addCustomIconsNbt(@NotNull ItemStack stack, BlockPos pos, String id, TCOTS_MapIcons.Type type) {
        NbtList nbtList;
        if (stack.hasNbt() && stack.getNbt().contains("CustomIcons", NbtElement.LIST_TYPE)) {
            nbtList = stack.getNbt().getList("CustomIcons", NbtElement.COMPOUND_TYPE);
        } else {
            nbtList = new NbtList();
            stack.setSubNbt("CustomIcons", nbtList);
        }
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putByte("type", type.getId());
        nbtCompound.putString("id", id);
        nbtCompound.putDouble("x", pos.getX());
        nbtCompound.putDouble("z", pos.getZ());
        nbtCompound.putDouble("rot", 180.0);
        nbtList.add(nbtCompound);
        if (type.hasTintColor()) {
            NbtCompound nbtCompound2 = stack.getOrCreateSubNbt("display");
            nbtCompound2.putInt("MapColor", type.getTintColor());
        }
    }
}

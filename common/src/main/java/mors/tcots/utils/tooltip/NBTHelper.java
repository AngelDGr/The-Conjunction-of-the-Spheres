package mors.tcots.utils.tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class NBTHelper {

	public static void putMarker(final CompoundTag nbt, final String marker) {
		nbt.putBoolean(marker, true);
	}
	
	// Backwards compatible with 1.20
	public static BlockPos readBlockPos(final CompoundTag nbt, final String key) {
		final Optional<BlockPos> pos = NbtUtils.readBlockPos(nbt, key);
		if (pos.isPresent())
			return pos.get();
		final CompoundTag oldTag = nbt.getCompound(key);
		return new BlockPos(oldTag.getInt("X"), oldTag.getInt("Y"), oldTag.getInt("Z"));
	}

	public static <T extends Enum<?>> T readEnum(final CompoundTag nbt, final String key, final Class<T> enumClass) {
		final T[] enumConstants = enumClass.getEnumConstants();
		if (enumConstants == null)
			throw new IllegalArgumentException("Non-Enum class passed to readEnum: " + enumClass.getName());
		if (nbt.contains(key, Tag.TAG_STRING)) {
			final String name = nbt.getString(key);
			for (final T t : enumConstants) {
				if (t.name()
					.equals(name))
					return t;
			}
		}
		return enumConstants[0];
	}

	public static <T extends Enum<?>> void writeEnum(final CompoundTag nbt, final String key, final T enumConstant) {
		nbt.putString(key, enumConstant.name());
	}

	public static <T> ListTag writeCompoundList(final Iterable<T> list, final Function<T, CompoundTag> serializer) {
		final ListTag listNBT = new ListTag();
		list.forEach(t -> {
			final CompoundTag apply = serializer.apply(t);
			if (apply == null)
				return;
			listNBT.add(apply);
		});
		return listNBT;
	}

	public static <T> List<T> readCompoundList(final ListTag listNBT, final Function<CompoundTag, T> deserializer) {
		final List<T> list = new ArrayList<>(listNBT.size());
		listNBT.forEach(inbt -> list.add(deserializer.apply((CompoundTag) inbt)));
		return list;
	}

	public static void iterateCompoundList(final ListTag listNBT, final Consumer<CompoundTag> consumer) {
		listNBT.forEach(inbt -> consumer.accept((CompoundTag) inbt));
	}

	public static ListTag writeItemList(final Iterable<ItemStack> stacks, final HolderLookup.Provider registries) {
		final ListTag listNBT = new ListTag();
		for (final ItemStack stack : stacks)
			listNBT.add(stack.saveOptional(registries));
		return listNBT;
	}

	public static List<ItemStack> readItemList(final ListTag stacks, final HolderLookup.Provider registries) {
		final List<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < stacks.size(); i++)
			list.add(i, ItemStack.parseOptional(registries, stacks.getCompound(i)));
		return list;
	}

	public static ListTag writeAABB(final AABB bb) {
		final ListTag bbtag = new ListTag();
		bbtag.add(FloatTag.valueOf((float) bb.minX));
		bbtag.add(FloatTag.valueOf((float) bb.minY));
		bbtag.add(FloatTag.valueOf((float) bb.minZ));
		bbtag.add(FloatTag.valueOf((float) bb.maxX));
		bbtag.add(FloatTag.valueOf((float) bb.maxY));
		bbtag.add(FloatTag.valueOf((float) bb.maxZ));
		return bbtag;
	}

	@Nullable
	public static AABB readAABB(final ListTag bbTag) {
		if (bbTag.isEmpty())
			return null;
		return new AABB(bbTag.getFloat(0), bbTag.getFloat(1), bbTag.getFloat(2), bbTag.getFloat(3),
			bbTag.getFloat(4), bbTag.getFloat(5));
	}

	public static ListTag writeVec3i(final Vec3i vec) {
		final ListTag tag = new ListTag();
		tag.add(IntTag.valueOf(vec.getX()));
		tag.add(IntTag.valueOf(vec.getY()));
		tag.add(IntTag.valueOf(vec.getZ()));
		return tag;
	}

	public static Vec3i readVec3i(final ListTag tag) {
		return new Vec3i(tag.getInt(0), tag.getInt(1), tag.getInt(2));
	}

	@NotNull
	public static Tag getINBT(final CompoundTag nbt, final String id) {
		final Tag inbt = nbt.get(id);
		if (inbt != null)
			return inbt;
		return new CompoundTag();
	}

	public static CompoundTag intToCompound(final int i) {
		final CompoundTag compoundTag = new CompoundTag();
		compoundTag.putInt("V", i);
		return compoundTag;
	}

	public static int intFromCompound(final CompoundTag compoundTag) {
		return compoundTag.getInt("V");
	}

	public static void writeResourceLocation(final CompoundTag nbt, final String key, final ResourceLocation location) {
		nbt.putString(key, location.toString());
	}

	public static ResourceLocation readResourceLocation(final CompoundTag nbt, final String key) {
		return ResourceLocation.parse(nbt.getString(key));
	}

}

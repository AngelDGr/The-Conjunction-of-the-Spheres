package mors.tcots.utils.tooltip;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.codec.StreamCodec;

public class Couple<T> extends Pair<T, T> implements Iterable<T> {

	private static final Couple<Boolean> TRUE_AND_FALSE = Couple.create(true, false);

	protected Couple(final T first, final T second) {
		super(first, second);
	}

	public static <T> Couple<T> create(final T first, final T second) {
		return new Couple<>(first, second);
	}

	public static <T> Couple<T> create(final Supplier<T> factory) {
		return new Couple<>(factory.get(), factory.get());
	}

	public static <T> Couple<T> createWithContext(final Function<Boolean, T> factory) {
		return new Couple<>(factory.apply(true), factory.apply(false));
	}

	public T get(final boolean first) {
		return first ? getFirst() : getSecond();
	}

	public void set(final boolean first, final T value) {
		if (first)
			setFirst(value);
		else
			setSecond(value);
	}

	@Override
	public Couple<T> copy() {
		return create(first, second);
	}

	public <S> Couple<S> map(final Function<T, S> function) {
		return Couple.create(function.apply(first), function.apply(second));
	}

	public <S> Couple<S> mapNotNull(final Function<T, S> function) {
		return Couple.create(first != null ? function.apply(first) : null, second != null ? function.apply(second) : null);
	}

	public <S> Couple<S> mapWithContext(final BiFunction<T, Boolean, S> function) {
		return Couple.create(function.apply(first, true), function.apply(second, false));
	}

	public <S, R> Couple<S> mapWithParams(final BiFunction<T, R, S> function, final Couple<R> values) {
		return Couple.create(function.apply(first, values.first), function.apply(second, values.second));
	}

	public <S, R> Couple<S> mapNotNullWithParam(final BiFunction<T, R, S> function, final R value) {
		return Couple.create(first != null ? function.apply(first, value) : null,
				second != null ? function.apply(second, value) : null);
	}

	public boolean both(final Predicate<T> test) {
		return test.test(getFirst()) && test.test(getSecond());
	}

	public boolean either(final Predicate<T> test) {
		return test.test(getFirst()) || test.test(getSecond());
	}

	public void replace(final Function<T, T> function) {
		setFirst(function.apply(getFirst()));
		setSecond(function.apply(getSecond()));
	}

	public void replaceWithContext(final BiFunction<T, Boolean, T> function) {
		replaceWithParams(function, TRUE_AND_FALSE);
	}

	public <S> void replaceWithParams(final BiFunction<T, S, T> function, final Couple<S> values) {
		setFirst(function.apply(getFirst(), values.getFirst()));
		setSecond(function.apply(getSecond(), values.getSecond()));
	}

	@Override
	public void forEach(final Consumer<? super T> consumer) {
		consumer.accept(getFirst());
		consumer.accept(getSecond());
	}

	public void forEachWithContext(final BiConsumer<T, Boolean> consumer) {
		forEachWithParams(consumer, TRUE_AND_FALSE);
	}

	public <S> void forEachWithParams(final BiConsumer<T, S> function, final Couple<S> values) {
		function.accept(getFirst(), values.getFirst());
		function.accept(getSecond(), values.getSecond());
	}

	public Couple<T> swap() {
		return Couple.create(second, first);
	}

	public ListTag serializeEach(final Function<T, CompoundTag> serializer) {
		return NBTHelper.writeCompoundList(ImmutableList.of(first, second), serializer);
	}

	public static <S> Couple<S> deserializeEach(final ListTag list, final Function<CompoundTag, S> deserializer) {
		final List<S> readCompoundList = NBTHelper.readCompoundList(list, deserializer);
		return new Couple<>(readCompoundList.get(0), readCompoundList.get(1));
	}

	public static <T> Codec<Couple<T>> codec(final Codec<T> codec) {
		return RecordCodecBuilder.create(instance -> instance.group(
			codec.fieldOf("first").forGetter(Couple::getFirst),
			codec.fieldOf("second").forGetter(Couple::getSecond)
		).apply(instance, Couple::new));
	}

	public static <B, T> StreamCodec<B, Couple<T>> streamCodec(final StreamCodec<? super B, T> codec) {
		return StreamCodec.composite(
			codec, Couple::getFirst,
			codec, Couple::getSecond,
			Couple::new
		);
	}

	@Override
	public Iterator<T> iterator() {
		return new Couplerator<>(this);
	}

	public Stream<T> stream() {
		return Stream.of(first, second);
	}

	private static class Couplerator<T> implements Iterator<T> {

		int state;
		private final Couple<T> couple;

		public Couplerator(final Couple<T> couple) {
			this.couple = couple;
			state = 0;
		}

		@Override
		public boolean hasNext() {
			return state != 2;
		}

		@Override
		public T next() {
			state++;
			if (state == 1)
				return couple.first;
			if (state == 2)
				return couple.second;
			return null;
		}

	}

}

package mors.tcots.utils.tooltip;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class Pair<F, S> {

	F first;
	S second;

	protected Pair(final F first, final S second) {
		this.first = first;
		this.second = second;
	}

	public static <F, S> Pair<F, S> of(final F first, final S second) {
		return new Pair<>(first, second);
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public void setFirst(final F first) {
		this.first = first;
	}

	public void setSecond(final S second) {
		this.second = second;
	}

	public Pair<F, S> copy() {
		return Pair.of(first, second);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Pair<?, ?> other) {
            return Objects.equals(first, other.first) && Objects.equals(second, other.second);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (nullHash(first) * 31) ^ nullHash(second);
	}

	int nullHash(final Object o) {
		return o == null ? 0 : o.hashCode();
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public Pair<S, F> swap() {
		return Pair.of(second, first);
	}

	public static <F, S> Codec<Pair<F, S>> codec(final Codec<F> firstCodec, final Codec<S> secondCodec) {
		return RecordCodecBuilder.create(instance -> instance.group(
			firstCodec.fieldOf("first").forGetter(Pair::getFirst),
			secondCodec.fieldOf("second").forGetter(Pair::getSecond)
		).apply(instance, Pair::new));
	}

	public static <B, F, S> StreamCodec<B, Pair<F, S>> streamCodec(final StreamCodec<? super B, F> firstCodec, final StreamCodec<? super B, S> secondCodec) {
		return StreamCodec.composite(
			firstCodec, Pair::getFirst,
			secondCodec, Pair::getSecond,
			Pair::new
		);
	}
}

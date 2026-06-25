package net.minecraft.util;

import java.util.Objects;

public final class Tuple<A, B> {
	private final A a;
	private final B b;

	public Tuple(A a, B b) {
		this.a = a;
		this.b = b;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tuple<?, ?> other)) {
			return false;
		}
		return Objects.equals(a, other.a) && Objects.equals(b, other.b);
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}

	@Override
	public String toString() {
		return "Tuple[" + a + ", " + b + "]";
	}
}

package com.semmle.bcache.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * A read-only array list.
 */
public final class Seq<T> extends AbstractList<T> {
	public static class Builder<T> {
		private final ArrayList<T> elements = new ArrayList<T>();

		public void add(T elem) {
			elements.add(elem);
		}

		public void addAll(Collection<T> elems) {
			elements.addAll(elems);
		}

		public Seq<T> build() {
			elements.trimToSize();
			return new Seq<T>(elements);
		}
	}

	private static Seq<Void> EMPTY = new Seq<Void>(new ArrayList<Void>(0));

	@SuppressWarnings("unchecked")
	public static <T> Seq<T> empty() {
		return (Seq<T>) EMPTY;
	}

	@JsonCreator
	public static <T> Seq<T> of(Iterable<T> elems) {
		if (elems instanceof Seq) {
			return (Seq<T>) elems;
		}
		ArrayList<T> javaList = new ArrayList<T>();
		for (T elem : elems) {
			javaList.add(elem);
		}
		javaList.trimToSize();
		return new Seq<T>(javaList);
	}

	public static <T> Seq<T> pair(T elem0, T elem1) {
		ArrayList<T> elems = new ArrayList<T>(1);
		elems.add(elem0);
		elems.add(elem1);
		return new Seq<T>(elems);
	}

	public static <T> Seq<T> singleton(T elem) {
		ArrayList<T> elems = new ArrayList<T>(1);
		elems.add(elem);
		return new Seq<T>(elems);
	}

	private final ArrayList<T> elements;

	private Seq(ArrayList<T> elements) {
		this.elements = elements;
	}

	@Override
	public boolean add(T elem) {
		throw new UnsupportedOperationException();
	}

	public T first() {
		return get(0);
	}

	@Override
	public T get(int idx) {
		return elements.get(idx);
	}

	public T last() {
		return get(size() - 1);
	}

	public T onlyElement() {
		if (size() != 1) {
			throw new ShouldBeImpossible("This sequence should have size 1");
		}
		return get(0);
	}

	@Override
	public int size() {
		return elements.size();
	}

	/**
	 * Take the first <code>n</code> elements of this squence.
	 */
	public Seq<T> take(int n) {
		return take(0, n);
	}

	/**
	 * Take the first <code>n</code> elements of this sequence starting at
	 * <code>start</code>.
	 */
	public Seq<T> take(int start, int n) {
		Seq.Builder<T> result = new Seq.Builder<T>();
		for (int i = start; i < start + n; i++) {
			result.add(get(i));
		}
		return result.build();
	}

}

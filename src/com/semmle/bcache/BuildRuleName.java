package com.semmle.bcache;

/**
 * The name of a build rule.
 */
public final class BuildRuleName implements Comparable<BuildRuleName> {
	public static BuildRuleName of(String name) {
		if (name == null) {
			return null;
		}
		return new BuildRuleName(name);
	}

	private final String name;

	private BuildRuleName(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}

	@Override
	public int compareTo(BuildRuleName other) {
		return name.compareTo(other.name);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof BuildRuleName)) {
			return false;
		}
		return name.equals(((BuildRuleName) other).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String name() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}

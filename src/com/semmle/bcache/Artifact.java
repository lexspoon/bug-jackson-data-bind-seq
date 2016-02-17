package com.semmle.bcache;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * A specification of a build artifact. It's either a file, a directory, or an
 * output from another build rule.
 */
public final class Artifact implements Comparable<Artifact> {
	private static final Pattern SpecifierPattern = Pattern.compile(
			"(([-a-zA-Z0-9_./+]+):)?([-a-zA-Z0-9_./+ ]+)");

	@JsonCreator
	public static Artifact parse(String specifier) {
		Matcher matcher = SpecifierPattern.matcher(specifier);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Ill-formed artifact specifier: " + specifier);
		}

		String ruleNameStr = matcher.group(2);
		String fileName = matcher.group(3);
		BuildRuleName ruleName;
		if (ruleNameStr == null) {
			ruleName = null;
		} else {
			ruleName = BuildRuleName.of(ruleNameStr);
		}
		if (fileName.startsWith("/")) {
			throw new IllegalArgumentException(
					"Artifact file names must be relative paths: " + specifier);
		}
		return new Artifact(ruleName, fileName);
	}

	private final String fileName;

	private final BuildRuleName ruleName;

	public Artifact(BuildRuleName ruleName, String fileName) {
		this.ruleName = ruleName;
		this.fileName = fileName;
	}

	@Override
	public int compareTo(Artifact other) {
		int result;

		// Compare by rule first
		if (ruleName == null) {
			if (other.ruleName() != null) {
				return -1;
			}
		} else {
			if (other.ruleName() == null) {
				return 1;
			}
			result = ruleName.compareTo(other.ruleName());
			if (result != 0) {
				return result;
			}
		}

		// Then compare by the output name
		return fileName().compareTo(other.fileName());
	}

	@Override
	public boolean equals(Object that) {
		if (!(that instanceof Artifact)) {
			return false;
		}
		return compareTo((Artifact) that) == 0;
	}

	public String fileName() {
		return fileName;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		if (ruleName != null) {
			hash = hash * 17 + ruleName.hashCode();
		}
		hash = hash * 17 + fileName.hashCode();
		return hash;
	}

	/**
	 * Whether this is an artifact for a tree, rather than for a build output.
	 */
	public boolean isTree() {
		return ruleName == null;
	}

	/**
	 * The rule from which this artifact is drawn. If it's null, then it's not
	 * associated with any rule.
	 */
	public BuildRuleName ruleName() {
		return ruleName;
	}

	public String specifier() {
		StringBuilder result = new StringBuilder();
		if (ruleName != null) {
			result.append(ruleName);
			result.append(":");
		}
		result.append(fileName);
		return result.toString();
	}

	public String toString() {
		return specifier();
	}
}

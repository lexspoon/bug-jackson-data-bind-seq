package com.semmle.bcache.util;

/**
 * An error that indicates some kind of bug in the implementation or
 * in the way it was deployed.
 */
public class ShouldBeImpossible extends RuntimeException {
	private static final long serialVersionUID = 4132771414092814913L;

	public ShouldBeImpossible(String message) {
		super(message);
	}

	public ShouldBeImpossible(Throwable throwable) {
		super(throwable);
	}

	public ShouldBeImpossible(String message, Throwable throwable) {
		super(message, throwable);
	}
}

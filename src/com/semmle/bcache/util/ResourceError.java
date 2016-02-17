package com.semmle.bcache.util;

/**
 * An error occurred while accessing a resource, usually a file.
 */
public class ResourceError extends RuntimeException {
	private static final long serialVersionUID = 4132771414092814913L;

	public ResourceError(String message) {
		super(message);
	}

	public ResourceError(Throwable throwable) {
		super(throwable);
	}

	public ResourceError(String message, Throwable throwable) {
		super(message, throwable);
	}
}

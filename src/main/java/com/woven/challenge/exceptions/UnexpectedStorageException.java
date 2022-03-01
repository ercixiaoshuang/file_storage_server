package com.woven.challenge.exceptions;

public class UnexpectedStorageException extends RuntimeException {

	public UnexpectedStorageException(String message) {
		super(message);
	}

	public UnexpectedStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}

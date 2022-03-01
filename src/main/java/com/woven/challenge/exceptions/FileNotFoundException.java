package com.woven.challenge.exceptions;

public class FileNotFoundException extends UnexpectedStorageException {

	public FileNotFoundException(String message) {
		super(message);
	}

	public FileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}

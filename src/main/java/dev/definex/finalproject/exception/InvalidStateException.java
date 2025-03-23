package dev.definex.finalproject.exception;

public class InvalidStateException extends RuntimeException {
	public InvalidStateException(String message) {
		super("InvalidStateException " + message);
	}	
}

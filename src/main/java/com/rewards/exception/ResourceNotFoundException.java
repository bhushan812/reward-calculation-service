package com.rewards.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

	private final String message;

	public ResourceNotFoundException(String message) {
		super();
		this.message = message;
	}

}

package com.rewards.DTO;


public class ErrorResponseDto {
	private String message;
	private String msgKey;
	public ErrorResponseDto(String message, String msgKey) {
		super();
		this.message = message;
		this.msgKey = msgKey;
	}

	public ErrorResponseDto() {
		super();

	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}
	
	
	
}

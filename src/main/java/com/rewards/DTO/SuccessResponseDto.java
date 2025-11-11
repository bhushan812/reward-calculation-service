package com.rewards.DTO;

import java.util.List;

public class SuccessResponseDto {

	private String message;
	private String msgKey;
	private List<RewardResponse> data;

	public SuccessResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SuccessResponseDto(String message, String msgKey, List<RewardResponse> data) {
		super();
		this.message = message;
		this.msgKey = msgKey;
		this.data = data;
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

	public List<RewardResponse> getData() {
		return data;
	}

	public void setData(List<RewardResponse> data) {
		this.data = data;
	}

}

package com.rewards.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data

public class CustomerRewardResponseDto {
	public CustomerRewardResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Long customerId;
	private String customerName;
	private Map<String, Integer> monthlyRewards;
	private Integer totalRewards;
	private List<TransactionResponseDto> transactions;

	public CustomerRewardResponseDto(Long customerId, String customerName, Map<String, Integer> monthlyRewards,
			Integer totalRewards, List<TransactionResponseDto> transactions) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.monthlyRewards = monthlyRewards;
		this.totalRewards = totalRewards;
		this.transactions = transactions;
	}

}

package com.rewards.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionResponseDto {

	private Long transactionId;

	private Double amount;

	private LocalDate transactionDate;

	private Integer pointsEarned;

}

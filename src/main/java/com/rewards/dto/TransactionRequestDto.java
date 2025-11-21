package com.rewards.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TransactionRequestDto {

	private Long customerId;
	private Double amount;
	private LocalDate date;
}

package com.rewards.service;

import java.util.List;

import com.rewards.dto.TransactionRequestDto;
import com.rewards.entity.TransactionEntity;

public interface TransactionService {

	void addTransaction(TransactionRequestDto transactionRequestDto);

	void updateTransaction(Long transactionId, TransactionRequestDto transactionRequestDto);

	void deleteTransaction(Long transactionId);

	List<TransactionEntity> getAllTransactions();

}

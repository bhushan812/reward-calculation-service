package com.rewards.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rewards.constants.Messages;
import com.rewards.dto.TransactionRequestDto;
import com.rewards.entity.CustomerRewardEntity;
import com.rewards.entity.TransactionEntity;
import com.rewards.exception.ResourceNotFoundException;
import com.rewards.repository.CustomerRewardRepository;
import com.rewards.repository.TransactionRepository;
import com.rewards.service.TransactionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionImpl implements TransactionService {

	@Autowired
	private CustomerRewardRepository customerRewardRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public void addTransaction(TransactionRequestDto transactionRequestDto) {
		log.info("Adding new transaction for customerId: {}", transactionRequestDto.getCustomerId());
		CustomerRewardEntity customerRewardEntity = customerRewardRepository
				.findById(transactionRequestDto.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException(Messages.CUSTOMER_NOT_FOUND_OR_NO_REWARDS));

		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setDate(transactionRequestDto.getDate());
		transactionEntity.setAmount(transactionRequestDto.getAmount());
		transactionEntity.setCustomerId(customerRewardEntity.getId());

		transactionRepository.save(transactionEntity);
		log.info("Transaction added successfully for customerId: {}", transactionRequestDto.getCustomerId());
	}

	@Override
	@Transactional
	public void updateTransaction(Long transactionId, TransactionRequestDto transactionRequestDto) {

		TransactionEntity transactionEntity = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new ResourceNotFoundException(Messages.TRANSACTION_NOT_FOUND));
		CustomerRewardEntity customerRewardEntity = customerRewardRepository
				.findById(transactionRequestDto.getCustomerId())
				.orElseThrow(() -> new ResourceNotFoundException(Messages.CUSTOMER_NOT_FOUND_OR_NO_REWARDS));

		transactionEntity.setAmount(transactionRequestDto.getAmount());
		transactionEntity.setCustomerId(customerRewardEntity.getId());
		transactionEntity.setDate(transactionRequestDto.getDate());

		transactionRepository.save(transactionEntity);
		log.info("Transaction {} updated successfully", transactionId);
	}

	@Override
	@Transactional
	public void deleteTransaction(Long transactionId) {

		transactionRepository.deleteById(transactionId);
		log.info("Transaction {} deleted successfully", transactionId);
	}

	@Override
	public List<TransactionEntity> getAllTransactions() {
		log.info("Fetching all transactions");
		return transactionRepository.findAll();
	}
}

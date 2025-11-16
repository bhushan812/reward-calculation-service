
package com.rewards.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
				.findById(transactionRequestDto.getCustomerId()).orElseThrow(() -> {

					return new ResourceNotFoundException(Messages.CUSTOMER_NOT_FOUND_OR_NO_REWARDS);
				});
		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setDate(transactionRequestDto.getDate());
		transactionEntity.setAmount(transactionRequestDto.getAmount());
		transactionEntity.setCustomerId(customerRewardEntity.getId());

		transactionRepository.save(transactionEntity);

		log.info("Transaction added successfully for customerId: {}", transactionRequestDto.getCustomerId());
	}

	@Override
	public void updateTransaction(Long id, TransactionRequestDto transactionRequestDto) {
		log.info("Updating transaction with ID: {}", transactionRequestDto.getTransactionId());

		TransactionEntity transactionEntity = transactionRepository.findById(transactionRequestDto.getTransactionId())
				.orElseThrow(() -> {
					log.error("Transaction not found with ID {}", transactionRequestDto.getTransactionId());
					return new ResourceNotFoundException(Messages.NO_CUSTOMERS_FOUND);
				});

		CustomerRewardEntity customerRewardEntity = customerRewardRepository.findById(id).orElseThrow(() -> {
			log.error("Customer not found with ID {}", id);
			return new ResourceNotFoundException(Messages.NO_CUSTOMERS_FOUND);
		});

		transactionEntity.setAmount(transactionRequestDto.getAmount());
		transactionEntity.setCustomerId(customerRewardEntity.getId());
		transactionEntity.setDate(transactionRequestDto.getDate());

		transactionRepository.save(transactionEntity);

		log.info("Transaction {} updated successfully", transactionRequestDto.getTransactionId());
	}

	@Override
	public void deleteTransaction(Long transactionId) {
		log.warn("Deleting transaction with ID: {}", transactionId);

		Optional<TransactionEntity> existingTransaction = transactionRepository.findById(transactionId);

		existingTransaction.ifPresentOrElse(tx -> {
			customerRewardRepository.deleteById(tx.getId());
			log.info("Transaction {} deleted successfully", transactionId);
		}, () -> {
			log.warn("No transaction found with ID {}", transactionId);
		});
	}

	@Override
	public List<TransactionEntity> getAllTransactions() {
		log.info("Fetching all transactions");

		List<TransactionEntity> transactionEntity = transactionRepository.findAll();

		log.info("Total transactions found: {}", transactionEntity.size());
		return transactionEntity;
	}
}

package com.rewards.service.impl;

import com.rewards.dto.TransactionRequestDto;
import com.rewards.entity.CustomerRewardEntity;
import com.rewards.entity.TransactionEntity;
import com.rewards.repository.CustomerRewardRepository;
import com.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private CustomerRewardRepository customerRewardRepository;

	@InjectMocks
	private TransactionImpl transactionService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testAddTransaction() {
		TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
		transactionRequestDto.setCustomerId(1L);
		transactionRequestDto.setAmount(100.0);
		transactionRequestDto.setDate(LocalDate.parse("2024-11-10"));

		CustomerRewardEntity existingCustomer = new CustomerRewardEntity();
		existingCustomer.setId(1L);
		existingCustomer.setName("John Doe");

		when(customerRewardRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));

		transactionService.addTransaction(transactionRequestDto);

		verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
	}

	@Test
	public void testGetAllTransactions() {
		TransactionEntity tx1 = new TransactionEntity();
		TransactionEntity tx2 = new TransactionEntity();
		when(transactionRepository.findAll()).thenReturn(Arrays.asList(tx1, tx2));

		List<TransactionEntity> result = transactionService.getAllTransactions();

		org.junit.jupiter.api.Assertions.assertNotNull(result);
		org.junit.jupiter.api.Assertions.assertEquals(2, result.size());
		verify(transactionRepository, times(1)).findAll();
	}

	@Test
	public void testUpdateTransaction() {
		Long transactionId = 101L;
		Long customerId = 1L;

		TransactionRequestDto dto = new TransactionRequestDto();
		dto.setTransactionId(transactionId);
		dto.setCustomerId(customerId);
		dto.setAmount(150.0);
		dto.setDate(LocalDate.parse("2024-11-12"));

		TransactionEntity existingTransaction = new TransactionEntity();
		existingTransaction.setId(transactionId);
		existingTransaction.setAmount(100.0);
		existingTransaction.setDate(LocalDate.parse("2024-11-10"));
		when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

		CustomerRewardEntity existingCustomer = new CustomerRewardEntity();
		existingCustomer.setId(customerId);
		existingCustomer.setName("John Doe");
		when(customerRewardRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

		transactionService.updateTransaction(customerId, dto);

		verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
	}

	@Test
	public void testUpdateTransactionNotFound() {
		Long transactionId = 999L;
		TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
		transactionRequestDto.setTransactionId(transactionId);
		transactionRequestDto.setCustomerId(1L);
		transactionRequestDto.setAmount(150.0);
		transactionRequestDto.setDate(LocalDate.parse("2024-11-12"));

		when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

		try {
			transactionService.updateTransaction(transactionId, transactionRequestDto);
		} catch (Exception e) {
			verify(transactionRepository, times(0)).save(any(TransactionEntity.class));
		}
	}

	@Test
	public void testDeleteTransaction() {
		Long transactionId = 1L;

		TransactionEntity existingTransaction = new TransactionEntity();
		existingTransaction.setId(transactionId);
		existingTransaction.setAmount(100.0);
		existingTransaction.setDate(LocalDate.parse("2024-11-10"));

		when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));

		transactionService.deleteTransaction(transactionId);
	}

	@Test
	public void testDeleteTransactionNotFound() {
		Long transactionId = 999L;

		doThrow(new RuntimeException("Transaction not found")).when(transactionRepository).deleteById(transactionId);

		try {
			transactionService.deleteTransaction(transactionId);
		} catch (Exception e) {
			verify(transactionRepository, times(1)).deleteById(transactionId);
		}
	}
	  

	    @Test
	    public void testGetAllTransactionsEmpty() {
	        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

	        List<TransactionEntity> result = transactionService.getAllTransactions();

	        org.junit.jupiter.api.Assertions.assertNotNull(result);
	        org.junit.jupiter.api.Assertions.assertTrue(result.isEmpty());
	        verify(transactionRepository, times(1)).findAll();
	    }
}

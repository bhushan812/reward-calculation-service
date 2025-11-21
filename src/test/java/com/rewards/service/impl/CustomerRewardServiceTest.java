package com.rewards.service.impl;

import com.rewards.constants.Messages;
import com.rewards.dto.CustomerRewardRequestDto;
import com.rewards.dto.CustomerRewardResponseDto;
import com.rewards.entity.CustomerRewardEntity;
import com.rewards.entity.TransactionEntity;
import com.rewards.exception.ResourceNotFoundException;
import com.rewards.repository.CustomerRewardRepository;
import com.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerRewardServiceTest {

	@Mock
	private CustomerRewardRepository customerRewardRepository;

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private CustomerRewardImpl customerRewardService;

	private CustomerRewardEntity customer;
	private List<TransactionEntity> transactions;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		customer = new CustomerRewardEntity();
		customer.setId(1L);
		customer.setName("John Doe");

		TransactionEntity tx1 = new TransactionEntity();
		tx1.setId(1L);
		tx1.setAmount(120.0);
		tx1.setDate(LocalDate.now());

		transactions = Arrays.asList(tx1);
	}

	@Test
	public void testGetRewardsByCustomer() {
		when(customerRewardRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
		when(transactionRepository.findByCustomerId(1L)).thenReturn(transactions);

		CustomerRewardResponseDto response = customerRewardService.getRewardsByCustomer(1L);

		assertNotNull(response);
		assertEquals(90, response.getTotalRewards());
	}

	@Test
	public void testAddCustomer() {
		CustomerRewardRequestDto dto = new CustomerRewardRequestDto();
		dto.setName("Jane Doe");

		customerRewardService.addCustomer(dto);

		verify(customerRewardRepository, times(1)).save(any(CustomerRewardEntity.class));
	}

	@Test
	public void testGetRewardsByCustomerNotFound() {
		when(customerRewardRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> customerRewardService.getRewardsByCustomer(1L));

		assertEquals("Customer not found or no rewards available.", exception.getMessage());
	}

	@Test
	public void testGetRewardsByCustomerNoTransactions() {
		when(customerRewardRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
		when(transactionRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

		CustomerRewardResponseDto response = customerRewardService.getRewardsByCustomer(1L);

		assertNotNull(response);
		assertEquals(0, response.getTotalRewards());
	}

	@Test
	public void testGetRewardsByCustomerInvalidTransactionData() {
		when(customerRewardRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));

		TransactionEntity invalidTransaction1 = new TransactionEntity();
		invalidTransaction1.setCustomerId(1L);
		invalidTransaction1.setAmount(-100.0);
		invalidTransaction1.setDate(LocalDate.parse("2025-11-01"));

		TransactionEntity validTransaction = new TransactionEntity();
		validTransaction.setCustomerId(1L);
		validTransaction.setAmount(200.0);
		validTransaction.setDate(LocalDate.parse("2025-11-02"));

		when(transactionRepository.findByCustomerId(1L))
				.thenReturn(Arrays.asList(invalidTransaction1, validTransaction));

		CustomerRewardResponseDto response = customerRewardService.getRewardsByCustomer(1L);

		assertNotNull(response);
		assertEquals(250, response.getTotalRewards());
	}

	@Test
	public void testDeleteCustomer_Success() {

		when(customerRewardRepository.deleteByIdReturnCount(1L)).thenReturn(1);

		when(transactionRepository.deleteByCustomerId(1L)).thenReturn(5);

		customerRewardService.deleteCustomer(1L);

		verify(transactionRepository, times(1)).deleteByCustomerId(1L);

		verify(customerRewardRepository, times(1)).deleteByIdReturnCount(1L);
	}

	@Test
	public void testAddCustomerSaveThrowsException() {
		CustomerRewardRequestDto dto = new CustomerRewardRequestDto();
		dto.setName("Jane Doe");

		when(customerRewardRepository.save(any(CustomerRewardEntity.class)))
				.thenThrow(new org.springframework.dao.DataIntegrityViolationException("duplicate"));

		assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
			customerRewardService.addCustomer(dto);
		});

		verify(customerRewardRepository, times(1)).save(any(CustomerRewardEntity.class));
	}

	@Test
	public void testDeleteCustomerTransactionDeleteFails() {
		when(customerRewardRepository.findById(1L)).thenReturn(Optional.of(customer));

		doThrow(new RuntimeException(Messages.TRANSACTION_NOT_FOUND)).when(transactionRepository)
				.deleteByCustomerId(1L);

		assertThrows(RuntimeException.class, () -> {
			customerRewardService.deleteCustomer(1L);
		});

		verify(transactionRepository, atLeastOnce()).deleteByCustomerId(1L);

		verify(customerRewardRepository, never()).deleteById(anyLong());
	}

}

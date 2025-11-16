package com.rewards.service.impl;

import com.rewards.dto.CustomerRewardRequestDto;
import com.rewards.dto.CustomerRewardResponseDto;
import com.rewards.entity.CustomerRewardEntity;
import com.rewards.entity.TransactionEntity;
import com.rewards.repository.CustomerRewardRepository;
import com.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
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
		tx1.setDate(java.time.LocalDate.now());

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
	public void testGetRewardsByCustomer_NotFound() {
		when(customerRewardRepository.findById(1L)).thenReturn(java.util.Optional.empty());

		CustomerRewardResponseDto response = customerRewardService.getRewardsByCustomer(1L);

		assertNull(response);
	}

	@Test
	public void testGetRewardsByCustomer_NoTransactions() {
		when(customerRewardRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
		when(transactionRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

		CustomerRewardResponseDto response = customerRewardService.getRewardsByCustomer(1L);

		assertNotNull(response);
		assertEquals(0, response.getTotalRewards());
	}

	@Test
	public void testGetRewardsByCustomer_InvalidTransactionData() {
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
	public void testDeleteCustomer() {
		when(customerRewardRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));
		when(transactionRepository.findByCustomerId(1L)).thenReturn(transactions);

		customerRewardService.deleteCustomer(1L);

		verify(customerRewardRepository, times(1)).deleteById(1L);
		verify(transactionRepository, times(1)).deleteById(anyLong());
	}
}

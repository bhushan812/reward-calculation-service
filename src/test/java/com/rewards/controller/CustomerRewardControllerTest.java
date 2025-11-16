package com.rewards.controller;

import com.rewards.constants.ApiUrls;
import com.rewards.constants.Messages;
import com.rewards.dto.CustomerRewardRequestDto;
import com.rewards.dto.CustomerRewardResponseDto;
import com.rewards.dto.TransactionResponseDto;
import com.rewards.service.CustomerRewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerRewardController.class)
public class CustomerRewardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerRewardService customerRewardService;

	@InjectMocks
	private CustomerRewardController customerRewardController;

	private CustomerRewardRequestDto customerRewardRequestDto;

	@BeforeEach
	public void setup() {
		customerRewardRequestDto = new CustomerRewardRequestDto();
		customerRewardRequestDto.setName("Bhushan Patel");
	}

	@Test
	public void testAddCustomer() throws Exception {
		doNothing().when(customerRewardService).addCustomer(any(CustomerRewardRequestDto.class));

		mockMvc.perform(MockMvcRequestBuilders.post(ApiUrls.BASE + ApiUrls.CUSTOMERS)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content("{ \"name\": \"Bhushan Patel\" }"))
				.andExpect(status().isCreated()).andExpect(content().string(Messages.CUSTOMER_ADD_SUCCESS));
	}

	@Test
	public void testGetCustomerRewards() throws Exception {

		CustomerRewardRequestDto dto = new CustomerRewardRequestDto();
		dto.setName("Ravi Kumar");

		CustomerRewardResponseDto customerRewardResponseDto = new CustomerRewardResponseDto();
		customerRewardResponseDto.setCustomerId(1L);
		customerRewardResponseDto.setCustomerName("Ravi Kumar");

		TransactionResponseDto transaction1 = new TransactionResponseDto();
		transaction1.setTransactionId(101L);
		transaction1.setAmount(120.00);
		transaction1.setTransactionDate(LocalDate.parse("2024-10-15"));

		TransactionResponseDto transaction2 = new TransactionResponseDto();
		transaction2.setTransactionId(102L);
		transaction2.setAmount(75.00);
		transaction2.setTransactionDate(LocalDate.parse("2024-11-03"));

		List<TransactionResponseDto> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);

		Map<String, Integer> monthly = new HashMap<>();
		monthly.put("OCTOBER", 120);
		monthly.put("NOVEMBER", 75);

		customerRewardResponseDto.setMonthlyRewards(monthly);
		customerRewardResponseDto.setTotalRewards(195);
		customerRewardResponseDto.setTransactions(transactions);

		when(customerRewardService.getRewardsByCustomer(1L)).thenReturn(customerRewardResponseDto);

		mockMvc.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 1L)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.customerId").value(1)).andExpect(jsonPath("$.customerName").value("Ravi Kumar"))
				.andExpect(jsonPath("$.monthlyRewards.OCTOBER").value(120))
				.andExpect(jsonPath("$.monthlyRewards.NOVEMBER").value(75))
				.andExpect(jsonPath("$.totalRewards").value(195))
				.andExpect(jsonPath("$.transactions[0].amount").value(120.00))
				.andExpect(jsonPath("$.transactions[1].amount").value(75.00));
	}

	@Test
	public void testGetCustomerRewards_CustomerNotFound() throws Exception {

		when(customerRewardService.getRewardsByCustomer(999L)).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 999L))
				.andExpect(status().isNotFound())
				.andExpect(content().string(Messages.CUSTOMER_NOT_FOUND_OR_NO_REWARDS));
	}

	@Test
	public void testGetCustomerRewards_NoTransactions() throws Exception {

		CustomerRewardRequestDto dto = new CustomerRewardRequestDto();
		dto.setName("Priya Sharma");

		CustomerRewardResponseDto customerRewardResponseDto = new CustomerRewardResponseDto();
		customerRewardResponseDto.setCustomerId(1L);
		customerRewardResponseDto.setCustomerName("Priya Sharma");

		List<TransactionResponseDto> transactions = new ArrayList<>();
		Map<String, Integer> monthly = new HashMap<>();

		customerRewardResponseDto.setMonthlyRewards(monthly);
		customerRewardResponseDto.setTotalRewards(0);
		customerRewardResponseDto.setTransactions(transactions);

		when(customerRewardService.getRewardsByCustomer(1L)).thenReturn(customerRewardResponseDto);

		mockMvc.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 1L)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.customerId").value(1))
				.andExpect(jsonPath("$.customerName").value("Priya Sharma"))
				.andExpect(jsonPath("$.totalRewards").value(0)).andExpect(jsonPath("$.transactions").isEmpty());
	}

	@Test
	public void testGetCustomerRewards_InvalidTransactionAmount() throws Exception {

		CustomerRewardRequestDto dto = new CustomerRewardRequestDto();
		dto.setName("Suresh Yadav");

		CustomerRewardResponseDto customerRewardResponseDto = new CustomerRewardResponseDto();
		customerRewardResponseDto.setCustomerId(1L);
		customerRewardResponseDto.setCustomerName("Suresh Yadav");

		TransactionResponseDto transaction1 = new TransactionResponseDto();
		transaction1.setTransactionId(101L);
		transaction1.setAmount(-120.00);
		transaction1.setTransactionDate(LocalDate.parse("2024-10-15"));

		List<TransactionResponseDto> transactions = new ArrayList<>();
		transactions.add(transaction1);

		Map<String, Integer> monthly = new HashMap<>();
		monthly.put("OCTOBER", 0);

		customerRewardResponseDto.setMonthlyRewards(monthly);
		customerRewardResponseDto.setTotalRewards(0);
		customerRewardResponseDto.setTransactions(transactions);

		when(customerRewardService.getRewardsByCustomer(1L)).thenReturn(customerRewardResponseDto);

		mockMvc.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 1L)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(jsonPath("$.customerId").value(1))
				.andExpect(jsonPath("$.customerName").value("Suresh Yadav"))
				.andExpect(jsonPath("$.totalRewards").value(0))
				.andExpect(jsonPath("$.transactions[0].amount").value(-120.00));
	}

	@Test
	public void testUpdateCustomer() throws Exception {
		doNothing().when(customerRewardService).updateCustomer(eq(1L), any(CustomerRewardRequestDto.class));

		mockMvc.perform(MockMvcRequestBuilders.put(ApiUrls.BASE + ApiUrls.CUSTOMERS + "/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content("{ \"name\": \"Bhushan Patel Updated\" }"))
				.andExpect(status().isOk()).andExpect(content().string(Messages.CUSTOMER_UPDATE_SUCCESS));
	}

	@Test
	public void testDeleteCustomer() throws Exception {
	    doNothing().when(customerRewardService).deleteCustomer(1L);

	    mockMvc.perform(MockMvcRequestBuilders.delete(ApiUrls.BASE + ApiUrls.CUSTOMERS + "/1"))
	            .andExpect(status().isOk()) 
	            .andExpect(content().string(Messages.CUSTOMER_DELETE_SUCCESS)); 
	}

}

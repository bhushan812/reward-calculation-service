package com.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rewards.constants.ApiUrls;
import com.rewards.constants.Messages;
import com.rewards.dto.TransactionRequestDto;
import com.rewards.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	@Mock
	private TransactionService transactionService;

	@InjectMocks
	private TransactionController transactionController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Test
	public void testAddTransaction() throws Exception {
		TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
		transactionRequestDto.setCustomerId(1L);
		transactionRequestDto.setAmount(100.0);
		transactionRequestDto.setDate(LocalDate.parse("2024-11-10"));

		mockMvc.perform(post(ApiUrls.TRANSACTION_BASE + ApiUrls.ADD_TRANSACTION).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequestDto))).andExpect(status().isCreated())
				.andExpect(content().string(Messages.TRANSACTION_ADD_SUCCESS));

		verify(transactionService, times(1)).addTransaction(transactionRequestDto);
	}

	@Test
	public void testUpdateTransaction() throws Exception {
		TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
		transactionRequestDto.setTransactionId(1L);
		transactionRequestDto.setCustomerId(1L);
		transactionRequestDto.setAmount(150.0);
		transactionRequestDto.setDate(LocalDate.parse("2024-11-12"));

		mockMvc.perform(put(ApiUrls.TRANSACTION_BASE + ApiUrls.UPDATE_TRANSACTION.replace("{id}", "1"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequestDto))).andExpect(status().isOk())
				.andExpect(content().string(Messages.TRANSACTION_UPDATE_SUCCESS));

		verify(transactionService, times(1)).updateTransaction(1L, transactionRequestDto);
	}

	@Test
	public void testUpdateTransaction_NotFound() throws Exception {
		TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
		transactionRequestDto.setTransactionId(999L);
		transactionRequestDto.setCustomerId(1L);
		transactionRequestDto.setAmount(150.0);
		transactionRequestDto.setDate(LocalDate.parse("2024-11-12"));

		mockMvc.perform(put(ApiUrls.TRANSACTION_BASE + ApiUrls.UPDATE_TRANSACTION.replace("{id}", "999"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(transactionRequestDto))).andExpect(status().isOk());
	}

	@Test
	public void testDeleteTransaction() throws Exception {
	    Long transactionId = 1L;

	    doNothing().when(transactionService).deleteTransaction(transactionId);

	    mockMvc.perform(delete(ApiUrls.TRANSACTION_BASE
	            + ApiUrls.DELETE_TRANSACTION.replace("{transactionId}", transactionId.toString())))
	            .andExpect(status().isOk())
	            .andExpect(content().string(Messages.TRANSACTION_DELETE_SUCCESS)); 

	    verify(transactionService, times(1)).deleteTransaction(transactionId);
	}

	@Test
	public void testGetAllTransactions() throws Exception {
		mockMvc.perform(get(ApiUrls.TRANSACTION_BASE + ApiUrls.LIST_TRANSACTIONS)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		verify(transactionService, times(1)).getAllTransactions();
	}
}

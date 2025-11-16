package com.rewards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rewards.constants.ApiUrls;
import com.rewards.constants.Messages;
import com.rewards.dto.TransactionRequestDto;
import com.rewards.entity.TransactionEntity;
import com.rewards.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
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

        mockMvc.perform(post(ApiUrls.TRANSACTION_BASE + ApiUrls.ADD_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionRequestDto)))
                .andExpect(status().isCreated())
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
                        .content(objectMapper.writeValueAsString(transactionRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(Messages.TRANSACTION_UPDATE_SUCCESS));

        verify(transactionService, times(1)).updateTransaction(1L, transactionRequestDto);
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
        mockMvc.perform(get(ApiUrls.TRANSACTION_BASE + ApiUrls.LIST_TRANSACTIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    public void testAddTransactionNative() {
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setCustomerId(1L);
        dto.setAmount(100.0);
        dto.setDate(LocalDate.parse("2024-11-10"));

        doNothing().when(transactionService).addTransaction(dto);

        ResponseEntity<?> response = transactionController.addTransaction(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Messages.TRANSACTION_ADD_SUCCESS, response.getBody());
        verify(transactionService, times(1)).addTransaction(dto);
    }

    @Test
    public void testUpdateTransactionNative() {
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setTransactionId(1L);
        dto.setCustomerId(1L);
        dto.setAmount(150.0);
        dto.setDate(LocalDate.parse("2024-11-12"));

        doNothing().when(transactionService).updateTransaction(1L, dto);

        ResponseEntity<?> response = transactionController.updateTransaction(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Messages.TRANSACTION_UPDATE_SUCCESS, response.getBody());
        verify(transactionService, times(1)).updateTransaction(1L, dto);
    }

    @Test
    public void testDeleteTransactionNative() {
        Long transactionId = 1L;
        doNothing().when(transactionService).deleteTransaction(transactionId);

        ResponseEntity<?> response = transactionController.deleteTransaction(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Messages.TRANSACTION_DELETE_SUCCESS, response.getBody());
        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }

    @Test
    public void testGetAllTransactionsNative() {
        TransactionEntity tx1 = new TransactionEntity();
        tx1.setId(1L);
        tx1.setCustomerId(1L);
        tx1.setAmount(100.0);
        tx1.setDate(LocalDate.parse("2024-11-10"));

        TransactionEntity tx2 = new TransactionEntity();
        tx2.setId(2L);
        tx2.setCustomerId(2L);
        tx2.setAmount(150.0);
        tx2.setDate(LocalDate.parse("2024-11-12"));

        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(tx1, tx2));

        ResponseEntity<?> response = transactionController.getAllTransactions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);

        List<?> responseList = (List<?>) response.getBody();
        assertEquals(2, responseList.size());

        verify(transactionService, times(1)).getAllTransactions();
    }
}

package com.rewards.controller;

import com.rewards.constants.ApiUrls;
import com.rewards.constants.Messages;
import com.rewards.dto.CustomerRewardRequestDto;
import com.rewards.dto.CustomerRewardResponseDto;
import com.rewards.dto.TransactionResponseDto;
import com.rewards.exception.ResourceNotFoundException;
import com.rewards.service.CustomerRewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
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
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"name\": \"Bhushan Patel\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().string(Messages.CUSTOMER_ADD_SUCCESS));
    }

    @Test
    public void testAddCustomerBadRequest() throws Exception {
        doThrow(new RuntimeException("Invalid data")).when(customerRewardService)
                .addCustomer(any(CustomerRewardRequestDto.class));

        mockMvc.perform(MockMvcRequestBuilders.post(ApiUrls.BASE + ApiUrls.CUSTOMERS)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"name\": \"\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(Messages.ERROR_ADD + "Invalid data"));
    }

    @Test
    public void testGetCustomerRewards() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Ravi Kumar"))
                .andExpect(jsonPath("$.monthlyRewards.OCTOBER").value(120))
                .andExpect(jsonPath("$.monthlyRewards.NOVEMBER").value(75))
                .andExpect(jsonPath("$.totalRewards").value(195))
                .andExpect(jsonPath("$.transactions[0].amount").value(120.00))
                .andExpect(jsonPath("$.transactions[1].amount").value(75.00));
    }

    @Test
    public void testGetCustomerRewardsCustomerNotFound() throws Exception {
        when(customerRewardService.getRewardsByCustomer(999L))
                .thenThrow(new ResourceNotFoundException(Messages.CUSTOMER_NOT_FOUND_OR_NO_REWARDS));

        MockMvc mockMvcLocal = MockMvcBuilders.standaloneSetup(customerRewardController)
                .setControllerAdvice(new Object() {
                    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
                    public ResponseEntity<String> handleNotFound(ResourceNotFoundException e) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
                    }
                })
                .build();

        mockMvcLocal.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 999L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetCustomerRewardsNoTransactions() throws Exception {
        CustomerRewardResponseDto customerRewardResponseDto = new CustomerRewardResponseDto();
        customerRewardResponseDto.setCustomerId(1L);
        customerRewardResponseDto.setCustomerName("Priya Sharma");

        List<TransactionResponseDto> transactions = new ArrayList<>();
        Map<String, Integer> monthly = new HashMap<>();

        customerRewardResponseDto.setMonthlyRewards(monthly);
        customerRewardResponseDto.setTotalRewards(0);
        customerRewardResponseDto.setTransactions(transactions);

        when(customerRewardService.getRewardsByCustomer(1L)).thenReturn(customerRewardResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Priya Sharma"))
                .andExpect(jsonPath("$.totalRewards").value(0))
                .andExpect(jsonPath("$.transactions").isEmpty());
    }

    @Test
    public void testGetCustomerRewardsInvalidTransactionAmount() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.get(ApiUrls.BASE + "/customers/{id}", 1L))
                .andExpect(status().isOk())
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
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"name\": \"Bhushan Patel Updated\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string(Messages.CUSTOMER_UPDATE_SUCCESS));
    }

    @Test
    public void testUpdateCustomerNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Customer not found")).when(customerRewardService)
                .updateCustomer(eq(999L), any(CustomerRewardRequestDto.class));

        mockMvc.perform(MockMvcRequestBuilders.put(ApiUrls.BASE + ApiUrls.CUSTOMERS + "/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{ \"name\": \"Unknown User\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        doNothing().when(customerRewardService).deleteCustomer(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete(ApiUrls.BASE + ApiUrls.CUSTOMERS + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(Messages.CUSTOMER_DELETE_SUCCESS));
    }

    @Test
    public void testDeleteCustomer_NotFound() throws Exception {
        doThrow(new RuntimeException("Customer not found")).when(customerRewardService).deleteCustomer(999L);

        mockMvc.perform(MockMvcRequestBuilders.delete(ApiUrls.BASE + ApiUrls.CUSTOMERS + "/999"))
                .andExpect(status().isBadRequest());
    }

}

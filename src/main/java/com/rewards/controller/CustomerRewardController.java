package com.rewards.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rewards.constants.ApiUrls;
import com.rewards.constants.Messages;
import com.rewards.dto.CustomerRewardRequestDto;
import com.rewards.dto.CustomerRewardResponseDto;
import com.rewards.service.CustomerRewardService;

@RestController
@RequestMapping(ApiUrls.BASE)
public class CustomerRewardController {

	@Autowired
	private CustomerRewardService customerService;

	@PostMapping(ApiUrls.CUSTOMERS)
	public ResponseEntity<?> addCustomer(@RequestBody CustomerRewardRequestDto customer) {
		try {
			customerService.addCustomer(customer);
			return ResponseEntity.status(HttpStatus.CREATED).body(Messages.CUSTOMER_ADD_SUCCESS);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.ERROR_ADD + e.getMessage());
		}
	}

	@PutMapping(ApiUrls.CUSTOMER_BY_ID)
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerRewardRequestDto dto) {
		try {
			customerService.updateCustomer(id, dto);
			return ResponseEntity.ok(Messages.CUSTOMER_UPDATE_SUCCESS);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.ERROR_UPDATE + e.getMessage());
		}
	}

	@DeleteMapping(ApiUrls.CUSTOMER_BY_CUSTOMERID)
	public ResponseEntity<?> deleteCustomer(@PathVariable Long customerId) {
		try {
			customerService.deleteCustomer(customerId);
			return ResponseEntity.ok(Messages.CUSTOMER_DELETE_SUCCESS);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.ERROR_DELETE + e.getMessage());
		}
	}

	// Fetch all customers along with their calculated reward points.
	@GetMapping(ApiUrls.CUSTOMERS)
	public ResponseEntity<?> getAllCustomers() {
		try {
			List<CustomerRewardResponseDto> customers = customerService.getAllCustomersRewards();
			if (customers.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Messages.NO_CUSTOMERS_FOUND);
			}
			return ResponseEntity.ok(customers);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Messages.ERROR_FETCH + e.getMessage());
		}
	}

	// Fetch reward calculation details for a specific customer.
	// This provides total reward points, monthly breakdown, and transactions.
	@GetMapping(ApiUrls.CUSTOMER_BY_ID)
	public ResponseEntity<?> getCustomerRewards(@PathVariable Long id) {
		try {
			CustomerRewardResponseDto response = customerService.getRewardsByCustomer(id);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.ERROR_FETCH_REWARDS + e.getMessage());
		}
	}
}

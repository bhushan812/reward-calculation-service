
package com.rewards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rewards.constants.ApiUrls;
import com.rewards.constants.Messages;
import com.rewards.dto.TransactionRequestDto;
import com.rewards.service.TransactionService;

@RestController
@RequestMapping(ApiUrls.TRANSACTION_BASE)
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping(ApiUrls.ADD_TRANSACTION)
	public ResponseEntity<?> addTransaction(@RequestBody TransactionRequestDto transactionRequestDto) {
		try {
			transactionService.addTransaction(transactionRequestDto);
			return ResponseEntity.status(HttpStatus.CREATED).body(Messages.TRANSACTION_ADD_SUCCESS);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Messages.ERROR_ADD_TRANSACTION + e.getMessage());
		}
	}

	@PutMapping(ApiUrls.UPDATE_TRANSACTION)
	public ResponseEntity<?> updateTransaction(@PathVariable Long transactionId,
			@RequestBody TransactionRequestDto transactionRequestDto) {

		transactionService.updateTransaction(transactionId, transactionRequestDto);
		return ResponseEntity.ok(Messages.TRANSACTION_UPDATE_SUCCESS);
	}

	@DeleteMapping(ApiUrls.DELETE_TRANSACTION)
	public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) {

		transactionService.deleteTransaction(transactionId);
		return ResponseEntity.ok(Messages.TRANSACTION_DELETE_SUCCESS);
	}

	@GetMapping(ApiUrls.LIST_TRANSACTIONS)
	public ResponseEntity<?> getAllTransactions() {

		return ResponseEntity.ok(transactionService.getAllTransactions());
	}
}

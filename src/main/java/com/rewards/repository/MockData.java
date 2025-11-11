package com.rewards.repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.rewards.model.TransactionModel;

public class MockData {

	public static List<TransactionModel> getTransactions() {
		return Arrays.asList(new TransactionModel("C001", "Bhushan", 120, LocalDate.of(2025, 8, 10)),
				new TransactionModel("C001", "Bhushan", 75, LocalDate.of(2025, 9, 5)),
				new TransactionModel("C002", "Amit", 200, LocalDate.of(2025, 8, 15)),
				new TransactionModel("C002", "Amit", 50, LocalDate.of(2025, 9, 20)),
				new TransactionModel("C001", "Bhushan", 130, LocalDate.of(2025, 10, 1)));
	}

}
package com.rewards.serviceImpl;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rewards.DTO.RewardResponse;
import com.rewards.model.TransactionModel;
import com.rewards.repository.MockData;
import com.rewards.service.RewardService;
import com.rewards.utils.ErrorMessage;

@Service
public  class RewardServiceImpl implements RewardService {

	@Override
	public List<RewardResponse> calculateRewards(LocalDate startDate, LocalDate endDate) {
		List<TransactionModel> transactions = MockData.getTransactions();

		if (transactions == null) {
			throw new NullPointerException(ErrorMessage.NULL_TRANSACTION_LIST);
		}

		if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
			throw new IllegalArgumentException(ErrorMessage.INVALID_DATE_RANGE);
		}

		Period period = Period.between(startDate, endDate);
		if (period.getMonths() > 3 || (period.getYears() > 0)) {
			throw new IllegalArgumentException(ErrorMessage.INVALID_DATE_RANGE);
		}

		Map<String, List<TransactionModel>> grouped = transactions.stream()
				.filter(txn -> !txn.getDate().isBefore(startDate) && !txn.getDate().isAfter(endDate))
				.collect(Collectors.groupingBy(TransactionModel::getCustomerId));

		List<RewardResponse> responses = new ArrayList<>();

		for (Map.Entry<String, List<TransactionModel>> entry : grouped.entrySet()) {
			String customerId = entry.getKey();
			List<TransactionModel> customerTxns = entry.getValue();

			Map<String, Integer> monthlyPoints = new HashMap<>();
			int total = 0;
			String customerName = customerTxns.get(0).getCustomerName();

			for (TransactionModel txn : customerTxns) {
				int points = calculatePoints(txn.getAmount());
				String month = txn.getDate().getMonth().toString();

				monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
				total += points;
			}

			responses.add(new RewardResponse(customerId, customerName, monthlyPoints, total));
		}

		return responses;
	}



private int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int)((amount - 100) * 2);
            points += 50;
        } else if (amount > 50) {
            points += (int)(amount - 50);
        }
        return points;
    }

}
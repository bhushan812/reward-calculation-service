package com.rewards.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rewards.constants.Messages;
import com.rewards.dto.CustomerRewardRequestDto;
import com.rewards.dto.CustomerRewardResponseDto;
import com.rewards.dto.TransactionResponseDto;
import com.rewards.entity.CustomerRewardEntity;
import com.rewards.entity.TransactionEntity;
import com.rewards.exception.ResourceNotFoundException;
import com.rewards.repository.CustomerRewardRepository;
import com.rewards.repository.TransactionRepository;
import com.rewards.service.CustomerRewardService;
import com.rewards.utils.CalculateRewardPointsUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class CustomerRewardImpl implements CustomerRewardService {

	@Autowired
	private CustomerRewardRepository customerRewardRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public CustomerRewardResponseDto getRewardsByCustomer(Long customerId) {
		log.info("Fetching rewards for customerId: {}", customerId);

		CustomerRewardEntity customerRewardEntity = customerRewardRepository.findById(customerId).orElse(null);
		log.debug("Customer entity fetched: {}", customerRewardEntity);

		if (null == customerRewardEntity) {
			log.warn("Customer not found for ID: {}", customerId);
			return null;
		}

		List<TransactionEntity> tx = transactionRepository.findByCustomerId(customerId);

		log.info("Total transactions found: {}", tx.size());

		return buildResponse(customerRewardEntity, tx);
	}

	@Override
	public List<CustomerRewardResponseDto> getAllCustomersRewards() {
		log.info("Fetching reward responses for all customers");

		List<CustomerRewardEntity> customerRewardEntities = customerRewardRepository.findAll();
		log.info("Total customers found: {}", customerRewardEntities.size());

		List<CustomerRewardResponseDto> list = new ArrayList<>();

		for (CustomerRewardEntity c : customerRewardEntities) {
			List<TransactionEntity> tx = transactionRepository.findByCustomerId(c.getId());
			log.debug("Processing customer {} with {} transactions", c.getId(), tx.size());
			list.add(buildResponse(c, tx));
		}

		log.info("Completed generating rewards for all customers");
		return list;
	}

	private CustomerRewardResponseDto buildResponse(CustomerRewardEntity c, List<TransactionEntity> txList) {
		log.debug("Building response for customer: {}", c.getId());

		Map<String, Integer> monthly = new HashMap<>();
		List<TransactionResponseDto> dtoList = new ArrayList<>();
		int total = 0;

		for (TransactionEntity t : txList) {
			int points = CalculateRewardPointsUtil.calculatePoints(t.getAmount());
			total += points;

			String month = t.getDate().getMonth().name();
			monthly.put(month, monthly.getOrDefault(month, 0) + points);
			TransactionResponseDto dto = new TransactionResponseDto();
			dto.setAmount(t.getAmount());
			dto.setTransactionDate(t.getDate());
			dto.setTransactionId(t.getId());
			dto.setPointsEarned(points);

			dtoList.add(dto);

			log.debug("Transaction {} processed with points {}", t.getId(), points);
		}

		log.info("Total points for customer {} = {}", c.getId(), total);

		return new CustomerRewardResponseDto(c.getId(), c.getName(), monthly, total, dtoList);
	}

	@Override
	public void addCustomer(CustomerRewardRequestDto customerRewardDto) {

		log.info(Messages.CUSTOMER_ADD_SUCCESS + " - {}", customerRewardDto.getName());

		CustomerRewardEntity customerRewardEntity = new CustomerRewardEntity();
		customerRewardEntity.setName(customerRewardDto.getName());
		customerRewardRepository.save(customerRewardEntity);

		log.info(Messages.CUSTOMER_ADD_SUCCESS + " - {}", customerRewardDto.getName());
	}

	@Override
	public void updateCustomer(Long id, CustomerRewardRequestDto customerRewardDto) {
		log.info("Updating customer with ID: {}", id);

		CustomerRewardEntity customerRewardEntity = customerRewardRepository.findById(id).orElseThrow(() -> {
			log.error("Customer with ID {} not found for update", id);

			return new ResourceNotFoundException(Messages.CUSTOMER_NOT_FOUND_OR_NO_REWARDS);
		});

		customerRewardEntity.setName(customerRewardEntity.getName());

		customerRewardRepository.save(customerRewardEntity);

		log.info(Messages.CUSTOMER_UPDATE_SUCCESS + " - {}", id);
	}

	@Override
	public void deleteCustomer(Long customerId) {
	    log.warn("Deleting customer with ID: {}", customerId);

	    List<TransactionEntity> transactions = transactionRepository.findByCustomerId(customerId);
	    log.info("Total transactions to delete for customer {}: {}", customerId, transactions.size());

	    transactions.forEach(tx -> {
	        transactionRepository.deleteById(tx.getId());
	        log.debug("Deleted transaction {}", tx.getId());
	    });

	    Optional<CustomerRewardEntity> existingCustomer = customerRewardRepository.findById(customerId);
	    existingCustomer.ifPresent(customer -> {
	        customerRewardRepository.deleteById(customer.getId());
	        log.info(Messages.CUSTOMER_DELETE_SUCCESS + " - {}", customerId);
	    });

	    log.info("All related transactions and customer deleted for ID {}", customerId);
	}


}

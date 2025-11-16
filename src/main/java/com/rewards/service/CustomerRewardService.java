package com.rewards.service;

import java.util.List;

import com.rewards.dto.CustomerRewardRequestDto;
import com.rewards.dto.CustomerRewardResponseDto;

public interface CustomerRewardService {
	
	CustomerRewardResponseDto getRewardsByCustomer(Long customerId);
	List<CustomerRewardResponseDto> getAllCustomersRewards();
	void addCustomer(CustomerRewardRequestDto customerRewardRequestDto);
	void updateCustomer(Long id, CustomerRewardRequestDto customerRewardRequestDto);
	void deleteCustomer(Long customerId);

}
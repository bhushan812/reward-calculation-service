package com.rewards.service;

import java.time.LocalDate;
import java.util.List;

import com.rewards.DTO.RewardResponse;

public interface RewardService {

	List<RewardResponse> calculateRewards(LocalDate startDate, LocalDate endDate);

}
package com.rewards.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.ErrorManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties.Transaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.DTO.ErrorResponseDto;
import com.rewards.DTO.RewardResponse;
import com.rewards.DTO.SuccessResponseDto;
import com.rewards.model.TransactionModel;
import com.rewards.repository.MockData;
import com.rewards.service.RewardService;
import com.rewards.utils.ApiUrls;
import com.rewards.utils.ErrorMessage;
import com.rewards.utils.SuccessMessage;
import com.rewards.utils.SuccessMessageCode;

@RestController
@RequestMapping(ApiUrls.REWARDS)
public class RewardController {

	@Autowired
	private RewardService rewardService;

	@GetMapping
	public ResponseEntity<?> getRewards(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		try {

			List<RewardResponse> rewards = rewardService.calculateRewards(startDate, endDate);

			return new ResponseEntity<>(
					new SuccessResponseDto(SuccessMessage.SUCCESS, SuccessMessageCode.SUCCESSKEY, rewards),
					HttpStatus.OK);
		} catch (Exception e) {

			return new ResponseEntity<>(new ErrorResponseDto(ErrorMessage.ERROR_MESSAGE, e.getMessage()),
					HttpStatus.BAD_REQUEST);

		}
	}

}

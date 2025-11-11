package com.rewards.rewardcontrollertest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.rewards.DTO.RewardResponse;
import com.rewards.controller.RewardController;
import com.rewards.serviceImpl.RewardServiceImpl;
import com.rewards.utils.ApiUrls;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)

public class RewardControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RewardServiceImpl rewardService;

	@Test
	public void testGetRewards() throws Exception {

		Map<String, Integer> monthlyPoints = new HashMap<>();
		monthlyPoints.put("AUGUST", 90);
		monthlyPoints.put("SEPTEMBER", 25);

		RewardResponse mockResponse = new RewardResponse("C001", "Bhushan", monthlyPoints, 115);

		
		Mockito.when(rewardService.calculateRewards( Mockito.any(), Mockito.any()))
				.thenReturn(Collections.singletonList(mockResponse));

		
		mockMvc.perform(get(ApiUrls.REWARDS).param("startDate", "2025-08-01").param("endDate", "2025-09-30")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].customerId").value("C001"))
				.andExpect(jsonPath("$[0].customerName").value("Bhushan"))
				.andExpect(jsonPath("$[0].totalPoints").value(115))
				.andExpect(jsonPath("$[0].monthlyPoints.AUGUST").value(90))
				.andExpect(jsonPath("$[0].monthlyPoints.SEPTEMBER").value(25));
	}

}

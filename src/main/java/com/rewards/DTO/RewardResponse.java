package com.rewards.DTO;


import java.util.Map;

public class RewardResponse {

	private String customerId;
	private String customerName;
	private Map<String, Integer> monthlyPoints;
	private int totalPoints;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}

	public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public RewardResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RewardResponse(String customerId, String customerName, Map<String, Integer> monthlyPoints, int totalPoints) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

}
package com.rewards.model;


import java.time.LocalDate;

public class TransactionModel {
	private String customerId;
	private String customerName;
	private double amount;
	private LocalDate date;

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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public TransactionModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public TransactionModel(String customerId, String customerName, double amount, LocalDate date) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.amount = amount;
		this.date = date;
	}

}

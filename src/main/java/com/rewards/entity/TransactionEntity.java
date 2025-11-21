package com.rewards.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class TransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "date")
	private LocalDate date;

}

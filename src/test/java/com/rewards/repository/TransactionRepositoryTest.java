package com.rewards.repository;

import com.rewards.entity.TransactionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transactionRepository;

	private TransactionEntity transaction1;
	private TransactionEntity transaction2;

	@BeforeEach
	public void setUp() {
		transaction1 = new TransactionEntity();
		transaction1.setCustomerId(1L);
		transaction1.setAmount(100.0);
		transaction1.setDate(LocalDate.of(2024, 10, 15));
		transactionRepository.save(transaction1);

		transaction2 = new TransactionEntity();
		transaction2.setCustomerId(1L);
		transaction2.setAmount(150.0);
		transaction2.setDate(LocalDate.of(2024, 11, 10));
		transactionRepository.save(transaction2);
	}

	@Test
	public void testFindByCustomerId() {
		List<TransactionEntity> transactions = transactionRepository.findByCustomerId(1L);
		assertThat(transactions).contains(transaction1, transaction2);
	}

	@Test
	public void testFindByCustomerId_NotFound() {
		List<TransactionEntity> transactions = transactionRepository.findByCustomerId(999L);
		assertThat(transactions).isEmpty();
	}

	@Test
	public void testSaveAndFindById() {
		TransactionEntity transaction = new TransactionEntity();
		transaction.setCustomerId(2L);
		transaction.setAmount(200.0);
		transaction.setDate(LocalDate.of(2024, 12, 5));

		transactionRepository.save(transaction);

		TransactionEntity foundTransaction = transactionRepository.findById(transaction.getId()).orElse(null);
		assertThat(foundTransaction).isNotNull();
		assertThat(foundTransaction.getCustomerId()).isEqualTo(2L);
		assertThat(foundTransaction.getAmount()).isEqualTo(200.0);
	}

	@Test
	public void testDeleteTransaction() {
		transactionRepository.delete(transaction1);
		List<TransactionEntity> transactions = transactionRepository.findByCustomerId(1L);
		assertThat(transactions).doesNotContain(transaction1);
	}
}

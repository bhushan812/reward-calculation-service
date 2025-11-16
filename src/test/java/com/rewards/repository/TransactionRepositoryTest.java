package com.rewards.repository;

import com.rewards.entity.TransactionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        Optional<TransactionEntity> foundTransactionOpt = transactionRepository.findById(savedTransaction.getId());

        assertTrue(foundTransactionOpt.isPresent());
        TransactionEntity foundTransaction = foundTransactionOpt.get();
        assertEquals(2L, foundTransaction.getCustomerId());
        assertEquals(200.0, foundTransaction.getAmount());
        assertEquals(LocalDate.of(2024, 12, 5), foundTransaction.getDate());
    }

    @Test
    public void testDeleteTransaction() {
        transactionRepository.delete(transaction1);
        List<TransactionEntity> transactions = transactionRepository.findByCustomerId(1L);
        assertThat(transactions).doesNotContain(transaction1);
    }

	@Test
	public void testFindByCustomerIdNative() {
		List<TransactionEntity> result = transactionRepository.findByCustomerId(1L);
		assertNotNull(result);
		assertTrue(result.contains(transaction1));
		assertTrue(result.contains(transaction2));
	}

    @Test
    public void testFindByCustomerIdNotFoundNative() {
        List<TransactionEntity> result = transactionRepository.findByCustomerId(999L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSaveAndFindByIdNative() {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setCustomerId(3L);
        transaction.setAmount(250.0);
        transaction.setDate(LocalDate.of(2024, 12, 20));

        TransactionEntity saved = transactionRepository.save(transaction);
        Optional<TransactionEntity> found = transactionRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(3L, found.get().getCustomerId());
        assertEquals(250.0, found.get().getAmount());
        assertEquals(LocalDate.of(2024, 12, 20), found.get().getDate());
    }

    @Test
    public void testDeleteTransactionNative() {
        transactionRepository.delete(transaction2);
        List<TransactionEntity> result = transactionRepository.findByCustomerId(1L);
        assertFalse(result.contains(transaction2));
    }
}

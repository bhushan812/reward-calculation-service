package com.rewards.repository;

import com.rewards.entity.CustomerRewardEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql(statements = { "SET REFERENTIAL_INTEGRITY FALSE", "DELETE FROM CUSTOMERS",
		"ALTER TABLE CUSTOMERS ALTER COLUMN ID RESTART WITH 1",
		"SET REFERENTIAL_INTEGRITY TRUE" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class CustomerRewardRepositoryTest {

	@Autowired
	private CustomerRewardRepository customerRewardRepository;

	private CustomerRewardEntity customer;

	@BeforeEach
	public void setup() {
		customer = new CustomerRewardEntity();
		customer.setName("John Doe");
	}

	@Test
	public void testSaveCustomer() {
		CustomerRewardEntity savedCustomer = customerRewardRepository.save(customer);
		assertNotNull(savedCustomer.getId());
	}

	@Test
	public void testFindById() {
		CustomerRewardEntity savedCustomer = customerRewardRepository.save(customer);
		CustomerRewardEntity foundCustomer = customerRewardRepository.findById(savedCustomer.getId()).orElse(null);
		assertNotNull(foundCustomer);
		assertEquals(savedCustomer.getName(), foundCustomer.getName());
	}

	@Test
	public void testDeleteCustomer() {
		CustomerRewardEntity savedCustomer = customerRewardRepository.save(customer);
		customerRewardRepository.delete(savedCustomer);
		assertFalse(customerRewardRepository.findById(savedCustomer.getId()).isPresent());
	}
}


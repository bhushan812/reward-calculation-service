package com.rewards.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;

import com.rewards.entity.CustomerRewardEntity;

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

	@Test
	public void testFindByIdNotExistingShouldReturnEmpty() {
		assertTrue(customerRewardRepository.findById(999L).isEmpty());
	}

	@Test
	public void testDeleteCustomerNotExistingShouldBeNoOp() {
		long before = customerRewardRepository.count();

		CustomerRewardEntity dummy = new CustomerRewardEntity();
		dummy.setId(999L);

		customerRewardRepository.delete(dummy);

		long after = customerRewardRepository.count();
		assertEquals(before, after, "Deleting a non-existing (detached) entity should be a no-op");
	}

	@Test
	public void testSaveCustomerDuplicateIdShouldUpdateExisting() {
		CustomerRewardEntity c1 = new CustomerRewardEntity();
		c1.setName("User 1");
		customerRewardRepository.saveAndFlush(c1);

		CustomerRewardEntity c2 = new CustomerRewardEntity();
		c2.setId(c1.getId());
		c2.setName("User 2");

		long beforeCount = customerRewardRepository.count();
		CustomerRewardEntity result = customerRewardRepository.saveAndFlush(c2);

		assertEquals(c1.getId(), result.getId());
		assertEquals("User 2", customerRewardRepository.findById(c1.getId()).get().getName());
		assertEquals(beforeCount, customerRewardRepository.count(), "save with existing id should update, not insert");
	}

}

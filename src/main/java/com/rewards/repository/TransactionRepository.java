package com.rewards.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rewards.entity.TransactionEntity;

import jakarta.transaction.Transactional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

	List<TransactionEntity> findByCustomerId(Long customerId);

	List<TransactionEntity> findByCustomerIdIn(Collection<Long> customerIds);

	@Transactional
	@Modifying
	@Query("DELETE FROM TransactionEntity t WHERE t.customerId = :customerId")
	int deleteByCustomerId(@Param("customerId") Long customerId);

}

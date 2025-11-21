package com.rewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rewards.entity.CustomerRewardEntity;

import jakarta.transaction.Transactional;

@Repository
public interface CustomerRewardRepository extends JpaRepository<CustomerRewardEntity, Long> {

	@Transactional
	@Modifying
	@Query("DELETE FROM CustomerRewardEntity c WHERE c.id = :id")
	int deleteByIdReturnCount(@Param("id") Long id);
}

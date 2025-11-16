package com.rewards.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rewards.entity.CustomerRewardEntity;
@Repository
public interface CustomerRewardRepository  extends JpaRepository<CustomerRewardEntity, Long> {
 }

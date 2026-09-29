package com.santosh.aiproductionintelligence.repository;

import com.santosh.aiproductionintelligence.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxEventRepository  extends JpaRepository<OutboxEvent, UUID> {








}

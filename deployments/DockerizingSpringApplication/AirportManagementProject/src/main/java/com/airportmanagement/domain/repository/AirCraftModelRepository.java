package com.airportmanagement.domain.repository;

import com.airportmanagement.domain.entity.AirCraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirCraftModelRepository extends JpaRepository<AirCraftEntity, String> {
}

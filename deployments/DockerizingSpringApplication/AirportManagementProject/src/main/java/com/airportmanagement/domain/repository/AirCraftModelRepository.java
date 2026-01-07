package com.airportmanagement.domain.repository;

import com.airportmanagement.domain.entity.AirCraftModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirCraftModelRepository extends JpaRepository<AirCraftModel, String> {
}

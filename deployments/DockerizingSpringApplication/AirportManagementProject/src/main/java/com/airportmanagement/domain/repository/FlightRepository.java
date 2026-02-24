package com.airportmanagement.domain.repository;

import com.airportmanagement.domain.entity.FlightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<FlightEntity, Long> {

}

package com.airportmanagement.domain.repository;

import com.airportmanagement.domain.entity.SeatEntity;
import com.airportmanagement.domain.enums.FareConditions;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<SeatEntity, String> {

  List<SeatEntity> findByFareConditions(FareConditions fareCondition);
}


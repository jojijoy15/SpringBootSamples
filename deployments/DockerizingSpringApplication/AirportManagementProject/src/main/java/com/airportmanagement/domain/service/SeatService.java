package com.airportmanagement.domain.service;

import com.airportmanagement.domain.dto.Seat;
import com.airportmanagement.domain.entity.SeatEntity;
import com.airportmanagement.domain.enums.FareConditions;
import com.airportmanagement.domain.mapper.SeatDtoMapper;
import com.airportmanagement.domain.repository.SeatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatService {

  private final SeatRepository repository;
  private final SeatDtoMapper mapper;

  public List<Seat> findSeatsByFareCondition(FareConditions fareConditions) {
    final List<SeatEntity> seats = repository.findByFareConditions(fareConditions);
    return mapper.mapToSeats(seats);
  }

}

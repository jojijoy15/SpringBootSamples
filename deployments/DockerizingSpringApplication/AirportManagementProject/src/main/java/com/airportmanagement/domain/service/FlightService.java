package com.airportmanagement.domain.service;

import com.airportmanagement.domain.dto.FlightDetails;
import com.airportmanagement.domain.entity.FlightEntity;
import com.airportmanagement.domain.mapper.FlightsDtoMapper;
import com.airportmanagement.domain.repository.FlightRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FlightService {

  private final FlightRepository repository;
  private final FlightsDtoMapper mapper;

  public List<FlightDetails> fetchAllFlights(Pageable pageable) {
    Page<FlightEntity> flights = repository.findAll(pageable);
    return mapper.mapToFlightDetails(flights.get().toList());
  }
}

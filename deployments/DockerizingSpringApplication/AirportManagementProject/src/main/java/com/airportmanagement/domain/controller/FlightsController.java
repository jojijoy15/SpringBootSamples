package com.airportmanagement.domain.controller;

import com.airportmanagement.domain.dto.FlightDetails;
import com.airportmanagement.domain.service.FlightService;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
public class FlightsController {

  private final FlightService service;

  @GetMapping("/flights")
  public ResponseEntity<List<FlightDetails>> fetchAllFlights(@RequestParam("pageNo") @Nonnull @Min(value = 1, message = "pageNo has to be a positive value") int pageNo,
      @RequestParam("size") int size) {
    PageRequest pageRequest = PageRequest.of(pageNo, size);
    List<FlightDetails> flightDetails = service.fetchAllFlights(pageRequest);
    return ResponseEntity.ok(flightDetails);
  }

}

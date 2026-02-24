package com.airportmanagement.domain.controller;

import com.airportmanagement.domain.dto.Seat;
import com.airportmanagement.domain.dto.Status;
import com.airportmanagement.domain.enums.FareConditions;
import com.airportmanagement.domain.service.SeatService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
//@Validated
public class SeatsController {

  private final SeatService service;

  @GetMapping("/seats/{fareCondition}")
  //TODO: Do fare condition validation
  public ResponseEntity<List<Seat>> findSeatsByFareConditions(@PathVariable("fareCondition") String fareCondition) {
    List<Seat> seats = service.findSeatsByFareCondition(FareConditions.valueOf(fareCondition));
    return ResponseEntity.ok(seats);
  }


  @PostMapping("/seats/create")
  public ResponseEntity<Status> createSeats(@Valid @RequestBody Seat seat) {
    log.info("Seat created: {}", seat);
    Status status = new Status();
    status.setSuccess("Created");
    return ResponseEntity.ok(status);
  }

}

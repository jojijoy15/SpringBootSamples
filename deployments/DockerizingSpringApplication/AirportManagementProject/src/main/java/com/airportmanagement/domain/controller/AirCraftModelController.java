package com.airportmanagement.domain.controller;

import com.airportmanagement.domain.dto.AirCraft;
import com.airportmanagement.domain.service.AirCraftModelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class AirCraftModelController {

  private final AirCraftModelService service;

  @GetMapping("/aircraft/models")
  public ResponseEntity<List<AirCraft>> getAllAirCraftModels() {
    log.info("Request received for fetching aircraft models");
    return ResponseEntity.ok(service.getAllAirCrafts());
  }
}

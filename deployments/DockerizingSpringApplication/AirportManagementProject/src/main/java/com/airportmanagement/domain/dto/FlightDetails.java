package com.airportmanagement.domain.dto;

import com.airportmanagement.domain.entity.AirCraftEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlightDetails {

  private Integer flightId;
  private String flightNo;
  private LocalDateTime scheduledDeparture;
  private LocalDateTime scheduledArrival;
  private String departureAirport;
  private String arrivalAirport;
  private String status;
  private AirCraft airCraftCode;
  private LocalDateTime actualDeparture;
  private LocalDateTime actualArrival;

}

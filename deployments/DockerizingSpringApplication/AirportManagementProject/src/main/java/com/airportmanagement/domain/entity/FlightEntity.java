package com.airportmanagement.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "flights", schema = "bookings")
public class FlightEntity {

  @Id
  @Column(name = "flight_id")
  private Integer flightId;

  @Column(name = "flight_no")
  private String flightNo;

  @Column(name = "scheduled_departure")
  private LocalDateTime scheduledDeparture;

  @Column(name = "scheduled_arrival")
  private LocalDateTime scheduledArrival;

  @Column(name = "departure_airport")
  private String departureAirport;

  @Column(name = "arrival_airport")
  private String arrivalAirport;

  @Column(name = "status")
  private String status;

  @OneToOne
  @JoinColumn(name = "aircraft_code")
  private AirCraftEntity airCraftCode;

  @Column(name = "actual_departure")
  private LocalDateTime actualDeparture;

  @Column(name = "actual_arrival")
  private LocalDateTime actualArrival;

}

package com.airportmanagement.domain.entity;

import com.airportmanagement.domain.enums.FareConditions;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "seats", schema = "bookings")
@Getter
@Setter
public class SeatEntity {

  @ManyToOne
  @JoinColumn(name = "aircraft_code")
  private AirCraftEntity airCraftCode;

  @Id
  @Column(name="seat_no")
  private String seatNo;

  @Column(name="fare_conditions")
  @Enumerated(EnumType.STRING)
  //  @ColumnTransformer
  private FareConditions fareConditions; //limitation
}

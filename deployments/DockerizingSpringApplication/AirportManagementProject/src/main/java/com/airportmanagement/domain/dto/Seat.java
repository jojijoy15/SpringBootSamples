package com.airportmanagement.domain.dto;

import com.airportmanagement.domain.dto.validators.FareCondition;
import com.airportmanagement.domain.enums.FareConditions;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seat {

  private String airCraftCode;
  private String seatNo;

  @FareCondition //Change to string
  private FareConditions fareConditions;

}

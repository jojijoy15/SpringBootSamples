package com.airportmanagement.domain.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AirCraft {

  private String airCraftCode;
  private Integer range;
  private JsonNode model;

}

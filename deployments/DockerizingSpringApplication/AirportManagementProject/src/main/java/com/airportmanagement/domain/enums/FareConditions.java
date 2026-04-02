package com.airportmanagement.domain.enums;

import java.util.Arrays;

public enum FareConditions {

  //TODO: make this caps and adjust query
  Economy("Economy"),
  Business("Business"),
  Comfort("Comfort");

  private final String value;

  public String getValue() {
    return this.value;
  }

  public static FareConditions fromValue(String value) {
    return Arrays.stream(values())
      .filter(fareConditions -> fareConditions.getValue().equalsIgnoreCase(value)
        || fareConditions.name().equalsIgnoreCase(value))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Invalid fare condition: " + value));
  }

  FareConditions(String value) {
    this.value = value;
  }
}

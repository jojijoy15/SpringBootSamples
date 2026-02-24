package com.airportmanagement.domain.enums;

public enum FareConditions {

  //TODO: make this caps and adjust query
  Economy("Economy"),
  Business("Business"),
  Comfort("Comfort");

  private String value;

  public String getValue() {
    return this.value;
  }

  FareConditions(String value) {
    this.value = value;
  }
}

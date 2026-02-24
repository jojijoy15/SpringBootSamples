package com.airportmanagement.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestError {

  private String errorCode;
  private String errorDescription;

}

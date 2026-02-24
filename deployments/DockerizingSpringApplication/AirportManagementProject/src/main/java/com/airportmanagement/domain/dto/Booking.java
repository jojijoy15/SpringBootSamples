  package com.airportmanagement.domain.dto;

  import com.fasterxml.jackson.annotation.JsonInclude;
  import java.time.LocalDateTime;
  import java.util.List;
  import lombok.Getter;
  import lombok.Setter;
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.stereotype.Component;

  @Getter
  @Setter
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public class Booking {

    private String bookingReference;
    private LocalDateTime bookingDate;
    private Double amount;
    private List<String> ticketIds;
  }

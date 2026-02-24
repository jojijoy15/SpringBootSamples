package com.airportmanagement.domain.controller;

import com.airportmanagement.domain.dto.Booking;
import com.airportmanagement.domain.service.BookingService;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1")
@Validated
public class BookingController {

  private final BookingService bookingService;

  @GetMapping("/bookings")
  public ResponseEntity<List<Booking>> fetchAllBookings(@RequestParam("pageNo") @Nonnull @Min(value = 1, message = "pageNo has to be a positive value") int pageNo,
      @RequestParam("size") int size) {
    PageRequest page = PageRequest.of(pageNo-1, size);
    return ResponseEntity.ok(bookingService.fetchAllBooking(page));
  }

}

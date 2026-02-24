package com.airportmanagement.domain.controller;


import com.airportmanagement.domain.dto.Ticket;
import com.airportmanagement.domain.service.TicketService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TicketsController {

	private final TicketService ticketService;

	@GetMapping(path = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Ticket>> getAllTickets(@RequestParam("pageNo") @Nonnull @Min(value = 1, message = "pageNo has to be a positive value") int pageNo,
			@RequestParam("size") int size) {
		PageRequest page = PageRequest.of(pageNo-1, size);
		return ResponseEntity.ok(ticketService.findAllTickets(page));
	}

}

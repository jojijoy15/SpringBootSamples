package com.airportmanagement.domain.service;

import com.airportmanagement.domain.dto.Ticket;
import com.airportmanagement.domain.entity.TicketEntity;
import com.airportmanagement.domain.mapper.TicketsDtoMapper;
import com.airportmanagement.domain.repository.TicketRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository repository;
  private final TicketsDtoMapper mapper;

  public List<Ticket> findAllTickets(PageRequest pageRequest) {
    Page<TicketEntity> entities = repository.findAll(pageRequest);
    List<TicketEntity> ticketEntities = entities.get().toList();
    return mapper.ticketEntityToTicket(ticketEntities);
  }

}

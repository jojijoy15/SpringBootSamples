package com.airportmanagement.domain.mapper;

import com.airportmanagement.domain.dto.Ticket;
import com.airportmanagement.domain.entity.TicketEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TicketsDtoMapper {

  List<Ticket> ticketEntityToTicket(List<TicketEntity> entities);

  @Mapping(
      source = "bookingReference",
      target = "booking"
  )
  Ticket mapToTicket(TicketEntity entity);
}

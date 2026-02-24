package com.airportmanagement.domain.mapper;

import com.airportmanagement.domain.dto.Booking;
import com.airportmanagement.domain.dto.Ticket;
import com.airportmanagement.domain.entity.BookingEntity;
import com.airportmanagement.domain.entity.TicketEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface BookingDtoMapper {

  @Mapping(target = "ticketIds", source = "tickets", qualifiedByName = "extractTicketIds")
  Booking mapToBooking(BookingEntity entity);

  List<Booking> mapToBookings(List<BookingEntity> entities);

  @Mapping(target = "booking", ignore = true)
  Ticket mapToTicket(TicketEntity entity);

  @Named("extractTicketIds")
  default List<String> extractTicketIds(Set<TicketEntity> tickets) {
    return tickets.stream()
        .map(TicketEntity::getTicketNo)
        .collect(Collectors.toList());
  }


}

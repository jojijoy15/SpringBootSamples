package com.airportmanagement.domain.service;

import com.airportmanagement.domain.dto.Booking;
import com.airportmanagement.domain.entity.BookingEntity;
import com.airportmanagement.domain.mapper.BookingDtoMapper;
import com.airportmanagement.domain.repository.BookingRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

  private final BookingRepository repository;
  private final BookingDtoMapper dtoMapper;

  public List<Booking> fetchAllBooking(PageRequest pageRequest) {
    Page<BookingEntity> pagedEntities = repository.findAll(pageRequest);
    List<BookingEntity> entities = pagedEntities.get().toList();
    BookingEntity bk = entities.stream().findFirst().get();
    return dtoMapper.mapToBookings(entities);
  }

}

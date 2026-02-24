package com.airportmanagement.domain.repository;

import com.airportmanagement.domain.entity.BookingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<BookingEntity, String> {

//  @Query("SELECT b FROM BookingEntity b JOIN FETCH b.tickets")
  public Page<BookingEntity> findAll(Pageable pageable);
}

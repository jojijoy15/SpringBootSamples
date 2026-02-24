package com.airportmanagement.domain.repository;

import com.airportmanagement.domain.entity.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, String> {

  Page<TicketEntity> findAll(Pageable pageable);
}

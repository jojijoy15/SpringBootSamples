package com.airportmanagement.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookings", schema = "bookings")
@Setter
@Getter
public class BookingEntity {

  @Id
  @Column(name="book_ref")
  private String bookingReference;

  @Column(name="book_date")
  private LocalDateTime bookingDate;

  @Column(name="total_amount")
  private Double amount;

  @OneToMany(mappedBy = "bookingReference") //Inverse Side
  private Set<TicketEntity> tickets;

}

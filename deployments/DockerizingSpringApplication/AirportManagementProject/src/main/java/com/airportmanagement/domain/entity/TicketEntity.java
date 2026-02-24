package com.airportmanagement.domain.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.repository.NativeQuery;

@Entity
@Table(name= "tickets", schema = "bookings")
@Setter
@Getter
public class TicketEntity {

  @Id
  @GeneratedValue
  @Column(name = "ticket_no", length = 13)
  private String ticketNo;

  @ManyToOne
  @JoinColumn(name = "book_ref")//owning side
  private BookingEntity bookingReference;

  @Column(name = "passenger_id")
  private String passengerId;

  @Column(name = "passenger_name")
  private String passengerName;

  @Column(name = "contact_data")
  @Type(JsonType.class)
  private JsonNode contactData;

}

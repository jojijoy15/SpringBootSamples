package com.airportmanagement.domain.entity;


import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;


@Setter
@Getter
@Entity
@Table(name = "aircrafts_data", schema = "bookings")
public class AirCraftEntity {

	@Id
	@Column(name = "aircraft_code")
	private String airCraftCode;

	@Column(name = "range")
	private Integer range;

	@Column(name = "model")
	@Type(JsonType.class)
	private JsonNode model;

	@OneToMany(mappedBy = "airCraftCode")
	private Set<SeatEntity> seats;

}

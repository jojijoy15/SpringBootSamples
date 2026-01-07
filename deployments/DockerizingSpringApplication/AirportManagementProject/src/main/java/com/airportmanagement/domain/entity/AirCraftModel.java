package com.airportmanagement.domain.entity;


import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Immutable;


@Setter
@Getter
@Entity
@Table(name = "aircrafts_data", schema = "bookings")
@Immutable
public class AirCraftModel {

	@Id
	@Column(name = "aircraft_code")
	private String airCraftCode;

	@Column(name = "range")
	private Integer range;

	@Column(name = "model")
	@Type(JsonType.class)
	private JsonNode model;

}

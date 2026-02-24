package com.airportmanagement.domain.mapper;

import com.airportmanagement.domain.dto.AirCraft;
import com.airportmanagement.domain.dto.FlightDetails;
import com.airportmanagement.domain.entity.AirCraftEntity;
import com.airportmanagement.domain.entity.FlightEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface FlightsDtoMapper {

  List<FlightDetails> mapToFlightDetails(List<FlightEntity> entities);

  FlightDetails mapToFlights(FlightEntity entity);

  AirCraft mapToAirCraft(AirCraftEntity airCraftEntity);

}

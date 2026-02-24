package com.airportmanagement.domain.mapper;

import com.airportmanagement.domain.dto.Seat;
import com.airportmanagement.domain.entity.AirCraftEntity;
import com.airportmanagement.domain.entity.SeatEntity;
import jakarta.persistence.FetchType;
import java.util.List;
import org.hibernate.annotations.FetchMode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface SeatDtoMapper {

  @Mapping(target = "airCraftCode", source = "airCraftCode", qualifiedByName = "extractAirCraftCode")
  Seat mapToSeat(SeatEntity entity);

  List<Seat> mapToSeats(List<SeatEntity> entities);

  @Named("extractAirCraftCode")
  default String extractAirCraftCode(AirCraftEntity airCraftEntity) {
    return airCraftEntity.getAirCraftCode();
  }

}

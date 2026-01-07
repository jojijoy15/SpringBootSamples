package com.airportmanagement.domain.mapper;

import com.airportmanagement.domain.dto.AirCraft;
import com.airportmanagement.domain.entity.AirCraftModel;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper()
public interface AirCraftModelDtoMapper {

  List<AirCraft> airCraftModeltoAirCraft(List<AirCraftModel> model);

}

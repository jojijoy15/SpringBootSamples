package com.airportmanagement.domain.service;

import com.airportmanagement.domain.dto.AirCraft;
import com.airportmanagement.domain.entity.AirCraftModel;
import com.airportmanagement.domain.mapper.AirCraftModelDtoMapper;
import com.airportmanagement.domain.repository.AirCraftModelRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirCraftModelService {

  private final AirCraftModelRepository repository;
  private final AirCraftModelDtoMapper mapper;

  public List<AirCraft> getAllAirCrafts() {
    List<AirCraftModel> airCraftModels =  repository.findAll();
    return mapper.airCraftModeltoAirCraft(airCraftModels);
  }
}

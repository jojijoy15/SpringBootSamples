package com.sample.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserDto(

    @NotBlank
    Long userId,

    @Min(18)
    @Max(150)
    Integer age,

    @Pattern(regexp = "^[A-Za-z][_\\dA-Za-z]{3,30}$")
    String name
) {

}

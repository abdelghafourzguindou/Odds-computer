package com.starwars.odds.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Component
@Data
@Accessors(chain = true)
@Validated
public class MillenniumFalconDto {
    @Min(1)
    private int autonomy;
    @NotBlank
    private String departure;
    @NotBlank
    private String arrival;
    @NotBlank
    @JsonAlias("routes_db")
    private String routesDb;
}

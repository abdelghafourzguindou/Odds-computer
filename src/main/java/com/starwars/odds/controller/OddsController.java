package com.starwars.odds.controller;

import com.starwars.odds.configuration.MillenniumFalconConfiguration;
import com.starwars.odds.model.dto.EmpireDto;
import com.starwars.odds.service.OddsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Profile("web")
public class OddsController {
    private final OddsService oddsService;

    private final MillenniumFalconConfiguration millenniumFalconDto;

    /**
     *
     * @param empireDto countdown and bounty hunters' positions
     * @return Http status code 200 with probability of the Millennium Falcon to reach the destination
     */
    @PostMapping
    public ResponseEntity<Float> computeOdds(@Valid @RequestBody EmpireDto empireDto) {
        return ResponseEntity.ok(oddsService.compute(empireDto, millenniumFalconDto));
    }
}

package com.starwars.odds.service;

import com.starwars.odds.model.dto.BountyHunterDto;
import com.starwars.odds.model.dto.EmpireDto;
import com.starwars.odds.model.dto.MillenniumFalconDto;
import com.starwars.odds.model.entity.Route;
import com.starwars.odds.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("web")
class OddsServiceTest {
    @Autowired
    private OddsService oddsService;

    @Mock
    private RouteRepository routeRepository;

    @BeforeEach
    void setUp() {
        var routes = List.of(
                new Route().setOrigin("Tatooine").setDestination("Dagobah").setTravelTime(6),
                new Route().setOrigin("Dagobah").setDestination("Endor").setTravelTime(4),
                new Route().setOrigin("Dagobah").setDestination("Hoth").setTravelTime(1),
                new Route().setOrigin("Hoth").setDestination("Endor").setTravelTime(1),
                new Route().setOrigin("Tatooine").setDestination("Hoth").setTravelTime(6)
        );
        Mockito.when(routeRepository.findAll()).thenReturn(routes);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    void computeTestWithSpecificMillenniumFalcon(float expectedResult, EmpireDto empireDto, MillenniumFalconDto millenniumFalconDto) {
        System.out.printf(millenniumFalconDto.toString());
        var result = oddsService.compute(empireDto, millenniumFalconDto);
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> dataProvider() {

        var bountyHunters = List.of(
                new BountyHunterDto("Hoth", 6),
                new BountyHunterDto("Hoth", 7),
                new BountyHunterDto("Hoth", 8)
        );
        var empireDto1 = new EmpireDto(7, bountyHunters);
        var empireDto2 = new EmpireDto(8, bountyHunters);
        var empireDto3 = new EmpireDto(9, bountyHunters);
        var empireDto4 = new EmpireDto(10, bountyHunters);

        var millenniumFalconDto = new MillenniumFalconDto()
                .setArrival("Endor")
                .setDeparture("Tatooine")
                .setAutonomy(6)
                .setRoutesDb("universe.db");

        return Stream.of(
                Arguments.of(0f, empireDto1, millenniumFalconDto),
                Arguments.of(81f, empireDto2, millenniumFalconDto),
                Arguments.of(90f, empireDto3, millenniumFalconDto),
                Arguments.of(100f, empireDto4, millenniumFalconDto)
        );
    }
}

package com.starwars.odds.service;

import com.starwars.odds.model.dto.MillenniumFalconDto;
import com.starwars.odds.model.entity.Route;
import com.starwars.odds.model.pojo.AccessibleRoute;
import com.starwars.odds.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("web")
class RouteServiceTest {
    @Mock
    private RouteRepository routeRepository;

    private RouteService routeService;

    @BeforeEach
    void setUp() {
        routeService = Mockito.spy(new RouteService(routeRepository));
    }

    @Test
    void getAccessibleRoutesMapTest() {
        // Given 3 routes;
        var route1 = new Route()
                .setOrigin("Tatooine")
                .setDestination("Dagobah")
                .setTravelTime(6);

        var route2 = new Route()
                .setOrigin("Dagobah")
                .setDestination("Endor")
                .setTravelTime(4);

        var route3 = new Route()
                .setOrigin("Hoth")
                .setDestination("Endor")
                .setTravelTime(1);

        var routes = new ArrayList<>(List.of(route1, route2, route3));

        var millenniumFalconDto = new MillenniumFalconDto()
                .setDeparture("Tatooine")
                .setArrival("Endor")
                .setAutonomy(6)
                .setRoutesDb("universe.db");

        Mockito.when(routeRepository.findAll()).thenReturn(routes);

        // When we get the accessible routes map
        var result = routeService.getAccessibleRoutesMap(millenniumFalconDto);

        // Then we should get the for each planet, a list of planet accessible
        assertTrue(result.get("Tatooine").contains(new AccessibleRoute("Dagobah", 6)));
        assertTrue(result.get("Dagobah").containsAll(Set.of(new AccessibleRoute("Tatooine", 6), new AccessibleRoute("Endor", 4))));
        assertTrue(result.get("Endor").containsAll(Set.of(new AccessibleRoute("Dagobah", 4), new AccessibleRoute("Hoth", 1))));
        assertTrue(result.get("Hoth").contains(new AccessibleRoute("Endor", 1)));
    }
}

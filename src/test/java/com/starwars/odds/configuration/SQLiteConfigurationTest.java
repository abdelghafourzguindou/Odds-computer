package com.starwars.odds.configuration;

import com.starwars.odds.model.entity.Route;
import com.starwars.odds.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ActiveProfiles;

import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("web")
class SQLiteConfigurationTest {
    @Autowired
    private DriverManagerDataSource driverManagerDataSource;
    @Autowired
    private RouteRepository routeRepository;

    @Test
    void databaseConfigurationTest() {
        assertEquals("jdbc:sqlite:universe.db", driverManagerDataSource.getUrl());
    }

    @Test
    void allRoutesLoadedTest() {
        // Given all routes defined in the universe.db file
        var route1 = new Route()
                .setOrigin("Tatooine")
                .setDestination("Dagobah")
                .setTravelTime(6);

        var route2 = new Route()
                .setOrigin("Dagobah")
                .setDestination("Endor")
                .setTravelTime(4);

        var route3 = new Route()
                .setOrigin("Dagobah")
                .setDestination("Hoth")
                .setTravelTime(1);

        var route4 = new Route()
                .setOrigin("Hoth")
                .setDestination("Endor")
                .setTravelTime(1);

        var route5 = new Route()
                .setOrigin("Tatooine")
                .setDestination("Hoth")
                .setTravelTime(6);

        // When all routes from the database are retrieved
        var allRoutes = routeRepository.findAll();

        // Then all routes are present
        assertEquals(5, allRoutes.size());
        assertTrue(allRoutes.contains(route1));
        assertTrue(allRoutes.contains(route2));
        assertTrue(allRoutes.contains(route3));
        assertTrue(allRoutes.contains(route4));
        assertTrue(allRoutes.contains(route5));
    }
}

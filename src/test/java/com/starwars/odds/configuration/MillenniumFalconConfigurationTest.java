package com.starwars.odds.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("web")
class MillenniumFalconConfigurationTest {
    @Autowired
    private MillenniumFalconConfiguration millenniumFalconConfiguration;

    @Test
    void millenniumFalconConfigurationTest() {
        assertEquals(6, millenniumFalconConfiguration.getAutonomy());
        assertEquals("Tatooine", millenniumFalconConfiguration.getDeparture());
        assertEquals("Endor", millenniumFalconConfiguration.getArrival());
        assertEquals("universe.db", millenniumFalconConfiguration.getRoutesDb());
    }

}

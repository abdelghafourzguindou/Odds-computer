package com.starwars.odds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starwars.odds.model.dto.BountyHunterDto;
import com.starwars.odds.model.dto.EmpireDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("web")
class OddsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void postOddsTest() throws Exception {
        // Given valid empire information
        var bountyHunters = List.of(new BountyHunterDto("Hoth", 6));
        var empireInformationDto = new EmpireDto(7, bountyHunters);

        // When performing post method
        // Then we should get http 200 code
        mockMvc.perform(post("/")
                .content(asJsonString(empireInformationDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    void postOddsNullPlanetTest() throws Exception {
        // Given empire information with null planet property
        var bountyHunters = List.of(new BountyHunterDto(null, 6));
        var empireInformationDto = new EmpireDto(7, bountyHunters);

        // When performing post method
        // Then we should get a 400 http error code
        mockMvc.perform(post("/")
                        .content(asJsonString(empireInformationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).
                andExpect(status().is4xxClientError());
    }

    @Test
    void postOddsEmptyPlanetTest() throws Exception {
        // Given empire information with empty planet property
        var bountyHunters = List.of(new BountyHunterDto("", 6));
        var empireInformationDto = new EmpireDto(7, bountyHunters);

        // When performing post method
        // Then we should get a 400 http error code
        mockMvc.perform(post("/")
                        .content(asJsonString(empireInformationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).
                andExpect(status().is4xxClientError());
    }

    @Test
    void postOddsNegativeCountdownTest() throws Exception {
        // Given empire information with negative countdown property
        var bountyHunters = List.of(new BountyHunterDto("Hoth", 6));

        // When performing post method
        // Then we should get a 400 http error code
        var empireInformationDto = new EmpireDto(-1, bountyHunters);
        mockMvc.perform(post("/")
                        .content(asJsonString(empireInformationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).
                andExpect(status().is4xxClientError());
    }



    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.starwars.odds.model.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TravelStep {
    private String currentPlanet;
    private int autonomy;
    private int numberOfDays = 0;
    private int numberOfMeetingsWithHunters = 0;
}


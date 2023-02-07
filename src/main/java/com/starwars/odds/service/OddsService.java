package com.starwars.odds.service;

import com.starwars.odds.model.dto.BountyHunterDto;
import com.starwars.odds.model.dto.MillenniumFalconDto;
import com.starwars.odds.model.pojo.AccessibleRoute;
import com.starwars.odds.model.pojo.TravelStep;
import com.starwars.odds.model.dto.EmpireDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OddsService {
    private final RouteService routeService;

    /**
     * Compute odds related to any Millennium Falcon config
     * @param empireDto countdown and bounty hunters' positions
     * @param millenniumFalconDto configuration file that contains autonomy, departure, arrival and route db
     * @return The odds that the Millennium Falcon reaches the destination in time
     */
    public float compute(EmpireDto empireDto, MillenniumFalconDto millenniumFalconDto) {
        return computeFinalOdd(findMinNumberOfMeetingsWithHunters(empireDto, millenniumFalconDto));
    }

    /**
     * @param empireDto countdown and bounty hunters' positions
     * @param millenniumFalconDto configuration file that contains autonomy, departure, arrival and route db
     * @return The minimum number of meetings with hunters
     */
    private int findMinNumberOfMeetingsWithHunters(EmpireDto empireDto, MillenniumFalconDto millenniumFalconDto) {
        int minNumberOfMeetingsWithHunters = Integer.MAX_VALUE;
        var accessibleRoutes = routeService.getAccessibleRoutesMap(millenniumFalconDto);

        var huntersPositionsByDays = getHuntersPositionsByDay(empireDto.bountyHunters());
        var queue = new ArrayDeque<TravelStep>();
        var departureStep = new TravelStep()
                .setCurrentPlanet(millenniumFalconDto.getDeparture())
                .setAutonomy(millenniumFalconDto.getAutonomy())
                .setNumberOfDays(0);

        queue.add(departureStep);

        while (!queue.isEmpty()) {
            var travelStep = queue.poll();
            var numberOfMeetingsWithHunters = travelStep.getNumberOfMeetingsWithHunters();
            var numberOfDays = travelStep.getNumberOfDays();
            var currentPlanet = travelStep.getCurrentPlanet();

            if (numberOfMeetingsWithHunters < minNumberOfMeetingsWithHunters && numberOfDays <= empireDto.countdown()) {
                if (areHuntersPresentOnPlanet(numberOfDays, currentPlanet, huntersPositionsByDays)) {
                    travelStep.setNumberOfMeetingsWithHunters(numberOfMeetingsWithHunters + 1);
                }
                if (currentPlanet.equals(millenniumFalconDto.getArrival())) {
                    minNumberOfMeetingsWithHunters = numberOfMeetingsWithHunters;
                    continue;
                }

                accessibleRoutes
                        .get(currentPlanet)
                        .forEach(accessibleRoute -> processNextAccessibleRoute(accessibleRoute, travelStep, queue));

                refuelStep(travelStep, queue, millenniumFalconDto);
            }
        }

        return minNumberOfMeetingsWithHunters;
    }

    /**
     * Refuel the Millennium Falcon on the current planet
     * @param travelStep current travel step
     * @param queue queue to process all next steps
     */
    private void refuelStep(TravelStep travelStep, Queue<TravelStep> queue, MillenniumFalconDto millenniumFalconDto) {
        var refuelStep = new TravelStep()
                .setNumberOfDays(travelStep.getNumberOfDays() + 1)
                .setCurrentPlanet(travelStep.getCurrentPlanet())
                .setNumberOfMeetingsWithHunters(travelStep.getNumberOfMeetingsWithHunters())
                .setAutonomy(millenniumFalconDto.getAutonomy());
        queue.add(refuelStep);
    }

    /**
     * Process the next travel step from an accessible route
     * @param accessibleRoute Next travel step
     * @param travelStep Current travel step
     * @param queue Queue to process all next steps
     */
    private void processNextAccessibleRoute(AccessibleRoute accessibleRoute,
                                            TravelStep travelStep,
                                            Queue<TravelStep> queue) {
        if (accessibleRoute.travelTime() <= travelStep.getAutonomy()) {
            var nextTravelStep = new TravelStep()
                    .setNumberOfDays(travelStep.getNumberOfDays() + accessibleRoute.travelTime())
                    .setCurrentPlanet(accessibleRoute.planet())
                    .setNumberOfMeetingsWithHunters(travelStep.getNumberOfMeetingsWithHunters())
                    .setAutonomy(travelStep.getAutonomy() - accessibleRoute.travelTime());
            queue.add(nextTravelStep);
        }
    }

    /**
     * Get the bounty hunters positions according to the days
     * @param bountyHunters Bounty hunters positions
     * @return A map with the presence of the hunters on the planets according to the days
     */
    private Map<Integer, Set<String>> getHuntersPositionsByDay(List<BountyHunterDto> bountyHunters) {
        return bountyHunters
                .stream()
                .collect(
                        Collectors.groupingBy(
                                BountyHunterDto::day,
                                Collectors.mapping(BountyHunterDto::planet, Collectors.toSet()))
                );
    }

    /**
     * Check if bounty hunters are present on the current planet
     * @param dayNumber Current day
     * @param planet Current planet
     * @param huntersPositionsByDays Hunters positions
     * @return The presence of hunters
     */
    private boolean areHuntersPresentOnPlanet(int dayNumber,
                                              String planet,
                                              Map<Integer, Set<String>> huntersPositionsByDays) {
        return huntersPositionsByDays.get(dayNumber) != null && huntersPositionsByDays.get(dayNumber).contains(planet);
    }

    /**
     * Compute odds to survive in percentage
     * @param numberOfMeetingsWithHunters Minimum number of meetings with hunters to reach the destination
     * @return Odds to survive
     */
    private float computeFinalOdd(int numberOfMeetingsWithHunters) {
        float finalOdds = 1;
        if (numberOfMeetingsWithHunters == Integer.MAX_VALUE) {
            return 0;
        } else {
            for (int i = 0; i < numberOfMeetingsWithHunters; i++) {
                finalOdds -= Math.pow(9, i)/ Math.pow(10, i+1);
            }
            return finalOdds * 100;
        }
    }

}

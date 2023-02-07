package com.starwars.odds.service;

import com.starwars.odds.model.dto.MillenniumFalconDto;
import com.starwars.odds.model.pojo.AccessibleRoute;
import com.starwars.odds.repository.RouteRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;

    /**
     * Get a Map of the planets accessible from each planet
     * @return Map of accessible routes
     */
    public Map<String, Set<AccessibleRoute>> getAccessibleRoutesMap(MillenniumFalconDto millenniumFalcon) {
        var routes = routeRepository.findAll();
        var routesAccessible = new HashMap<String, Set<AccessibleRoute>>();
        routes
                .stream()
                .filter(route -> route.getTravelTime() <= millenniumFalcon.getAutonomy())
                .forEach(route -> {
                    fillAccessibleRoutes(route.getOrigin(), route.getDestination(), route.getTravelTime(), routesAccessible);
                    fillAccessibleRoutes(route.getDestination(), route.getOrigin(), route.getTravelTime(), routesAccessible);
        });
        return routesAccessible;
    }

    /**
     * Fill the accessible routes map
     * @param origin origin planet
     * @param destination destination planet
     * @param autonomy Autonomy needed to reach the destination from origin
     * @param accessibleRoutes current map of the accessible routes
     */
    private void fillAccessibleRoutes(String origin, String destination,
                                      int autonomy,
                                      Map<String, Set<AccessibleRoute>> accessibleRoutes) {
        var accessibleRoute = new AccessibleRoute(destination, autonomy);
        if (!accessibleRoutes.containsKey(origin)) {
            accessibleRoutes.put(origin, new HashSet<>(Set.of(accessibleRoute)));
        } else {
            accessibleRoutes.get(origin).add(accessibleRoute);
        }
    }
}

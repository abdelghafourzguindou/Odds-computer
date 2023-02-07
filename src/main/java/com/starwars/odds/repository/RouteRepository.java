package com.starwars.odds.repository;

import com.starwars.odds.model.entity.Route;
import com.starwars.odds.model.entity.RouteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, RouteId> {}

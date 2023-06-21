package com.softuni.Pathfinder.repository;

import com.softuni.Pathfinder.model.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Long> {

    @Query("SELECT r FROM RouteEntity r")
    List<RouteEntity> getAllRoutes();

    RouteEntity getById(Long routeId);

    Optional<RouteEntity> getByName(String name);

    @Query("SELECT r FROM RouteEntity r JOIN r.categories c WHERE c.name = 'PEDESTRIAN'")
    List<RouteEntity> getAllPedestrianRoutes();

    @Query("SELECT r FROM RouteEntity r JOIN r.categories c WHERE c.name = 'CAR'")
    List<RouteEntity> getAllCarRoutes();

    @Query("SELECT r FROM RouteEntity r JOIN r.categories c WHERE c.name = 'BICYCLE'")
    List<RouteEntity> getAllBicycleRoutes();

    @Query("SELECT r FROM RouteEntity r JOIN r.categories c WHERE c.name = 'MOTORCYCLE'")
    List<RouteEntity> getAllMotorcycleRoutes();

    @Query("SELECT r FROM RouteEntity r ORDER BY size(r.comments) DESC LIMIT 1")
    RouteEntity getMostCommentedRoute();
}

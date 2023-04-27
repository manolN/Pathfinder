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
}

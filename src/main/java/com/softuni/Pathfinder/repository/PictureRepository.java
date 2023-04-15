package com.softuni.Pathfinder.repository;

import com.softuni.Pathfinder.model.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

    List<PictureEntity> findByRouteId(Long routeId);
}

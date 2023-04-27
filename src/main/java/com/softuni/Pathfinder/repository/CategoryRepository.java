package com.softuni.Pathfinder.repository;

import com.softuni.Pathfinder.model.entity.CategoryEntity;
import com.softuni.Pathfinder.model.entity.enums.CategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByName(CategoryEnum name);
}

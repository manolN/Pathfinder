package com.softuni.Pathfinder.repository;


import com.softuni.Pathfinder.model.entity.RoleEntity;
import com.softuni.Pathfinder.model.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByRole(RoleEnum role);
}

package com.softuni.Pathfinder.model.entity;

import com.softuni.Pathfinder.model.entity.enums.RoleEnum;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public RoleEnum getRole() {
        return role;
    }

    public RoleEntity setRole(RoleEnum role) {
        this.role = role;
        return this;
    }
}

package com.softuni.Pathfinder.model.entity;

import com.softuni.Pathfinder.model.entity.enums.LevelEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;
    private Integer age;
    @Column(nullable = false)
    private String fullName;
    @Enumerated(EnumType.STRING)
    private LevelEnum level;
    @Column(nullable = false)
    private String password;
    @ManyToMany
    private List<RoleEntity> roles;

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserEntity setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public UserEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public UserEntity setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }
}

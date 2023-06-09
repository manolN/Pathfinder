package com.softuni.Pathfinder.model.entity;

import com.softuni.Pathfinder.model.entity.enums.LevelEnum;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "routes")
public class RouteEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    @Column(columnDefinition = "LONGTEXT")
    private String gpxCoordinates;
    @Enumerated(EnumType.STRING)
    private LevelEnum level;
    private String videoUrl;
    @ManyToOne
    private UserEntity author;
    @ManyToMany
    private List<CategoryEntity> categories;
    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    public String getName() {
        return name;
    }

    public RouteEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RouteEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getGpxCoordinates() {
        return gpxCoordinates;
    }

    public RouteEntity setGpxCoordinates(String gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public RouteEntity setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RouteEntity setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public RouteEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public RouteEntity setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public RouteEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }
}

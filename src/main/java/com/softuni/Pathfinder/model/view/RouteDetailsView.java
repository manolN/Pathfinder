package com.softuni.Pathfinder.model.view;

import com.softuni.Pathfinder.model.entity.CategoryEntity;
import com.softuni.Pathfinder.model.entity.PictureEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.entity.enums.LevelEnum;

import java.util.List;

public class RouteDetailsView {

    private Long id;
    private String name;
    private String description;
    private String gpxCoordinates;
    private LevelEnum level;
    private String videoUrl;
    private UserEntity author;
    private List<CategoryEntity> categories;
    private List<PictureEntity> pictures;

    public Long getId() {
        return id;
    }

    public RouteDetailsView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RouteDetailsView setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RouteDetailsView setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getGpxCoordinates() {
        return gpxCoordinates;
    }

    public RouteDetailsView setGpxCoordinates(String gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public RouteDetailsView setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RouteDetailsView setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public RouteDetailsView setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public RouteDetailsView setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
        return this;
    }

    public List<PictureEntity> getPictures() {
        return pictures;
    }

    public RouteDetailsView setPictures(List<PictureEntity> pictures) {
        this.pictures = pictures;
        return this;
    }
}

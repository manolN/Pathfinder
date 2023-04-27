package com.softuni.Pathfinder.model.service;

import com.softuni.Pathfinder.model.entity.enums.CategoryEnum;
import com.softuni.Pathfinder.model.entity.enums.LevelEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RouteAddServiceModel {

    private String name;
    private String description;
    private MultipartFile gpxCoordinates;
    private LevelEnum level;
    private String videoUrl;
    private List<CategoryEnum> categories;

    public String getName() {
        return name;
    }

    public RouteAddServiceModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RouteAddServiceModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public MultipartFile getGpxCoordinates() {
        return gpxCoordinates;
    }

    public RouteAddServiceModel setGpxCoordinates(MultipartFile gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public RouteAddServiceModel setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RouteAddServiceModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public List<CategoryEnum> getCategories() {
        return categories;
    }

    public RouteAddServiceModel setCategories(List<CategoryEnum> categories) {
        this.categories = categories;
        return this;
    }
}

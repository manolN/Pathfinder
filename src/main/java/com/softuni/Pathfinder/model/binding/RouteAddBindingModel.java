package com.softuni.Pathfinder.model.binding;

import com.softuni.Pathfinder.model.entity.enums.CategoryEnum;
import com.softuni.Pathfinder.model.entity.enums.LevelEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RouteAddBindingModel {

    @NotBlank
    @Size(min = 4)
    private String name;
    @NotBlank
    @Size(min = 5)
    private String description;
    private MultipartFile gpxCoordinates;
    @NotNull
    private LevelEnum level;
    private String videoUrl;
    @NotNull
    private List<CategoryEnum> categories;

    public String getName() {
        return name;
    }

    public RouteAddBindingModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RouteAddBindingModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public MultipartFile getGpxCoordinates() {
        return gpxCoordinates;
    }

    public RouteAddBindingModel setGpxCoordinates(MultipartFile gpxCoordinates) {
        this.gpxCoordinates = gpxCoordinates;
        return this;
    }

    public LevelEnum getLevel() {
        return level;
    }

    public RouteAddBindingModel setLevel(LevelEnum level) {
        this.level = level;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public RouteAddBindingModel setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public List<CategoryEnum> getCategories() {
        return categories;
    }

    public RouteAddBindingModel setCategories(List<CategoryEnum> categories) {
        this.categories = categories;
        return this;
    }
}

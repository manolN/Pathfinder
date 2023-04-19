package com.softuni.Pathfinder.model.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class PictureBindingModel {

    @NotBlank
    private String routeId;
    @NotBlank
    private String title;
    @NotNull
    private MultipartFile picture;

    public String getRouteId() {
        return routeId;
    }

    public PictureBindingModel setRouteId(String routeId) {
        this.routeId = routeId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PictureBindingModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public PictureBindingModel setPicture(MultipartFile picture) {
        this.picture = picture;
        return this;
    }
}

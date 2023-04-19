package com.softuni.Pathfinder.model.service;

import org.springframework.web.multipart.MultipartFile;

public class PictureServiceModel {

    private String title;
    private Long routeId;
    private MultipartFile picture;

    public String getTitle() {
        return title;
    }

    public PictureServiceModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public Long getRouteId() {
        return routeId;
    }

    public PictureServiceModel setRouteId(Long routeId) {
        this.routeId = routeId;
        return this;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public PictureServiceModel setPicture(MultipartFile picture) {
        this.picture = picture;
        return this;
    }
}

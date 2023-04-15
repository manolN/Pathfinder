package com.softuni.Pathfinder.model.view;

public class RouteView {

    private Long id;
    private String name;
    private String description;
    private String pictureUrl;

    public Long getId() {
        return id;
    }

    public RouteView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RouteView setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RouteView setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public RouteView setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }
}

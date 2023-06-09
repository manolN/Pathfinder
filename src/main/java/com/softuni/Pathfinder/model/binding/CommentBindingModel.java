package com.softuni.Pathfinder.model.binding;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentBindingModel {

    private Long routeId;
    @NotBlank
    @Size(min = 5)
    private String message;

    public Long getRouteId() {
        return routeId;
    }

    public CommentBindingModel setRouteId(Long routeId) {
        this.routeId = routeId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentBindingModel setMessage(String message) {
        this.message = message;
        return this;
    }
}

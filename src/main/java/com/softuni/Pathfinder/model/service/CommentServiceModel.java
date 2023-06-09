package com.softuni.Pathfinder.model.service;

public class CommentServiceModel {

    private Long commentId;
    private Long routeId;
    private String creator;
    private String message;

    public Long getCommentId() {
        return commentId;
    }

    public CommentServiceModel setCommentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }

    public Long getRouteId() {
        return routeId;
    }

    public CommentServiceModel setRouteId(Long routeId) {
        this.routeId = routeId;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public CommentServiceModel setCreator(String creator) {
        this.creator = creator;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentServiceModel setMessage(String message) {
        this.message = message;
        return this;
    }
}

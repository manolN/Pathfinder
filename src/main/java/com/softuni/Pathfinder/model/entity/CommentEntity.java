package com.softuni.Pathfinder.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    private boolean approved;
    @Column(columnDefinition = "TEXT")
    @Size(min = 5)
    private String textContent;
    @ManyToOne
    private UserEntity author;
    @ManyToOne
    private RouteEntity route;
    @Column(nullable = false)
    private LocalDateTime created;

    public boolean isApproved() {
        return approved;
    }

    public CommentEntity setApproved(boolean approved) {
        this.approved = approved;
        return this;
    }

    public String getTextContent() {
        return textContent;
    }

    public CommentEntity setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public CommentEntity setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public RouteEntity getRoute() {
        return route;
    }

    public CommentEntity setRoute(RouteEntity route) {
        this.route = route;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentEntity setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }
}

package com.softuni.Pathfinder.model.view;

import java.time.LocalDateTime;

public class CommentViewModel {

    private Long commentId;
    private String message;
    private LocalDateTime created;
    private String user;
    private boolean canEdit;

    public Long getCommentId() {
        return commentId;
    }

    public CommentViewModel setCommentId(Long commentId) {
        this.commentId = commentId;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CommentViewModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public CommentViewModel setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }

    public String getUser() {
        return user;
    }

    public CommentViewModel setUser(String user) {
        this.user = user;
        return this;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}

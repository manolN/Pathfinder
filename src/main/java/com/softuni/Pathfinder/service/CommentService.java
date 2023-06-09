package com.softuni.Pathfinder.service;

import com.softuni.Pathfinder.model.service.CommentServiceModel;
import com.softuni.Pathfinder.model.view.CommentViewModel;

import java.util.List;

public interface CommentService {
    List<CommentViewModel> getComments(Long routeId);
    CommentViewModel createComment(CommentServiceModel commentServiceModel);

    boolean canEdit(String username, Long commentId);
    boolean deleteComment(CommentServiceModel commentServiceModel);

    CommentViewModel editComment(CommentServiceModel commentServiceModel);
}

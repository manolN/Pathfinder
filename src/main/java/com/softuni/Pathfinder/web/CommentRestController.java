package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.model.binding.CommentBindingModel;
import com.softuni.Pathfinder.model.service.CommentServiceModel;
import com.softuni.Pathfinder.model.validation.ApiError;
import com.softuni.Pathfinder.model.view.CommentViewModel;
import com.softuni.Pathfinder.service.CommentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
public class CommentRestController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;

    public CommentRestController(CommentService commentService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/api/{routeId}/comments")
    public ResponseEntity<List<CommentViewModel>> getAllComments(@PathVariable Long routeId, Principal principal) {

        List<CommentViewModel> comments = commentService.getComments(routeId);

        comments.forEach(c -> {
            if (principal != null) {
                boolean canEdit = commentService.canEdit(principal.getName(), c.getCommentId());
                c.setCanEdit(canEdit);
            }
        });

        return ResponseEntity.ok(comments);
    }

    @PostMapping("/api/{routeId}/comments/add")
    public ResponseEntity<CommentViewModel> addComment(@PathVariable Long routeId,
                                                       @RequestBody @Valid CommentBindingModel commentBindingModel,
                                                       Principal principal) {

        CommentServiceModel serviceModel = modelMapper.map(commentBindingModel, CommentServiceModel.class);
        serviceModel
                .setRouteId(routeId)
                .setCreator(principal.getName());

        CommentViewModel comment = commentService.createComment(serviceModel);

        URI locationOfNewComment =
                URI.create(String.format("/api/%s/comments/%s", routeId, comment.getCommentId()));

        return ResponseEntity
                .created(locationOfNewComment)
                .body(comment);
    }

    @DeleteMapping("/api/{routeId}/comments/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable Long routeId, @PathVariable Long commentId) {

        boolean isDeleted = commentService
                .deleteComment(new CommentServiceModel()
                        .setCommentId(commentId)
                        .setRouteId(routeId));

        if (!isDeleted) {

        }

        return ResponseEntity
                .ok("Comment deleted");
    }

    @PatchMapping("/api/{routeId}/comments/{commentId}/edit")
    public ResponseEntity<CommentViewModel> editComment(@PathVariable Long routeId, @PathVariable Long commentId,
                                               @RequestBody @Valid CommentBindingModel commentBindingModel,
                                                        Principal principal) {

        CommentServiceModel serviceModel = modelMapper.map(commentBindingModel, CommentServiceModel.class);
        serviceModel
                .setRouteId(routeId)
                .setCommentId(commentId)
                .setCreator(principal.getName());

        CommentViewModel comment = commentService.editComment(serviceModel);

        URI locationOfNewComment =
                URI.create(String.format("/api/%s/comments/%s", routeId, comment.getCommentId()));
        return ResponseEntity.created(locationOfNewComment).body(comment);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> onFailedValidation(MethodArgumentNotValidException exception) {
        ApiError errors = new ApiError(HttpStatus.BAD_REQUEST);

        exception
                .getFieldErrors()
                .forEach(fe -> errors.addFieldWithError(fe.getField()));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }
}
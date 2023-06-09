package com.softuni.Pathfinder.service.impl;

import com.softuni.Pathfinder.model.entity.CommentEntity;
import com.softuni.Pathfinder.model.entity.RoleEntity;
import com.softuni.Pathfinder.model.entity.RouteEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.entity.enums.RoleEnum;
import com.softuni.Pathfinder.model.service.CommentServiceModel;
import com.softuni.Pathfinder.model.view.CommentViewModel;
import com.softuni.Pathfinder.repository.CommentRepository;
import com.softuni.Pathfinder.repository.RouteRepository;
import com.softuni.Pathfinder.repository.UserRepository;
import com.softuni.Pathfinder.service.CommentService;
import com.softuni.Pathfinder.service.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final RouteRepository routeRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, RouteRepository routeRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<CommentViewModel> getComments(Long routeId) {
        Optional<RouteEntity> optRoute = routeRepository.findById(routeId);

        if (optRoute.isEmpty()) {
            throw new ObjectNotFoundException("Route with id " + routeId + " was not found!");
        }

        return optRoute.get()
                .getComments()
                .stream()
                .map(this::mapAsCommentViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public CommentViewModel createComment(CommentServiceModel commentServiceModel) {
        RouteEntity routeEntity = routeRepository.findById(commentServiceModel.getRouteId()).get();
        UserEntity userEntity = userRepository.findByUsername(commentServiceModel.getCreator()).get();

        CommentEntity commentEntity = mapAsCommentEntity(userEntity, routeEntity, commentServiceModel);

        commentRepository.save(commentEntity);

        return mapAsCommentViewModel(commentEntity);
    }

    @Override
    public boolean canEdit(String username, Long commentId) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        Optional<UserEntity> caller = userRepository.findByUsername(username);

        if (optionalComment.isEmpty() || caller.isEmpty()) {
            return false;
        }

        CommentEntity commentEntity = optionalComment.get();

        String authorUsername = commentEntity.getAuthor().getUsername();

        return isAdmin(caller.get()) || username.equals(authorUsername);
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .anyMatch(r -> r == RoleEnum.ADMIN);
    }

    @Override
    public boolean deleteComment(CommentServiceModel commentServiceModel) {
        commentRepository.deleteById(commentServiceModel.getCommentId());
        return true;
    }

    @Override
    public CommentViewModel editComment(CommentServiceModel commentServiceModel) {
        RouteEntity routeEntity = routeRepository.findById(commentServiceModel.getRouteId()).get();
        UserEntity userEntity = userRepository.findByUsername(commentServiceModel.getCreator()).get();

        CommentEntity commentEntity = commentRepository.findById(commentServiceModel.getCommentId()).get();
        commentEntity.setTextContent(commentServiceModel.getMessage())
                        .setCreated(LocalDateTime.now());

        commentRepository.save(commentEntity);

        return mapAsCommentViewModel(commentEntity);
    }

    private CommentViewModel mapAsCommentViewModel(CommentEntity commentEntity) {
        return new CommentViewModel()
                .setCommentId(commentEntity.getId())
                .setMessage(commentEntity.getTextContent())
                .setUser(commentEntity.getAuthor().getUsername())
                .setCreated(commentEntity.getCreated());
    }

    private CommentEntity mapAsCommentEntity(UserEntity userEntity, RouteEntity routeEntity,
                                             CommentServiceModel commentServiceModel) {

        return new CommentEntity()
                .setAuthor(userEntity)
                .setRoute(routeEntity)
                .setTextContent(commentServiceModel.getMessage())
                .setCreated(LocalDateTime.now())
                .setApproved(true);
    }
}
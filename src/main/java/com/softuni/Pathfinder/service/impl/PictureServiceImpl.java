package com.softuni.Pathfinder.service.impl;

import com.softuni.Pathfinder.model.entity.PictureEntity;
import com.softuni.Pathfinder.model.entity.RouteEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.service.PictureServiceModel;
import com.softuni.Pathfinder.repository.PictureRepository;
import com.softuni.Pathfinder.repository.RouteRepository;
import com.softuni.Pathfinder.repository.UserRepository;
import com.softuni.Pathfinder.service.CloudinaryImage;
import com.softuni.Pathfinder.service.CloudinaryService;
import com.softuni.Pathfinder.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    private final CloudinaryService cloudinaryService;
    private final RouteRepository routeRepository;

    public PictureServiceImpl(PictureRepository pictureRepository, CloudinaryService cloudinaryService,
                              RouteRepository routeRepository) {
        this.pictureRepository = pictureRepository;
        this.cloudinaryService = cloudinaryService;
        this.routeRepository = routeRepository;
    }

    @Override
    public void uploadPicture(PictureServiceModel pictureServiceModel) throws IOException {
       PictureEntity picture = createPictureEntity(pictureServiceModel.getPicture(), pictureServiceModel);

       pictureRepository.save(picture);
    }

    private PictureEntity createPictureEntity(MultipartFile file, PictureServiceModel pictureServiceModel) throws IOException {
        CloudinaryImage uploadImage = cloudinaryService.uploadImage(file);

        RouteEntity route = routeRepository.getById(pictureServiceModel.getRouteId());
        UserEntity author = route.getAuthor();

        return new PictureEntity()
                .setTitle(pictureServiceModel.getTitle())
                .setRoute(route)
                .setAuthor(author)
                .setUrl(uploadImage.getUrl());
    }
}

package com.softuni.Pathfinder.service.impl;

import com.softuni.Pathfinder.model.entity.CategoryEntity;
import com.softuni.Pathfinder.model.entity.PictureEntity;
import com.softuni.Pathfinder.model.entity.RouteEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.service.RouteAddServiceModel;
import com.softuni.Pathfinder.model.view.RouteDetailsView;
import com.softuni.Pathfinder.model.view.RouteView;
import com.softuni.Pathfinder.repository.CategoryRepository;
import com.softuni.Pathfinder.repository.PictureRepository;
import com.softuni.Pathfinder.repository.RouteRepository;
import com.softuni.Pathfinder.repository.UserRepository;
import com.softuni.Pathfinder.service.RouteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {

    private final ModelMapper modelMapper;
    private final RouteRepository routeRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public RouteServiceImpl(ModelMapper modelMapper,
                            RouteRepository routeRepository, PictureRepository pictureRepository,
                            UserRepository userRepository, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.routeRepository = routeRepository;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<RouteView> getAllRoutes() {
        List<RouteEntity> allRoutes = routeRepository.getAllRoutes();

        return allRoutes
                .stream()
                .map(r -> {
                    RouteView routeView = modelMapper.map(r, RouteView.class);
                    setPictures(r.getId(), routeView);

                    return routeView;
                })
                .collect(Collectors.toList());
    }

    @Override
    public RouteDetailsView getRouteById(Long id) {
        RouteDetailsView routeDetailsView = modelMapper
                .map(routeRepository.getById(id), RouteDetailsView.class);
        routeDetailsView.setPictures(pictureRepository.findByRouteId(id));

        return routeDetailsView;
    }

    @Override
    public boolean createRoute(RouteAddServiceModel routeAddServiceModel, Principal principal) throws IOException {
        if (routeRepository.getByName(routeAddServiceModel.getName()).orElse(null) != null) {
            return false;
        }

        RouteEntity routeEntity = modelMapper.map(routeAddServiceModel, RouteEntity.class);

        List<CategoryEntity> categories = routeAddServiceModel
                .getCategories()
                .stream()
                .map(c -> categoryRepository.findByName(c).orElse(null))
                .collect(Collectors.toList());

        routeEntity.setCategories(categories);

        if (!routeAddServiceModel.getVideoUrl().isBlank()) {
            String videoUrlSubstring = routeAddServiceModel
                    .getVideoUrl()
                    .substring(routeAddServiceModel.getVideoUrl().length() - 11);

            routeEntity.setVideoUrl(videoUrlSubstring);
        }

        if (!routeAddServiceModel.getGpxCoordinates().isEmpty()) {
            String gpxCoordinates = routeAddServiceModel
                    .getGpxCoordinates()
                    .getResource()
                    .getContentAsString(Charset.defaultCharset());

            routeEntity.setGpxCoordinates(gpxCoordinates);
        } else {
            routeEntity.setGpxCoordinates("");
        }

        UserEntity userEntity = userRepository.findByUsername(principal.getName()).get();
        routeEntity.setAuthor(userEntity);

        routeRepository.save(routeEntity);

        return true;
    }

    @Override
    public List<RouteView> getRouteByCategory(String category) {
        List<RouteEntity> routes = null;

        if (category.equalsIgnoreCase("pedestrian")) {
            routes = routeRepository.getAllPedestrianRoutes();
        } else if (category.equalsIgnoreCase("car")) {
            routes = routeRepository.getAllCarRoutes();
        } else if (category.equalsIgnoreCase("bicycle")) {
            routes = routeRepository.getAllBicycleRoutes();
        } else if (category.equalsIgnoreCase("motorcycle")) {
            routes = routeRepository.getAllMotorcycleRoutes();
        }

        return routes
                .stream()
                .map(r -> {
                    RouteView routeView = modelMapper.map(r, RouteView.class);
                    setPictures(r.getId(), routeView);

                    return routeView;
                })
                .collect(Collectors.toList());
    }

    private void setPictures(Long routeId, RouteView routeView) {
        List<PictureEntity> pictures = pictureRepository.findByRouteId(routeId);

        if (pictures.isEmpty()) {
            routeView.setPictureUrl("/images/pic4.jpg");
        } else {
            routeView.setPictureUrl(pictureRepository.findByRouteId(routeId).get(0).getUrl());
        }
    }
}
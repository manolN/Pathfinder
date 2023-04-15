package com.softuni.Pathfinder.service.impl;

import com.softuni.Pathfinder.model.entity.RouteEntity;
import com.softuni.Pathfinder.model.view.RouteView;
import com.softuni.Pathfinder.repository.PictureRepository;
import com.softuni.Pathfinder.repository.RouteRepository;
import com.softuni.Pathfinder.service.RouteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {

    private final ModelMapper modelMapper;
    private final RouteRepository routeRepository;
    private final PictureRepository pictureRepository;

    public RouteServiceImpl(ModelMapper modelMapper,
                            RouteRepository routeRepository, PictureRepository pictureRepository) {
        this.modelMapper = modelMapper;
        this.routeRepository = routeRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public List<RouteView> getAllRoutes() {
        List<RouteEntity> allRoutes = routeRepository.getAllRoutes();

        return allRoutes
                .stream()
                .map(r -> {
                    RouteView routeView = modelMapper.map(r, RouteView.class);
                    routeView.setPictureUrl(pictureRepository.findByRouteId(r.getId()).get(0).getUrl());

                    return routeView;
                })
                .collect(Collectors.toList());
    }
}

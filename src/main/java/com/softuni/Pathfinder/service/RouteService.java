package com.softuni.Pathfinder.service;

import com.softuni.Pathfinder.model.view.RouteDetailsView;
import com.softuni.Pathfinder.model.view.RouteView;

import java.util.List;

public interface RouteService {

    List<RouteView> getAllRoutes();

    RouteDetailsView getRouteById(Long id);
}

package com.softuni.Pathfinder.service;

import com.softuni.Pathfinder.model.service.RouteAddServiceModel;
import com.softuni.Pathfinder.model.view.RouteDetailsView;
import com.softuni.Pathfinder.model.view.RouteView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface RouteService {

    List<RouteView> getAllRoutes();

    RouteDetailsView getRouteById(Long id);

    boolean createRoute(RouteAddServiceModel routeAddServiceModel, Principal principal) throws IOException;

    List<RouteView> getRouteByCategory(String category);

    RouteView getMostCommentedRoute();
}

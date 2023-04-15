package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.service.RouteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public String routes(Model model) {

        model.addAttribute("routes", routeService.getAllRoutes());

        return "routes";
    }

    @GetMapping("/add")
    public String addRoute() {
        return "add-route";
    }
}

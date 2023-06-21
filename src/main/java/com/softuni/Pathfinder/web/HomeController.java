package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.service.RouteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final RouteService routeService;

    public HomeController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/")
    public String index(Model model) {

        model.addAttribute("route", routeService.getMostCommentedRoute());

        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.model.binding.RouteAddBindingModel;
import com.softuni.Pathfinder.model.entity.enums.CategoryEnum;
import com.softuni.Pathfinder.model.entity.enums.LevelEnum;
import com.softuni.Pathfinder.model.service.RouteAddServiceModel;
import com.softuni.Pathfinder.service.RouteService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;
    private final ModelMapper modelMapper;

    public RouteController(RouteService routeService, ModelMapper modelMapper) {
        this.routeService = routeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String routes(Model model) {

        model.addAttribute("routes", routeService.getAllRoutes());

        return "routes";
    }

    @GetMapping("/{id}/details")
    public String routeDetails(@PathVariable Long id, Model model) {

        model.addAttribute("route", routeService.getRouteById(id));

        return "route-details";
    }

    @GetMapping("/add")
    public String addRoute(Model model) {

        model.addAttribute("levels", LevelEnum.values());
        model.addAttribute("categories", CategoryEnum.values());

        return "add-route";
    }

    @PostMapping("/add")
    public String addRoute(@Valid RouteAddBindingModel routeAddBindingModel,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes
                    .addFlashAttribute("routeAddBindingModel", routeAddBindingModel);
            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.routeAddBindingModel",
                            bindingResult);

            return "redirect:/routes/add";
        }

        RouteAddServiceModel serviceModel = modelMapper.map(routeAddBindingModel, RouteAddServiceModel.class);

        boolean createSuccessful = routeService.createRoute(serviceModel, principal);

        if (createSuccessful) {
            return "redirect:/routes";
        }

        redirectAttributes
                .addFlashAttribute("routeAddBindingModel", routeAddBindingModel);

        return "redirect:/routes/add";
    }

    @GetMapping("/pedestrian")
    public String pedestrianRoutes(Model model) {

        model.addAttribute("pedestrianRoutes", routeService.getRouteByCategory("pedestrian"));

        return "pedestrian";
    }

    @GetMapping("/car")
    public String carRoutes(Model model) {

        model.addAttribute("carRoutes", routeService.getRouteByCategory("car"));

        return "car";
    }

    @GetMapping("/bicycle")
    public String bicycleRoutes(Model model) {

        model.addAttribute("bicycleRoutes", routeService.getRouteByCategory("bicycle"));

        return "bicycle";
    }

    @GetMapping("/motorcycle")
    public String motorcycleRoutes(Model model) {

        model.addAttribute("motorcycleRoutes", routeService.getRouteByCategory("motorcycle"));

        return "motorcycle";
    }

    @ModelAttribute
    public RouteAddBindingModel routeAddBindingModel() {
        return new RouteAddBindingModel();
    }
}

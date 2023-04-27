package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.model.binding.PictureBindingModel;
import com.softuni.Pathfinder.model.service.PictureServiceModel;
import com.softuni.Pathfinder.service.PictureService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class PictureController {

    private final PictureService pictureService;
    private final ModelMapper modelMapper;

    public PictureController(PictureService pictureService, ModelMapper modelMapper) {
        this.pictureService = pictureService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/picture/upload")
    public String uploadPicture(@Valid PictureBindingModel pictureBindingModel,
                                BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors() || pictureBindingModel.getPicture().isEmpty()) {
            return "redirect:/routes/" + pictureBindingModel.getRouteId() + "/details";
        }

        pictureService.uploadPicture(modelMapper.map(pictureBindingModel, PictureServiceModel.class));

        return "redirect:/routes/" + pictureBindingModel.getRouteId() + "/details";
    }
}

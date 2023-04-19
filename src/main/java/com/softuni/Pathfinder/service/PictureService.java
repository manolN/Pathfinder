package com.softuni.Pathfinder.service;

import com.softuni.Pathfinder.model.service.PictureServiceModel;

import java.io.IOException;

public interface PictureService {

    void uploadPicture(PictureServiceModel pictureServiceModel) throws IOException;
}

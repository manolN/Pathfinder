package com.softuni.Pathfinder.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    CloudinaryImage uploadImage(MultipartFile file) throws IOException;
}

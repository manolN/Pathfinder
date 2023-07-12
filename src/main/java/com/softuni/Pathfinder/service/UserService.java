package com.softuni.Pathfinder.service;

import com.softuni.Pathfinder.model.service.UserRegisterServiceModel;
import com.softuni.Pathfinder.model.view.UserProfileView;

public interface UserService {

    boolean register(UserRegisterServiceModel userRegisterServiceModel);

    UserProfileView getUserInfo(String username);

    boolean isPasswordsMatch(String password, String confirmPassword);

    boolean isUsernameOccupied(String username);
}

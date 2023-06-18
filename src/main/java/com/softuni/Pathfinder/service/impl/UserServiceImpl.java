package com.softuni.Pathfinder.service.impl;

import com.softuni.Pathfinder.model.entity.RoleEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.entity.enums.LevelEnum;
import com.softuni.Pathfinder.model.service.UserRegisterServiceModel;
import com.softuni.Pathfinder.model.view.UserProfileView;
import com.softuni.Pathfinder.repository.RoleRepository;
import com.softuni.Pathfinder.repository.UserRepository;
import com.softuni.Pathfinder.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(UserRegisterServiceModel serviceModel) {

        Optional<UserEntity> optionalUser = userRepository.findByUsername(serviceModel.getUsername());

        if (!optionalUser.isEmpty()) {
            return false;
        }

        UserEntity user = modelMapper.map(serviceModel, UserEntity.class);
        RoleEntity userRole = roleRepository.findById(3L).orElse(null);
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setRoles(List.of(userRole));
        user.setPassword(encodedPassword);
        user.setLevel(LevelEnum.BEGINNER);
        userRepository.save(user);

        return true;
    }

    @Override
    public UserProfileView getUserInfo(String username) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return null;
        }

        UserEntity userEntity = optionalUser.get();

        return new UserProfileView()
                .setUsername(userEntity.getUsername())
                .setFullName(userEntity.getFullName())
                .setAge(userEntity.getAge());
    }
}

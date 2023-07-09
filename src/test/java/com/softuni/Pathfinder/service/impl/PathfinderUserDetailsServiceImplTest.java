package com.softuni.Pathfinder.service.impl;

import com.softuni.Pathfinder.model.entity.RoleEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.entity.enums.RoleEnum;
import com.softuni.Pathfinder.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class PathfinderUserDetailsServiceImplTest {

    private UserEntity testUser;
    private RoleEntity adminRole, userRole;
    private PathfinderUserDetailsServiceImpl serviceToTest;
    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void init() {
        serviceToTest = new PathfinderUserDetailsServiceImpl(mockUserRepository);

        adminRole = new RoleEntity().setRole(RoleEnum.ADMIN);
        userRole = new RoleEntity().setRole(RoleEnum.USER);

        testUser = new UserEntity()
                .setUsername("manol")
                .setRoles(List.of(adminRole, userRole))
                .setPassword("1234");
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> serviceToTest.loadUserByUsername(testUser.getUsername())
        );
    }

    @Test
    void testUserFound() {
        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        UserDetails actual = serviceToTest.loadUserByUsername(testUser.getUsername());
        String actualPassword = actual.getPassword();

        String expectedPassword = "1234";

        String actualRoles = actual.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        String expectedRoles = "ROLE_ADMIN, ROLE_USER";

        Assertions.assertEquals(testUser.getUsername(), actual.getUsername());
        Assertions.assertEquals(expectedPassword, actualPassword);
        Assertions.assertEquals(expectedRoles, actualRoles);
    }
}
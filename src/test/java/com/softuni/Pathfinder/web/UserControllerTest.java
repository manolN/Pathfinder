package com.softuni.Pathfinder.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.softuni.Pathfinder.model.entity.RoleEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.entity.enums.RoleEnum;
import com.softuni.Pathfinder.repository.RoleRepository;
import com.softuni.Pathfinder.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final static String TEST_USER_USERNAME = "testUser";
    private final static String TEST_USER_FULL_NAME = "Test User";
    private final static String TEST_USER_EMAIL = "user@user.com";
    private final static String TEST_USER_AGE = "20";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    void initUser() {
        UserEntity userEntity = new UserEntity()
                .setUsername(TEST_USER_USERNAME)
                .setFullName("Ivan Ivanov")
                .setPassword("1234")
                .setAge(30)
                .setEmail("ivan@ivan.com");

        userRepository.save(userEntity);
    }

    @BeforeEach
    void initRole() {
        RoleEntity userRole = new RoleEntity().setRole(RoleEnum.USER);

        roleRepository.save(userRole);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void testOpenLoginForm() throws Exception {
        mockMvc
                .perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLoginWithIncorrectUsername() throws Exception {
        initUser();

        mockMvc
                .perform(post("/users/login")
                        .param("username", "invalidUsername")
                        .param("password", "1234"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testLoginWithIncorrectPassword() throws Exception {
        initUser();

        mockMvc
                .perform(post("/users/login")
                        .param("username", TEST_USER_USERNAME)
                        .param("password", "invalidPassword"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser("testUser")
    void testOpenProfilePage() throws Exception {
        initUser();

        mockMvc
                .perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    void testOpenRegisterForm() throws Exception {
        mockMvc
                .perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testRegisterWithEmptyForm() throws Exception {
        mockMvc
                .perform(post("/users/register")
                        .param("username", "")
                        .param("fullName", "")
                        .param("email", "")
                        .param("age", "")
                        .param("password", "")
                        .param("confirmPassword", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    void testRegisterWithDifferentPasswords() throws Exception {
        mockMvc
                .perform(post("/users/register")
                        .param("username", TEST_USER_USERNAME)
                        .param("fullName", TEST_USER_FULL_NAME)
                        .param("email", TEST_USER_EMAIL)
                        .param("age", TEST_USER_AGE)
                        .param("password", "1234")
                        .param("confirmPassword", "12345")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    void testRegisterWithExistingUsername() throws Exception {
        initUser();

        mockMvc
                .perform(post("/users/register")
                        .param("username", TEST_USER_USERNAME)
                        .param("fullName", TEST_USER_FULL_NAME)
                        .param("email", TEST_USER_EMAIL)
                        .param("age", TEST_USER_AGE)
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(1, userRepository.count());

        Optional<UserEntity> optionalUser = userRepository.findByUsername(TEST_USER_USERNAME);

        Assertions.assertTrue(optionalUser.isPresent());

        UserEntity initialUser = optionalUser.get();

        Assertions.assertEquals(TEST_USER_USERNAME, initialUser.getUsername());
        Assertions.assertEquals("Ivan Ivanov", initialUser.getFullName());
        Assertions.assertEquals("ivan@ivan.com", initialUser.getEmail());
        Assertions.assertEquals("30", String.valueOf(initialUser.getAge()));
    }

    @Test
    void testRegisterUser() throws Exception {
        mockMvc
                .perform(post("/users/register")
                        .param("username", TEST_USER_USERNAME)
                        .param("fullName", TEST_USER_FULL_NAME)
                        .param("email", TEST_USER_EMAIL)
                        .param("age", TEST_USER_AGE)
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(1, userRepository.count());

        Optional<UserEntity> optionalUser = userRepository.findByUsername(TEST_USER_USERNAME);

        Assertions.assertTrue(optionalUser.isPresent());

        UserEntity newlyCreatedUser = optionalUser.get();

        Assertions.assertEquals(TEST_USER_USERNAME, newlyCreatedUser.getUsername());
        Assertions.assertEquals(TEST_USER_FULL_NAME, newlyCreatedUser.getFullName());
        Assertions.assertEquals(TEST_USER_EMAIL, newlyCreatedUser.getEmail());
        Assertions.assertEquals(TEST_USER_AGE, String.valueOf(newlyCreatedUser.getAge()));
    }
}
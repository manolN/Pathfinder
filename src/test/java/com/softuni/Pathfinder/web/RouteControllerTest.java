package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.model.entity.CategoryEntity;
import com.softuni.Pathfinder.model.entity.RouteEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.model.entity.enums.CategoryEnum;
import com.softuni.Pathfinder.model.entity.enums.LevelEnum;
import com.softuni.Pathfinder.repository.CategoryRepository;
import com.softuni.Pathfinder.repository.RouteRepository;
import com.softuni.Pathfinder.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser("manol")
@SpringBootTest
@AutoConfigureMockMvc
class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity()
                .setUsername("manol")
                .setFullName("Manol Naydenov")
                .setPassword("1234")
                .setEmail("manol@manol.com");

        userRepository.save(testUser);

        CategoryEntity pedestrianCategory = new CategoryEntity().setName(CategoryEnum.PEDESTRIAN).setDescription("pedestrian");
        CategoryEntity carCategory = new CategoryEntity().setName(CategoryEnum.CAR).setDescription("car");
        CategoryEntity bicycleCategory = new CategoryEntity().setName(CategoryEnum.BICYCLE).setDescription("bicycle");
        CategoryEntity motorcycleCategory = new CategoryEntity().setName(CategoryEnum.MOTORCYCLE).setDescription("motorcycle");

        categoryRepository.saveAll(List.of(pedestrianCategory, carCategory, bicycleCategory, motorcycleCategory));

        RouteEntity pedestrianRoute = new RouteEntity();
        pedestrianRoute
                .setName("Pedestrian route")
                .setAuthor(testUser)
                .setLevel(LevelEnum.BEGINNER)
                .setCategories(List.of(pedestrianCategory))
                .setDescription("pedestrian route");

        RouteEntity carRoute = new RouteEntity();
        carRoute
                .setName("Car route")
                .setAuthor(testUser)
                .setLevel(LevelEnum.BEGINNER)
                .setCategories(List.of(carCategory))
                .setDescription("car route");

        RouteEntity bicycleRoute = new RouteEntity();
        bicycleRoute
                .setName("Bicycle route")
                .setAuthor(testUser)
                .setLevel(LevelEnum.BEGINNER)
                .setCategories(List.of(bicycleCategory))
                .setDescription("bicycle route");

        RouteEntity motorcycleRoute = new RouteEntity();
        motorcycleRoute
                .setName("Motorcycle route")
                .setAuthor(testUser)
                .setLevel(LevelEnum.BEGINNER)
                .setCategories(List.of(motorcycleCategory))
                .setDescription("motorcycle route");

        routeRepository.saveAll(List.of(pedestrianRoute, carRoute, bicycleRoute, motorcycleRoute));
    }

    @AfterEach
    void tearDown() {
        routeRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testOpenAllRoutesPage() throws Exception {
        mockMvc
                .perform(get("/routes"))
                .andExpect(status().isOk())
                .andExpect(view().name("routes"))
                .andExpect(model().attributeExists("routes"));
    }

    @Test
    void testOpenRouteDetailsPage() throws Exception {
        Long routeId = routeRepository.getByName("Car route").get().getId();
        mockMvc
                .perform(get("/routes/" + routeId + "/details"))
                .andExpect(status().isOk())
                .andExpect(view().name("route-details"))
                .andExpect(model().attributeExists("route"));
    }

    @Test
    void testOpenRouteAddPage() throws Exception {
        mockMvc
                .perform(get("/routes/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-route"))
                .andExpect(model().attributeExists("levels"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @Transactional
    void testCreateRoute() throws Exception {
        routeRepository.deleteAll();
        Assertions.assertEquals(0, routeRepository.count());

        mockMvc
                .perform(post("/routes/add")
                        .param("name", "Test route")
                        .param("description", "test route")
                        .param("level", LevelEnum.BEGINNER.toString())
                        .param("categories", CategoryEnum.PEDESTRIAN.toString())
                        .param("videoUrl", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(1, routeRepository.count());

        RouteEntity testRoute = routeRepository.getByName("Test route").get();

        Assertions.assertEquals("Test route", testRoute.getName());
        Assertions.assertEquals("test route", testRoute.getDescription());
        Assertions.assertEquals(LevelEnum.BEGINNER, testRoute.getLevel());
        Assertions.assertEquals("PEDESTRIAN", testRoute.getCategories().get(0).getName().name());
    }

    @Test
    @Transactional
    void testCreateRouteWithExistingName() throws Exception {
        Assertions.assertEquals(4, routeRepository.count());

        mockMvc
                .perform(post("/routes/add")
                        .param("name", "Car route")
                        .param("description", "invalid route")
                        .param("level", LevelEnum.BEGINNER.toString())
                        .param("categories", CategoryEnum.CAR.toString())
                        .param("videoUrl", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(4, routeRepository.count());
        Assertions.assertEquals("car route", routeRepository.getByName("Car route").get().getDescription());
    }

    @Test
    @Transactional
    void testCreateRouteWithInvalidName() throws Exception {
        Assertions.assertEquals(4, routeRepository.count());

        mockMvc
                .perform(post("/routes/add")
                        .param("name", "Car")
                        .param("description", "invalid route")
                        .param("level", LevelEnum.BEGINNER.toString())
                        .param("categories", CategoryEnum.CAR.toString())
                        .param("videoUrl", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(4, routeRepository.count());
    }

    @Test
    @Transactional
    void testCreateRouteWithInvalidDescription() throws Exception {
        Assertions.assertEquals(4, routeRepository.count());

        mockMvc
                .perform(post("/routes/add")
                        .param("name", "Test route")
                        .param("description", "inv")
                        .param("level", LevelEnum.BEGINNER.toString())
                        .param("categories", CategoryEnum.CAR.toString())
                        .param("videoUrl", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(4, routeRepository.count());
    }

    @Test
    @Transactional
    void testCreateRouteWithoutLevel() throws Exception {
        Assertions.assertEquals(4, routeRepository.count());

        mockMvc
                .perform(post("/routes/add")
                        .param("name", "Test route")
                        .param("description", "invalid route")
                        .param("level", "")
                        .param("categories", CategoryEnum.CAR.toString())
                        .param("videoUrl", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(4, routeRepository.count());
    }

    @Test
    @Transactional
    void testCreateRouteWithoutCategory() throws Exception {
        Assertions.assertEquals(4, routeRepository.count());

        mockMvc
                .perform(post("/routes/add")
                        .param("name", "Test route")
                        .param("description", "invalid route")
                        .param("level", LevelEnum.BEGINNER.toString())
                        .param("categories", "")
                        .param("videoUrl", "")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(4, routeRepository.count());
        Assertions.assertTrue(routeRepository.getByName("Test route").isEmpty());
    }

    @Test
    void testGetPedestrianRoutes() throws Exception {
        mockMvc
                .perform(get("/routes/pedestrian"))
                .andExpect(status().isOk())
                .andExpect(view().name("pedestrian"))
                .andExpect(model().attributeExists("pedestrianRoutes"))
                .andExpect(model().attribute("pedestrianRoutes", hasItem(
                        allOf(
                                hasProperty("name", is("Pedestrian route")),
                                hasProperty("description", is("pedestrian route"))
                        )
                )));
    }

    @Test
    void testGetCarRoutes() throws Exception {
        mockMvc
                .perform(get("/routes/car"))
                .andExpect(status().isOk())
                .andExpect(view().name("car"))
                .andExpect(model().attributeExists("carRoutes"))
                .andExpect(model().attribute("carRoutes", hasItem(
                        allOf(
                                hasProperty("name", is("Car route")),
                                hasProperty("description", is("car route"))
                        )
                        )));
    }

    @Test
    void testGetBicycleRoutes() throws Exception {
        mockMvc
                .perform(get("/routes/bicycle"))
                .andExpect(status().isOk())
                .andExpect(view().name("bicycle"))
                .andExpect(model().attributeExists("bicycleRoutes"))
                .andExpect(model().attribute("bicycleRoutes", hasItem(
                        allOf(
                                hasProperty("name", is("Bicycle route")),
                                hasProperty("description", is("bicycle route"))
                        )
                )));
    }

    @Test
    void testGetMotorcycleRoutes() throws Exception {
        mockMvc
                .perform(get("/routes/motorcycle"))
                .andExpect(status().isOk())
                .andExpect(view().name("motorcycle"))
                .andExpect(model().attributeExists("motorcycleRoutes"))
                .andExpect(model().attribute("motorcycleRoutes", hasItem(
                        allOf(
                                hasProperty("name", is("Motorcycle route")),
                                hasProperty("description", is("motorcycle route"))
                        )
                )));
    }
}
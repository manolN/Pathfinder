package com.softuni.Pathfinder.web;

import com.softuni.Pathfinder.model.entity.RouteEntity;
import com.softuni.Pathfinder.repository.RouteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteRepository routeRepository;

    @AfterEach
    void tearDown() {
        routeRepository.deleteAll();
    }

    RouteEntity initRoute() {
        RouteEntity testRoute = new RouteEntity();
        testRoute
                .setName("Test route")
                .setDescription("test route");

        return routeRepository.save(testRoute);
    }

    @Test
    void testOpenIndexPage() throws Exception {
        initRoute();

        mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testOpenAboutPage() throws Exception {
        mockMvc
                .perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(view().name("about"));
    }
}
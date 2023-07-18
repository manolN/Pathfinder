package com.softuni.Pathfinder.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softuni.Pathfinder.model.binding.CommentBindingModel;
import com.softuni.Pathfinder.model.entity.CommentEntity;
import com.softuni.Pathfinder.model.entity.RouteEntity;
import com.softuni.Pathfinder.model.entity.UserEntity;
import com.softuni.Pathfinder.repository.CommentRepository;
import com.softuni.Pathfinder.repository.RouteRepository;
import com.softuni.Pathfinder.repository.UserRepository;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

@WithMockUser("manol")
@SpringBootTest
@AutoConfigureMockMvc
class CommentRestControllerTest {

    private static final String COMMENT_1 = "first comment";
    private static final String COMMENT_2 = "second comment ";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity()
                .setUsername("manol")
                .setFullName("Manol Naydenov")
                .setPassword("1234")
                .setEmail("manol@manol.com");

        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAll();
        routeRepository.deleteAll();
        userRepository.deleteAll();
    }

    RouteEntity initRoute() {
        RouteEntity testRoute = new RouteEntity();
        testRoute
                .setName("Test route")
                .setDescription("test route");

        return routeRepository.save(testRoute);
    }

    void initComments(RouteEntity testRoute) {
        CommentEntity testComment1 = new CommentEntity();
        testComment1
                .setCreated(LocalDateTime.now())
                .setAuthor(testUser)
                .setTextContent(COMMENT_1)
                .setApproved(true)
                .setRoute(testRoute);

        CommentEntity testComment2 = new CommentEntity();
        testComment2
                .setCreated(LocalDateTime.now())
                .setAuthor(testUser)
                .setTextContent(COMMENT_2)
                .setApproved(true)
                .setRoute(testRoute);

        commentRepository.saveAll(List.of(testComment1, testComment2));

        testRoute.setComments(List.of(testComment1, testComment2));

        routeRepository.save(testRoute);
    }

    @Test
    void testGetAllComments() throws Exception {
        RouteEntity testRoute = initRoute();
        initComments(testRoute);

        mockMvc
                .perform(get("/api/" + testRoute.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].message", is(COMMENT_1)))
                .andExpect(jsonPath("$.[1].message", is(COMMENT_2)));
    }

    @Test
    void testAddComment() throws Exception {
        RouteEntity emptyRoute = initRoute();

        CommentBindingModel testComment = new CommentBindingModel();
        testComment
                .setRouteId(emptyRoute.getId())
                .setMessage(COMMENT_1);

        Assertions.assertEquals(0, commentRepository.count());

        mockMvc
                .perform(post("/api/" + emptyRoute.getId() + "/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testComment))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", MatchesPattern.matchesPattern("/api/" + emptyRoute.getId() + "/comments/\\d")))
                .andExpect(jsonPath("$.message").value(is(COMMENT_1)));

        Assertions.assertEquals(1, commentRepository.count());

        Assertions.assertEquals(COMMENT_1, commentRepository.findAll().get(0).getTextContent());
    }

    @Test
    void testDeleteComment() throws Exception {
        RouteEntity testRoute = initRoute();
        initComments(testRoute);

        Assertions.assertEquals(2, commentRepository.count());

        List<CommentEntity> allComments = commentRepository.findAll();
        Long firstCommentId = allComments.get(0).getId();
        Long secondCommentId = allComments.get(1).getId();

        mockMvc
                .perform(delete("/api/" + testRoute.getId() + "/comments/" + firstCommentId + "/delete")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertEquals(1, commentRepository.count());

        Assertions.assertEquals(COMMENT_2, commentRepository.findById(secondCommentId).get().getTextContent());
    }

    @Test
    void testEditComment() throws Exception {
        RouteEntity testRoute = initRoute();
        initComments(testRoute);

        CommentBindingModel editedComment = new CommentBindingModel();
        editedComment
                .setRouteId(testRoute.getId())
                .setMessage("edited comment");

        Long editedCommentId = commentRepository.findAll().get(0).getId();

        Assertions.assertEquals(COMMENT_1, commentRepository.findById(editedCommentId).get().getTextContent());

        mockMvc
                .perform(patch("/api/" + testRoute.getId() + "/comments/" + editedCommentId + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editedComment))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", MatchesPattern.matchesPattern("/api/" + testRoute.getId() + "/comments/\\d")))
                .andExpect(jsonPath("$.message").value("edited comment"));

        Assertions.assertEquals("edited comment", commentRepository.findById(editedCommentId).get().getTextContent());
    }

    @Test
    void testCreateCommentWithInvalidMessage() throws Exception {
        RouteEntity emptyRoute = initRoute();

        CommentBindingModel testComment = new CommentBindingModel();
        testComment
                .setRouteId(emptyRoute.getId())
                .setMessage("inv");

        mockMvc
                .perform(post("/api/" + emptyRoute.getId() + "/comments/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testComment))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldWithErrors[0]").value(is("message")));

        Assertions.assertEquals(0, commentRepository.count());
    }

    @Test
    void testEditCommentWithInvalidMessage() throws Exception {
        RouteEntity testRoute = initRoute();
        initComments(testRoute);

        CommentBindingModel editedComment = new CommentBindingModel();
        editedComment
                .setRouteId(testRoute.getId())
                .setMessage("inv");

        List<CommentEntity> allComments = commentRepository.findAll();
        Long firstCommentId = allComments.get(0).getId();

        Assertions.assertEquals(COMMENT_1, commentRepository.findById(firstCommentId).get().getTextContent());

        mockMvc
                .perform(patch("/api/" + testRoute.getId() + "/comments/" + firstCommentId + "/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editedComment))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldWithErrors[0]").value("message"));

        Assertions.assertEquals(COMMENT_1, commentRepository.findById(firstCommentId).get().getTextContent());
    }
}
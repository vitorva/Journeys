/**
 * @team Journeys
 * @file UserControllerTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.journeys.main.model.Coordinates;
import com.journeys.main.model.Journey;
import com.journeys.main.model.PointOfInterest;
import com.journeys.main.model.User;
import com.journeys.main.relationships.Experience;
import com.journeys.main.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class , SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTests {
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private static List<User> mockUser;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation){
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * Initializing the mock data with dummy User
     */
    @BeforeAll
    public static void UserControllerTests_init() {
        User jean = new User("jdupont", "Jean", "Dupont", "passw0rd", "jdupont@dupont.ch", new HashSet<>(),new HashSet<>());
        User jeanne = new User("jeanoskur", "Jeanne", "D'arc", "oskour", "jdarc@gmail.com", new HashSet<>(), new HashSet<>());
        User john = new User("jdoe", "John", "Doe", "jdoe4lif", "john.doe@gmail.com", new HashSet<>(), new HashSet<>());

        Set<Experience> experienceSet = new HashSet<>();
        experienceSet.add(
                new Experience(1L, 1, "Can our head explose if put in the jet d'eau ?", new ArrayList<>(),
                        new PointOfInterest("1", "Jet d'eau de Genève", "/1_jet_d_eau.png",
                                new Coordinates(2L, 46.3424, 6.46235)),null));

        jean.getJourneys().add(
                new Journey("1", "Trip around the lac de Genf",
                        new Coordinates(1L, 45.4353, 6.345),
                        new Coordinates(2L, 46.2342, 6.453),
                        experienceSet));

        mockUser = new ArrayList<>(Arrays.asList(jean, jeanne, john));
    }

    /**
     * Testing fetching every user
     * @throws Exception
     */
    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void getUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(mockUser);

        mvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", containsString("Jean")))
                .andExpect(jsonPath("$[0].lastName", containsString("Dupont")))
                .andExpect(jsonPath("$[0].email", containsString("jdupont@dupont.ch")))
                .andExpect(jsonPath("$[0].userName", containsString("jdupont")))
                .andExpect(jsonPath("$[1].firstName", containsString("Jeanne")))
                .andExpect(jsonPath("$[1].lastName", containsString("D'arc")))
                .andExpect(jsonPath("$[1].email", containsString("jdarc@gmail.com")))
                .andExpect(jsonPath("$[1].userName", containsString("jeanoskur")))
                .andExpect(jsonPath("$[2].firstName", containsString("John")))
                .andExpect(jsonPath("$[2].lastName", containsString("Doe")))
                .andExpect(jsonPath("$[2].email", containsString("john.doe@gmail.com")))
                .andExpect(jsonPath("$[2].userName", containsString("jdoe")))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[].firstName").description("first name of the user"),
                                        fieldWithPath("[].lastName").description("last name of the user"),
                                        fieldWithPath("[].email").description("email of the user"),
                                        fieldWithPath("[].userName").description("user name of the user")
                                )
                        )
                );
    }

    /**
     * Testing fetching a user and his journeys
     * @throws Exception
     */
    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void getUserJourneys() throws Exception {
        Mockito.when(userService.getUser("jdupont")).thenReturn(mockUser.get(0));

        mvc.perform(RestDocumentationRequestBuilders.get("/api/user/{username}/journeys","jdupont"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", containsString("jdupont")))
                .andExpect(jsonPath("$.journeys[0].title", containsString("Trip around the lac de Genf")))
                .andExpect(jsonPath("$.journeys[0].id", containsString("1")))
                .andExpect(jsonPath("$.journeys[0].experiences[0].description", containsString("Can our head explose if put in the jet d'eau ?")))
                .andExpect(jsonPath("$.journeys[0].experiences[0].images", is(empty())))
                .andExpect(jsonPath("$.journeys[0].experiences[0].pointOfInterest.id", containsString("1")))
                .andDo(
                        document(
                                "{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("username").description("User  from whom we get the journeys")),
                                responseFields(
                                        fieldWithPath("userName").description("User  from whom we get the journeys"),
                                        subsectionWithPath("journeys[]").description("A list of journeys from the user")
                                )
                        )
                );
    }

    /**
     * Testing fetching a user
     * @throws Exception
     */
    @Test
    @WithMockUser(username="admin",roles={"USER","ADMIN"})
    public void getUser() throws Exception {
        Mockito.when(userService.getUserDetails("jdupont")).thenReturn(mockUser.get(0).getAsDetails());

        mvc.perform(MockMvcRequestBuilders.get("/api/user/jdupont"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", containsString("Jean")))
                .andExpect(jsonPath("$.lastName", containsString("Dupont")))
                .andExpect(jsonPath("$.email", containsString("jdupont@dupont.ch")))
                .andExpect(jsonPath("$.userName", containsString("jdupont")))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("firstName").description("first name of the user"),
                                        fieldWithPath("lastName").description("last name of the user"),
                                        fieldWithPath("email").description("email of the user"),
                                        fieldWithPath("userName").description("user name of the user")
                                )
                        )
                );
    }
}
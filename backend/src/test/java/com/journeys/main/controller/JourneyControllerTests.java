/**
 * @team Journeys
 * @file JourneyControllerTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.journeys.main.dto.JourneyCreateRequest;
import com.journeys.main.dto.JourneyUpdateRequest;
import com.journeys.main.exceptions.experience.ExperienceNotFound;
import com.journeys.main.exceptions.handlers.JourneyExceptionHandler;
import com.journeys.main.exceptions.journey.JourneyDeleteException;
import com.journeys.main.exceptions.journey.JourneyNotFoundException;
import com.journeys.main.exceptions.journey.JourneySaveException;
import com.journeys.main.dto.APIResponse;
import com.journeys.main.exceptions.user.UserNotFoundException;
import com.journeys.main.model.Coordinates;
import com.journeys.main.model.Journey;
import com.journeys.main.model.PointOfInterest;
import com.journeys.main.relationships.Experience;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class , SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
public class JourneyControllerTests extends BaseControllerTest{

    @MockBean
    private JourneyController journeyController;

    private static List<Journey> mockJourney;
    private final JourneyExceptionHandler handler = new JourneyExceptionHandler();
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation){
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * Initializing the mock data with dummy Journey
     */
    @BeforeAll
    public static void JourneyControllerTests_init() {
        Journey journey1 = new Journey(
                "1",
                "Trip to wonderland",
                new Coordinates(1L, 46.34533, 6.3455),
                new Coordinates(3L, 46.5555, 6.8678),
                new HashSet<>(List.of(
                        new Experience(
                                2L,
                                1,
                                "This is a beer",
                                new ArrayList<>(Arrays.asList("temp.jpg", "temp2.jpg")),
                                new PointOfInterest(
                                        "1",
                                        "Chillout",
                                        "dummy_image.png",
                                        new Coordinates(4L, 23.2424, 5.3545)),
                                LocalDateTime.now()))));
        Journey journey2 = new Journey(
                "1",
                "Going to the dark side",
                new Coordinates(1L, 46.34533, 6.3455),
                new Coordinates(3L, 46.5555, 6.8678),
                new HashSet<>(List.of(
                        new Experience(
                                2L,
                                1,
                                "What is this cheap version of the jet d'eau in Zürich ?",
                                new ArrayList<>(Arrays.asList("what.jpg", "is_this.jpg")),
                                new PointOfInterest(
                                        "1",
                                        "Jet d'eau of Zürich",
                                        "fake_water_zurich.png",
                                        new Coordinates()),
                                LocalDateTime.now()))));
        mockJourney = new ArrayList<>(Arrays.asList(journey1, journey2));
    }

    /**
     * Testing fetching a journey with all experiences associated
     * @throws Exception
     */
    @Test
    public void getJourney() throws Exception {
        Mockito.when(journeyController.getJourney(mockJourney.get(0).getId())).thenReturn(mockJourney.get(0));
        mvc.perform(RestDocumentationRequestBuilders.get("/api/journey/{journey}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", containsString("1")))
                .andExpect(jsonPath("$.title", containsString("Trip to wonderland")))
                .andExpect(jsonPath("$.start.lng", is(46.34533)))
                .andExpect(jsonPath("$.start.lat", is(6.3455)))
                .andExpect(jsonPath("$.end.lng", is(46.5555)))
                .andExpect(jsonPath("$.end.lat", is(6.8678)))
                .andExpect(jsonPath("$.experiences[0].id", is(2)))
                .andExpect(jsonPath("$.experiences[0].order", is(1)))
                .andExpect(jsonPath("$.experiences[0].description", containsString("This is a beer")))
                .andExpect(jsonPath("$.experiences[0].images[0]", containsString("temp.jpg")))
                .andExpect(jsonPath("$.experiences[0].images[1]", containsString("temp2.jpg")))
                .andExpect(jsonPath("$.experiences[0].pointOfInterest.id", containsString("1")))
                .andExpect(jsonPath("$.experiences[0].pointOfInterest.name", containsString("Chillout")))
                .andExpect(jsonPath("$.experiences[0].pointOfInterest.url", containsString("dummy_image.png")))
                .andExpect(jsonPath("$.experiences[0].pointOfInterest.coordinates.lng", is(23.2424)))
                .andExpect(jsonPath("$.experiences[0].pointOfInterest.coordinates.lat", is(5.3545)))
                .andDo(document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("journey").description("id of the journey to search")),
                                responseFields(
                                        fieldWithPath("id").description("unique identifier of the journey"),
                                        fieldWithPath("title").description("title of the journey"),
                                        subsectionWithPath("start").description("Starting point of the journey"),
                                        subsectionWithPath("end").description("ending point of the journey"),
                                        subsectionWithPath("experiences.[]").description("A list of experiences belonging to the journey"),
                                        subsectionWithPath("asExperiences").description("A simplified representation of a journey containing only a list of experiences "))
                        )
                );
    }

    /**
     * Testing fetching experiences from the journey
     * @throws Exception
     */
    @Test
    public void getJourneyExperiences() throws Exception {
        Mockito.when(journeyController.getExperiences(
                mockJourney.get(0).getId())).thenReturn(List.of(mockJourney.get(0).getAsExperiences()));

        mvc.perform(RestDocumentationRequestBuilders.get("/api/journey/{id}/experience",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", containsString("1")))
                .andExpect(jsonPath("$[0].title", containsString("Trip to wonderland")))
                .andExpect(jsonPath("$[0].experiences[0].description", containsString("This is a beer")))
                .andExpect(jsonPath("$[0].experiences[0].pointOfInterest.id", containsString("1")))
                .andExpect(jsonPath("$[0].experiences[0].images[0]", containsString("temp.jpg")))
                .andExpect(jsonPath("$[0].experiences[0].images[1]", containsString("temp2.jpg")))
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("id").description("identifier of the journey")),
                        responseFields(
                                fieldWithPath("[].id").description("unique identifier of the journey"),
                                fieldWithPath("[].title").description("title of the journey"),
                                subsectionWithPath("[].experiences.[]").description("List of experiences")))
                );
    }

    /**
     * Testing saving a journey
     * @throws Exception
     */
    @Test
    public void JourneySave() throws Exception {
        JourneyCreateRequest req = new JourneyCreateRequest(
                "My super journey",
                Arrays.stream(new String[]{"Lausanne","Vevey","Montreux"}).collect(Collectors.toList()),
                new Coordinates(0L,46.9627427,	6.8209242),
                new Coordinates(1L,46.4910092,	6.7446857)
        );
        Mockito.when(journeyController.saveJourney(Mockito.any())).thenReturn(new APIResponse("My super journey"));
        mvc.perform(RestDocumentationRequestBuilders.post("/api/journey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("Title of the journey"),
                                fieldWithPath("orderedPOIsArray").description("List of POI's in order f the journey"),
                                fieldWithPath("start").description("start of the journey"),
                                fieldWithPath("start.lng").ignored(),
                                fieldWithPath("start.lat").ignored(),
                                fieldWithPath("end").description("end of the journey"),
                                fieldWithPath("end.lng").ignored(),
                                fieldWithPath("end.lat").ignored()
                        ),
                        responseFields(
                                fieldWithPath("responseMsg").description("Response message")
                        )
                ));
    }

    /**
     * Testing throwing exception during save of journey
     * @throws Exception
     */
    @Test
    public void JourneySaveException() throws Exception {

        JourneyCreateRequest req = new JourneyCreateRequest();
        Mockito.when(journeyController.saveJourney(Mockito.any())).thenThrow(new JourneySaveException("Could not save"));


        mvc.perform(RestDocumentationRequestBuilders.post("/api/journey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(doDocumentException("{method-name}"));
    }

    /**
     * Testing update of experience that is part of a journey
     * @throws Exception
     */
    @Test
    public void updateExperienceFromJourney() throws Exception {
        JourneyUpdateRequest req = new JourneyUpdateRequest();

        Mockito.when(journeyController.updateExperienceFromJourney(Mockito.any())).thenReturn(new APIResponse("Updated experience from journey"));

        mvc.perform(RestDocumentationRequestBuilders.put("/api/journey/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("method-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("responseMsg").description("Success update message")
                        ))
                );
    }

    /**
     * Testing throwing exception while updating an experience from a journey
     * @throws Exception
     */
    @Test
    public void UpdateExperienceJourneyException() throws Exception{
        JourneyUpdateRequest req = new JourneyUpdateRequest();

        Mockito.when(journeyController.updateExperienceFromJourney(Mockito.any())).thenThrow(new ExperienceNotFound("Experience does not exist"));
        mvc.perform(RestDocumentationRequestBuilders.put("/api/journey/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(doDocumentException("{method-name}"));
    }

    /**
     * Testing updating a single journey (not part of a journey)
     * @throws Exception
     */
    @Test
    public void updateSingleExperience() throws Exception {
        JourneyUpdateRequest req = new JourneyUpdateRequest();

        Mockito.when(journeyController.updateSingleExperience(Mockito.any())).thenReturn(new APIResponse("Updated single experience"));

        mvc.perform(RestDocumentationRequestBuilders.put("/api/journey/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("method-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("responseMsg").description("Success single update message")
                        ))
                );
    }


    /**
     * Testing throwing exception while updating a single journey
     * @throws Exception
     */
    @Test
    public void updateSingleExperienceException() throws Exception {
        JourneyUpdateRequest req = new JourneyUpdateRequest();
        Mockito.when(journeyController.updateSingleExperience(Mockito.any())).thenThrow(new ExperienceNotFound("Experience does not exist"));
        mvc.perform(RestDocumentationRequestBuilders.put("/api/journey/user/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req).getBytes(StandardCharsets.UTF_8))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(doDocumentException("{method-name}"));
    }

    /**
     * Testing deleting a journey
     * @throws Exception
     */
    @Test
    public void DeleteJourney() throws Exception{
        Mockito.when(journeyController.deleteJourney("1")).thenReturn(new APIResponse("deleted Journey"));

        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/{id}","1"))
                .andExpect(status().isOk())
                .andDo(document("method-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("responseMsg").description("Success delete message")
                        ))
                );
    }

    /**
     * Testing throwing exception while deleting a journey
     * @throws Exception
     */
    @Test
    public void DeleteJourneyException() throws Exception{
        Mockito.when(journeyController.deleteJourney(Mockito.any())).thenThrow(new JourneyDeleteException("Could not delete"));
        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/{id}","1"))
                .andExpect(status().isInternalServerError())
                .andDo(doDocumentException("{method-name}"));
    }

    /**
     * Testing throwing exception journey not found
     * @throws Exception
     */
    @Test
    public void JourneyNotFoundException() throws Exception{
        Mockito.when(journeyController.deleteJourney(Mockito.any())).thenThrow(new JourneyNotFoundException("Could not find"));
        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/{id}","1"))
                .andExpect(status().isNotFound())
                .andDo(doDocumentException("{method-name}"));
    }

    /**
     * Testing deleting an experience from a journey
     * @throws Exception
     */
    @Test
    public void deleteExperienceFromJourney() throws Exception {
        Mockito.when(journeyController.deleteExperienceFromJourney("1", "1")).thenReturn(new APIResponse("Delete experience from journey"));

        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/{journey_id}/{poi_id}","1", "1"))
                .andExpect(status().isOk())
                .andDo(document("method-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("responseMsg").description("Success single delete message")
                        ))
                );
    }

    /**
     * Testing throwing exception while deleting an experience from a journey
     * @throws Exception
     */
    @Test
    public void DeleteExperienceJourneyException() throws Exception{
        Mockito.when(journeyController.deleteExperienceFromJourney("1", "5")).thenThrow(new JourneyNotFoundException("Could not find the experience to delete"));
        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/{journey_id}/{poi_id}","1", "5"))
                .andExpect(status().isNotFound())
                .andDo(doDocumentException("{method-name}"));
    }

    /**
     * Testing deleting a single experience
     * @throws Exception
     */
    @Test
    public void deleteSingleExperience() throws Exception {
        Mockito.when(journeyController.deleteSingleExperience("1")).thenReturn(new APIResponse("Deleted single experience"));

        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/user/{id}","1"))
                .andExpect(status().isOk())
                .andDo(document("method-name",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("responseMsg").description("Success single delete message")
                        ))
                );
    }

    /**
     * Testing throwing while deleting a single experience (user not found)
     * @throws Exception
     */
    @Test
    public void DeleteExperienceUserException() throws Exception{
        Mockito.when(journeyController.deleteSingleExperience(Mockito.any())).thenThrow(new UserNotFoundException("Couldn't find the user"));
        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/user/{id}","4"))
                .andExpect(status().isNotFound())
                .andDo(doDocumentException("{method-name}"));
    }

    /**
     * Testing throwing exception while deleting a single experience (experience not found)
     * @throws Exception
     */
    @Test
    public void DeleteExperienceException() throws Exception{
        Mockito.when(journeyController.deleteSingleExperience(Mockito.any())).thenThrow(new JourneyNotFoundException("Could not find the experience to delete"));
        mvc.perform(RestDocumentationRequestBuilders.delete("/api/journey/user/{id}","4"))
                .andExpect(status().isNotFound())
                .andDo(doDocumentException("{method-name}"));
    }
}
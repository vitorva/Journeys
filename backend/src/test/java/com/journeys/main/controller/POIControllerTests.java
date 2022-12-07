/**
 * @team Journeys
 * @file POIControllerTests.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.journeys.main.model.Coordinates;
import com.journeys.main.model.PointOfInterest;
import com.journeys.main.dto.POIFindBetween;
import com.journeys.main.service.POIService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class , SpringExtension.class})
@AutoConfigureMockMvc
@SpringBootTest
public class POIControllerTests {

    private MockMvc mvc;

    @MockBean
    private POIService poiService;

    private static List<PointOfInterest> mockPoi;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation){
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    /**
     * Initializing the mock data with dummy POI
     */
    @BeforeAll
    public static void POIControllerTests_init() {
        PointOfInterest alimentarium = new PointOfInterest(
                "1",
                "Alimentarium de Vevey",
                "alimentarium.png",
                new Coordinates(1L, 46.458222, 6.846436));
        PointOfInterest cathedrale = new PointOfInterest(
                "2",
                "Cathedrale de Lausanne",
                null,
                new Coordinates(2L, 46.522772, 6.635414));
        PointOfInterest museum = new PointOfInterest(
                "3",
                "Musee suisse du jeu",
                "musee_suisse_jeu.png",
                new Coordinates(3L, 46.452651, 6.854623));

        mockPoi = new ArrayList<>(Arrays.asList(alimentarium, cathedrale, museum));
    }

    /**
     * Testing getting every point of interests
     * @throws Exception
     */
    @Test
    public void getPointOfInterests() throws Exception {
        Mockito.when(poiService.getPOIs()).thenReturn(mockPoi);

        mvc.perform(RestDocumentationRequestBuilders.get("/api/poi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", containsString("Alimentarium de Vevey")))
                .andExpect(jsonPath("$[0].url", containsString("alimentarium.png")))
                .andExpect(jsonPath("$[0].coordinates.lat", is(mockPoi.get(0).getCoordinates().getLat())))
                .andExpect(jsonPath("$[0].coordinates.lng", is(mockPoi.get(0).getCoordinates().getLng())))
                .andExpect(jsonPath("$[1].name", containsString("Cathedrale de Lausanne")))
                .andExpect(jsonPath("$[1].coordinates.lat", is(mockPoi.get(1).getCoordinates().getLat())))
                .andExpect(jsonPath("$[1].coordinates.lng", is(mockPoi.get(1).getCoordinates().getLng())))
                .andExpect(jsonPath("$[1].url").value(IsNull.nullValue()))
                .andExpect(jsonPath("$[2].name", containsString("Musee suisse du jeu")))
                .andExpect(jsonPath("$[2].coordinates.lat", is(mockPoi.get(2).getCoordinates().getLat())))
                .andExpect(jsonPath("$[2].coordinates.lng", is(mockPoi.get(2).getCoordinates().getLng())))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("[].id").description("id of the point of interest"),
                                        fieldWithPath("[].name").description("name of the point of interest"),
                                        fieldWithPath("[].url").optional().description("Image of a point of interest"),
                                        subsectionWithPath("[].coordinates").description("Localisation of the point of interest"),
                                        subsectionWithPath("[].simplified").ignored()
                                )));
    }

    /**
     * Testing getting a single point of interest
     * @throws Exception
     */
    @Test
    public void getSinglePOI() throws Exception {
        Mockito.when(poiService.getPOI("1")).thenReturn(mockPoi.get(0));

        mvc.perform(RestDocumentationRequestBuilders.get("/api/poi/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", containsString("Alimentarium de Vevey")))
                .andExpect(jsonPath("url", containsString("alimentarium.png")))
                .andExpect(jsonPath("coordinates.lat", is(mockPoi.get(0).getCoordinates().getLat())))
                .andExpect(jsonPath("coordinates.lng", is(mockPoi.get(0).getCoordinates().getLng())))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("Id of the point of interest")),
                                responseFields(
                                        fieldWithPath("id").description("id of the point of interest"),
                                        fieldWithPath("name").description("name of the point of interest"),
                                        fieldWithPath("url").optional().description("Image of a point of interest"),
                                        subsectionWithPath("coordinates").description("Localisation of the point of interest"),
                                        subsectionWithPath("simplified").description("Simplified version returning only the Identifier")
                                )));
    }

    /**
     * Testing fetching point of interest between two coordinates
     * @throws Exception
     */
    @Test
    public void GetPOIBetween() throws Exception {
        Mockito.when(poiService.getNearby(ArgumentMatchers.any(POIFindBetween.class))).thenReturn(Arrays.asList(mockPoi.get(0), mockPoi.get(2)));

        //myjourneys.ch/api/poi/between?start=46.538979,6.581460&dest=46.529018,6.566070&radius=10
        mvc.perform(MockMvcRequestBuilders.get("/api/poi/map?startLat=46.538979&startLng=6.581460&destLat=46.529018&destLng=6.566070&radius=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", containsString("1")))
                .andExpect(jsonPath("$[0].coordinates.lng", is(46.458222)))
                .andExpect(jsonPath("$[0].coordinates.lat", is(6.846436)))
                .andExpect(jsonPath("$[1].id", containsString("3")))
                .andExpect(jsonPath("$[1].coordinates.lng", is(46.452651)))
                .andExpect(jsonPath("$[1].coordinates.lat", is(6.854623)))
                .andDo(
                        document("{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("startLat").description("Start latitude point of the journey"),
                                        parameterWithName("startLng").description("Start longitude point of the journey"),
                                        parameterWithName("destLat").description("destination latitude point of the journey"),
                                        parameterWithName("destLng").description("destination longitude point of the journey"),

                                        parameterWithName("radius").description("radius to look into")
                                ),
                                responseFields(
                                        fieldWithPath("[].id").description("Identifier of the  point of interest"),
                                        subsectionWithPath("[].coordinates").description("Localisation of the point of interest"),
                                        subsectionWithPath("[].journey").description("map of journeys and a default image"),
                                        subsectionWithPath("[].name").description("name of poi and ")
                                ))
                );
    }
}
/**
 * @team Journeys
 * @file JourneyController.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.journeys.main.dto.APIResponse;
import com.journeys.main.dto.JourneyCreateRequest;
import com.journeys.main.dto.JourneyUpdateRequest;
import com.journeys.main.exceptions.journey.JourneySaveException;
import com.journeys.main.model.Journey;
import com.journeys.main.model.projections.JourneyExperiences;
import com.journeys.main.service.JourneyService;
import com.journeys.main.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/journey")
public class JourneyController {

    private final JourneyService journeyService;
    private final StorageService storageService;

    /**
     * Constructor of JourneyController
     * @param journeyService
     * @param storageService
     */
    @Autowired
    public JourneyController(JourneyService journeyService, StorageService storageService) {
        this.journeyService
                = journeyService;
        this.storageService
                = storageService;
    }

    /**
     * Saves a journey to the database
     * @param journey
     * @return returns a message containing the title of the journey
     */
    @PostMapping
    public APIResponse saveJourney(@RequestBody JourneyCreateRequest journey) {
        if (!journey.IsValid()) {
            throw new JourneySaveException("The required fields are not present");
        }
        //throws Journeysave exception
        //throws userNotFound exception
        //throws POINotFound exception
        Journey saved = journeyService.saveJourney(journey);
        return new APIResponse("Saved journey " + saved.getTitle());
    }

    /**
     * Returns a Journey object
     * @param journey_id
     * @return a journey
     */
    @GetMapping("/{journey_id}")
    public Journey getJourney(@PathVariable String journey_id) {
        //throws JourneyNotFound exception
        return journeyService.getJourney(journey_id);
    }

    /**
     * Getting experiences associated to a journey
     * @param journey_id the id of the journey
     * @return the list of experience
     */
    @GetMapping("{journey_id}/experience")
    public Collection<JourneyExperiences> getExperiences(@PathVariable String journey_id) {
        //throws JourneyNotFoundException
        return journeyService.getExperiencesFromJourney(journey_id);
    }

    /**
     * Deleting an experience that is part of a Journey
     * @param journey a DTO containing everything needed for the update
     * @return the response HTTP
     */
    @PutMapping()
    public APIResponse updateExperienceFromJourney(JourneyUpdateRequest journey) {
        journeyService.updateExperienceFromJourney(journey);
        return new APIResponse("Updated experience from journey");
    }

    /**
     * Deleting a single experience (not part of the journey)
     * @param experience a DTO containing everything needed for the update
     * @return the response API
     */
    @PutMapping("/user")
    public APIResponse updateSingleExperience(JourneyUpdateRequest experience) {
        journeyService.updateSingleExperience(experience);
        return new APIResponse("Updated single experience");
    }

    /**
     * Deleting the journey and every experience associated
     * @param journey_id the id of the journeys
     * @return the response HTTP
     */
    @DeleteMapping("{journey_id}")
    public APIResponse deleteJourney(@PathVariable String journey_id) {
        //throws journey not found exception
        //throws journey delete exception
        journeyService.deleteJourney(journey_id);
        return new APIResponse("Your journey was successfully deleted ");
    }

    /**
     * Deleting an experience that is part of a Journey
     * @param journey_id the id of the journey
     * @param poi_id the id of the point of interest
     * @return the response HTTP
     */
    @DeleteMapping("/{journey_id}/{poi_id}")
    public APIResponse deleteExperienceFromJourney(@PathVariable String journey_id,
                                                   @PathVariable String poi_id) {
        journeyService.deleteExperienceFromJourney(journey_id, poi_id);
        return new APIResponse("Delete experience from journey");
    }

    /**
     * Deleting a single experience (not part of the journey)
     * @param poi_id the id of the point of interest
     * @return the response API
     */
    @DeleteMapping("/user/{poi_id}")
    public APIResponse deleteSingleExperience(@PathVariable String poi_id) {
        journeyService.deleteSingleExperience(poi_id);
        return new APIResponse("Deleted single experience");
    }

    /**
     * Get a single item
     * @param journey_id the id of the journey
     * @param filename the name of the file
     * @return a Ressource
     */
    @GetMapping(value = "{journey_id}/image/{filename}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody Resource getSingleItem(@PathVariable String journey_id, @PathVariable String filename) {
        //throws Storage exception
        String poi = filename.substring(0,filename.lastIndexOf("-"));
        System.out.println(poi);
        return storageService.loadAsRessource(journey_id,poi, filename);
    }
}
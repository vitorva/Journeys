/**
 * @team Journeys
 * @file POIController.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.journeys.main.dto.POIFindBetween;
import com.journeys.main.exceptions.journey.JourneyNotFoundException;
import com.journeys.main.model.Coordinates;
import com.journeys.main.model.PointOfInterest;
import com.journeys.main.model.projections.JourneyExperiences;
import com.journeys.main.model.projections.PointOfInterestExperiences;
import com.journeys.main.service.JourneyService;
import com.journeys.main.service.POIService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("poi")
public class POIController {

    private final POIService poiService;
    private final JourneyService journeyService;

    /**
     * Constructor of POIController
     * @param poiService
     * @param journeyService
     */
    @Autowired
    public POIController(POIService poiService, JourneyService journeyService) {
        this.poiService = poiService;
        this.journeyService = journeyService;
    }

    /**
     * Get every point of interests
     * @return a collection of POI
     */
    @GetMapping
    public Collection<PointOfInterest> getPOIs() {
        return poiService.getPOIs();
    }

    /**
     * Get a point of interest by its id
     * @param id the id of the POI
     * @return the object POI
     * @throws RuntimeException
     */
    @GetMapping("{id}")
    public PointOfInterest getPOI(@PathVariable("id") String id) throws RuntimeException {
        return poiService.getPOI(id);
    }

    /**
     * Save a point of interest to the database
     * @param poi
     * @throws RuntimeException
     */
    @PostMapping
    public void savePOI(@RequestBody PointOfInterest poi) throws RuntimeException {
        poiService.savePOI(poi);
    }

    /**
     * Getting POI between two coordinates
     * @param between a DTO object
     * @return a Collection of POI
     */
    @GetMapping(path = "/between")
    public Collection<PointOfInterest> getBetween(POIFindBetween between) {
        return poiService.getNearby(between);
    }

    /**
     * Getting POI between two coordinates for preview
     * @param between a DTO object
     * @return a Collection of PoiPreview
     */
    @GetMapping(path = "/map")
    public Collection<PoiPreviews> getBetweenForPreview(POIFindBetween between) {
        return poiService.getNearby(between).stream().map(this::preview).collect(Collectors.toList());
    }

    /**
     * Only returns experiences linked to a specific Point of interest
     *
     * @param id
     * @return
     */
    //TODO move to dedicated experience controller
    @GetMapping("{id}/experience")
    public ResponseEntity<Set<PoiExperiences>> getExperiences(@PathVariable("id") String id) {

        return ResponseEntity.ok().body(experiences(poiService.getPOI(id)));
    }

    /**
     * Method preview
     * @param p a Point of Interest
     * @return the preview of POI
     */
    PoiPreviews preview(PointOfInterest p){
        PoiPreviews prev = new PoiPreviews();
        Map<String,String>  journeys = new HashMap<>();

        for (JourneyExperiences entity : poiService.getExperiencesBelongingToJourneys(p.getId()) ) {
            Optional<String> user = journeyService.getJourneyCreator(entity.getId());
            if (user.isEmpty()) {
                throw new JourneyNotFoundException("Error Getting experience from journey");
            }
            for(PointOfInterestExperiences exp : entity.getExperiences()){
                if(exp.getImages() != null && exp.getImages().size() > 0){
                    System.out.println(exp.getImages().size());
                    Random rand = new Random();
                    journeys.put(entity.getId(),exp.getImages().get(rand.nextInt(exp.getImages().size())));
                }
            }
        }
        prev.setJourney(journeys);
        prev.setId(p.getId());
        prev.setName(p.getName());
        prev.setCoordinates(p.getCoordinates());
        return prev;
    }

    /**
     * Method experience
     * @param p a point of interest
     * @return a Set of PoiExperience
     */
    Set<PoiExperiences> experiences(PointOfInterest p){

        Set<PoiExperiences> details = new HashSet<>();
        for (JourneyExperiences entity : poiService.getExperiencesBelongingToJourneys(p.getId())) {
            Optional<String> user = journeyService.getJourneyCreator(entity.getId());
            if (user.isEmpty()) {
                throw new JourneyNotFoundException("Error Getting experience from journey");
            }
            String concrete_user = user.get();
            for (PointOfInterestExperiences exp : entity.getExperiences()) {
                details.add(new PoiExperiences(
                        exp.getPointOfInterest().getId(),
                        exp.getPointOfInterest().getName(),
                        exp.getImages(),
                        exp.getDescription(),
                        entity.getId(),
                        entity.getTitle(),
                        concrete_user,
                        exp.getDate()
                ));
            }
        }

        return details;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class PoiPreviews {
        Map<String,String> journey;
        String id;
        String name;
        Coordinates coordinates;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class PoiExperiences {
        private String poiId;
        private String name;
        private List<String> images;
        private String description;

        private String journeyId;
        private String journeyTitle;
        private String creator;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        private LocalDateTime date;
    }
}
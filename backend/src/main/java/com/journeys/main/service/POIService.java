/**
 * @team Journeys
 * @file POIService.java
 * @date January 21st, 2022
 */

package com.journeys.main.service;

import com.journeys.main.dto.POIFindBetween;
import com.journeys.main.exceptions.POI.POINotFoundException;
import com.journeys.main.exceptions.POI.POIWriteFormatException;
import com.journeys.main.model.Coordinates;
import com.journeys.main.model.PointOfInterest;
import com.journeys.main.model.projections.JourneyExperiences;
import com.journeys.main.repositories.JourneyRepository;
import com.journeys.main.repositories.POIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class POIService {

    private final double DELTA_LON = 0.05;
    private final double DELTA_LAT = DELTA_LON / 2;

    private final POIRepository poiRepository;
    private final JourneyRepository journeyRepository;

    /**
     * Constructor of POIService
     * @param poiRepository
     * @param journeyRepository
     */
    @Autowired
    public POIService(POIRepository poiRepository, JourneyRepository journeyRepository) {
        this.poiRepository = poiRepository;
        this.journeyRepository = journeyRepository;
    }

    /**
     * Fetching every POI
     * @return a list of POI
     */
    public Collection<PointOfInterest> getPOIs() {
        Collection<PointOfInterest> list = poiRepository.getPOI();
        return checkUrlImage(list);
    }

    /**
     * Fetching a POI
     * @param id the id of the point of interest
     * @return the point of interest as an object
     * @throws POINotFoundException
     */
    public PointOfInterest getPOI(String id) throws POINotFoundException {
        Optional<PointOfInterest> result = poiRepository.findById(id);
        if (!result.isPresent())
            throw new POINotFoundException("Could not find POI " + id);
        return result.get();
    }

    /**
     * Saving a point of interest to database
     * @param poi a point of interest
     * @throws POIWriteFormatException
     */
    public void savePOI(PointOfInterest poi) throws POIWriteFormatException {
        poiRepository.save(poi);
    }

    /**
     * Fetching all point between
     * @param between a DTO containing coordinates
     * @return a list of point of interest
     */
    public Collection<PointOfInterest> getNearby(POIFindBetween between) {
        // Extending the top left corner more to the far left up
        Coordinates topLeft = new Coordinates();
        topLeft.setLat(Math.max(between.getStartLat(), between.getDestLat()) + DELTA_LAT);
        topLeft.setLng(Math.min(between.getStartLng(), between.getDestLng()) - DELTA_LON);

        // Extending the bottom right corner more to the far right down
        Coordinates bottomRight = new Coordinates();
        bottomRight.setLat(Math.min(between.getStartLat(), between.getDestLat()) - DELTA_LAT);
        bottomRight.setLng(Math.max(between.getStartLng(), between.getDestLng()) + DELTA_LON);

        Collection<PointOfInterest> list = poiRepository.getBetween(
                topLeft.getLat(),
                topLeft.getLng(),
                bottomRight.getLat(),
                bottomRight.getLng()
        );

        return checkUrlImage(list);
    }

    /**
     * Check if images of POI exists
     * @param list the list of POI
     * @return the list of POI with updated (or not) url
     */
    public Collection<PointOfInterest> checkUrlImage(Collection<PointOfInterest> list) {
        list.forEach(n -> {
            if (n.getUrl() == null) { // sets default image if none
                n.setUrl("images-poi/placeholder.png");
            }
        });
        return list;
    }

    /**
     * Getting experiences from a journey
     * @param poi a point of interest
     * @return the list of JourneyExperiences
     */
    public Collection<JourneyExperiences> getExperiencesBelongingToJourneys(String poi) {
        return journeyRepository.getExperiencesFromPoi(poi);
    }
}
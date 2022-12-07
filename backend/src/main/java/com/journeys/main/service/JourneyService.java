/**
 * @team Journeys
 * @file JourneyService.java
 * @date 21st January 2022
 */

package com.journeys.main.service;


import com.journeys.main.dto.JourneyCreateRequest;
import com.journeys.main.dto.JourneyUpdateRequest;
import com.journeys.main.exceptions.POI.POINotFoundException;
import com.journeys.main.exceptions.experience.ExperienceNotFound;
import com.journeys.main.exceptions.journey.JourneyDeleteException;
import com.journeys.main.exceptions.journey.JourneyNotFoundException;
import com.journeys.main.exceptions.journey.JourneySaveException;
import com.journeys.main.exceptions.user.UserNotFoundException;
import com.journeys.main.model.Coordinates;
import com.journeys.main.model.Journey;
import com.journeys.main.model.PointOfInterest;
import com.journeys.main.model.User;
import com.journeys.main.model.projections.JourneyExperiences;
import com.journeys.main.relationships.Experience;
import com.journeys.main.repositories.CoordinatesRepository;
import com.journeys.main.repositories.JourneyRepository;
import com.journeys.main.repositories.POIRepository;
import com.journeys.main.repositories.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@Service
public class JourneyService {

    private final CoordinatesRepository coordinatesRepository;
    private final UserRepository userRepository;
    private final JourneyRepository journeyRepository;
    private final AuthenticationService authenticationService;
    private final POIRepository poiRepository;
    private final StorageService storageService;

    /**
     * Constructor of JourneyService
     * @param coordinates
     * @param users
     * @param journeys
     * @param authenticationService
     * @param poiRepository
     * @param storageService
     */
    @Autowired
    public JourneyService(CoordinatesRepository coordinates,
                          UserRepository users,
                          JourneyRepository journeys,
                          AuthenticationService authenticationService,
                          POIRepository poiRepository,
                          StorageService storageService){
        this.coordinatesRepository = coordinates;
        this.userRepository = users;
        this.journeyRepository = journeys;
        this.authenticationService = authenticationService;
        this.poiRepository = poiRepository;
        this.storageService = storageService;
    }

    /**
     * Getting a journey
     * @param id the id of the journey
     * @return a Journey object
     */
    public Journey getJourney(String id){
        Optional<Journey> journey = journeyRepository.findById(id);

        if (journey.isEmpty()) {
            throw new JourneyNotFoundException("Could not find Journey with ID " + id);
        }
        return journey.get();
    }

    /**
     * Saving a jourrney
     * @param journey a journey Request
     * @return the journey
     * @throws RuntimeException
     */
    @Transactional
    public Journey saveJourney(JourneyCreateRequest journey) throws RuntimeException {
        Optional<Coordinates> start = coordinatesRepository.findExisting(
                journey.getStart().getLat(),
                journey.getStart().getLng()
        );

        Optional<Coordinates> end = coordinatesRepository.findExisting(
                journey.getEnd().getLat(),
                journey.getEnd().getLng()
        );


        UUIDStringGenerator uuid = new UUIDStringGenerator();
        String generated = uuid.generateId(null, null);
        Journey j = new Journey();
        Optional<User> creator = userRepository.findById(authenticationService.getTheDetailsOfCurrentUser().getUserName());


        if (start.isPresent())
            j.setStart(start.get());
        else
            j.setStart(journey.getStart());
        if (end.isPresent())
            j.setEnd(end.get());
        else
            j.setEnd(journey.getEnd());

        j.setTitle(journey.getTitle());
        j.setId(generated);

        if (creator.isPresent()) {
            Set<Journey> journeys = creator.get().getJourneys();
            journeys.add(j);
            creator.get().setJourneys(journeys);
            userRepository.save(creator.get());
        } else {
            throw new UserNotFoundException("Could not find user");
        }


        Set<Experience> experienceSet = new HashSet<>();
        List<String> pois = journey.getOrderedPOIsArray();
        for (int i = 0; i < pois.size(); i++) {

            Optional<PointOfInterest> p = poiRepository.getSinglePoi(pois.get(i));

            Experience exp = new Experience();
            exp.setOrder(i);
            exp.setDate(LocalDateTime.now());
            if (p.isPresent())
                exp.setPointOfInterest(p.get());
            else
                throw new POINotFoundException("Could not find poi with ID " + pois.get(i));
            experienceSet.add(exp);

        }

        j.setExperiences(experienceSet);
        Journey saved = journeyRepository.save(j);

        if(saved.getId() == null)
            throw new JourneySaveException("Could not save journey " + j.getTitle());
        return j;
    }

    /**
     * Getting experiences from a journey
     * @param id the id of the journey
     * @return a list of JourneyExperiences
     */
    public Collection<JourneyExperiences> getExperiencesFromJourney(String id) {
        if (journeyRepository.findById(id).isEmpty())
            throw new JourneyNotFoundException("Trouble finding journey");
        return journeyRepository.getExperiencesFromJourney(id);
    }

    /**
     * Findind the username of the journey's creator
     * @param id
     * @return
     */
    public Optional<String> getJourneyCreator(String id) {
        return journeyRepository.findJourneyCreator(id);
    }

    /**
     * Updating an experience belonging to a journey
     * @param journey an object JourneyUpdateRequest
     */
    public void updateExperienceFromJourney(JourneyUpdateRequest journey) {
        // Get the username of the creator
        Optional<String> creator = journeyRepository.findJourneyCreator(journey.getJourneyId());
        if(creator.isEmpty()) {
            throw new JourneySaveException("Could not update journey");
        }

        String concrete_creator = creator.get();
        // Check if the creator is the connected user
        if(!authenticationService.getTheDetailsOfCurrentUser().getUserName().equals(concrete_creator)){
            throw new JourneySaveException("Could not update journey");
        }
        JourneyExperiences experience = journeyRepository.getExperience(journey.getPoiId(), journey.getJourneyId());

        // TODO Factorization duplicate code
        // If relationship does not exist
        if (experience == null) {
            throw new ExperienceNotFound("Experience does not exist");
        }

        Optional<Journey> j = journeyRepository.findById(journey.getJourneyId());
        Optional<PointOfInterest> poi = poiRepository.findById(journey.getPoiId());

        if(j.isEmpty() || poi.isEmpty())
            throw new ExperienceNotFound("Could not update experience");

        Journey concrete_j = j.get();
        PointOfInterest concrete_poi = poi.get();
        Experience exp = null;
        for(Experience e : concrete_j.getExperiences()){
            if(Objects.equals(e.getPointOfInterest().getId(), concrete_poi.getId())){
               exp = e;
               break;
            }
        }


        // Get the relationship between User and the Experience
        List<String> imageNames =  storageService.replaceJourneysImages(journey.getImages(),journey.getJourneyId(),journey.getPoiId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if(exp == null){
            throw new ExperienceNotFound("No experience");
        }


        LocalDate dateTime;
        if (journey.getDate() == null) {
            if(exp.getDate() != null)
                dateTime = exp.getDate().toLocalDate();
            else
                dateTime = LocalDate.now();
        } else {
            dateTime = LocalDate.parse(journey.getDate(), formatter);
        }
        // Updating the relationship between Journey and POI
        journeyRepository.updateExperience(journey.getPoiId(), journey.getJourneyId(), journey.getDescription(), imageNames, dateTime.atStartOfDay());
    }

    /**
     * Updating an experience that is not part of a Journey
     * @param experience an Object JourneyUpdateRequest
     */
    public void updateSingleExperience(JourneyUpdateRequest experience) {
        // Finding the connected User
        Optional<User> creator = userRepository.findById(authenticationService.getTheDetailsOfCurrentUser().getUserName());

        // If User does not exist
        if(creator.isEmpty()) {
            throw new UserNotFoundException("Couldn't find the user");
        }

        User user = creator.get();

        // Get the relationship between User and the Experience
        int experienceGot = journeyRepository.getSingleExperience(user.getUserName(), experience.getPoiId());

        // If relationship does not exist
        if(experienceGot == 0) {
            throw new ExperienceNotFound("Experience does not exist");
        }

        // Store images to server
        List<String> imageNames = storageService.replaceJourneysImages(experience.getImages(),user.getUserName(),experience.getPoiId());

        // Updating the database with data of relationship Experience
        userRepository.updateSingleExperience(experience.getPoiId(), user.getUserName(), experience.getDescription(), imageNames);
    }

    /**
     * Deleting the journey
     * @param id the ID of the Journey
     */
    public void deleteJourney(String id){
        // Get the ID from the database
        Optional<Journey> toDelete = journeyRepository.findById(id);

        // Check if exists
        if (toDelete.isEmpty()) {
            throw new JourneyNotFoundException(id);
        }
        journeyRepository.deleteById(id);

        toDelete = journeyRepository.findById(id);

        if (toDelete.isPresent()) {
            throw new JourneyDeleteException(toDelete.get().getTitle());
        }
    }

    /**
     * Deleting experience from journey
     * @param idJourney the id of the journey
     * @param idPoi the id of the point of interest
     */
    public void deleteExperienceFromJourney(String idJourney, String idPoi) {
        int experienceToDelete = journeyRepository.checkExistenceExperienceFromJourney(idJourney, idPoi);

        if (experienceToDelete == 0) {
            throw new JourneyNotFoundException("Could not find the experience to delete");
        }

        journeyRepository.deleteExperienceFromJourney(idJourney, idPoi);
    }

    /**
     * Deleting a single experience
     * @param idPoi the id of the experience
     */
    public void deleteSingleExperience(String idPoi) {
        Optional<User> creator = userRepository.findById(authenticationService.getTheDetailsOfCurrentUser().getUserName());

        if(creator.isEmpty()) {
            throw new UserNotFoundException("Couldn't find the user");
        }

        User user = creator.get();
        int experienceToDelete = journeyRepository.checkExistenceSingleExperience(user.getUserName(), idPoi);

        if(experienceToDelete == 0) {
            throw new JourneyNotFoundException("Could not find the experience to delete");
        }

        journeyRepository.deleteExperience(user.getUserName(), idPoi);
    }
}
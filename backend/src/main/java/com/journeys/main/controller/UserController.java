/**
 * @team Journeys
 * @file UserController.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.journeys.main.exceptions.user.UserIllegalDataException;
import com.journeys.main.model.User;
import com.journeys.main.model.projections.*;
import com.journeys.main.relationships.Experience;
import com.journeys.main.repositories.UserRepository;
import com.journeys.main.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Constructor of UserController
     * @param userService
     * @param userRepository
     */
    @Autowired
    public UserController(UserService userService, UserRepository userRepository ) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Getting every user
     * @return the list of users
     */
    @GetMapping
    public Collection<UserDetails> getUsers() {
        Collection<User> users = userService.getAllUsers();
        return users.stream().map(User::getAsDetails).collect(Collectors.toList());
    }

    /**
     * Gettng journeys of the User
     * @param username the username
     * @return a UserJourneys object
     */
    //TODO move to journeys controller
    @GetMapping("/{username}/journeys")
    public UserJourneys getUserJourneys(@PathVariable String username) {
        User u = userService.getUser(username);
        return u.getAsJourneys();
    }

    /**
     * Saving a new user
     * @param user the User
     * @throws RuntimeException
     */
    @PostMapping
    public void saveUser(@RequestBody User user) throws RuntimeException {
        if (user.getUserName() == null || user.getUserName().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty())
            throw new UserIllegalDataException(" Please verify fields");
        userService.saveUser(user);
    }


    /**
     * Getting a single user
     * @param userName
     * @return a UserDetails experience
     */
    @GetMapping("/{userName}")
    public UserDetails getUser(@PathVariable String userName) {
        return userService.getUserDetails(userName);
    }

    /**
     * Getting experiences
     * @param userName the username of the user
     * @param solo a boolean
     * @return a collection of UserExperience
     */
    @GetMapping("/{userName}/experience")
    public Collection<UserExperiences> getExperiences(@PathVariable String userName,@RequestParam boolean solo) {
        return experiences(userName,solo);
    }

    /**
     * Getting experience
     * @param userName the username of the user
     * @param solo a boolean
     * @return a collection of UserExperience
     */
    Set<UserExperiences> experiences(String userName,boolean solo){

        Set<UserExperiences> details = new HashSet<>();
        User u = userService.getUser(userName);
        UserJourneys journeys = u.getAsJourneys();
        if(!solo){
            for(JourneyExperiences j : journeys.getJourneys()){
                for(PointOfInterestExperiences exp  : j.getExperiences()){
                    details.add(
                            new UserExperiences(
                                    exp.getPointOfInterest().getId(),
                                    exp.getPointOfInterest().getName(),
                                    exp.getImages(),
                                    exp.getDescription(),
                                    journeys.getJourneys().stream().map(UserController::getIdFromExperiences).collect(Collectors.toSet())
                            )
                    );
                }
            }
        } else {
            Collection<UserExperience> exps=  userRepository.getSingleExperiencesFrom(userName);
            for (UserExperience entity : exps) {
                for (Experience exp: entity.getExperiences()) {
                    System.out.println(exp.getPointOfInterest().getName());
                    details.add(new UserExperiences(
                            exp.getPointOfInterest().getId(),
                            exp.getPointOfInterest().getName(),
                            exp.getImages(),
                            exp.getDescription(),
                            entity.getJourneys()
                    ));
                }
            }
        }
        return details;
    }

    private static UserExperience.JourneyId getIdFromExperiences(JourneyExperiences exps){
        return exps::getId;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class UserExperiences {
        private String poiId;
        private String name;
        private List<String> images;
        private String description;

        private Set<UserExperience.JourneyId> journeys;
    }
}
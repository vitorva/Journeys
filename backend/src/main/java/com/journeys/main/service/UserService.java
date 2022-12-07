/**
 * @team Journeys
 * @file UserService.java
 * @date January 21st, 2022
 */

package com.journeys.main.service;

import com.journeys.main.authentication.JourneysAuthenticationProvider;
import com.journeys.main.exceptions.user.UserAlreadyExistsException;
import com.journeys.main.exceptions.user.UserNotFoundException;
import com.journeys.main.exceptions.user.UserSaveException;
import com.journeys.main.model.User;
import com.journeys.main.model.projections.UserDetails;
import com.journeys.main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JourneysAuthenticationProvider authenticationProvider;

    /**
     * Constructor of UserService
     * @param repository
     * @param authenticationProvider
     */
    @Autowired
    public UserService(UserRepository repository, JourneysAuthenticationProvider authenticationProvider) {
        this.userRepository = repository;
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Fetching every User
     * @return the list of User
     */
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Getting the User
     * @param userName the name of the User
     * @return the object User
     */
    public User getUser(String userName) {
        Optional<User> user = userRepository.findById(userName);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found " + userName);
        }
        return user.get();
    }

    /**
     * Getting the UserDetails
     * @param userName the name of the User
     * @return the object UserDetails
     */
    public UserDetails getUserDetails(String userName) {
        Optional<UserDetails> userDetails = userRepository.findByUserName(userName);
        if (userDetails.isEmpty()) {
            throw new UserNotFoundException("UserDetails not found " + userName);
        }

        return userDetails.get();
    }

    /**
     * Saving a user after registration
     * @param user a User
     * @return the User
     */
    public User saveUser(User user) {
        if (userRepository.findById(user.getUserName()).isPresent()) {
            throw new UserAlreadyExistsException(user.getUserName());
        } else {
            user.setPassword(authenticationProvider.getEncoder().encode(user.getPassword()));
            User storedUser = userRepository.save(user);
            if (!userRepository.findById(user.getUserName()).isPresent()) {
                throw new UserSaveException(user.getUserName());
            }
            return storedUser;
        }
    }
}
/**
 * @team Journeys
 * @file AuthenticationController.java
 * @date January 21st, 2022
 */

package com.journeys.main.controller;

import com.journeys.main.config.JwtProperties;
import com.journeys.main.config.JwtUtil;
import com.journeys.main.dto.APIResponse;
import com.journeys.main.dto.AuthenticationRequest;
import com.journeys.main.dto.AuthenticationResponse;
import com.journeys.main.model.User;
import com.journeys.main.service.AuthenticationService;
import com.journeys.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final JwtProperties properties;
    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Value("${com.journeys.main.jwt.accessToken}")
    private String accessTokenName;

    /**
     * Constructor of AuthenticationController
     * @param jwtUtil
     * @param authenticationService
     * @param userService
     * @param props
     */
    @Autowired
    public AuthenticationController(JwtUtil jwtUtil, AuthenticationService authenticationService, UserService userService, JwtProperties props) {
        this.jwtUtil = jwtUtil;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.properties = props;
    }

    /**
     * Creation of token authentication
     * @param authenticationRequest
     * @return a response
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        try {
            //throws user not found
            User found = userService.getUser(authenticationRequest.getUserName());
            //throws bad credential exception
            authenticationService.authenticateUser(authenticationRequest.getUserName(), authenticationRequest.getPassword());
            return generateResponseEntity(found);
        } catch (RuntimeException ex) {
            throw new BadCredentialsException("Unable to verify credentials");
        }
    }


    /**
     * Empties the value of the Http cookie
     * @return a response
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<?> logOut() {

        final String jwt = ""; // create an empty token

        // store jwt into a http cookie to avoid cookie theft by XSS attack
        HttpCookie cookie = ResponseCookie.from(accessTokenName, jwt)
                .httpOnly(true)
                .path("/api")
                .build();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().headers(responseHeaders).body(new APIResponse("The user has been logged out"));

    }

    /**
     * Signs up the user passed in parameter
     * @param user
     * @return an HTTP OK status with a success message
     */
    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        String usrName = user.getUserName();
        String pwd = user.getPassword();
        User saved = userService.saveUser(user);

        //throws bad credential exception
        authenticationService.authenticateUser(usrName, pwd);
        return generateResponseEntity(saved);
    }

    /**
     * Refreshes the token for a new fixed duration
     * @return
     */
    @GetMapping("/authrefresh")
    public ResponseEntity<?> refreshToken() {
        User userDetails = authenticationService.getTheDetailsOfCurrentUser();
        if (userDetails != null) {
            return generateResponseEntity(userDetails);
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * Generates a response entity with the username passed in parameter
     * @param user the user which will be connected
     * @return the response entity generated
     */
    public ResponseEntity<?> generateResponseEntity(User user) {

        final String jwt = jwtUtil.generateToken(user);

        // store jwt into a http cookie to avoid cookie theft by XSS attack
        HttpCookie cookie = ResponseCookie.from(properties.getAccessToken(), jwt)
                .maxAge(Integer.parseInt(properties.getDuration()))
                .httpOnly(true)
                .path("/api")
                .build();

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + Integer.parseInt(properties.getDuration()) * 1000L);

        AuthenticationResponse response = new AuthenticationResponse(user.getAsDetails(), expirationDate);

        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }
}
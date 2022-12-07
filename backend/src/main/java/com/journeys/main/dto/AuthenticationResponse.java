/**
 * @team Journeys
 * @file AuthenticationResponse.java
 * @date January 21st, 2022
 */

package com.journeys.main.dto;

import com.journeys.main.model.projections.UserDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuthenticationResponse {

    UserDetails user;
    Date tokenDuration;

    /**
     * Constructor of AuthenticationResponse
     * @param user the User
     * @param tokenDuration the duration of the token
     */
    public AuthenticationResponse(UserDetails user, Date tokenDuration) {
        this.user = user;
        this.tokenDuration = tokenDuration;
    }
}
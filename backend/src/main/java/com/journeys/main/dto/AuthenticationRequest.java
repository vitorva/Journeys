/**
 * @team Journeys
 * @file AuthenticationRequest.java
 * @date January 21st, 2022
 */

package com.journeys.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {

    private String userName;
    private String password;

    /**
     * ToString format
     * @return a String
     */
    public String toString() {
        return String.format("%s={%s %s}", AuthenticationRequest.class, userName, password);
    }
}
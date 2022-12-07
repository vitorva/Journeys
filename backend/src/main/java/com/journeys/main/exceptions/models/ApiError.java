/**
 * @team Journeys
 * @file ApiError.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyy@HH:mm:ss")
    Date date;
    HttpStatus status;
    String message;
    String url;

    /**
     * Constructor of ApiError
     * @param code the code error
     * @param message the message linked to code error
     * @param date the date
     * @param url the url
     */
    public ApiError(HttpStatus code, String message, Date date, String url) {
        this.date = date;
        this.status = code;
        this.message = message;
        this.url = url;
    }
}
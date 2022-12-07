/**
 * @team Journeys
 * @file ExperienceExceptionHandler.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.handlers;

import com.journeys.main.exceptions.experience.ExperienceNotFound;
import com.journeys.main.exceptions.models.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExperienceExceptionHandler {

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({ExperienceNotFound.class})
    public ResponseEntity<ApiError> handleExperienceNotFoundException(ExperienceNotFound ex, WebRequest request){
        return DefaultExceptionHandler.makeError(ex,request,HttpStatus.NOT_FOUND);
    }
}
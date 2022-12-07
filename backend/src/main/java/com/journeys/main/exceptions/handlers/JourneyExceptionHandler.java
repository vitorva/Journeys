/**
 * @team Journeys
 * @file JourneyExceptionHandler.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.handlers;

import com.journeys.main.exceptions.journey.JourneyDeleteException;
import com.journeys.main.exceptions.journey.JourneyInternalSaveException;
import com.journeys.main.exceptions.journey.JourneyNotFoundException;
import com.journeys.main.exceptions.journey.JourneySaveException;
import com.journeys.main.exceptions.models.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class JourneyExceptionHandler {

    /**
     *
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({JourneySaveException.class})
    public ResponseEntity<ApiError> handleJourneySaveException(JourneySaveException ex, WebRequest request) {
        return DefaultExceptionHandler.makeError(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({JourneyInternalSaveException.class})
    public ResponseEntity<ApiError> handleJourneyInternalSaveException(JourneyInternalSaveException ex, WebRequest request) {
        return DefaultExceptionHandler.makeError(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({JourneyDeleteException.class})
    public ResponseEntity<ApiError> handleJourneyDeleteException(JourneyDeleteException ex, WebRequest request) {
        return DefaultExceptionHandler.makeError(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({JourneyNotFoundException.class})
    public ResponseEntity<ApiError> handleJourneyNotFoundException(JourneyNotFoundException ex, WebRequest request) {
        return DefaultExceptionHandler.makeError(ex, request, HttpStatus.NOT_FOUND);
    }
}
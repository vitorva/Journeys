/**
 * @team Journeys
 * @file POIExceptionHandler.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.handlers;

import com.journeys.main.exceptions.POI.POINotFoundException;
import com.journeys.main.exceptions.POI.POIReadFormatException;
import com.journeys.main.exceptions.POI.POIWriteFormatException;
import com.journeys.main.exceptions.models.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class POIExceptionHandler {

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({POINotFoundException.class})
    public ResponseEntity<ApiError> handlePOINotFoundException(POINotFoundException ex, WebRequest request) {
        return DefaultExceptionHandler.makeError(ex, request, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({POIReadFormatException.class})
    public ResponseEntity<ApiError> handlePOIReadException(POIReadFormatException ex, WebRequest request) {
        return DefaultExceptionHandler.makeError(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({POIWriteFormatException.class})
    public ResponseEntity<ApiError> handlePOIWriteException(POIWriteFormatException ex, WebRequest request) {
        return DefaultExceptionHandler.makeError(ex, request, HttpStatus.BAD_REQUEST);
    }
}
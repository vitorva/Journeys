/**
 * @team Journeys
 * @file AuthenticationExceptionHandler.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.handlers;

import com.journeys.main.exceptions.models.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    /**
     * Sending an Exception if bad credentials provided
     * @param exc exception given
     * @param req a request HTTP by client
     * @return a ResponseEntity
     */
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ApiError> handleBadCredentialException(BadCredentialsException exc, WebRequest req) {
        return DefaultExceptionHandler.makeError(exc, req, HttpStatus.FORBIDDEN);
    }
}
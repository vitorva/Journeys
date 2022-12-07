/**
 * @team Journeys
 * @file UserExceptionHandler.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.handlers;

import com.journeys.main.exceptions.models.ApiError;
import com.journeys.main.exceptions.user.UserAlreadyExistsException;
import com.journeys.main.exceptions.user.UserIllegalDataException;
import com.journeys.main.exceptions.user.UserNotFoundException;
import com.journeys.main.exceptions.user.UserSaveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class UserExceptionHandler {

    /**
     *
     * @param exc exception given
     * @param req request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException exc, WebRequest req) {
        return DefaultExceptionHandler.makeError(exc, req, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param exc exception given
     * @param req request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({UserIllegalDataException.class})
    public ResponseEntity<ApiError> handeUserIllegalData(UserIllegalDataException exc, WebRequest req) {
        return DefaultExceptionHandler.makeError(exc, req, HttpStatus.BAD_REQUEST);
    }

    /**
     *
     * @param exc exception given
     * @param req request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({UserSaveException.class})
    public ResponseEntity<ApiError> handleUserSave(UserSaveException exc, WebRequest req) {
        return DefaultExceptionHandler.makeError(exc, req, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param exc exception given
     * @param req request HTTP by client
     * @return the response HTTP with the exception
     */
    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException exc, WebRequest req) {
        return DefaultExceptionHandler.makeError(exc, req, HttpStatus.NOT_FOUND);
    }
}
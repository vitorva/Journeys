/**
 * @team Journeys
 * @file DefaultExceptionHandler.java
 * @date January 21st, 2022
 */

package com.journeys.main.exceptions.handlers;

import com.journeys.main.exceptions.models.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Calendar;
import java.util.Date;

public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * @return current time
     */
    protected static Date Now() {
        return new Date(Calendar.getInstance().getTime().getTime());
    }

    /**
     * @param request
     * @return the URI of the request
     */
    protected static String getURI(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    /**
     * @param ex      exception given
     * @param request request HTTP by client
     * @param status  status HTTP that the application gives to client (200, 403, 404, ...)
     * @return the response HTTP with the exception
     */
    protected static ResponseEntity<ApiError> makeError(Exception ex, WebRequest request, HttpStatus status) {
        ApiError error = new ApiError(status, ex.getMessage(), Now(), getURI(request));
        return new ResponseEntity<>(error, new HttpHeaders(), status);
    }

    /**
     * @param ex exception given
     * @param request request HTTP by client
     * @return the response HTTP
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> handleDefaultException(Exception ex, WebRequest request) {
        return makeError(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
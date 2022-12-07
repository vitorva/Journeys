package com.journeys.main.exceptions.handlers;

import com.journeys.main.exceptions.storage.FileUploadException;
import com.journeys.main.exceptions.storage.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.File;

@ControllerAdvice
public class StorageExceptionHandler {

    @ExceptionHandler(FileUploadException.class)
    public void handleFileUpdloadException(FileUploadException ex, WebRequest req){
        DefaultExceptionHandler.makeError(ex,req, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StorageException.class)
    public void handleStorageException(StorageException ex, WebRequest req){
        DefaultExceptionHandler.makeError(ex,req, HttpStatus.BAD_REQUEST);
    }
}

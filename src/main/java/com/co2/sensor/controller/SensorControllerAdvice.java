package com.co2.sensor.controller;

import com.co2.sensor.exception.InvalidUuidDException;
import com.co2.sensor.exception.RecordNotFoundException;
import com.co2.sensor.exception.SensorEmptyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SensorControllerAdvice
{
    @ExceptionHandler(value = SensorEmptyValueException.class)
    public ResponseEntity<String> badRequest(SensorEmptyValueException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exception.getMessage());
    }

    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<String> recordNotFound(RecordNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(exception.getMessage());
    }

    @ExceptionHandler(value = InvalidUuidDException.class)
    public ResponseEntity<String> recordNotFound(InvalidUuidDException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(exception.getMessage());
    }


}

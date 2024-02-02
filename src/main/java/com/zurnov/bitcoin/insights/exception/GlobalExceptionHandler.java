package com.zurnov.bitcoin.insights.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError("500", HttpStatus.INTERNAL_SERVER_ERROR, "Couldn't fetch data", ex.getMessage(), UUID.randomUUID().toString());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(OperationFailedException.class)
    public ResponseEntity<Object> handleOperationFailedException(OperationFailedException ex) {
        ApiError apiError = new ApiError("500", HttpStatus.INTERNAL_SERVER_ERROR, "Operation Failed", ex.getMessage(), UUID.randomUUID().toString());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException ex) {
        ApiError apiError = new ApiError("400", HttpStatus.BAD_REQUEST, "Invalid Request", ex.getMessage(), UUID.randomUUID().toString());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        ApiError apiError = new ApiError("400", HttpStatus.BAD_REQUEST, "Validation Exception", ex.getMessage(), UUID.randomUUID().toString());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        ApiError apiError = new ApiError("500", HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected exception", ex.getMessage(), UUID.randomUUID().toString());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        ObjectMapper objectMapper = new ObjectMapper();
        String apiErrorJSON = null;
        try {
            apiErrorJSON = objectMapper.writeValueAsString(apiError);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        log.error("Bitcoin Insights API Service error response -> {}", apiErrorJSON);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
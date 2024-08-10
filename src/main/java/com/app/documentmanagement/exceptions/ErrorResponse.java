package com.app.documentmanagement.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This {@Code ErrorResponse} class will use send error to the client when system throws exception
 * This class is used by {@Code GlobalExceptionHandler} which intercept exceptions
 * 
 * @author Zeeshan Hanif
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Status Code of Error e.g 400, 404, 409
     */
    private int statusCode;

    /**
     * Error message 
     */
    private String message;

    /**
     * Constructs {@Code ErrorResponse} with errro message
     * 
     * @param message error message
     */
    public ErrorResponse(String message) {
        this.message = message;
    }
}

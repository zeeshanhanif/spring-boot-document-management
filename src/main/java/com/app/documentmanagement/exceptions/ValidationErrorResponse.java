package com.app.documentmanagement.exceptions;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This {@Code ValidationErrorResponse} class will use send validation errors to the client 
 * when there is validation errors on input fields that are received from client.
 * This class is used by {@Code GlobalExceptionHandler} which intercept exceptions
 * 
 * @author Zeeshan Hanif
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {

    /**
     * Status Code of Error e.g 400, 404, 409
     */
    private int statusCode;

    /**
     * Key Value pair of all the error
     * e.g firstName: First Name is mandatory
     */
    private Map<String, String> errors;
}
package com.app.documentmanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;

/**
 * This {@Code GlobalExceptionHandler} class act as global exception handler and it intercepts
 * exception that are provided in methods of this class
 * When any exception is thrown in any of the Rest Controller will be handled by {@Code GlobalExceptionHandler}
 * 
 * @author Zeeshan Hanif
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles {@Code AuthorNotFoundException} exception and returns
     * {@Code ErrorResponse} with NOT_FOUND status and error message
     * 
     * @param exception {@Code AuthorNotFoundException}
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(AuthorNotFoundException exception){
        log.error(String.format("AuthorNotFoundException: StatusCode: %s -- Message: %s",HttpStatus.NOT_FOUND.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    /**
     * Handles {@Code AuthorAlreadyExistsException} exception and returns
     * {@Code ErrorResponse} with CONFLICT status and error message
     * 
     * @param exception {@Code AuthorAlreadyExistsException}
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = AuthorAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleException(AuthorAlreadyExistsException exception){
        log.error(String.format("AuthorAlreadyExistsException: StatusCode: %s -- Message: %s",HttpStatus.CONFLICT.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }

    /**
     * Handles {@Code AuthorNullValueException} exception and returns
     * {@Code ErrorResponse} with BAD_REQUEST status and error message
     * 
     * @param exception {@Code AuthorNullValueException}
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = AuthorNullValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(AuthorNullValueException exception){
        log.error(String.format("AuthorNullValueException: StatusCode: %s -- Message: %s",HttpStatus.BAD_REQUEST.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    /**
     * Handles {@Code DocumentAttachedToAuthorException} exception and returns
     * {@Code ErrorResponse} with BAD_REQUEST status and error message
     * 
     * @param exception {@Code DocumentAttachedToAuthorException}
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = DocumentAttachedToAuthorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(DocumentAttachedToAuthorException exception){
        log.error(String.format("DocumentAttachedToAuthorException: StatusCode: %s -- Message: %s",HttpStatus.BAD_REQUEST.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    /**
     * Handles {@Code DocumentNotFoundException} exception and returns
     * {@Code ErrorResponse} with NOT_FOUND status and error message
     * 
     * @param exception {@Code DocumentNotFoundException}
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = DocumentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(DocumentNotFoundException exception){
        log.error(String.format("DocumentNotFoundException: StatusCode: %s -- Message: %s",HttpStatus.NOT_FOUND.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    /**
     * Handles {@Code DocumentAlreadyExistsException} exception and returns
     * {@Code ErrorResponse} with CONFLICT status and error message
     * 
     * @param exception {@Code DocumentAlreadyExistsException}
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = DocumentAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleException(DocumentAlreadyExistsException exception){
        log.error(String.format("DocumentAlreadyExistsException: StatusCode: %s -- Message: %s",HttpStatus.CONFLICT.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }

    /**
     * Handles {@Code DocumentNullValueException} exception and returns
     * {@Code ErrorResponse} with BAD_REQUEST status and error message
     * 
     * @param exception {@Code DocumentNullValueException}
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = DocumentNullValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(DocumentNullValueException exception){
        log.error(String.format("DocumentNullValueException: StatusCode: %s -- Message: %s",HttpStatus.BAD_REQUEST.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
    
    /**
     * Handles {@Code MethodArgumentNotValidException} exception, this exception is thronw by
     * spring boot validation system. When inputs received from client are not expected as specified
     * then {@Code MethodArgumentNotValidException} will be thrown 
     * 
     * @param exception {@Code MethodArgumentNotValidException}
     * @return {@Code ValidationErrorResponse} Key Value pair of errro message
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ValidationErrorResponse handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error(String.format("MethodArgumentNotValidException: StatusCode: %s -- Field: %s -- Error Message: %s",HttpStatus.BAD_REQUEST.value(), fieldName, errorMessage));
        });
        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(),errors);
    }

    /**
     * Works as gloabl exception handler for all the exceptions that are not handled by other mehtods 
     * of this class
     * 
     * @param exception accept all other exceptions
     * @return {@Code ErrorResponse} with status code and error message
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleException(Exception exception){
        log.error(String.format("Exception: StatusCode: %s -- Message: %s",HttpStatus.BAD_REQUEST.value(),exception.getMessage()));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

}

package com.app.documentmanagement.exceptions;

/**
 * This {@Code AuthorNullValueException} is Runtime exception and inherits 
 * from {@Code DocumentManagementException}. This exception will be thrown when system
 * tries to work with null values
 * 
 * @author Zeeshan Hanif
 * @see DocumentManagementException
 */
public class AuthorNullValueException extends DocumentManagementException {
    public AuthorNullValueException() {}

    public AuthorNullValueException(String msg) {
        super(msg);
    }
}

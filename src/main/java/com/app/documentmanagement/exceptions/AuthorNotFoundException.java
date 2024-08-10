package com.app.documentmanagement.exceptions;

/**
 * This {@Code AuthorNotFoundException} is Runtime exception and inherits 
 * from {@Code DocumentManagementException}. This exception will be thrown when system
 * tries to get author that does not exists
 * 
 * @author Zeeshan Hanif
 * @see DocumentManagementException
 */
public class AuthorNotFoundException extends DocumentManagementException {
    
    public AuthorNotFoundException() {}

    public AuthorNotFoundException(String msg) {
        super(msg);
    }
}

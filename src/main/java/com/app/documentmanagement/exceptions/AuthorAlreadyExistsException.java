package com.app.documentmanagement.exceptions;

/**
 * This {@Code AuthorAlreadyExistsException} is Runtime exception and inherits 
 * from {@Code DocumentManagementException}. This exception will be thrown when system
 * tries to add similar author
 * 
 * @author Zeeshan Hanif
 * @see AuthorRepository
 * @see AuthorServiceImpl 
 */
public class AuthorAlreadyExistsException extends DocumentManagementException {
    
    public AuthorAlreadyExistsException() {}

    public AuthorAlreadyExistsException(String msg) {
        super(msg);
    }
}

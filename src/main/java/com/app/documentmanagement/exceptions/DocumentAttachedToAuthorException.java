package com.app.documentmanagement.exceptions;

/**
 * This {@Code DocumentAttachedToAuthorException} is Runtime exception and inherits 
 * from {@Code DocumentManagementException}. This exception will be thrown when system
 * tries remove author that has document assigned to it
 * 
 * @author Zeeshan Hanif
 * @see DocumentManagementException
 */
public class DocumentAttachedToAuthorException extends DocumentManagementException{
    public DocumentAttachedToAuthorException() {}

    public DocumentAttachedToAuthorException(String msg) {
        super(msg);
    }
}

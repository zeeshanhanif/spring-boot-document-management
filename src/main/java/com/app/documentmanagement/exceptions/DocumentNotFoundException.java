package com.app.documentmanagement.exceptions;

/**
 * This {@Code DocumentNotFoundException} is Runtime exception and inherits 
 * from {@Code DocumentManagementException}. This exception will be thrown when system
 * tries to get document that does not exists
 * 
 * @author Zeeshan Hanif
 * @see DocumentManagementException
 */
public class DocumentNotFoundException extends DocumentManagementException{
    
    public DocumentNotFoundException() {}

    public DocumentNotFoundException(String msg) {
        super(msg);
    }
}

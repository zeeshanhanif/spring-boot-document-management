package com.app.documentmanagement.exceptions;

/**
 * This {@Code DocumentAlreadyExistsException} is Runtime exception and inherits 
 * from {@Code DocumentManagementException}. This exception will be thrown when system
 * tries to add same doucument more then once in single author
 * 
 * @author Zeeshan Hanif
 * @see DocumentManagementException
 */
public class DocumentAlreadyExistsException extends DocumentManagementException{
    
    public DocumentAlreadyExistsException() {}

    public DocumentAlreadyExistsException(String msg) {
        super(msg);
    }
}

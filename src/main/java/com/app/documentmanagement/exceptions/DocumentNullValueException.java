package com.app.documentmanagement.exceptions;

/**
 * This {@Code DocumentNullValueException} is Runtime exception and inherits 
 * from {@Code DocumentManagementException}. This exception will be thrown when system
 * tries to work with null values
 * 
 * @author Zeeshan Hanif
 * @see DocumentManagementException
 */
public class DocumentNullValueException extends DocumentManagementException {
    public DocumentNullValueException() {}

    public DocumentNullValueException(String msg) {
        super(msg);
    }
}

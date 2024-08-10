package com.app.documentmanagement.exceptions;

/**
 * This {@Code DocumentManagementException} is Runtime exception and is parent exception of
 * all the exception of Document Management System
 * 
 * @author Zeeshan Hanif
 */
public class DocumentManagementException extends RuntimeException {
 
    public DocumentManagementException() {}

    public DocumentManagementException(String msg) {
        super(msg);
    }
}

package com.app.documentmanagement.exceptions;

public class DocumentManagementException extends RuntimeException {
 
    public DocumentManagementException() {}

    public DocumentManagementException(String msg) {
        super(msg);
    }
}

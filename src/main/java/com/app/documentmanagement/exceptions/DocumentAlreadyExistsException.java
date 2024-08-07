package com.app.documentmanagement.exceptions;

public class DocumentAlreadyExistsException extends DocumentManagementException{
    
    public DocumentAlreadyExistsException() {}

    public DocumentAlreadyExistsException(String msg) {
        super(msg);
    }
}

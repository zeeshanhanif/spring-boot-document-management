package com.app.documentmanagement.exceptions;

public class DocumentNotFoundException extends DocumentManagementException{
    
    public DocumentNotFoundException() {}

    public DocumentNotFoundException(String msg) {
        super(msg);
    }
}

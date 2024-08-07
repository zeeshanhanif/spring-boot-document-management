package com.app.documentmanagement.exceptions;

public class AuthorNotFoundException extends DocumentManagementException {
    
    public AuthorNotFoundException() {}

    public AuthorNotFoundException(String msg) {
        super(msg);
    }
}

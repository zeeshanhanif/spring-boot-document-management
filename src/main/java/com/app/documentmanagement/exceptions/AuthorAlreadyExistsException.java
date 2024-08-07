package com.app.documentmanagement.exceptions;

public class AuthorAlreadyExistsException extends DocumentManagementException {
    
    public AuthorAlreadyExistsException() {}

    public AuthorAlreadyExistsException(String msg) {
        super(msg);
    }
}

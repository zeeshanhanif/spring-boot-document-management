package com.app.documentmanagement.exceptions;

public class AuthorNullValueException extends DocumentManagementException {
    public AuthorNullValueException() {}

    public AuthorNullValueException(String msg) {
        super(msg);
    }
}

package com.app.documentmanagement.exceptions;

public class DocumentNullValueException extends DocumentManagementException {
    public DocumentNullValueException() {}

    public DocumentNullValueException(String msg) {
        super(msg);
    }
}

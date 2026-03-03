package com.web.forum.Security.Error.CustomErrors;

//Custom error => Can be used via throw new exception and ErrorExceptionHandler
public class BadRequestError extends RuntimeException {
    public BadRequestError(String message) {
        super(message);
    }
}


package com.web.forum.Security.Error.CustomErrors;

//Custom error => Can be used via throw new exception and ErrorExceptionHandler
public class NotFoundError extends RuntimeException {
    public NotFoundError(String message) {
        super(message);
    }
}

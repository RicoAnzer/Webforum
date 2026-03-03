package com.web.forum.Security.Error.CustomErrors;

//Custom error => Can be used via throw new exception and ErrorExceptionHandler
public class ConflictError extends RuntimeException {
    public ConflictError(String message) {
        super(message);
    }
}

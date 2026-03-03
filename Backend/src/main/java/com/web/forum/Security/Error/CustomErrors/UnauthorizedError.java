package com.web.forum.Security.Error.CustomErrors;

//Custom error => Can be used via throw new exception and ErrorExceptionHandler
public class UnauthorizedError extends RuntimeException {
    public UnauthorizedError(String message) {
        super(message);
    }
}

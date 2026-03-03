package com.web.forum.Security.Error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.web.forum.Security.Error.CustomErrors.BadRequestError;
import com.web.forum.Security.Error.CustomErrors.ConflictError;
import com.web.forum.Security.Error.CustomErrors.NotFoundError;
import com.web.forum.Security.Error.CustomErrors.UnauthorizedError;

//Custom exception Handler 
// => throw new ResponseStatusException(HttpStatus, message) doesn't show message by default for safety reasons
// => Custom Errors and ExceptionHandlers allow control over display of information in body
@ControllerAdvice
public class ErrorExceptionHandler {
    //HttpStatus = BadRequest
    @ExceptionHandler(BadRequestError.class)
    public ResponseEntity<Map<String, Object>> generateBadRequest(BadRequestError ex) {
        Map<String, Object> body = new HashMap<>();
        //Information to display
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    //HttpStatus = NotFound
    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<Map<String, Object>> generateNotFound(NotFoundError ex) {
        Map<String, Object> body = new HashMap<>();
        //Information to display
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    //HttpStatus = Conflict
    @ExceptionHandler(ConflictError.class)
    public ResponseEntity<Map<String, Object>> generateConflict(ConflictError ex) {
        Map<String, Object> body = new HashMap<>();
        //Information to display
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    //HttpStatus = Unauthorized
    @ExceptionHandler(UnauthorizedError.class)
    public ResponseEntity<Map<String, Object>> generateUnauthorized(UnauthorizedError ex) {
        Map<String, Object> body = new HashMap<>();
        //Information to display
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
}

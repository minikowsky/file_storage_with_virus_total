package org.wsb;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler
    public String globalErrorHandler(RuntimeException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AuthenticationException.class)
    public String authenticationHandler(AuthenticationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public String authorizationHandler(AuthorizationDeniedException e) {
        return e.getMessage();
    }
}

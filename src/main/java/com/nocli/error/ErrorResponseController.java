package com.nocli.error;

import com.nocli.error.records.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ControllerAdvice
class ErrorResponseController {

    // fallback method
    @ExceptionHandler(Exception.class) // exception handled
    public ResponseEntity<ErrorResponse> handleExceptions(
            Exception e
    ) {
        // ... potential custom logic

        Class<? extends Exception> eClass = e.getClass();

        ResponseStatus statusAnnotation = eClass.getAnnotation(ResponseStatus.class);

        if (statusAnnotation != null) {
            HttpStatus status = statusAnnotation.code();



            return new ResponseEntity<>(
                    new ErrorResponse(
                            status.value(),
                            status.name(),
                            e.getMessage(),
                            String.join("\n", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())
                    ),
                    status
            );
        }
        return new ResponseEntity<>(
                new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        e.getMessage(),
                        String.join("\n", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).toList())
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

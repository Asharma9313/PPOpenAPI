package com.pulsepoint.commons.exception;

import lombok.extern.log4j.Log4j2;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidDataException.class})
    public ResponseEntity<CustomErrorResponse> customHandleNotFound(Exception ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setError(ex.getMessage());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        log.error("Error occurred while processing the request", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

   @ExceptionHandler({ RuntimeException.class})
    public ResponseEntity<CustomErrorResponse> RuntimeExceptionHandler(Exception ex, WebRequest request)  {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setError("Error occurred while processing the request");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(Exception ex, WebRequest request, HttpServletResponse response) throws IOException {
        log.error("Error occurred while processing the request", ex);
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all fields errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        body.put("errors", errors);
        log.error("Error occurred while processing the request", ex);
        return new ResponseEntity<>(body, headers, status);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> ExceptionHandler(Exception ex, WebRequest request) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setError("Error occurred while processing the request");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("Error occurred while processing the request", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
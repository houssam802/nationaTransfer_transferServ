package com.agent_srv.handler;

import com.agent_srv.exception.TransfersException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class TransferExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                errors,new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {TransfersException.class})
    public ResponseEntity<?> handleTransfersException(TransfersException ex , WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(
                errors,new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {SQLException.class})
    public ResponseEntity<?> handleSQLException(SQLException ex , WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        String message=ex.getMessage();
        if(ex.getMessage().contains("multitransfer_ibfk_1")){
            message = "Expense option doesn't exist !";
        }

        if(ex.getMessage().contains("status_fk")){
            message = "Status option doesn't exist !";
        }
        errors.put("error", message);
        return new ResponseEntity<>(
                errors,new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex , WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        String message=ex.getMessage();
        errors.put("error", message);
        return new ResponseEntity<>(
                errors,new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

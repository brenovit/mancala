package com.bol.brenovit.mancala.entrypoint.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;
import com.bol.brenovit.mancala.core.common.exception.ResourceNotFoundException;
import com.bol.brenovit.mancala.core.game.exception.GameErrorException;
import com.bol.brenovit.mancala.core.game.exception.SowException;
import com.bol.brenovit.mancala.entrypoint.error.dto.EntryPointError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EntryPointError> processValidationError(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        EntryPointError apiErros = new EntryPointError(ErrorCode.INVALID_ARGUMENT);

        ex.getBindingResult().getFieldErrors()
                .forEach(e -> apiErros.addErro(new EntryPointError(ErrorCode.INVALID_ARGUMENT, e.getDefaultMessage())));

        return new ResponseEntity<>(apiErros, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<EntryPointError> processValidationError(ResourceNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        EntryPointError apiErros = new EntryPointError(ex.getError());
        return new ResponseEntity<>(apiErros, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameErrorException.class)
    public ResponseEntity<EntryPointError> processValidationError(GameErrorException ex) {
        log.error(ex.getMessage(), ex);
        EntryPointError apiErros = new EntryPointError(ex.getError());
        return new ResponseEntity<>(apiErros, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SowException.class)
    public ResponseEntity<EntryPointError> processValidationError(SowException ex) {
        log.error(ex.getMessage(), ex);
        EntryPointError apiErros = new EntryPointError(ex.getError());
        return new ResponseEntity<>(apiErros, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EntryPointError> processValidationError(Exception ex) {
        log.error(ex.getMessage(), ex);
        EntryPointError apiErro = new EntryPointError(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(apiErro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

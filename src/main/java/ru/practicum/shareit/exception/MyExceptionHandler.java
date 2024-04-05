package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ResponseEntity<Error> handleValidEmailException(final ValidEmailException e) {
        log.error("Exception ValidEmailException: {}, статус ответа: {}", e.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ResponseEntity<Error> handleNotFoundException(final NotFoundException e) {
        log.error("Exception NotFoundException: {}, статус ответа: {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<Error> handleBadRequestException(final BadRequestException e) {
        log.error("Exception BadRequestException: {}, статус ответа: {}", e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ResponseEntity<Error> handleConstraintViolationException(final ConstraintViolationException e) {
        log.error("Exception ConstraintViolationException: {}, статус ответа: {}", e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new Error("Ошибка: " + e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

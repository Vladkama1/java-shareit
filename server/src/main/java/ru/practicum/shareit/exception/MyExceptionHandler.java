package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ValidEmailException.class)
    public ResponseEntity<ResponseError> handleValidEmailException(final ValidEmailException e) {
        log.error("Exception ValidEmailException: {}, статус ответа: {}", e.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(new ResponseError(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseError> handleNotFoundException(final NotFoundException e) {
        log.error("Exception NotFoundException: {}, статус ответа: {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ResponseError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseError> handleBadRequestException(final BadRequestException e) {
        log.error("Exception BadRequestException: {}, статус ответа: {}", e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}

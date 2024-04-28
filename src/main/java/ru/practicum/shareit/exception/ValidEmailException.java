package ru.practicum.shareit.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ValidEmailException extends RuntimeException {
    private final String message;
}

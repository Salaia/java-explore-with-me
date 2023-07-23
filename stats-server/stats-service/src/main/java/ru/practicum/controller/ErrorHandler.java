package ru.practicum.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.ApiError;
import ru.practicum.exceptions.BadRequestException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiError badRequestException(final BadRequestException e) {
        log.warn("Исключение badRequestException {}", e.getMessage());
        return ApiError.builder()
                .reason("Неверные даты")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
package ru.yandex.practicum.explore.exception;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@NoArgsConstructor
public class CustomExceptionHandler {
    @ExceptionHandler({ ValidationException.class, ConstraintViolationException.class,
                        MissingServletRequestParameterException.class,
                        DuplicateKeyException.class, BadRequestException.class, MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalArgumentExceptionHandler(Exception exception) {
        String reason = "For the requested operation the conditions are not met.";
        log.warn("{STATUS: {}, reason: {}, message: {}, time: {}}", exception.getMessage(), reason, HttpStatus.BAD_REQUEST.name(),
                LocalDateTime.now());
        return new ErrorResponse(exception.getStackTrace(), exception.getMessage(), reason,
                HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler({NullPointerException.class,
            NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundExceptionHandler(Exception exception) {
        String reason = "The required object was not found.";
        log.warn("{STATUS: {}, reason: {}, message: {}, time: {}}", HttpStatus.NOT_FOUND.name(), reason, exception.getMessage(),
                LocalDateTime.now());

        return new ErrorResponse(exception.getStackTrace(), exception.getMessage(), reason, HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler({NotAllowedException.class,
            ConditionsNotMetException.class,
            IllegalStateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse notAllowedExceptionHandler(Exception exception) {
        String reason = "For the requested operation the conditions are not met.";
        log.warn("{STATUS: {}, reason: {}, message: {}, time: {}}", HttpStatus.FORBIDDEN.name(), reason, exception.getMessage(),
                LocalDateTime.now());

        return new ErrorResponse(exception.getStackTrace(), exception.getMessage(), reason, HttpStatus.FORBIDDEN,
                LocalDateTime.now());
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class, HttpClientErrorException.Conflict.class,
                        DataIntegrityViolationException.class, ConflictRequestException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conditionsNotMetExceptionHandler(Exception exception) {
        String reason = "For the requested operation the conditions are not met.";
        log.warn("{STATUS: {}, reason: {}, message: {}, time: {}}", HttpStatus.CONFLICT.name(), reason, exception.getMessage(),
                LocalDateTime.now());

        return new ErrorResponse(exception.getStackTrace(), exception.getMessage(), reason, HttpStatus.CONFLICT,
                LocalDateTime.now());
    }
}

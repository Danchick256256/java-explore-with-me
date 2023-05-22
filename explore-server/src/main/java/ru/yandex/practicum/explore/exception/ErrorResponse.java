package ru.yandex.practicum.explore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
    private final List<String> error;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ErrorResponse(StackTraceElement[] error, String message, String reason, HttpStatus status, LocalDateTime timestamp) {
        this.error = Arrays.stream(error).map(StackTraceElement::toString).collect(Collectors.toList());
        this.message = message;
        this.reason = reason;
        this.status = status.name();
        this.timestamp = timestamp.format(formatter);
    }
}

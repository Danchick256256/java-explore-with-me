package ru.yandex.practicum.explore.exception;

public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException(String message) {
        super(message);
    }
}

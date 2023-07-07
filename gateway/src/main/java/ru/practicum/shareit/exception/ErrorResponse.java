package ru.practicum.shareit.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ErrorResponse {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
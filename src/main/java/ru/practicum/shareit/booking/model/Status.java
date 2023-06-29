package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.NotFoundException;

public enum Status {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Status getSearchStatus(String text) {
        if (text.isBlank()) {
            return Status.ALL;
        }
        try {
            return Status.valueOf(text.toUpperCase().trim());
        } catch (Exception e) {
            throw new NotFoundException("status not found");
        }
    }
}
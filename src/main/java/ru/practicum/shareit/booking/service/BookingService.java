package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.InputBookingDto;
import ru.practicum.shareit.booking.model.OutputBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingService {

    OutputBookingDto create(InputBookingDto bookingDto, long userId);

    OutputBookingDto approveBooking(long bookingId, long userId, boolean approve);

    Booking getBooking(long bookingId, long userId);

    OutputBookingDto getBookingDto(long bookingId, long userId);

    List<OutputBookingDto> getBookingUser(Status status, long bookerId);

    List<OutputBookingDto> getBookingOwner(Status status, long ownerId);
}

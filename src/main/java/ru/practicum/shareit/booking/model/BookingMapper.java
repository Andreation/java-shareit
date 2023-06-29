package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    private BookingMapper() {
    }

    public static OutputBookingDto toOutputBookingDto(Booking booking) {
        return booking == null ? null : OutputBookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.toItemDto(booking.getItem()))
                .booker(UserMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static List<OutputBookingDto> toOutputBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toOutputBookingDto)
                .collect(Collectors.toList());
    }

}
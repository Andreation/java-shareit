package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

@Generated
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutputBookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}

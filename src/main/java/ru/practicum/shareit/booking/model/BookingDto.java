package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.ItemDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private long bookerId;
}
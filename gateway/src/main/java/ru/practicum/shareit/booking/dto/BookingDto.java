package ru.practicum.shareit.booking.dto;

import lombok.*;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class BookingDto {
    private Long id;
    @PastOrPresent(message = "start mustnt be in future")
    private LocalDateTime start;
    @FutureOrPresent(message = "end mustnt be in past")
    private LocalDateTime end;
    private Long itemId;
}
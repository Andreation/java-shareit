package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.valid.StartBeforeOrNotEqualEndDateValid;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@StartBeforeOrNotEqualEndDateValid
public class BookingDto {
    private Long id;
    @PastOrPresent(message = "start mustnt be in future")
    private LocalDateTime start;
    @FutureOrPresent(message = "end mustnt be in past")
    private LocalDateTime end;
    @NotNull
    private Long itemId;
}
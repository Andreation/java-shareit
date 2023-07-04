package ru.practicum.shareit.booking.model;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Generated
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InputBookingDto {
    @NotNull
    private Long itemId;
    @NotNull(message = "start = null")
    @FutureOrPresent(message = "start mustnt be future")
    private LocalDateTime start;
    @NotNull(message = "end = null")
    @Future(message = "end mustnt be past")
    private LocalDateTime end;
}

package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "start")
    @PastOrPresent
    private LocalDateTime startOfBooking;
    @JsonProperty(value = "end")
    @FutureOrPresent
    private LocalDateTime endOfBooking;
    @NotNull
    private Long itemId;
}
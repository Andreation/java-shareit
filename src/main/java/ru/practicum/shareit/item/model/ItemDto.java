package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
}
package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemDto {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private Long request;
}
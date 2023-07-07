package ru.practicum.shareit.user.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private long id;
    private String name;
    private String email;
}

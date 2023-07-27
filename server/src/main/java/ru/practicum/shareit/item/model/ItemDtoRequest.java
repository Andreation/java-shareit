package ru.practicum.shareit.item.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDtoRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}

package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.item.model.ItemDtoRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDtoRequest> items;
}

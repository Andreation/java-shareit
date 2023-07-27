package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    @Size(max = 400)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
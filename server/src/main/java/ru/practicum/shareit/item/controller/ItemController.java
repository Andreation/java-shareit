package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemDtoRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final String line = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDtoRequest create(@RequestHeader(line) long ownerId, @RequestBody ItemDtoRequest itemDto) {
        return itemService.add(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(line) long ownerId, @PathVariable long itemId,
                          @RequestBody Map<String, String> updates) {
        return itemService.update(ownerId, itemId, updates);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader(line) long userId, @PathVariable long itemId) {
        return itemService.getItemDtoById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader(line) long ownerId,
                                @RequestParam(value = "from", required = false) Long from,
                                @RequestParam(value = "size", required = false) Long size) {
        return itemService.getAllUserItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader(line) long userId, @RequestParam(name = "text") String text,
                                     @RequestParam(value = "from", required = false) Long from,
                                     @RequestParam(value = "size", required = false) Long size) {
        return itemService.searchItems(userId, text, from, size);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(line) long ownerId, @PathVariable long itemId) {
        itemService.delete(ownerId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(line) long userId, @PathVariable long itemId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}

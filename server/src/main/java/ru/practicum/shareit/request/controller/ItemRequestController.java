package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestServiceInDb;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestServiceInDb itemRequestService;

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long requestId) {
        return ItemRequestMapper.toDto(itemRequestService.getItemRequest(userId,requestId));
    }

    @GetMapping
    public List<ItemRequestDto> geOwnItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemRequestMapper.toDtoList(itemRequestService.getOwnItemRequest(userId));
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(value = "from", required = false) Long from,
                                                  @RequestParam(value = "size", required = false) Long size) {
        return ItemRequestMapper.toDtoList(itemRequestService.getAllItemRequest(userId,from,size));
    }

    @PostMapping
    public ItemRequestDto newItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemRequestDto itemRequestDto) {
        return ItemRequestMapper.toDto(itemRequestService.create(userId,itemRequestDto));
    }
}

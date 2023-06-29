package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto create(long ownerId, Item item);

    ItemDto update(long ownerId, long itemId, Map<String, String> updates);

    ItemDto getItemDto(long itemId);

    List<ItemDto> getAllItemsUser(long userId);

    List<ItemDto> search(String query);

    void delete(long itemId);

    Item getItem(Long itemId);
}

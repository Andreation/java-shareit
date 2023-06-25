package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemRepository {

    List<Item> getAllItemsUser(Long userId);

    Item get(long id);

    List<Item> search(String string);

    Item create(Item item);

    void delete(long userId);

    void deleteAll();

    Item update(long userId, long itemId, Map<String, String> updates);
}
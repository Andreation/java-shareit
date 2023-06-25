package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(long userId, Item item) {
        User owner = userRepository.getUser(userId);
        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(long userId, long itemId, Map<String, String> updates) {
        return ItemMapper.toItemDto(itemRepository.update(userId, itemId, updates));

    }

    @Override
    public ItemDto get(long itemId) {
        return ItemMapper.toItemDto(itemRepository.get(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsUser(long userId) {
        return ItemMapper.toItemDtoList(itemRepository.getAllItemsUser(userId));
    }

    @Override
    public void delete(long itemId) {
        itemRepository.delete(itemId);
    }

    @Override
    public List<ItemDto> search(String string) {
        return itemRepository.search(string).stream().map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}

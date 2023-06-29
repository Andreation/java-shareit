package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found."));
    }

    @Override
    public ItemDto create(long userId, Item item) {
        User owner = getUser(userId);
        item.setOwner(owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(long owner, long itemId, Map<String, String> updates) {
        log.trace("update item..");
        Item item = getItem(itemId);
        if (item.getOwner().getId() != owner) throw new NotFoundException("user not owner Item");
        if (updates.containsKey("name") && !updates.get("name").isBlank()) {
            item.setName(updates.get("name"));
            log.info("name updated");
        }
        if (updates.containsKey("description") && !updates.get("description").isBlank()) {
            item.setDescription(updates.get("description"));
            log.info("description updated");
        }
        if (updates.containsKey("available") && !updates.get("available").isBlank()) {
            item.setAvailable(Boolean.valueOf(updates.get("available")));
            log.info("available updated");
        }
        log.info("update successful {}", itemId);
        return ItemMapper.toItemDto(itemRepository.save(item));

    }

    @Override
    public ItemDto getItemDto(long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found")));
    }

    @Override
    public List<ItemDto> getAllItemsUser(long userId) {
        return ItemMapper.toItemDtoList(itemRepository.findAllByOwnerId(userId));
    }

    @Override
    public void delete(long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDto> search(String string) {
        log.info("search item..");
        return string.isBlank() ? new ArrayList<>() : ItemMapper.toItemDtoList(itemRepository.searchItem(string.toLowerCase()));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }
}

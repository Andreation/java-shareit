package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceDb implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDtoRequest add(Long ownerId, ItemDtoRequest itemDto) {
        User owner = userService.getUser(ownerId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestService.getItemRequest(ownerId, itemDto.getRequestId());
            item.setRequest(itemRequest);
        }
        return ItemMapper.toItemDtoRequest(itemRepository.save(item));
    }

    @Transactional
    @Override
    public ItemDto update(Long ownerId, Long itemId, Map<String, String> updates) {
        Item item = getItemById(itemId);
        if (!getOwnerId(itemId).equals(ownerId)) {
            throw new NotFoundException("user musnt be owner this item");
        }
        if (updates.containsKey("name") && !updates.get("name").isBlank()) {
            String value = updates.get("name");
            item.setName(value);
        }
        if (updates.containsKey("description") && !updates.get("description").isBlank()) {
            String value = updates.get("description");
            item.setDescription(value);
        }
        if (updates.containsKey("available")) {
            item.setAvailable(Boolean.valueOf(updates.get("available")));
        }
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    @Override
    public Long getOwnerId(Long itemId) {
        Item item = getItemById(itemId);
        User owner = item.getOwner();
        if (owner == null) throw new NotFoundException("owner item not found");
        return owner.getId();
    }


    @Transactional(readOnly = true)
    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("item not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemDtoById(Long itemId, Long userId) {
        Item item = getItemById(itemId);
        List<Comment> comments = commentRepository.findAllByItemId(item.getId());
        comments.sort(Comparator.comparing(Comment::getCreated));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (item.getOwner() != null && item.getOwner().getId().equals(userId)) {
            setBookings(itemDto, bookingRepository.findAllByItemIdAndStatus(itemId, BookingStatus.APPROVED, null));
        }
        setComments(itemDto, comments);
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getAllUserItems(Long userId, Long from, Long size) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        Pageable pageable = Pagination.setPageable(from,size);
        List<Booking> bookings = bookingRepository.findAllByOwnerIdAndStatus(userId, BookingStatus.APPROVED, pageable);
        List<Comment> comments = commentRepository.findAllByItemIdIn(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        comments.sort(Comparator.comparing(Comment::getCreated));
        List<ItemDto> listItemDto = ItemMapper.toItemDtoList(items);
        listItemDto.forEach(i -> {
            setBookings(i, bookings);
            setComments(i, comments);
        });
        return listItemDto;
    }

    @Transactional
    @Override
    public void delete(Long ownerId, Long itemId) {
        Item item = getItemById(itemId);
        if (!getOwnerId(itemId).equals(ownerId)) {
            throw new NotFoundException("user not owner");
        }
        itemRepository.delete(item);
    }

    @Override
    public List<ItemDto> searchItems(Long userId, String query, Long from, Long size) {
        Pageable pageable = Pagination.setPageable(from,size);
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        return ItemMapper.toItemDtoList(itemRepository.searchAvailableItems(query, pageable));
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userService.getUser(userId);
        Item item = getItemById(itemId);
        bookingRepository.findFirstByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED,
                        LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("User not booking."));
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toDto(commentRepository.save(comment));
    }
    @Generated
    private void setBookings(ItemDto itemDto, List<Booking> bookings) {
        itemDto.setNextBooking(bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .filter(booking -> booking.getItem().getId().equals(itemDto.getId()))
                .sorted(Comparator.comparing(Booking::getStart))
                .map(BookingMapper::toBookingDto)
                .limit(1)
                .findFirst().orElse(null));
        itemDto.setLastBooking(bookings.stream()
                .filter(booking -> booking.getItem().getId().equals(itemDto.getId()))
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::toBookingDto)
                .limit(1)
                .findFirst().orElse(null));
    }

    @Generated
    private void setComments(ItemDto itemDto, List<Comment> comments) {
        Long itemId = itemDto.getId();
        itemDto.setComments(comments.stream()
                .filter(comment -> comment.getItem().getId().equals(itemId))
                .map(CommentMapper::toDto)
                .collect(Collectors.toList()));
    }
}


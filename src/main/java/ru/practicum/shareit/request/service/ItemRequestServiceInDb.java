package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Qualifier("ItemRequestServiceInDb")
@Primary
public class ItemRequestServiceInDb implements ItemRequestService {

    private final UserService userService;

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequest create(Long userId, ItemRequestDto itemRequestDto) {
        User user = userService.getUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.fromDto(itemRequestDto,user);
        return itemRequestRepository.save(itemRequest);
    }

    public List<ItemRequest> getOwnItemRequest(Long userId) {
        userService.getUser(userId);
        return itemRequestRepository.findByRequester_IdOrderByCreatedAsc(userId);
    }

    public List<ItemRequest> getAllItemRequest(Long userId, Long from, Long size) {
        return itemRequestRepository.findALlByRequesterInOrderByCreatedAsc(userRepository.findByIdNot(userId),
                                                                                Pagination.setPageable(from,size));
    }

    public ItemRequest getItemRequest(Long userId,Long itemRequestId) {
        userService.getUser(userId);
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("request not found"));
    }

}

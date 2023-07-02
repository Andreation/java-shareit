package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest,Long> {
    List<ItemRequest> findALlByRequesterInOrderByCreatedAsc(List<User> users, Pageable pageable);

    List<ItemRequest> findByRequester_IdOrderByCreatedAsc(Long userId);
}

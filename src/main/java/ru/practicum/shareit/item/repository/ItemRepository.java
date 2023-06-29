package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId);

    @Query("select i from Item i " +
            "where i.available=true and " +
            "(LOWER(i.name) like LOWER(concat('%', :text,'%')) or " +
            "LOWER(i.description) like LOWER(concat('%', :text,'%')))"
    )
    List<Item> searchItem(@Param("text") String text);

    void deleteById(long itemId);
}

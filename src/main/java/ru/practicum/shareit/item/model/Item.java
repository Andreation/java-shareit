package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(name = "is_available", nullable = false)
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}

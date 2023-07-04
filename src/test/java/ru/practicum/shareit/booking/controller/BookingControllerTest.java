package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.InputBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    void getBookingDto() {
    }

    @Test
    void getBookingUser() {
    }

    @Test
    void getBookingOwner() {
    }

    @Test
    void create() {
        InputBookingDto bookingIn = InputBookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .itemId(1L)
                .build();

        when(bookingService.create(Mockito.any(InputBookingDto.class), anyLong()))
                .thenAnswer(u -> {
                    InputBookingDto itemMock = u.getArgument(1, InputBookingDto.class);
                    Booking booking = new Booking(new Item(),new User(),itemMock.getStart(),
                            itemMock.getEnd(), BookingStatus.WAITING);
                    booking.setId(1L);
                    return booking;
                });
    }

    @Test
    void update() {
    }
}
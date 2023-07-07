package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private static final String line = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                                @RequestHeader(line) @Positive Long userId) {
        return bookingClient.createBooking(bookingDto, userId);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> considerBooking(@RequestParam Boolean approved, @PathVariable Long bookingId,
                                                  @RequestHeader(line) @Positive Long userId) {
        return bookingClient.considerBooking(approved, bookingId, userId);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable @Positive Long bookingId,
                                             @RequestHeader(line) @Positive Long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestParam(defaultValue = "ALL") String state,
                                                  @RequestHeader(line) @Positive Long userId,
                                                  @RequestParam(required = false) int from,
                                                  @RequestParam(required = false) int size) {
        State bookingState = State.getState(state);
        log.info("Get user bookings with state {}, userId={}, from={}, size={}", bookingState, userId, from, size);
        return bookingClient.getUserBookings(bookingState, userId, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUserItemBookings(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader(line) Long userId,
                                                      @RequestParam(required = false)  int from,
                                                      @RequestParam(required = false)  int size) {
        State bookingState = State.getState(state);
        log.info("Get user item bookings with state {}, userId={}, from={}, size={}", bookingState, userId, from, size);
        return bookingClient.getUserItemBookings(bookingState, userId, from, size);
    }
}
package ru.practicum.shareit.booking.controlers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.InputBookingDto;
import ru.practicum.shareit.booking.model.OutputBookingDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String USER_ID_IN_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public OutputBookingDto getBooking(@RequestHeader(USER_ID_IN_HEADER) Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingDto(bookingId, userId);
    }

    @GetMapping
    public List<OutputBookingDto> getBookingUser(@RequestHeader(USER_ID_IN_HEADER) Long userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingUser(Status.getSearchStatus(state), userId);
    }

    @GetMapping("/owner")
    public List<OutputBookingDto> getBookingOwner(@RequestHeader(USER_ID_IN_HEADER) Long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingOwner(Status.getSearchStatus(state), userId);
    }

    @PostMapping
    public OutputBookingDto create(@RequestHeader(USER_ID_IN_HEADER) long userId,
                                   @Valid @RequestBody InputBookingDto booking) {
        return bookingService.create(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public OutputBookingDto update(@RequestHeader(USER_ID_IN_HEADER) Long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }

}

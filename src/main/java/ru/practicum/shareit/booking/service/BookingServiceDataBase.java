package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceDataBase implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    private Item getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    @Transactional
    public OutputBookingDto create(InputBookingDto bookingDto, long userId) {
        long itemId = bookingDto.getItemId();
        Item item = getItemById(itemId);
        User owner = item.getOwner();
        if (owner == null) {
            throw new NotFoundException("owner not found");
        }
        if (owner.getId() == (userId)) {
            throw new NotFoundException("owner can't be booker");
        }
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (end.isBefore(start) || end.equals(start)) {
            throw new ValidationException(String.format("Wrong booking time start = %s and end = %s", start, end));
        }
        if (!item.getAvailable()) {
            throw new ValidationException(String.format("Item with id: %d is not available!", userId));
        }
        Booking booking = Booking.builder()
                .start(start)
                .end(end)
                .item(item)
                .booker(getUserById(userId))
                .status(BookingStatus.WAITING)
                .build();
        return BookingMapper.toOutputBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public OutputBookingDto approveBooking(long bookingId, long userId, boolean approve) {
        Booking booking = getBooking(bookingId, userId);
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException(String.format("Booking with id: %d already have status %s",
                    bookingId, BookingStatus.APPROVED));
        }
        cheakBooking(booking, userId, false);
        BookingStatus bookingStatus = approve ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(bookingStatus);
        bookingRepository.updateStatus(bookingStatus, bookingId);
        return BookingMapper.toOutputBookingDto(bookingRepository.save(booking));
    }

    @Override
    public Booking getBooking(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking with id: %d not found", bookingId)));
        cheakBooking(booking, userId, true);
        return booking;
    }

    @Override
    public OutputBookingDto getBookingDto(long bookingId, long userId) {
        return BookingMapper.toOutputBookingDto(getBooking(bookingId, userId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutputBookingDto> getBookingUser(Status status, long bookerId) {
        getUserById(bookerId);
        List<Booking> bookings;
        switch (status) {
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.REJECTED);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBefore(bookerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfter(bookerId, LocalDateTime.now());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, LocalDateTime.now());
                break;
            default:
                bookings = bookingRepository.findAllByBookerId(bookerId);
        }
        return BookingMapper.toOutputBookingDtoList(bookings);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutputBookingDto> getBookingOwner(Status status, long ownerId) {
        getUserById(ownerId);
        List<Booking> bookings;
        switch (status) {
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(ownerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED);
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdAndEndBefore(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdAndStartAfter(ownerId, LocalDateTime.now());
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now());
                break;
            default:
                bookings = bookingRepository.findAllByOwnerId(ownerId);
        }
        return BookingMapper.toOutputBookingDtoList(bookings);
    }

    private void cheakBooking(Booking booking, long userId, boolean accessForBooker) {
        User booker = booking.getBooker();
        Item item = booking.getItem();
        User owner = item.getOwner();
        if (booker == null) {
            throw new NotFoundException("booker not found");
        }
        if (item == null) {
            throw new NotFoundException("item not found");
        }
        if (owner == null) {
            throw new NotFoundException("owner not found");
        }
        long bookerId = booker.getId();
        long ownerId = owner.getId();
        if (ownerId == userId) {
            return;
        }
        if (accessForBooker && bookerId == userId) {
            return;
        }
        throw new NotFoundException(String.format("Access to User id:%s for booking id:%s is denied",
                userId, booking.getId()));
    }
}

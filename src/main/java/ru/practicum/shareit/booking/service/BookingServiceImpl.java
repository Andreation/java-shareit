package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Pagination;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public OutputBookingDto create(InputBookingDto bookingDto, Long userId) {
        User user = userService.getUser(userId);
        Item item = itemService.getItemById(bookingDto.getItemId());
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
                bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("time isnt correct");
        }
        if (item.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("booker musnt be owner");
        }
        if (item.getAvailable()) {
            Booking booking = Booking.builder()
                    .start(bookingDto.getStart())
                    .end(bookingDto.getEnd())
                    .item(item)
                    .booker(user)
                    .status(BookingStatus.WAITING)
                    .build();
            return BookingMapper.toOutputBookingDto(bookingRepository.save(booking));
        }  else {
            throw new ValidationException("item not available");
        }
    }

    @Override
    @Transactional
    public OutputBookingDto updateBooking(Long bookingId, Long userId, Boolean approve) {
        Booking booking = getBooking(bookingId, userId);
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("status already APPROVED");
        }
        checkBooking(booking, userId, false);
        booking.setStatus(approve ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.updateStatus(booking.getStatus(), bookingId);
        return BookingMapper.toOutputBookingDto(bookingRepository.save(booking));
    }

    @Override
    public Booking getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking not found"));
        checkBooking(booking, userId, true);
        return booking;
    }

    @Override
    public OutputBookingDto getBookingDto(Long bookingId, Long userId) {
        Booking booking = getBooking(bookingId, userId);
        return BookingMapper.toOutputBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutputBookingDto> getBookingBooker(State state, Long bookerId, Long from, Long size) {
        userService.getUser(bookerId);
        Pageable pageable = Pagination.setPageable(from,size);
        List<Booking> bookings;
        switch (state) {
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(bookerId, LocalDateTime.now(), pageable);
                break;
            default:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId, pageable);
        }
        return BookingMapper.toOutputsBookingDtoList(bookings);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OutputBookingDto> getBookingOwner(State state, Long ownerId, Long from, Long size) {
        userService.getUser(ownerId);
        Pageable pageable = Pagination.setPageable(from,size);
        List<Booking> bookings;
        switch (state) {
            case WAITING:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(ownerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByOwnerIdAndStatus(ownerId, BookingStatus.REJECTED, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findAllByOwnerIdAndEndBefore(ownerId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByOwnerIdAndStartAfter(ownerId, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByOwnerIdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now(), pageable);
                break;
            default:
                bookings = bookingRepository.findAllByOwnerId(ownerId, pageable);
        }
        return BookingMapper.toOutputsBookingDtoList(bookings);
    }

    private void checkBooking(Booking booking, long userId, boolean accessForBooker) {
        User booker = booking.getBooker();
        User owner = booking.getItem().getOwner();
        if (booker == null) {
            throw new NotFoundException("booker not found");
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
        throw new NotFoundException("checkBooking exception");
    }
}

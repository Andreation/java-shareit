package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(long bookerId);

    List<Booking> findAllByBookerIdAndStatus(long bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartAfter(long bookerId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndEndBefore(long bookerId, LocalDateTime end);

    @Modifying
    @Query("UPDATE Booking i SET i.status = :status WHERE i.id = :bookingId")
    void updateStatus(@Param("status") BookingStatus status, @Param("bookingId") Long bookingId);

    @Query(value = "SELECT i FROM Booking i " +
            "WHERE i.booker.id = :bookerId " +
            "AND i.start < :dateTime " +
            "AND i.end > :dateTime " +
            "ORDER BY i.start desc")
    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(@Param("bookerId")long bookerId, @Param("dateTime")LocalDateTime dateTime);

    @Query(value = "SELECT i FROM Booking i " +
            "JOIN FETCH i.item AS i " +
            "JOIN FETCH i.owner AS o " +
            " WHERE o.id = :ownerId AND i.status = :status " +
            "ORDER BY i.start desc")
    List<Booking> findAllByOwnerIdAndStatus(@Param("ownerId") Long ownerId, @Param("status") BookingStatus status);

    @Query(value = "SELECT i FROM Booking i " +
            "JOIN FETCH i.item AS i " +
            "JOIN FETCH i.owner AS o " +
            " WHERE o.id = :ownerId " +
            "ORDER BY i.start desc")
    List<Booking> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query(value = "SELECT i FROM Booking i " +
            "JOIN FETCH i.item AS i " +
            "JOIN FETCH i.owner AS o " +
            " WHERE o.id = :ownerId AND i.start < :dateTime " +
            "AND i.end > :dateTime " +
            "ORDER BY i.start desc")
    List<Booking> findAllByOwnerIdAndStartBeforeAndEndAfter(@Param("ownerId") Long ownerId,
                                                            @Param("dateTime") LocalDateTime dateTime);

    @Query(value = "SELECT i FROM Booking i " +
            "JOIN FETCH i.item AS i " +
            "JOIN FETCH i.owner AS o " +
            " WHERE o.id = :ownerId AND i.start > :dateTime  " +
            "ORDER BY i.start desc")
    List<Booking> findAllByOwnerIdAndStartAfter(@Param("ownerId") Long ownerId,
                                                @Param("dateTime") LocalDateTime dateTime);

    @Query(value = "SELECT i FROM Booking i " +
            "JOIN FETCH i.item AS i " +
            "JOIN FETCH i.owner AS o " +
            " WHERE o.id = :ownerId AND i.end < :dateTime  " +
            "ORDER BY i.start desc")
    List<Booking> findAllByOwnerIdAndEndBefore(@Param("ownerId") Long ownerId,
                                               @Param("dateTime") LocalDateTime dateTime);

}

package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(Long id, Long bookerId, Long bookingId, Long ownerId);

    List<Booking> findAllByBookerIdOrderByEndDesc(Long userId);

    List<Booking> findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByEndDesc(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByBookerIdAndEndLessThanOrderByEndDesc(Long userId, LocalDateTime end);

    List<Booking> findAllByBookerIdAndStartGreaterThanOrderByEndDesc(Long userId, LocalDateTime start);

    List<Booking> findAllByBookerIdAndStatusOrderByEndDesc(Long userId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndEndLessThanOrderByStartDesc(Long userId, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartGreaterThanOrderByStartDesc(Long userId, LocalDateTime start);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status status);

    List<Booking> findAllByItem_IdAndStatusIsNot(Long itemId, Status status);

    List<Booking> findAllByItem_IdInAndStatusIsNot(List<Long> itemIds, Status status);

    Boolean existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
            Long itemId, Long userId, Status status, LocalDateTime currentTime
    );
}

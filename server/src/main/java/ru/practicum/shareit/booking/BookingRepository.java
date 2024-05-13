package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(Long id, Long bookerId, Long bookingId, Long ownerId);

    Page<Booking> findAllByBookerIdOrderByEndDesc(Long userId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByEndDesc(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndLessThanOrderByEndDesc(Long userId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartGreaterThanOrderByEndDesc(Long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusOrderByEndDesc(Long userId, Status status, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStartDesc(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndEndLessThanOrderByStartDesc(Long userId, LocalDateTime end, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStartGreaterThanOrderByStartDesc(Long userId, LocalDateTime start, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status status, Pageable pageable);

    List<Booking> findAllByItem_IdAndStatusIsNot(Long itemId, Status status);

    Page<Booking> findAllByItem_IdInAndStatusIsNot(List<Long> itemIds, Status status, Pageable pageable);

    Boolean existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
            Long itemId, Long userId, Status status, LocalDateTime currentTime
    );
}

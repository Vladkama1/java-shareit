package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    @Test
    void whenFindBookingByIdAndBookerIdOrIdAndItemOwnerId_thenBookingIsFound() {
        Long bookingId = 1L;
        Long bookerId = 1L;
        Long ownerId = 1L;
        Booking booking = new Booking();
        when(bookingRepository.findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(
                bookingId, bookerId, bookingId, ownerId)).thenReturn(Optional.of(booking));

        Optional<Booking> foundBooking = bookingRepository.findBookingByIdAndBooker_IdOrIdAndItem_Owner_Id(
                bookingId, bookerId, bookingId, ownerId);

        assertTrue(foundBooking.isPresent());
        assertEquals(booking, foundBooking.get());
    }

    @Test
    void whenFindAllByBookerIdOrderByEndDesc_thenBookingsAreFound() {
        Long bookerId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        List<Booking> bookings = Collections.singletonList(new Booking());
        Page<Booking> bookingPage = new PageImpl<>(bookings, pageable, bookings.size());
        when(bookingRepository.findAllByBookerIdOrderByEndDesc(bookerId, pageable)).thenReturn(bookingPage);

        Page<Booking> foundBookings = bookingRepository.findAllByBookerIdOrderByEndDesc(bookerId, pageable);

        assertFalse(foundBookings.isEmpty());
        assertEquals(bookings.size(), foundBookings.getContent().size());
    }

    @Test
    void whenFindAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqual_thenBookingsAreFound() {
        Long bookerId = 1L;
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        PageRequest pageable = PageRequest.of(0, 10);
        List<Booking> bookings = Collections.singletonList(new Booking());
        Page<Booking> bookingPage = new PageImpl<>(bookings, pageable, bookings.size());
        when(bookingRepository.findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByEndDesc(
                bookerId, start, end, pageable)).thenReturn(bookingPage);

        Page<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByEndDesc(
                bookerId, start, end, pageable);

        assertFalse(foundBookings.isEmpty());
        assertEquals(bookings.size(), foundBookings.getContent().size());
    }

    @Test
    void whenExistsByItem_IdAndBooker_IdAndStatusAndEndIsBefore_thenCorrectResultReturned() {
        Long itemId = 1L;
        Long bookerId = 1L;
        Status status = Status.APPROVED;
        LocalDateTime currentTime = LocalDateTime.now();
        when(bookingRepository.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                itemId, bookerId, status, currentTime)).thenReturn(true);

        Boolean exists = bookingRepository.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                itemId, bookerId, status, currentTime);

        assertTrue(exists);
    }

    @Test
    void whenFindAllByBookerIdAndEndLessThan_thenBookingsAreFound() {
        Long bookerId = 1L;
        LocalDateTime end = LocalDateTime.now();
        PageRequest pageable = PageRequest.of(0, 10);
        List<Booking> bookings = Collections.singletonList(new Booking());
        Page<Booking> bookingPage = new PageImpl<>(bookings, pageable, bookings.size());
        when(bookingRepository.findAllByBookerIdAndEndLessThanOrderByEndDesc(bookerId, end, pageable)).thenReturn(bookingPage);

        Page<Booking> foundBookings = bookingRepository.findAllByBookerIdAndEndLessThanOrderByEndDesc(bookerId, end, pageable);

        assertFalse(foundBookings.isEmpty());
        assertEquals(bookings.size(), foundBookings.getContent().size());
    }

    @Test
    void whenFindAllByBookerIdAndStartGreaterThan_thenBookingsAreFound() {
        Long bookerId = 1L;
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        PageRequest pageable = PageRequest.of(0, 10);
        List<Booking> bookings = Collections.singletonList(new Booking());
        Page<Booking> bookingPage = new PageImpl<>(bookings, pageable, bookings.size());
        when(bookingRepository.findAllByBookerIdAndStartGreaterThanOrderByEndDesc(bookerId, start, pageable)).thenReturn(bookingPage);

        Page<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStartGreaterThanOrderByEndDesc(bookerId, start, pageable);

        assertFalse(foundBookings.isEmpty());
        assertEquals(bookings.size(), foundBookings.getContent().size());
    }

    @Test
    void whenFindAllByBookerIdAndStatus_thenBookingsAreFound() {
        Long bookerId = 1L;
        Status status = Status.APPROVED;
        PageRequest pageable = PageRequest.of(0, 10);
        List<Booking> bookings = Collections.singletonList(new Booking());
        Page<Booking> bookingPage = new PageImpl<>(bookings, pageable, bookings.size());
        when(bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(bookerId, status, pageable)).thenReturn(bookingPage);

        Page<Booking> foundBookings = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(bookerId, status, pageable);

        assertFalse(foundBookings.isEmpty());
        assertEquals(bookings.size(), foundBookings.getContent().size());
    }

    @Test
    void whenFindAllByItemOwnerIdOrderByStartDesc_thenBookingsAreFound() {
        Long ownerId = 1L;
        PageRequest pageable = PageRequest.of(0, 10);
        List<Booking> bookings = Collections.singletonList(new Booking());
        Page<Booking> bookingPage = new PageImpl<>(bookings, pageable, bookings.size());
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId, pageable)).thenReturn(bookingPage);

        Page<Booking> foundBookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId, pageable);

        assertFalse(foundBookings.isEmpty());
        assertEquals(bookings.size(), foundBookings.getContent().size());
    }
}
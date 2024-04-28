package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {
    BookingOutDto findById(Long userId, Long bookingId);

    BookingOutDto saveBooking(Long userId, BookingDto bookingDto);

    BookingOutDto updateBooking(Long userId, Long id, Boolean approved);

    List<BookingOutDto> findByBookerAndState(Long userId, String state);

    List<BookingOutDto> findByOwnerAndState(Long userId, String state);
}

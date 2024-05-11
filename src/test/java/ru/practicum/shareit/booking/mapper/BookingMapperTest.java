package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {
    private BookingMapper bookingMapper;

    @BeforeEach
    void setup() {
        bookingMapper = Mappers.getMapper(BookingMapper.class);
    }

    @Test
    void shouldMapBookingDtoToBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBookerId(2L);
        bookingDto.setItemId(3L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingDto.setStatus(Status.APPROVED);

        Booking booking = bookingMapper.toModel(bookingDto);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getStart(), booking.getStart());
        assertEquals(bookingDto.getEnd(), booking.getEnd());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
        assertNotNull(booking.getBooker());
        assertEquals(bookingDto.getBookerId(), booking.getBooker().getId());
        assertNotNull(booking.getItem());
        assertEquals(bookingDto.getItemId(), booking.getItem().getId());
    }

    @Test
    void shouldMapBookingToBookingDto() {
        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(3L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(Status.APPROVED);

        BookingDto bookingDto = bookingMapper.toDTO(booking);

        assertNotNull(bookingDto);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
        assertEquals(booker.getId(), bookingDto.getBookerId());
        assertEquals(item.getId(), bookingDto.getItemId());
    }

    @Test
    void shouldMapBookingToBookingOutDto() {
        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(3L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus(Status.APPROVED);

        BookingOutDto bookingOutDto = bookingMapper.toOutDTO(booking);

        assertNotNull(bookingOutDto);
        assertEquals(booking.getId(), bookingOutDto.getId());
        assertEquals(booking.getStart(), bookingOutDto.getStart());
        assertEquals(booking.getEnd(), bookingOutDto.getEnd());
        assertEquals(booking.getStatus(), bookingOutDto.getStatus());
    }

    @Test
    void shouldMapBookingListToBookingOutDtoList() {
        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(3L);

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setBooker(booker);
        booking1.setItem(item);
        booking1.setStart(LocalDateTime.now());
        booking1.setEnd(LocalDateTime.now().plusDays(1));
        booking1.setStatus(Status.APPROVED);

        Booking booking2 = new Booking();
        booking2.setId(4L);
        booking2.setBooker(booker);
        booking2.setItem(item);
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(LocalDateTime.now().plusDays(1));
        booking2.setStatus(Status.REJECTED);

        List<Booking> bookings = Arrays.asList(booking1, booking2);

        List<BookingOutDto> bookingOutDtos = bookingMapper.toListOutDTO(bookings);

        assertNotNull(bookingOutDtos);
        assertEquals(2, bookingOutDtos.size());
        assertEquals(bookings.get(0).getId(), bookingOutDtos.get(0).getId());
        assertEquals(bookings.get(1).getId(), bookingOutDtos.get(1).getId());
    }

    @Test
    void shouldMapBookingDtoToBooking1() {
        Booking booking = bookingMapper.toModel(null);
        assertNull(booking);
    }

    @Test
    void shouldMapBookingDtoToBooking2() {
        BookingDto bookingDto = bookingMapper.toDTO(null);
        assertNull(bookingDto);
    }

    @Test
    void shouldMapBookingDtoToBooking3() {
        BookingOutDto bookingOutDto = bookingMapper.toOutDTO(null);
        assertNull(bookingOutDto);
    }

    @Test
    void shouldMapBookingDtoToBooking4() {
        List<BookingOutDto> bookingOutDtos = bookingMapper.toListOutDTO(null);
        assertNull(bookingOutDtos);
    }
}
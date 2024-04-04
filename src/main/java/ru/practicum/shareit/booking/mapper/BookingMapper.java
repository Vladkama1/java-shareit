package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toModel(BookingDto bookingDto);

    BookingDto toDTO(Booking booking);

    List<Booking> toListModels(List<BookingDto> bookingDtoList);

    List<BookingDto> toListDTO(List<Booking> bookingList);
}

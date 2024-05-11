package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingShortOutDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mappings({
            @Mapping(source = "bookerId", target = "booker.id"),
            @Mapping(source = "itemId", target = "item.id")
    })
    Booking toModel(BookingDto bookingDto);

    @Mappings({
            @Mapping(source = "booker.id", target = "bookerId"),
            @Mapping(source = "item.id", target = "itemId")
    })
    BookingDto toDTO(Booking booking);

    BookingOutDto toOutDTO(Booking booking);

    @Mapping(source = "booker.id", target = "bookerId")
    BookingShortOutDto toShortOutDTO(BookingOutDto bookingOutDto);

    List<BookingOutDto> toListOutDTO(List<Booking> bookingList);
}

package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.user.dto.UserOutDto;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookingOutDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemOutDto item;
    private UserOutDto booker;
    private Status status;
}

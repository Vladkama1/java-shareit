package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDto {
    @Null
    @NonNull
    private Long bookingId;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    @NotBlank(message = "Booker can`t null.")
    private String booker;
    @NotNull(message = "IsPositive can`t null!")
    private boolean status;
}

package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder(toBuilder = true)
public class Booking {
    private Long bookingId;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private String booker;
    private boolean status;
}

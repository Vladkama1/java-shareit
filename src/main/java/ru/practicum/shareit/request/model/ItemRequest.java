package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder(toBuilder = true)
public class ItemRequest {
    private Long itemRequestId;
    private String description;
    private String request;
    private LocalDateTime created;
}

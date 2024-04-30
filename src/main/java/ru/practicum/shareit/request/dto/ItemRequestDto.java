package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequestDto {
    @Null
    @NonNull
    private Long itemRequestId;
    @NotBlank(message = "Description can`t null.")
    private String description;
    @NotBlank(message = "Request can`t null.")
    private String request;
    private LocalDateTime created;
}

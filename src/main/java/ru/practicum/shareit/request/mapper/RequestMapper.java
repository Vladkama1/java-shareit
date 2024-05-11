package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestDto toDTO(Request request);
}

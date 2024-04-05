package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    ItemRequest toModel(ItemRequestDto itemRequestDto);

    ItemRequestDto toDTO(ItemRequest itemRequest);

    List<ItemRequest> toListModels(List<ItemRequestDto> itemRequestDtoList);

    List<ItemRequestDto> toListDTO(List<ItemRequest> itemRequestList);
}

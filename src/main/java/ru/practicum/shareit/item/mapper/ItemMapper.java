package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    Item toModel(ItemDto itemDto);

    @Mapping(source = "owner.id", target = "ownerId")
    ItemDto toDTO(Item item);

    ItemOutDto toOutDTO(Item item);

    List<Item> toListModels(List<ItemDto> itemDtoList);

    List<ItemDto> toListDTO(List<Item> itemList);

    List<ItemOutDto> toListOutDTO(List<Item> itemList);
}

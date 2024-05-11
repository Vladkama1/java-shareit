package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    Item updateToModel(ItemDto itemDto);

    default Item toModel(ItemDto itemDto) {
        Item item = updateToModel(itemDto);
        if (itemDto.getRequestId() != null) {
            item.setRequest(Request.builder().id(itemDto.getRequestId()).build());
        } else {
            item.setRequest(null);
        }
        return item;
    }

    @Mappings({
            @Mapping(source = "owner.id", target = "ownerId"),
            @Mapping(source = "request.id", target = "requestId")
    })
    ItemDto toDTO(Item item);

    @Mapping(source = "request.id", target = "requestId")
    ItemOutDto toOutDTO(Item item);

    List<ItemOutDto> toListOutDTO(List<Item> itemList);
}


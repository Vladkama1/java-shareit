package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;

import java.util.List;

public interface ItemService {
    ItemOutDto findById(Long userId, Long itemId);

    List<ItemOutDto> getAllItemsOwner(Long ownerId,Integer from, Integer size);

    ItemOutDto saveItem(ItemDto itemDto, Long userId);

    ItemOutDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> searchBy(String text,Integer from, Integer size);

    CommentOutDto addComment(Long userId, Long itemId, CommentDto requestDto);
}

package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto findById(Long id) {
        return itemMapper.toDTO(itemStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Данный товар отсутствует!")));
    }

    @Override
    public List<ItemDto> getAllItemsOwners(Long ownerId) {
        isExistUserInDb(ownerId);
        return itemMapper.toListDTO(itemStorage.getAllItemsOwners(ownerId));
    }

    @Override
    public ItemDto saveItem(ItemDto itemDto, Long userId) {
        isExistUserInDb(userId);
        if (itemDto.getName() == null || itemDto.getName().isEmpty() || itemDto.getDescription() == null ||
                itemDto.getAvailable() == null) {
            throw new BadRequestException("Invalid data for item");
        }
        itemDto.setOwnerId(userId);
        return itemMapper.toDTO(itemStorage.saveItem(itemMapper.toModel(itemDto)));
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        isExistUserInDb(userId);
        Item item = itemMapper.toModel(findById(itemId));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Дынный пользователь не является владельцем товара!");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemStorage.updateItem(item);
        return itemMapper.toDTO(item);
    }

    @Override
    public List<ItemDto> searchBy(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemStorage.searchBy(text.toLowerCase())
                .stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void isExistUserInDb(Long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Данный пользователь отсутствует в базе!");
        }
    }
}

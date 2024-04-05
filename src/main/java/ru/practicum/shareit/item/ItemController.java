package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId) {
        log.info("Получен запрос GET, на получения товара, по id: {}", itemId);
        return itemService.findById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsOwners(@RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Получен запрос GET, на получения всех предметов.");
        List<ItemDto> itemDtoList = itemService.getAllItemsOwners(ownerId);
        log.info("Получен ответ, список товаров, размер: {}", itemDtoList.size());
        return itemDtoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto saveItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestBody ItemDto itemDto) {
        log.info("Получен запрос Post, на обновление данных товара.");
        log.info("Добавлен товар: {}", itemDto.getName());
        return itemService.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос Put, на обновление товара");
        ItemDto itemDto1 = itemService.updateItem(userId, itemId, itemDto);
        log.info("Обновлён пользователь: {}", itemDto1.getName());
        return itemDto1;
    }

    @GetMapping("/search")
    public List<ItemDto> searchBy(@RequestParam String text) {
        log.info("Получен запрос на поиск всех вещей по тексту: " + text);
        List<ItemDto> items = itemService.searchBy(text);
        log.info("Отработан запрос на поиск всех вещей по тексту: " + text);
        return items;
    }
}

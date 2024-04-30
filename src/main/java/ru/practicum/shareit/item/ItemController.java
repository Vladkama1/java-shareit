package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemOutDto findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                               @PathVariable Long itemId) {
        log.info("Получен запрос GET, на получения товара, по id: {}", itemId);
        return itemService.findById(userId, itemId);
    }

    @GetMapping
    public List<ItemOutDto> getAllItemsOwners(@RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Получен запрос GET, на получения всех предметов.");
        List<ItemOutDto> itemDtoList = itemService.getAllItemsOwner(ownerId);
        log.info("Получен ответ, список товаров, размер: {}", itemDtoList.size());
        return itemDtoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemOutDto saveItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос Post, на обновление данных товара.");
        log.info("Добавлен товар: {}", itemDto.getName());
        return itemService.saveItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemOutDto updateItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody ItemDto itemDto) {
        log.info("Получен запрос Put, на обновление товара");
        ItemOutDto itemOutDto = itemService.updateItem(userId, itemId, itemDto);
        log.info("Обновлён пользователь: {}", itemOutDto.getName());
        return itemOutDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchBy(@RequestParam String text) {
        log.info("Получен запрос на поиск всех вещей по тексту: " + text);
        List<ItemDto> items = itemService.searchBy(text);
        log.info("Отработан запрос на поиск всех вещей по тексту: " + text);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public CommentOutDto addComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                    @PathVariable Long itemId,
                                    @Valid @RequestBody CommentDto requestDto) {
        log.info("Получен запрос на создание комментариев вещей под id - ", itemId);
        return itemService.addComment(userId, itemId, requestDto);
    }
}

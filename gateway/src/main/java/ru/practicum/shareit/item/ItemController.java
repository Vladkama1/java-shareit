package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.constant.ItemConstants.X_SHARER_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @PathVariable Long itemId) {
        log.info("Получен запрос GET, на получения товара, по id: {}", itemId);
        return itemClient.findById(userId, itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос Post, на обновление данных товара.");
        log.info("Добавлен товар: {}", itemDto.getName());
        return itemClient.saveItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Получен запрос Put, на обновление товара");
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsOwners(@RequestHeader(X_SHARER_USER_ID) Long ownerId,
                                                    @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                                    @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Получен запрос GET, на получения всех предметов.");
        return itemClient.getAllItemsOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchBy(@RequestParam String text,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Получен запрос на поиск всех вещей по тексту: " + text);
        ResponseEntity<Object> items = itemClient.searchBy(text, from, size);
        log.info("Отработан запрос на поиск всех вещей по тексту: " + text);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto requestDto) {
        log.info("Получен запрос на создание комментариев вещей под id - ", itemId);
        return itemClient.addComment(userId, itemId, requestDto);
    }
}

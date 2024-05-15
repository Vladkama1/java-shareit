package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.constant.ItemConstants.X_SHARER_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @Valid @RequestBody RequestDto requestDto) {
        log.info("Получен запрос Post, на создание запроса.");
        return requestClient.saveRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByRequest(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Получен запрос GET, на получения всех запросов.");
        ResponseEntity<Object> requestDtoList = requestClient.findAllByRequest(userId);
        return requestDtoList;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findByAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                            @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                            @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Получен запрос GET, на получения всех запросов.");
        ResponseEntity<Object> requestDtoList = requestClient.findByAll(userId, from, size);
        return requestDtoList;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                           @PathVariable Long requestId) {
        log.info("Получен запрос GET, на получения запроса.");
        return requestClient.findById(userId, requestId);
    }
}

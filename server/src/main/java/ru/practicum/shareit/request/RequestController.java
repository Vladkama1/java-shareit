package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto saveItem(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestBody RequestDto requestDto) {
        log.info("Получен запрос Post, на создание запроса.");
        return requestService.saveRequest(requestDto, userId);
    }

    @GetMapping
    public List<RequestDto> findAllByRequest(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Получен запрос GET, на получения всех запросов.");
        List<RequestDto> requestDtoList = requestService.findAllByRequest(userId);
        log.info("Получен ответ, список запросов, размер: {}", requestDtoList.size());
        return requestDtoList;
    }

    @GetMapping("/all")
    public List<RequestDto> findByAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                      @RequestParam Integer from,
                                      @RequestParam Integer size) {
        log.info("Получен запрос GET, на получения всех запросов.");
        List<RequestDto> requestDtoList = requestService.findByAll(userId, from, size);
        log.info("Получен ответ, список запросов, размер: {}", requestDtoList.size());
        return requestDtoList;
    }

    @GetMapping("/{requestId}")
    public RequestDto findById(@RequestHeader(X_SHARER_USER_ID) Long userId,
                               @PathVariable Long requestId) {
        log.info("Получен запрос GET, на получения запроса.");
        return requestService.findById(userId, requestId);
    }
}

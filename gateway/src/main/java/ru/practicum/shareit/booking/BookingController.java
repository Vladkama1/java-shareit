package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.constant.ItemConstants.X_SHARER_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long bookingId) {
        log.info("Получен запрос GET, на получения заказа, по bookingId: {}", bookingId);
        return bookingClient.findById(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> saveBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                              @Valid @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос Post, на обновление данных о заказах.");
        return bookingClient.saveBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam Boolean approved) {
        log.info("Получен запрос на обновление заказа по id: {}", bookingId);
        return bookingClient.updateBooking(userId, bookingId, approved);

    }

    @GetMapping
    public ResponseEntity<Object> findByBookerAndState(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state,
                                                       @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                                       @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Получен запрос GET, на получение списка всех бронирований текущего пользователя: {}", userId);
        return bookingClient.findByBookerAndState(userId, validState(state), from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findByOwnerAndState(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                      @RequestParam(defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") @Min(value = 0) Integer from,
                                                      @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Получен запрос GET, на получение списка бронирований " +
                "для всех вещей текущего пользователя: {}", userId);
        ResponseEntity<Object> bookingOutDtoList = bookingClient.findByOwnerAndState(userId, validState(state), from, size);
        return bookingOutDtoList;
    }

    private State validState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(String.format("Unknown state: %s", state));
        }
    }
}

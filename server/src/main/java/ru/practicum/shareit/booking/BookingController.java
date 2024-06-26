package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.enums.State;

import java.util.List;

import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingOutDto findById(@RequestHeader(X_SHARER_USER_ID) Long userId, @PathVariable Long bookingId) {
        log.info("Получен запрос GET, на получения заказа, по bookingId: {}", bookingId);
        return bookingService.findById(userId, bookingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingOutDto saveBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                     @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос Post, на обновление данных о заказах.");
        return bookingService.saveBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto updateBooking(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                       @PathVariable Long bookingId,
                                       @RequestParam Boolean approved) {
        log.info("Получен запрос на обновление заказа по id: {}", bookingId);
        return bookingService.updateBooking(userId, bookingId, approved);

    }

    @GetMapping
    public List<BookingOutDto> findByBookerAndState(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                    @RequestParam State state,
                                                    @RequestParam Integer from,
                                                    @RequestParam Integer size) {
        log.info("Получен запрос GET, на получение списка всех бронирований текущего пользователя: {}", userId);
        return bookingService.findByBookerAndState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> findByOwnerAndState(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                   @RequestParam State state,
                                                   @RequestParam Integer from,
                                                   @RequestParam Integer size) {
        log.info("Получен запрос GET, на получение списка бронирований " +
                "для всех вещей текущего пользователя: {}", userId);
        List<BookingOutDto> bookingOutDtoList = bookingService.findByOwnerAndState(userId, state, from, size);
        return bookingOutDtoList;
    }
}

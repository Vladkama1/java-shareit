package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingShortOutDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private UserMapper userMapper;

    ObjectMapper objectMapper;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingDto bookingDto;
    private Booking booking;
    private User user;
    private Item item;
    private User user2;
    private UserOutDto userOutDto;
    private Item item2;
    private Item item3;
    private ItemDto itemDto;
    private UserDto userDto;
    private ItemOutDto itemOutDto;

    private BookingOutDto bookingOutDto;
    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("name")
                .email("name@name.ru")
                .build();

        userOutDto = UserOutDto.builder()
                .name("name")
                .email("name@name.ru")
                .build();

        item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .request(null)
                .build();

        item2 = Item.builder()
                .name("name2")
                .description("description2")
                .available(true)
                .owner(user2)
                .request(null)
                .build();

        item3 = Item.builder()
                .name("name3")
                .description("description3")
                .available(true)
                .owner(user)
                .request(null)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .ownerId(1L)
                .build();
        BookingShortOutDto lastBooking = BookingShortOutDto.builder()
                .id(1L)
                .bookerId(1L)
                .start(now.plusHours(1))
                .end(now.plusHours(12))
                .build();

        BookingShortOutDto nextBooking = BookingShortOutDto.builder()
                .id(1L)
                .bookerId(1L)
                .start(now.plusHours(1))
                .end(now.plusHours(12))
                .build();
        CommentOutDto commentOutDto = CommentOutDto.builder()
                .id(1L)
                .text("text")
                .authorName("authorName")
                .created(now.plusHours(1))
                .itemId(1L)
                .build();

        itemOutDto = ItemOutDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(List.of(commentOutDto))
                .build();
        booking = Booking.builder()
                .id(1L)
                .start(now.plusHours(1))
                .end(now.plusHours(12))
                .item(item)
                .status(Status.WAITING)
                .booker(user)
                .build();

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(now.plusHours(1))
                .end(now.plusHours(12))
                .build();

        bookingOutDto = BookingOutDto.builder()
                .id(1L)
                .start(now.plusHours(1))
                .end(now.plusHours(12))
                .item(itemOutDto)
                .booker(userMapper.toOutDTO(user))
                .status(Status.WAITING)
                .build();
    }

    @Test
    public void saveBooking_ItemNotAvailable_ThrowsBadRequestException() {
        Long userId = 1L;
        ItemOutDto itemOutDto = ItemOutDto.builder()
                .available(false)
                .build();
        Item item = Item.builder()
                .available(false)
                .build();
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));
        when(itemMapper.toOutDTO(item)).thenReturn(itemOutDto);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toOutDTO(user)).thenReturn(userOutDto);

        assertThrows(BadRequestException.class, () -> bookingService.saveBooking(userId, bookingDto));
    }

    @Test
    public void saveBooking_ThrowsNotFoundException() {
        Long userId = 1L;
        ItemOutDto itemOutDto = ItemOutDto.builder()
                .available(true)
                .build();
        Item item = Item.builder()
                .available(true)
                .build();
        when(itemRepository.findById(bookingDto.getItemId())).thenReturn(Optional.of(item));
        when(itemMapper.toOutDTO(item)).thenReturn(itemOutDto);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toOutDTO(user)).thenReturn(userOutDto);
        when(itemRepository.existsByIdAndOwner_Id(bookingDto.getItemId(), userId)).thenReturn(true);

        assertThrows(NotFoundException.class, () -> bookingService.saveBooking(userId, bookingDto));
    }

    @Test
    public void updateBookingTest() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = true;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        user.setId(userId);
        booking.setStatus(Status.APPROVED);
        assertThrows(BadRequestException.class, () -> bookingService.updateBooking(userId, bookingId, approved));
    }

    @Test
    public void updateBookingTest1() {
        Long userId = 1L;
        Long bookingId = 1L;
        Boolean approved = false;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        user.setId(userId);
        booking.setStatus(Status.REJECTED);
        assertThrows(BadRequestException.class, () -> bookingService.updateBooking(userId, bookingId, approved));
    }

    @Test
    public void saveBooking_ThrowsBadRequestException() {
        Long userId = 1L;
        bookingDto = BookingDto.builder()
                .start(now.plusHours(12))
                .end(now)
                .build();
        assertThrows(BadRequestException.class, () -> bookingService.saveBooking(userId, bookingDto));
    }

    @Test
    public void saveBooking_ThrowsBadRequestException1() {
        Long userId = 1L;
        bookingDto = BookingDto.builder()
                .start(now.plusHours(12))
                .end(now.plusHours(12))
                .build();
        assertThrows(BadRequestException.class, () -> bookingService.saveBooking(userId, bookingDto));
    }
}
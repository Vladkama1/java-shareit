package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingShortOutDto;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatus;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {
    private final Long userId1 = 1L;
    private final Long userId2 = 2L;
    private final Long bookingId1 = 1L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;
    private final LocalDateTime now1 = LocalDateTime.now();

    private final LocalDateTime startTime = now1.plusHours(1);
    private final LocalDateTime endTime = now1.plusHours(2);

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    private User user;
    private User user2;
    private Item item;
    private Item item2;
    private Item item3;
    private ItemDto itemDto;
    private ItemOutDto itemOutDto;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingOutDto bookingOutDto;
    private LocalDateTime now = LocalDateTime.now();

    public void initData() {
        userRepository.save(user);
        userRepository.save(user2);
        itemRepository.save(item);
        itemRepository.save(item2);
        itemRepository.save(item3);
    }

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        user = User.builder()
                .name("name")
                .email("name@name.ru")
                .build();

        user2 = User.builder()
                .name("name2")
                .email("name2@name.ru")
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

    @Order(0)
    @SneakyThrows
    @Test
    void saveBookingTest() {
        initData();
        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, 2)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(bookingOutDto.getId()), Long.class));
    }

    @Order(1)
    @SneakyThrows
    @Test
    void updateBookingTrueTest() {
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(X_SHARER_USER_ID, invalidId)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Order(2)
    @SneakyThrows
    @Test
    void updateBookingFalseTest() {
        String result = mockMvc.perform(patch("/bookings/{bookingId}?approved=false", 1L)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(3)
    @SneakyThrows
    public void testUpdateBooking_WithInvalidBookingId_ResulStatusNotFound() {
        mockMvc.perform(patch("/bookings/{bookingId}", invalidId)
                        .header(X_SHARER_USER_ID, userId1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(4)
    @SneakyThrows
    public void testUpdateBooking_WithUserNotItemOwner_ResulStatusNotFound() {
        mockMvc.perform(patch("/bookings/{bookingId}", 505)
                        .header(X_SHARER_USER_ID, userId1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(5)
    @SneakyThrows
    public void testUpdateBooking_WithRejectedAfterApproved_ResulStatusBadRequest() {
        mockMvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(X_SHARER_USER_ID, userId2)
                        .param("approved", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Order(6)
    @SneakyThrows
    @Test
    void findByIdTest() {
        mockMvc.perform(get("/bookings/{bookingId}", bookingId1)
                        .header(X_SHARER_USER_ID, userId1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(bookingOutDto.getId()), Long.class))
                .andReturn();
    }

    @Test
    @Order(7)
    @SneakyThrows
    public void testGetAllBookings_WithUserBookerAndStateAll_ResulStatusOk() {
        bookingOutDto = BookingOutDto.builder()
                .id(bookingId1)
                .build();
        List<BookingOutDto> bookings = Collections.singletonList(bookingOutDto);

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(8)
    @SneakyThrows
    public void testGetAllBookings_WithUserBookerAndStateCurrent_ResulStatusOk() {
        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .param("state", "CURRENT")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(9)
    @SneakyThrows
    public void testGetAllBookings_WithUserBookerAndStatePast_ResulStatusOk() {
        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .param("state", "PAST")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(10)
    @SneakyThrows
    public void testGetAllBookings_WithUserBookerAndStateFuture_ResulStatusOk() {

        bookingOutDto = BookingOutDto.builder()
                .id(bookingId1)
                .build();
        List<BookingOutDto> bookings = Collections.singletonList(bookingOutDto);

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .param("state", "FUTURE")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(11)
    @SneakyThrows
    public void testGetAllBookings_WithUserBookerAndStateWaiting_ResulStatusOk() {
        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .param("state", "WAITING")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(12)
    @SneakyThrows
    public void testGetAllBookings_WithUserBookerAndStateRejected_ResulStatusOk() {
        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .param("state", "REJECTED")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(13)
    @SneakyThrows
    public void testGetAllBookings_WithUserBookerAndStateUnknown_ResulStatusBadRequest() {

        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .param("state", "Unknown")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(14)
    @SneakyThrows
    public void testGetBookingById_WithInvalidBookingId_ResulStatusNotFound() {
        mockMvc.perform(get("/bookings/{bookingId}", invalidId)
                        .header(X_SHARER_USER_ID, userId1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }


    @Test
    @Order(15)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwner_ResulStatusOk() {
        bookingOutDto = BookingOutDto.builder()
                .id(bookingId1)
                .build();
        List<BookingOutDto> bookings = Collections.singletonList(bookingOutDto);

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(16)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwnerAndStateCurrent_ResulStatusOk() {
        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("state", "CURRENT")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(17)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwnerAndStatePast_ResulStatusOk() {
        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("state", "PAST")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(18)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwnerAndStateFuture_ResulStatusOk() {
        bookingOutDto = BookingOutDto.builder()
                .id(bookingId1)
                .build();
        List<BookingOutDto> bookings = Collections.singletonList(bookingOutDto);

        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("state", "FUTURE")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(19)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwnerAndStateWaiting_ResulStatusOk() {
        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("state", "WAITING")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(20)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwnerAndStateRejected_ResulStatusOk() {
        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("state", "REJECTED")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(21)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwnerAndStateUnknown_ResulStatusBadRequest() {
        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("state", "Unknown")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UnsupportedStatus.class));
    }

    @Test
    @Order(22)
    @SneakyThrows
    public void testGetAllBookings_WithUserItemOwnerInvalidId_ResulStatusNotFound() {
        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, invalidId)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(23)
    @SneakyThrows
    public void testCreateBooking_WithUserItemBooker_ResulStatusNotFound() {
        bookingOutDto = BookingOutDto.builder()
                .start(startTime)
                .end(endTime)
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, userId1)
                        .content(objectMapper.writeValueAsString(bookingOutDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));
    }

    @Test
    @Order(24)
    @SneakyThrows
    public void testCreateBooking_WithItemIdInvalid_ResulStatusNotFound() {
        bookingOutDto = BookingOutDto.builder()
                .start(startTime)
                .end(endTime)
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, userId2)
                        .content(objectMapper.writeValueAsString(bookingOutDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));
    }

    @Test
    @Order(25)
    @SneakyThrows
    public void testCreateBooking_WithStartAfterEndTime_ResulStatusBadRequest() {
        bookingOutDto = BookingOutDto.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, userId2)
                        .content(objectMapper.writeValueAsString(bookingOutDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));
    }

    @Test
    @Order(26)
    @SneakyThrows
    public void testCreateBooking_WithStartEqualsEndTime_ResulStatusBadRequest() {
        LocalDateTime startAndEndTime = LocalDateTime.now().plusDays(1);
        bookingOutDto = BookingOutDto.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, userId2)
                        .content(objectMapper.writeValueAsString(bookingOutDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));
    }

    @Test
    @Order(27)
    @SneakyThrows
    public void testCreateBooking_WithItemStatusNotAvailable_ResulStatusBadRequest() {
        bookingOutDto = BookingOutDto.builder()
                .start(startTime)
                .end(endTime)
                .build();

        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, userId2)
                        .content(objectMapper.writeValueAsString(bookingOutDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));
    }
}
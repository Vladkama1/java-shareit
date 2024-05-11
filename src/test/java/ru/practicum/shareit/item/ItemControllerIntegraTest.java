package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ItemControllerIntegraTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private BookingService bookingService;

    private ItemDto itemDto;

    private ItemOutDto itemOutDto;
    private User user1;
    private User user2;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Long itemId1 = 1L;
    private final Long userId1 = 1L;
    private final Long userId2 = 2L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;

    public void init() {
        user1 = User.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        user2 = User.builder()
                .email("comcom@gmail.com")
                .name("ComCom")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @BeforeEach
    public void setUp() {

        itemDto = ItemDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        itemOutDto = ItemOutDto.builder()
                .id(itemId1)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .comments(new ArrayList<>())
                .build();
    }

    @Test
    @Order(0)
    @SneakyThrows
    public void testCreateItem_ReturnsStatusCreated() {
        init();
        mvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", Matchers.is(itemOutDto.getId()), Long.class));
    }

    @Test
    @Order(1)
    @SneakyThrows
    public void testCreateItem_WithInvalidUserId_ReturnsStatusNotFound() {
        mvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, invalidId)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(2)
    @SneakyThrows
    public void testUpdateItem1_WithInvalidItemId_ReturnsStatusNotFound() {
        itemDto.toBuilder().name("Дрель--").build();

        mvc.perform(patch("/items/{itemId}", invalidId)
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(3)
    @SneakyThrows
    public void testCreateItem2_WithUser2_ReturnsStatusCreated() {
        itemDto = itemDto.toBuilder()
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();
        itemOutDto = itemOutDto.toBuilder()
                .id(3L)
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(4)
    @SneakyThrows
    public void testCreateItem3_WithUser2_ReturnsStatusCreated() {
        itemDto = itemDto.toBuilder()
                .name("Телевизор")
                .description("Телевизор 40 дюймов.")
                .available(true)
                .build();
        itemOutDto = itemOutDto.toBuilder()
                .id(3L)
                .name("Телевизор")
                .description("Телевизор 40 дюймов.")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(5)
    @SneakyThrows
    public void testGetItem_ReturnsStatusOk() {
        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(X_SHARER_USER_ID, userId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemOutDto)));
    }

    @Test
    @Order(6)
    @SneakyThrows
    public void testGetItem_WithUserIdNotOwner_ReturnsStatusOk() {
        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(X_SHARER_USER_ID, userId2)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemOutDto)));
    }

    @Test
    @Order(7)
    @SneakyThrows
    public void testGetItem_WithInvalidItemId_ReturnsStatusNotFound() {
        mvc.perform(get("/items/{itemId}", invalidId)
                        .header(X_SHARER_USER_ID, userId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(8)
    @SneakyThrows
    public void testGetItem_WithInvalidUserId_ReturnsStatusNotFound() {
        mvc.perform(get("/items/{itemId}", 546)
                        .header(X_SHARER_USER_ID, invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(9)
    @SneakyThrows
    public void testGetAllItems_WithOwnerId_ReturnsStatusOk() {
        ItemOutDto itemOutputDTO1 = ItemOutDto.builder()
                .id(2L)
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();

        ItemOutDto itemOutputDTO2 = ItemOutDto.builder()
                .id(3L)
                .name("Телевизор")
                .description("Телевизор 40 дюймов.")
                .available(true)
                .build();
        List<ItemOutDto> items = Arrays.asList(itemOutputDTO1, itemOutputDTO2);

        mvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(items)));
    }

    @Test
    @Order(10)
    @SneakyThrows
    public void testSearchAllItems_WithText_ReturnsStatusOk() {
        ItemOutDto itemOutputDTO = ItemOutDto.builder()
                .id(2L)
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();
        String text = "отвер";

        List<ItemOutDto> items = Collections.singletonList(itemOutputDTO);

        mvc.perform(get("/items/search")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(12)
    @SneakyThrows
    public void testUpdateItem_OnlyName_ReturnsStatusOk() {
        ItemDto itemInputDTO = ItemDto.builder()
                .id(itemId1)
                .name("Дрель++")
                .build();
        ItemOutDto itemShortOutputDTO = ItemOutDto.builder()
                .id(itemId1)
                .name("Дрель++")
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(13)
    @SneakyThrows
    public void testUpdateItem_OnlyDescription_ReturnsStatusOk() {
        ItemDto itemInputDTO = ItemDto.builder()
                .id(itemId1)
                .description("Простая дрель++")
                .build();
        ItemOutDto itemShortOutputDTO = ItemOutDto.builder()
                .id(itemId1)
                .description("Простая дрель++")
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(14)
    @SneakyThrows
    public void testUpdateItem_InvalidRequestId_ReturnsStatusNotFound() {
        ItemDto itemInputDTO = ItemDto.builder()
                .id(itemId1)
                .requestId(invalidId)
                .build();

        mvc.perform(patch("/items/{itemId}", 5049)
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
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
    public void testAddComment_WithNotConfirmBooking_ReturnsStatusBadRequest() {
        CommentDto commentInputDTO = CommentDto.builder()
                .text("Add comment from user1")
                .build();

        mvc.perform(post("/items/{itemId}/comment", 2L)
                        .header(X_SHARER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(commentInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        BadRequestException.class));
    }

    @Test
    @Order(16)
    @SneakyThrows
    public void testCreate_WithItemRequestId_ReturnsStatusOk() {
        Request itemRequest = Request.builder().description("Нужен диван").requester(user2).build();
        requestRepository.save(itemRequest);
        ItemDto itemInputDTO = ItemDto.builder()
                .name("Диван")
                .description("Мягкий диван.")
                .available(true)
                .requestId(1L)
                .build();
        ItemOutDto itemOutputDTO = ItemOutDto.builder()
                .id(4L)
                .name("Диван")
                .description("Мягкий диван.")
                .available(true)
                .requestId(1L)
                .build();

        mvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(itemOutputDTO.getId()), Long.class))
                .andExpect(jsonPath("name", is(itemOutputDTO.getName())))
                .andExpect(jsonPath("description", is(itemOutputDTO.getDescription())))
                .andExpect(jsonPath("available", is(itemOutputDTO.getAvailable())));
    }

    @Test
    @Order(17)
    @SneakyThrows
    public void testUpdateItem_OnlyItemRequestId_ReturnsStatusOk() {
        ItemDto itemInputDTO = ItemDto.builder()
                .id(itemId1)
                .requestId(1L)
                .build();
        ItemOutDto itemShortOutputDTO = ItemOutDto.builder()
                .id(itemId1)
                .requestId(1L)
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(18)
    @SneakyThrows
    public void testUpdateItem_OnlyNameAndHaveItemRequestId_ReturnsStatusOk() {
        ItemDto itemInputDTO = ItemDto.builder()
                .id(itemId1)
                .name("Дрель!!!")
                .build();
        ItemOutDto itemShortOutputDTO = ItemOutDto.builder()
                .id(itemId1)
                .name("Дрель!!!")
                .requestId(1L)
                .available(true)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(X_SHARER_USER_ID, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Order(19)
    @SneakyThrows
    public void testUpdateItem_WithNotOwner_ReturnsStatusNotFound() {
        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(X_SHARER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(20)
    @SneakyThrows
    public void testAddComment_WithInvalidItemId_ReturnsStatusNotFound() {
        CommentDto commentInputDTO = CommentDto.builder()
                .text("Add comment from user1")
                .build();

        mvc.perform(post("/items/{itemId}/comment", invalidId)
                        .header(X_SHARER_USER_ID, userId2)
                        .content(mapper.writeValueAsString(commentInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(21)
    @SneakyThrows
    public void testSearchAllItems_WithEmptyText_ReturnsStatusOk() {
        String text = "";

        mvc.perform(get("/items/search")
                        .header(X_SHARER_USER_ID, userId2)
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }
}
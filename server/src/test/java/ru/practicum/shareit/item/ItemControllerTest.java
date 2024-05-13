package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShortOutDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;
    private ItemMapper itemMapper;
    private User user;
    private ItemDto itemDto;
    private ItemOutDto itemOutDto;
    private CommentDto commentDto;
    private CommentOutDto commentOutDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("name@name.ru")
                .build();

        BookingShortOutDto lastBooking = BookingShortOutDto.builder()
                .id(1L)
                .bookerId(1L)
                .start(LocalDateTime.of(2023, 1, 1, 23, 0, 0))
                .end(LocalDateTime.of(2023, 1, 2, 23, 0, 0))
                .build();

        BookingShortOutDto nextBooking = BookingShortOutDto.builder()
                .id(1L)
                .bookerId(1L)
                .start(LocalDateTime.of(3023, 1, 1, 23, 0, 0))
                .end(LocalDateTime.of(3023, 1, 2, 23, 0, 0))
                .build();

        commentDto = CommentDto.builder()
                .text("text")
                .build();

        commentOutDto = CommentOutDto.builder()
                .id(1L)
                .text("text")
                .itemId(1L)
                .authorName("name")
                .created(LocalDateTime.of(2024, 1, 2, 23, 0, 0))
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(1L)
                .ownerId(1L)
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
    }

    @SneakyThrows
    @Test
    void saveItemTest() {
        when(itemService.saveItem(itemDto, user.getId()))
                .thenReturn(itemOutDto);

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void findByIdTest() {
        long userId = 1L;
        long itemId = 1L;

        when(itemService.findById(userId, itemId))
                .thenReturn(itemOutDto);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemOutDto), result);
    }

    @SneakyThrows
    @Test
    void getAllItemsOwnersTest() {
        when(itemService.getAllItemsOwner(1L, 0, 20))
                .thenReturn(List.of(itemOutDto));

        String result = mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void searchByTest() {
        mockMvc.perform(get("/items/search")
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void searchByEmptyText() {
        when(itemService.searchBy("", 0, 10))
                .thenReturn(Collections.emptyList());

        String result = mockMvc.perform(get("/items/search?text=")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void addCommentTest() {
        when(itemService.addComment(user.getId(), itemOutDto.getId(), commentDto))
                .thenReturn(commentOutDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(X_SHARER_USER_ID, 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentOutDto), result);
    }
}
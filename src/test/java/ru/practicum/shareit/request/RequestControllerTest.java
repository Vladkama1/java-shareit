package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShortOutDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.constant.ItemConstants.X_SHARER_USER_ID;

@SpringBootTest
@AutoConfigureMockMvc
class RequestControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mockMvc;
    private ItemOutDto itemOutDto;
    private CommentOutDto commentOutDto;

    private RequestDto requestDto;


    @BeforeEach
    void onStart() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("name@name.ru")
                .build();
        String description = "description";
        LocalDateTime dateTime = LocalDateTime.of(2024, 4, 1, 23, 0, 0);

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
        commentOutDto = CommentOutDto.builder()
                .id(1L)
                .text("text")
                .itemId(1L)
                .authorName("name")
                .created(LocalDateTime.of(2024, 1, 2, 23, 0, 0))
                .build();


        itemOutDto = ItemOutDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(false)
                .requestId(1L)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(List.of(commentOutDto))
                .build();

        requestDto = RequestDto.builder()
                .id(1L)
                .description(description)
                .items(List.of(itemOutDto))
                .created(dateTime)
                .build();
    }

    @SneakyThrows
    @Test
    void saveRequestTest() {
        when(requestService.saveRequest(requestDto, 1L))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID, 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void findAllByRequestTest() {
        when(requestService.findAllByRequest(1L))
                .thenReturn(List.of(requestDto));

        String result = mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @SneakyThrows
    @Test
    void findByAllTest() {
        long userId = 1L;
        when(requestService.findByAll(userId, 0, 20))
                .thenReturn(List.of(requestDto));
        String result = mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @SneakyThrows
    @Test
    void findByIdTest() {
        long userId = 1L;
        long itemRequestId = 1L;

        when(requestService.findById(userId, itemRequestId))
                .thenReturn(requestDto);

        String result = mockMvc.perform(get("/requests/{requestId}", itemRequestId)
                        .header(X_SHARER_USER_ID, 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }
}
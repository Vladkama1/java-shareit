package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RequestMapper mapper;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    private Request request;
    private RequestDto requestDto;
    private final Long userId = 1L;
    private final Long itemRequestId = 1L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {

        request = Request.builder()
                .id(itemRequestId)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now)
                .requester(User.builder()
                        .id(userId)
                        .name("RuRu")
                        .email("RuRu@yandex.ru")
                        .build())
                .build();


        requestDto = RequestDto.builder()
                .id(itemRequestId)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now)
                .items(new ArrayList<>())
                .build();
    }

    @Test
    @SneakyThrows
    void testCreateItemRequest_ReturnItemRequestOutputDTO() {
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(mapper.toDTO(any(Request.class))).thenReturn(requestDto);

        RequestDto result = requestService.saveRequest(requestDto, userId);

        assertEquals(requestDto.getId(), result.getId());
        assertEquals(requestDto.getDescription(), result.getDescription());
        assertEquals(requestDto.getCreated(), result.getCreated());
    }

    @Test
    @SneakyThrows
    void testCreateItemRequest_WithInvalidUserId_ReturnNotFoundException() {
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> requestService.saveRequest(requestDto, invalidId));
        assertEquals("Пользователь " + invalidId + " не найден", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void whenSaveRequest_thenReturnsRequestDto() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(mapper.toDTO(any(Request.class))).thenReturn(requestDto);

        RequestDto result = requestService.saveRequest(requestDto, userId);

        assertEquals(requestDto, result);
    }

    @Test
    @SneakyThrows
    void whenFindAllByRequest_thenReturnsListOfRequestDto() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByRequesterOrderByCreatedDesc(any(User.class)))
                .thenReturn(List.of(request));
        when(mapper.toDTO(any(Request.class))).thenReturn(requestDto);

        List<RequestDto> result = requestService.findAllByRequest(userId);

        assertEquals(List.of(requestDto), result);
    }

    @Test
    @SneakyThrows
    void whenFindByAll_thenReturnsListOfRequestDto() {
        Pageable pageable = PageRequest.of(from / size, size);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findAllByRequesterNotOrderByCreatedDesc(any(User.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(request)));
        when(mapper.toDTO(any(Request.class))).thenReturn(requestDto);

        List<RequestDto> result = requestService.findByAll(userId, from, size);

        assertEquals(List.of(requestDto), result);
    }

    @Test
    @SneakyThrows
    void whenFindById_thenReturnsRequestDto() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findById(itemRequestId)).thenReturn(Optional.of(request));
        when(mapper.toDTO(any(Request.class))).thenReturn(requestDto);

        RequestDto result = requestService.findById(userId, itemRequestId);

        assertEquals(requestDto, result);
    }
}
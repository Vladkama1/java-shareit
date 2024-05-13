package ru.practicum.shareit.item.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;
    private CommentDto commentDto;
    private CommentOutDto commentOutDto;
    private LocalDateTime now = LocalDateTime.now();

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemMapper itemMapper;

    @Test
    void whenSaveItem_thenReturnsItemOutDto() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto();
        Item item = new Item();
        ItemOutDto expectedItemOutDto = new ItemOutDto();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemMapper.toModel(any(ItemDto.class))).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(itemMapper.toOutDTO(item)).thenReturn(expectedItemOutDto);

        ItemOutDto result = itemService.saveItem(itemDto, userId);

        assertEquals(expectedItemOutDto, result);
    }

    @Test
    @SneakyThrows
    void whenUpdateItem_thenReturnsUpdatedItemOutDto() {
        Long userId = 1L;
        Long itemId = 1L;
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Name");
        Item item = new Item();
        item.setOwner(new User(userId, "Owner", "owner@example.com"));
        ItemOutDto expectedItemOutDto = new ItemOutDto();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemMapper.toOutDTO(any(Item.class))).thenReturn(expectedItemOutDto);

        ItemOutDto result = itemService.updateItem(userId, itemId, itemDto);

        assertEquals(expectedItemOutDto, result);
    }

    @Test
    @SneakyThrows
    void whenSearchByWithNonEmptyText_thenReturnsListOfItemDto() {
        String text = "some search text";
        Integer from = 0;
        Integer size = 20;
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> items = List.of(new Item());

        when(itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrue(text.toLowerCase(), pageable))
                .thenReturn(new PageImpl<>(items));
        List<ItemDto> expectedList = items.stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());

        List<ItemDto> result = itemService.searchBy(text, from, size);

        assertEquals(expectedList, result);
    }

    @Test
    @SneakyThrows
    void whenSearchByWithEmptyText_thenReturnsEmptyList() {
        String text = "";
        Integer from = 0;
        Integer size = 10;

        List<ItemDto> result = itemService.searchBy(text, from, size);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenUserExists_thenNoExceptionThrown() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> userRepository.existsById(userId));
    }
}
package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    private ItemMapper itemMapper;

    @BeforeEach
    void setup() {
        itemMapper = Mappers.getMapper(ItemMapper.class);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(itemMapper.updateToModel(null));
        assertNull(itemMapper.toDTO(null));
        assertNull(itemMapper.toOutDTO(null));
        assertNull(itemMapper.toListOutDTO(null));
    }

    @Test
    void shouldMapItemDtoToItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setOwnerId(2L);
        itemDto.setRequestId(3L);
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);

        Item item = itemMapper.toModel(itemDto);

        assertNotNull(item);
        assertEquals(itemDto.getId(), item.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());
        assertNotNull(item.getOwner());
        assertEquals(itemDto.getOwnerId(), item.getOwner().getId());
        assertNotNull(item.getRequest());
        assertEquals(itemDto.getRequestId(), item.getRequest().getId());
    }

    @Test
    void shouldMapItemToItemDto() {
        User owner = new User();
        owner.setId(2L);

        Request request = new Request();
        request.setId(3L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);
        item.setRequest(request);
        item.setName("Drill");
        item.setDescription("Powerful drill");
        item.setAvailable(true);

        ItemDto itemDto = itemMapper.toDTO(item);

        assertNotNull(itemDto);
        assertEquals(item.getId(), itemDto.getId());
        assertEquals(item.getName(), itemDto.getName());
        assertEquals(item.getDescription(), itemDto.getDescription());
        assertEquals(item.getAvailable(), itemDto.getAvailable());
        assertEquals(owner.getId(), itemDto.getOwnerId());
        assertEquals(request.getId(), itemDto.getRequestId());
    }

    @Test
    void shouldMapItemToItemOutDto() {
        Request request = new Request();
        request.setId(3L);

        Item item = new Item();
        item.setId(1L);
        item.setRequest(request);
        item.setName("Drill");
        item.setDescription("Powerful drill");
        item.setAvailable(true);

        ItemOutDto itemOutDto = itemMapper.toOutDTO(item);

        assertNotNull(itemOutDto);
        assertEquals(item.getId(), itemOutDto.getId());
        assertEquals(item.getName(), itemOutDto.getName());
        assertEquals(item.getDescription(), itemOutDto.getDescription());
        assertEquals(item.getAvailable(), itemOutDto.getAvailable());
        assertEquals(request.getId(), itemOutDto.getRequestId());
    }

    @Test
    void shouldMapItemListToItemOutDtoList() {
        Request request = new Request();
        request.setId(3L);

        Item item1 = new Item();
        item1.setId(1L);
        item1.setRequest(request);
        item1.setName("Drill");
        item1.setDescription("Powerful drill");
        item1.setAvailable(true);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setRequest(request);
        item2.setName("Screwdriver");
        item2.setDescription("Electric screwdriver");
        item2.setAvailable(false);

        List<Item> items = Arrays.asList(item1, item2);

        List<ItemOutDto> itemOutDtos = itemMapper.toListOutDTO(items);

        assertNotNull(itemOutDtos);
        assertEquals(2, itemOutDtos.size());
        assertEquals(items.get(0).getId(), itemOutDtos.get(0).getId());
        assertEquals(items.get(1).getId(), itemOutDtos.get(1).getId());
        assertEquals(items.get(0).getName(), itemOutDtos.get(0).getName());
        assertEquals(items.get(1).getName(), itemOutDtos.get(1).getName());
        assertEquals(items.get(0).getDescription(), itemOutDtos.get(0).getDescription());
        assertEquals(items.get(1).getDescription(), itemOutDtos.get(1).getDescription());
        assertEquals(items.get(0).getAvailable(), itemOutDtos.get(0).getAvailable());
        assertEquals(items.get(1).getAvailable(), itemOutDtos.get(1).getAvailable());
        assertEquals(request.getId(), itemOutDtos.get(0).getRequestId());
        assertEquals(request.getId(), itemOutDtos.get(1).getRequestId());
    }
}
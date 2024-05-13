package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {
    private RequestMapper requestMapper;

    @BeforeEach
    void setup() {
        requestMapper = Mappers.getMapper(RequestMapper.class);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(requestMapper.toDTO(null));
    }

    @Test
    void shouldMapRequestToRequestDTO() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Need a drill");
        request.setCreated(LocalDateTime.now());

        RequestDto requestDTO = requestMapper.toDTO(request);

        assertNotNull(requestDTO);
        assertEquals(request.getId(), requestDTO.getId());
        assertEquals(request.getDescription(), requestDTO.getDescription());
        assertEquals(request.getCreated(), requestDTO.getCreated());
    }
}
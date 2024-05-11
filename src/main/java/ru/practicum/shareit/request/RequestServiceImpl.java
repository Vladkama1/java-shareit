package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public RequestDto saveRequest(RequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + userId + " не найден"));
        Request request = new Request();
        request.setDescription(requestDto.getDescription());
        request.setRequester(user);
        return requestMapper.toDTO(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> findAllByRequest(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + userId + " не найден"));
        List<Request> requestList = requestRepository.findAllByRequesterOrderByCreatedDesc(user);
        return getRequestDtoListSetItem(requestList);
    }

    @Override
    public List<RequestDto> findByAll(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + userId + " не найден"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<Request> requestList = requestRepository.findAllByRequesterNotOrderByCreatedDesc(user, pageable).getContent();
        return getRequestDtoListSetItem(requestList);
    }

    @Override
    public RequestDto findById(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + userId + " не найден"));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос " + requestId + " не найден"));
        List<Request> requestList = new ArrayList<>();
        requestList.add(request);
        return getRequestDtoListSetItem(requestList).get(0);
    }

    private List<RequestDto> getRequestDtoListSetItem(List<Request> requestList) {
        List<Item> itemList = itemRepository.findAllByRequestIn(requestList);
        return requestList.stream()
                .map(requestMapper::toDTO)
                .peek(requestDto -> requestDto.setItems(itemMapper.toListOutDTO(itemList.stream()
                        .filter(item -> item.getRequest().getId().equals(requestDto.getId()))
                        .collect(Collectors.toList()))))
                .collect(Collectors.toList());
    }
}
